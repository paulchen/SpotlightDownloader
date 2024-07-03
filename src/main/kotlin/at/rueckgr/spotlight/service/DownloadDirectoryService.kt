package at.rueckgr.spotlight.service

import at.rueckgr.spotlight.model.DownloadableImage
import at.rueckgr.spotlight.model.DownloadedImage
import at.rueckgr.spotlight.util.Logging
import at.rueckgr.spotlight.util.SpotlightException
import at.rueckgr.spotlight.util.logger
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files

class DownloadDirectoryService (private val metadataService: MetadataService, private val metricsService: MetricsService) : Logging {
    val folderName = "images"
    private val prettyFilenamePattern = "^((?!\\W|^R(.){5,6}$).|,| | |-|\\.|\\(|\\)|')*$".toRegex()
    private val preferredLanguages = arrayOf("en", "de")

    val downloadedImages = HashMap<String, DownloadedImage>()

    private val imageDownloaderService = ImageDownloaderService()

    fun init() {
        createDirectory()
        metadataService.loadMetadata()
        loadDownloadedImages()
    }

    private fun createDirectory() {
        val directory = File(folderName)
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw SpotlightException("Unable to create download directory")
            }
        }
    }

    private fun loadDownloadedImages() {
        val directory = File(folderName)
        val directoryListing = directory.listFiles()
        if (directoryListing != null) {
            for(file in directoryListing) {
                if (FilenameUtils.getExtension(file.name).equals("jpg", ignoreCase = true)) {
                    val hash = DigestUtils.sha256Hex(IOUtils.toByteArray(FileInputStream(file)))
                    downloadedImages[hash] = DownloadedImage(file, hash, metadataService.getLocale(hash))
                }
            }
        }
    }

    fun saveImageIfNotExists(downloadableImage: DownloadableImage) {
        val hash = downloadableImage.hash
        if (downloadedImages.containsKey(hash)) {
            logger().info("File already exists: {}", downloadableImage)
            logger().info("Existing file: {}", downloadedImages[hash])
            renameImageIfRequired(downloadableImage)
            return
        }

        val newImageBytes = imageDownloaderService.fetchImage(downloadableImage)
        val nextFile = getNextFile(downloadableImage.description)
        Files.copy(ByteArrayInputStream(newImageBytes), nextFile.toPath())
        downloadedImages[hash] = DownloadedImage(nextFile, hash, downloadableImage.downloadUrl.locale)

        logger().info("New image added: {}", downloadableImage)
        metricsService.recordMetric(MetricsService.Metric.DOWNLOAD_IMAGE)
    }

    private fun renameImageIfRequired(downloadableImage: DownloadableImage) {
        if (isRenameRequired(downloadableImage)) {
            val newFile = getNextFile(downloadableImage.description)
            val hash = downloadableImage.hash
            val downloadedImage = downloadedImages[hash]
            if (!downloadedImage!!.file.renameTo(newFile)) {
                throw SpotlightException(String.format("Unable to rename file %s to %s", downloadedImage.name, newFile.name))
            }

            logger().info("Renaming {} to {}", downloadedImage.name, newFile.name)
            downloadedImages[hash] = DownloadedImage(newFile, hash, downloadableImage.downloadUrl.locale)

            metricsService.recordMetric(MetricsService.Metric.RENAME_IMAGE)
        }
    }

    private fun isRenameRequired(downloadableImage: DownloadableImage): Boolean {
        val downloadedImage = downloadedImages[downloadableImage.hash]

        val oldFileNeedRename = !FilenameUtils.removeExtension(downloadedImage!!.name).matches(prettyFilenamePattern)
        val newFilename = downloadableImage.description
        val newFileIsValidName = newFilename.matches(prettyFilenamePattern)

        val oldLanguage = downloadedImage.locale?.language ?: ""
        val newLanguage = downloadableImage.downloadUrl.locale.language
        val betterLanguageFound = (oldLanguage !in preferredLanguages && newLanguage in preferredLanguages) ||
                (oldLanguage in preferredLanguages && preferredLanguages.contains(newLanguage) &&
                        preferredLanguages.indexOf(oldLanguage) < preferredLanguages.indexOf(newFilename))

        logger().debug("oldFileNeedRename: {}, betterLanguageFound: {}, newFileIsValidName: {}", oldFileNeedRename, betterLanguageFound, newFileIsValidName)

        return (oldFileNeedRename || betterLanguageFound) && newFileIsValidName
    }

    private fun getNextFile(filename: String): File {
        val cleanFilename = filename.replace("/", ", ")
        var file = File(folderName + File.separator + cleanFilename + ".jpg")
        var fileNo = 0
        while (file.exists()) {
            fileNo++
            file = File(folderName + File.separator + cleanFilename + fileNo + ".jpg")
        }
        return file
    }
}
