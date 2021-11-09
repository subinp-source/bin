/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.core.service.impl

import static org.assertj.core.api.Assertions.assertThat

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration
import de.hybris.platform.searchservices.admin.data.SnIndexType
import de.hybris.platform.searchservices.admin.data.SnLanguage
import de.hybris.platform.searchservices.core.service.SnContext
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.searchservices.core.service.impl.LanguageSnQualifierProvider
import de.hybris.platform.servicelayer.i18n.CommonI18NService

import org.junit.Test

import spock.lang.Specification


@UnitTest
public class LanguageSnQualifierProviderSpec extends Specification {

	static final String INDEX_CONFIGURATION_ID = "indexConfiguration"
	static final String INDEX_TYPE_ID = "indexType"

	static final String EN_LANGUAGE_ID = "en"
	static final String DE_LANGUAGE_ID = "de"
	static final String FR_LANGUAGE_ID = "fr"

	SnContext context = Mock()
	SnIndexConfiguration indexConfiguration = Mock()
	SnIndexType indexType = Mock()
	SnField field = Mock()
	ItemModel item = Mock()

	CommonI18NService commonI18NService = Mock()

	LanguageSnQualifierProvider qualifierProvider

	def setup() {
		indexConfiguration.getId() >> INDEX_CONFIGURATION_ID
		indexType.getId() >> INDEX_TYPE_ID

		Map<String, Object> attributes = new HashMap<>()
		context.getAttributes() >> attributes
		context.getIndexConfiguration() >> indexConfiguration
		context.getIndexType() >> indexType

		qualifierProvider = new LanguageSnQualifierProvider()
		qualifierProvider.setCommonI18NService(commonI18NService)
	}

	@Test
	def "Return supported qualifier classes"() {
		when:
		Set<Class<?>> qualifierClasses = qualifierProvider.getSupportedQualifierClasses()

		then:
		assertThat(qualifierClasses).contains(LanguageModel, Locale)
	}

	@Test
	def "Fail to modify supported qualifier classes"() {
		given:
		Set<Class<?>> qualifierClasses = qualifierProvider.getSupportedQualifierClasses()

		when:
		qualifierClasses.add(this.getClass())

		then:
		thrown(UnsupportedOperationException)
	}

	@Test
	def "Available qualifiers is empty"() {
		when:
		List<SnQualifier> qualifiers = qualifierProvider.getAvailableQualifiers(context)

		then:
		qualifiers != null
		qualifiers.isEmpty()
	}

	@Test
	def "Available qualifiers has single qualifier"() {
		given:
		LanguageModel language = new LanguageModel(isocode: EN_LANGUAGE_ID)
		Locale locale = Locale.forLanguageTag(EN_LANGUAGE_ID)

		commonI18NService.getLanguage(EN_LANGUAGE_ID) >> language
		commonI18NService.getLocaleForLanguage(language) >> locale

		context.getIndexConfiguration().getLanguages() >> List.of(new SnLanguage(id: EN_LANGUAGE_ID))

		when:
		List<SnQualifier> qualifiers = qualifierProvider.getAvailableQualifiers(context)

		then:
		qualifiers != null
		qualifiers.size() == 1

		with(qualifiers[0]) {
			id == EN_LANGUAGE_ID
			getAs(LanguageModel) == language
			getAs(Locale) == locale
		}
	}

	@Test
	def "Available qualifiers has multiple qualifiers"() {
		given:
		LanguageModel enLanguage = new LanguageModel(isocode: EN_LANGUAGE_ID)
		LanguageModel deLanguage = new LanguageModel(isocode: DE_LANGUAGE_ID)
		Locale enLocale = Locale.forLanguageTag(EN_LANGUAGE_ID)
		Locale deLocale = Locale.forLanguageTag(DE_LANGUAGE_ID)

		commonI18NService.getLanguage(EN_LANGUAGE_ID) >> enLanguage
		commonI18NService.getLanguage(DE_LANGUAGE_ID) >> deLanguage
		commonI18NService.getLocaleForLanguage(enLanguage) >> enLocale
		commonI18NService.getLocaleForLanguage(deLanguage) >> deLocale

		context.getIndexConfiguration().getLanguages() >> List.of(new SnLanguage(id: EN_LANGUAGE_ID), new SnLanguage(id: DE_LANGUAGE_ID))

		when:
		List<SnQualifier> qualifiers = qualifierProvider.getAvailableQualifiers(context)

		then:
		qualifiers != null
		qualifiers.size() == 2

		with(qualifiers[0]) {
			id == EN_LANGUAGE_ID
			getAs(LanguageModel) == enLanguage
			getAs(Locale) == enLocale
		}

		with(qualifiers[1]) {
			id == DE_LANGUAGE_ID
			getAs(LanguageModel) == deLanguage
			getAs(Locale) == deLocale
		}
	}

	@Test
	def "Current qualifiers is empty"() {
		when:
		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		then:
		qualifiers != null
		qualifiers.isEmpty()
	}

	@Test
	def "Current qualifiers has single qualifier"() {
		given:
		LanguageModel language = new LanguageModel(isocode: EN_LANGUAGE_ID)
		Locale locale = Locale.forLanguageTag(EN_LANGUAGE_ID)

		commonI18NService.getCurrentLanguage() >> language
		commonI18NService.getLocaleForLanguage(language) >> locale

		when:
		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		then:
		qualifiers != null
		qualifiers.size() == 1

		with(qualifiers[0]) {
			id == EN_LANGUAGE_ID
			getAs(LanguageModel) == language
			getAs(Locale) == locale
		}
	}

	@Test
	def "Current qualifiers has mutiple qualifiers"() {
		given:
		LanguageModel enLanguage = new LanguageModel(isocode: EN_LANGUAGE_ID)
		LanguageModel deLanguage = new LanguageModel(isocode: DE_LANGUAGE_ID)
		LanguageModel frLanguage = new LanguageModel(isocode: FR_LANGUAGE_ID, fallbackLanguages: List.of(enLanguage, deLanguage))
		Locale enLocale = Locale.forLanguageTag(EN_LANGUAGE_ID)
		Locale deLocale = Locale.forLanguageTag(DE_LANGUAGE_ID)
		Locale frLocale = Locale.forLanguageTag(FR_LANGUAGE_ID)

		commonI18NService.getCurrentLanguage() >> frLanguage
		commonI18NService.getLocaleForLanguage(enLanguage) >> enLocale
		commonI18NService.getLocaleForLanguage(deLanguage) >> deLocale
		commonI18NService.getLocaleForLanguage(frLanguage) >> frLocale

		when:
		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		then:
		qualifiers != null
		qualifiers.size() == 3

		with(qualifiers[0]) {
			id == FR_LANGUAGE_ID
			getAs(LanguageModel) == frLanguage
			getAs(Locale) == frLocale
		}

		with(qualifiers[1]) {
			id == EN_LANGUAGE_ID
			getAs(LanguageModel) == enLanguage
			getAs(Locale) == enLocale
		}

		with(qualifiers[2]) {
			id == DE_LANGUAGE_ID
			getAs(LanguageModel) == deLanguage
			getAs(Locale) == deLocale
		}
	}

	@Test
	def "Fail to get qualifier as not supported class"() {
		given:
		LanguageModel language = new LanguageModel(isocode: EN_LANGUAGE_ID)
		Locale locale = Locale.forLanguageTag(EN_LANGUAGE_ID)

		commonI18NService.getCurrentLanguage() >> language
		commonI18NService.getLocaleForLanguage(language) >> locale

		List<SnQualifier> qualifier = qualifierProvider.getCurrentQualifiers(context)

		when:
		qualifier.get(0).getAs(CurrencyModel)

		then:
		thrown(IllegalArgumentException)
	}

	@Test
	def "Can get qualifier as LanguageModel class"() {
		given:
		LanguageModel language = new LanguageModel(isocode: EN_LANGUAGE_ID)
		Locale locale = Locale.forLanguageTag(EN_LANGUAGE_ID)

		commonI18NService.getCurrentLanguage() >> language
		commonI18NService.getLocaleForLanguage(language) >> locale

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		boolean result = qualifiers.get(0).canGetAs(LanguageModel)

		then:
		result == true
	}

	@Test
	def "Cannot get qualifier as LanguageModel subclass"() {
		given:
		LanguageModel language = new LanguageModel(isocode: EN_LANGUAGE_ID)
		Locale locale = Locale.forLanguageTag(EN_LANGUAGE_ID)

		commonI18NService.getCurrentLanguage() >> language
		commonI18NService.getLocaleForLanguage(language) >> locale

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		boolean result = qualifiers.get(0).canGetAs(ExtendedLanguageModel)

		then:
		result == false
	}

	@Test
	def "Can get qualifier as Locale class"() {
		given:
		LanguageModel language = new LanguageModel(isocode: EN_LANGUAGE_ID)
		Locale locale = Locale.forLanguageTag(EN_LANGUAGE_ID)

		commonI18NService.getCurrentLanguage() >> language
		commonI18NService.getLocaleForLanguage(language) >> locale

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		boolean result = qualifiers.get(0).canGetAs(Locale)

		then:
		result == true
	}

	@Test
	def "Cannot get qualifier as not supported class"() {
		given:
		LanguageModel language = new LanguageModel(isocode: EN_LANGUAGE_ID)
		Locale locale = Locale.forLanguageTag(EN_LANGUAGE_ID)

		commonI18NService.getCurrentLanguage() >> language
		commonI18NService.getLocaleForLanguage(language) >> locale

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		boolean result = qualifiers.get(0).canGetAs(CurrencyModel)

		then:
		result == false
	}

	static class ExtendedLanguageModel extends LanguageModel {
	}
}
