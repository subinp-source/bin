/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.searchservices.strategies.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.adaptivesearch.data.AsExpressionData
import de.hybris.platform.adaptivesearch.data.AsIndexConfigurationData
import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData
import de.hybris.platform.adaptivesearch.data.AsIndexTypeData
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator
import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.searchservices.admin.data.*
import de.hybris.platform.searchservices.admin.service.SnCommonConfigurationService
import de.hybris.platform.searchservices.admin.service.SnFieldTypeRegistry
import de.hybris.platform.searchservices.admin.service.SnIndexConfigurationService
import de.hybris.platform.searchservices.admin.service.SnIndexTypeService
import de.hybris.platform.searchservices.core.service.SnContextFactory
import de.hybris.platform.searchservices.core.service.SnQualifierTypeFactory
import de.hybris.platform.searchservices.core.service.SnSessionService
import de.hybris.platform.searchservices.enums.SnFieldType
import de.hybris.platform.searchservices.search.data.SnMatchQuery
import de.hybris.platform.searchservices.search.service.SnSearchService
import de.hybris.platform.servicelayer.i18n.CommonI18NService
import de.hybris.platform.servicelayer.i18n.I18NService
import de.hybris.platform.servicelayer.type.TypeService

import org.junit.Test

import spock.lang.Specification

@UnitTest
class SnAsSearchProviderSpec extends Specification {

	protected static final String INDEX_CONFIGURATION_CODE = "indexConfigurationId"
	protected static final String INDEX_CONFIGURATION_NAME_EN = "indexConfigurationName"
	protected static final Map<Locale, String> INDEX_CONFIGURATION_NAME = [
		(Locale.ENGLISH): INDEX_CONFIGURATION_NAME_EN
	]

	protected static final String INDEX_TYPE_CODE = "indexTypeId"
	protected static final String INDEX_TYPE_NAME_EN = "indexTypeName"
	protected static final Map<Locale, String> INDEX_TYPE_NAME =  [
		(Locale.ENGLISH): INDEX_TYPE_NAME_EN
	]

	protected static final String INDEX_PROPERTY_CODE = "indexPropertyId"
	protected static final String INDEX_PROPERTY_NAME_EN = "indexPropertyName"
	protected static final Map<Locale, String> INDEX_PROPERTY_NAME =  [
		(Locale.ENGLISH): INDEX_PROPERTY_NAME_EN
	]
	protected static final String INDEX_TYPE_ITEM_TYPE = "Product"

	protected static final String EXPRESSION_EXPRESSION = "expression"
	protected static final String EXPRESSION_NAME_EN = "expressionName"
	protected static final Map<Locale, String> EXPRESSION_NAME = [
		(Locale.ENGLISH): EXPRESSION_NAME_EN
	]

	TypeService typeService = Mock()
	I18NService i18nService = Mock()
	CommonI18NService commonI18NService = Mock()
	CatalogVersionService catalogVersionService = Mock()
	SnSessionService snSessionService = Mock()
	SnFieldTypeRegistry snFieldTypeRegistry = Mock()
	SnIndexConfigurationService snIndexConfigurationService = Mock()
	SnIndexTypeService snIndexTypeService = Mock()
	SnCommonConfigurationService snCommonConfigurationService = Mock()
	SnQualifierTypeFactory snQualifierTypeFactory = Mock()
	SnContextFactory snContextFactory = Mock()
	SnSearchService snSearchService = Mock()

	SnAsSearchProvider snAsSearchProvider

	def setup() {
		i18nService.getCurrentLocale() >> Locale.ENGLISH

		snAsSearchProvider = new SnAsSearchProvider()
		snAsSearchProvider.setTypeService(typeService)
		snAsSearchProvider.setI18nService(i18nService)
		snAsSearchProvider.setCommonI18NService(commonI18NService)
		snAsSearchProvider.setCatalogVersionService(catalogVersionService)
		snAsSearchProvider.setSnSessionService(snSessionService)
		snAsSearchProvider.setSnFieldTypeRegistry(snFieldTypeRegistry)
		snAsSearchProvider.setSnIndexConfigurationService(snIndexConfigurationService)
		snAsSearchProvider.setSnIndexTypeService(snIndexTypeService)
		snAsSearchProvider.setSnCommonConfigurationService(snCommonConfigurationService)
		snAsSearchProvider.setSnQualifierTypeFactory(snQualifierTypeFactory)
		snAsSearchProvider.setSnContextFactory(snContextFactory)
		snAsSearchProvider.setSnSearchService(snSearchService)

		snAsSearchProvider.afterPropertiesSet()
	}

	@Test
	def "Get empty index configurations"() {
		given:
		snIndexConfigurationService.getAllIndexConfigurations() >> List.of()

		when:
		List<AsIndexConfigurationData> resultIndexConfigurations = snAsSearchProvider.getIndexConfigurations()

		then:
		resultIndexConfigurations != null
		resultIndexConfigurations.size() == 0
	}

	@Test
	def "Get index configurations"() {
		given:
		SnIndexConfiguration indexConfiguration = new SnIndexConfiguration(id: INDEX_CONFIGURATION_CODE, name: INDEX_CONFIGURATION_NAME)
		snIndexConfigurationService.getAllIndexConfigurations() >> List.of(indexConfiguration)

		when:
		List<AsIndexConfigurationData> resultIndexConfigurations = snAsSearchProvider.getIndexConfigurations()

		then:
		resultIndexConfigurations != null
		resultIndexConfigurations.size() == 1

		with(resultIndexConfigurations[0]) {
			code == INDEX_CONFIGURATION_CODE
			name == INDEX_CONFIGURATION_NAME_EN
		}
	}

	@Test
	def "Get non existing index configuration for code"() {
		given:
		snIndexConfigurationService.getIndexConfigurationForId(INDEX_CONFIGURATION_CODE) >> Optional.empty()

		when:
		Optional<AsIndexConfigurationData> resultIndexConfigurationOptional = snAsSearchProvider.getIndexConfigurationForCode(INDEX_CONFIGURATION_CODE)

		then:
		resultIndexConfigurationOptional.isEmpty()
	}

	@Test
	def "Get index configuration for code"() {
		given:
		SnIndexConfiguration indexConfiguration = new SnIndexConfiguration(id: INDEX_CONFIGURATION_CODE, name: INDEX_CONFIGURATION_NAME)
		snIndexConfigurationService.getIndexConfigurationForId(INDEX_CONFIGURATION_CODE) >> Optional.of(indexConfiguration)

		when:
		Optional<AsIndexConfigurationData> resultIndexConfigurationOptional = snAsSearchProvider.getIndexConfigurationForCode(INDEX_CONFIGURATION_CODE)

		then:
		resultIndexConfigurationOptional.isPresent()

		with(resultIndexConfigurationOptional.get()) {
			code == INDEX_CONFIGURATION_CODE
			name == INDEX_CONFIGURATION_NAME_EN
		}
	}

	@Test
	def "Get empty index types"() {
		given:
		snIndexTypeService.getAllIndexTypes() >> List.of()

		when:
		List<AsIndexTypeData> resultIndexTypes = snAsSearchProvider.getIndexTypes()

		then:
		resultIndexTypes != null
		resultIndexTypes.size() == 0
	}

	@Test
	def "Get index types"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_CODE, name: INDEX_TYPE_NAME, itemComposedType: INDEX_TYPE_ITEM_TYPE)
		snIndexTypeService.getAllIndexTypes() >> List.of(indexType)

		ComposedTypeModel composedType = Mock()
		composedType.getCode() >> INDEX_TYPE_ITEM_TYPE
		composedType.getCatalogItemType() >> Boolean.TRUE

		typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType

		when:
		List<AsIndexTypeData> resultIndexTypes = snAsSearchProvider.getIndexTypes()

		then:
		resultIndexTypes != null
		resultIndexTypes.size() == 1

		with(resultIndexTypes[0]) {
			code == INDEX_TYPE_CODE
			name == INDEX_TYPE_NAME_EN
			itemType == INDEX_TYPE_ITEM_TYPE
			catalogVersionAware == Boolean.TRUE
		}
	}

	@Test
	def "Get non existing index type for code"() {
		given:
		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.empty()

		when:
		Optional<AsIndexTypeData> resultIndexTypeOptional = snAsSearchProvider.getIndexTypeForCode(INDEX_TYPE_CODE)

		then:
		resultIndexTypeOptional.isEmpty()
	}

	@Test
	def "Get index type for code"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_CODE, name: INDEX_TYPE_NAME, itemComposedType: INDEX_TYPE_ITEM_TYPE)
		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.of(indexType)

		ComposedTypeModel composedType = Mock()
		composedType.getCode() >> INDEX_TYPE_ITEM_TYPE
		composedType.getCatalogItemType() >> Boolean.TRUE

		typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType

		when:
		Optional<AsIndexTypeData> resultIndexTypeOptional = snAsSearchProvider.getIndexTypeForCode(INDEX_TYPE_CODE)

		then:
		resultIndexTypeOptional.isPresent()

		with(resultIndexTypeOptional.get()) {
			code == INDEX_TYPE_CODE
			name == INDEX_TYPE_NAME_EN
			itemType == INDEX_TYPE_ITEM_TYPE
			catalogVersionAware == Boolean.TRUE
		}
	}

	@Test
	def "Get empty index properties"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_CODE, name: INDEX_TYPE_NAME, itemComposedType: INDEX_TYPE_ITEM_TYPE, fields: Map.of())
		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.of(indexType)

		ComposedTypeModel composedType = Mock()
		composedType.getCode() >> INDEX_TYPE_ITEM_TYPE
		composedType.getCatalogItemType() >> Boolean.TRUE

		typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType

		when:
		List<AsIndexPropertyData> resultIndexProperties = snAsSearchProvider.getIndexProperties(INDEX_TYPE_CODE)

		then:
		resultIndexProperties != null
		resultIndexProperties.size() == 0
	}

	@Test
	def "Get index properties"() {
		given:
		SnField field = new SnField(id: INDEX_PROPERTY_CODE, name: INDEX_PROPERTY_NAME, fieldType: SnFieldType.STRING)
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_CODE, name: INDEX_TYPE_NAME, itemComposedType: INDEX_TYPE_ITEM_TYPE, fields: Map.of(INDEX_PROPERTY_CODE, field))
		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.of(indexType)

		ComposedTypeModel composedType = Mock()
		composedType.getCode() >> INDEX_TYPE_ITEM_TYPE
		composedType.getCatalogItemType() >> Boolean.TRUE

		typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType

		SnFieldTypeInfo fieldTypeInfo = new SnFieldTypeInfo(valueType: String.class, supportedQueryTypes: List.of(SnMatchQuery.TYPE))

		snFieldTypeRegistry.getFieldTypeInfo(SnFieldType.STRING) >> fieldTypeInfo

		when:
		List<AsIndexPropertyData> resultIndexProperties = snAsSearchProvider.getIndexProperties(INDEX_TYPE_CODE)

		then:
		resultIndexProperties != null
		resultIndexProperties.size() == 1

		with(resultIndexProperties[0]) {
			code == INDEX_PROPERTY_CODE
			name == INDEX_PROPERTY_NAME_EN
			type == String.class
			supportedBoostOperators == Set.of(AsBoostOperator.MATCH)
		}
	}

	@Test
	def "Get non existing index property for code"() {
		given:
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_CODE, name: INDEX_TYPE_NAME, itemComposedType: INDEX_TYPE_ITEM_TYPE, fields: Map.of())
		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.of(indexType)

		ComposedTypeModel composedType = Mock()
		composedType.getCode() >> INDEX_TYPE_ITEM_TYPE
		composedType.getCatalogItemType() >> Boolean.TRUE

		typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType

		when:
		Optional<AsIndexPropertyData> resultIndexPropertyOptional = snAsSearchProvider.getIndexPropertyForCode(INDEX_TYPE_CODE, INDEX_PROPERTY_CODE)

		then:
		resultIndexPropertyOptional.isEmpty()
	}

	@Test
	def "Get index property for code"() {
		given:
		SnField field = new SnField(id: INDEX_PROPERTY_CODE, name: INDEX_PROPERTY_NAME, fieldType: SnFieldType.STRING)
		SnIndexType indexType = new SnIndexType(id: INDEX_TYPE_CODE, name: INDEX_TYPE_NAME, itemComposedType: INDEX_TYPE_ITEM_TYPE, fields: Map.of(INDEX_PROPERTY_CODE, field))
		snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.of(indexType)

		ComposedTypeModel composedType = Mock()
		composedType.getCode() >> INDEX_TYPE_ITEM_TYPE
		composedType.getCatalogItemType() >> Boolean.TRUE

		typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType

		SnFieldTypeInfo fieldTypeInfo = new SnFieldTypeInfo(valueType: String.class, supportedQueryTypes: List.of(SnMatchQuery.TYPE))

		snFieldTypeRegistry.getFieldTypeInfo(SnFieldType.STRING) >> fieldTypeInfo

		when:
		Optional<AsIndexPropertyData> resultIndexPropertyOptional = snAsSearchProvider.getIndexPropertyForCode(INDEX_TYPE_CODE, INDEX_PROPERTY_CODE)

		then:
		resultIndexPropertyOptional.isPresent()

		with(resultIndexPropertyOptional.get()) {
			code == INDEX_PROPERTY_CODE
			name == INDEX_PROPERTY_NAME_EN
			type == String.class
			supportedBoostOperators == Set.of(AsBoostOperator.MATCH)
		}
	}

	@Test
	def "Get empty supported catalog versions"() {
		given:
		snCommonConfigurationService.getCatalogVersions(INDEX_TYPE_CODE) >> List.of()

		when:
		List<CatalogVersionModel> resultCatalogVersions = snAsSearchProvider.getSupportedCatalogVersions(INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE)

		then:
		resultCatalogVersions != null
		resultCatalogVersions.size() == 0
	}

	@Test
	def "Get supported catalog versions"() {
		given:
		CatalogVersionModel catalogVersion = Mock()
		snCommonConfigurationService.getCatalogVersions(INDEX_TYPE_CODE) >> List.of(catalogVersion)

		when:
		List<CatalogVersionModel> resultCatalogVersions = snAsSearchProvider.getSupportedCatalogVersions(INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE)

		then:
		resultCatalogVersions != null
		resultCatalogVersions.size() == 1
		resultCatalogVersions[0].is(catalogVersion)
	}

	@Test
	def "Get empty supported languages"() {
		given:
		snCommonConfigurationService.getLanguages(INDEX_TYPE_CODE) >> List.of()

		when:
		List<LanguageModel> resultLanguages = snAsSearchProvider.getSupportedLanguages(INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE)

		then:
		resultLanguages != null
		resultLanguages.size() == 0
	}

	@Test
	def "Get supported languages"() {
		given:
		CatalogVersionModel language = Mock()
		snCommonConfigurationService.getLanguages(INDEX_TYPE_CODE) >> List.of(language)

		when:
		List<LanguageModel> resultLanguages = snAsSearchProvider.getSupportedLanguages(INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE)

		then:
		resultLanguages != null
		resultLanguages.size() == 1
		resultLanguages[0].is(language)
	}

	@Test
	def "Get empty supported currencies"() {
		given:
		snCommonConfigurationService.getCurrencies(INDEX_TYPE_CODE) >> List.of()

		when:
		List<CurrencyModel> resultCurrencies = snAsSearchProvider.getSupportedCurrencies(INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE)

		then:
		resultCurrencies != null
		resultCurrencies.size() == 0
	}

	@Test
	def "Get supported currencies"() {
		given:
		CurrencyModel currency = Mock()
		snCommonConfigurationService.getCurrencies(INDEX_TYPE_CODE) >> List.of(currency)

		when:
		List<CurrencyModel> resultCurrencies = snAsSearchProvider.getSupportedCurrencies(INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE)

		then:
		resultCurrencies != null
		resultCurrencies.size() == 1
		resultCurrencies[0].is(currency)
	}

	@Test
	def "Get empty supported facet expressions"() {
		given:
		snCommonConfigurationService.getFacetExpressions(INDEX_TYPE_CODE) >> List.of()

		when:
		List<AsExpressionData> expressionInfo = snAsSearchProvider.getSupportedFacetExpressions(INDEX_TYPE_CODE)

		then:
		expressionInfo != null
		expressionInfo.size() == 0
	}

	@Test
	def "Get supported facet expressions"() {
		given:
		SnExpressionInfo expressionInfo = new SnExpressionInfo(expression: EXPRESSION_EXPRESSION, name: EXPRESSION_NAME)
		snCommonConfigurationService.getFacetExpressions(INDEX_TYPE_CODE) >> List.of(expressionInfo)

		when:
		List<AsExpressionData> resultExpressions = snAsSearchProvider.getSupportedFacetExpressions(INDEX_TYPE_CODE)

		then:
		resultExpressions != null
		resultExpressions.size() == 1

		with(resultExpressions[0]) {
			expression == EXPRESSION_EXPRESSION
			name == EXPRESSION_NAME_EN
		}
	}

	@Test
	def "Is valid facet expression"() {
		given:
		snCommonConfigurationService.isValidFacetExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> true

		when:
		boolean result = snAsSearchProvider.isValidFacetExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION)

		then:
		result == true
	}

	@Test
	def "Is invalid facet expression"() {
		given:
		snCommonConfigurationService.isValidFacetExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> false

		when:
		boolean result = snAsSearchProvider.isValidFacetExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION)

		then:
		result == false
	}

	@Test
	def "Get empty supported sort expressions"() {
		given:
		snCommonConfigurationService.getSortExpressions(INDEX_TYPE_CODE) >> List.of()

		when:
		List<AsExpressionData> expressionInfo = snAsSearchProvider.getSupportedSortExpressions(INDEX_TYPE_CODE)

		then:
		expressionInfo != null
		expressionInfo.size() == 0
	}

	@Test
	def "Get supported sort expressions"() {
		given:
		SnExpressionInfo expressionInfo = new SnExpressionInfo(expression: EXPRESSION_EXPRESSION, name: EXPRESSION_NAME)
		snCommonConfigurationService.getSortExpressions(INDEX_TYPE_CODE) >> List.of(expressionInfo)

		when:
		List<AsExpressionData> resultExpressions = snAsSearchProvider.getSupportedSortExpressions(INDEX_TYPE_CODE)

		then:
		resultExpressions != null
		resultExpressions.size() == 1

		with(resultExpressions[0]) {
			expression == EXPRESSION_EXPRESSION
			name == EXPRESSION_NAME_EN
		}
	}

	@Test
	def "Is valid sort expression"() {
		given:
		snCommonConfigurationService.isValidSortExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> true

		when:
		boolean result = snAsSearchProvider.isValidSortExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION)

		then:
		result == true
	}

	@Test
	def "Is invalid sort expression"() {
		given:
		snCommonConfigurationService.isValidSortExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> false

		when:
		boolean result = snAsSearchProvider.isValidSortExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION)

		then:
		result == false
	}

	@Test
	def "Get empty supported group expressions"() {
		given:
		snCommonConfigurationService.getGroupExpressions(INDEX_TYPE_CODE) >> List.of()

		when:
		List<AsExpressionData> expressionInfo = snAsSearchProvider.getSupportedGroupExpressions(INDEX_TYPE_CODE)

		then:
		expressionInfo != null
		expressionInfo.size() == 0
	}

	@Test
	def "Get supported group expressions"() {
		given:
		SnExpressionInfo expressionInfo = new SnExpressionInfo(expression: EXPRESSION_EXPRESSION, name: EXPRESSION_NAME)
		snCommonConfigurationService.getGroupExpressions(INDEX_TYPE_CODE) >> List.of(expressionInfo)

		when:
		List<AsExpressionData> resultExpressions = snAsSearchProvider.getSupportedGroupExpressions(INDEX_TYPE_CODE)

		then:
		resultExpressions != null
		resultExpressions.size() == 1

		with(resultExpressions[0]) {
			expression == EXPRESSION_EXPRESSION
			name == EXPRESSION_NAME_EN
		}
	}

	@Test
	def "Is valid group expression"() {
		given:
		snCommonConfigurationService.isValidGroupExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> true

		when:
		boolean result = snAsSearchProvider.isValidGroupExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION)

		then:
		result == true
	}

	@Test
	def "Is invalid group expression"() {
		given:
		snCommonConfigurationService.isValidGroupExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> false

		when:
		boolean result = snAsSearchProvider.isValidGroupExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION)

		then:
		result == false
	}
}
