/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.unit.provider.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.classification.ClassificationService
import de.hybris.platform.classification.ClassificationSystemService
import de.hybris.platform.classification.features.Feature
import de.hybris.platform.classification.features.FeatureList
import de.hybris.platform.classification.features.FeatureValue
import de.hybris.platform.classification.features.LocalizedFeature
import de.hybris.platform.commerceservices.search.searchservices.dao.SnClassAttributeAssignmentModelDao
import de.hybris.platform.commerceservices.search.searchservices.provider.impl.ProductClassificationAttributeSnIndexerValueProvider
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.core.service.SnQualifier
import de.hybris.platform.searchservices.core.service.SnSessionService
import de.hybris.platform.searchservices.document.data.SnDocument
import de.hybris.platform.searchservices.indexer.SnIndexerException
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerFieldWrapper
import org.junit.Test
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

@UnitTest
public class ProductClassificationAttributeSnIndexerValueProviderSpec extends Specification {

	static final String FIELD_1_ID = "field1"
	static final String FIELD_2_ID = "field2"

	static final PK PRODUCT_PK = PK.fromLong(1)

	static final String CLASSIFICATON_SYTEM_ID = "ElectronicsClassification"
	static final String CLASSIFICATON_SYTEM_VERSION = "ElectronicsClassification"

	static final String CLASSIFICATON_CLASS_1_CODE = "622"
	static final String CLASSIFICATON_ATTRIBUTE_1_CODE = "Size, 1147"
	static final String CLASSIFICATON_ATTRIBUTE_1 = CLASSIFICATON_SYTEM_ID + "/" + CLASSIFICATON_SYTEM_VERSION + "/" + CLASSIFICATON_CLASS_1_CODE + "." + CLASSIFICATON_ATTRIBUTE_1_CODE

	static final String CLASSIFICATON_CLASS_2_CODE = "631"
	static final String CLASSIFICATON_ATTRIBUTE_2_CODE = "Resolution, 80"
	static final String CLASSIFICATON_ATTRIBUTE_2 = CLASSIFICATON_SYTEM_ID + "/" + CLASSIFICATON_SYTEM_VERSION + "/" + CLASSIFICATON_CLASS_2_CODE + "." + CLASSIFICATON_ATTRIBUTE_2_CODE

	SnIndexerContext indexerContext = Mock()
	ProductModel product = Mock()
	SnDocument document = new SnDocument()

	ClassificationSystemService classificationSystemService = Mock()
	ClassificationService classificationService = Mock()
	SnSessionService snSessionService = Mock()
	SnClassAttributeAssignmentModelDao snClassAttributeAssignmentModelDao = Mock()

	ProductClassificationAttributeSnIndexerValueProvider valueProvider

	def setup() {
		product.getPk() >> PRODUCT_PK

		Map<String, Object> attributes = new HashMap<>()
		indexerContext.getAttributes() >> attributes

		valueProvider = new ProductClassificationAttributeSnIndexerValueProvider()
		valueProvider.setClassificationSystemService(classificationSystemService)
		valueProvider.setClassificationService(classificationService)
		valueProvider.setSnSessionService(snSessionService)
		valueProvider.setSnClassAttributeAssignmentModelDao(snClassAttributeAssignmentModelDao)
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
		Map<String, String> valueProviderParameters = Map.of(ProductClassificationAttributeSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_PARAM, CLASSIFICATON_ATTRIBUTE_1)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		ClassificationSystemVersionModel classSystemVersion = Mock()
		ClassificationClassModel classClass = Mock()
		ClassificationAttributeModel classAttribute = Mock()
		ClassAttributeAssignmentModel classAttributeAssignment = Mock()
		FeatureList featureList = Mock()
		Feature feature = Mock()
		FeatureValue featureValue = Mock()
		Object value = Mock()

		classificationSystemService.getSystemVersion(CLASSIFICATON_SYTEM_ID, CLASSIFICATON_SYTEM_VERSION) >> classSystemVersion
		classificationSystemService.getClassForCode(classSystemVersion, CLASSIFICATON_CLASS_1_CODE) >> classClass
		classificationSystemService.getAttributeForCode(classSystemVersion, CLASSIFICATON_ATTRIBUTE_1_CODE) >> classAttribute
		snClassAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classClass, classAttribute) >> Optional.of(classAttributeAssignment)
		classificationService.getFeatures(product, List.of(classAttributeAssignment)) >> featureList
		featureList.getFeatureByAssignment(classAttributeAssignment) >> feature
		feature.getValues() >> List.of(featureValue)
		featureValue.getValue() >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == List.of(value)
	}

	@Test
	def "Provide null value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of(ProductClassificationAttributeSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_PARAM, CLASSIFICATON_ATTRIBUTE_1)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		ClassificationSystemVersionModel classSystemVersion = Mock()
		ClassificationClassModel classClass = Mock()
		ClassificationAttributeModel classAttribute = Mock()
		ClassAttributeAssignmentModel classAttributeAssignment = Mock()

		classificationSystemService.getSystemVersion(CLASSIFICATON_SYTEM_ID, CLASSIFICATON_SYTEM_VERSION) >> classSystemVersion
		classificationSystemService.getClassForCode(classSystemVersion, CLASSIFICATON_CLASS_1_CODE) >> classClass
		classificationSystemService.getAttributeForCode(classSystemVersion, CLASSIFICATON_ATTRIBUTE_1_CODE) >> classAttribute
		snClassAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classClass, classAttribute) >> Optional.of(classAttributeAssignment)

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == null
	}

	@Test
	def "Provide localized value"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID, localized: true)
		Map<String, String> valueProviderParameters = Map.of(ProductClassificationAttributeSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_PARAM, CLASSIFICATON_ATTRIBUTE_1)
		SnQualifier qualifier1 = Mock()
		SnQualifier qualifier2 = Mock()
		List<SnQualifier> qualifiers = List.of(qualifier1, qualifier2)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters, qualifiers: qualifiers)
		]

		qualifier1.getAs(Locale) >> Locale.ENGLISH
		qualifier2.getAs(Locale) >> Locale.GERMAN

		ClassificationSystemVersionModel classSystemVersion = Mock()
		ClassificationClassModel classClass = Mock()
		ClassificationAttributeModel classAttribute = Mock()
		ClassAttributeAssignmentModel classAttributeAssignment = Mock()
		FeatureList featureList = Mock()
		LocalizedFeature feature = Mock()
		FeatureValue featureValueEn = Mock()
		Object valueEn = Mock()
		FeatureValue featureValueDe = Mock()
		Object valueDe = Mock()

		classificationSystemService.getSystemVersion(CLASSIFICATON_SYTEM_ID, CLASSIFICATON_SYTEM_VERSION) >> classSystemVersion
		classificationSystemService.getClassForCode(classSystemVersion, CLASSIFICATON_CLASS_1_CODE) >> classClass
		classificationSystemService.getAttributeForCode(classSystemVersion, CLASSIFICATON_ATTRIBUTE_1_CODE) >> classAttribute
		snClassAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classClass, classAttribute) >> Optional.of(classAttributeAssignment)
		classificationService.getFeatures(product, List.of(classAttributeAssignment)) >> featureList
		featureList.getFeatureByAssignment(classAttributeAssignment) >> feature
		feature.getValuesForAllLocales() >> [
				(Locale.ENGLISH): List.of(featureValueEn),
				(Locale.GERMAN) : List.of(featureValueDe)
		]
		feature.getValues(Locale.ENGLISH) >> List.of(featureValueEn)
		featureValueEn.getValue() >> valueEn
		feature.getValues(Locale.GERMAN) >> List.of(featureValueDe)
		featureValueDe.getValue() >> valueDe

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == [
				(Locale.ENGLISH): List.of(valueEn),
				(Locale.GERMAN) : List.of(valueDe)
		]
	}

	@Test
	def "Provide values for multiple fields"() {
		given:
		SnField field1 = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters1 = Map.of(ProductClassificationAttributeSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_PARAM, CLASSIFICATON_ATTRIBUTE_1)

		SnField field2 = new SnField(id: FIELD_2_ID)
		Map<String, String> valueProviderParameters2 = Map.of(ProductClassificationAttributeSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_PARAM, CLASSIFICATON_ATTRIBUTE_2)

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field1, valueProviderParameters: valueProviderParameters1),
				new DefaultSnIndexerFieldWrapper(field: field2, valueProviderParameters: valueProviderParameters2)
		]

		ClassificationSystemVersionModel classSystemVersion = Mock()
		FeatureList featureList = Mock()

		ClassificationClassModel classClass1 = Mock()
		ClassificationAttributeModel classAttribute1 = Mock()
		ClassAttributeAssignmentModel classAttributeAssignment1 = Mock()
		Feature feature1 = Mock()
		FeatureValue featureValue1 = Mock()
		Object value1 = Mock()

		ClassificationClassModel classClass2 = Mock()
		ClassificationAttributeModel classAttribute2 = Mock()
		ClassAttributeAssignmentModel classAttributeAssignment2 = Mock()
		Feature feature2 = Mock()
		FeatureValue featureValue2 = Mock()
		Object value2 = Mock()

		classificationSystemService.getSystemVersion(CLASSIFICATON_SYTEM_ID, CLASSIFICATON_SYTEM_VERSION) >> classSystemVersion
		classificationService.getFeatures(product, List.of(classAttributeAssignment1, classAttributeAssignment2)) >> featureList
		classificationService.getFeatures(product, List.of(classAttributeAssignment2, classAttributeAssignment1)) >> featureList

		classificationSystemService.getClassForCode(classSystemVersion, CLASSIFICATON_CLASS_1_CODE) >> classClass1
		classificationSystemService.getAttributeForCode(classSystemVersion, CLASSIFICATON_ATTRIBUTE_1_CODE) >> classAttribute1
		snClassAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classClass1, classAttribute1) >> Optional.of(classAttributeAssignment1)
		featureList.getFeatureByAssignment(classAttributeAssignment1) >> feature1
		feature1.getValues() >> List.of(featureValue1)
		featureValue1.getValue() >> value1

		classificationSystemService.getClassForCode(classSystemVersion, CLASSIFICATON_CLASS_2_CODE) >> classClass2
		classificationSystemService.getAttributeForCode(classSystemVersion, CLASSIFICATON_ATTRIBUTE_2_CODE) >> classAttribute2
		snClassAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classClass2, classAttribute2) >> Optional.of(classAttributeAssignment2)
		featureList.getFeatureByAssignment(classAttributeAssignment2) >> feature2
		feature2.getValues() >> List.of(featureValue2)
		featureValue2.getValue() >> value2

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		document.getFields().get(FIELD_1_ID) == List.of(value1)
		document.getFields().get(FIELD_2_ID) == List.of(value2)
	}

	@Test
	def "Fail to provide value without classification attribute parameter"() {
		given:
		SnField field = new SnField(id: FIELD_1_ID)
		Map<String, String> valueProviderParameters = Map.of()

		List<SnIndexerFieldWrapper> fieldWrappers = [
				new DefaultSnIndexerFieldWrapper(field: field, valueProviderParameters: valueProviderParameters)
		]

		ClassificationSystemVersionModel classSystemVersion = Mock()
		ClassificationClassModel classClass = Mock()
		ClassificationAttributeModel classAttribute = Mock()
		ClassAttributeAssignmentModel classAttributeAssignment = Mock()
		FeatureList featureList = Mock()
		Feature feature = Mock()
		FeatureValue featureValue = Mock()
		Object value = Mock()

		classificationSystemService.getSystemVersion(CLASSIFICATON_SYTEM_ID, CLASSIFICATON_SYTEM_VERSION) >> classSystemVersion
		classificationSystemService.getClassForCode(classSystemVersion, CLASSIFICATON_CLASS_1_CODE) >> classClass
		classificationSystemService.getAttributeForCode(classSystemVersion, CLASSIFICATON_ATTRIBUTE_1_CODE) >> classAttribute
		snClassAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classClass, classAttribute) >> Optional.of(classAttributeAssignment)
		classificationService.getFeatures(product, List.of(classAttributeAssignment)) >> featureList
		featureList.getFeatureByAssignment(classAttributeAssignment) >> feature
		feature.getValues() >> List.of(featureValue)
		featureValue.getValue() >> value

		when:
		valueProvider.provide(indexerContext, fieldWrappers, product, document)

		then:
		thrown(SnIndexerException)
	}
}
