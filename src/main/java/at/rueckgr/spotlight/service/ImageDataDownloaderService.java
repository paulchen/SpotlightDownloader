package at.rueckgr.spotlight.service;

import at.rueckgr.spotlight.model.DownloadUrl;
import at.rueckgr.spotlight.model.DownloadableImage;
import at.rueckgr.spotlight.util.SpotlightException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Log4j2
@RequiredArgsConstructor
public class ImageDataDownloaderService {
    private static final String URL = "https://arc.msn.com/v3/Delivery/Cache?pid=%s&ctry=%s&lc=%s&fmt=json&lo=%s&disphorzres=9999&dispvertres=9999";
    private static final List<String> PID_LIST = Arrays.asList("209567", "279978", "209562");

    private final LocalesService localesService;

    private DownloadUrl getDownloadUrl() {
        final Random random = new Random();

        final String pid = PID_LIST.get(random.nextInt(PID_LIST.size()));
        final int lo = random.nextInt(900000) + 100000;

        final Locale locale = localesService.getRandomLocale();
        final String country = locale.getCountry().toLowerCase();
        final String language = locale.getLanguage().toLowerCase();

        final String url = String.format(URL, pid, country, language, lo);
        return new DownloadUrl(url, locale);
    }

    public DownloadableImage fetchImageData() {
        final CloseableHttpClient httpClient = HttpClients.custom().disableDefaultUserAgent().build();
        final ObjectMapper objectMapper = new ObjectMapper();

        final DownloadUrl downloadUrl = getDownloadUrl();
        log.info("Download URL: {}", downloadUrl);
        final HttpGet httpGet = new HttpGet(downloadUrl.getUrl());
        httpGet.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        httpGet.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");

        try (final CloseableHttpResponse response = httpClient.execute(httpGet)) {
            final HttpEntity entity = response.getEntity();
            final String body = IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new SpotlightException(String.format("Status Code Error: %s", statusCode));
            }

            final JsonNode batchrsp = objectMapper.readTree(body).get("batchrsp");
            final JsonNode errors = batchrsp.get("errors");
            if (errors != null) {
                int errorCode = errors.get(0).get("code").asInt();
                if (errorCode == 2040 || errorCode == 2000) {
                    return null;
                }
                throw new SpotlightException(String.format("Error in response: %s", body));
            }

            final String item = objectMapper.readTree(body).get("batchrsp").get("items").get(0).get("item").asText();
            final JsonNode jsonNode = objectMapper.readTree(item);

            final JsonNode imageNode = jsonNode.get("ad").get("image_fullscreen_001_landscape");
            final String hash = Hex.encodeHexString(Base64.getDecoder().decode(imageNode.get("sha256").asText()));
            final String imageUrl = imageNode.get("u").asText();

            final String imageId;
            if (imageUrl.lastIndexOf("?") != -1) {
                imageId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("?"));
            } else {
                imageId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            }

            final JsonNode descriptionNode = jsonNode.get("ad").get("title_text");
            final String imageDescription;
            if (descriptionNode != null) {
                imageDescription = descriptionNode.get("tx").asText();
            }
            else {
                imageDescription = imageId;
            }

            final DownloadableImage downloadableImage = new DownloadableImage(imageId, imageUrl, normalizeDescription(imageDescription), hash, downloadUrl);
            log.info("Downloaded image data: {}", downloadableImage.toString());
            return downloadableImage;
        }
        catch (IOException e) {
            throw new SpotlightException(e);
        }
    }

    private String normalizeDescription(String description) {
        return Normalizer
                .normalize(description, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
