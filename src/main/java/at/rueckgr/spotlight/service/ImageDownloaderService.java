package at.rueckgr.spotlight.service;

import at.rueckgr.spotlight.model.DownloadableImage;
import at.rueckgr.spotlight.util.SpotlightException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class ImageDownloaderService {
    public byte[] fetchImage(final DownloadableImage downloadableImage) {
        final CloseableHttpClient httpClient = HttpClients.custom().disableDefaultUserAgent().build();

        final HttpGet httpGet = new HttpGet(downloadableImage.getUrl());
        try (final CloseableHttpResponse response = httpClient.execute(httpGet)) {
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new SpotlightException(String.format("Fail to retrive image content: %s", statusCode));
            }
            HttpEntity entity = response.getEntity();
            return IOUtils.toByteArray(entity.getContent());
        }
        catch (IOException e) {
            throw new SpotlightException(e);
        }
    }
}
