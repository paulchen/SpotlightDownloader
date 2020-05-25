package at.rueckgr.spotlight.service

import at.rueckgr.spotlight.model.DownloadableImage
import at.rueckgr.spotlight.util.Logging
import at.rueckgr.spotlight.util.logger

class Worker (private val imageDataDownloaderService: ImageDataDownloaderService,
              private val downloadDirectoryService: DownloadDirectoryService): Logging {
    fun run() {
        val downloadableImage: DownloadableImage? = imageDataDownloaderService.fetchImageData()
        if (downloadableImage == null) {
            logger().info("o image found, aborting")
            return
        }

        downloadDirectoryService.saveImageIfNotExists(downloadableImage)
    }
}
