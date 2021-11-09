/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.service.IntegrationLocalizationService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultODataLocalizationServiceUnitTest extends Specification {
	def localizationService = Stub IntegrationLocalizationService

	def oDataLocalizationService = new DefaultODataLocalizationService(localizationService:  localizationService)

	@Test
	def "successfully get the locale for the language iso code"() {
		given:
		def locale = Locale.ENGLISH
		localizationService.getSupportedLocale("en") >> Optional.of(locale)

		when:
		def actualLocale = oDataLocalizationService.getLocaleForLanguage("en")

		then:
		locale == actualLocale
	}

	@Test
	def "exception is thrown when isocode is = 'not a language code'"() {
		given:
		final String isoCode = "not a language code"
		localizationService.getSupportedLocale(isoCode) >> Optional.empty()

		when:
		oDataLocalizationService.getLocaleForLanguage(isoCode)

		then:
		def e = thrown LanguageNotSupportedException
		e.language == isoCode
	}

	@Test
	@Unroll
	def "get all supported locales when locales are #locales"()
	{
		given:
		localizationService.getAllSupportedLocales() >> locales

		expect:
		oDataLocalizationService.getSupportedLocales() == locales.toArray()

		where:
		locales << [[Locale.ENGLISH, Locale.FRENCH], []]
	}

	@Test
	def 'default locale is same as in the IntegrationLocalizationService'() {
		given:
		localizationService.getDefaultLocale() >> Locale.CHINESE

		expect:
		oDataLocalizationService.getCommerceSuiteLocale() == localizationService.defaultLocale
	}
}
