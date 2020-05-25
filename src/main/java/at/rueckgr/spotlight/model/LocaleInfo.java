package at.rueckgr.spotlight.model;

public class LocaleInfo {
    private String country;
    private String language;

    public LocaleInfo(String country, String language) {
        this.country = country;
        this.language = language;
    }

    public String getCountry() {
        return this.country;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LocaleInfo)) return false;
        final LocaleInfo other = (LocaleInfo) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$country = this.getCountry();
        final Object other$country = other.getCountry();
        if (this$country == null ? other$country != null : !this$country.equals(other$country)) return false;
        final Object this$language = this.getLanguage();
        final Object other$language = other.getLanguage();
        if (this$language == null ? other$language != null : !this$language.equals(other$language)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LocaleInfo;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $country = this.getCountry();
        result = result * PRIME + ($country == null ? 43 : $country.hashCode());
        final Object $language = this.getLanguage();
        result = result * PRIME + ($language == null ? 43 : $language.hashCode());
        return result;
    }

    public String toString() {
        return "LocaleInfo(country=" + this.getCountry() + ", language=" + this.getLanguage() + ")";
    }
}
