/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.product.UnitModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json

@IntegrationTest
class ODataFacadePatchLocalizedPersistenceIntegrationTest extends ServicelayerTransactionalSpockSpecification {
	private static final def SERVICE_NAME = 'LocalePathTest'
	private static final def PRODUCTS = 'Products'
	private static final def PRODUCT_CODE = "exampleProduct"
	private static final def CATALOG_ID = "Default"
	private static final def CATALOG_VERSION_VERSION = "Staged"
	private static final def UNIT = "pieces"
	private static final def SPANISH = new Locale("es")
	private static final def PRODUCT_NAME_EN = "product in english"
	private static final def PRODUCT_NAME_ES = "producto en español"
	private static final def PRODUCT_DESCRIPTION_EN = "description in english"
	private static final def PRODUCT_DESCRIPTION_ES = "descripción en español"
	private static final def PRODUCT_NAME_UPDATED_EN = "the product name has changed!!!"
	private static final def PRODUCT_NAME_UPDATED_ES = "el nombre del producto ha cambiado!!!"
	private static final def PRODUCT_DESCRIPTION_UPDATED_EN = "and the description too!"
	private static final def PRODUCT_DESCRIPTION_UPDATED_ES = "y la descripción también!"
	private static final def INTEGRATION_KEY = "Staged|Default|" + PRODUCT_CODE

	@Resource(name = 'defaultODataFacade')
	private ODataFacade facade

	def setup() {
		// create test integration object
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true];",
				"; $SERVICE_NAME",
				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique=true]; code[unique = true]; type(code)",
				"; $SERVICE_NAME ; Product        ; Product",
				"; $SERVICE_NAME ; Catalog        ; Catalog",
				"; $SERVICE_NAME ; CatalogVersion ; CatalogVersion",
				"; $SERVICE_NAME ; Unit 		  ; Unit",
				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]",
				"; $SERVICE_NAME:Product        ; code              ; Product:code                	;									",
				"; $SERVICE_NAME:Product        ; name              ; Product:name                  ;									",
				"; $SERVICE_NAME:Product        ; description       ; Product:description           ;									",
				"; $SERVICE_NAME:Product        ; catalogVersion    ; Product:catalogVersion        ; 	$SERVICE_NAME:CatalogVersion	",
				"; $SERVICE_NAME:Product        ; unit			    ; Product:unit			        ; 	$SERVICE_NAME:Unit				",
				"; $SERVICE_NAME:Catalog        ; id                ; Catalog:id                    ;									",
				"; $SERVICE_NAME:CatalogVersion ; catalog           ; CatalogVersion:catalog        ; 	$SERVICE_NAME:Catalog			",
				"; $SERVICE_NAME:CatalogVersion ; version           ; CatalogVersion:version        ;									",
				"; $SERVICE_NAME:CatalogVersion ; version           ; CatalogVersion:version        ;									",
				"; $SERVICE_NAME:Unit           ; code              ; Unit:code                    	;									",
				"; $SERVICE_NAME:Unit           ; name              ; Unit:name                   	;									",
				"; $SERVICE_NAME:Unit           ; unitType          ; Unit:unitType                	;									",
				"INSERT_UPDATE Catalog;id[unique=true];name[lang=en];defaultCatalog;",
				";$CATALOG_ID;$CATALOG_ID;true",
				"INSERT_UPDATE CatalogVersion; catalog(id)[unique=true]; version[unique=true];active;",
				";$CATALOG_ID;$CATALOG_VERSION_VERSION;true",
				'INSERT_UPDATE Unit; code[unique = true]; unitType',
				"                  ; $UNIT              ; $UNIT",
				"INSERT_UPDATE Language;isocode[unique=true];name",
				";es;Spanish",
				"INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); name[lang = en]; name[lang = es]; description[lang = en]; description[lang = es]; unit(code)",
				"; $PRODUCT_CODE ; $CATALOG_ID:$CATALOG_VERSION_VERSION ; $PRODUCT_NAME_EN ; $PRODUCT_NAME_ES;  $PRODUCT_DESCRIPTION_EN ; $PRODUCT_DESCRIPTION_ES	; $UNIT")
	}

	def cleanup() {
		IntegrationTestUtil.removeAll IntegrationObjectModel
		IntegrationTestUtil.removeAll ProductModel
		IntegrationTestUtil.removeAll UnitModel
		IntegrationTestUtil.remove LanguageModel, { it.isocode != 'en' }
	}

	@Test
	def 'all localized values are removed when no content language header is sent, no localized attributes are present in the payload and the value sent is null'() {
		given: 'a patch request with null localized attribute is sent'
		def content = json()
				.withField('name', PRODUCT_NAME_UPDATED_ES)
				.withField('description', null)

		when:
		def response = facade.handleRequest patch(INTEGRATION_KEY, content, null)

		then: 'the response is successful'
		response.status == HttpStatusCodes.OK
		and: 'the localized values for description are removed but the name remains'
		getPersistedProduct().getProperty("name", Locale.ENGLISH) == PRODUCT_NAME_UPDATED_ES
		getPersistedProduct().getProperty("name", SPANISH) == null
		getPersistedProduct().getProperty("description", Locale.ENGLISH) == null
		getPersistedProduct().getProperty("description", SPANISH) == null
		getPersistedProduct().unit.code == UNIT
	}

	@Test
	def 'existing localized value is updated when content language header is sent, no localized attributes are present in the payload and the value sent is not null'() {
		given: 'a patch request with localized attribute values is sent'
		def content = json()
				.withField('name', PRODUCT_NAME_UPDATED_ES)
				.withField('description', PRODUCT_DESCRIPTION_UPDATED_ES)

		when: 'the content language header is spanish'
		def response = facade.handleRequest patch(INTEGRATION_KEY, content, SPANISH)

		then: 'the response is successful'
		response.status == HttpStatusCodes.OK
		and: 'the localized value for content language is updated'
		getPersistedProduct().getProperty("name", Locale.ENGLISH) == PRODUCT_NAME_EN
		getPersistedProduct().getProperty("name", SPANISH) == PRODUCT_NAME_UPDATED_ES
		getPersistedProduct().getProperty("description", Locale.ENGLISH) == PRODUCT_DESCRIPTION_EN
		getPersistedProduct().getProperty("description", SPANISH) == PRODUCT_DESCRIPTION_UPDATED_ES
	}

	@Test
	def 'non-existing localized value is created when content language header is sent, no localized attributes are present in the payload and a value is sent'() {
		given: 'product does not have any localized attribute values'
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); name[lang = en]; name[lang = es]; description[lang = en]; description[lang = es]",
				"; $PRODUCT_CODE ; Default:Staged ;  ; ; ; "
		)
		and: 'a patch request with a localized attribute value is sent'
		def content = json()
				.withField('description', PRODUCT_DESCRIPTION_UPDATED_ES)

		when: 'the content language header is spanish'
		def response = facade.handleRequest patch(INTEGRATION_KEY, content, SPANISH)

		then: 'the response is successful'
		response.status == HttpStatusCodes.OK
		and: 'the localized value for content language is updated'
		getPersistedProduct().getProperty("name", Locale.ENGLISH) == null
		getPersistedProduct().getProperty("name", SPANISH) == null
		getPersistedProduct().getProperty("description", Locale.ENGLISH) == null
		getPersistedProduct().getProperty("description", SPANISH) == PRODUCT_DESCRIPTION_UPDATED_ES
	}

	@Test
	def 'non-existing localized values are created when localized attributes are present in the payload, and the provided attribute values are created for the language in the header'() {
		given: 'product does not have any localized attribute values'
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); name[lang = en]; name[lang = es]; description[lang = en]; description[lang = es]",
				"; $PRODUCT_CODE ; Default:Staged ;  ; ; ; "
		)
		and: 'a patch request with a localized attribute value is sent'
		def content = json()
				.withField('description', PRODUCT_DESCRIPTION_UPDATED_ES)
				.withLocalizedAttributes(
						[language: 'en', name: PRODUCT_NAME_UPDATED_EN],
						[language: 'es', name: PRODUCT_NAME_UPDATED_ES])

		when: 'the content language header is spanish'
		def response = facade.handleRequest patch(INTEGRATION_KEY, content, Locale.ENGLISH)

		then: 'the response is successful'
		response.status == HttpStatusCodes.OK
		and: 'the localized value for content language is updated'
		getPersistedProduct().getProperty("name", Locale.ENGLISH) == PRODUCT_NAME_UPDATED_EN
		getPersistedProduct().getProperty("name", SPANISH) == PRODUCT_NAME_UPDATED_ES
		getPersistedProduct().getProperty("description", Locale.ENGLISH) == PRODUCT_DESCRIPTION_UPDATED_ES
		getPersistedProduct().getProperty("description", SPANISH) == null
	}

	@Test
	@Unroll
	def 'localized attributes are updated with the localized attributes provided in the payload, content header is #languageHeader and localized attribute value is #localizedAttributeValue'() {
		given: 'a patch request with localized attribute is sent'
		def content = json()
				.withField('description', localizedAttributeValue)
				.withLocalizedAttributes(
						[language: 'en', name: PRODUCT_NAME_UPDATED_EN, description: PRODUCT_DESCRIPTION_UPDATED_EN])

		when: 'the content language header is #languageHeader'
		def response = facade.handleRequest patch(INTEGRATION_KEY, content, languageHeader)

		then: 'the response is successful'
		response.status == HttpStatusCodes.OK
		and: 'the localized value for content language is updated'
		getPersistedProduct().getProperty("name", Locale.ENGLISH) == PRODUCT_NAME_UPDATED_EN
		getPersistedProduct().getProperty("name", SPANISH) == null
		getPersistedProduct().getProperty("description", Locale.ENGLISH) == PRODUCT_DESCRIPTION_UPDATED_EN
		getPersistedProduct().getProperty("description", SPANISH) == null

		where:
		languageHeader | localizedAttributeValue
		null           | "this should not matter"
		SPANISH        | "this should not matter"
		null           | null
		SPANISH        | null
	}

	@Test
	def 'all localized data is cleared out when no content header is provided and the localized attributes value is an empty map'() {
		given: 'a patch request with localized attribute is sent'
		def content = json()
				.withLocalizedAttributes([:])

		when: 'the content language header is spanish'
		def response = facade.handleRequest patch(INTEGRATION_KEY, content, null)

		then: 'the response is successful'
		response.status == HttpStatusCodes.OK
		and: 'the localized value for content language is updated'
		getPersistedProduct().getProperty("name", Locale.ENGLISH) == null
		getPersistedProduct().getProperty("name", SPANISH) == null
		getPersistedProduct().getProperty("description", Locale.ENGLISH) == null
		getPersistedProduct().getProperty("description", SPANISH) == null
		getPersistedProduct().unit.code == UNIT
	}

	@Test
	@Unroll
	def 'the patch request is ignored when a null value is provided in the localizedAttributes value and the content language header is #languageHeader'() {
		given: 'a patch request with localized attribute is sent'
		def content = json()
				.withField("localizedAttributes", null)

		when: 'the content language header is #languageHeader'
		def response = facade.handleRequest patch(INTEGRATION_KEY, content, languageHeader)

		then: 'the response is successful'
		response.status == HttpStatusCodes.OK
		and: 'the localized value for content language is updated'
		getPersistedProduct().getProperty("name", Locale.ENGLISH) == PRODUCT_NAME_EN
		getPersistedProduct().getProperty("name", SPANISH) == PRODUCT_NAME_ES
		getPersistedProduct().getProperty("description", Locale.ENGLISH) == PRODUCT_DESCRIPTION_EN
		getPersistedProduct().getProperty("description", SPANISH) == PRODUCT_DESCRIPTION_ES

		where:
		languageHeader << [null, SPANISH]
	}

	@Test
	def 'all localized data for the content language provided is cleared out when the localized attributes value is an empty map'() {
		given: 'a patch request with localized attribute is sent'
		def content = json()
				.withLocalizedAttributes([:])

		when: 'the content language header is spanish'
		def response = facade.handleRequest patch(INTEGRATION_KEY, content, Locale.ENGLISH)

		then: 'the response is successful'
		response.status == HttpStatusCodes.OK
		and: 'the localized value for content language is updated'
		getPersistedProduct().getProperty("name", Locale.ENGLISH) == null
		getPersistedProduct().getProperty("name", SPANISH) == PRODUCT_NAME_ES
		getPersistedProduct().getProperty("description", Locale.ENGLISH) == null
		getPersistedProduct().getProperty("description", SPANISH) == PRODUCT_DESCRIPTION_ES
	}

	private static ProductModel getPersistedProduct() {
		IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT_CODE }).orElse(null)
	}

	ODataContext patch(String key, JsonBuilder body, Locale locale) {
		ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataPatchRequest()
				.withPathInfo(PathInfoBuilder.pathInfo()
						.withServiceName(SERVICE_NAME)
						.withEntitySet(PRODUCTS)
						.withEntityKeys(key))
				.withContentType('application/json')
				.withAccepts('application/json')
				.withContentLanguage(locale)
				.withBody(body)
	}
}
