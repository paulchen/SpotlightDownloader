package at.rueckgr.spotlight.service;

import at.rueckgr.spotlight.model.DownloadableImage;
import org.apache.logging.log4j.Logger;


public class Worker implements Runnable {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(Worker.class);
    private final ImageDataDownloaderService imageDataDownloaderService;
    private final DownloadDirectoryService downloadDirectoryService;

    public Worker(ImageDataDownloaderService imageDataDownloaderService, DownloadDirectoryService downloadDirectoryService) {
        this.imageDataDownloaderService = imageDataDownloaderService;
        this.downloadDirectoryService = downloadDirectoryService;
    }

    public void run() {
        final DownloadableImage downloadableImage = imageDataDownloaderService.fetchImageData();
        if (downloadableImage == null) {
            log.info("No image found, aborting");
            return;
        }

        downloadDirectoryService.saveImageIfNotExists(downloadableImage);
    }
}

