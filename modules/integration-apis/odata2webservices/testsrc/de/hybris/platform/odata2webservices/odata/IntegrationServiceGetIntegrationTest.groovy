/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.TestConstants
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2services.odata.ODataSchema
import de.hybris.platform.odata2services.odata.schema.SchemaGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.CatalogIntegrationObjectItemPayloadBuilder.catalogIntegrationObjectItem
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.CatalogVersionIntegrationObjectItemPayloadBuilder.catalogVersionIntegrationObjectItem
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.CategoryIntegrationObjectItemPayloadBuilder.categoryIntegrationObjectItem
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.ClassificationAttributeAssignmentPayloadBuilder.classificationAttributeAssignment
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.IntegrationObjectItemClassificationAttributePayloadBuilder.classificationAttribute
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.IntegrationObjectPayloadBuilder.integrationObject
import static de.hybris.platform.odata2webservices.odata.builders.meta.payload.ProductIntegrationObjectItemPayloadBuilder.productIntegrationObjectItem
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class IntegrationServiceGetIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    private static final String IOI_CODE = 'MyProduct'
    private static final String PHONE_IO = 'IntegrationServiceGetIntegrationTestIO'
    private static final String SERVICE_NAME = 'IntegrationService'
    private static final String CLASS_SYSTEM = 'Electronics'
    private static final String CLASS_VERSION = 'Phones'
    private static final String CLASSIFICATION_CLASS = 'phoneSpecs'
    private static final String CLASS_SYSTEM_VERSION = "$CLASS_SYSTEM:$CLASS_VERSION"
    private static final String PRODUCT_WITH_CLASS_ATTRIBUTES_IO = "ProductWithClassAttributesIO"

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
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$class = classificationClass($catalogVersionHeader, code)',
                '$attribute = classificationAttribute($systemVersionHeader, code)',

                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',

                'INSERT_UPDATE ClassificationSystem; id[unique = true]',
                "                                  ; $CLASS_SYSTEM",
                'INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]',
                "                                         ; $CLASS_SYSTEM             ; $CLASS_VERSION",
                'INSERT_UPDATE ClassificationClass; code[unique = true]  ; $catalogVersionHeader[unique = true]',
                "                                 ; $CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION",
                'INSERT_UPDATE ClassificationAttribute; code[unique = true]; $systemVersionHeader[unique = true]',
                "                                     ; brand              ; $CLASS_SYSTEM_VERSION",
                "                                     ; model              ; $CLASS_SYSTEM_VERSION",
                'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]                      ; $attribute[unique = true]    ; unit($systemVersionHeader, code); attributeType(code); mandatory[default = false]',
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION:brand  ;                                 ; number             ; true",
                "                                      ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS; $CLASS_SYSTEM_VERSION:model  ;                                 ; number             ; true",

                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $PHONE_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $PHONE_IO                             ; Product            ; Product",
                "                                   ; $PHONE_IO                             ; CatalogVersion     ; CatalogVersion",
                "                                   ; $PHONE_IO                             ; Catalog            ; Catalog",

                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]     ; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $PHONE_IO:Catalog        ; id                          ; Catalog:id",
                "                                            ; $PHONE_IO:Product        ; code                        ; Product:code",
                "                                            ; $PHONE_IO:Product        ; catalogVersion              ; Product:catalogVersion                             ; $PHONE_IO:CatalogVersion",
                "                                            ; $PHONE_IO:CatalogVersion ; version                     ; CatalogVersion:version",
                "                                            ; $PHONE_IO:CatalogVersion ; catalog                     ; CatalogVersion:catalog                             ; $PHONE_IO:Catalog"
        )
    }

    def cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll ClassAttributeAssignmentModel
        IntegrationTestUtil.removeAll ClassificationAttributeModel
        IntegrationTestUtil.removeAll ClassificationAttributeUnitModel
        IntegrationTestUtil.removeAll ClassificationClassModel
    }
    
    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == "IntegrationService" }
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == "IntegrationService" }
    }

    @Test
    def "GET IntegrationObjects returns all integration objects"() {
        given:
        def context = oDataGetContext("IntegrationObjects")

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == 2
        json.getCollectionOfObjects('d.results[*].code').containsAll('IntegrationService', PHONE_IO)
    }

    @Test
    def "GET IntegrationObjectItems returns all integration object items"() {
        given:
        def params = ['$top': '1000']
        def context = oDataGetContext("IntegrationObjectItems", params)

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def expectedIOIs = ['Product', 'CatalogVersion', 'Catalog',
                            'IntegrationObject', 'IntegrationType', 'ItemTypeMatchEnum', 'IntegrationObjectItem',
                            'IntegrationObjectItemAttribute', 'IntegrationObjectItemClassificationAttribute',
                            'ClassificationSystem', 'ClassificationSystemVersion', 'ClassAttributeAssignment',
                            'ClassificationClass', 'ClassificationAttribute', 'AttributeDescriptor', 'ComposedType',
                            'InboundChannelConfiguration', 'AuthenticationType',
                            'IntegrationObjectVirtualAttributeDescriptor', 'IntegrationObjectItemVirtualAttribute', 'Type']
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == expectedIOIs.size()
        json.getCollectionOfObjects('d.results[*].code').containsAll(expectedIOIs)
    }

    @Test
    @Unroll
    def "GET IntegrationObjectItemAttributes returns all attributes for type #itemType"() {
        given: "A large page size to make sure the first page will include all attributes that we expect"
        def params = ['$filter': "code eq " + "'$itemType'", '$expand': 'attributes']
        def context = oDataGetContext("IntegrationObjectItems", params)

        when:
        ODataResponse response = facade.handleRequest(context)

        then: 'verify the total number of attributes and that each type has the expected ones'
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].attributes.results[*]').size() == expectedAttributes.size()
        json.getCollectionOfObjects('d.results[*].attributes.results[*].attributeName').containsAll(expectedAttributes)

        where:
        itemType                                       | expectedAttributes
        'Product'                                      | ['code', 'catalogVersion']
        'CatalogVersion'                               | ['version', 'catalog']
        'Catalog'                                      | ['id']
        'IntegrationObject'                            | ['code', 'integrationType', 'items']
        'IntegrationType'                              | ['code']
        'ItemTypeMatchEnum'                            | ['code']
        'IntegrationObjectItem'                        | ['code', 'type', 'itemTypeMatch', 'integrationObject', 'root', 'attributes', 'classificationAttributes', 'virtualAttributes']
        'IntegrationObjectItemAttribute'               | ['attributeName', 'returnIntegrationObjectItem', 'attributeDescriptor', 'unique', 'autoCreate', 'integrationObjectItem']
        'IntegrationObjectItemClassificationAttribute' | ['attributeName', 'classAttributeAssignment', 'integrationObjectItem', 'returnIntegrationObjectItem']
        'ClassificationSystem'                         | ['id']
        'ClassificationSystemVersion'                  | ['catalog', 'version']
        'ClassAttributeAssignment'                     | ['classificationClass', 'classificationAttribute']
        'ClassificationClass'                          | ['code', 'catalogVersion']
        'ClassificationAttribute'                      | ['code', 'systemVersion']
        'AttributeDescriptor'                          | ['qualifier', 'enclosingType']
        'ComposedType'                                 | ['code']
        'InboundChannelConfiguration'                  | ['integrationObject', 'authenticationType']
        'AuthenticationType'                           | ['code']
        'IntegrationObjectVirtualAttributeDescriptor'  | ['code', 'logicLocation', 'type']
        'IntegrationObjectItemVirtualAttribute'        | ['integrationObjectItem', 'attributeName', 'retrievalDescriptor']
        'Type'                                         | ['code']
    }

    @Test
    def "GET IntegrationObjectItemClassificationAttributes returns classification attributes for Product"() {
        given:
        classificationAttributesForProduct()
        and:
        def context = oDataGetContext("IntegrationObjectItemClassificationAttributes")

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].attributeName').containsAll('brand', 'model')
    }

    @Test
    def "POST IntegrationObjects, IntegrationObjectItem and IntegrationObjectItemAttribute creates new service"() {
        given:
        def ioCode = "CategoryOne"
        categoryIntegrationObjectIsCreated(ioCode)

        when:
        def schema = ODataSchema.from(generator.generateSchema(getIntegrationObjectItemModelDefinitions()))

        then:
        with(schema)
                {
                    getEntityTypeNames().containsAll("Category", TestConstants.LOCALIZED_ENTITY_PREFIX + "Category")
                    getEntityType("Category").getPropertyNames().containsAll("code", "name")
                    getEntityType("Category").getAnnotatableProperty("code").getAnnotationNames() == ["Nullable", "s:IsUnique"]
                    getEntityType("Category").getAnnotatableProperty("name").getAnnotationNames() == ["s:IsLanguageDependent", "Nullable"]
                    getEntityType("Category").getNavigationPropertyNames() == ["localizedAttributes"]
                }

        cleanup:
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == ioCode }
    }

    @Test
    def "POST an IO with Classification Attributes for type Product creates schema containing those Classification Attributes"() {
        given: 'product IO with brand classification attribute'
        def body = integrationObject(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                .withItems(
                        productIntegrationObjectItem(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                                .withIntegrationObjectItemCode(IOI_CODE)
                                .withClassificationAttributes(contextClassAttribute('brand')).build(),
                        contextCatalogVersionIntegrationObjectItem(),
                        contextCatalogIntegrationObjectItem()).build()
        and: 'product is POSTed to the integration api'
        productIntegrationObjectIsCreated(body)

        when:
        "schema is generate for $IOI_CODE"
        def schema = ODataSchema.from(generator.generateSchema(IntegrationTestUtil.findAll(IntegrationObjectItemModel, { it.code == IOI_CODE })))

        then: 'schema contains regular and classification attributes'
        with(schema)
                {
                    getEntityTypeNames().containsAll(IOI_CODE)
                    getEntityType(IOI_CODE).getPropertyNames().containsAll("code", "brand")
                    getEntityType(IOI_CODE).getNavigationPropertyNames().containsAll("catalogVersion")
                }
    }

    @Test
    def "PATCH an IO with Classification Attributes for type Product updates schema with new Classification Attributes"() {
        given: 'product IOI with brand classification attribute'
        def body = integrationObject(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                .withItems(
                        productIntegrationObjectItem(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                                .withIntegrationObjectItemCode(IOI_CODE)
                                .withClassificationAttributes(contextClassAttribute('brand'))
                                .build(),
                        catalogVersionIntegrationObjectItem(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                                .build(),
                        catalogIntegrationObjectItem(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                                .build())
                .build()
        and: 'product is POSTed to the integration api'
        productIntegrationObjectIsCreated(body)
        when: 'product is PATCHed to the integration api with brand and model classification attributes'
        def patchedBody = integrationObject(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                .withItems(
                        productIntegrationObjectItem(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                                .withIntegrationObjectItemCode(IOI_CODE)
                                .withClassificationAttributes(
                                        contextClassAttribute('brand'), contextClassAttribute('model'))
                                .build(),
                        catalogVersionIntegrationObjectItem(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                                .build(),
                        catalogIntegrationObjectItem(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                                .build())
                .build()

        productIntegrationObjectIsPatched(PRODUCT_WITH_CLASS_ATTRIBUTES_IO, patchedBody)

        and: 'schema is generated for MyProduct'
        def schema = ODataSchema.from(generator.generateSchema(IntegrationTestUtil.findAll(IntegrationObjectItemModel, { it.code == 'MyProduct' })))

        then: 'schema contains regular and PATCHed classification attributes'
        with(schema)
                {
                    getEntityTypeNames().containsAll(IOI_CODE)
                    getEntityType(IOI_CODE).getPropertyNames().containsAll("code", "brand", "model")
                }
    }

    @Test
    def "POST multiple IntegrationObjects including duplicated Attribute name/descriptor creates different Integration Objects"() {
        given:
        def ioCode1 = "CategoryOne"
        def ioCode2 = "CategoryTwo"
        categoryIntegrationObjectIsCreated(ioCode1)
        categoryIntegrationObjectIsCreated(ioCode2)

        when:
        ODataResponse response = facade.handleRequest(oDataGetContext("IntegrationObjects"))

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].code').containsAll('CategoryOne', 'CategoryTwo')

        cleanup:
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == ioCode1 || it.code == ioCode2 }
    }

    def classificationAttributesForProduct() {
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment = classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment',
                "                                                          ; $PHONE_IO:Product   ; brand                       ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:brand",
                "                                                          ; $PHONE_IO:Product   ; model                       ; $CLASS_SYSTEM_VERSION:$CLASSIFICATION_CLASS:$CLASS_SYSTEM_VERSION:model"
        )
    }

    def categoryIntegrationObjectIsCreated(String integrationObjectCode) {
        def categoryOneIntegrationObjectContext = oDataPostContext("IntegrationObjects",
                integrationObject(integrationObjectCode)
                        .withItems(
                                categoryIntegrationObjectItem(integrationObjectCode)
                                        .build()).build())
        facade.handleRequest(categoryOneIntegrationObjectContext)
    }

    def productIntegrationObjectIsCreated(String productBody) {
        def productIntegrationObjectContext = oDataPostContext("IntegrationObjects", productBody)
        facade.handleRequest(productIntegrationObjectContext)
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
        catalogIntegrationObjectItem(PRODUCT_WITH_CLASS_ATTRIBUTES_IO).build()
    }

    def contextCatalogVersionIntegrationObjectItem() {
        catalogVersionIntegrationObjectItem(PRODUCT_WITH_CLASS_ATTRIBUTES_IO).build()
    }

    def contextClassAttribute(String attributeName) {
        classificationAttribute()
                .withIntegrationObjectCode(PRODUCT_WITH_CLASS_ATTRIBUTES_IO)
                .withIntegrationObjectItemCode(IOI_CODE)
                .withType('Product')
                .withAttributeName(attributeName)
                .withClassAttributeAssignment(contextClassAttributeAssignment(attributeName))
                .build()
    }

    def contextClassAttributeAssignment(String attributeName) {
        classificationAttributeAssignment()
                .withAttributeName(attributeName)
                .withClassificationClass(CLASSIFICATION_CLASS)
                .withClassificationSystem(CLASS_SYSTEM)
                .withClassificationVersion(CLASS_VERSION)
                .build()
    }

    def extractEntitiesFrom(ODataResponse response) {
        extractBodyWithExpectedStatus(response, HttpStatusCodes.OK)
    }

    def extractErrorFrom(ODataResponse response) {
        extractBodyWithExpectedStatus(response, HttpStatusCodes.BAD_REQUEST)
    }

    def extractBodyWithExpectedStatus(ODataResponse response, HttpStatusCodes expStatus) {
        assert response.getStatus() == expStatus
        JsonObject.createFrom response.getEntity() as InputStream
    }

    def getIntegrationObjectItemModelDefinitions() {
        IntegrationTestUtil.findAll(IntegrationObjectItemModel.class) as Collection<IntegrationObjectItemModel>
    }
}
