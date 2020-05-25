package at.rueckgr.spotlight.model;

import java.util.List;

public class MetadataFile {
    private List<MetadataInfo> downloadedFiles;

    public MetadataFile(List<MetadataInfo> downloadedFiles) {
        this.downloadedFiles = downloadedFiles;
    }

    public List<MetadataInfo> getDownloadedFiles() {
        return this.downloadedFiles;
    }

    public void setDownloadedFiles(List<MetadataInfo> downloadedFiles) {
        this.downloadedFiles = downloadedFiles;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MetadataFile)) return false;
        final MetadataFile other = (MetadataFile) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$downloadedFiles = this.getDownloadedFiles();
        final Object other$downloadedFiles = other.getDownloadedFiles();
        if (this$downloadedFiles == null ? other$downloadedFiles != null : !this$downloadedFiles.equals(other$downloadedFiles))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MetadataFile;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $downloadedFiles = this.getDownloadedFiles();
        result = result * PRIME + ($downloadedFiles == null ? 43 : $downloadedFiles.hashCode());
        return result;
    }

    public String toString() {
        return "MetadataFile(downloadedFiles=" + this.getDownloadedFiles() + ")";
    }
}
