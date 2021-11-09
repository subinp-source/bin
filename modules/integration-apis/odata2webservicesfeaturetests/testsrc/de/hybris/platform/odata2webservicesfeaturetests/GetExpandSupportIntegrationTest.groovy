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

package de.hybris.platform.odata2webservicesfeaturetests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.order.OrderEntryModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.product.UnitModel
import de.hybris.platform.core.model.type.SearchRestrictionModel
import de.hybris.platform.core.model.user.AddressModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.inboundservices.util.InboundMonitoringRule
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItemModel
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel
import de.hybris.platform.ordersplitting.model.ConsignmentModel
import de.hybris.platform.ordersplitting.model.VendorModel
import de.hybris.platform.ordersplitting.model.WarehouseModel
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.junit.Rule
import org.junit.Test
import spock.lang.Issue

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.givenUserExistsWithUidAndGroups
import static de.hybris.platform.odata2webservicesfeaturetests.ws.InboundChannelConfigurationBuilder.inboundChannelConfigurationBuilder

/**
 * Tests for STOUT-1258 Expand Support feature
 */
@NeedsEmbeddedServer(webExtensions = Odata2webservicesConstants.EXTENSIONNAME)
@IntegrationTest
class GetExpandSupportIntegrationTest extends ServicelayerSpockSpecification {
    private static final Collection searchRestrictions = ['inboundMonitoringIntegrationVisibility', 'outboundMonitoringIntegrationVisibility', 'integrationServiceVisibility', 'outboundChannelConfigVisibility', 'scriptServiceVisibility']
    private static String TEST_EXPAND_2_LEVEL = 'TestItemForEnumExpand'
    private static String EXPAND_SUPPORT_IO = 'ExpandSupport'
    private static String USER = 'expandTester'
    private static String PASSWORD = 'retset'
    private static String PRODUCT_1 = 'product1'
    private static String PRODUCT_2 = 'product2'
    private static String CATALOG = 'expandTestCatalog'
    private static String CATALOG_VERSION = 'expandTestCatalogVersion'
    private static String CURRENCY_ISOCODE = 'expandTestEUR'

    @Rule
    InboundMonitoringRule monitoring = InboundMonitoringRule.disabled()

    def setupSpec() {
        importCsv("/impex/essentialdata-odata2services.impex", "UTF-8") // for the integrationadmingroup
        IntegrationTestUtil.importImpEx(
                "\$catalogVersion = $CATALOG:$CATALOG_VERSION",
                'INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;',
                "                     ; $CATALOG         ; $CATALOG       ; false",
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;',
                "                            ; $CATALOG                  ; $CATALOG_VERSION      ; false",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                "; $PRODUCT_1 ; \$catalogVersion",
                "; $PRODUCT_2 ; \$catalogVersion",
                "INSERT_UPDATE Currency; isocode[unique = true]; symbol",
                "                      ; $CURRENCY_ISOCODE     ; EUR",
        )
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove ProductModel, { it.code == PRODUCT_1 || it.code == PRODUCT_2 }
        IntegrationTestUtil.remove CatalogVersionModel, { it.version == CATALOG_VERSION && it.catalog.id == CATALOG }
        IntegrationTestUtil.remove CatalogModel, { it.id == CATALOG }
        IntegrationTestUtil.remove CurrencyModel, { it.isocode == CURRENCY_ISOCODE }
        IntegrationTestUtil.remove EmployeeModel, { it.uid == USER }
        searchRestrictions.each { searchRestriction -> IntegrationTestUtil.remove SearchRestrictionModel, { searchRestrictions.contains it.code } }
    }

    def setup() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $EXPAND_SUPPORT_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $EXPAND_SUPPORT_IO                    ; Product         ; Product",
                "                                   ; $EXPAND_SUPPORT_IO                    ; Catalog         ; Catalog",
                "                                   ; $EXPAND_SUPPORT_IO                    ; CatalogVersion  ; CatalogVersion",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $EXPAND_SUPPORT_IO:Catalog                                         ; id                          ; Catalog:id",
                "                                            ; $EXPAND_SUPPORT_IO:CatalogVersion                                  ; catalog                     ; CatalogVersion:catalog                            ; $EXPAND_SUPPORT_IO:Catalog",
                "                                            ; $EXPAND_SUPPORT_IO:CatalogVersion                                  ; version                     ; CatalogVersion:version",
                "                                            ; $EXPAND_SUPPORT_IO:Product                                         ; code                        ; Product:code",
                "                                            ; $EXPAND_SUPPORT_IO:Product                                         ; catalogVersion              ; Product:catalogVersion                            ; $EXPAND_SUPPORT_IO:CatalogVersion",
        )
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationadmingroup")
        inboundChannelConfigurationBuilder()
                .withCode(EXPAND_SUPPORT_IO)
                .withAuthType(AuthenticationType.BASIC)
                .build()
    }

    def cleanup() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == EXPAND_SUPPORT_IO }
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.findAny(EmployeeModel, { it.uid == USER }).ifPresent { IntegrationTestUtil.remove it }
    }

    @Test
    def "entities not expanded when \$expand is not present"() {
        when:
        def response = basicAuthRequest(EXPAND_SUPPORT_IO)
                .path('Products')
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 200
        def json = extractBody response
        json.exists "\$.d.results[?(@.code == 'product1')].catalogVersion.__deferred"
        json.exists "\$.d.results[?(@.code == 'product2')].catalogVersion.__deferred"
    }

    @Test
    def "entities are expanded when \$expand is present"() {
        when:
        def response = basicAuthRequest(EXPAND_SUPPORT_IO)
                .path('Products')
                .queryParam('$expand', 'catalogVersion')
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 200
        def json = extractBody(response)
        json.getCollectionOfObjects("\$.d.results[*].catalogVersion.version") == [CATALOG_VERSION, CATALOG_VERSION]
    }

    @Test
    def "expands collection of enums"() {
        given:
        String itemKey = "testItem1Code"
        String enumKey1 = "string"
        String enumKey2 = "bool"
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]                         ; type(code)",
                "                                   ; $EXPAND_SUPPORT_IO                    ; TestIntegrationItem                         ; TestIntegrationItem",
                "                                   ; $EXPAND_SUPPORT_IO                    ; OData2webservicesFeatureTestPropertiesTypes ; OData2webservicesFeatureTestPropertiesTypes",
                "INSERT_UPDATE IntegrationObjectItemAttribute ; integrationObjectItem(integrationObject(code), code)[unique = true] ; attributeName[unique = true] ; attributeDescriptor(enclosingType(code), qualifier) ; returnIntegrationObjectItem(integrationObject(code), code)     ; unique[default = false]",
                "                                             ; $EXPAND_SUPPORT_IO:TestIntegrationItem                              ; code                         ; TestIntegrationItem:code",
                "                                             ; $EXPAND_SUPPORT_IO:TestIntegrationItem                              ; testEnums                    ; TestIntegrationItem:testEnums                      ; $EXPAND_SUPPORT_IO:OData2webservicesFeatureTestPropertiesTypes  ; false",
                "                                             ; $EXPAND_SUPPORT_IO:OData2webservicesFeatureTestPropertiesTypes      ; code                         ; OData2webservicesFeatureTestPropertiesTypes:code",

                "INSERT_UPDATE TestIntegrationItem ; code[unique = true] ; testEnums(code)",
                "                                  ; $itemKey            ; $enumKey1,$enumKey2"
        )

        when:
        def response = basicAuthRequest(EXPAND_SUPPORT_IO)
                .path('TestIntegrationItems')
                .queryParam('$expand', 'testEnums')
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 200
        def json = extractBody(response)
        json.getCollectionOfObjects('d.results[?(@.code == "testItem1Code")].testEnums.results[*].code').containsAll([enumKey1, enumKey2])

        cleanup:
        IntegrationTestUtil.remove TestIntegrationItemModel, { it.code == itemKey }
    }

    @Test
    def "not found error returned when invalid \$expand option is specified"() {
        when:
        def response = basicAuthRequest(EXPAND_SUPPORT_IO)
                .path('Products')
                .queryParam('$expand', 'units')
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 404
        def json = extractBody(response)
        json.getString("\$.error.message.value").contains "units"
    }

    @Test
    def "entities are expanded when \$expand option of an enum is provided at the second level"() {
        given:
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $TEST_EXPAND_2_LEVEL",
                "INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true] ; type(code)",
                "                                   ; $TEST_EXPAND_2_LEVEL                  ; Order               ; Order",
                "                                   ; $TEST_EXPAND_2_LEVEL                  ; Gender              ; Gender",
                "                                   ; $TEST_EXPAND_2_LEVEL                  ; Address             ; Address",
                "INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]",
                "                                            ; $TEST_EXPAND_2_LEVEL:Order                                         ; code                        ; Order:code                                        ;                                                           ; true",
                "                                            ; $TEST_EXPAND_2_LEVEL:Order                                         ; paymentAddress              ; Order:paymentAddress                              ; $TEST_EXPAND_2_LEVEL:Address",
                "                                            ; $TEST_EXPAND_2_LEVEL:Gender                                        ; code                        ; Gender:code                                       ;                                                           ; true",
                "                                            ; $TEST_EXPAND_2_LEVEL:Address                                       ; firstname                    ; Address:firstname                                  ;                                                           ; true",
                "                                            ; $TEST_EXPAND_2_LEVEL:Address                                       ; lastname                    ; Address:lastname                                  ;                                                           ; true",
                "                                            ; $TEST_EXPAND_2_LEVEL:Address                                       ; gender                      ; Address:gender                                    ; $TEST_EXPAND_2_LEVEL:Gender",

                "INSERT_UPDATE Address; firstname[unique = true]; lastname[unique = true]; gender(code); owner(User.uid)",
                "                     ; Alberto                ; Contador               ; MALE        ; admin",
                "INSERT_UPDATE Order; code[unique = true]; paymentAddress(firstname, lastname); date[dateformat = dd/MM/yyyy]; currency(isocode); user(uid)",
                "                   ; firstOrder          ; Alberto:Contador                  ; 04/04/2019                   ; $CURRENCY_ISOCODE; admin")

        and:
        "An InboundChannelConfiguration is linked to the $TEST_EXPAND_2_LEVEL"
        inboundChannelConfigurationBuilder()
                .withCode(TEST_EXPAND_2_LEVEL)
                .withAuthType(AuthenticationType.BASIC)
                .build()

        when:
        def response = basicAuthRequest(TEST_EXPAND_2_LEVEL)
                .path('Orders')
                .queryParam('$expand', 'paymentAddress/gender')
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 200
        def json = extractBody(response)
        json.getCollectionOfObjects("\$.d.results[*].paymentAddress.firstname").contains('Alberto')
        json.getCollectionOfObjects("\$.d.results[*].paymentAddress.lastname").contains('Contador')
        json.getCollectionOfObjects("\$.d.results[*].paymentAddress.gender.code").contains('MALE')

        cleanup:
        IntegrationTestUtil.remove OrderModel, { it.code == 'firstOrder' }
        IntegrationTestUtil.remove AddressModel, { it.firstname == 'Alberto' && it.lastname == 'Contador' }
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == TEST_EXPAND_2_LEVEL }
    }

    @Test
    def 'entities are expanded when $expand for collection'() {
        given:
        importCsv '/impex/essentialdata-integrationservices.impex', 'UTF-8'
        importCsv '/impex/essentialdata-odata2webservices.impex', 'UTF-8'

        and: "User is in both in integrationadmingroup and integrationservicegroup"
        givenUserExistsWithUidAndGroups(USER, PASSWORD, "integrationadmingroup,integrationservicegroup")

        and: "An InboundChannelConfiguration is linked to the IntegrationService"
        inboundChannelConfigurationBuilder()
                .withCode("IntegrationService")
                .withAuthType(AuthenticationType.BASIC)
                .build()

        when:
        def response = basicAuthRequest('IntegrationService')
                .path("IntegrationObjects('ExpandSupport')")
                .queryParam('$expand', 'items/attributes')
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 200
        def json = extractBody(response)
        !json.getCollectionOfObjects("\$.d.items.results[*].attributes.results[*].attributeName").empty

        cleanup:
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == "IntegrationService" }
    }

    @Issue('https://jira.hybris.com/browse/IAPI-4103')
    @Test
    def "entities are expanded with many levels"() {
        given: 'IO with deep levels'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)         ; root[default = false]; itemTypeMatch(code)',
                "                                   ; $EXPAND_SUPPORT_IO                    ; Consignment        ; Consignment        ;                      ; ;",
                "                                   ; $EXPAND_SUPPORT_IO                    ; AbstractOrderEntry ; AbstractOrderEntry ;                      ; ;",
                "                                   ; $EXPAND_SUPPORT_IO                    ; ConsignmentEntry   ; ConsignmentEntry   ;                      ; ;",
                "                                   ; $EXPAND_SUPPORT_IO                    ; AbstractOrder      ; AbstractOrder      ;                      ; ;",
                "                                   ; $EXPAND_SUPPORT_IO                    ; Order              ; Order              ; true                 ; ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $EXPAND_SUPPORT_IO:Consignment                                     ; code                        ; Consignment:code                                   ;                                                           ; true                   ;",
                "                                            ; $EXPAND_SUPPORT_IO:Consignment                                     ; consignmentEntries          ; Consignment:consignmentEntries                     ; $EXPAND_SUPPORT_IO:ConsignmentEntry                       ;                        ;",
                "                                            ; $EXPAND_SUPPORT_IO:AbstractOrderEntry                              ; order                       ; AbstractOrderEntry:order                           ; $EXPAND_SUPPORT_IO:AbstractOrder                          ; true                   ;",
                "                                            ; $EXPAND_SUPPORT_IO:AbstractOrderEntry                              ; entryNumber                 ; AbstractOrderEntry:entryNumber                     ;                                                           ; true                   ;",
                "                                            ; $EXPAND_SUPPORT_IO:AbstractOrderEntry                              ; product                     ; AbstractOrderEntry:product                         ; $EXPAND_SUPPORT_IO:Product                                ;                        ;",
                "                                            ; $EXPAND_SUPPORT_IO:ConsignmentEntry                                ; quantity                    ; ConsignmentEntry:quantity                          ;                                                           ; true                   ;",
                "                                            ; $EXPAND_SUPPORT_IO:ConsignmentEntry                                ; orderEntry                  ; ConsignmentEntry:orderEntry                        ; $EXPAND_SUPPORT_IO:AbstractOrderEntry                     ;                        ;",
                "                                            ; $EXPAND_SUPPORT_IO:AbstractOrder                                   ; code                        ; AbstractOrder:code                                 ;                                                           ; true                   ;",
                "                                            ; $EXPAND_SUPPORT_IO:Order                                           ; code                        ; Order:code                                         ;                                                           ; true                   ;",
                "                                            ; $EXPAND_SUPPORT_IO:Order                                           ; consignments                ; Order:consignments                                 ; $EXPAND_SUPPORT_IO:Consignment                            ;                        ;"
        )

        and: 'Order containing a product that can be reached 4 levels down'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Order; code[unique = true]; date[dateformat = YYYY-MM-dd]; currency(isocode); user(uid)',
                "                   ; expandTestOrder    ; 2020-04-16                   ; $CURRENCY_ISOCODE; $USER",

                'INSERT_UPDATE Unit; code[unique = true]; unitType',
                '                  ; expandTestUnit     ; expandTestType',

                'INSERT_UPDATE OrderEntry; entryNumber[unique = true]; order(code)     ; product(code, catalogVersion(version, catalog(id))) ; quantity; unit(code)',
                "                        ; 1                         ; expandTestOrder ; $PRODUCT_1:$CATALOG_VERSION:$CATALOG                ; 1       ; expandTestUnit",

                'INSERT_UPDATE Vendor; code[unique = true]',
                '                    ; expandTestVendor',

                'INSERT_UPDATE Warehouse; code[unique = true] ; vendor(code)',
                '                       ; expandTestWarehouse ; expandTestVendor',

                'INSERT Address; &addrId           ; owner(Order.code)',
                '              ; expandTestAddress ; expandTestOrder',

                'INSERT_UPDATE Consignment; code[unique = true]   ;  warehouse(code)     ; status(code); shippingAddress(&addrId)',
                '                         ; expandTestConsignment ;  expandTestWarehouse ; READY       ; expandTestAddress',

                'INSERT_UPDATE ConsignmentEntry; quantity[unique = true]; orderEntry(entryNumber); consignment(code)',
                '                              ; 1                      ; 1                      ; expandTestConsignment',

                'UPDATE Consignment; code[unique = true]   ; consignmentEntries(quantity)',
                '                  ; expandTestConsignment ; 1',

                'UPDATE Order; code[unique = true]; consignments(code)',
                '            ; expandTestOrder   ; expandTestConsignment'
        )

        when:
        def response = basicAuthRequest(EXPAND_SUPPORT_IO)
                .path("Orders")
                .queryParam('$expand', 'consignments/consignmentEntries/orderEntry/product/catalogVersion/catalog')
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then: 'catalog is expanded at the 6th level'
        response.status == 200
        def json = extractBody(response)
        json.getString("\$.d.results[0].consignments.results[0].consignmentEntries.results[0].orderEntry.product.catalogVersion.catalog.id") == CATALOG

        cleanup:
        IntegrationTestUtil.remove ConsignmentEntryModel, { it.quantity == 1 }
        IntegrationTestUtil.remove OrderEntryModel, { it.entryNumber == 1 }
        IntegrationTestUtil.remove UnitModel, { it.code == 'expandTestUnit' }
        IntegrationTestUtil.remove ConsignmentModel, { it.code == 'expandTestConsignment' }
        IntegrationTestUtil.remove VendorModel, { it.code == 'expandTestVendor' }
        IntegrationTestUtil.remove WarehouseModel, { it.code == 'expandTestWarehouse' }
        IntegrationTestUtil.findAny(OrderModel, { it.code == 'expandTestOrder' })
                .ifPresent({ order ->
                    IntegrationTestUtil.remove(AddressModel, { it.owner == order })
                })
        IntegrationTestUtil.remove OrderModel, { it.code == 'expandTestOrder' }
    }

    BasicAuthRequestBuilder basicAuthRequest(String serviceName) {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .credentials(USER, PASSWORD) // defined inside setup()
                .path(serviceName)
    }

    JsonObject extractBody(final Response response) {
        JsonObject.createFrom((InputStream) response.getEntity())
    }
}