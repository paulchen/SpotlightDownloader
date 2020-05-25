package at.rueckgr.spotlight.model;

import java.util.Locale;

public class DownloadUrl {
    private final String url;
    private final Locale locale;

    public DownloadUrl(String url, Locale locale) {
        this.url = url;
        this.locale = locale;
    }

    public String getUrl() {
        return this.url;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DownloadUrl)) return false;
        final DownloadUrl other = (DownloadUrl) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$url = this.getUrl();
        final Object other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) return false;
        final Object this$locale = this.getLocale();
        final Object other$locale = other.getLocale();
        if (this$locale == null ? other$locale != null : !this$locale.equals(other$locale)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DownloadUrl;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $url = this.getUrl();
        result = result * PRIME + ($url == null ? 43 : $url.hashCode());
        final Object $locale = this.getLocale();
        result = result * PRIME + ($locale == null ? 43 : $locale.hashCode());
        return result;
    }

    public String toString() {
        return "DownloadUrl(url=" + this.getUrl() + ", locale=" + this.getLocale() + ")";
    }
}
