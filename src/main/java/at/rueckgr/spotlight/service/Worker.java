package at.rueckgr.spotlight.service;

import at.rueckgr.spotlight.model.DownloadableImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RequiredArgsConstructor
public class Worker implements Runnable {

    private final ImageDataDownloaderService imageDataDownloaderService;
    private final DownloadDirectoryService downloadDirectoryService;

    public void run() {
        final DownloadableImage downloadableImage = imageDataDownloaderService.fetchImageData();
        if (downloadableImage == null) {
            log.info("No image found, aborting");
            return;
        }

        downloadDirectoryService.saveImageIfNotExists(downloadableImage);
    }
}

