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
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.core.model.order.OrderEntryModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test

import javax.annotation.Resource

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataPrimitiveCollectionPersistenceFacadeIntegrationTest extends ServicelayerTransactionalSpockSpecification {

	def SERVICE_NAME = "PrimitiveCollection"
	def URL1 = "url1"
	def URL2 = "url2"
	def URL_PATTERNS = "urlPatterns"

	@Resource(name = 'oDataContextGenerator')
	private ODataContextGenerator contextGenerator
	@Resource(name = "defaultODataFacade")
	private ODataFacade facade

    def cleanup() {
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == 'PrimitiveCollection'}
    }

	@Test
	def "can persist and get a String collection"() {
		given: 'IntegrationObject is defined with attribute "urlPatterns" that is a collection of Strings'
		IntegrationTestUtil.importImpEx(
				'INSERT_UPDATE IntegrationObject; code[unique = true]',
				"                               ; $SERVICE_NAME      ",
				'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
				"                                   ; $SERVICE_NAME                         ; Catalog            ; Catalog",
				'$ioItem=integrationObjectItem(integrationObject(code), code)',
				'$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
				'INSERT_UPDATE IntegrationObjectItemAttribute; $ioItem[unique = true]; attributeName[unique = true]; $attributeDescriptor',
				"                                            ; $SERVICE_NAME:Catalog ; id                          ; Catalog:id",
				"                                            ; $SERVICE_NAME:Catalog ; urlPatterns                 ; Catalog:urlPatterns"
		)
		and: 'the payload contains two elements for the urlPatterns attribute'
        def catalogId = "testyCat"
		def body = 	JsonBuilder.json()
				.withId(catalogId)
				.withFieldValues(URL_PATTERNS,
						JsonBuilder.json().withField("value", URL1),
						JsonBuilder.json().withField("value", URL2))

		when:
		def postResponse = facade.handleRequest postRequest('Catalogs', body)
		def getResponse = facade.handleRequest getRequest('Catalogs', 'urlPatterns', catalogId)
		def getBody = extractBody getResponse as IntegrationODataResponse

		then:
		postResponse.getStatus() == HttpStatusCodes.CREATED
		getBody.getCollection("\$.d.results").size() == 2
		getBody.getCollectionOfObjects("d.results[*].value") == [URL1, URL2]

        cleanup:
        IntegrationTestUtil.removeSafely CatalogModel, {it.id == catalogId}
    }

	@Test
	def "can persist and get an Integer collection"() {
		given:
		IntegrationTestUtil.importImpEx(
				'INSERT_UPDATE IntegrationObject; code[unique = true]',
				"; $SERVICE_NAME",
				'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
				"; $SERVICE_NAME ; Order          ; Order",
				"; $SERVICE_NAME ; OrderEntry     ; OrderEntry",
				"; $SERVICE_NAME ; CatalogVersion ; CatalogVersion",
				"; $SERVICE_NAME ; Catalog        ; Catalog",
				"; $SERVICE_NAME ; Product        ; Product",
				"; $SERVICE_NAME ; Unit           ; Unit",
				"; $SERVICE_NAME ; Currency       ; Currency",
				"; $SERVICE_NAME ; User           ; User",

                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor; $refType; unique[default = false]; autoCreate[default = false]',
				"; $SERVICE_NAME:Order          ; code              ; Order:code                   ;",
				"; $SERVICE_NAME:Order          ; currency          ; Order:currency               ; $SERVICE_NAME:Currency",
				"; $SERVICE_NAME:Order          ; user              ; Order:user                   ; $SERVICE_NAME:User    ;",
				"; $SERVICE_NAME:Order          ; date              ; Order:date                   ;",

				"; $SERVICE_NAME:Currency       ; isocode           ; Currency:isocode             ;",

				"; $SERVICE_NAME:User           ; uid               ; User:uid                     ;                        ;",

				"; $SERVICE_NAME:Catalog        ; id                ; Catalog:id                   ;",

				"; $SERVICE_NAME:CatalogVersion ; catalog           ; CatalogVersion:catalog       ; $SERVICE_NAME:Catalog ",
				"; $SERVICE_NAME:CatalogVersion ; version           ; CatalogVersion:version       ;",

				"; $SERVICE_NAME:Product        ; code              ; Product:code                 ;",
				"; $SERVICE_NAME:Product        ; catalogVersion    ; Product:catalogVersion       ; $SERVICE_NAME:CatalogVersion",
				"; $SERVICE_NAME:Product        ; name              ; Product:name                 ;",

				"; $SERVICE_NAME:Unit           ; code              ; Unit:code                    ;",
				"; $SERVICE_NAME:Unit           ; name              ; Unit:name                    ;",
				"; $SERVICE_NAME:Unit           ; unitType          ; Unit:unitType                ;",

				"; $SERVICE_NAME:OrderEntry     ; entryGroupNumbers ; OrderEntry:entryGroupNumbers ;",
				"; $SERVICE_NAME:OrderEntry     ; entryNumber       ; OrderEntry:entryNumber       ;",
				"; $SERVICE_NAME:OrderEntry     ; quantity          ; OrderEntry:quantity          ;",
				"; $SERVICE_NAME:OrderEntry     ; order             ; OrderEntry:order             ; $SERVICE_NAME:Order   ;",
				"; $SERVICE_NAME:OrderEntry     ; product           ; OrderEntry:product           ; $SERVICE_NAME:Product ;",
				"; $SERVICE_NAME:OrderEntry     ; unit              ; OrderEntry:unit              ; $SERVICE_NAME:Unit    ;")

		IntegrationTestUtil.importImpEx(
				'$catalogVersion = Default:Staged',
				'INSERT_UPDATE Unit; code[unique = true]; name[lang = en]; unitType;',
				'; pieces ; pieces ; pieces',
				'INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;',
				'; Default ; Default ; true',
				'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;',
				'; Default ; Staged ; true',
				'INSERT_UPDATE Currency; isocode[unique = true]',
				'; EUR',
				'INSERT_UPDATE User; uid[unique = true]',
				'; anonymous',
				'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); name',
				'; prod1 ; $catalogVersion ; product1',
				'INSERT_UPDATE Order; code[unique = true]; currency(isocode); user(uid); date[dateformat=MM:dd:yyyy]',
				'; order1 ; EUR ; anonymous ; 08:27:1984')

		def body = integrationObjectWithIntegerCollection()

		when:
		def postResponse = facade.handleRequest postRequest('OrderEntries', body)
		def getResponse = facade.handleRequest getRequest('OrderEntries', 'entryGroupNumbers', '237|order1')
		def getBody = extractBody getResponse as IntegrationODataResponse

		then:
		postResponse.getStatus() == HttpStatusCodes.CREATED
		getBody.getCollection("\$.d.results").size() == 5
		getBody.getCollectionOfObjects("d.results[*].value").containsAll(11, 22, 33, 44, 55)

        cleanup:
        IntegrationTestUtil.removeAll OrderEntryModel
    }


	def integrationObjectWithIntegerCollection() {
		JsonBuilder.json()
				.withField("order", JsonBuilder.json().withCode("order1"))
				.withField("entryNumber", 237)
				.withField("product", JsonBuilder.json().withCode("prod1")
				.withField("catalogVersion", JsonBuilder.json()
				.withField("catalog", JsonBuilder.json().withId("Default"))
				.withField("version", "Staged")))
				.withField("unit", JsonBuilder.json()
				.withCode("pieces")
				.withField("name", "pieces")
				.withField("unitType", "pieces"))
				.withField("quantity", "25")
				.withFieldValues("entryGroupNumbers",
				JsonBuilder.json().withField("value", 11),
				JsonBuilder.json().withField("value", 22),
				JsonBuilder.json().withField("value", 33),
				JsonBuilder.json().withField("value", 44),
				JsonBuilder.json().withField("value", 55))
	}

	JsonObject extractBody(IntegrationODataResponse response) {
		JsonObject.createFrom response.entityAsStream
	}

	ODataContext postRequest(String entityType, JsonBuilder content) {
		ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataPostRequest()
				.withPathInfo(PathInfoBuilder.pathInfo()
						.withServiceName(SERVICE_NAME)
						.withEntitySet(entityType))
				.withContentType(APPLICATION_JSON_VALUE)
				.withAccepts(APPLICATION_JSON_VALUE)
				.withBody(content)
				.build()
	}

	ODataContext getRequest(String entityType, String segment, String... keys) {
		ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataGetRequest()
				.withPathInfo(PathInfoBuilder.pathInfo()
						.withServiceName(SERVICE_NAME)
						.withEntitySet(entityType)
						.withNavigationSegment(segment)
						.withEntityKeys(keys))
				.withAccepts(APPLICATION_JSON_VALUE)
				.build()
	}
}
