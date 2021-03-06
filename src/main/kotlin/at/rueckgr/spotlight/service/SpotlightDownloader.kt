package at.rueckgr.spotlight.service

import org.apache.logging.log4j.LogManager
import kotlin.system.exitProcess

fun main() {
    try {
        val log = LogManager.getLogger("Main")

        val iterations = 100

        val localesService = LocalesService()
        val metricsService = MetricsService()
        val metadataService = MetadataService(localesService)
        val downloadDirectoryService = DownloadDirectoryService(metadataService, metricsService)
        metadataService.downloadDirectoryService = downloadDirectoryService
        val imageDataDownloaderService = ImageDataDownloaderService(localesService, metricsService)

        downloadDirectoryService.init()
        val worker = Worker(imageDataDownloaderService, downloadDirectoryService)
        log.info("Starting download")
        for (i in 1..iterations) {
            worker.run()
            log.info("Iteration {}/{} done", i, iterations)
        }

        metadataService.saveMetadata()
        metricsService.logMetrics()
    }
    catch (e: Throwable) {
        exitProcess(1)
    }
}
