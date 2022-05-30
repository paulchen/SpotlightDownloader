package at.rueckgr.spotlight.service

import at.rueckgr.spotlight.model.DownloadUrl
import at.rueckgr.spotlight.model.DownloadableImage
import at.rueckgr.spotlight.util.Logging
import at.rueckgr.spotlight.util.SpotlightException
import at.rueckgr.spotlight.util.logger
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.binary.Hex
import org.apache.commons.io.IOUtils
import org.apache.http.HttpHeaders
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.nio.charset.StandardCharsets
import java.text.Normalizer
import java.util.*

class ImageDataDownloaderService (private val localesService: LocalesService, private val metricsService: MetricsService) : Logging {
    private val url = "https://arc.msn.com/v3/Delivery/Placement?pid=%s&cdm=1&ctry=%s&lc=%s&pl=%s&fmt=json&lo=%s&disphorzres=9999&dispvertres=9999"
    private val pidList = arrayOf("209567", "279978", "209562")

    private fun getDownloadUrl(): DownloadUrl {
        val locale =  localesService.getRandomLocale()
        val downloadUrl = String.format(url, pidList.random(), locale.country, locale.language, locale.language, IntRange(100000, 999999).random())
        return DownloadUrl(downloadUrl, locale)
    }

    fun fetchImageData(): DownloadableImage? {
        val httpClient = HttpClients.custom().disableDefaultUserAgent().build()
        val objectMapper = ObjectMapper()

        val downloadUrl = getDownloadUrl()
        logger().info("Download URL: {}", downloadUrl)
        val httpGet = HttpGet(downloadUrl.url)
        httpGet.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache")
        httpGet.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
        httpClient.execute(httpGet).use {
            metricsService.recordMetric(MetricsService.Metric.DOWNLOAD_IMAGE_DATA)
            if(it.statusLine.statusCode != 200) {
                throw SpotlightException(String.format("Status Code Error: %s", it.statusLine.statusCode))
            }

            val body = IOUtils.toString(it.entity.content, StandardCharsets.UTF_8)
            val batchrsp = objectMapper.readTree(body)["batchrsp"]
            if (batchrsp["errors"] != null) {
                val errorCode = batchrsp["errors"][0]["code"].asInt()
                if (errorCode == 2040 || errorCode == 2000) {
                    metricsService.recordMetric(MetricsService.Metric.NO_IMAGE_DATA)
                    return null
                }
                throw SpotlightException(String.format("Error in response: %s", body))
            }

            val item = batchrsp["items"][0]["item"].asText()
            val jsonNode = objectMapper.readTree(item)

            val imageNode = jsonNode["ad"]["image_fullscreen_001_landscape"]
            val hash = Hex.encodeHexString(Base64.getDecoder().decode(imageNode["sha256"].asText()))
            val imageUrl = imageNode["u"].asText()

            val imageId = if (imageUrl.lastIndexOf("?") != -1) {
                imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("?"))
            }
            else {
                imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
            }

            val descriptionNode = jsonNode["ad"]["title_text"]
            val imageDescription = descriptionNode?.get("tx")?.asText() ?: imageId

            val downloadableImage = DownloadableImage(imageId, imageUrl, normalizeDescription(imageDescription), hash, downloadUrl)
            logger().info("Downloaded image data: {}", downloadableImage)
            return downloadableImage
        }
    }

    private fun normalizeDescription(description: String): String {
        return Normalizer
                .normalize(description, Normalizer.Form.NFD)
                .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
                .trim()
    }
}
