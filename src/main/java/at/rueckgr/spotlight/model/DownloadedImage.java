package at.rueckgr.spotlight.model;

import java.io.File;
import java.util.Locale;

public class DownloadedImage {
    private final File file;
    private final String hash;
    private final Locale locale;

    public DownloadedImage(File file, String hash, Locale locale) {
        this.file = file;
        this.hash = hash;
        this.locale = locale;
    }

    public String getName() {
        return file.getName();
    }

    public File getFile() {
        return this.file;
    }

    public String getHash() {
        return this.hash;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DownloadedImage)) return false;
        final DownloadedImage other = (DownloadedImage) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$file = this.getFile();
        final Object other$file = other.getFile();
        if (this$file == null ? other$file != null : !this$file.equals(other$file)) return false;
        final Object this$hash = this.getHash();
        final Object other$hash = other.getHash();
        if (this$hash == null ? other$hash != null : !this$hash.equals(other$hash)) return false;
        final Object this$locale = this.getLocale();
        final Object other$locale = other.getLocale();
        if (this$locale == null ? other$locale != null : !this$locale.equals(other$locale)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DownloadedImage;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $file = this.getFile();
        result = result * PRIME + ($file == null ? 43 : $file.hashCode());
        final Object $hash = this.getHash();
        result = result * PRIME + ($hash == null ? 43 : $hash.hashCode());
        final Object $locale = this.getLocale();
        result = result * PRIME + ($locale == null ? 43 : $locale.hashCode());
        return result;
    }

    public String toString() {
        return "DownloadedImage(file=" + this.getFile() + ", hash=" + this.getHash() + ", locale=" + this.getLocale() + ")";
    }
}
