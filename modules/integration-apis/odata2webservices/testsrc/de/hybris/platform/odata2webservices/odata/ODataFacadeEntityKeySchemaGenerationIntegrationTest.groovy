/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.XmlObject
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test

import javax.annotation.Resource

@IntegrationTest
class ODataFacadeEntityKeySchemaGenerationIntegrationTest extends ServicelayerSpockSpecification {
    static def IO = 'TestIO'

    @Resource(name = "defaultODataFacade")
    ODataFacade facade

    def setup() {
        importCsv("/impex/essentialdata-odata2services.impex", "UTF-8")
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true];',
                "                               ; $IO")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    def 'reports an error when a type system key attribute is missing in the item definition'() {
        given: 'IO item Product is defined without attributes for its keys in the type system, e.g. code, catalogVersion, etc'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Product            ; Product",
                '$item=integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)',
                "                                            ; $IO:Product         ; name                        ; Product:name")

        when: 'metadata is requested'
        def response = facade.handleGetSchema request()

        then: 'an error is reported'
        response.status == HttpStatusCodes.BAD_REQUEST
        def xml = XmlObject.createFrom response.entityAsStream
        xml.get('/error/code') == 'invalid_key_definition'
        xml.get('/error/message').contains 'Product'
    }

    @Test
    def 'reports an error when key attribute references a collection'() {
        given: 'rootCategories attribute in Catalog references a collection of Categories and is marked unique'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Catalog            ; Catalog",
                "                                   ; $IO                                   ; Category           ; Category",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$references=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor           ; $references ; unique[default = false]',
                "                                            ; $IO:Category        ; code                        ; Category:code",
                "                                            ; $IO:Catalog         ; id                          ; Catalog:id",
                "                                            ; $IO:Catalog         ; categories                  ; Catalog:rootCategories; $IO:Category; true")

        when: 'metadata is requested'
        def response = facade.handleGetSchema request()

        then: 'an error is reported'
        response.status == HttpStatusCodes.BAD_REQUEST
        def xml = XmlObject.createFrom response.entityAsStream
        xml.get('/error/code') == 'invalid_property_definition'
        xml.get('/error/message') == "Cannot generate unique navigation property for collections [Catalog.categories]"
    }

    @Test
    def 'reports an error when key attribute references a map'() {
        given: 'integration object contains a Map attribute "specialTreatmentClasses" with primitive key/value'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO                ",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $IO                                   ; Product            ; Product   ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $IO:Product                                                        ; code                        ; Product:code                                       ;                                                           ;",
                "                                            ; $IO:Product                                                        ; specialTreatmentClasses     ; Product:specialTreatmentClasses                    ;                                                           ; true",
        )

        when: 'metadata is requested'
        def response = facade.handleGetSchema request()

        then: 'an error is reported'
        response.status == HttpStatusCodes.BAD_REQUEST
        def xml = XmlObject.createFrom response.entityAsStream
        xml.get('/error/code') == 'invalid_property_definition'
        xml.get('/error/message').contains("Product.specialTreatmentClasses")
    }

    @Test
    def 'reports an error when key includes a localized attribute'() {
        given: 'localized attribute "name" is declared as key in the Catalog item'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Catalog            ; Catalog",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor ; unique[default = false]',
                "                                            ; $IO:Catalog         ; id                          ; Catalog:id",
                "                                            ; $IO:Catalog         ; name                        ; Catalog:name; true")

        when: 'metadata is requested'
        def response = facade.handleGetSchema request()

        then: 'an error is reported'
        response.status == HttpStatusCodes.BAD_REQUEST
        def xml = XmlObject.createFrom response.entityAsStream
        xml.get('/error/code') == 'misconfigured_attribute'
        with(xml.get('/error/message')) {
            contains 'Catalog'
            contains 'name'
        }
    }

    @Test
    def 'reports an error when key attribute reference forms a loop'() {
        given: 'a key attribute reference path forms a loop back to the item'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Customer           ; Customer",
                "                                   ; $IO                                   ; Address            ; Address",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$references=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor                   ; $references ; unique[default = false]',
                "                                            ; $IO:Customer        ; uid                         ; Customer:uid",
                "                                            ; $IO:Customer        ; defaultPaymentAddress       ; Customer:defaultPaymentAddress; $IO:Address ; true",
                "                                            ; $IO:Address         ; owner                       ; Address:owner                 ; $IO:Customer; true")

        when: 'metadata is requested'
        def response = facade.handleGetSchema request()

        then: 'an error is reported'
        response.status == HttpStatusCodes.BAD_REQUEST
        def xml = XmlObject.createFrom response.entityAsStream
        xml.get('/error/code') == 'misconfigured_attribute'
        xml.get('/error/message').contains 'circular'
    }

    @Test
    def 'generates simple key for an entity type'() {
        given: 'an item has only a simple key defined'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; Unit               ; Unit",
                '$item=integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)',
                "                                            ; $IO:Unit            ; code                        ; Unit:code")

        when: 'metadata is requested'
        def response = facade.handleGetSchema request()

        then: 'the key is generated'
        response.status == HttpStatusCodes.OK
        def xml = XmlObject.createFrom response.entityAsStream
        xml.get('//EntityType/Key/PropertyRef/@Name') == 'integrationKey'
        xml.get('//Property[@Name="code"]/@Nullable') == 'false'
        xml.get('//Property[@Name="code"]/@IsUnique') == 'true'
        xml.get('//Property[@Name="integrationKey"]/@Nullable') == 'false'
        xml.exists('//Property[@Name="integrationKey"]/@Alias')
        !xml.exists('//Property[@Name="integrationKey"]/@IsUnique')
    }

    /**
     * In Simple Terms this test attempts to create the Key a AttributeReferencePath with the attributeChain including the following IntegrationObjectItem codes:
     *
     * <ol>
     *     <li>  D -> B  </li>
     *     <li>  C -> B  </li>
     * </ol>
     * where, B is Address, D is Customer.defaultPaymentAddress and C is Customer.defaultShipmentAddress
     * See STOUT-2493 and/or STOUT-2528 for details
     */
    @Test
    def "generates metadata when item has key with same entityType in independent attribute reference paths"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO                ",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root',
                "                                   ; $IO                                   ; Customer           ; Customer  ; true",
                "                                   ; $IO                                   ; Address            ; Address   ; false",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$references=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor                    ; $references; unique[default = false]',
                "                                            ; $IO:Customer        ; uid                         ; Customer:uid",
                "                                            ; $IO:Customer        ; defaultPaymentAddress       ; Customer:defaultPaymentAddress ; $IO:Address; true",
                "                                            ; $IO:Customer        ; defaultShipmentAddress      ; Customer:defaultShipmentAddress; $IO:Address; true",
                "                                            ; $IO:Address         ; email                       ; Address:email                  ;            ; true")

        when:
        def response = facade.handleGetSchema request()

        then:
        response.status == HttpStatusCodes.OK
        def xml = XmlObject.createFrom response.entityAsStream
        xml.get('//EntityType/Key/PropertyRef/@Name') == 'integrationKey'
    }

    def request() {
        ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataGetRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO)
                        .withRequestPath('$metadata'))
    }
}
