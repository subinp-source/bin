/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.order.OrderEntryModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.product.UnitModel
import de.hybris.platform.core.model.user.UserModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItem3Model
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItemModel
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import org.springframework.http.MediaType
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.postRequestBuilder

@IntegrationTest
class ODataCircularDependencyIntegrationTest extends ServicelayerSpockSpecification {
    private static final def CODE = '001'
    private static final def OTHER_CODE = '002'
    private static final def IO_CODE = 'CircularDependencyTest'
    private static final def CURRENCY = 'EUR'
    private static final def USER_ID = 'john'
    private static final def PRODUCT = 'jewels'
    private static final def CATALOG_ID = 'Jewelry'
    private static final def VERSION = 'Demo'
    private static final def UNIT = 'kg'


    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = 'defaultODataFacade')
    private ODataFacade facade

    def setupSpec() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_CODE",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_CODE                              ; Order              ; Order",
                "                                   ; $IO_CODE                              ; OrderEntry         ; OrderEntry",
                "                                   ; $IO_CODE                              ; Currency           ; Currency",
                "                                   ; $IO_CODE                              ; User               ; User",
                "                                   ; $IO_CODE                              ; Product            ; Product",
                "                                   ; $IO_CODE                              ; CatalogVersion     ; CatalogVersion",
                "                                   ; $IO_CODE                              ; Catalog            ; Catalog",
                "                                   ; $IO_CODE                              ; Unit               ; Unit",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attribute=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]   ; attributeName[unique = true]; $attribute            ; $refType               ; unique[default = false]',
                "                                            ; $IO_CODE:Order         ; code                        ; Order:code",
                "                                            ; $IO_CODE:Order         ; entries                     ; Order:entries         ; $IO_CODE:OrderEntry",
                "                                            ; $IO_CODE:OrderEntry    ; order                       ; OrderEntry:order      ; $IO_CODE:Order         ; true",
                // required attributes on Order and OrderEntry but not relevant for test
                "                                            ; $IO_CODE:OrderEntry    ; product                     ; OrderEntry:product    ; $IO_CODE:Product",
                "                                            ; $IO_CODE:OrderEntry    ; unit                        ; OrderEntry:unit       ; $IO_CODE:Unit",
                "                                            ; $IO_CODE:OrderEntry    ; quantity                    ; OrderEntry:quantity",
                "                                            ; $IO_CODE:Order         ; user                        ; Order:user            ; $IO_CODE:User",
                "                                            ; $IO_CODE:Order         ; currency                    ; Order:currency        ; $IO_CODE:Currency",
                "                                            ; $IO_CODE:Order         ; date                        ; Order:date",
                "                                            ; $IO_CODE:Currency      ; isocode                     ; Currency:isocode      ;                        ; true",
                "                                            ; $IO_CODE:User          ; uid                         ; User:uid              ;                        ; true",
                "                                            ; $IO_CODE:Catalog       ; id                          ; Catalog:id            ;                        ; true",
                "                                            ; $IO_CODE:CatalogVersion; catalog                     ; CatalogVersion:catalog; $IO_CODE:Catalog",
                "                                            ; $IO_CODE:CatalogVersion; version                     ; CatalogVersion:version;                        ; true",
                "                                            ; $IO_CODE:Unit          ; code                        ; Unit:code             ;                        ; true",
                "                                            ; $IO_CODE:Product       ; code                        ; Product:code          ;                        ; true",
                "                                            ; $IO_CODE:Product       ; catalogVersion              ; Product:catalogVersion; $IO_CODE:CatalogVersion"
        )
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Currency; isocode[unique = true];',
                "                      ; $CURRENCY")
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE User; uid[unique = true];',
                "                  ; $USER_ID")
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Unit; code[unique = true]; unitType',
                "                  ; $UNIT              ; weight")
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Catalog; id[unique = true]',
                "                     ; $CATALOG_ID")
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE CatalogVersion; version[unique = true]; catalog(id)[unique = true]',
                "                            ; $VERSION              ; $CATALOG_ID")
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(version, catalog(id))[unique = true]',
                "                     ; $PRODUCT           ; $VERSION:$CATALOG_ID")
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeSafely IntegrationObjectModel, { it.code == IO_CODE }
        IntegrationTestUtil.removeSafely CurrencyModel, { it.isocode == CURRENCY }
        IntegrationTestUtil.removeSafely UserModel, { it.uid == USER_ID }
        IntegrationTestUtil.removeSafely UnitModel, { it.code == UNIT }
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == VERSION }
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG_ID }
        IntegrationTestUtil.removeSafely ProductModel, { it.code == PRODUCT }
        IntegrationTestUtil.removeAll OrderModel
        IntegrationTestUtil.removeAll TestIntegrationItemModel
        IntegrationTestUtil.removeAll TestIntegrationItem3Model
    }

    @Unroll
    @Test
    def "can persist an order with order #entryType that refer back to the same order"() {
        when:
        def response = facade.handleRequest(createContext(request))

        then:
        response.status == HttpStatusCodes.CREATED

        where:
        request                                  | entryType
        orderWith(entry(CODE))                   | "entry"
        orderWith(entry(CODE), entry(CODE))      | "entries"
        orderWith(entryWithAllOrderAttributes()) | "entry with all order attributes"
    }

    @Unroll
    @Test
    def "persist an order with #entryType that refers to a different existing order fails"() {
        given:
        "order $OTHER_CODE exists"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Order; code[unique = true]; user(uid); currency(isocode); date[dateformat = yyyymmdd]',
                "                   ; $OTHER_CODE        ; $USER_ID ; $CURRENCY        ; 20181101")

        when:
        def response = facade.handleRequest(createContext(request))

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        def json = JsonObject.createFrom response.entityAsStream
        with(json) {
            getString('error.code') == 'invalid_attribute_value'
            getString('error.message.value').contains 'Order'
            getString('error.innererror') == CODE
        }

        where:
        entryType                | request
        'only one order entry'   | orderWith(entry(OTHER_CODE))
        'one entry among others' | orderWith(entry(CODE), entry(OTHER_CODE))
    }

    @Unroll
    @Test
    def "persist an order with #entry that refers to a different non-existing order"() {
        when:
        def response = facade.handleRequest(createContext(request))

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'Order'
        json.getString('error.innererror') == CODE

        where:
        entry                    | request
        'only one order entry'   | orderWith(entryWithAllOrderAttributes('non-existing-order'))
        'one entry among others' | orderWith(entry(CODE), entryWithAllOrderAttributes('non-existing-order'))
    }

    @Test
    @Unroll
    @Issue('https://jira.hybris.com/browse/STOUT-2423')
    def "successfully creates an order with an entry that #condition"() {
        given:
        def request = orderWith(entry)

        when:
        def response = facade.handleRequest(createContext(request))

        then:
        response.status == HttpStatusCodes.CREATED

        where:
        condition                  | entry
        'does not refer any order' | entryWithoutOrder()
        'refers null order'        | entry(null)
    }

    @Test
    @Unroll
    @Issue('https://jira.hybris.com/browse/STOUT-2423')
    def "successfully updates an order entry that #condition"() {
        given: 'an order with an entry already exists'
        IntegrationTestUtil.importImpEx(
                'INSERT Order; code[unique = true]; user(uid); currency(isocode); date[dateformat=MMddyyyy]',
                "            ; $CODE              ; $USER_ID ; $CURRENCY        ; 02202020",
                '$product=product(code, catalogVersion(version, catalog(id)))',
                'INSERT OrderEntry; order(code)[unique=true]; quantity; $product                     ; unit(code)',
                "                 ; $CODE                   ; 1       ; $PRODUCT:$VERSION:$CATALOG_ID; $UNIT")
        and: 'request that changes quantity in the entry'
        def request = orderWith(entry)

        when:
        def response = facade.handleRequest(createContext(request))

        then:
        response.status == HttpStatusCodes.CREATED
        and:
        IntegrationTestUtil.findAll(OrderEntryModel, { it.order.code == CODE }).collect({ it.quantity }) == [2L]

        where:
        condition                  | entry
        'does not refer any order' | entryWithoutOrder().withField('quantity', '2')
        'refers null order'        | entry(null).withField('quantity', '2')
    }

    @Test
    @Issue('https://jira.hybris.com/browse/STOUT-2423')
    def "successfully creates an item with a missing nested required non-key reference attribute of the same type"() {
        given: "TestIntegrationItem and TestIntegrationItem3 objects are defined"
        def parentItemCode = 'parentCode'
        def childItemCode = 'childCode'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_CODE                              ; TestIntegrationItem              ; TestIntegrationItem",
                "                                   ; $IO_CODE                              ; TestIntegrationItem3         ; TestIntegrationItem3",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attribute=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]         ; attributeName[unique = true]; $attribute                       ; $refType                     ; autocreate[default = false]',
                "                                            ; $IO_CODE:TestIntegrationItem ; item3                       ; TestIntegrationItem:item3        ; $IO_CODE:TestIntegrationItem3; true ;",
                "                                            ; $IO_CODE:TestIntegrationItem ; code                        ; TestIntegrationItem:code         ; ",
                "                                            ; $IO_CODE:TestIntegrationItem3; requiredItem                ; TestIntegrationItem3:requiredItem; $IO_CODE:TestIntegrationItem;",
                "                                            ; $IO_CODE:TestIntegrationItem3; code                        ; TestIntegrationItem3:code        ; "
        )
        and: "a request body where TestIntegrationItem3 does not provide its required item attribute of type TestIntegrationItem"
        def request = request('TestIntegrationItems').withBody(
                json()
                        .withCode(parentItemCode)
                        .withField("item3",
                                json().withCode(childItemCode)
                        ))

        when:
        def response = facade.handleRequest(createContext(request))

        then:
        response.status == HttpStatusCodes.CREATED
    }

    private static ODataRequestBuilder orderWith(JsonBuilder... entries) {
        request('Orders').withBody(
                order().withFieldValues("entries", entries)
        )
    }

    private static ODataRequestBuilder request(final String entitySetName) {
        postRequestBuilder(IO_CODE, entitySetName, MediaType.APPLICATION_JSON_VALUE)
    }

    private static JsonBuilder order(String code = CODE) {
        json()
                .withCode(code)
                .withField("date", "/Date(1540469763000)/")
                .withField("user", json().withField("uid", USER_ID))
                .withField("currency", json().withField("isocode", CURRENCY))
    }

    private static JsonBuilder entryWithAllOrderAttributes(String code = CODE) {
        entryWithoutOrder().withField('order', order(code))
    }

    private static JsonBuilder entry(String code) {
        def order = Optional.ofNullable(code)
                .map({ json().withCode(it) })
                .orElse(null)
        entryWithoutOrder().withField 'order', order
    }

    private static JsonBuilder entryWithoutOrder() {
        json()
                .withField('quantity', '7')
                .withField('unit', json().withCode(UNIT))
                .withField('product', json()
                        .withCode(PRODUCT)
                        .withField('catalogVersion', json()
                                .withField('version', VERSION)
                                .withField('catalog', json().withId(CATALOG_ID)))
                )
    }
}
