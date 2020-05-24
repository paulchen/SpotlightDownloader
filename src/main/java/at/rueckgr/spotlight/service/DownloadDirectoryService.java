package at.rueckgr.spotlight.service;

import at.rueckgr.spotlight.model.DownloadableImage;
import at.rueckgr.spotlight.model.DownloadedImage;
import at.rueckgr.spotlight.util.SpotlightException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class DownloadDirectoryService {

    public static final String FOLDER_NAME = "images";
    public static final String PRETTY_FILENAME_PATTERN = "^((?!\\W|^R(.){5,6}$).|,| | |-|\\.|\\(|\\)|')*$";

    private static final List<String> preferredLanguages = Arrays.asList("en", "de");

    private final Map<String, DownloadedImage> downloadedImages = new HashMap<>();

    private final ImageDownloaderService imageDownloaderService = new ImageDownloaderService();
    private final MetadataService metadataService;

    public void init() {
        createDirectory();
        metadataService.loadMetadata();
        loadDownloadedImages();
    }

    private void createDirectory() {
        File directory = new File(FOLDER_NAME);
        if (!directory.exists()) {
            if(!directory.mkdir()) {
                throw new SpotlightException("Unable to create download directory");
            }
        }
    }

    public void loadDownloadedImages() {
        final File directory = new File(FOLDER_NAME);
        final File[] directoryListing = directory.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("jpg")) {
                    try {
                        final String hash = DigestUtils.sha256Hex(IOUtils.toByteArray(new FileInputStream(file)));
                        downloadedImages.put(hash, new DownloadedImage(file, hash, metadataService.getLocale(hash)));
                    }
                    catch (IOException e) {
                        throw new SpotlightException(e);
                    }
                }
            }
        }
    }

    public synchronized void saveImageIfNotExists(final DownloadableImage downloadableImage) {
        final String hash = downloadableImage.getHash();
        if (downloadedImages.containsKey(hash)) {
            log.info("File already exists: {}", downloadableImage.toString());
            log.info("Existing file: {}", downloadedImages.get(hash));
            renameImageIfRequired(downloadableImage);
            return;
        }

        byte[] newImageBytes = imageDownloaderService.fetchImage(downloadableImage);
        final File nextFile = getNextFile(downloadableImage.getDescription());
        try {
            Files.copy(new ByteArrayInputStream(newImageBytes), nextFile.toPath());
        }
        catch (IOException e) {
            throw new SpotlightException(e);
        }
        downloadedImages.put(hash, new DownloadedImage(nextFile, hash, downloadableImage.getDownloadUrl().getLocale()));

        log.info(String.format("New image added : %s", downloadableImage));
    }

    private void renameImageIfRequired(final DownloadableImage downloadableImage) {
        final String hash = downloadableImage.getHash();
        final DownloadedImage downloadedImage = downloadedImages.get(hash);

        final boolean oldFileNeedRename =  !FilenameUtils.removeExtension(downloadedImage.getName()).matches(PRETTY_FILENAME_PATTERN);
        final String newFilename = downloadableImage.getDescription();
        final boolean newFileIsValidName = newFilename.matches(PRETTY_FILENAME_PATTERN);

        final String oldLanguage = downloadedImage.getLocale().getLanguage();
        final String newLanguage = downloadableImage.getDownloadUrl().getLocale().getLanguage();
        final boolean betterLanguageFound = (!preferredLanguages.contains(oldLanguage) && preferredLanguages.contains(newLanguage)) ||
                (preferredLanguages.contains(oldLanguage) && preferredLanguages.contains(newLanguage) && preferredLanguages.indexOf(oldLanguage) < preferredLanguages.indexOf(newFilename));

        log.debug("oldFileNeedRename: {}, betterLanguageFound: {}, newFileIsValidName: {}", oldFileNeedRename, betterLanguageFound, newFileIsValidName);
        if ((oldFileNeedRename || betterLanguageFound) && newFileIsValidName) {
            final File newFile = getNextFile(newFilename);
            if (!downloadedImage.getFile().renameTo(newFile)) {
                throw new SpotlightException(String.format("Unable to rename file %s to %s", downloadedImage.getName(), newFile.getName()));
            }

            log.info("Renaming {} to {}", downloadedImage.getName(), newFile.getName());
            downloadedImages.put(hash, new DownloadedImage(newFile, hash, downloadableImage.getDownloadUrl().getLocale()));
        }
    }

    private File getNextFile(final String filename) {
        File file = new File(FOLDER_NAME + File.separator + filename + ".jpg");
        int fileNo = 0;
        while (file.exists()) {
            fileNo++;
            file = new File(FOLDER_NAME + File.separator + filename + fileNo + ".jpg");
        }
        return file;
    }

    public String getFolderName() {
        return FOLDER_NAME;
    }

    public Map<String, DownloadedImage> getDownloadedFiles() {
        return this.downloadedImages;
    }
}
