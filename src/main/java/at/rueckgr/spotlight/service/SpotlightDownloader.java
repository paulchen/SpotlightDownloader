package at.rueckgr.spotlight.service;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpotlightDownloader {
    private final DownloadDirectoryService downloadDirectoryService;
    private final ImageDataDownloaderService imageDataDownloaderService;
    private final MetadataService metadataService;
    public static final int ITERATIONS = 100;

    public SpotlightDownloader() {
        // using Dependency Injection, life would be easier
        final LocalesService localesService = new LocalesService();
        metadataService = new MetadataService(localesService);
        downloadDirectoryService = new DownloadDirectoryService(metadataService);
        metadataService.setDownloadDirectoryService(downloadDirectoryService);
        imageDataDownloaderService = new ImageDataDownloaderService(localesService);
    }

    public void run() {
        downloadDirectoryService.init();

        final Worker worker = new Worker(imageDataDownloaderService, downloadDirectoryService);
        for(int i = 0; i < ITERATIONS; i++) {
            log.info("Iteration {}/{} done", i, ITERATIONS);
            worker.run();
        }

        metadataService.saveMetadata();
    }
}
