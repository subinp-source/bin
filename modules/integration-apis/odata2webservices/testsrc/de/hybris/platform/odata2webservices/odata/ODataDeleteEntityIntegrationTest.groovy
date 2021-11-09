/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
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
import de.hybris.platform.core.model.user.CustomerModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

/**
 * Tests for delete entity feature (STOUT-410)
 */
@IntegrationTest
class ODataDeleteEntityIntegrationTest extends ServicelayerSpockSpecification {
    private static final String CATALOG = 'Test'
    private static final String IO_CODE = 'Test'
    private static final String OWNER_ENTITY_SET = 'Orders'
    private static final String OWNED_ENTITY_SET = 'OrderEntries'
    private static final String PRODUCT1 = 'Camera'
    private static final String PRODUCT2 = 'Tripod'
    private static final String CUSTOMER = 'ob1'
    private static final String UNIT = 'pieces'
    private static final String USD = 'us'

    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setup() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_CODE",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $IO_CODE                              ; Order              ; Order     ; true",
                "                                   ; $IO_CODE                              ; OrderEntry         ; OrderEntry;",
                "                                   ; $IO_CODE                              ; Customer           ; Customer  ;",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor           ; $attributeType     ; unique[default = false]',
                "                                            ; $IO_CODE:Order      ; code                        ; Order:code            ;                    ;",
                "                                            ; $IO_CODE:Order      ; user                        ; Order:user            ; $IO_CODE:Customer  ;",
                "                                            ; $IO_CODE:Order      ; entries                     ; Order:entries         ; $IO_CODE:OrderEntry;",
                "                                            ; $IO_CODE:Customer   ; uid                         ; User:uid              ;                    ;",
                "                                            ; $IO_CODE:OrderEntry ; entryNumber                 ; OrderEntry:entryNumber;                    ; true",
                "                                            ; $IO_CODE:OrderEntry ; order                       ; OrderEntry:order      ; $IO_CODE:Order     ; true")
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Customer; uid[unique = true]',
                "                      ; $CUSTOMER",
                'INSERT_UPDATE Catalog ; id[unique = true]',
                "                      ; $CATALOG",
                'INSERT_UPDATE CatalogVersion; version[unique = true]; catalog(id)[unique = true]',
                "                            ; Good                  ; $CATALOG",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(version, catalog(id))[unique = true]',
                "                     ; $PRODUCT1          ; Good:Test",
                "                     ; $PRODUCT2          ; Good:Test",
                'INSERT_UPDATE Currency; isocode[unique = true]; symbol',
                "                      ; $USD                  ; \$",
                'INSERT_UPDATE Unit; code[unique = true]; unitType',
                "                  ; $UNIT              ; $UNIT")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll OrderModel
        IntegrationTestUtil.removeAll UnitModel
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.removeAll CatalogVersionModel
        IntegrationTestUtil.findAny(CatalogModel, { it.id == CATALOG })
                .ifPresent { IntegrationTestUtil.remove it }
        IntegrationTestUtil.findAny(CurrencyModel, { it.isocode == USD })
                .ifPresent { IntegrationTestUtil.remove it }
        IntegrationTestUtil.findAny(CustomerModel, { it.uid == CUSTOMER })
                .ifPresent { IntegrationTestUtil.remove it }
    }

    @Test
    def "owned items are deleted when the owner item is deleted"() {
        given: "an order exists with references to a customer and order lines"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Order; code[unique = true]; user(uid); &orderPk; currency(isocode); date[dateformat=MM/dd/yyyy]',
                "                   ; order1             ; $CUSTOMER; order1pk; $USD             ; 02/20/2020",
                'INSERT_UPDATE OrderEntry; order(&orderPk)[unique = true]; entryNumber[unique = true]; product(code); unit(code); quantity',
                "                        ; order1pk                       ; 1                         ; $PRODUCT1    ; $UNIT     ; 1",
                "                        ; order1pk                       ; 2                         ; $PRODUCT2    ; $UNIT     ; 1")

        when: "DELETE request is sent for the existing order"
        facade.handleRequest deleteRequest(OWNER_ENTITY_SET, 'order1')

        then: "the order is deleted"
        findOrder('order1') == null
        and: "all referenced OrderEntries are also deleted because they are 'partOf' the Order (owned items)"
        findOrderEntries('order1').empty
        and: "the referenced customer still exists because it's not a 'partOf' relationship (now owned by Order)"
        IntegrationTestUtil.findAny(CustomerModel, { it.uid == CUSTOMER }).present
        and: "referenced products still exist because they are not partOf entries"
        !IntegrationTestUtil.findAll(ProductModel).empty
    }

    @Test
    def "owner item is not deleted when an owned item is deleted"() {
        def orderCode = 'order2'
        given: "an order exists with references to order lines"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Order; code[unique = true]; user(uid); &orderPk; currency(isocode); date[dateformat=MM/dd/yyyy]',
                "                   ; $orderCode         ; $CUSTOMER; theOrder; $USD             ; 02/20/2020",
                'INSERT_UPDATE OrderEntry; order(&orderPk)[unique = true]; entryNumber[unique = true]; product(code); unit(code); quantity',
                "                        ; theOrder                      ; 1                         ; $PRODUCT1    ; $UNIT     ; 1",
                "                        ; theOrder                      ; 2                         ; $PRODUCT2    ; $UNIT     ; 1")

        when: "DELETE request is sent for the existing order"
        facade.handleRequest deleteRequest(OWNED_ENTITY_SET, "2|$orderCode")

        then: "the order is not deleted because it's an owner"
        findOrder(orderCode) != null
        and: "the specified OrderEntry is deleted"
        !IntegrationTestUtil.findAny(OrderEntryModel, { it.order.code == orderCode && it.entryNumber == 2 }).present
        and: "the other OrderEntry still exists"
        IntegrationTestUtil.findAny(OrderEntryModel, { it.order.code == orderCode && it.entryNumber == 1 }).present
        and: "the referenced product still exist"
        IntegrationTestUtil.findAny(ProductModel, { it.code == PRODUCT2 }).present
    }

    @Test
    @Unroll
    def "does not delete anything when #desc item does not exist"() {
        def orderCode = 'order3'
        given: "an order exists"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Order; code[unique = true]; user(uid); &orderPk; currency(isocode); date[dateformat=MM/dd/yyyy]',
                "                   ; $orderCode         ; $CUSTOMER; theOrder; $USD             ; 02/20/2020",
                'INSERT_UPDATE OrderEntry; order(&orderPk)[unique = true]; entryNumber[unique = true]; product(code); unit(code); quantity',
                "                        ; theOrder                      ; 1                         ; $PRODUCT1    ; $UNIT     ; 1")

        when: "DELETE request is sent for a non-existing item"
        facade.handleRequest deleteRequest(entitySet, 'missing')

        then: "the existing order is not deleted"
        findOrder(orderCode) != null
        and: "all referenced OrderEntries are also not deleted"
        !findOrderEntries(orderCode).empty

        where:
        desc    | entitySet
        'owner' | OWNER_ENTITY_SET
        'owned' | OWNED_ENTITY_SET
    }

    @Test
    @Unroll
    def "does not delete all #desc items"() {
        def orderCode = 'order4'
        given: "an order exists"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Order; code[unique = true]; user(uid); &orderPk; currency(isocode); date[dateformat=MM/dd/yyyy]',
                "                   ; $orderCode         ; $CUSTOMER; theOrder; $USD             ; 02/20/2020",
                'INSERT_UPDATE OrderEntry; order(&orderPk)[unique = true]; entryNumber[unique = true]; product(code); unit(code); quantity',
                "                        ; theOrder                      ; 1                         ; $PRODUCT1    ; $UNIT     ; 1")

        when:
        "DELETE all $desc items request is sent"
        facade.handleRequest deleteRequest(entitySet)

        then: "the existing order is not deleted"
        findOrder(orderCode) != null
        and: "all OrderEntries also aren't deleted"
        !findOrderEntries(orderCode).empty

        where:
        desc    | entitySet
        'owner' | OWNER_ENTITY_SET
        'owned' | OWNED_ENTITY_SET
    }

    @Test
    @Unroll
    def "does not delete #desc items based on filter condition"() {
        def orderCode = 'order5'
        given: "an order exists"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Order; code[unique = true]; user(uid); &orderPk; currency(isocode); date[dateformat=MM/dd/yyyy]',
                "                   ; $orderCode         ; $CUSTOMER; theOrder; $USD             ; 02/20/2020",
                'INSERT_UPDATE OrderEntry; order(&orderPk)[unique = true]; entryNumber[unique = true]; product(code); unit(code); quantity',
                "                        ; theOrder                      ; 1                         ; $PRODUCT1    ; $UNIT     ; 1")

        when:
        "DELETE $desc items request is sent with filter $filter"
        facade.handleRequest deleteRequest(entitySet, ['$filter': filter as String])

        then: "the existing order is not deleted"
        findOrder(orderCode) != null
        and: "all OrderEntries also aren't deleted"
        !findOrderEntries(orderCode).empty

        where:
        desc    | entitySet        | filter
        'owner' | OWNER_ENTITY_SET | "user/uid eq '$CUSTOMER'"
        'owned' | OWNED_ENTITY_SET | "product/code eq '$PRODUCT1'"
    }

    private static ODataContext deleteRequest(String entitySet, String[] keys) {
        def reqSpec = ODataRequestBuilder.oDataDeleteRequest()
                .withPathInfo(PathInfoBuilder.pathInfo().withServiceName(IO_CODE).withEntitySet(entitySet).withEntityKeys(keys))
        ODataFacadeTestUtils.createContext reqSpec
    }

    private static ODataContext deleteRequest(String entitySet, Map<String, String> params = [:]) {
        def reqSpec = ODataRequestBuilder.oDataDeleteRequest()
                .withPathInfo(PathInfoBuilder.pathInfo().withServiceName(IO_CODE).withEntitySet(entitySet))
                .withParameters(params)
        ODataFacadeTestUtils.createContext reqSpec
    }

    private static OrderModel findOrder(String code) {
        IntegrationTestUtil.findAny(OrderModel, { it.code == code })
                .orElse(null)
    }

    private static Collection<OrderEntryModel> findOrderEntries(String orderCode) {
        IntegrationTestUtil.findAll(OrderEntryModel)
                .findAll { it.order.code == orderCode } as Collection<OrderEntryModel>
    }
}
