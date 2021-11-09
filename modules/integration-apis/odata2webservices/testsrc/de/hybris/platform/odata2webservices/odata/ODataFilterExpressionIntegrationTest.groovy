/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.core.model.c2l.CountryModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.product.UnitModel
import de.hybris.platform.europe1.model.PriceRowModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.regex.Pattern

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

/**
 * WARNING: Don't use GString syntax (e.g. "$str1 $str2") to concatenate Strings in tests,
 * you will get an internal server error. Use + instead (e.g. str1 + str2).
 */
@IntegrationTest
class ODataFilterExpressionIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    private static final String IO = 'FilterTest'
    private static final Pattern INCOMPLETE_TIME_PATTERN = Pattern.compile(":[0-9]\$")

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setupSpec() {
        setupProduct()
        setupCart()
        setupPriceRow()
        setupCountry()
    }

    def setupProduct() {
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "                               ; $IO                ; INBOUND",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Product            ; Product",
                "                                   ; $IO                                   ; Catalog            ; Catalog",
                "                                   ; $IO                                   ; CatalogVersion     ; CatalogVersion",
                "                                   ; $IO                                   ; Category           ; Category",
                "                                   ; $IO                                   ; PriceRow           ; PriceRow",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $IO:Catalog                                                        ; id                          ; Catalog:id",
                "                                            ; $IO:CatalogVersion                                                 ; catalog                     ; CatalogVersion:catalog                             ; $IO:Catalog",
                "                                            ; $IO:CatalogVersion                                                 ; version                     ; CatalogVersion:version",
                "                                            ; $IO:Product                                                        ; code                        ; Product:code",
                "                                            ; $IO:Product                                                        ; name                        ; Product:name",
                "                                            ; $IO:Product                                                        ; creationtime                ; Product:creationtime",
                "                                            ; $IO:Product                                                        ; catalogVersion              ; Product:catalogVersion                             ; $IO:CatalogVersion",
                "                                            ; $IO:Product                                                        ; supercategories             ; Product:supercategories                            ; $IO:Category",
                "                                            ; $IO:Product                                                        ; pricerow                    ; Product:ownEurope1Prices                           ; $IO:PriceRow",
                "                                            ; $IO:Category                                                       ; code                        ; Category:code",
                "                                            ; $IO:Category                                                       ; name                        ; Category:name",
                "                                            ; $IO:Category                                                       ; products                    ; Category:products                                  ; $IO:Product",

                'INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;',
                '                     ; Default          ; Default        ; true',

                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;',
                '                            ; Default                   ; Staged                ; true',
                '                            ; Default                   ; Online                ; true'
        )
    }

    def setupCart() {
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "                               ; $IO                ; INBOUND",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Cart               ; Cart",
                "                                   ; $IO                                   ; Currency           ; Currency",
                "                                   ; $IO                                   ; User               ; User",
                "                                   ; $IO                                   ; Discount           ; Discount",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $IO:Cart                                                           ; code                        ; Cart:code                                          ;                                                           ; true",
                "                                            ; $IO:Cart                                                           ; currency                    ; Cart:currency                                      ; $IO:Currency",
                "                                            ; $IO:Cart                                                           ; user                        ; Cart:user                                          ; $IO:User",
                "                                            ; $IO:Cart                                                           ; discounts                   ; Cart:discounts                                     ; $IO:Discount",
                "                                            ; $IO:Cart                                                           ; date                        ; Cart:date",
                "                                            ; $IO:Currency                                                       ; isocode                     ; Currency:isocode                                   ;                                                           ; true",
                "                                            ; $IO:User                                                           ; uid                         ; User:uid                                           ;                                                           ; true",
                "                                            ; $IO:Discount                                                       ; code                        ; Discount:code                                      ;                                                           ; true"
        )
    }

    def setupPriceRow() {
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "                               ; $IO                ; INBOUND",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Currency           ; Currency",
                "                                   ; $IO                                   ; PriceRow           ; PriceRow",
                "                                   ; $IO                                   ; Unit               ; Unit",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $IO:Currency                                                       ; isocode                     ; Currency:isocode                                   ;                                                           ; true",
                "                                            ; $IO:PriceRow                                                       ; currency                    ; PriceRow:currency                                  ; $IO:Currency                                              ; true",
                "                                            ; $IO:PriceRow                                                       ; unit                        ; PriceRow:unit                                      ; $IO:Unit                                                  ;     ",
                "                                            ; $IO:PriceRow                                                       ; price                       ; PriceRow:price                                     ;                                                           ; true",
                "                                            ; $IO:Unit                                                           ; code                        ; Unit:code                                          ;                                                           ; true",
        )
    }

    def setupCountry() {
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "                                      ; $IO                ; INBOUND",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $IO                                   ; Country            ; Country   ; true",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $IO:Country                                                        ; isocode                     ; Country:isocode                                    ;                                                           ;true",
                "                                            ; $IO:Country                                                        ; name                        ; Country:name                                       ;                                                           ;",
                "                                            ; $IO:Country                                                        ; active                      ; Country:active                                     ;                                                           ;",
        )
    }

    def cleanup() {
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.removeAll CategoryModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll CartModel
        IntegrationTestUtil.removeAll PriceRowModel
        IntegrationTestUtil.removeAll UnitModel
        IntegrationTestUtil.removeSafely CountryModel, {
            it.isocode == 'DE' || it.isocode == 'FR' || it.isocode == 'EN_CA' || it.isocode == 'EN'
        }
        IntegrationTestUtil.removeSafely LanguageModel, { it.isocode == 'de' }
    }

    @Test
    def "can filter by navigation property integrationKey"() {
        given:
        importImpEx(
                '$stagedVersion = Default:Staged',
                '$onlineVersion = Default:Online',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                '                     ; prod1              ; $stagedVersion',
                '                     ; prod2              ; $onlineVersion',
                '                     ; prod3              ; $onlineVersion')
        def context = oDataContextForProducts(['$filter': "catalogVersion/integrationKey eq 'Staged|Default'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollection("d.results").size() == 1
        json.getString("d.results[0].code") == "prod1"
    }

    @Test
    @Unroll
    def "cannot filter navigation property integration key with operator #operator other than eq"() {
        given:
        importImpEx(
                '$stagedVersion = Default:Staged',
                '$onlineVersion = Default:Online',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)',
                '                     ; prod1              ; $stagedVersion',
                '                     ; prod2              ; $onlineVersion',
                '                     ; prod3              ; $onlineVersion')
        def context = oDataContextForProducts(['$filter': "catalogVersion/integrationKey " + operator + ' ' + "'Staged|Default'"])

        expect:
        def oDataResponse = facade.handleRequest(context)
        String oDataResponseContent = extractErrorFrom(oDataResponse).toString()
        oDataResponseContent.contains("For nested attribute filtering at someObject/IntegrationKey, logical operators other than 'eq' are not supported.")
        oDataResponseContent.contains("\"code\":\"operation_not_supported\"")

        where:
        operator << ['ne', 'lt', 'le', 'gt', 'ge']
    }

    @Test
    def "can filter by localized property using the default language system [en]"() {
        given:
        importImpEx(
                'INSERT_UPDATE Language; isocode[unique = true]',
                '                      ; de',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id)[unique = true], version); name[lang=en]',
                '                     ; prod1              ; Default:Staged                                     ; Product One',
                '                     ; prod2              ; Default:Staged                                     ; Product Two',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]; name[lang=de]',
                '                     ; prod1              ; Default:Staged                                     ; Produkt Eins')
        def context = oDataContextForProducts(['$filter': "name eq 'Product One'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollection("d.results").size() == 1
        json.getString("d.results[0].name") == 'Product One'
    }

    @Test
    @Unroll
    def "can filter by localized property name[lang=#acceptLanguage] = #filterValue"() {
        given:
        importImpEx(
                'INSERT_UPDATE Language; isocode[unique = true]',
                '                      ; de',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id)[unique = true], version); name[lang=en]',
                '                     ; prod1              ; Default:Staged                                     ; Product One',
                '                     ; prod2              ; Default:Staged                                     ; Product Two',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]; name[lang=de]',
                '                     ; prod1              ; Default:Staged                                     ; Produkt Eins')

        def context = oDataContextForProducts(['$filter': "name eq ${filterValue}".toString()], acceptLanguage)

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollection("d.results").size() == result.size()

        Arrays.equals(json.getCollectionOfObjects("d.results[*].name").toArray(), result.toArray())

        where:
        acceptLanguage | filterValue      | result
        'en'           | "'Product One'"  | ['Product One']
        'de'           | "'Product One'"  | []
        'de'           | "'Produkt Eins'" | ['Produkt Eins']
        'en'           | "'Produkt Eins'" | []
    }

    @Test
    def 'can filter by simple property when it has same name in integration object as in type system'() {
        given:
        importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]',
                '                     ; prod1              ; Default:Staged ',
                '                     ; prod2              ; Default:Online ',
                '                     ; prod2              ; Default:Staged ')
        def context = oDataContextForProducts(['$filter': "code eq 'prod2'", '$expand': 'catalogVersion'])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results[*].code") == ["prod2", "prod2"]
        json.getCollectionOfObjects("d.results[*].catalogVersion.version").containsAll(["Online", "Staged"])
    }

    @Issue('https://jira.hybris.com/browse/STOUT-3398')
    @Test
    def 'can filter by simple property when its name in integration object differs from the name in type system'() {
        given: 'property "code" is renamed to "productId" in integration object'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                'UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeDescriptor(enclosingType(code), qualifier)[unique = true]; attributeName',
                "                                     ; $IO:Product         ; Product:code                                                      ; productId")
        and: 'there these products existing in the platform'
        importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]',
                '                     ; prod1              ; Default:Staged ',
                '                     ; prod2              ; Default:Staged ')
        and: 'the request specifies filter by productId attribute'
        def context = oDataContextForProducts(['$filter': "productId eq 'prod1'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].productId') == ['prod1']

        cleanup:
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                'UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeDescriptor(enclosingType(code), qualifier)[unique = true]; attributeName',
                "                                     ; $IO:Product         ; Product:code                                                      ; code")
    }

    @Issue('https://jira.hybris.com/browse/STOUT-3398')
    @Test
    def 'can filter by navigation property that has different name in IO compared to the name in the type system'() {
        given: 'property "supercategories" is renamed to "categories" in integration object'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                'UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeDescriptor(enclosingType(code), qualifier)[unique = true]; attributeName',
                "                                     ; $IO:Product         ; Product:supercategories                                           ; categories")
        and: 'these products exists'
        importImpEx(
                'INSERT_UPDATE Category; code[unique = true]',
                '                      ; cat1',
                '                      ; cat2',
                'INSERT_UPDATE Product; code[unique = true]; supercategories(code); catalogVersion(catalog(id), version)[unique = true]',
                '                     ; prod1              ; cat1                 ; Default:Staged',
                '                     ; prod2              ; cat2                 ; Default:Staged')
        def context = oDataContextForProducts(['$filter': "categories/code eq 'cat1'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].code') == ['prod1']

        cleanup:
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                'UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeDescriptor(enclosingType(code), qualifier)[unique = true]; attributeName',
                "                                     ; $IO:Product         ; Product:supercategories                                           ; supercategories")
    }

    @Issue('https://jira.hybris.com/browse/IAPI-3667')
    @Test
    def 'can filter by navigation property that has many to one relation'() {
        given:
        importImpEx(
                'INSERT_UPDATE Catalog; id[unique = true]; name[lang = en];',
                '                     ; Catalog1          ; Catalog1             ;',
                '                     ; Catalog2          ; Catalog2             ;',

                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true];',
                '                            ; Catalog1                  ; Version 1a            ;',
                '                            ; Catalog1                  ; Version 1b            ;',
                '                            ; Catalog2                  ; Version 2             ;')

        def context = oDataContext(['$filter': "catalog/integrationKey eq 'Catalog1'"], 'CatalogVersions')

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].version').containsAll('Version 1a', 'Version 1b')

        cleanup:
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == 'Catalog1' || it.id == 'Catalog2' }
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version.startsWith 'Version' }
    }

    @Test
    def "can filter by inherited property"() {
        given:
        importImpEx(
                'INSERT_UPDATE Discount; code[unique = true]',
                '                      ; testDiscount1',
                '                      ; testDiscount2',
                'INSERT_UPDATE Currency; isocode[unique = true]; symbol',
                '                      ; USD                   ; USD',
                'INSERT_UPDATE Cart; code[unique = true]; discounts(code); currency(isocode); user(uid); date [dateformat=\'yyyy-MM-dd\']',
                '                  ; testCart1          ; testDiscount1  ; USD              ; admin    ;  2019-04-03',
                '                  ; testCart2          ; testDiscount2  ; USD              ; admin    ;  2019-04-03')

        def context = oDataContext(['$filter': "discounts/code eq 'testDiscount1'", '$expand': 'discounts'], 'Carts')

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects('d.results[*].code') == ['testCart1']
        json.getCollectionOfObjects('d.results[*].discounts.results[*].code') == ['testDiscount1']
    }

    @Test
    @Unroll
    def "can filter by nested key entity property: catalogVersion/version #operator #rightOperand"() {
        given:
        importImpEx(
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;',
                '                            ; Default                   ; aVersion                ; true',
                '                            ; Default                   ; bVersion                ; true',
                '                            ; Default                   ; cVersion                ; true',

                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]',
                '                     ; prod1              ; Default:Staged',
                '                     ; prod2              ; Default:Online',
                '                     ; prod3              ; Default:aVersion',
                '                     ; prod4              ; Default:bVersion',
                '                     ; prod5              ; Default:cVersion')

        expect:
        def context = oDataContextForProducts(['$filter': "catalogVersion/version " + operator + " " + rightOperand])
        ODataResponse response = facade.handleRequest(context)
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results[*].code") == result

        where:
        operator | rightOperand | result
        'eq'     | "'Staged'"   | ['prod1']
        'ne'     | "'Staged'"   | ['prod2', 'prod3', 'prod4', 'prod5']
        'le'     | "'cVersion'" | ['prod1', 'prod2', 'prod3', 'prod4', 'prod5']
        'lt'     | "'cVersion'" | ['prod1', 'prod2', 'prod3', 'prod4']
        'ge'     | "'aVersion'" | ['prod3', 'prod4', 'prod5']
        'gt'     | "'aVersion'" | ['prod4', 'prod5']
    }

    @Test
    @Unroll
    def "filter expression can contain '#operator' operator"() {
        given:
        importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]',
                '                     ; prod1              ; Default:Staged ',
                '                     ; prod1              ; Default:Online ',
                '                     ; prod2              ; Default:Staged ')
        def context = oDataContextForProducts(['$filter': "catalogVersion/integrationKey eq 'Staged|Default' " + operator + " code eq 'prod1'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollection("d.results").size() == result.size()
        json.getCollectionOfObjects("d.results[*].integrationKey").containsAll(result)

        where:
        operator | result
        'and'    | ['Staged|Default|prod1']
        'or'     | ['Staged|Default|prod1', 'Online|Default|prod1', 'Staged|Default|prod2']
    }

    @Test
    def "empty results returned when filter is not satisfied"() {
        given:
        def context = oDataContextForProducts(['$filter': "code eq 'some_product'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollection("d.results").isEmpty()
    }

    @Test
    def "filter is ignored when used with get an entity by ID"() {
        given:
        importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]',
                '                     ; pr                 ; Default:Staged')
        def context = oDataContextForProducts('Staged|Default|pr', ['$filter': "code eq 'prod1'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getString("d.code") == 'pr'
    }

    @Test
    @Unroll
    def "reports error when attempting to filter on non-existing property: #filterExpr"() {
        given:
        def context = oDataContextForProducts(['$filter': filterExpr])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractErrorFrom response
        !json.exists("error.code")
        json.getString("error.message.value").contains(filterExpr)

        where:
        filterExpr << ["catalogVersion-integrationKey eq 'Staged|Default'",
                       "code eq 'prod1' and catalogVersion-integrationKey eq 'Staged|Default'"]
    }

    @Test
    @Unroll
    def "filter expression with [#operator] logical operator [#left #operator #right]"() {
        given:
        importImpEx(
                'INSERT_UPDATE Unit;code[unique=true];unitType[unique=true];',
                '                         ;pieces           ;EA',
                'INSERT_UPDATE Currency;isocode[unique=true]',
                '                      ;USD                 ',
                'INSERT_UPDATE PriceRow;price[unique=true];currency(isocode)[unique=true];unit(code)[default=pieces]',
                '                      ;10.33             ;USD;',
                '                      ;15.55             ;USD;',
                '                      ;20.70             ;USD;')

        def context = oDataContextForPriceRows(['$filter': left + " " + operator + " " + right])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        Arrays.equals(json.getCollectionOfObjects("d.results[*].integrationKey").toArray(), result.toArray())

        where:
        left              | operator | right             | result
        'price'           | 'gt'     | '10.33M'          | ['USD|15.55', 'USD|20.7']
        'price'           | 'ge'     | '10.33M'          | ['USD|10.33', 'USD|15.55', 'USD|20.7']
        'price'           | 'lt'     | '15.55M'          | ['USD|10.33']
        'price'           | 'le'     | '15.55M'          | ['USD|10.33', 'USD|15.55']
        'price'           | 'ne'     | '15.55M'          | ['USD|10.33', 'USD|20.7']
        'price gt 10.33M' | 'and'    | 'price lt 20.70M' | ['USD|15.55']
        'price gt 10.33M' | 'and'    | 'price le 20.70M' | ['USD|15.55', 'USD|20.7']
        'price gt 10.33M' | 'and'    | 'price lt 15.55M' | []
        'price lt 10.33M' | 'or'     | 'price ge 20.70M' | ['USD|20.7']
        'price gt 20.70M' | 'or'     | 'price gt 25.70M' | []
    }

    @Issue('https://jira.hybris.com/browse/IAPI-3670')
    @Test
    @Unroll
    def "can filter by navigation property that has one to many relation: pricerow/price #operator 4"() {
        given:
        importImpEx(
                'INSERT_UPDATE Unit;code[unique=true];unitType[unique=true]',
                '                         ;pieces           ;EA',
                'INSERT_UPDATE Currency;isocode[unique=true]',
                '                      ;USD                 ',
                'INSERT_UPDATE PriceRow;price[unique=true];currency(isocode)[unique=true];unit(code)[default=pieces]',
                '                      ;10.33             ;USD;',
                '                      ;15.55             ;USD;',
                '                      ;20.70             ;USD;',
                '$prices=europe1prices[translator=de.hybris.platform.europe1.jalo.impex.Europe1PricesTranslator]',
                'INSERT_UPDATE Product;code[unique=true];$prices',
                '             ;prod1            ;1 pieces = 3 USD N',
                ';prod2            ;1 pieces = 5 USD N')

        def context = oDataContextForProducts(['$filter': "pricerow/price " + operator + " 4"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results[*].code") == result

        where:
        operator | result
        'lt'     | ["prod1"]
        'gt'     | ["prod2"]
    }

    @Test
    @Unroll
    def "order by [#orderBy] [#orderType]"() {
        given:
        importImpEx(
                'INSERT_UPDATE Unit;code[unique=true];unitType[unique=true];',
                '                         ;pieces           ;EA',
                'INSERT_UPDATE Currency;isocode[unique=true]',
                '                      ;USD                 ',
                'INSERT_UPDATE PriceRow;price[unique=true];currency(isocode)[unique=true];unit(code)[default=pieces]',
                '                      ;10.33             ;USD;',
                '                      ;20.70             ;USD;',
                '                      ;15.55             ;USD;')

        def context = oDataContextForPriceRows(['$orderby': orderBy + " " + orderType])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        Arrays.equals(json.getCollectionOfObjects("d.results[*].integrationKey").toArray(), result.toArray())

        where:
        orderBy | orderType | result
        'price' | 'asc'     | ['USD|10.33', 'USD|15.55', 'USD|20.7']
        'price' | 'desc'    | ['USD|20.7', 'USD|15.55', 'USD|10.33']
    }

    @Test
    @Unroll
    def "order by localized attributes #orderBy[lang=#acceptLanguage]"() {
        given:
        importImpEx(
                'INSERT_UPDATE Language; isocode[unique=true]',
                '                             ; de')
        importImpEx(
                'INSERT_UPDATE Country;isocode[unique=true] ;name[lang=de]   ;name[lang=en]    ;active',
                '                             ;DE                  ;Deutschland     ;Germany          ;true  ',
                '                             ;FR                  ;Frankreich      ;France           ;true  ',
                '                             ;EN_CA               ;Kanada          ;Canada           ;true  ',
                '                             ;EN                  ;Großbritannien  ;Britain          ;true  ')

        def context = oDataContextForCountries(['$orderby': orderBy + " " + orderType], acceptLanguage)

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        Arrays.equals(json.getCollectionOfObjects("d.results[*].integrationKey").toArray(), result.toArray())

        where:
        acceptLanguage | orderBy | orderType | result
        'en'           | 'name'  | 'asc'     | ['EN', 'EN_CA', 'FR', 'DE']
        'de'           | 'name'  | 'asc'     | ['DE', 'FR', 'EN', 'EN_CA']
    }

    @Test
    @Unroll
    def 'order by renamed localized attributes #orderBy[lang=#acceptLanguage]'() {
        given: 'property "name" is renamed to "aliasName" in integration object'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                'UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeDescriptor(enclosingType(code), qualifier)[unique = true]; attributeName',
                "                                     ; $IO:Country         ; Country:name                                                      ; aliasName")
        importImpEx(
                'INSERT_UPDATE Language; isocode[unique=true]',
                '                             ; de')
        importImpEx(
                'INSERT_UPDATE Country;isocode[unique=true] ;name[lang=de]   ;name[lang=en]    ;active',
                '                             ;FR                  ;Frankreich      ;France           ;true  ',
                '                             ;DE                  ;Deutschland     ;Germany          ;true  ',
                '                             ;EN_CA               ;Kanada          ;Canada           ;true  ',
                '                             ;EN                  ;Großbritannien  ;Britain          ;true  ')

        def context = oDataContextForCountries(['$orderby': orderBy + " " + orderType], acceptLanguage)

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        Arrays.equals(json.getCollectionOfObjects("d.results[*].integrationKey").toArray(), result.toArray())

        where:
        acceptLanguage | orderBy     | orderType | result
        'en'           | 'aliasName' | 'desc'    | ['DE', 'FR', 'EN_CA', 'EN']
        'de'           | 'aliasName' | 'desc'    | ['EN_CA', 'EN', 'FR', 'DE']
    }

    @Test
    def "order by nested attribute is not supported"() {
        given:
        def context = oDataContextForPriceRows(['$orderby': "currency/isocode"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractErrorFrom response
        json.getString("error.code") == "order_by_nested_attribute_not_supported"
        json.getString("error.message.value").contains("Ordering by nested attribute [currency/isocode] is not currently supported!")
    }

    @Test
    def "order by integrationKey is not supported"() {
        given:
        def context = oDataContextForPriceRows(['$orderby': "integrationKey"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractErrorFrom response
        json.getString("error.code") == "order_by_integration_key_not_supported"
        json.getString("error.message.value").contains("Ordering by integrationKey is not supported!")
    }

    @Test
    def "filtering by integration key is not supported"() {
        given:
        def context = oDataContextForProducts(['$filter': "integrationKey eq 'Staged|Default|prod'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractErrorFrom response
        json.getString("error.code") == "integration_key_not_supported"
        json.getString("error.message.value").contains("integration key is not supported")
    }

    @Test
    def "filtering by nested entity properties is not supported"() {
        given:
        def context = oDataContextForProducts(['$filter': "catalogVersion/catalog/id eq 'Default'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractErrorFrom response
        json.getString("error.code") == "filter_not_supported"
        json.getString("error.message.value").contains("catalogVersion/catalog")
    }

    @Test
    @Unroll
    def "can filter on property with many-to-many relationship by #property"() {
        given:
        importImpEx(
                'INSERT_UPDATE Category; code[unique = true]',
                '                      ; cat1',
                '                      ; cat2',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]; supercategories(code)',
                '                     ; prod1              ; Default:Staged                                     ; cat1',
                '                     ; prod2              ; Default:Staged                                     ; cat1, cat2')
        def context = oDataContextForProducts(['$filter': "supercategories/" + property + " eq 'cat2'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == 1
        json.getCollectionOfObjects("d.results[*].code") == ["prod2"]

        where:
        property << ["integrationKey", "code"]
    }

    @Test
    @Issue('https://jira.hybris.com/browse/STOUT-2241')
    def "can filter on property with many-to-many relationship by name when more than 1 item with name is found"() {
        given:
        importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; name',
                '                      ; cat1               ; sameName',
                '                      ; cat2               ; sameName',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]; supercategories(code)',
                '                     ; prod1              ; Default:Staged                                     ; cat1',
                '                     ; prod2              ; Default:Staged                                     ; cat1, cat2')
        def context = oDataContextForProducts(['$filter': "supercategories/name eq 'sameName'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == 2
        json.getCollectionOfObjects("d.results[*].code").containsAll(["prod1", "prod2"])
    }

    @Test
    def "can filter on property with many-to-many relationship, one-to-many relationship, and simple property"() {
        given:
        importImpEx(
                'INSERT_UPDATE Category; code[unique = true]',
                '                      ; cat1',
                '                      ; cat2',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]; supercategories(code)',
                '                     ; prod1              ; Default:Staged                                     ; cat1',
                '                     ; prod2              ; Default:Staged                                     ; cat2')
        def context = oDataContextForProducts(['$filter': "catalogVersion/integrationKey eq 'Staged|Default' and code eq 'prod2' or supercategories/integrationKey eq 'cat1'"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == 2
        json.getCollectionOfObjects("d.results[*].code") == ["prod1", "prod2"]
    }

    @Test
    @Unroll
    def "can filter reverse direction many-to-many relationship by #property"() {
        given:
        importImpEx(
                'INSERT_UPDATE Category; code[unique = true]',
                '                      ; cat1',
                '                      ; cat2',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]; supercategories(code)',
                '                     ; prod1              ; Default:Staged                                     ; cat1',
                '                     ; prod2              ; Default:Staged                                     ; cat1, cat2')
        def context = oDataContext(['$filter': "products/" + property + " eq '" + value + "'"], 'Categories')

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == 1
        json.getCollectionOfObjects("d.results[*].code") == ["cat1"]

        where:
        property         | value
        "integrationKey" | "Staged|Default|prod1"
        "code"           | "prod1"
    }

    @Issue('https://jira.hybris.com/browse/GRIFFIN-4021')
    @Test
    @Unroll
    def "filter products by creation time: filter=#left #operator #right"() {
        given:
        importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; creationtime[dateformat=yyyy-MM-dd HH:mm:ss.SSS]; catalogVersion(catalog(id), version)[default = Default:Staged]',
                '                            ; prod0              ; 2020-02-10 16:33:12.100                         ;',
                '                            ; prod1              ; 2020-02-11 16:33:12.110                         ;',
                '                            ; prod2              ; 2020-02-12 16:33:12.122                         ;',
                '                            ; prod3              ; 2020-02-13 16:33:12.133                         ;')

        def context = oDataContextForProducts(['$filter': "${left} ${operator} ${right}".toString()])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        Arrays.equals(json.getCollectionOfObjects("d.results[*].integrationKey").toArray(), result.toArray())

        where:
        left                                                                   | operator | right                                                                  | result
        "creationtime"                                                         | "eq"     | convertTimeZone("datetime", "2020-02-10T16:33:12.100")                 | ['Staged|Default|prod0']
        "creationtime"                                                         | "eq"     | convertTimeZone("datetime", "2020-02-10T16:33:12.10")                  | ['Staged|Default|prod0']
        "creationtime"                                                         | "eq"     | convertTimeZone("datetime", "2020-02-10T16:33:12.1")                   | ['Staged|Default|prod0']
        "creationtime"                                                         | "ge"     | convertTimeZone("datetime", "2020-02-10T16:33:12.10")                  | ['Staged|Default|prod0', 'Staged|Default|prod1', 'Staged|Default|prod2', 'Staged|Default|prod3']
        "creationtime"                                                         | "gt"     | convertTimeZone("datetime", "2020-02-10T16:33:12.1")                   | ['Staged|Default|prod1', 'Staged|Default|prod2', 'Staged|Default|prod3']
        "creationtime"                                                         | "gt"     | convertTimeZone("datetime", "2020-02-10T16:33:12")                     | ['Staged|Default|prod0', 'Staged|Default|prod1', 'Staged|Default|prod2', 'Staged|Default|prod3']
        "creationtime"                                                         | "gt"     | convertTimeZone("datetime", "2020-02-10T16:33:1")                      | ['Staged|Default|prod0', 'Staged|Default|prod1', 'Staged|Default|prod2', 'Staged|Default|prod3']
        "creationtime"                                                         | "ge"     | convertTimeZone("datetime", "2020-02-11T16:33")                        | ['Staged|Default|prod1', 'Staged|Default|prod2', 'Staged|Default|prod3']
        "creationtime"                                                         | "ge"     | convertTimeZone("datetime", "2020-02-11T16:3")                         | ['Staged|Default|prod1', 'Staged|Default|prod2', 'Staged|Default|prod3']
        "creationtime"                                                         | "eq"     | convertTimeZone("datetime", "2020-02-11T16:3")                         | []
        "creationtime"                                                         | "lt"     | convertTimeZone("datetime", "2020-02-13T16:33:12.133")                 | ['Staged|Default|prod0', 'Staged|Default|prod1', 'Staged|Default|prod2']
        "creationtime"                                                         | "le"     | convertTimeZone("datetime", "2020-02-13T16:33:12.133")                 | ['Staged|Default|prod0', 'Staged|Default|prod1', 'Staged|Default|prod2', 'Staged|Default|prod3']
        convertTimeZone("creationtime gt datetime", "2020-02-10T16:33:12.100") | "and"    | convertTimeZone("creationtime lt datetime", "2020-02-12T16:33:12.122") | ['Staged|Default|prod1']
        convertTimeZone("creationtime ge datetime", "2020-02-10T16:33:12.100") | "and"    | convertTimeZone("creationtime le datetime", "2020-02-12T16:33:12.122") | ['Staged|Default|prod0', 'Staged|Default|prod1', 'Staged|Default|prod2']
        convertTimeZone("creationtime eq datetime", "2020-02-10T16:33:12.100") | "or"     | convertTimeZone("creationtime eq datetime", "2020-02-12T16:33:12.122") | ['Staged|Default|prod0', 'Staged|Default|prod2']
    }

    ODataContext oDataContext(Map params, String entitySetName) {
        def request = requestBuilder(params, entitySetName).build()
        contextGenerator.generate request
    }

    ODataContext oDataContextForProducts(Map params) {
        def request = requestBuilder(params, 'Products').build()
        contextGenerator.generate request
    }

    ODataContext oDataContextForProducts(Map params, String language) {
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withParameters(params)
                .withAcceptLanguage(new Locale(language))
                .withPathInfo(pathInfo('Products', null)).build()
        contextGenerator.generate request
    }

    ODataContext oDataContextForProducts(String key, Map params) {
        def request = requestBuilder(params, 'Products', key).build()
        contextGenerator.generate request
    }

    ODataContext oDataContextForPriceRows(Map params) {
        def request = requestBuilder(params, 'PriceRows').build()
        contextGenerator.generate request
    }

    ODataContext oDataContextForCountries(Map params, String language) {
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withParameters(params)
                .withAcceptLanguage(new Locale(language))
                .withPathInfo(pathInfo('Countries')).build()
        contextGenerator.generate request
    }

    def requestBuilder(Map params, String entitySetName, String... keys) {
        ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withParameters(params)
                .withPathInfo(pathInfo(entitySetName, keys))
    }

    def pathInfo(String entitySetName, String... keys) {
        def builder = PathInfoBuilder.pathInfo()
                .withServiceName(IO)
                .withEntitySet(entitySetName)
        keys != null && keys.length > 0 ? builder.withEntityKeys(keys) : builder
    }

    def extractEntitiesFrom(ODataResponse response) {
        extractBodyWithExpectedStatus(response, HttpStatusCodes.OK)
    }

    def extractErrorFrom(ODataResponse response) {
        extractBodyWithExpectedStatus(response, HttpStatusCodes.BAD_REQUEST)
    }

    def extractBodyWithExpectedStatus(ODataResponse response, HttpStatusCodes expStatus) {
        assert response.getStatus() == expStatus
        JsonObject.createFrom response.getEntityAsStream()
    }

    def convertTimeZone(final String prefix, final String dateString) {

        def dateTime = LocalDateTime.parse(prepareDateTimeValue(dateString))
        def systemDateTime = dateTime.atZone(ZoneId.systemDefault())
        def utcDateTime = systemDateTime.withZoneSameInstant(ZoneOffset.UTC)
        def stringBuilder = new StringBuilder(prefix)
        stringBuilder.append("'").append(utcDateTime.toLocalDateTime().toString()).append("'")

        return stringBuilder.toString()
    }

    def prepareDateTimeValue(final String dateTimeValue) {
        final StringBuilder preparedDateTimeValue = new StringBuilder(dateTimeValue.replace("'", ""));
        return INCOMPLETE_TIME_PATTERN.matcher(dateTimeValue).find() ?
                preparedDateTimeValue.append("0").toString() : preparedDateTimeValue.toString();
    }

}
