package at.rueckgr.spotlight.model

import java.io.File
import java.util.*

data class DownloadedImage (val file: File, val hash: String, val locale: Locale?) {
    val name: String get() { return file.name }
}
