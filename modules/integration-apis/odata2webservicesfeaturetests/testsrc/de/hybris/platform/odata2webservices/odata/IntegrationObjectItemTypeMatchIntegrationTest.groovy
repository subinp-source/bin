/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.test.TestItemModel
import de.hybris.platform.core.model.test.TestItemType3Model
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
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class IntegrationObjectItemTypeMatchIntegrationTest extends ServicelayerSpockSpecification {
    private static final String IO = "IntegrationObjectItemTypeMatchIntegrationTest"
    private static final String ALL_ITEMS_PATH = "d.results"
    private static final String ALL_ITEMS_KEY_PATH = "d.results[*].integrationKey"
    private static final String KEY_ATTR_NAME = 'a'
    private static final String SUPERTYPE_ITEM_KEY = 'testItem'
    private static final String SAMETYPE_ITEM_KEY = 'testItemType2'
    private static final String SUBTYPE_ITEM_KEY = 'testItemType3'
    private static final String ENTITY_SET = "TestItemType2s"

    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

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
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor         ; unique',
                "                                            ; $IO:TestItemType2   ; sealed                      ; TestItemType2:sealed; false",
                "                                            ; $IO:TestItemType2   ; a                           ; TestItemType2:a     ; true",
                "                                            ; $IO:TestItemType2   ; b                           ; TestItemType2:b     ; false",
                "                                            ; $IO:TestItemType2   ; foo                         ; TestItemType2:foo   ; false"
        )
    }

    def setup() {
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
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    @Unroll
    @Issue(value = "IAPI-3884")
    def "returns #msg when IntegrationObjectItem.itemTypeMatch is set to #itemItemTypeMatch"() {
        given:
        givenTestItemType2WithItemTypeMatch(itemItemTypeMatch)

        when:
        final ODataResponse oDataResponse = handleRequest(getRequest(ENTITY_SET))

        then:
        def responseBody = extractBody oDataResponse as IntegrationODataResponse
        oDataResponse.getStatus() == HttpStatusCodes.OK
        responseBody.getCollectionOfObjects(ALL_ITEMS_PATH).size() == resultSize
        responseBody.getCollectionOfObjects(ALL_ITEMS_KEY_PATH).containsAll(keys)
        and: "attribute defined on a super-type item type is present for all instances of sub-type entities in the response"
        responseBody.getCollectionOfObjects("d.results[*].foo").size() == resultSize
        and: "attribute defined on exact item type (that is not on super-type) is present for all entities in the response"
        responseBody.getCollectionOfObjects("d.results[*].sealed").size() == resultSize

        where:
        msg                                         | itemItemTypeMatch                     | resultSize | keys
        "super, sub and same item types"            | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | 3          | [SUPERTYPE_ITEM_KEY, SAMETYPE_ITEM_KEY, SUBTYPE_ITEM_KEY]
        "items matching the requested type exactly" | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | 1          | SAMETYPE_ITEM_KEY
        "sub types and the same item types"         | ItemTypeMatch.ALL_SUBTYPES            | 2          | [SAMETYPE_ITEM_KEY, SUBTYPE_ITEM_KEY]
    }

    @Test
    @Unroll
    def "get entity with #msg integrationKey when IntegrationObjectItem.itemTypeMatch is set to #itemItemTypeMatch & item search type match is set to #configItemTypeMatch"() {
        given:
        givenTestItemType2WithItemTypeMatch(itemItemTypeMatch)

        final ODataRequestBuilder oDataRequest = getRequest(ENTITY_SET, PathInfoBuilder.pathInfo()
                .withServiceName(IO)
                .withEntitySet(ENTITY_SET)
                .withEntityKeys(itemKeyToSearch))

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        oDataResponse.getStatus() == HttpStatusCodes.NOT_FOUND

        where:
        msg           | itemKeyToSearch    | itemItemTypeMatch
        "supertype's" | SUPERTYPE_ITEM_KEY | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        "subtype's"   | SUBTYPE_ITEM_KEY   | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        "supertype's" | SUPERTYPE_ITEM_KEY | ItemTypeMatch.ALL_SUBTYPES
    }

    @Test
    @Unroll
    def "get entity with #msg integrationKey when IntegrationObjectItem.itemTypeMatch is set to #itemItemTypeMatch & item search type match is set to #configItemTypeMatch finds item"() {
        given:
        givenTestItemType2WithItemTypeMatch(itemItemTypeMatch)

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
        msg         | integrationKey    | itemItemTypeMatch
        "same type" | SAMETYPE_ITEM_KEY | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES
        "subtype's" | SUBTYPE_ITEM_KEY  | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES
        "same type" | SAMETYPE_ITEM_KEY | ItemTypeMatch.ALL_SUBTYPES
        "subtype's" | SUBTYPE_ITEM_KEY  | ItemTypeMatch.ALL_SUBTYPES
        "same type" | SAMETYPE_ITEM_KEY | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
    }

    @Test
    @Unroll
    def "post creates a new item when IntegrationObjectItem.itemTypeMatch is set to #itemTypeMatch and #msg exists with the same attribute that's unique in the IO only"() {
        given:
        givenTestItemType2WithItemTypeMatch(itemTypeMatch)

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
    def "post finds and updates existing item when IntegrationObjectItem.itemTypeMatch is set to ALL_SUBTYPES and sub-type exists with the same attribute that's unique in the IO only"() {
        given:
        def attrBValue = 'bValue'
        givenTestItemType2WithItemTypeMatch(ItemTypeMatch.ALL_SUBTYPES)

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
    def "patch fails to update #type when IntegrationObjectItem.itemTypeMatch is set to #itemTypeSearch and #type exists with the same integrationKey"() {
        given:
        def attrBValue = 'bValue'
        givenTestItemType2WithItemTypeMatch(itemTypeSearch)

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
    def "patch updates sub-type when IntegrationObjectItem.itemTypeMatch is set to ALL_SUBTYPES and sub-type exists with the same integrationKey"() {
        given:
        def attrBValue = 'bValue'
        givenTestItemType2WithItemTypeMatch(ItemTypeMatch.ALL_SUBTYPES)

        final ODataRequestBuilder oDataRequest = patchRequest(SUBTYPE_ITEM_KEY)
                .withBody(json().withField('b', attrBValue))

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
    def "delete responds with not found when IntegrationObjectItem.itemTypeMatch is set to #itemTypeMatch and #msg exists with the same integrationKey"() {
        given:
        givenTestItemType2WithItemTypeMatch(itemTypeMatch)

        final ODataRequestBuilder oDataRequest = deleteRequest(keyAttrValue)

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then:
        "Fails to find super-type with integratioKey=$keyAttrValue"
        oDataResponse.getStatus() == HttpStatusCodes.NOT_FOUND

        IntegrationTestUtil.findAll(TestItemModel, { it.a == keyAttrValue }).collect({ it.a }).size() == 1

        where:
        msg          | keyAttrValue       | itemTypeMatch
        "super-type" | SUPERTYPE_ITEM_KEY | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        "sub-type"   | SUBTYPE_ITEM_KEY   | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        "super-type" | SUPERTYPE_ITEM_KEY | ItemTypeMatch.ALL_SUBTYPES
    }

    @Test
    def "deletes item when IntegrationObjectItem.itemTypeMatch is set to ALL_SUBTYPES and sub-type exists with the same integrationKey"() {
        given:
        givenTestItemType2WithItemTypeMatch(ItemTypeMatch.ALL_SUBTYPES)

        final ODataRequestBuilder oDataRequest = deleteRequest(SUBTYPE_ITEM_KEY)

        when:
        final ODataResponse oDataResponse = handleRequest(oDataRequest)

        then: "Finds and deletes existing matching sub-type item"
        oDataResponse.getStatus() == HttpStatusCodes.OK

        IntegrationTestUtil.findAll(TestItemModel, { it.a == SUBTYPE_ITEM_KEY }).collect({ it.a }).size() == 0
    }

    private static givenTestItemType2WithItemTypeMatch(final ItemTypeMatch itemTypeMatch) {
        IntegrationTestUtil.importImpEx(
                '$ioHeader = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $ioHeader[unique = true]; code[unique = true]; type(code)   ; itemTypeMatch(code)',
                "                                   ; $IO                       ; TestItemType2    ; TestItemType2; $itemTypeMatch"
        )
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