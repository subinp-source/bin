/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.integrationservices.util.VirtualAttributeBuilder
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2services.odata.ODataSchema
import de.hybris.platform.odata2services.odata.schema.SchemaGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.scripting.model.ScriptModel
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test

import javax.annotation.Resource

import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.CatalogIntegrationObjectItemPayloadBuilder.catalogIntegrationObjectItem
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.CatalogVersionIntegrationObjectItemPayloadBuilder.catalogVersionIntegrationObjectItem
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.IntegrationObjectItemVirtualAttributePayloadBuilder.virtualAttribute
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.IntegrationObjectPayloadBuilder.integrationObject
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.ProductIntegrationObjectItemPayloadBuilder.productIntegrationObjectItem
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.VirtualAttributeDescriptorPayloadBuilder.retrievalDescriptor
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class IntegrationServiceVirtualAttributesIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    private static final String IOI_CODE = 'MyProduct'
    private static final String PHONE_IO = 'IntegrationServiceVirtualAttributesIntegrationTestIO'
    private static final String SERVICE_NAME = 'IntegrationService'
    private static final String PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO = "ProductWithVirtualAttributesIO"
    private static final String CONTEXT_RETRIEVAL_DESCRIPTOR_CODE = 'testRetrievalDescriptor'
    private static final String CONTEXT_LOGIC_LOCATION = 'model://logLoc'
    private static final String CONTEXT_VIRTUAL_ATTRIBUTE_DESCRIPTOR_TYPE = 'java.lang.String'

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade
    @Resource(name = "oDataSchemaGenerator")
    private SchemaGenerator generator

    def setupSpec() {
        importCsv("/impex/essentialdata-integrationservices.impex", "UTF-8")
    }

    def setup() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $PHONE_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $PHONE_IO                             ; Product            ; Product",
                "                                   ; $PHONE_IO                             ; CatalogVersion     ; CatalogVersion",
                "                                   ; $PHONE_IO                             ; Catalog            ; Catalog",

                '$item = integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]     ; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $PHONE_IO:Catalog        ; id                          ; Catalog:id",
                "                                            ; $PHONE_IO:Product        ; code                        ; Product:code",
                "                                            ; $PHONE_IO:Product        ; catalogVersion              ; Product:catalogVersion                             ; $PHONE_IO:CatalogVersion",
                "                                            ; $PHONE_IO:CatalogVersion ; version                     ; CatalogVersion:version",
                "                                            ; $PHONE_IO:CatalogVersion ; catalog                     ; CatalogVersion:catalog                             ; $PHONE_IO:Catalog"
        )
    }

    def cleanup() {
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == PHONE_IO || it.code == PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO }
        IntegrationTestUtil.removeAll IntegrationObjectVirtualAttributeDescriptorModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == "IntegrationService" }
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == "IntegrationService" }
        IntegrationTestUtil.remove ScriptModel, { it.code == "logLoc" }
    }

    @Test
    def "GET IntegrationObjectItemVirtualAttributes returns virtual attributes for Product"() {
        given:
        def testVirtualAttributeName1 = 'testVirtualAttribute1'
        def testVirtualAttributeName2 = 'testVirtualAttribute2'
        virtualAttributesExistForProduct(testVirtualAttributeName1, testVirtualAttributeName2)
        and:
        def context = oDataGetContext('IntegrationObjectItemVirtualAttributes')

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].attributeName') == [testVirtualAttributeName1, testVirtualAttributeName2]
    }

    @Test
    def "GET IntegrationObjectVirtualAttributeDescriptors returns the default type when descriptor type was not provided"() {
        given:
        def testVirtualAttributeName1 = 'testVirtualAttribute1'
        virtualAttributeDescriptorWithNoType(CONTEXT_RETRIEVAL_DESCRIPTOR_CODE)
        virtualAttributeExistForProduct(testVirtualAttributeName1, CONTEXT_RETRIEVAL_DESCRIPTOR_CODE)
        and:
        def params = ['$expand': 'retrievalDescriptor/type']
        def context = oDataGetContext('IntegrationObjectItemVirtualAttributes', params)

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].retrievalDescriptor.type.code') == [CONTEXT_VIRTUAL_ATTRIBUTE_DESCRIPTOR_TYPE]
    }

    @Test
    def "VirtualAttributes can be expanded when making a GET request of IntegrationObjectItems"() {
        given:
        def testVirtualAttributeName1 = 'testVirtualAttribute1'
        virtualAttributeForProduct(testVirtualAttributeName1)
        and:
        def params = ['$filter': "code eq 'Product'", '$expand': 'virtualAttributes']
        def context = oDataGetContext("IntegrationObjectItems", params)

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].virtualAttributes.results[*].attributeName') == [testVirtualAttributeName1]
    }

    @Test
    def "Virtual Attribute Descriptor can be expanded when making a GET request of IntegrationObjectItems"() {
        given:
        def testVirtualAttributeName1 = 'testVirtualAttribute1'
        virtualAttributeForProduct(testVirtualAttributeName1)
        and:
        def params = ['$filter': "attributeName eq " + "'$testVirtualAttributeName1'", '$expand': 'retrievalDescriptor']
        def context = oDataGetContext("IntegrationObjectItemVirtualAttributes", params)

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].retrievalDescriptor.logicLocation') == [CONTEXT_LOGIC_LOCATION]
        json.getCollectionOfObjects('d.results[*].retrievalDescriptor.code') == [CONTEXT_RETRIEVAL_DESCRIPTOR_CODE]
    }

    @Test
    def "POST an IO with Virtual Attributes creates schema containing those Virtual Attributes"() {
        given: 'product IOI with myVirtualAttribute virtual attribute'
        def virtualAttributeName = 'myVirtualAttribute'
        def body = integrationObject(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO)
                .withItems(
                        productIntegrationObjectItem(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO)
                                .withIntegrationObjectItemCode(IOI_CODE)
                                .withVirtualAttributes(contextVirtualAttribute(virtualAttributeName))
                                .build(),
                        contextCatalogVersionIntegrationObjectItem(),
                        contextCatalogIntegrationObjectItem()

                )
                .build()
        and: 'product is POSTed to the integration api'
        productIntegrationObjectIsCreated(body)

        when: 'schema is generate for MyProduct'
        def schema = ODataSchema.from(generator.generateSchema(IntegrationTestUtil.findAll(IntegrationObjectItemModel, { it.code == IOI_CODE })))

        then: 'schema contains regular and virtual attributes'
        with(schema)
                {
                    getEntityTypeNames().containsAll(IOI_CODE)
                    getEntityType(IOI_CODE).getPropertyNames().containsAll(virtualAttributeName)
                }
    }

    @Test
    def "POSTing an IO with Virtual Attributes with a malformed logic location generates a 500 response"() {
        given: 'product IOI with a virtual attribute containing a malformed logic location'
        def virtualAttributeName = 'myVirtualAttribute'
        def body = integrationObject(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO)
                .withItems(
                        productIntegrationObjectItem(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO)
                                .withIntegrationObjectItemCode(IOI_CODE)
                                .withVirtualAttributes(virtualAttribute()
                                        .withAttributeName(virtualAttributeName)
                                        .withIntegrationObjectCode(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO)
                                        .withIntegrationObjectItemCode(IOI_CODE)
                                        .withType('Product')
                                        .withRetrievalDescriptor(retrievalDescriptor()
                                                .withCode(CONTEXT_RETRIEVAL_DESCRIPTOR_CODE)
                                                .withLogicLocation("thisLocationIsAllWrong")
                                                .withType(CONTEXT_VIRTUAL_ATTRIBUTE_DESCRIPTOR_TYPE)
                                                .build()
                                        )
                                        .build()).build(),
                        contextCatalogVersionIntegrationObjectItem(),
                        contextCatalogIntegrationObjectItem())
                .build()

        when: 'product is POSTed to the integration api'
        def response = productIntegrationObjectIsCreated(body)

        then: 'response returns a 500 error'
        def errorFrom = extractErrorFrom(response, HttpStatusCodes.BAD_REQUEST)
        errorFrom.getObject('error.code') == 'invalid_attribute_value'
        errorFrom.getObject('error.message.value').toString().contains('does not meet the pattern')
    }

    @Test
    def "PATCH an IO with Virtual Attributes updates schema with new Virtual Attributes"() {
        given: 'product IOI with existing virtual attribute'
        def initialVirtualAttribute = 'initialVirtualAttribute'
        def body = integrationObject(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO)
                .withItems(
                        productIntegrationObjectItem(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO)
                                .withIntegrationObjectItemCode(IOI_CODE)
                                .withVirtualAttributes(contextVirtualAttribute(initialVirtualAttribute)).build(),
                        contextCatalogVersionIntegrationObjectItem(),
                        contextCatalogIntegrationObjectItem()
                )
                .build()
        and: 'product is POSTed to the integration api'
        productIntegrationObjectIsCreated(body)

        when: 'product is PATCHed to the integration api with brand and model virtual attributes'
        def newVirtualAttribute = 'newVirtualAttribute'
        def patchedBody = integrationObject(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO)
                .withItems(
                        productIntegrationObjectItem(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO)
                                .withIntegrationObjectItemCode(IOI_CODE)
                                .withVirtualAttributes(
                                        contextVirtualAttribute(initialVirtualAttribute),
                                        contextVirtualAttribute(newVirtualAttribute))
                                .build(),
                        contextCatalogVersionIntegrationObjectItem(),
                        contextCatalogIntegrationObjectItem()
                )
                .build()
        productIntegrationObjectIsPatched(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO, patchedBody)
        and: 'schema is generated for MyProduct'
        def schema = ODataSchema.from(generator.generateSchema(IntegrationTestUtil.findAll(IntegrationObjectItemModel, { it.code == IOI_CODE })))

        then: 'schema contains regular and PATCHed virtual attributes'
        with(schema)
                {
                    getEntityTypeNames().containsAll(IOI_CODE)
                    getEntityType(IOI_CODE).getPropertyNames().containsAll("code", initialVirtualAttribute, newVirtualAttribute)
                }
    }

    @Test
    def "DELETE an IO with Virtual Attributes deletes the virtual attributes but not the virtual attribute descriptor"() {
        given: 'an IO with virtual attributes exists'
        def testVirtualAttributeName1 = 'testVirtualAttribute1'
        virtualAttributeForProduct(testVirtualAttributeName1)
        and: 'the IO is deleted'
        facade.handleRequest deleteRequest('IntegrationObjects', PHONE_IO)

        when: 'retrieving virtual attributes and virtual attribute descriptors'
        def virtualAttributeContext = oDataGetContext('IntegrationObjectItemVirtualAttributes')
        def virtualAttributeDescriptorContext = oDataGetContext('IntegrationObjectVirtualAttributeDescriptors')
        ODataResponse virtualAttributeResponse = facade.handleRequest(virtualAttributeContext)
        ODataResponse virtualAttributeDescriptorResponse = facade.handleRequest(virtualAttributeDescriptorContext)

        then: 'the virtual attribute for the IO is deleted'
        def virtualAttributesJson = extractEntitiesFrom virtualAttributeResponse
        !virtualAttributesJson.getCollectionOfObjects('d.results[*].attributeName').contains(testVirtualAttributeName1)
        and: 'the virtual attribute descriptor is still present'
        def virtualAttributeDescriptorsJson = extractEntitiesFrom virtualAttributeDescriptorResponse
        virtualAttributeDescriptorsJson.getCollectionOfObjects('d.results[*].code').contains(CONTEXT_RETRIEVAL_DESCRIPTOR_CODE)
    }

    def virtualAttributesExistForProduct(String... attributeNames) {
        attributeNames.toList().forEach({ name -> virtualAttributeForProduct(name) })
    }

    def virtualAttributeForProduct(String attributeName) {
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE Script; code[unique = true]; scriptType(code); content',
                '                    ; logLoc             ; GROOVY          ; hello world from model',
                'INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]                        ; logicLocation           ; type(code)',
                "                                                         ; $CONTEXT_RETRIEVAL_DESCRIPTOR_CODE ; $CONTEXT_LOGIC_LOCATION ; $CONTEXT_VIRTUAL_ATTRIBUTE_DESCRIPTOR_TYPE",
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute ; $item             ; attributeName[unique = true] ; retrievalDescriptor(code)',
                "                                                    ; $PHONE_IO:Product ; $attributeName               ; $CONTEXT_RETRIEVAL_DESCRIPTOR_CODE"
        )
    }

    def virtualAttributeExistForProduct(String attributeName, String descriptorCode) {
        VirtualAttributeBuilder.attribute()
                .withName(attributeName)
                .forItem("$PHONE_IO:Product")
                .withRetrievalDescriptor(descriptorCode)
                .setup()
    }

    def virtualAttributeDescriptorWithNoType(String descriptorCode) {
        VirtualAttributeBuilder.logicDescriptor()
                .withLogicLocation(CONTEXT_LOGIC_LOCATION)
                .withCode(descriptorCode)
                .setup()
    }

    def productIntegrationObjectIsCreated(String productBody) {
        facade.handleRequest(oDataPostContext("IntegrationObjects", productBody))
    }

    def productIntegrationObjectIsPatched(String integrationObjectCode, String patchedBody) {
        facade.handleRequest patch(SERVICE_NAME, "IntegrationObjects", integrationObjectCode, patchedBody)
    }

    static ODataContext patch(String serviceName, String entitySet, String key, String body) {
        ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataPatchRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(serviceName)
                        .withEntitySet(entitySet)
                        .withEntityKeys(key))
                .withContentType('application/json')
                .withAccepts('application/json')
                .withBody(body)
    }

    private static ODataContext deleteRequest(String entitySet, String[] keys) {
        def reqSpec = ODataRequestBuilder.oDataDeleteRequest()
                .withPathInfo(PathInfoBuilder.pathInfo().withServiceName(SERVICE_NAME).withEntitySet(entitySet).withEntityKeys(keys))
        ODataFacadeTestUtils.createContext reqSpec
    }


    ODataContext oDataGetContext(String entitySetName) {
        oDataGetContext(entitySetName, [:])
    }

    ODataContext oDataGetContext(String entitySetName, Map params) {
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(entitySetName))
                .withParameters(params)
                .build()

        contextGenerator.generate request
    }

    ODataContext oDataPostContext(String entitySetName, String content) {
        def request = ODataFacadeTestUtils
                .oDataPostRequest(SERVICE_NAME, entitySetName, content, APPLICATION_JSON_VALUE)

        contextGenerator.generate request
    }

    def contextCatalogIntegrationObjectItem() {
        catalogIntegrationObjectItem(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO).build()
    }

    def contextCatalogVersionIntegrationObjectItem() {
        catalogVersionIntegrationObjectItem(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO).build()
    }

    def contextVirtualAttribute(String attributeName) {
        virtualAttribute()
                .withAttributeName(attributeName)
                .withIntegrationObjectCode(PRODUCT_WITH_VIRTUAL_ATTRIBUTES_IO)
                .withIntegrationObjectItemCode(IOI_CODE)
                .withType('Product')
                .withRetrievalDescriptor(contextRetrievalDescriptor())
                .build()
    }

    def contextRetrievalDescriptor() {
        retrievalDescriptor()
                .withCode(CONTEXT_RETRIEVAL_DESCRIPTOR_CODE)
                .withLogicLocation(CONTEXT_LOGIC_LOCATION)
                .withType(CONTEXT_VIRTUAL_ATTRIBUTE_DESCRIPTOR_TYPE)
                .build()
    }

    def extractEntitiesFrom(ODataResponse response) {
        extractBodyWithExpectedStatus(response, HttpStatusCodes.OK)
    }

    def extractErrorFrom(ODataResponse response, HttpStatusCodes expectedStatus) {
        extractBodyWithExpectedStatus(response, expectedStatus)
    }

    def extractBodyWithExpectedStatus(ODataResponse response, HttpStatusCodes expStatus) {
        assert response.getStatus() == expStatus
        JsonObject.createFrom response.getEntity() as InputStream
    }
}
