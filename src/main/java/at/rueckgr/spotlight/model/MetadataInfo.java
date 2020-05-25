package at.rueckgr.spotlight.model;

public class MetadataInfo {
    private String hash;
    private String filename;
    private LocaleInfo locale;

    public MetadataInfo(String hash, String filename, LocaleInfo locale) {
        this.hash = hash;
        this.filename = filename;
        this.locale = locale;
    }

    public String getHash() {
        return this.hash;
    }

    public String getFilename() {
        return this.filename;
    }

    public LocaleInfo getLocale() {
        return this.locale;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setLocale(LocaleInfo locale) {
        this.locale = locale;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MetadataInfo)) return false;
        final MetadataInfo other = (MetadataInfo) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$hash = this.getHash();
        final Object other$hash = other.getHash();
        if (this$hash == null ? other$hash != null : !this$hash.equals(other$hash)) return false;
        final Object this$filename = this.getFilename();
        final Object other$filename = other.getFilename();
        if (this$filename == null ? other$filename != null : !this$filename.equals(other$filename)) return false;
        final Object this$locale = this.getLocale();
        final Object other$locale = other.getLocale();
        if (this$locale == null ? other$locale != null : !this$locale.equals(other$locale)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MetadataInfo;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $hash = this.getHash();
        result = result * PRIME + ($hash == null ? 43 : $hash.hashCode());
        final Object $filename = this.getFilename();
        result = result * PRIME + ($filename == null ? 43 : $filename.hashCode());
        final Object $locale = this.getLocale();
        result = result * PRIME + ($locale == null ? 43 : $locale.hashCode());
        return result;
    }

    public String toString() {
        return "MetadataInfo(hash=" + this.getHash() + ", filename=" + this.getFilename() + ", locale=" + this.getLocale() + ")";
    }
}
