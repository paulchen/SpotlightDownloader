package at.rueckgr.spotlight.service

import org.apache.logging.log4j.LogManager
import kotlin.system.exitProcess

fun main() {
    val log = LogManager.getLogger("Main")

    try {
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

        if (metricsService.getMetric(MetricsService.Metric.DOWNLOAD_IMAGE) == 0) {
            log.error("No image was downloaded")
            exitProcess(1)
        }
    }
    catch (e: Throwable) {
        log.error("Exception occurred", e)
        exitProcess(1)
    }
}
