package at.rueckgr.spotlight.service

import org.apache.logging.log4j.LogManager

fun main() {
    val log = LogManager.getLogger("Main")

    val iterations = 1

    val localesService = LocalesService()
    val metricsService = MetricsService()
    val metadataService = MetadataService(localesService)
    val downloadDirectoryService = DownloadDirectoryService(metadataService, metricsService)
    metadataService.setDownloadDirectoryService(downloadDirectoryService)
    val imageDataDownloaderService = ImageDataDownloaderService(localesService, metricsService)

    downloadDirectoryService.init()
    val worker = Worker(imageDataDownloaderService, downloadDirectoryService)
    for(i in 0..iterations) {
        log.info("Iteration {}/{} done", i, iterations)
        worker.run()
    }
    log.info("All iterations done")

    metadataService.saveMetadata()
    metricsService.logMetrics()
}
