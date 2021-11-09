/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.core.service.impl

import static org.assertj.core.api.Assertions.assertThat

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.searchservices.admin.data.SnCurrency
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration
import de.hybris.platform.searchservices.admin.data.SnIndexType
import de.hybris.platform.searchservices.core.service.SnContext
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.searchservices.core.service.impl.CurrencySnQualifierProvider
import de.hybris.platform.servicelayer.i18n.CommonI18NService

import org.junit.Test

import spock.lang.Specification


@UnitTest
public class CurrencySnQualifierProviderSpec extends Specification {

	static final String INDEX_CONFIGURATION_ID = "indexConfiguration"
	static final String INDEX_TYPE_ID = "indexType"

	static final String EUR_CURRENCY_ID = "EUR"
	static final String USD_CURRENCY_ID = "USD"

	SnContext context = Mock()
	SnIndexConfiguration indexConfiguration = Mock()
	SnIndexType indexType = Mock()
	SnField field = Mock()
	ItemModel item = Mock()

	CommonI18NService commonI18NService = Mock()

	CurrencySnQualifierProvider qualifierProvider

	def setup() {
		indexConfiguration.getId() >> INDEX_CONFIGURATION_ID
		indexType.getId() >> INDEX_TYPE_ID

		Map<String, Object> attributes = new HashMap<>()
		context.getAttributes() >> attributes
		context.getIndexConfiguration() >> indexConfiguration
		context.getIndexType() >> indexType

		qualifierProvider = new CurrencySnQualifierProvider()
		qualifierProvider.setCommonI18NService(commonI18NService)
	}

	@Test
	def "Return supported qualifier classes"() {
		when:
		Set<Class<?>> qualifierClasses = qualifierProvider.getSupportedQualifierClasses()

		then:
		assertThat(qualifierClasses).contains(CurrencyModel)
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
		CurrencyModel currency = new CurrencyModel(isocode: EUR_CURRENCY_ID)

		commonI18NService.getCurrency(EUR_CURRENCY_ID) >> currency

		context.getIndexConfiguration().getCurrencies() >> List.of(new SnCurrency(id: EUR_CURRENCY_ID))

		when:
		List<SnQualifier> qualifiers = qualifierProvider.getAvailableQualifiers(context)

		then:
		qualifiers != null
		qualifiers.size() == 1

		with(qualifiers[0]) {
			id == EUR_CURRENCY_ID
			getAs(CurrencyModel) == currency
		}
	}

	@Test
	def "Available qualifiers has multiple qualifiers"() {
		given:
		CurrencyModel eurCurrency = new CurrencyModel(isocode: EUR_CURRENCY_ID)
		CurrencyModel usdCurrency = new CurrencyModel(isocode: USD_CURRENCY_ID)

		commonI18NService.getCurrency(EUR_CURRENCY_ID) >> eurCurrency
		commonI18NService.getCurrency(USD_CURRENCY_ID) >> usdCurrency

		context.getIndexConfiguration().getCurrencies() >> List.of(new SnCurrency(id: EUR_CURRENCY_ID), new SnCurrency(id: USD_CURRENCY_ID))

		when:
		List<SnQualifier> qualifiers = qualifierProvider.getAvailableQualifiers(context)

		then:
		qualifiers != null
		qualifiers.size() == 2

		with(qualifiers[0]) {
			id == EUR_CURRENCY_ID
			getAs(CurrencyModel) == eurCurrency
		}

		with(qualifiers[1]) {
			id == USD_CURRENCY_ID
			getAs(CurrencyModel) == usdCurrency
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
		CurrencyModel currency = new CurrencyModel(isocode: EUR_CURRENCY_ID)

		commonI18NService.getCurrentCurrency() >> currency

		when:
		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		then:
		qualifiers != null
		qualifiers.size() == 1

		with(qualifiers[0]) {
			id == EUR_CURRENCY_ID
			getAs(CurrencyModel) == currency
		}
	}

	@Test
	def "Fail to get qualifier as not supported class"() {
		given:
		CurrencyModel currency = new CurrencyModel(isocode: EUR_CURRENCY_ID)

		commonI18NService.getCurrentCurrency() >> currency

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		qualifiers.get(0).getAs(LanguageModel)

		then:
		thrown(IllegalArgumentException)
	}


	@Test
	def "Can get qualifier as CurrencyModel class"() {
		given:
		CurrencyModel currency = new CurrencyModel(isocode: EUR_CURRENCY_ID)

		commonI18NService.getCurrentCurrency() >> currency

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		boolean result = qualifiers.get(0).canGetAs(CurrencyModel)

		then:
		result == true
	}

	@Test
	def "Cannot get qualifier as CurrencyModel subclass"() {
		given:
		CurrencyModel currency = new CurrencyModel(isocode: EUR_CURRENCY_ID)

		commonI18NService.getCurrentCurrency() >> currency

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		boolean result = qualifiers.get(0).canGetAs(ExtendedCurrencyModel)

		then:
		result == false
	}

	@Test
	def "Cannot get qualifier as not supported class"() {
		given:
		CurrencyModel currency = new CurrencyModel(isocode: EUR_CURRENCY_ID)

		commonI18NService.getCurrentCurrency() >> currency

		List<SnQualifier> qualifiers = qualifierProvider.getCurrentQualifiers(context)

		when:
		boolean result = qualifiers.get(0).canGetAs(LanguageModel)

		then:
		result == false
	}

	static class ExtendedCurrencyModel extends CurrencyModel {
	}
}
