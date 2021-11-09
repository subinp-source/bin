/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.core.model.test.TestItemModel
import de.hybris.platform.core.model.test.TestItemType3Model
import de.hybris.platform.core.model.user.UserModel
import de.hybris.platform.integrationservices.config.DefaultIntegrationServicesConfiguration
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.search.ItemTypeMatch
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.odata2webservicesfeaturetests.model.ComplexTestIntegrationItemModel
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItemDetailModel
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItemModel
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataItemTypeMatchIntegrationTest extends ServicelayerSpockSpecification {
    private static ItemTypeMatch DEFAULT_ITEMTYPESEARCH
    private static final String IO = "ODataItemTypeMatchIntegrationTest"
    private static final String ALL_ITEMS_PATH = "d.results"
    private static final String ALL_ITEMS_KEY_PATH = "d.results[*].integrationKey"
    private static final String IO2 = "TestItemSearchQueryIO"
    private static final String CODE = "code"
    private static final String KEY_ATTR_NAME = 'a'
    private static final String SUPERTYPE_ITEM_KEY = 'testItem'
    private static final String SAMETYPE_ITEM_KEY = 'testItemType2'
    private static final String SUBTYPE_ITEM_KEY = 'testItemType3'
    private static final String ENTITY_SET = "TestItemType2s"
    private static final String ERROR_MSG_PATH = "error.message.value"
    private static final String CATALOG = "Default"
    private static final String VERSION = "Staged"

    @Resource(name = "defaultODataFacade")
    private ODataFacade facade
    @Resource(name = "defaultIntegrationServicesConfiguration")
    private DefaultIntegrationServicesConfiguration configurationService

    def setupSpec() throws Exception {
        //TestItem <- TestItemType2 <- TestItemType3
        IntegrationTestUtil.importImpEx(
                '$ioHeader = integrationObject(code)',
                '$itemHeader = integrationObjectItem(integrationObject(code), code)',
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; $ioHeader[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                       ; TestItemType2    ; TestItemType2;",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); unique',
                "                                            ; $IO:TestItemType2   ; a                           ; TestItemType2:a  ; true",
                "                                            ; $IO:TestItemType2   ; b                           ; TestItemType2:b  ; false"
        )
    }

    def setup() {
        DEFAULT_ITEMTYPESEARCH = configurationService.getItemTypeMatch()
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE TestItem; a[unique = true]",
                "                      ; $SUPERTYPE_ITEM_KEY",
                "INSERT_UPDATE TestItemType2; a[unique = true]",
                "                           ; $SAMETYPE_ITEM_KEY",
                "INSERT_UPDATE TestItemType3; a[unique = true]     ; itemTypeTwo(a)  ; itemsTypeTwo(a)",
                "                           ; $SUBTYPE_ITEM_KEY ; $SAMETYPE_ITEM_KEY ; $SAMETYPE_ITEM_KEY"
        )
    }

    def cleanup() {
        IntegrationTestUtil.findAll(TestItemModel, {
            it.a == SUPERTYPE_ITEM_KEY || it.a == SAMETYPE_ITEM_KEY || it.a == SUBTYPE_ITEM_KEY
        }).forEach({ it -> IntegrationTestUtil.remove(it) })
        setItemTypeSearchMatch(DEFAULT_ITEMTYPESEARCH)
        IntegrationTestUtil.removeAll TestIntegrationItemModel
        IntegrationTestUtil.removeAll TestIntegrationItemDetailModel
        IntegrationTestUtil.removeAll ComplexTestIntegrationItemModel
        IntegrationTestUtil.findAny(IntegrationObjectModel, { it.code == IO2 }).ifPresent {
            IntegrationTestUtil.remove(it)
        }
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    def setItemTypeSearchMatch(final ItemTypeMatch match) {
        configurationService.setItemTypeMatch(match)
    }

    @Test
    @Unroll
    def "returns #msg when item search type match is set to #configItemTypeMatch"() {
        given:
        setItemTypeSearchMatch(configItemTypeMatch)

        when:
        final ODataResponse oDataResponse = handleRequest(getRequest(ENTITY_SET))

        then:
        def responseBody = extractBody oDataResponse as IntegrationODataResponse
        oDataResponse.getStatus() == HttpStatusCodes.OK
        responseBody.getCollectionOfObjects(ALL_ITEMS_PATH).size() == resultSize
        responseBody.getCollectionOfObjects(ALL_ITEMS_KEY_PATH).containsAll(keys)

        where:
        msg                                         | configItemTypeMatch                   | resultSize | keys
        "super, sub and same item types"            | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | 3          | [SUPERTYPE_ITEM_KEY, SAMETYPE_ITEM_KEY, SUBTYPE_ITEM_KEY]
        "items matching the requested type exactly" | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | 1          | SAMETYPE_ITEM_KEY
        "sub types and the same item types"         | ItemTypeMatch.ALL_SUBTYPES            | 2          | [SAMETYPE_ITEM_KEY, SUBTYPE_ITEM_KEY]
    }

    @Test
    @Unroll
    def "get entity with #msg integrationKey when item search type match is set to #configItemTypeMatch"() {
        given:
        setItemTypeSearchMatch(configItemTypeMatch)

        final ODataRequestBuilder oDataRequest = getRequest(ENTITY_SET, PathInfoBuilder.pathInfo()
                .withServiceName(IO)
                .withEntitySet(ENTITY_SET)
                .withEntityKeys(itemKeyToSearch))

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        oDataResponse.getStatus() == HttpStatusCodes.NOT_FOUND

        where:
        msg           | itemKeyToSearch    | configItemTypeMatch
        "supertype's" | SUPERTYPE_ITEM_KEY | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        "subtype's"   | SUBTYPE_ITEM_KEY   | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        "supertype's" | SUPERTYPE_ITEM_KEY | ItemTypeMatch.ALL_SUBTYPES
    }

    @Test
    @Unroll
    def "get entity with #msg integrationKey when item search type match is set to #configItemTypeMatch finds item"() {
        given:
        setItemTypeSearchMatch(configItemTypeMatch)

        final ODataRequestBuilder oDataRequest = getRequest(ENTITY_SET, PathInfoBuilder.pathInfo()
                .withServiceName(IO)
                .withEntitySet(ENTITY_SET)
                .withEntityKeys(integrationKey))

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        def responseBody = extractBody oDataResponse as IntegrationODataResponse
        oDataResponse.getStatus() == HttpStatusCodes.OK
        responseBody.getObject("\$.d.integrationKey") == integrationKey
        responseBody.getObject("\$.d.a") == integrationKey

        where:
        msg         | integrationKey    | configItemTypeMatch
        "same type" | SAMETYPE_ITEM_KEY | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES
        "subtype's" | SUBTYPE_ITEM_KEY  | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES
        "same type" | SAMETYPE_ITEM_KEY | ItemTypeMatch.ALL_SUBTYPES
        "subtype's" | SUBTYPE_ITEM_KEY  | ItemTypeMatch.ALL_SUBTYPES
        "same type" | SAMETYPE_ITEM_KEY | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
    }

    @Test
    @Unroll
    def "post creates a new item when item search type match is set to #itemTypeMatch and #msg exists with the same attribute that's unique in the IO only"() {
        given:
        setItemTypeSearchMatch(itemTypeMatch)

        final ODataRequestBuilder oDataRequest = postRequest()
                .withBody(
                        json()
                                .withField(KEY_ATTR_NAME, itemKey))

        when:
        handleRequest(oDataRequest)

        then:
        IntegrationTestUtil.findAll(TestItemModel, { it.a == itemKey }).collect({ it.a }).size() == 2

        where:
        msg          | itemKey            | itemTypeMatch
        "super-type" | SUPERTYPE_ITEM_KEY | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        "super-type" | SUPERTYPE_ITEM_KEY | ItemTypeMatch.ALL_SUBTYPES
        "sub-type"   | SUBTYPE_ITEM_KEY   | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
    }

    @Test
    def "post finds and updates existing item when item search type match is set to ALL_SUBTYPES and sub-type exists with the same attribute that's unique in the IO only"() {
        given:
        def attrBValue = 'bValue'
        setItemTypeSearchMatch(ItemTypeMatch.ALL_SUBTYPES)

        final ODataRequestBuilder oDataRequest = postRequest()
                .withBody(
                        json()
                                .withField(KEY_ATTR_NAME, SUBTYPE_ITEM_KEY)
                                .withField('b', attrBValue))

        when:
        handleRequest(oDataRequest)

        then: "sub-type item has been updated"
        final matchingItemsWithKey = IntegrationTestUtil.findAll(TestItemModel, { it.a == SUBTYPE_ITEM_KEY })
        matchingItemsWithKey.size() == 1
        matchingItemsWithKey.get(0) instanceof TestItemType3Model
        (matchingItemsWithKey.get(0) as TestItemType3Model).b == attrBValue
    }

    @Test
    @Unroll
    def "patch fails to update #type when item search type match is set to #itemTypeSearch and #type exists with the same integrationKey"() {
        given:
        def attrBValue = 'bValue'
        setItemTypeSearchMatch(itemTypeSearch)

        final ODataRequestBuilder oDataRequest = patchRequest(itemKey)
                .withBody(json().withField('b', attrBValue))

        when:
        def oDataResponse = handleRequest(oDataRequest)

        then:
        oDataResponse.getStatus() == HttpStatusCodes.NOT_FOUND

        and: "sub-type item has not been updated"
        final matchingItemsWithKey = IntegrationTestUtil.findAll(TestItemModel, { it.a == itemKey })
        matchingItemsWithKey.size() == 1
        (matchingItemsWithKey.get(0) as TestItemModel).b == null

        where:
        type         | itemKey            | itemTypeSearch
        'super-type' | SUPERTYPE_ITEM_KEY | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        'sub-type'   | SUBTYPE_ITEM_KEY   | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        'super-type' | SUPERTYPE_ITEM_KEY | ItemTypeMatch.ALL_SUBTYPES
    }

    @Test
    def "patch updates sub-type when item search type match is set to ALL_SUBTYPES and sub-type exists with the same integrationKey"() {
        given:
        def attrBValue = 'bValue'
        setItemTypeSearchMatch(ItemTypeMatch.ALL_SUBTYPES)

        final ODataRequestBuilder oDataRequest = patchRequest(SUBTYPE_ITEM_KEY)
                .withBody(
                        json().withField('b', attrBValue))

        when:
        handleRequest(oDataRequest)

        then: "sub-type item has been updated"
        final matchingItemsWithKey = IntegrationTestUtil.findAll(TestItemModel, { it.a == SUBTYPE_ITEM_KEY })
        matchingItemsWithKey.size() == 1
        matchingItemsWithKey.get(0) instanceof TestItemType3Model
        (matchingItemsWithKey.get(0) as TestItemType3Model).b == attrBValue
    }

    @Test
    @Unroll
    def "delete responds with not found when item search type match is set to #configItemTypeMatch and #msg exists with the same integrationKey"() {
        given:
        setItemTypeSearchMatch(configItemTypeMatch)

        final ODataRequestBuilder oDataRequest = deleteRequest(keyAttrValue)

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        "Fails to find super-type with integratioKey=$keyAttrValue"
        oDataResponse.getStatus() == HttpStatusCodes.NOT_FOUND

        IntegrationTestUtil.findAll(TestItemModel, { it.a == keyAttrValue }).collect({ it.a }).size() == 1

        where:
        msg          | keyAttrValue       | configItemTypeMatch
        "super-type" | SUPERTYPE_ITEM_KEY | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        "sub-type"   | SUBTYPE_ITEM_KEY   | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        "super-type" | SUPERTYPE_ITEM_KEY | ItemTypeMatch.ALL_SUBTYPES
    }

    @Test
    def "deletes item when item search type match is set to ALL_SUBTYPES and sub-type exists with the same integrationKey"() {
        given:
        setItemTypeSearchMatch(ItemTypeMatch.ALL_SUBTYPES)

        final ODataRequestBuilder oDataRequest = deleteRequest(SUBTYPE_ITEM_KEY)

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then: "Finds and deletes existing matching sub-type item"
        oDataResponse.getStatus() == HttpStatusCodes.OK

        IntegrationTestUtil.findAll(TestItemModel, { it.a == SUBTYPE_ITEM_KEY }).collect({ it.a }).size() == 0
    }

    @Test
    @Unroll
    def "post fails to create a new item when item search type match is set to #itemTypeMatch and super-type exists with the same attribute that's unique in the type system"() {
        given: "IO definition for ComplexTestIntegrationItem that extends TestIntegrationItem"
        complexTestIntegrationItemIntegrationObjectIsDefined()
        and: "super-type TestIntegrationItem item exists"
        IntegrationTestUtil.importImpEx(
                "INSERT TestIntegrationItem; code[unique = true]",
                "; $SUPERTYPE_ITEM_KEY"
        )
        and:
        setItemTypeSearchMatch(itemTypeMatch)

        final ODataRequestBuilder oDataRequest =
                postRequestWithEntitySet("ComplexTestIntegrationItems")
                        .withBody(
                                json()
                                        .withField(CODE, SUPERTYPE_ITEM_KEY)
                                        .withField("requiredDetails", [])
                        )

        when:
        def oDataResponse = handleRequest(oDataRequest)

        then: "an error response indicates that an item already exists with the same key"
        def responseBody = extractBody oDataResponse as IntegrationODataResponse
        oDataResponse.getStatus() == HttpStatusCodes.BAD_REQUEST
        responseBody.getObject(ERROR_MSG_PATH).toString().contains('ambiguous unique keys')

        and:
        "the same pre-existing super-type item exists with code = $SUPERTYPE_ITEM_KEY"
        final matchingItemsWithKey = findTestIntegrationItemWithCode(SUPERTYPE_ITEM_KEY)
        matchingItemsWithKey.size() == 1
        def item = matchingItemsWithKey.get(0)
        item instanceof TestIntegrationItemModel
        !(item instanceof ComplexTestIntegrationItemModel)

        where:
        itemTypeMatch << [ItemTypeMatch.RESTRICT_TO_ITEM_TYPE, ItemTypeMatch.ALL_SUBTYPES]
    }

    @Test
    def "post finds and updates existing item when item search type match is set to ALL_SUBTYPES and sub-type exists with the same attribute that's unique in the type system"() {
        given: "IO definition for ComplexTestIntegrationItem that extends TestIntegrationItem"
        givenTestIntegrationItemIntegrationObjectIsDefined()
        and: "sub-type ComplexTestIntegrationItem item exists"
        complexTestIntegrationItemExistsWithCode(SUBTYPE_ITEM_KEY)
        and:
        setItemTypeSearchMatch(ItemTypeMatch.ALL_SUBTYPES)

        final ODataRequestBuilder oDataRequest =
                postRequestWithEntitySet("TestIntegrationItems")
                        .withBody(
                                json()
                                        .withField(CODE, SUBTYPE_ITEM_KEY)
                        )

        when:
        def oDataResponse = handleRequest(oDataRequest)

        then: "a success response"
        oDataResponse.getStatus() == HttpStatusCodes.CREATED

        and:
        "the same pre-existing sub-type item exists with code = $SUBTYPE_ITEM_KEY"
        final matchingItemsWithKey = findTestIntegrationItemWithCode(SUBTYPE_ITEM_KEY)
        matchingItemsWithKey.size() == 1
        def item = matchingItemsWithKey.get(0)
        item instanceof ComplexTestIntegrationItemModel
    }

    @Test
    def "post fails to create a new item when item search type match is set to RESTRICT_TO_ITEM_TYPE and sub-type exists with the same attribute that's unique in the type system"() {
        given: "IO definition for ComplexTestIntegrationItem that extends TestIntegrationItem"
        givenTestIntegrationItemIntegrationObjectIsDefined()
        and: "sub-type ComplexTestIntegrationItem item exists"
        complexTestIntegrationItemExistsWithCode(SUBTYPE_ITEM_KEY)
        and:
        setItemTypeSearchMatch(ItemTypeMatch.RESTRICT_TO_ITEM_TYPE)

        final ODataRequestBuilder oDataRequest =
                postRequestWithEntitySet("TestIntegrationItems")
                        .withBody(
                                json()
                                        .withField(CODE, SUBTYPE_ITEM_KEY)
                        )

        when:
        def oDataResponse = handleRequest(oDataRequest)

        then: "an error response indicates that an item already exists with the same key"
        def responseBody = extractBody oDataResponse as IntegrationODataResponse
        oDataResponse.getStatus() == HttpStatusCodes.BAD_REQUEST
        responseBody.getObject(ERROR_MSG_PATH).toString().contains('ambiguous unique keys')

        and:
        "the same pre-existing sub-type item exists with code = $SUBTYPE_ITEM_KEY"
        final matchingItemsWithKey = findTestIntegrationItemWithCode(SUBTYPE_ITEM_KEY)
        matchingItemsWithKey.size() == 1
        def item = matchingItemsWithKey.get(0)
        item instanceof ComplexTestIntegrationItemModel
    }

    @Test
    def "post fails to update existing item when item search type match is set to ALL_SUB_AND_SUPERTYPES and super-type exists with the same attribute that's unique in the type system and a required attribute that is not defined on the super-type (only in the current type) is provided in the payload"() {
        given: "IO definition for ComplexTestIntegrationItem that extends TestIntegrationItem"
        complexTestIntegrationItemIntegrationObjectIsDefined()
        and: "super-type TestIntegrationItem item exists"
        IntegrationTestUtil.importImpEx(
                "INSERT TestIntegrationItem; code[unique = true]",
                "                          ; $SUPERTYPE_ITEM_KEY"
        )

        and: "A request payload includes 'requiredDetails' that is an attribute defined on ComplexTestIntegrationItem, but not its parent TestIntegrationItem itemtype"
        final ODataRequestBuilder oDataRequest =
                postRequestWithEntitySet("ComplexTestIntegrationItems")
                        .withBody(
                                json()
                                        .withField(CODE, SUPERTYPE_ITEM_KEY)
                                        .withField("requiredDetails", [])
                        )

        when:
        def oDataResponse = handleRequest(oDataRequest)

        then: "an error response indicating a problem because the instance of TestIntegrationItem does not have an attribute for requiredDetails"
        def responseBody = extractBody oDataResponse as IntegrationODataResponse
        oDataResponse.getStatus() == HttpStatusCodes.INTERNAL_SERVER_ERROR
        responseBody.getObject('error.code').toString()== "internal_error"
        responseBody.getObject('error.innererror').toString()== SUPERTYPE_ITEM_KEY
        responseBody.getObject(ERROR_MSG_PATH).toString().contains('No attribute with qualifier requiredDetails found.')
    }

    @Test
    def "post updates existing item when item search type match is set to ALL_SUB_AND_SUPERTYPES and super-type exists with the same attribute that's unique in the type system and a required attribute that is not defined on the super-type (only in the current type) is not provided in the payload"() {
        given: "IO definition for ComplexTestIntegrationItem that extends TestIntegrationItem"
        complexTestIntegrationItemIntegrationObjectIsDefined()
        and: "super-type TestIntegrationItem item exists"
        IntegrationTestUtil.importImpEx(
                "INSERT TestIntegrationItem; code[unique = true]",
                "                          ; $SUPERTYPE_ITEM_KEY"
        )

        final ODataRequestBuilder oDataRequest =
                postRequestWithEntitySet("ComplexTestIntegrationItems")
                        .withBody(
                                json()
                                        .withField(CODE, SUPERTYPE_ITEM_KEY)
                        )

        when:
        def oDataResponse = handleRequest(oDataRequest)

        then: "a success response"
        oDataResponse.getStatus() == HttpStatusCodes.CREATED

        and:
        "the same pre-existing super-type item exists with code = $SUPERTYPE_ITEM_KEY"
        final matchingItemsWithKey = findTestIntegrationItemWithCode(SUPERTYPE_ITEM_KEY)
        matchingItemsWithKey.size() == 1
        def item = matchingItemsWithKey.get(0)
        item instanceof TestIntegrationItemModel
        !(item instanceof ComplexTestIntegrationItemModel)
    }

    @Issue(value = "IAPI-2505")
    @Test
    def "post where abstract attribute"() {
        given: "IO definition for Category with allowedPrincipals of abstract Principal type"
        IntegrationTestUtil.importImpEx(
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO2               ",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO2                                  ; Principal          ; Principal",
                "                                   ; $IO2                                  ; Category           ; Category",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); $attributeType; unique[default = false]',
                "                                            ; $IO2:Principal                                                     ; uid                         ; Principal:uid                                      ;               ; true",
                "                                            ; $IO2:Category                                                      ; code                        ; Category:code",
                "                                            ; $IO2:Category                                                      ; allowedPrincipals           ; Category:allowedPrincipals                         ; $IO2:Principal",

                //Catalog & CatalogVersion exist for required attributes on Category
                "INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;",
                "                     ; $CATALOG         ; $CATALOG       ; true",
                "INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;",
                "                            ; $CATALOG                  ; $VERSION              ; true",
                "INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]",
                "                      ; $CATALOG           ; $CATALOG:$VERSION",
                "INSERT_UPDATE User; uid[unique = true]",
                "                  ; p1"
        )

        final ODataRequestBuilder oDataRequest =
                postRequestWithEntitySet("Categories")
                        .withBody(
                                json()
                                        .withField(CODE, SUPERTYPE_ITEM_KEY)
                                        .withField("allowedPrincipals", [json().withField("uid", "p1")])
                        )

        when:
        def oDataResponse = handleRequest(oDataRequest)

        then: "a success response"
        oDataResponse.getStatus() == HttpStatusCodes.CREATED

        cleanup:
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG }
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == VERSION }
        IntegrationTestUtil.removeAll CategoryModel
        IntegrationTestUtil.removeSafely UserModel, { it.uid == "p1" }
    }

    private static complexTestIntegrationItemExistsWithCode(final String code) {
        IntegrationTestUtil.importImpEx(
                "INSERT ComplexTestIntegrationItem; code[unique = true]; requiredDetails(&detailPk)",
                "                                 ; $code              ; detail1",
                "INSERT TestIntegrationItemDetail; &detailPk; code; master(code)",
                "                                ; detail1  ; d1  ; $SUBTYPE_ITEM_KEY",
        )
    }

    private static givenTestIntegrationItemIntegrationObjectIsDefined() {
        IntegrationTestUtil.importImpEx(
                '$ioHeader = integrationObject(code)',
                '$itemHeader = integrationObjectItem(integrationObject(code), code)',
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $IO2",
                'INSERT_UPDATE IntegrationObjectItem; $ioHeader[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO2                    ; TestIntegrationItem; TestIntegrationItem;",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]    ; attributeName[unique = true]; $descriptor',
                "                                            ; $IO2:TestIntegrationItem; code                        ; TestIntegrationItem:code"
        )
    }

    private static complexTestIntegrationItemIntegrationObjectIsDefined() {
        IntegrationTestUtil.importImpEx(
                '$ioHeader = integrationObject(code)',
                '$itemHeader = integrationObjectItem(integrationObject(code), code)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $IO2",
                'INSERT_UPDATE IntegrationObjectItem; $ioHeader[unique = true]; code[unique = true]       ; type(code)',
                "                                   ; $IO2                    ; ComplexTestIntegrationItem; ComplexTestIntegrationItem;",
                "                                   ; $IO2                    ; TestIntegrationItemDetail ; TestIntegrationItemDetail;",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]           ; attributeName[unique = true]; $descriptor                              ; $attributeType',
                "                                            ; $IO2:TestIntegrationItemDetail ; code                        ; TestIntegrationItemDetail:code",
                "                                            ; $IO2:ComplexTestIntegrationItem; code                        ; ComplexTestIntegrationItem:code",
                "                                            ; $IO2:ComplexTestIntegrationItem; requiredDetails             ; ComplexTestIntegrationItem:requiredDetails; $IO2:TestIntegrationItemDetail"
        )
    }

    private static ODataRequestBuilder postRequestWithEntitySet(final String entitySet) {
        ODataRequestBuilder.oDataPostRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withContentType(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO2)
                        .withEntitySet(entitySet))
    }

    private static Collection<TestIntegrationItemModel> findTestIntegrationItemWithCode(final String code) {
        IntegrationTestUtil.findAll(TestIntegrationItemModel, { it.code == code })
    }

    private static ODataRequestBuilder deleteRequest(final String... keys) {
        ODataRequestBuilder.oDataDeleteRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO)
                        .withEntitySet(ENTITY_SET)
                        .withEntityKeys(keys))
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

    private static ODataRequestBuilder postRequest() {
        ODataRequestBuilder.oDataPostRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withContentType(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO)
                        .withEntitySet(ENTITY_SET))
    }

    JsonObject extractBody(IntegrationODataResponse response) {
        JsonObject.createFrom response.entityAsStream
    }

    static TestItemModel findTestItemModelByAttributeA(final String aValue) {
        return IntegrationTestUtil.findAny(TestItemModel, { it.a == aValue }).orElse(null)
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
