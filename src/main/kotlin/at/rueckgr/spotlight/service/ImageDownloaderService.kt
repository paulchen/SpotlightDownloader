package at.rueckgr.spotlight.service

import at.rueckgr.spotlight.model.DownloadableImage
import at.rueckgr.spotlight.util.SpotlightException
import org.apache.commons.io.IOUtils
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.HttpClients

class ImageDownloaderService {
    fun fetchImage(downloadableImage: DownloadableImage): ByteArray {
        val httpClient = HttpClients.custom().disableDefaultUserAgent().build()

        httpClient.execute(HttpGet(downloadableImage.url)).use {
            val statusCode = it.code
            if (statusCode != 200) {
                throw SpotlightException(String.format("Fail to retrieve image content: %s", statusCode))
            }
            return IOUtils.toByteArray(it.entity.content)
        }
    }
}
