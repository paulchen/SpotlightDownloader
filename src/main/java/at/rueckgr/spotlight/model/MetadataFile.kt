package at.rueckgr.spotlight.model

data class MetadataFile (val downloadedFiles: List<MetadataInfo>)

data class MetadataInfo (val hash: String, val filename: String, val locale: LocaleInfo)

data class LocaleInfo (val country: String, val language: String)
