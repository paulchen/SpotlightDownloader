package at.rueckgr.spotlight.service;

import com.neovisionaries.i18n.LocaleCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

public class LocalesService {
    private final List<Locale> locales;

    public LocalesService() {
        locales = Arrays.stream(LocaleCode.values())
                .map(LocaleCode::toLocale)
                .filter(locale -> StringUtils.isNoneBlank(locale.getCountry(), locale.getLanguage()))
                .collect(Collectors.toList());
    }

    public Locale getRandomLocale() {
        return locales.get(new Random().nextInt(locales.size()));
    }

    public Locale getLocale(final String country, final String language) {
        for (final Locale locale : locales) {
            if (StringUtils.equals(country, locale.getCountry()) && StringUtils.equals(language, locale.getLanguage())) {
                return locale;
            }
        }
        return null;
    }
}
