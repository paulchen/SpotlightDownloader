package at.rueckgr.spotlight.model;

public class DownloadableImage {
    private final String id;
    private final String url;
    private final String description;
    private final String hash;
    private final DownloadUrl downloadUrl;

    public DownloadableImage(String id, String url, String description, String hash, DownloadUrl downloadUrl) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.hash = hash;
        this.downloadUrl = downloadUrl;
    }

    public String getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHash() {
        return this.hash;
    }

    public DownloadUrl getDownloadUrl() {
        return this.downloadUrl;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DownloadableImage)) return false;
        final DownloadableImage other = (DownloadableImage) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$url = this.getUrl();
        final Object other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$hash = this.getHash();
        final Object other$hash = other.getHash();
        if (this$hash == null ? other$hash != null : !this$hash.equals(other$hash)) return false;
        final Object this$downloadUrl = this.getDownloadUrl();
        final Object other$downloadUrl = other.getDownloadUrl();
        if (this$downloadUrl == null ? other$downloadUrl != null : !this$downloadUrl.equals(other$downloadUrl))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DownloadableImage;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $url = this.getUrl();
        result = result * PRIME + ($url == null ? 43 : $url.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $hash = this.getHash();
        result = result * PRIME + ($hash == null ? 43 : $hash.hashCode());
        final Object $downloadUrl = this.getDownloadUrl();
        result = result * PRIME + ($downloadUrl == null ? 43 : $downloadUrl.hashCode());
        return result;
    }

    public String toString() {
        return "DownloadableImage(id=" + this.getId() + ", url=" + this.getUrl() + ", description=" + this.getDescription() + ", hash=" + this.getHash() + ", downloadUrl=" + this.getDownloadUrl() + ")";
    }
}
