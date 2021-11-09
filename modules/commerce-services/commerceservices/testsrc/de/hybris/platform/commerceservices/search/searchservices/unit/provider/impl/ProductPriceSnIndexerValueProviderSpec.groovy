/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.unit.provider.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.commerceservices.search.searchservices.provider.impl.ProductPriceSnIndexerValueProvider
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.jalo.order.price.PriceInformation
import de.hybris.platform.product.PriceService
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.searchservices.core.service.SnSessionService
import de.hybris.platform.searchservices.document.data.SnDocument
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerFieldWrapper
import de.hybris.platform.servicelayer.i18n.CommonI18NService
import de.hybris.platform.util.PriceValue
import org.junit.Test
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

@UnitTest
public class ProductPriceSnIndexerValueProviderSpec extends Specification {

	static final String FIELD_1_ID = "field1"
	static final String FIELD_2_ID = "field2"

	static final String CURRENCY_EUR = "EUR"
	static final String CURRENCY_USD = "USD"

	static final String PRICE_1_CURRENCY = CURRENCY_EUR
	static final double PRICE_1_PRICE = 1.1d
	static final boolean PRICE_1_NETTO = false

	static final String PRICE_2_CURRENCY = CURRENCY_USD
	static final double PRICE_2_PRICE = 2.2d
	static final boolean PRICE_2_NETTO = false

	static final String QUALIFIER_TYPE_ID = "qualifierType"

	SnIndexerContext indexerContext = Mock()
	ProductModel product = Mock()
	SnDocument document = new SnDocument()

	CommonI18NService commonI18NService = Mock()
	PriceService priceService = Mock()
	SnSessionService snSessionService = Mock()

	ProductPriceSnIndexerValueProvider valueProvider

	def setup() {
		valueProvider = new ProductPriceSnIndexerValueProvider()
		valueProvider.setCommonI18NService(commonI18NService)
		valueProvider.setPriceService(priceService)
		valueProvider.setSnSessionService(snSessionService)
	}

	@Test
	def "Return supported qualifier classes"() {
		when:
		Set<Class<?>> qualifierClasses = valueProvider.getSupportedQualifierClasses()

		then:
		assertThat(qualifierClasses).contains(CurrencyModel)
	}

	@Test
	def "Fail to modify supported qualifier classes"() {
		given:
		Set<Class<?>> qualifierClasses = valueProvider.getSupportedQualifierClasses()

		when:
		qualifierClasses.add(this.getClass())

		then:
		thrown(UnsupportedOperationException)
	}

	@Test
	def "Provide value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of()

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		PriceValue priceValue = new PriceValue(PRICE_1_CURRENCY, PRICE_1_PRICE, PRICE_1_NETTO)
		PriceInformation priceInformation = new PriceInformation(priceValue)

		priceService.getPriceInformationsForProduct(product) >> List.of(priceInformation)

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == PRICE_1_PRICE
	}

	@Test
	def "Provide null value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of()

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == null
	}

	@Test
	def "Provide qualified value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID, localized: true, qualifierTypeId: QUALIFIER_TYPE_ID)
		Map<String, String> valueProviderParameters = Map.of()
		SnQualifier qualifier1 = Mock()
		SnQualifier qualifier2 = Mock()
		List<SnQualifier> qualifiers = List.of(qualifier1, qualifier2)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters, qualifiers: qualifiers)
		]

		PriceValue priceValue1 = new PriceValue(PRICE_1_CURRENCY, PRICE_1_PRICE, PRICE_1_NETTO)
		PriceInformation priceInformation1 = new PriceInformation(priceValue1)

		PriceValue priceValue2 = new PriceValue(PRICE_2_CURRENCY, PRICE_2_PRICE, PRICE_2_NETTO)
		PriceInformation priceInformation2 = new PriceInformation(priceValue2)

		priceService.getPriceInformationsForProduct(product) >>> [
				List.of(priceInformation1),
				List.of(priceInformation2)
		]

		CurrencyModel currency1 = new CurrencyModel(isocode: CURRENCY_EUR)
		qualifier1.getId() >> CURRENCY_EUR
		qualifier1.getAs(LanguageModel) >> currency1

		CurrencyModel currency2 = new CurrencyModel(isocode: CURRENCY_USD)
		qualifier2.getId() >> CURRENCY_USD
		qualifier2.getAs(LanguageModel) >> currency2

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == [
				(CURRENCY_EUR): PRICE_1_PRICE,
				(CURRENCY_USD): PRICE_2_PRICE
		]
	}

	@Test
	def "Provide values for multiple fields"() {
		given:
		SnField field1 = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters1 = Map.of()

		SnField field2 = new SnField(id: FIELD_2_ID)
		Map<String, String> valueProviderParameters2 = Map.of()

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field1, valueProviderParameters: valueProviderParameters1),
				new DefaultSnIndexerFieldWrapper(field: field2, valueProviderParameters: valueProviderParameters2)
		]

		PriceValue priceValue = new PriceValue(PRICE_1_CURRENCY, PRICE_1_PRICE, PRICE_1_NETTO)
		PriceInformation priceInformation = new PriceInformation(priceValue)

		priceService.getPriceInformationsForProduct(product) >> List.of(priceInformation)

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == PRICE_1_PRICE
		document.getFields().get(FIELD_2_ID) == PRICE_1_PRICE
	}
}
