package at.rueckgr.spotlight.model

data class DownloadableImage (val id: String, val url: String, val description: String, val hash: String, val downloadUrl: DownloadUrl)
