package at.rueckgr.spotlight.service

import com.neovisionaries.i18n.LocaleCode
import java.util.*

class LocalesService {
    val locales: List<Locale> = LocaleCode.values()
            .map { it.toLocale() }
            .filter { it.country.isNotBlank() && it.language.isNotBlank() }

    fun getRandomLocale(): Locale {
        return locales.random()
    }

    fun getLocale(country: String, language: String): Locale? {
        return locales.find { it.country == country && it.language == language }
    }
}
