/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItem2Model
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json

@IntegrationTest
class ODataFacadePatchErrorsIntegrationTest extends ServicelayerTransactionalSpockSpecification {
	private static final def SERVICE_NAME = 'PatchErrorsTest'
	private static final def ENTITYSET = 'Products'
	private static final String PRODUCT_CODE = "prod1code"

	@Resource(name = 'defaultODataFacade')
	private ODataFacade facade

	def setup() {
		IntegrationTestUtil.importImpEx(
				'$item=integrationObjectItem(integrationObject(code), code)',
				'$name=attributeName',
				'$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
				'$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
				"INSERT_UPDATE IntegrationObject; code[unique = true];",
				"; $SERVICE_NAME",
				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique=true]; code[unique = true]; type(code) ;root[default=false]",
				"                                   ; $SERVICE_NAME                       ; Product        	   ; Product    ;true",
				"                                   ; $SERVICE_NAME                       ; Catalog        	   ; Catalog",
				"                                   ; $SERVICE_NAME                       ; CatalogVersion 	   ; CatalogVersion",
				"                                   ; $SERVICE_NAME                       ; Unit               ; Unit",
				'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]         ; $name[unique = true]  ; $attributeDescriptor              ; $attributeType; unique[default = false]',
				"                                            ; $SERVICE_NAME:Product        ; code                  ; Product:code                      ;",
				"                                            ; $SERVICE_NAME:Product        ; name                  ; Product:name                      ;",
				"                                            ; $SERVICE_NAME:Product        ; description           ; Product:description               ;",
				"                                            ; $SERVICE_NAME:Product        ; catalogVersion        ; Product:catalogVersion            ; $SERVICE_NAME:CatalogVersion",
				"                                            ; $SERVICE_NAME:Product        ; unit                  ; Product:unit                      ; $SERVICE_NAME:Unit",
				"                                            ; $SERVICE_NAME:Catalog        ; id                    ; Catalog:id                        ;",
				"                                            ; $SERVICE_NAME:Unit           ; code                  ; Unit:code                         ;",
				"                                            ; $SERVICE_NAME:Unit           ; unitType              ; Unit:unitType                     ;",
				"                                            ; $SERVICE_NAME:CatalogVersion ; catalog               ; CatalogVersion:catalog            ; $SERVICE_NAME:Catalog",
				"                                            ; $SERVICE_NAME:CatalogVersion ; version               ; CatalogVersion:version            ;",
				"                                            ; $SERVICE_NAME:CatalogVersion ; categorySystemName    ; CatalogVersion:categorySystemName ;",
				"INSERT_UPDATE Catalog;id[unique=true];name[lang=en];defaultCatalog;",
				"                     ;Default        ;Default      ;true",
				"INSERT_UPDATE CatalogVersion; catalog(id)[unique=true]; version[unique=true];active;",
				"                            ;Default                  ;Staged               ;true"
		)
	}

	def cleanup() {
		IntegrationTestUtil.removeAll IntegrationObjectModel
		IntegrationTestUtil.remove ProductModel, { it.code == PRODUCT_CODE }
	}

	@Test
	def 'no item with integrationKey exists'() {
		when: 'a patchProducts request is made to a entity set that does not have an item with a matching integration key'
		def response = facade.handleRequest patchProducts('Staged|Default|nonExistingProductCode', json().withField('name', ''))

		then: 'the expected error response details are returned'
		response.status == HttpStatusCodes.NOT_FOUND
		and:
		def json = JsonObject.createFrom response.entityAsStream
		json.getString('error.code') == 'not_found'
		json.getString('error.message.value').contains 'Product'
		json.getString('error.message.value').contains 'Staged|Default|nonExistingProductCode'
	}

	@Test
	def 'fail to replace referenced item when the item does not exist and cannot be created'() {
		given: 'a product exists'
		IntegrationTestUtil.importImpEx(
				'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); name',
				"                     ; $PRODUCT_CODE      ; Default:Staged                      ; product1"
		)
		and: 'the payload contains a unit that does not exist'
		def content = json()
				.withField('code', PRODUCT_CODE)
				.withField('catalogVersion', json()
						.withField('version', 'Staged')
						.withField('catalog', json()
								.withField('id', 'Default')))
				.withField('unit', json()
						.withField('code', 'unitCodeThatDoesNotExist')
						.withField('unitType', 'unitTypeThatDoesNotExist'))

		when: 'a patchProducts request is made for a product'
		def response = facade.handleRequest patchProducts("Staged|Default|$PRODUCT_CODE" , content)

		then: 'the expected error response details are returned'
		response.status == HttpStatusCodes.BAD_REQUEST
		and:
		def json = JsonObject.createFrom response.entityAsStream
		json.getString('error.code') == 'missing_nav_property'
		and:
		json.getString('error.message.value') == "Item referenced by attribute [unit] in [Product] item does not exist in the system. Cannot create referenced item for this attribute because it is not partof or autocreate for the item that it belongs to."
		and: 'integrationKey is shown in the response'
		json.getString('error.innererror') == "Staged|Default|$PRODUCT_CODE"
	}
	
	@Test
	def 'url key does not match key in request body'() {
		given: 'a product exists'
		IntegrationTestUtil.importImpEx(
				'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); name',
				"                     ; $PRODUCT_CODE      ; Default:Staged                      ; product1"
		)
		and: 'the payload has a different integrationKey'
		def content = json()
				.withField('code', 'DIFFERENT-PRODUCT-CODE')
				.withField('catalogVersion', json()
						.withField('version', 'Staged')
						.withField('catalog', json()
								.withField('id', 'Default')))
		
		when: 'a patchProducts request is made for a product'
		def response = facade.handleRequest patchProducts("Staged|Default|$PRODUCT_CODE" , content)

		then: 'the expected error response details are returned'
		response.status == HttpStatusCodes.BAD_REQUEST
		and:
		def json = JsonObject.createFrom response.entityAsStream
		json.getString('error.code') == 'invalid_key'
		json.getString('error.message.value') == "Key [Staged|Default|DIFFERENT-PRODUCT-CODE] in the payload does not match the key [Staged|Default|$PRODUCT_CODE] in the path"
	}
	
	@Test
	def 'cannot set required non-key simple property to null'() {
		given: 'TestIntegrationItem2 with requiredName is defined'
		final String itemCode = 'keyCode'
		IntegrationTestUtil.importImpEx(
				'$item=integrationObjectItem(integrationObject(code), code)',
				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique=true]; code[unique = true]  ; type(code)",
				"                                   ; $SERVICE_NAME                       ; TestIntegrationItem2 ; TestIntegrationItem2",
				'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]                ; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
				"                                            ; $SERVICE_NAME:TestIntegrationItem2  ; code                        ; TestIntegrationItem2:code",
				"                                            ; $SERVICE_NAME:TestIntegrationItem2  ; requiredName                ; TestIntegrationItem2:requiredName",
				"                                            ; $SERVICE_NAME:TestIntegrationItem2  ; requiredStringMap           ; TestIntegrationItem2:requiredStringMap",
				"                                            ; $SERVICE_NAME:TestIntegrationItem2  ; optionalSimpleAttr          ; TestIntegrationItem2:optionalSimpleAttr"
		)

		and: 'a TestIntegrationItem2 item exists'
		IntegrationTestUtil.importImpEx(
				'INSERT_UPDATE TestIntegrationItem2; code[unique = true]; requiredName  ; optionalSimpleAttr; requiredStringMap(key, value)[map-delimiter = |]',
				"                                  ; $itemCode          ; nameNameValue ; simpleAttrValue   ; testKey->testVal"
		)

		and: 'the payload has a null value for a required non-key attribute'
		def content = json()
				.withField('code', itemCode)
				.withField('requiredName', null)

		when: 'a patch request is made for a TestIntegrationItem2'
		def response = facade.handleRequest patch(SERVICE_NAME, "TestIntegrationItem2s", itemCode , content)

		then: 'the expected error response details are returned'
		response.status == HttpStatusCodes.BAD_REQUEST
		and:
		def json = JsonObject.createFrom response.entityAsStream
		json.getString('error.code') == 'missing_property'
		and:
		json.getString('error.message.value') == "Property [requiredName] is required for EntityType [TestIntegrationItem2]."
		and: 'integrationKey is shown in the response'
		json.getString('error.innererror') == itemCode

        cleanup:
        IntegrationTestUtil.removeAll TestIntegrationItem2Model
	}

	@Test
	def "fails to update item as root when a root item already exists on the same IO"() {
		given: 'IntegrationService IO exists'
		importData("/impex/essentialdata-integrationservices.impex", "UTF-8")
		and: 'the payload includes setting the root of CatalogVersion to true'
		final String itemKey = "CatalogVersion|$SERVICE_NAME"
		def content = json()
				.withField('root', true)
				.withField('integrationObject', json()
						.withField('code', SERVICE_NAME))
				.withField('code', 'CatalogVersion')

		when: 'a patch request is made for a IntegrationObjectItems'
		def response = facade.handleRequest patch('IntegrationService', 'IntegrationObjectItems', itemKey , content)

		then: 'the expected error response details are returned'
		response.status == HttpStatusCodes.BAD_REQUEST
		and:
		def json = JsonObject.createFrom response.entityAsStream
		json.getString('error.code') == 'invalid_attribute_value'
		and: 'integrationKey is shown in the response'
		json.getString('error.innererror') == "CatalogVersion|$SERVICE_NAME"
	}

	private static ODataContext patchProducts(String key, JsonBuilder body) {
		patch(SERVICE_NAME, ENTITYSET, key, body)
	}

	private static ODataContext patch(String serviceName, String entitySet, String key, JsonBuilder body) {
		ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataPatchRequest()
				.withPathInfo(PathInfoBuilder.pathInfo()
						.withServiceName(serviceName)
						.withEntitySet(entitySet)
						.withEntityKeys(key))
				.withContentType('application/json')
				.withAccepts('application/json')
				.withBody(body)
	}
}
