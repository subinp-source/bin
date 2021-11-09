/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.unit.provider.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.commerceservices.search.searchservices.provider.impl.ProductImageAttributeSnIndexerValueProvider
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.media.MediaContainerModel
import de.hybris.platform.core.model.media.MediaFormatModel
import de.hybris.platform.core.model.media.MediaModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.searchservices.document.data.SnDocument
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerFieldWrapper
import de.hybris.platform.variants.model.VariantProductModel
import org.junit.Test
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

@UnitTest
public class ProductImageAttributeSnIndexerValueProviderSpec extends Specification {

	static final String FIELD_1_ID = "field1"
	static final String FIELD_2_ID = "field2"

	static final PK PRODUCT_PK = PK.fromLong(1)
	static final PK VARIANT_PRODUCT_PK = PK.fromLong(2)

	static final String MEDIA_CONTAINER_1_QUALIFIER = "mediaContainerQualifier1"
	static final String MEDIA_CONTAINER_2_QUALIFIER = "mediaContainerQualifier2"

	static final String MEDIA_FORMAT_1_QUALIFIER = "mediaFormatQualifier1"
	static final String MEDIA_FORMAT_2_QUALIFIER = "mediaFormatQualifier2"

	SnIndexerContext indexerContext = Mock()
	ProductModel product = Mock()
	SnDocument document = new SnDocument()

	SnExpressionEvaluator snExpressionEvaluator = Mock()

	ProductImageAttributeSnIndexerValueProvider valueProvider

	def setup() {
		product.getPk() >> PRODUCT_PK

		valueProvider = new ProductImageAttributeSnIndexerValueProvider()
		valueProvider.setSnExpressionEvaluator(snExpressionEvaluator)
	}

	@Test
	def "Return supported qualifier classes"() {
		when:
		Set<Class<?>> qualifierClasses = valueProvider.getSupportedQualifierClasses()

		then:
		assertThat(qualifierClasses).contains(Locale)
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

		MediaFormatModel mediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel media = new MediaModel(mediaFormat: mediaFormat)
		MediaContainerModel mediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(media))
		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer)

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(media), MediaModel.URL) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide null value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == null
	}

	@Test
	def "Provide localized value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID, localized: true, qualifierTypeId: null)
		Map<String, String> valueProviderParameters = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)
		SnQualifier qualifier1 = Mock()
		SnQualifier qualifier2 = Mock()
		List<SnQualifier> qualifiers = List.of(qualifier1, qualifier2)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters, qualifiers: qualifiers)
		]

		MediaFormatModel mediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel media = new MediaModel(mediaFormat: mediaFormat)
		MediaContainerModel mediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(media))
		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer)

		qualifier1.getAs(Locale) >> Locale.ENGLISH
		qualifier2.getAs(Locale) >> Locale.GERMAN
		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(media), FIELD_1_ID, List.of(Locale.ENGLISH, Locale.GERMAN)) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide values for multiple fields"() {
		given:
		SnField field1 = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters1 = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)

		SnField field2 = new SnField(id: FIELD_2_ID)
		Map<String, String> valueProviderParameters2 = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_2_ID)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field1, valueProviderParameters: valueProviderParameters1),
				new DefaultSnIndexerFieldWrapper(field: field2, valueProviderParameters: valueProviderParameters2)
		]

		MediaFormatModel mediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel media = new MediaModel(mediaFormat: mediaFormat)
		MediaContainerModel mediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(media))
		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer)

		Object value1 = Mock()
		Object value2 = Mock()
		snExpressionEvaluator.evaluate(Set.of(media), FIELD_1_ID) >> value1
		snExpressionEvaluator.evaluate(Set.of(media), FIELD_2_ID) >> value2

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value1)
		document.getFields().get(FIELD_2_ID).is(value2)
	}

	@Test
	def "Provide value using expression"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		MediaFormatModel mediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel media = new MediaModel(mediaFormat: mediaFormat)
		MediaContainerModel mediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(media))
		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer)

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(media), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with variants with default product selector"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		VariantProductModel variantProduct = Mock()
		variantProduct.getPk() >> VARIANT_PRODUCT_PK
		variantProduct.getBaseProduct() >> product

		MediaFormatModel variantMediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel variantMedia = new MediaModel(mediaFormat: variantMediaFormat)
		MediaContainerModel variantMediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(variantMedia))
		snExpressionEvaluator.evaluate(variantProduct, ProductModel.GALLERYIMAGES) >> List.of(variantMediaContainer)

		MediaFormatModel mediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_2_QUALIFIER)
		MediaModel media = new MediaModel(mediaFormat: mediaFormat)
		MediaContainerModel mediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_2_QUALIFIER, medias: List.of(media))
		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer)

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(variantMedia), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, variantProduct, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with variants with CURRENT product selector"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(
				ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID,
				ProductImageAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_PARAM, ProductImageAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_VALUE_CURRENT)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		VariantProductModel variantProduct = Mock()
		variantProduct.getPk() >> VARIANT_PRODUCT_PK
		variantProduct.getBaseProduct() >> product

		MediaFormatModel variantMediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel variantMedia = new MediaModel(mediaFormat: variantMediaFormat)
		MediaContainerModel variantMediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(variantMedia))
		snExpressionEvaluator.evaluate(variantProduct, ProductModel.GALLERYIMAGES) >> List.of(variantMediaContainer)

		MediaFormatModel mediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_2_QUALIFIER)
		MediaModel media = new MediaModel(mediaFormat: mediaFormat)
		MediaContainerModel mediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_2_QUALIFIER, medias: List.of(media))
		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer)

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(variantMedia), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, variantProduct, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with variants with CURRENT_PARENT product selector"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(
				ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID,
				ProductImageAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_PARAM, ProductImageAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_VALUE_CURRENT_PARENT)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		VariantProductModel variantProduct = Mock()
		variantProduct.getPk() >> VARIANT_PRODUCT_PK
		variantProduct.getBaseProduct() >> product

		MediaFormatModel variantMediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel variantMedia = new MediaModel(mediaFormat: variantMediaFormat)
		MediaContainerModel variantMediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(variantMedia))
		snExpressionEvaluator.evaluate(variantProduct, ProductModel.GALLERYIMAGES) >> List.of(variantMediaContainer)

		MediaFormatModel mediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_2_QUALIFIER)
		MediaModel media = new MediaModel(mediaFormat: mediaFormat)
		MediaContainerModel mediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_2_QUALIFIER, medias: List.of(media))
		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer)

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(variantMedia, media), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, variantProduct, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with variants with BASE product selector"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(
				ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID,
				ProductImageAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_PARAM, ProductImageAttributeSnIndexerValueProvider.PRODUCT_SELECTOR_VALUE_BASE)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		VariantProductModel variantProduct = Mock()
		variantProduct.getPk() >> VARIANT_PRODUCT_PK
		variantProduct.getBaseProduct() >> product

		MediaFormatModel variantMediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel variantMedia = new MediaModel(mediaFormat: variantMediaFormat)
		MediaContainerModel variantMediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(variantMedia))
		snExpressionEvaluator.evaluate(variantProduct, ProductModel.GALLERYIMAGES) >> List.of(variantMediaContainer)

		MediaFormatModel mediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_2_QUALIFIER)
		MediaModel media = new MediaModel(mediaFormat: mediaFormat)
		MediaContainerModel mediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_2_QUALIFIER, medias: List.of(media))
		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer)

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(media), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, variantProduct, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product using media expression"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID, ProductImageAttributeSnIndexerValueProvider.MEDIA_EXPRESSION_PARAM, ProductModel.PICTURE)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		MediaFormatModel mediaFormat = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel media = new MediaModel(mediaFormat: mediaFormat)

		snExpressionEvaluator.evaluate(product, ProductModel.PICTURE) >> media

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(media), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with multiple media containers"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		MediaFormatModel mediaFormat1 = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel media1 = new MediaModel(mediaFormat: mediaFormat1)
		MediaContainerModel mediaContainer1 = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(media1))

		MediaFormatModel mediaFormat2 = new MediaFormatModel(qualifier: MEDIA_FORMAT_2_QUALIFIER)
		MediaModel media2 = new MediaModel(mediaFormat: mediaFormat2)
		MediaContainerModel mediaContainer2 = new MediaContainerModel(qualifier: MEDIA_CONTAINER_2_QUALIFIER, medias: List.of(media2))

		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer1, mediaContainer2)

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(media1, media2), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with multiple media containers using media container"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID, ProductImageAttributeSnIndexerValueProvider.MEDIA_CONTAINER_PARAM, MEDIA_CONTAINER_2_QUALIFIER)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		MediaFormatModel mediaFormat1 = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel media1 = new MediaModel(mediaFormat: mediaFormat1)
		MediaContainerModel mediaContainer1 = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(media1))

		MediaFormatModel mediaFormat2 = new MediaFormatModel(qualifier: MEDIA_FORMAT_2_QUALIFIER)
		MediaModel media2 = new MediaModel(mediaFormat: mediaFormat2)
		MediaContainerModel mediaContainer2 = new MediaContainerModel(qualifier: MEDIA_CONTAINER_2_QUALIFIER, medias: List.of(media2))

		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer1, mediaContainer2)

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(media2), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with multiple medias"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID, ProductImageAttributeSnIndexerValueProvider.MEDIA_CONTAINER_PARAM, MEDIA_CONTAINER_1_QUALIFIER)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		MediaFormatModel mediaFormat1 = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel media1 = new MediaModel(mediaFormat: mediaFormat1)

		MediaFormatModel mediaFormat2 = new MediaFormatModel(qualifier: MEDIA_FORMAT_2_QUALIFIER)
		MediaModel media2 = new MediaModel(mediaFormat: mediaFormat2)

		MediaContainerModel mediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(media1, media2))

		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer)

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(media1, media2), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}

	@Test
	def "Provide value for product with multiple medias using media format"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductImageAttributeSnIndexerValueProvider.EXPRESSION_PARAM, FIELD_1_ID, ProductImageAttributeSnIndexerValueProvider.MEDIA_FORMAT_PARAM, MEDIA_FORMAT_2_QUALIFIER)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		MediaFormatModel mediaFormat1 = new MediaFormatModel(qualifier: MEDIA_FORMAT_1_QUALIFIER)
		MediaModel media1 = new MediaModel(mediaFormat: mediaFormat1)

		MediaFormatModel mediaFormat2 = new MediaFormatModel(qualifier: MEDIA_FORMAT_2_QUALIFIER)
		MediaModel media2 = new MediaModel(mediaFormat: mediaFormat2)

		MediaContainerModel mediaContainer = new MediaContainerModel(qualifier: MEDIA_CONTAINER_1_QUALIFIER, medias: List.of(media1, media2))

		snExpressionEvaluator.evaluate(product, ProductModel.GALLERYIMAGES) >> List.of(mediaContainer)

		Object value = Mock()
		snExpressionEvaluator.evaluate(Set.of(media2), FIELD_1_ID) >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID).is(value)
	}
}
