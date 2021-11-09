/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.config.DefaultIntegrationServicesConfiguration
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.search.ItemTypeMatch
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class MultipleIntegrationObjectItemTypeMatchIntegrationTest extends ServicelayerSpockSpecification {
    private static final String ENTITY_SET = "Products"
    private static final String IO = "ComplexIntegrationObjectItemTypeMatchIntegrationTest"
    private static final String CATALOG_SUBTYPE_ID = "class1"
    private static final String CV_SUBTYPE_VERSION = "csv1"
    private static final String CATALOG_VERSION_SUBTYPE = "$CATALOG_SUBTYPE_ID:$CV_SUBTYPE_VERSION"

    @Resource(name = "defaultODataFacade")
    private ODataFacade facade
    @Resource(name = "defaultIntegrationServicesConfiguration")
    private DefaultIntegrationServicesConfiguration configurationService

    def setupSpec() {
        IntegrationTestUtil.importImpEx(
                '$ioHeader = integrationObject(code)',
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; $ioHeader[unique = true]; code[unique = true]; type(code)    ; itemTypeMatch(code)',
                "                                   ; $IO                       ; Product          ; Product       ; ALL_SUBTYPES",
                "                                   ; $IO                       ; CatalogVersion   ; CatalogVersion",
                "                                   ; $IO                       ; Catalog          ; Catalog       ",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor           ; $attributeType ; unique[default = false]; autoCreate[default = false]',
                "                                            ; $IO:Catalog         ; id                          ; Catalog:id",
                "                                            ; $IO:CatalogVersion  ; catalog                     ; CatalogVersion:catalog; $IO:Catalog",
                "                                            ; $IO:CatalogVersion  ; version                     ; CatalogVersion:version",
                "                                            ; $IO:Product         ; code                        ; Product:code",
                "                                            ; $IO:Product         ; catalogVersion              ; Product:catalogVersion; $IO:CatalogVersion",

                // Create an instance of ClassificationSystemVersion that extends -> CatalogVersion
                "\$classSysId = $CATALOG_SUBTYPE_ID",
                "\$classSysVersion = $CV_SUBTYPE_VERSION",
                'INSERT_UPDATE ClassificationSystem; id[unique = true]',
                "                                  ; $CATALOG_SUBTYPE_ID",
                'INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true] ; active',
                "                                         ; $CATALOG_SUBTYPE_ID       ; $CV_SUBTYPE_VERSION    ; false"
        )
    }

    def cleanup() {
        setItemTypeMatchForType('Product', ItemTypeMatch.ALL_SUBTYPES)
        //setItemTypeMatch for CatalogVersion to null
        IntegrationTestUtil.importImpEx(
                '$ioHeader = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $ioHeader[unique = true]; code[unique = true]; type(code)    ; itemTypeMatch',
                "                                   ; $IO                       ; CatalogVersion   ; CatalogVersion;"
        )
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.remove ClassificationSystemModel, { it.id == CATALOG_SUBTYPE_ID }
        IntegrationTestUtil.remove ClassificationSystemVersionModel, { it.version == CV_SUBTYPE_VERSION }
    }

    @Test
    @Unroll
    def "get all Product #msg ignores referenced attributes IntegrationObjectItem.itemTypeMatch"() {
        given: "A IOI for CatalogVersion type with itemTypeMatch set to RESTRICT_TO_ITEM_TYPE"
        setItemTypeMatchForType('CatalogVersion', ItemTypeMatch.RESTRICT_TO_ITEM_TYPE)
        and: "A Product with a catalogVersion referencing an instance of a subtype of CatalogVersion (ClassificationSystemVersion)"
        def productCode = 'prodCode1'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "                     ; $productCode       ; $CATALOG_VERSION_SUBTYPE")

        when: "a get request is made to get all Products"
        final ODataRequestBuilder oDataRequest = getRequest(ENTITY_SET, PathInfoBuilder.pathInfo()
                .withServiceName(IO)
                .withEntitySet(ENTITY_SET))
                .withParameters(params)
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        oDataResponse.status == HttpStatusCodes.OK

        cleanup:
        IntegrationTestUtil.remove ProductModel, { it.code == productCode }

        where:
        msg                    | params
        "with no query params" | [:]
        "with filter"          | ['$filter': "catalogVersion/integrationKey eq 'class1|csv1'"]
    }

    @Test
    def "get Product by integrationKey returns not found when CatalogVersion IntegrationObjectItem.itemTypeMatch=RESTRICT_TO_ITEM_TYPE and Product.catalogVersion refers an existing subtype of CatalogVersion"() {
        given: "A IOI for CatalogVersion type with itemTypeMatch set to RESTRICT_TO_ITEM_TYPE"
        setItemTypeMatchForType('CatalogVersion', ItemTypeMatch.RESTRICT_TO_ITEM_TYPE)
        and: "A Product with a catalogVersion referencing an instance of a subtype of CatalogVersion (ClassificationSystemVersion)"
        def productCode = 'myProduct1'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "                     ; $productCode       ; $CATALOG_VERSION_SUBTYPE")

        when:
        final ODataRequestBuilder oDataRequest = getRequest(ENTITY_SET, PathInfoBuilder.pathInfo()
                .withServiceName(IO)
                .withEntitySet(ENTITY_SET)
                .withEntityKeys("$CV_SUBTYPE_VERSION|$CATALOG_SUBTYPE_ID|$productCode"))
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        oDataResponse.status == HttpStatusCodes.NOT_FOUND

        cleanup:
        IntegrationTestUtil.remove ProductModel, { it.code == productCode }
    }

    @Test
    def "fails to create a Product referencing a CatalogVersion subtype in the payload, for which integration object itemTypeMatch is set to RESTRICT_TO_ITEM_TYPE"() {
        given: "An IOI for CatalogVersion set to RESTRICT_TO_ITEM_TYPE"
        setItemTypeMatchForType('CatalogVersion', ItemTypeMatch.RESTRICT_TO_ITEM_TYPE)

        def productCode = 'myProduct1'
        final ODataRequestBuilder oDataRequest = productsPostRequest()
                .withBody(json()
                        .withField('code', productCode)
                        .withField('catalogVersion', json()
                                .withField('version', CV_SUBTYPE_VERSION)
                                .withField('catalog', json()
                                        .withField('id', CATALOG_SUBTYPE_ID))))


        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then: 'the expected error response details are returned'
        oDataResponse.status == HttpStatusCodes.BAD_REQUEST
        and:
        def json = JsonObject.createFrom oDataResponse.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        and:
        def errorMessage = json.getString('error.message.value')
        errorMessage.contains('catalogVersion')
        errorMessage.contains('Product')
    }

    @Test
    def "post new Product when referenced catalogVersion has an IntegrationObjectItem with a null itemTypeMatch uses configuration property integrationservices.flexsearch.itemtypematch"() {
        given: "A IOI for Product type with itemTypeMatch set to RESTRICT_TO_ITEM_TYPE differing from integrationservices.flexsearch.itemtypematch=ALL_SUB_AND_SUPER_TYPES"
        def defaultItemTypeSearch = configurationService.getItemTypeMatch()
        configurationService.setItemTypeMatch(ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES)

        setItemTypeMatchForType('Product', ItemTypeMatch.RESTRICT_TO_ITEM_TYPE)
        def productCode = 'myProduct1'
        final ODataRequestBuilder oDataRequest = productsPostRequest()
                .withBody(json()
                        .withField('code', productCode)
                        .withField('catalogVersion', json()
                                .withField('version', CV_SUBTYPE_VERSION)
                                .withField('catalog', json()
                                        .withField('id', CATALOG_SUBTYPE_ID))))

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        oDataResponse.status == HttpStatusCodes.CREATED

        cleanup:
        configurationService.setItemTypeMatch(defaultItemTypeSearch)
    }

    @Test
    def "fails to patch an item referencing a CatalogVersion subtype in the payload, for which integration object itemTypeMatch is set to RESTRICT_TO_ITEM_TYPE"() {
        given: "A IOI for CatalogVersion type with itemTypeMatch set to RESTRICT_TO_ITEM_TYPE"
        setItemTypeMatchForType('CatalogVersion', ItemTypeMatch.RESTRICT_TO_ITEM_TYPE)
        and: "A Product with a catalogVersion referencing an instance of a subtype of CatalogVersion (ClassificationSystemVersion)"
        def productCode = 'myProduct1'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "                     ; $productCode       ; $CATALOG_VERSION_SUBTYPE")

        final ODataRequestBuilder oDataRequest = patchRequest("$CV_SUBTYPE_VERSION|$CATALOG_SUBTYPE_ID|$productCode")
                .withBody(json()
                        .withField('code', productCode))

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        oDataResponse.status == HttpStatusCodes.NOT_FOUND
    }

    @Test
    def "fails to patch an item referencing a non-key Media subtype in the payload, for which integration object itemTypeMatch is set to RESTRICT_TO_ITEM_TYPE"() {
        // CatalogUnawareMedia extends Media
        given: "A IOI for Media type with itemTypeMatch set to RESTRICT_TO_ITEM_TYPE"
        IntegrationTestUtil.importImpEx(
                '$ioHeader = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $ioHeader[unique = true]; code[unique = true]; type(code); itemTypeMatch(code)',
                "                                   ; $IO                     ; Media              ; Media            ; RESTRICT_TO_ITEM_TYPE")
        and: "An IntegrationObjectItemAttribute definition for a Product.media referencing a Media type"
        IntegrationTestUtil.importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor      ; $attributeType ; unique[default = false]; autoCreate[default = false]',
                "                                            ; $IO:Product         ; media                       ; Product:picture  ; $IO:Media",
                "                                            ; $IO:Media           ; code                        ; Media:code       ; "

        )
        and: "A Product with a catalogVersion referencing an instance of a subtype of CatalogVersion (ClassificationSystemVersion)"
        def productCode = 'myProduct1'
        def mediaSubTypeCode = 'mediaCode'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "                     ; $productCode       ; $CATALOG_VERSION_SUBTYPE",
                "INSERT_UPDATE CatalogUnawareMedia; code[unique=true]",
                "                                 ; $mediaSubTypeCode"
        )

        final ODataRequestBuilder oDataRequest = patchRequest("$CV_SUBTYPE_VERSION|$CATALOG_SUBTYPE_ID|$productCode").withBody(json().withField('code', productCode).withField('media', json().withField('code', mediaSubTypeCode)))

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        oDataResponse.status == HttpStatusCodes.BAD_REQUEST
        and:
        def json = JsonObject.createFrom oDataResponse.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        and:
        def errorMessage = json.getString('error.message.value')
        errorMessage.contains('media')
        errorMessage.contains('Product')
    }

    @Test
    def "fails to delete a Product with CatalogVersion referencing a sub-type of CatalogVersion, when CatalogVersion integration object itemTypeMatch is set to RESTRICT_TO_ITEM_TYPE"() {
        given: "A IOI for CatalogVersion type with itemTypeMatch set to RESTRICT_TO_ITEM_TYPE"
        setItemTypeMatchForType('CatalogVersion', ItemTypeMatch.RESTRICT_TO_ITEM_TYPE)
        and: "A Product with a catalogVersion referencing an instance of a subtype of CatalogVersion (ClassificationSystemVersion)"
        def productCode = 'myProduct1'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "                     ; $productCode       ; $CATALOG_VERSION_SUBTYPE")

        final ODataRequestBuilder oDataRequest = deleteRequest("$CV_SUBTYPE_VERSION|$CATALOG_SUBTYPE_ID|$productCode")

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        oDataResponse.status == HttpStatusCodes.NOT_FOUND
    }

    private static setItemTypeMatchForType(final String itemType, final ItemTypeMatch itemTypeMatch) {
        def itemTypeMatchCode = itemTypeMatch.toString()
        IntegrationTestUtil.importImpEx(
                '$ioHeader = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $ioHeader[unique = true]; code[unique = true]; type(code); itemTypeMatch(code)',
                "                                   ; $IO                     ; $itemType          ; $itemType ; $itemTypeMatchCode")
    }

    private static ODataRequestBuilder deleteRequest(final String... keys) {
        ODataRequestBuilder.oDataDeleteRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO)
                        .withEntitySet(ENTITY_SET)
                        .withEntityKeys(keys))
    }

    private static ODataRequestBuilder productsPostRequest() {
        ODataFacadeTestUtils.postRequestBuilder(IO, ENTITY_SET, APPLICATION_JSON_VALUE)
    }

    private static ODataRequestBuilder patchRequest(final String... keys) {
        ODataRequestBuilder.oDataPatchRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withContentType(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO)
                        .withEntitySet(ENTITY_SET)
                        .withEntityKeys(keys))
    }

    def getRequest(final String entitySetName, final PathInfoBuilder pathInfo = PathInfoBuilder.pathInfo()
            .withServiceName(IO)
            .withEntitySet(entitySetName)) {
        ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(pathInfo)
    }

    def handleRequest(final ODataRequestBuilder builder) {
        ODataFacadeTestUtils.handleRequest(facade, builder.build())
    }
}