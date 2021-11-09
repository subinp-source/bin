/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.product.UnitModel
import de.hybris.platform.core.model.test.TestItemModel
import de.hybris.platform.cronjob.model.TriggerModel
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel
import de.hybris.platform.impex.model.cronjob.ImpExImportJobModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2services.odata.asserts.ODataAssertions
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test

import javax.annotation.Resource
import java.text.SimpleDateFormat

import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.ERROR_CODE
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.PRODUCTS_ENTITYSET
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.oDataGetRequest
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.oDataPostRequest
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataReadEntityIntegrationTest extends ServicelayerSpockSpecification {
    private static final String IO = "GetIntegrationTestGroup1"

    private static final String PRODUCT_CODE_ENCODED = "testProduct001%7Cwith%7Cpipes"
    private static final String PRODUCT_CODE = "testProduct001|with|pipes"
    private static final String PRODUCT_INTEGRATION_KEY = "Staged|Default|" + PRODUCT_CODE_ENCODED

    private static final String CATALOG = "Default"
    private static final String VERSION = "Staged"

    @Resource
    private FlexibleSearchService flexibleSearchService
    @Resource
    private ODataContextGenerator oDataContextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setup() throws Exception {
        IntegrationTestUtil.importImpEx(
                '$io = integrationObject(code)',
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)    ; root[default = false]',
                "                                   ; $IO               ; Catalog            ; Catalog       ;",
                "                                   ; $IO               ; Product            ; Product       ; true",
                "                                   ; $IO               ; CatalogVersion     ; CatalogVersion;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor   ; $attributeType        ; unique[default = false]',
                "                                            ; $IO:Product         ; code                        ; Product:code          ;",
                "                                            ; $IO:Product         ; name                        ; Product:name          ;",
                "                                            ; $IO:Product         ; catalogVersion              ; Product:catalogVersion; $IO:CatalogVersion",
                "                                            ; $IO:Catalog         ; id                          ; Catalog:id            ;",
                "                                            ; $IO:CatalogVersion  ; catalog                     ; CatalogVersion:catalog; $IO:Catalog",
                "                                            ; $IO:CatalogVersion  ; version                     ; CatalogVersion:version;",
                "INSERT_UPDATE Language; isocode[unique = true]",
                "; fr"
        )

        givenProductsExist()
    }

    def cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll ImpExImportJobModel
        IntegrationTestUtil.removeAll ImpExImportCronJobModel
        IntegrationTestUtil.removeAll TriggerModel
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.removeAll UnitModel
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG }
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == VERSION }
    }

    def givenProductsExist(productCodes = [PRODUCT_CODE]) {
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;",
                "                     ; $CATALOG         ; $CATALOG       ; true",
                "INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;",
                "                            ; $CATALOG                  ; $VERSION              ; true"
        )
        for (String code : productCodes) {
            IntegrationTestUtil.importImpEx(
                    "INSERT_UPDATE Unit; code[unique = true]; unitType[unique=true]",
                    "                  ; pieces      ; Pieces",
                    "INSERT_UPDATE Product; code[unique = true] ; catalogVersion(catalog(id), version); name[lang = en]            ; name[lang = fr] ; contentUnit(code)",
                    "                     ; $code               ; $CATALOG:$VERSION                   ; en name for testProduct001 ; fr name for testProduct001 ; pieces"
            )
        }
    }

    @Test
    def "get entity returns existing product"() {
        given:
        final ODataRequest oDataRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, PRODUCT_INTEGRATION_KEY, Locale.FRANCE)
        final ODataContext context = oDataContext(oDataRequest)

        when:
        final ODataResponse oDataResponse = facade.handleRequest(context)

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .hasPathWithValue("d.code", PRODUCT_CODE)
                .hasPathWithValue("d.name", "fr name for testProduct001")
                .hasPathWithValue("d.integrationKey", PRODUCT_INTEGRATION_KEY)
    }

    @Test
    def "get entities navigation property when value exists"() {
        given:
        final ODataRequest oDataRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, Locale.FRANCE, "catalogVersion", PRODUCT_INTEGRATION_KEY)

        when:
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .hasPathWithValue("d.version", "Staged")
                .hasPathWithValue("d.integrationKey", "Staged|Default")
    }

    @Test
    def "get entities navigation property when property value is null"() {
        given:
        IntegrationTestUtil.importImpEx(
                '$io = integrationObject(code)',
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $IO               ; Unit               ; Unit          ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]   ; attributeName[unique = true]; $descriptor   ; $attributeType        ; unique[default = false]',
                "                                            ; $IO:Unit            ; code                        ; Unit:code          ;",
                "                                            ; $IO:Product         ; unit                        ; Product:unit          ; $IO:Unit"
        )
        final ODataRequest oDataRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, Locale.FRANCE, "unit", PRODUCT_INTEGRATION_KEY)

        when:
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.NOT_FOUND)
                .jsonBody()
                .hasPathWithValue(ERROR_CODE, "not_found")
    }

    @Test
    def "get entities navigation property when property is not defined"() {
        given:
        final ODataRequest oDataRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, Locale.FRANCE, "unit", PRODUCT_INTEGRATION_KEY)

        when:
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.NOT_FOUND)
                .jsonBody()
                .hasPathWithValue(ERROR_CODE, null)
                .hasPathWithValue("error.message.value", "Could not find property with name: \'unit\'.")
    }

    @Test
    def "get entity by integrationKey when integrationKey is made up of a single navigation property"() {
        given:
        final String integrationKey = "A-Test-ImpExImportCronJob"
        IntegrationTestUtil.importImpEx(
                '$io = integrationObject(code)',
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $IO               ; Trigger            ; Trigger   ;",
                "                                   ; $IO               ; ImpExImportCronJob ; ImpExImportCronJob   ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]   ; attributeName[unique = true]; $descriptor   ; $attributeType        ; unique[default = false]',
                "                                            ; $IO:ImpExImportCronJob ; code                        ; ImpExImportCronJob:code             ;",
                "                                            ; $IO:ImpExImportCronJob ; triggers                    ; ImpExImportCronJob:triggers; $IO:Trigger ; ",
                "                                            ; $IO:Trigger            ; cronExpression              ; Trigger:cronExpression             ; ;",
                "                                            ; $IO:Trigger            ; day                         ; Trigger:day             ;",
                "                                            ; $IO:Trigger            ; cronJob                     ; Trigger:cronJob; $IO:ImpExImportCronJob ;true",
                "INSERT_UPDATE ImpExImportJob; code[unique = true]",
                "; test-ImpExImportJob",

                "INSERT_UPDATE ImpExImportCronJob; code[unique = true]       ; job(code)",
                "; A-Test-ImpExImportCronJob ; test-ImpExImportJob",

                "INSERT_UPDATE Trigger; cronExpression[unique = true]; cronJob(code)[unique = true]; day",
                "                     ; 2 2 2 1/1 * ? *              ; $integrationKey             ; 101"
        )

        when:
        final ODataRequest oDataRequest = oDataGetRequest(IO, "Triggers", integrationKey, Locale.ENGLISH)
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .hasPathWithValue("d.integrationKey", integrationKey)
    }

    @Test
    def "get entity by integrationKey when integrationKey includes 1 simple & 2 navigation properties"() {
        given:
        IntegrationTestUtil.importImpEx(
                '$io = integrationObject(code)',
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)    ; root[default = false]',
                "                                   ; $IO               ; Unit               ; Unit ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor   ; $attributeType        ; unique[default = false]',
                "                                            ; $IO:Unit                                          ; code          ; Unit:code             ;",
                "                                            ; $IO:Product                                       ; contentUnit; Product:contentUnit; $IO:Unit ; true"
        )
        final String integrationKey = "Staged|Default|" + PRODUCT_CODE_ENCODED + "|pieces"

        when:
        final ODataRequest oDataRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, integrationKey, Locale.FRANCE)
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .hasPathWithValue("d.code", PRODUCT_CODE)
                .hasPathWithValue("d.name", "fr name for testProduct001")
                .hasPathWithValue("d.integrationKey", integrationKey)
    }

    @Test
    def "get entity with empty integrationKey returns expected error response"() {
        given:
        final String integrationKey = "''"

        when:
        final ODataRequest oDataRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, integrationKey, Locale.FRANCE)
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
                .jsonBody()
                .hasPathWithValue(ERROR_CODE, "invalid_key")
    }

    @Test
    def "get entity with malformed key returns expected error response"() {
        given:
        final String integrationKey = "Staged||Default|" + PRODUCT_CODE_ENCODED
        final ODataRequest oDataRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, integrationKey, Locale.FRANCE)

        when:
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
                .jsonBody()
                .hasPathWithValue(ERROR_CODE, "invalid_key")
    }

    @Test
    def "get entity returns content when searching by parent type"() {
        given:
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE ClassificationSystem; id[unique = true];",
                "; MyCatalog ;",
                "INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version          [unique = true];",
                "; MyCatalog ; MyCatalogVersion ;",
                "; MyCatalog ; MyCatalogVersion ;",
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "; MyCatalogVersionIntegrationObject",
                "INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)",
                "; MyCatalogVersionIntegrationObject ; CatalogVersion ; CatalogVersion",
                "; MyCatalogVersionIntegrationObject ; Catalog        ; Catalog",
                "INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
                "; MyCatalogVersionIntegrationObject:CatalogVersion ; catalog ; CatalogVersion:catalog ; MyCatalogVersionIntegrationObject:Catalog ; true ;",
                "; MyCatalogVersionIntegrationObject:CatalogVersion ; version ; CatalogVersion:version ;                                           ; true ;",
                "; MyCatalogVersionIntegrationObject:Catalog        ; id      ; Catalog:id             ;                                           ; true ;");

        when:
        final ODataRequest oDataRequest = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName("MyCatalogVersionIntegrationObject")
                        .withEntitySet("CatalogVersions"))
                .build()
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .hasPathWithValueContaining("d.results[*].integrationKey", "MyCatalogVersion|MyCatalog")
    }

    @Test
    def "get entity returns invalid key when key is empty"() {
        when:
        final ODataRequest oDataRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, "", Locale.FRANCE)
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
                .jsonBody()
                .hasPathWithValue(ERROR_CODE, "invalid_key")
    }

    @Test
    def "get entity returns invalid key when more than 1 key is provided"() {
        when:
        final String keyPredicate = "integrationKey='abc',keyTwo='def'"

        final ODataRequest oDataRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, Locale.FRANCE, "", "integrationKey=abc", "keyTwo=def")
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
                .jsonBody()
                .hasPathWithValue("error.message.value", "Invalid key predicate: '" + keyPredicate + "'.")
    }

    @Test
    def "get entity returns not_found when item with matching integrationKey does not exist"() {
        when:
        final String testModelCode = "testProduct001_DOES_NOT_EXIST"
        final String integrationKey = "Staged|Default|" + testModelCode

        final ODataRequest oDataRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, integrationKey, Locale.FRANCE)
        final ODataResponse oDataResponse = facade.handleRequest(oDataContext(oDataRequest))

        then:
        ODataAssertions.assertThat(oDataResponse)
                .hasStatus(HttpStatusCodes.NOT_FOUND)
                .jsonBody()
                .hasPathWithValue(ERROR_CODE, "not_found")
    }

    @Test
    def "round trip for product"() {
        given:
        final String productCode = "testProduct201|with|pipes"
        final String productCodeEncoded = URLEncoder.encode(productCode, "UTF-8")
        final String name = "myTest"
        final String expectedIntegrationKey = "Staged|Default|" + productCodeEncoded

        final String productContent = productWithCodeAndName(productCode, name)

        when:
        final ODataRequest postRequest = oDataPostRequest(IO, PRODUCTS_ENTITYSET, productContent, Locale.ENGLISH, APPLICATION_JSON_VALUE)
        final ODataResponse oDataPostResponse = facade.handleRequest(oDataContext(postRequest))

        then:
        ODataAssertions.assertThat(oDataPostResponse)
                .hasStatus(HttpStatusCodes.CREATED)
                .jsonBody()
                .hasPathWithValue("d.code", productCode)
                .hasPathWithValue("d.integrationKey", expectedIntegrationKey)

        and:
        final ProductModel productModel = new ProductModel()
        productModel.setCode(productCode)
        final ProductModel persistedProductModel = flexibleSearchService.getModelByExample(productModel)
        persistedProductModel.getName(Locale.ENGLISH) == name

        when:
        final ODataRequest getRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, expectedIntegrationKey, Locale.ENGLISH)
        final ODataResponse oDataGetResponse = facade.handleRequest(oDataContext(getRequest))

        then:
        ODataAssertions.assertThat(oDataGetResponse)
                .isSuccessful()
                .jsonBody()
                .hasPathWithValue("d.code", productCode)
                .hasPathWithValue("d.name", name)
                .hasPathWithValue("d.integrationKey", expectedIntegrationKey)
    }

    @Test
    def "round trip for item with enum type attribute"() {
        given:
        approvalStatusIsDefinedOnTheIO()
        final String productCode = "testProduct201|with|pipes"
        final String expectedIntegrationKey = "Staged|Default|" + URLEncoder.encode(productCode, "UTF-8")

        final String productContent = productWithApprovalStatus("check")

        when:
        final ODataRequest postRequest = oDataPostRequest(IO, PRODUCTS_ENTITYSET, productContent, Locale.FRANCE, APPLICATION_JSON_VALUE)
        final ODataResponse oDataPostResponse = facade.handleRequest(oDataContext(postRequest))

        then:
        ODataAssertions.assertThat(oDataPostResponse)
                .hasStatus(HttpStatusCodes.CREATED)
                .jsonBody()
                .hasPath("d.approvalStatus")

        and:
        final ODataRequest getRequest = oDataGetRequest(IO, PRODUCTS_ENTITYSET, Locale.FRANCE, "approvalStatus", expectedIntegrationKey)

        when:
        final ODataResponse oDataGetResponse = facade.handleRequest(oDataContext(getRequest))

        then:
        ODataAssertions.assertThat(oDataGetResponse)
                .isSuccessful()
                .jsonBody()
                .hasPathWithValue("d.code", "check")
                .hasPathWithValue("d.integrationKey", "check")
    }

    @Test
    def "persist product with invalid enum type value throws error"() {
        given:
        approvalStatusIsDefinedOnTheIO()
        final String productContent = productWithApprovalStatus("nonExistantApprovalStatusEnumValue")

        when:
        final ODataRequest postRequest = oDataPostRequest(IO, PRODUCTS_ENTITYSET, productContent, Locale.FRANCE, APPLICATION_JSON_VALUE)
        final ODataResponse oDataPostResponse = facade.handleRequest(oDataContext(postRequest))

        then:
        ODataAssertions.assertThat(oDataPostResponse)
                .hasStatus(HttpStatusCodes.BAD_REQUEST)
                .jsonBody()
                .hasPathWithValue(ERROR_CODE, "missing_nav_property")
    }

    @Test
    def "get entity with all possible primitive attribute types"() {
        given:
        IntegrationTestUtil.importImpEx(
                '$io = integrationObject(code)',
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; TestObject",
                'INSERT_UPDATE IntegrationObjectItem; $io[unique=true]; code[unique = true]; type(code)',
                "                                   ; TestObject      ; TestItem           ; TestItem",
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor            ; unique[default = false]',
                "                                            ; TestObject:TestItem ; string                      ; TestItem:string        ; true",
                "                                            ; TestObject:TestItem ; boolean                     ; TestItem:boolean       ;",
                "                                            ; TestObject:TestItem ; primitiveShort              ; TestItem:primitiveShort;",
                "                                            ; TestObject:TestItem ; character                   ; TestItem:character     ;",
                "                                            ; TestObject:TestItem ; integer                     ; TestItem:integer       ;",
                "                                            ; TestObject:TestItem ; byte                        ; TestItem:byte          ;",
                "                                            ; TestObject:TestItem ; long                        ; TestItem:long          ;",
                "                                            ; TestObject:TestItem ; float                       ; TestItem:float         ;",
                "                                            ; TestObject:TestItem ; double                      ; TestItem:double        ;",
                "                                            ; TestObject:TestItem ; date                        ; TestItem:date          ;",
                "INSERT_UPDATE TestItem; string[unique=true]; primitiveShort; character; integer; byte; long       ; boolean; float; double; date[dateformat='MM/dd/yyyy']",
                "                      ; myItem             ; 300           ; a        ; 50000  ; 20  ; 90000000000; true   ; 500  ; 4001  ; 11/25/2019")
        final Date d11_25_2019 = new SimpleDateFormat("MM/dd/yyyy").parse("11/25/2019")

        final ODataRequest request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName("TestObject")
                        .withEntitySet("TestItems"))
                .build()

        when:
        final ODataResponse response = facade.handleRequest(oDataContext(request))

        then:
        ODataAssertions.assertThat(response)
                .isSuccessful()
                .jsonBody()
                .hasPathWithValue("d.results[0].string", "myItem")
                .hasPathWithValue("d.results[0].primitiveShort", "300")
                .hasPathWithValue("d.results[0].character", "a")
                .hasPathWithValue("d.results[0].integer", "50000")
                .hasPathWithValue("d.results[0].byte", "20")
                .hasPathWithValue("d.results[0].long", "90000000000")
                .hasPathWithValue("d.results[0].boolean", "true")
                .hasPathWithValue("d.results[0].float", "500.0")
                .hasPathWithValue("d.results[0].double", "4001.0")
                .hasPathWithValue("d.results[0].date", "/Date(" + d11_25_2019.getTime() + ")/")

        cleanup:
        IntegrationTestUtil.removeAll TestItemModel
    }

    private static approvalStatusIsDefinedOnTheIO() {
        IntegrationTestUtil.importImpEx(
                '$io = integrationObject(code)',
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)    ; root[default = false]',
                "                                   ; $IO               ; ArticleApprovalStatus ; ArticleApprovalStatus ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]     ; attributeName[unique = true]; $descriptor               ; $attributeType        ; unique[default = false]',
                "                                            ; $IO:ArticleApprovalStatus; code                        ; ArticleApprovalStatus:code;",
                "                                            ; $IO:Product              ; approvalStatus              ; Product:approvalStatus    ; $IO:ArticleApprovalStatus ;"
        )
    }

    private ODataContext oDataContext(final ODataRequest oDataRequest) {
        return oDataContextGenerator.generate(oDataRequest)
    }

    private static String productWithCodeAndName(final String code, final String name) {
        return "{" +
                " \"code\": \"" + code + "\"," +
                " \"name\": \"" + name + "\"," +
                " \"catalogVersion\": {" +
                "  \"catalog\": {" +
                "   \"id\": \"Default\"" +
                "  }," +
                "  \"version\": \"Staged\"" +
                " }" +
                "}"
    }

    private static String productWithApprovalStatus(final String code) {
        return "{" +
                " \"code\": \"testProduct201|with|pipes\"," +
                " \"catalogVersion\": {" +
                "  \"catalog\": {" +
                "   \"id\": \"Default\"" +
                "  }," +
                "  \"version\": \"Staged\"" +
                " }," +
                " \"approvalStatus\": {" +
                "   \"code\": \"" + code + "\"" +
                "    }" +
                "}"
    }
}
