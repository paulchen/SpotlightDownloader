package at.rueckgr.spotlight.service

import at.rueckgr.spotlight.model.DownloadableImage
import at.rueckgr.spotlight.util.Logging
import at.rueckgr.spotlight.util.SpotlightException
import at.rueckgr.spotlight.util.logger

class Worker (private val imageDataDownloaderService: ImageDataDownloaderService,
              private val downloadDirectoryService: DownloadDirectoryService): Logging {
    fun run() {
        val downloadableImage: DownloadableImage? = imageDataDownloaderService.fetchImageData()
        if (downloadableImage == null) {
            logger().info("No image found, aborting")
            return
        }

        try {
            downloadDirectoryService.saveImageIfNotExists(downloadableImage)
        }
        catch (e: SpotlightException) {
            logger().error("Failed to save image", e)
        }
    }
}
