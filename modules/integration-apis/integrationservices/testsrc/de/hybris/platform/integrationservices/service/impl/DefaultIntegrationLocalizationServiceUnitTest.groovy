/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.integrationservices.exception.LocaleNotSupportedException
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
import de.hybris.platform.servicelayer.i18n.CommonI18NService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultIntegrationLocalizationServiceUnitTest extends Specification {
    def i18Service = Stub CommonI18NService
    def localizationService = new DefaultIntegrationLocalizationService(internationalizationService: i18Service)

    @Test
    def 'getSupportedLocale() returns locale if it is supported by the platform'() {
        given:
        def isoCode = 'en'
        i18Service.getLocaleForIsoCode(isoCode) >> Locale.ENGLISH
        i18Service.getLanguage(isoCode) >> language(isoCode)

        when:
        def locale = localizationService.getSupportedLocale isoCode

        then:
        locale.present
        locale.get() == Locale.ENGLISH
    }

    @Test
    def 'validateLocale does not throw an exception when language tag is supported by the platform'() {
        given:
        def isoCode = 'zz'
        def locale = Locale.forLanguageTag(isoCode)
        i18Service.getLocaleForIsoCode(isoCode) >> locale
        i18Service.getLanguage(isoCode) >> language(isoCode)

        when:
        localizationService.validateLocale locale

        then:
        noExceptionThrown()
    }

    @Test
    def 'getSupportedLocaleForLanguageTag returns a locale when language tag is supported by the platform'() {
        given:
        def isoCode = 'zz'
        def locale = Locale.forLanguageTag(isoCode)
        i18Service.getLocaleForIsoCode(isoCode) >> locale
        i18Service.getLanguage(isoCode) >> language(isoCode)

        expect:
        localizationService.getSupportedLocaleForLanguageTag(isoCode) == locale
    }

    @Test
    @Unroll
    def "getSupportedLocale() is empty when internationalization service throws #exception"() {
        given:
        def unsupportedIsoCode = 'ru'
        i18Service.getLocaleForIsoCode(unsupportedIsoCode) >> Locale.forLanguageTag(unsupportedIsoCode)
        i18Service.getLanguage(unsupportedIsoCode) >> { throw exception }

        expect:
        localizationService.getSupportedLocale(unsupportedIsoCode).empty

        where:
        exception << [new UnknownIdentifierException(''), new IllegalArgumentException()]
    }

    @Test
    @Unroll
    def 'validateLocale throws an exception when #msg language tag is not supported by the platform'() {
        given:
        def locale = Locale.forLanguageTag(languageTag)
        i18Service.getLocaleForIsoCode(languageTag) >> locale
        i18Service.getLanguage(languageTag) >> { throw exception }

        when:
        localizationService.validateLocale locale

        then:
        def e = thrown(LocaleNotSupportedException)
        with(e) {
            language == languageTag
            message ==  "The language provided [$languageTag] is not available in the system."
        }

        where:
        languageTag | exception                          | msg
        ''          | new UnknownIdentifierException('') | 'empty'
        'ss'        | new IllegalArgumentException()     | 'non-empty'
    }

    @Test
    @Unroll
    def 'getSupportedLocaleForLanguageTag throws an exception when language tag is not supported by the platform'() {
        given:
        def locale = Locale.forLanguageTag(languageTag)
        i18Service.getLocaleForIsoCode(languageTag) >> locale
        i18Service.getLanguage(languageTag) >> { throw exception }

        when:
        localizationService.getSupportedLocaleForLanguageTag languageTag

        then:
        def e = thrown(LocaleNotSupportedException)
        with(e) {
            language == languageTag
            message ==  "The language provided [$languageTag] is not available in the system."
        }

        where:
        languageTag | exception                          | msg
        ''          | new UnknownIdentifierException('') | 'empty'
        'ss'        | new IllegalArgumentException()     | 'non-empty'
    }

    @Test
    def 'getAllSupportedLocales() returns locales from the internationalization service'() {
        given:
        def lang = language('fr')
        i18Service.getAllLanguages() >> [lang]
        i18Service.getLocaleForLanguage(lang) >> Locale.FRENCH

        expect:
        localizationService.getAllSupportedLocales() == [Locale.FRENCH]
    }

    @Test
    @Unroll
    def "getDefaultLocale() is #loc when internationalization service returns #lang"() {
        given:
        i18Service.getCurrentLanguage() >> lang
        i18Service.getLocaleForLanguage(lang) >> Locale.GERMAN

        expect:
        localizationService.getDefaultLocale() == loc

        where:
        lang           | loc
        language('de') | Locale.GERMAN
        null           | Locale.ENGLISH
    }

    LanguageModel language(String code) {
        Stub(LanguageModel) {
            getIsocode() >> code
        }
    }
}
