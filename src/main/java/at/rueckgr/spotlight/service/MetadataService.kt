package at.rueckgr.spotlight.service

import at.rueckgr.spotlight.model.LocaleInfo
import at.rueckgr.spotlight.model.MetadataFile
import at.rueckgr.spotlight.model.MetadataInfo
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.collections.HashMap

class MetadataService (val localesService: LocalesService) {
    private val metadataFilename = ".meta"
    private val fileLocales = HashMap<String, Locale>()
    var downloadDirectoryService: DownloadDirectoryService? = null

    fun loadMetadata() {
        val metadataFile = File(downloadDirectoryService!!.folderName + File.separator + metadataFilename)
        if (metadataFile.exists()) {
            val metadata: MetadataFile = Gson().fromJson(FileReader(metadataFile), MetadataFile::class.java)
            for(downloadedFile in metadata.downloadedFiles) {
                val country = downloadedFile.locale.country
                val language = downloadedFile.locale.language

                val locale = localesService.getLocale(country, language)
                if (locale != null) {
                    fileLocales[downloadedFile.hash] = locale
                }
            }
        }
    }

    fun saveMetadata() {
        val downloadedFiles = downloadDirectoryService!!.downloadedFiles.values
                .filter { it.locale != null }
                .map { MetadataInfo(it.hash, it.name, LocaleInfo(it.locale.country, it.locale.language)) }
        val metadata = MetadataFile(downloadedFiles)

        val metadataFile = File(downloadDirectoryService!!.folderName + File.separator + metadataFilename)
        FileWriter(metadataFile).use { Gson().toJson(metadata, it) }
    }

    fun getLocale(hash: String): Locale? {
        return fileLocales[hash]
    }
}
