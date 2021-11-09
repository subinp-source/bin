/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.admin.service

import static de.hybris.platform.searchservices.support.util.ObjectUtils.matchContains

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.core.model.user.UserModel
import de.hybris.platform.searchservices.admin.data.SnExpressionInfo
import de.hybris.platform.searchservices.admin.service.SnCommonConfigurationService
import de.hybris.platform.searchservices.constants.SearchservicesConstants
import de.hybris.platform.searchservices.enums.SnFieldType
import de.hybris.platform.searchservices.integration.admin.AbstractSnAdminSpec
import de.hybris.platform.searchservices.model.SnFieldModel
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel
import de.hybris.platform.searchservices.model.SnIndexTypeModel
import de.hybris.platform.servicelayer.i18n.CommonI18NService
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.type.TypeService
import de.hybris.platform.servicelayer.user.UserService

import java.nio.charset.StandardCharsets

import javax.annotation.Resource

import org.junit.Test

import spock.lang.Unroll


@IntegrationTest
public class SnCommonConfigurationServiceSpec extends AbstractSnAdminSpec {

	protected static final String USER_UID = "user"

	protected static final String LANGUAGE_1_ISOCODE = "language1"
	protected static final String LANGUAGE_2_ISOCODE = "language2"

	protected static final String CURRENCY_1_ISOCODE = "currency1"
	protected static final String CURRENCY_1_SYMBOL = "A"
	protected static final String CURRENCY_2_ISOCODE = "currency2"
	protected static final String CURRENCY_2_SYMBOL = "B"

	protected static final String CATALOG_1_ID = "catalog1"
	protected static final String CATALOG_2_ID = "catalog2"
	protected static final String CATALOG_VERSION_ONLINE = "Online"
	protected static final String CATALOG_VERSION_STAGED = "Staged"

	protected static final String FIELD_1_ID = "field1"
	protected static final String FIELD_1_NAME_EN = "field name1 (en)"
	protected static final String FIELD_1_NAME_DE = "field name1 (de)"

	protected static final String FIELD_2_ID = "field2"
	protected static final String FIELD_2_NAME_EN = "field name2 (en)"
	protected static final String FIELD_2_NAME_DE = "field name2 (de)"

	@Resource
	private TypeService typeService

	@Resource
	private ModelService modelService

	@Resource
	private UserService userService

	@Resource
	private CommonI18NService commonI18NService

	@Resource
	SnCommonConfigurationService snCommonConfigurationService

	private ComposedTypeModel itemComposedType

	def setup() {
		itemComposedType = typeService.getComposedTypeForClass(ProductModel.class)
		importData("/searchservices/test/integration/snLanguages.impex", StandardCharsets.UTF_8.name())
	}

	@Test
	def "Get default user"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		UserModel resultUser = snCommonConfigurationService.getUser(INDEX_TYPE_1_ID)

		then:
		userService.isAnonymousUser(resultUser)
	}

	def "Get user"() {
		UserModel user = new UserModel(uid: USER_UID)
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID, user: user)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		UserModel resultUser = snCommonConfigurationService.getUser(INDEX_TYPE_1_ID)

		then:
		resultUser == user
	}

	@Test
	def "Get default languages"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		List<LanguageModel> resultLanguages = snCommonConfigurationService.getLanguages(INDEX_TYPE_1_ID)

		then:
		resultLanguages != null
		resultLanguages.size() == 0
	}

	def "Get languages with single item"() {
		LanguageModel language = new LanguageModel(isocode: LANGUAGE_1_ISOCODE)
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID, languages: List.of(language))
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(language, indexConfiguration, indexType)

		when:
		List<LanguageModel> resultLanguages = snCommonConfigurationService.getLanguages(INDEX_TYPE_1_ID)

		then:
		resultLanguages != null
		resultLanguages.size() == 1

		resultLanguages[0].isocode == LANGUAGE_1_ISOCODE
	}

	def "Get languages with multiple items"() {
		LanguageModel language1 = new LanguageModel(isocode: LANGUAGE_1_ISOCODE)
		LanguageModel language2 = new LanguageModel(isocode: LANGUAGE_2_ISOCODE)
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID, languages: List.of(language1, language2))
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(language1, language2, indexConfiguration, indexType)

		when:
		List<LanguageModel> resultLanguages = snCommonConfigurationService.getLanguages(INDEX_TYPE_1_ID)

		then:
		resultLanguages != null
		resultLanguages.size() == 2

		resultLanguages[0].isocode == LANGUAGE_1_ISOCODE
		resultLanguages[1].isocode == LANGUAGE_2_ISOCODE
	}

	@Test
	def "Get default currencies"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		List<CurrencyModel> resultCurrencies = snCommonConfigurationService.getCurrencies(INDEX_TYPE_1_ID)

		then:
		resultCurrencies != null
		resultCurrencies.size() == 0
	}

	def "Get currencies with single item"() {
		CurrencyModel currency = new CurrencyModel(isocode: CURRENCY_1_ISOCODE, symbol: CURRENCY_1_SYMBOL)
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID, currencies: List.of(currency))
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(currency, indexConfiguration, indexType)

		when:
		List<CurrencyModel> resultCurrencies = snCommonConfigurationService.getCurrencies(INDEX_TYPE_1_ID)

		then:
		resultCurrencies != null
		resultCurrencies.size() == 1

		resultCurrencies[0].isocode == CURRENCY_1_ISOCODE
	}

	def "Get currencies with multiple items"() {
		CurrencyModel currency1 = new CurrencyModel(isocode: CURRENCY_1_ISOCODE, symbol: CURRENCY_1_SYMBOL)
		CurrencyModel currency2 = new CurrencyModel(isocode: CURRENCY_2_ISOCODE, symbol: CURRENCY_2_SYMBOL)
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID, currencies: List.of(currency1, currency2))
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(currency1, currency2, indexConfiguration, indexType)

		when:
		List<CurrencyModel> resultCurrencies = snCommonConfigurationService.getCurrencies(INDEX_TYPE_1_ID)

		then:
		resultCurrencies != null
		resultCurrencies.size() == 2

		resultCurrencies[0].isocode == CURRENCY_1_ISOCODE
		resultCurrencies[1].isocode == CURRENCY_2_ISOCODE
	}

	@Test
	def "Get default catalog versions"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		List<CatalogVersionModel> resultCatalogVersions = snCommonConfigurationService.getCatalogVersions(INDEX_TYPE_1_ID)

		then:
		resultCatalogVersions != null
		resultCatalogVersions.size() == 0
	}

	@Test
	def "Get catalog versions from catalog with single item"() {
		CatalogModel catalog = new CatalogModel(id: CATALOG_1_ID)
		CatalogVersionModel catalogVersion = new CatalogVersionModel(catalog: catalog, version: CATALOG_VERSION_ONLINE)
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration, catalogs: List.of(catalog))
		modelService.saveAll(catalog, catalogVersion, indexConfiguration, indexType)

		when:
		List<CatalogVersionModel> resultCatalogVersions = snCommonConfigurationService.getCatalogVersions(INDEX_TYPE_1_ID)

		then:
		resultCatalogVersions != null
		resultCatalogVersions.size() == 1

		resultCatalogVersions[0].catalog.id == CATALOG_1_ID
		resultCatalogVersions[0].version == CATALOG_VERSION_ONLINE
	}

	@Test
	def "Get catalog versions from catalog with multiple items"() {
		CatalogModel catalog = new CatalogModel(id: CATALOG_1_ID)
		CatalogVersionModel catalogVersion1 = new CatalogVersionModel(catalog: catalog, version: CATALOG_VERSION_ONLINE)
		CatalogVersionModel catalogVersion2 = new CatalogVersionModel(catalog: catalog, version: CATALOG_VERSION_STAGED)
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration, catalogs: List.of(catalog))
		modelService.saveAll(catalog, catalogVersion1, catalogVersion2, indexConfiguration, indexType)

		when:
		List<CatalogVersionModel> resultCatalogVersions = snCommonConfigurationService.getCatalogVersions(INDEX_TYPE_1_ID)

		then:
		resultCatalogVersions != null
		resultCatalogVersions.size() == 2

		resultCatalogVersions[0].catalog.id == CATALOG_1_ID
		resultCatalogVersions[0].version == CATALOG_VERSION_ONLINE

		resultCatalogVersions[1].catalog.id == CATALOG_1_ID
		resultCatalogVersions[1].version == CATALOG_VERSION_STAGED
	}

	@Test
	def "Get catalog versions from catalog versions with single item"() {
		CatalogModel catalog = new CatalogModel(id: CATALOG_1_ID)
		CatalogVersionModel catalogVersion = new CatalogVersionModel(catalog: catalog, version: CATALOG_VERSION_ONLINE)
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration, catalogVersions: List.of(catalogVersion))
		modelService.saveAll(catalog, catalogVersion, indexConfiguration, indexType)

		when:
		List<CatalogVersionModel> resultCatalogVersions = snCommonConfigurationService.getCatalogVersions(INDEX_TYPE_1_ID)

		then:
		resultCatalogVersions != null
		resultCatalogVersions.size() == 1

		resultCatalogVersions[0].catalog.id == CATALOG_1_ID
		resultCatalogVersions[0].version == CATALOG_VERSION_ONLINE
	}

	@Test
	def "Get catalog versions from catalog versions with multiple items"() {
		CatalogModel catalog = new CatalogModel(id: CATALOG_1_ID)
		CatalogVersionModel catalogVersion1 = new CatalogVersionModel(catalog: catalog, version: CATALOG_VERSION_ONLINE)
		CatalogVersionModel catalogVersion2 = new CatalogVersionModel(catalog: catalog, version: CATALOG_VERSION_STAGED)
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration, catalogVersions: List.of(catalogVersion1, catalogVersion2))
		modelService.saveAll(catalog, catalogVersion1, catalogVersion2, indexConfiguration, indexType)

		when:
		List<CatalogVersionModel> resultCatalogVersions = snCommonConfigurationService.getCatalogVersions(INDEX_TYPE_1_ID)

		then:
		resultCatalogVersions != null
		resultCatalogVersions.size() == 2

		resultCatalogVersions[0].catalog.id == CATALOG_1_ID
		resultCatalogVersions[0].version == CATALOG_VERSION_ONLINE

		resultCatalogVersions[1].catalog.id == CATALOG_1_ID
		resultCatalogVersions[1].version == CATALOG_VERSION_STAGED
	}

	@Test
	def "Get catalog versions with multiple items"() {
		CatalogModel catalog1 = new CatalogModel(id: CATALOG_1_ID)
		CatalogVersionModel catalogVersion1 = new CatalogVersionModel(catalog: catalog1, version: CATALOG_VERSION_ONLINE)
		CatalogModel catalog2 = new CatalogModel(id: CATALOG_2_ID)
		CatalogVersionModel catalogVersion2 = new CatalogVersionModel(catalog: catalog2, version: CATALOG_VERSION_STAGED)
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration, catalogs: List.of(catalog1), catalogVersions: List.of(catalogVersion2))
		modelService.saveAll(catalog1, catalogVersion1, catalog2, catalogVersion2, indexConfiguration, indexType)

		when:
		List<CatalogVersionModel> resultCatalogVersions = snCommonConfigurationService.getCatalogVersions(INDEX_TYPE_1_ID)

		then:
		resultCatalogVersions != null
		resultCatalogVersions.size() == 2

		resultCatalogVersions[0].catalog.id == CATALOG_1_ID
		resultCatalogVersions[0].version == CATALOG_VERSION_ONLINE

		resultCatalogVersions[1].catalog.id == CATALOG_2_ID
		resultCatalogVersions[1].version == CATALOG_VERSION_STAGED
	}

	@Test
	def "Get default facet expressions"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getFacetExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null

		resultExpressions.every {
			snCommonConfigurationService.isValidFacetExpression(INDEX_TYPE_1_ID, it.expression)
		}
	}

	def "Get facet expressions with single additional item"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.STRING)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getFacetExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null
		resultExpressions.size() >= 1

		resultExpressions.every {
			snCommonConfigurationService.isValidFacetExpression(INDEX_TYPE_1_ID, it.expression)
		}

		resultExpressions.any {
			it.expression == FIELD_1_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_1_NAME_EN,
					(LOCALE_DE): FIELD_1_NAME_DE
				])
		}
	}

	def "Get facet expressions with multiple additional items"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field1 = new SnFieldModel()
		field1.setIndexType(indexType)
		field1.setId(FIELD_1_ID)
		field1.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field1.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field1.setFieldType(SnFieldType.STRING)

		SnFieldModel field2 = new SnFieldModel()
		field2.setIndexType(indexType)
		field2.setId(FIELD_2_ID)
		field2.setName(FIELD_2_NAME_EN, LOCALE_EN)
		field2.setName(FIELD_2_NAME_DE, LOCALE_DE)
		field2.setFieldType(SnFieldType.STRING)

		modelService.saveAll(indexConfiguration, indexType, field1, field2)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getFacetExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null
		resultExpressions.size() >= 2

		resultExpressions.every {
			snCommonConfigurationService.isValidFacetExpression(INDEX_TYPE_1_ID, it.expression)
		}

		resultExpressions.any {
			it.expression == FIELD_1_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_1_NAME_EN,
					(LOCALE_DE): FIELD_1_NAME_DE
				])
		}

		resultExpressions.any {
			it.expression == FIELD_2_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_2_NAME_EN,
					(LOCALE_DE): FIELD_2_NAME_DE
				])
		}
	}

	def "Get facet expressions, filter by valid expression"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field1 = new SnFieldModel()
		field1.setIndexType(indexType)
		field1.setId(FIELD_1_ID)
		field1.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field1.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field1.setFieldType(SnFieldType.STRING)

		SnFieldModel field2 = new SnFieldModel()
		field2.setIndexType(indexType)
		field2.setId(FIELD_2_ID)
		field2.setName(FIELD_2_NAME_EN, LOCALE_EN)
		field2.setName(FIELD_2_NAME_DE, LOCALE_DE)
		field2.setFieldType(SnFieldType.TEXT)

		modelService.saveAll(indexConfiguration, indexType, field1, field2)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getFacetExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null
		resultExpressions.size() >= 1

		resultExpressions.every {
			snCommonConfigurationService.isValidFacetExpression(INDEX_TYPE_1_ID, it.expression)
		}

		resultExpressions.any {
			it.expression == FIELD_1_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_1_NAME_EN,
					(LOCALE_DE): FIELD_1_NAME_DE
				])
		}

		resultExpressions.any {
			!(it.expression == FIELD_2_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_2_NAME_EN,
					(LOCALE_DE): FIELD_2_NAME_DE
				]))
		}
	}

	def "Is valid facet expression"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.STRING)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		boolean result = snCommonConfigurationService.isValidFacetExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == true
	}

	def "Is not valid facet expression for non existing field"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		boolean result = snCommonConfigurationService.isValidFacetExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == false
	}

	def "Is not valid facet expression for wrong field type"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.TEXT)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		boolean result = snCommonConfigurationService.isValidFacetExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == false
	}

	@Unroll
	def "Is valid facet expression check for special expressions #testId: '#expression' -> '#expectedResult'"(testId, expression, expectedResult) {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		boolean result = snCommonConfigurationService.isValidFacetExpression(INDEX_TYPE_1_ID, expression)

		then:
		result == expectedResult

		where:
		testId | expression                                  || expectedResult
		1      | null                                        || false
		2      | SearchservicesConstants.ID_FIELD    || false
		3      | SearchservicesConstants.SCORE_FIELD || false
	}

	@Test
	def "Get default sort expressions"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getSortExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null

		resultExpressions.every {
			snCommonConfigurationService.isValidSortExpression(INDEX_TYPE_1_ID, it.expression)
		}

		resultExpressions.any {
			it.expression == SearchservicesConstants.ID_FIELD
		}

		resultExpressions.any {
			it.expression == SearchservicesConstants.SCORE_FIELD
		}
	}

	def "Get sort expressions with single additional item"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.STRING)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getSortExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null
		resultExpressions.size() >= 1

		resultExpressions.every {
			snCommonConfigurationService.isValidSortExpression(INDEX_TYPE_1_ID, it.expression)
		}

		resultExpressions.any {
			it.expression == SearchservicesConstants.ID_FIELD
		}

		resultExpressions.any {
			it.expression == SearchservicesConstants.SCORE_FIELD
		}

		resultExpressions.any {
			it.expression == FIELD_1_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_1_NAME_EN,
					(LOCALE_DE): FIELD_1_NAME_DE
				])
		}
	}

	def "Get sort expressions with multiple additional items"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field1 = new SnFieldModel()
		field1.setIndexType(indexType)
		field1.setId(FIELD_1_ID)
		field1.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field1.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field1.setFieldType(SnFieldType.STRING)

		SnFieldModel field2 = new SnFieldModel()
		field2.setIndexType(indexType)
		field2.setId(FIELD_2_ID)
		field2.setName(FIELD_2_NAME_EN, LOCALE_EN)
		field2.setName(FIELD_2_NAME_DE, LOCALE_DE)
		field2.setFieldType(SnFieldType.STRING)

		modelService.saveAll(indexConfiguration, indexType, field1, field2)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getSortExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null
		resultExpressions.size() >= 2

		resultExpressions.every {
			snCommonConfigurationService.isValidSortExpression(INDEX_TYPE_1_ID, it.expression)
		}

		resultExpressions.any {
			it.expression == SearchservicesConstants.ID_FIELD
		}

		resultExpressions.any {
			it.expression == SearchservicesConstants.SCORE_FIELD
		}

		resultExpressions.any {
			it.expression == FIELD_1_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_1_NAME_EN,
					(LOCALE_DE): FIELD_1_NAME_DE
				])
		}

		resultExpressions.any {
			it.expression == FIELD_2_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_2_NAME_EN,
					(LOCALE_DE): FIELD_2_NAME_DE
				])
		}
	}

	def "Get sort expressions, filter by valid expression"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field1 = new SnFieldModel()
		field1.setIndexType(indexType)
		field1.setId(FIELD_1_ID)
		field1.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field1.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field1.setFieldType(SnFieldType.STRING)

		SnFieldModel field2 = new SnFieldModel()
		field2.setIndexType(indexType)
		field2.setId(FIELD_2_ID)
		field2.setName(FIELD_2_NAME_EN, LOCALE_EN)
		field2.setName(FIELD_2_NAME_DE, LOCALE_DE)
		field2.setFieldType(SnFieldType.TEXT)

		modelService.saveAll(indexConfiguration, indexType, field1, field2)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getSortExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null
		resultExpressions.size() >= 1

		resultExpressions.every {
			snCommonConfigurationService.isValidSortExpression(INDEX_TYPE_1_ID, it.expression)
		}

		resultExpressions.any {
			it.expression == SearchservicesConstants.ID_FIELD
		}

		resultExpressions.any {
			it.expression == SearchservicesConstants.SCORE_FIELD
		}

		resultExpressions.any {
			it.expression == FIELD_1_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_1_NAME_EN,
					(LOCALE_DE): FIELD_1_NAME_DE
				])
		}

		resultExpressions.any {
			!(it.expression == FIELD_2_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_2_NAME_EN,
					(LOCALE_DE): FIELD_2_NAME_DE
				]))
		}
	}

	def "Is valid sort expression"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.STRING)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		boolean result = snCommonConfigurationService.isValidSortExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == true
	}

	def "Is not valid sort expression for non existing field"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		boolean result = snCommonConfigurationService.isValidSortExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == false
	}

	def "Is not valid sort expression for wrong field type"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.TEXT)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		boolean result = snCommonConfigurationService.isValidSortExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == false
	}

	def "Is not valid sort expression for multi-valued field"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.STRING)
		field.setMultiValued(true)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		boolean result = snCommonConfigurationService.isValidSortExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == false
	}

	@Unroll
	def "Is valid sort expression check for special expressions #testId: '#expression' -> '#expectedResult'"(testId, expression, expectedResult) {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		boolean result = snCommonConfigurationService.isValidSortExpression(INDEX_TYPE_1_ID, expression)

		then:
		result == expectedResult

		where:
		testId | expression                                  || expectedResult
		1      | null                                        || false
		2      | SearchservicesConstants.ID_FIELD    || true
		3      | SearchservicesConstants.SCORE_FIELD || true
	}

	@Test
	def "Get default group expressions"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getGroupExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null

		resultExpressions.every {
			snCommonConfigurationService.isValidGroupExpression(INDEX_TYPE_1_ID, it.expression)
		}
	}

	def "Get group expressions with single additional item"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.STRING)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getGroupExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null
		resultExpressions.size() >= 1

		resultExpressions.every {
			snCommonConfigurationService.isValidGroupExpression(INDEX_TYPE_1_ID, it.expression)
		}

		resultExpressions.any {
			it.expression == FIELD_1_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_1_NAME_EN,
					(LOCALE_DE): FIELD_1_NAME_DE
				])
		}
	}

	def "Get group expressions with multiple additional items"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field1 = new SnFieldModel()
		field1.setIndexType(indexType)
		field1.setId(FIELD_1_ID)
		field1.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field1.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field1.setFieldType(SnFieldType.STRING)

		SnFieldModel field2 = new SnFieldModel()
		field2.setIndexType(indexType)
		field2.setId(FIELD_2_ID)
		field2.setName(FIELD_2_NAME_EN, LOCALE_EN)
		field2.setName(FIELD_2_NAME_DE, LOCALE_DE)
		field2.setFieldType(SnFieldType.STRING)

		modelService.saveAll(indexConfiguration, indexType, field1, field2)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getGroupExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null
		resultExpressions.size() >= 2

		resultExpressions.every {
			snCommonConfigurationService.isValidGroupExpression(INDEX_TYPE_1_ID, it.expression)
		}

		resultExpressions.any {
			it.expression == FIELD_1_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_1_NAME_EN,
					(LOCALE_DE): FIELD_1_NAME_DE
				])
		}

		resultExpressions.any {
			it.expression == FIELD_2_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_2_NAME_EN,
					(LOCALE_DE): FIELD_2_NAME_DE
				])
		}
	}

	def "Get group expressions, filter by valid expression"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field1 = new SnFieldModel()
		field1.setIndexType(indexType)
		field1.setId(FIELD_1_ID)
		field1.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field1.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field1.setFieldType(SnFieldType.STRING)

		SnFieldModel field2 = new SnFieldModel()
		field2.setIndexType(indexType)
		field2.setId(FIELD_2_ID)
		field2.setName(FIELD_2_NAME_EN, LOCALE_EN)
		field2.setName(FIELD_2_NAME_DE, LOCALE_DE)
		field2.setFieldType(SnFieldType.TEXT)

		modelService.saveAll(indexConfiguration, indexType, field1, field2)

		when:
		List<SnExpressionInfo> resultExpressions = snCommonConfigurationService.getGroupExpressions(INDEX_TYPE_1_ID)

		then:
		resultExpressions != null
		resultExpressions.size() >= 1

		resultExpressions.every {
			snCommonConfigurationService.isValidGroupExpression(INDEX_TYPE_1_ID, it.expression)
		}

		resultExpressions.any {
			it.expression == FIELD_1_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_1_NAME_EN,
					(LOCALE_DE): FIELD_1_NAME_DE
				])
		}

		resultExpressions.any {
			!(it.expression == FIELD_2_ID &&
				matchContains(it.name, [
					(LOCALE_EN): FIELD_2_NAME_EN,
					(LOCALE_DE): FIELD_2_NAME_DE
				]))
		}
	}

	def "Is valid group expression"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.STRING)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		boolean result = snCommonConfigurationService.isValidGroupExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == true
	}

	def "Is not valid group expression for non existing field"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		boolean result = snCommonConfigurationService.isValidGroupExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == false
	}

	def "Is not valid group expression for wrong field type"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.TEXT)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		boolean result = snCommonConfigurationService.isValidGroupExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == false
	}

	def "Is not valid group expression for multi valued field"() {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)

		SnFieldModel field = new SnFieldModel()
		field.setIndexType(indexType)
		field.setId(FIELD_1_ID)
		field.setName(FIELD_1_NAME_EN, LOCALE_EN)
		field.setName(FIELD_1_NAME_DE, LOCALE_DE)
		field.setFieldType(SnFieldType.STRING)
		field.setMultiValued(true)

		modelService.saveAll(indexConfiguration, indexType, field)

		when:
		boolean result = snCommonConfigurationService.isValidGroupExpression(INDEX_TYPE_1_ID, FIELD_1_ID)

		then:
		result == false
	}

	@Unroll
	def "Is valid group expression check for special expressions #testId: '#expression' -> '#expectedResult'"(testId, expression, expectedResult) {
		SnIndexConfigurationModel indexConfiguration = new SnIndexConfigurationModel(id: INDEX_CONFIGURATION_ID)
		SnIndexTypeModel indexType = new SnIndexTypeModel(id: INDEX_TYPE_1_ID, itemComposedType: itemComposedType, indexConfiguration: indexConfiguration)
		modelService.saveAll(indexConfiguration, indexType)

		when:
		boolean result = snCommonConfigurationService.isValidGroupExpression(INDEX_TYPE_1_ID, expression)

		then:
		result == expectedResult

		where:
		testId | expression                                  || expectedResult
		1      | null                                        || false
		2      | SearchservicesConstants.ID_FIELD    || false
		3      | SearchservicesConstants.SCORE_FIELD || false
	}
}
