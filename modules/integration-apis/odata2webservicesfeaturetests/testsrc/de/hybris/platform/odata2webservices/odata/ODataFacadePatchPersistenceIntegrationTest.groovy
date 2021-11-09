/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.c2l.CountryModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.user.AddressModel
import de.hybris.platform.core.model.user.CustomerModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItem2Model
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json

@IntegrationTest
class ODataFacadePatchPersistenceIntegrationTest extends ServicelayerTransactionalSpockSpecification {
    private static final def SERVICE_NAME = 'PatchTest'
    private static final def ENTITYSET = 'Customers'
    private static final def UID = 'jdoe'
    private static final String PRODUCT_CODE = "prod1code"

    @Resource(name = 'defaultODataFacade')
    private ODataFacade facade

    def setup() {
        // create test integration object
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "                               ; $SERVICE_NAME      ; INBOUND",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $SERVICE_NAME                         ; Customer           ; Customer",
                "                                   ; $SERVICE_NAME                         ; Address            ; Address",
                "                                   ; $SERVICE_NAME                         ; Country            ; Country",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]  ; $name[unique = true]; $attributeDescriptor       ; $attributeType        ; unique[default = false]',
                "                                            ; $SERVICE_NAME:Customer; uid                 ; Principal:uid",
                "                                            ; $SERVICE_NAME:Customer; shippingAddress     ; User:defaultShipmentAddress; $SERVICE_NAME:Address",
                "                                            ; $SERVICE_NAME:Customer; name                ; User:name",
                "                                            ; $SERVICE_NAME:Customer; loginDisabled       ; User:loginDisabled",
                "                                            ; $SERVICE_NAME:Address ; owner               ; Address:owner              ; $SERVICE_NAME:Customer; true",
                "                                            ; $SERVICE_NAME:Address ; email               ; Address:email              ;                       ; true",
                "                                            ; $SERVICE_NAME:Address ; country             ; Address:country            ; $SERVICE_NAME:Country",
                "                                            ; $SERVICE_NAME:Address ; appartment          ; Address:appartment",
                "                                            ; $SERVICE_NAME:Country ; code                ; Country:isocode            ;                       ; true",
                "                                            ; $SERVICE_NAME:Country ; active              ; Country:active",
                "                                            ; $SERVICE_NAME:Country ; name                ; Country:name")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.remove CustomerModel, { it.uid == "$UID" }
        IntegrationTestUtil.removeAll CountryModel
        IntegrationTestUtil.remove LanguageModel, { it.isocode != 'en'}
        IntegrationTestUtil.remove ProductModel, { it.code == "$PRODUCT_CODE" }
    }

    @Test
    def 'replaces the root item'() {
        given: 'a customer exists with login enabled'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Customer; uid[unique = true]; name   ; loginDisabled',
                "                      ; $UID              ; Stephen; false")
        and: 'the payload patches only loginDisabled attribute of the root item'
        def content = json()
                .withField('uid', UID)
                .withField('loginDisabled', true)

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response indicates success'
        response.status == HttpStatusCodes.OK
        and: 'the response body contains new value of the patched attribute'
        def json = JsonObject.createFrom response.entityAsStream
        json.getBoolean 'd.loginDisabled'
        and: 'the response body contains non-patched attributes'
        json.getString('d.name') == 'Stephen'
        and: 'the item is updated in the database'
        def item = getPersistedCustomer()
        item.loginDisabled
        and: 'attributes not present in the paylaod are not changed in the database'
        item.displayName == 'Stephen'
    }

    @Test
    def 'replaces a nested item attribute'() {
        given: 'a customer exists with an address'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Country; isocode[unique = true]; name; active',
                '                     ; us                    ; US  ; true',
                'INSERT_UPDATE Customer; uid[unique = true]; defaultShipmentAddress( &addrId )',
                "                      ; $UID              ; addr1",
                'INSERT_UPDATE Address; &addrId ; email[unique = true]; owner(Customer.uid); country(isocode)',
                "                     ; addr1   ; $UID@customers.io   ; $UID               ; us")
        and: 'the payload patches the nested country name only'
        def content = json()
                .withField('uid', UID)
                .withField('shippingAddress', json()
                        .withField('owner', json()
                            .withField('uid', UID))
                        .withField('email', "$UID@customers.io")
                        .withField('country', json()
                                .withCode('us')
                                .withField('name', 'USA')))

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the country name has changed in the database'
        def item = getPersistedCountry('us')
        item.name == 'USA'
        and: 'attribute not present in the payload is unchanged'
        item.active
    }

    @Test
    def 'replaces a nested item reference'() {
        given: 'a customer exists with a German address'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Country; isocode[unique = true]',
                '                     ; de',
                '                     ; us',
                'INSERT_UPDATE Customer; uid[unique = true]; defaultShipmentAddress( &addrId )',
                "                      ; $UID              ; addr1",
                'INSERT_UPDATE Address; &addrId; email[unique = true]; owner(Customer.uid); country(isocode)',
                "                     ; addr1  ; $UID@customers.io   ; $UID               ; de")
        and: 'the payload changes country to USA'
        def content = json()
                .withField('uid', UID)
                .withField('shippingAddress', json()
                        .withField('owner', json()
                            .withField('uid', UID))
                        .withField('email', "$UID@customers.io")
                        .withField('country', json().withCode('us')))

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the address is changed in the database'
        def customer = getPersistedCustomer()
        customer.defaultShipmentAddress.country.isocode == 'us'
        and: 'the previous country still exists in the database'
        getPersistedCountry('de')
    }

    @Test
    def 'patches a nested partOf item'() {
        given: 'the IO has an attribute referencing a partOf item type (User.userprofile)'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $SERVICE_NAME                         ; UserProfile        ; UserProfile",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]     ; $name[unique = true]; $attributeDescriptor         ; $attributeType           ; unique[default = false]',
                "                                            ; $SERVICE_NAME:Customer   ; profile             ; User:userprofile             ; $SERVICE_NAME:UserProfile",
                "                                            ; $SERVICE_NAME:UserProfile; owner               ; UserProfile:owner            ; $SERVICE_NAME:Customer   ; true",
                "                                            ; $SERVICE_NAME:UserProfile; expandInitial       ; UserProfile:expandInitial")
        and: 'a customer exists with an user profile'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Customer; uid[unique = true]; userprofile( &prof )',
                "                      ; $UID              ; p1",
                'INSERT_UPDATE UserProfile; &prof; owner(Customer.uid)[unique = true]; expandInitial',
                "                         ; p1   ; $UID                              ; false")
        and: 'the payload changes expandInitial attribute of the partOf UserProfile'
        def content = json()
                .withField('uid', UID)
                .withField('profile', json()
                        .withField('owner', json()
                                .withField('uid', UID))
                        .withField('expandInitial', true))

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the profile is changed in the database'
        getPersistedCustomer().userprofile.expandInitial
    }

    @Test
    def 'replaces a nested autoCreate item reference'() {
        given: 'a customer exists with a german address'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Country; isocode[unique = true]',
                '                     ; de',
                'INSERT_UPDATE Customer; uid[unique = true]; defaultShipmentAddress( &addrId )',
                "                      ; $UID              ; addr1",
                'INSERT_UPDATE Address; &addrId; email[unique = true]; owner(Customer.uid); country(isocode)',
                "                     ; addr1  ; $UID@customers.io   ; $UID               ; de")
        and: 'the integration object declares country attribute as autoCreate'
        IntegrationTestUtil.importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                'UPDATE IntegrationObjectItemAttribute; $item[unique = true] ; attributeName[unique = true]; autoCreate',
                "                                     ; $SERVICE_NAME:Address; country                     ; true")
        and: 'the payload changes country to Canada'
        def content = json()
                .withField('uid', UID)
                .withField('shippingAddress', json()
                        .withField('owner', json()
                            .withField('uid', UID))
                        .withField('email', "$UID@customers.io")
                        .withField('country', json().withCode('ca')))

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the address is changed in the database'
        def customer = getPersistedCustomer()
        customer.defaultShipmentAddress.country.isocode == 'ca'
        and: 'the previous country still exists in the database'
        getPersistedCountry('de')
    }

    @Test
    def 'patch does not change item PKs'() {
        given: 'a customer exists with an address'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Country; isocode[unique = true]; active',
                '                     ; us                    ; false',
                'INSERT_UPDATE Customer; uid[unique = true]; defaultShipmentAddress( &addrId ); loginDisabled',
                "                      ; $UID              ; addr1                            ; true",
                'INSERT_UPDATE Address; &addrId; email[unique = true]; owner(Customer.uid); country(isocode)',
                "                     ; addr1  ; $UID@customers.io   ; $UID               ; us")
        def rootItemPk = getPersistedCustomer().pk
        def nestedItemPk = getPersistedCustomer().defaultShipmentAddress.country.pk
        and: 'the payload patches attributes of the root and the nested items'
        def content = json()
                .withField('uid', UID)
                .withField('loginDisabled', false)
                .withField('shippingAddress', json()
                        .withField('owner', json()
                                .withField('uid', UID))
                        .withField('email', "$UID@customers.io")
                        .withField('country', json()
                                .withCode('us')
                                .withField('active', true)))

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the PKs of the patched items did not change'
        getPersistedCustomer().pk == rootItemPk
        getPersistedCountry('us').pk == nestedItemPk
    }

    @Test
    def 'replaces an item collection'() {
        given: 'the IO has an attribute of simple collection type (UserProfile.readableLanguages)'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $SERVICE_NAME                         ; UserProfile        ; UserProfile",
                "                                   ; $SERVICE_NAME                         ; Language           ; Language",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]     ; $name[unique = true]; $attributeDescriptor         ; $attributeType           ; unique[default = false]',
                "                                            ; $SERVICE_NAME:Customer   ; profile             ; User:userprofile             ; $SERVICE_NAME:UserProfile",
                "                                            ; $SERVICE_NAME:UserProfile; owner               ; UserProfile:owner            ; $SERVICE_NAME:Customer   ; true",
                "                                            ; $SERVICE_NAME:UserProfile; languages           ; UserProfile:readableLanguages; $SERVICE_NAME:Language",
                "                                            ; $SERVICE_NAME:Language   ; code                ; Language:isocode")
        and: 'a customer exists with german and spanish languages'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Language; isocode[unique = true]',
                '                      ; en',
                '                      ; de',
                '                      ; es',
                '                      ; fr',
                'INSERT_UPDATE Customer; uid[unique = true]; userprofile(&profilePk)',
                "                      ; $UID              ; prof1",
                'INSERT_UPDATE UserProfile; &profilePk; owner(Customer.uid)[unique = true]; readableLanguages(isocode)',
                "                         ; prof1     ; $UID                              ; de,es")
        and: 'the payload changes languages to english and french'
        def content = json()
                .withField('uid', UID)
                .withField('profile', json()
                        .withField('owner', json()
                            .withField('uid', UID))
                        .withField('languages', [json().withCode('en'), json().withCode('fr')]))

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the customer languages changed in the database'
        getPersistedCustomer().userprofile.readableLanguages.collect { it.isocode } == ['en', 'fr']
        and: 'the old languages still exist'
        getPersistedLanguage 'de'
        getPersistedLanguage 'es'
    }

    @Test
    def 'replaces a partOf item collection'() {
        given: 'the IO has an attribute of partOf collection type (Customer.addresses)'
        IntegrationTestUtil.importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]  ; $name[unique = true]; $attributeDescriptor; $attributeType',
                "                                            ; $SERVICE_NAME:Customer; addresses           ; User:addresses      ; $SERVICE_NAME:Address")
        and: 'a customer exists with german and spanish email addresses'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Customer; uid[unique = true]; addresses(&addrPk)',
                "                      ; $UID              ; a1,a2",
                'INSERT_UPDATE Address; &addrPk; email[unique = true]; owner(Customer.uid)',
                "                     ; a1     ; $UID@customers.de   ; $UID",
                "                     ; a2     ; $UID@customers.sp   ; $UID")
        and: 'the payload changes email addresses to english and french'
        def content = json()
                .withField('uid', UID)
                .withField('addresses', [
                        json().withField('owner', json().withField('uid', UID)).withField('email', "$UID@customers.us"),
                        json().withField('owner', json().withField('uid', UID)).withField('email', "$UID@customers.fr")])

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the customer languages changed in the database'
        def addresses = getPersistedCustomer().addresses.collect { it.email }
        addresses.size() == 2
        addresses.containsAll([UID + '@customers.us', UID + '@customers.fr'])
        and: 'the old addresses are deleted'
        !getPersistedAddress("$UID@customers.de")
        !getPersistedAddress("$UID@customers.sp")
    }

    @Test
    def 'replaces an autoCreate item collection'() {
        given: 'the IO has an attribute of simple collection type (UserProfile.readableLanguages)'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $SERVICE_NAME                         ; UserProfile        ; UserProfile",
                "                                   ; $SERVICE_NAME                         ; Language           ; Language",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]     ; $name[unique = true]; $attributeDescriptor         ; $attributeType           ; unique[default = false]; autoCreate[default = false]',
                "                                            ; $SERVICE_NAME:Customer   ; profile             ; User:userprofile             ; $SERVICE_NAME:UserProfile",
                "                                            ; $SERVICE_NAME:UserProfile; owner               ; UserProfile:owner            ; $SERVICE_NAME:Customer   ; true",
                "                                            ; $SERVICE_NAME:UserProfile; languages           ; UserProfile:readableLanguages; $SERVICE_NAME:Language   ;                        ; true",
                "                                            ; $SERVICE_NAME:Language   ; code                ; Language:isocode")
        and: 'a customer exists with german and spanish languages'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Language; isocode[unique = true]',
                '                      ; de',
                '                      ; es',
                'INSERT_UPDATE Customer; uid[unique = true]; userprofile(&profilePk)',
                "                      ; $UID              ; prof1",
                'INSERT_UPDATE UserProfile; &profilePk; owner(Customer.uid)[unique = true]; readableLanguages(isocode)',
                "                         ; prof1     ; $UID                              ; de,es")
        and: 'the payload changes languages to english and french'
        def content = json()
                .withField('uid', UID)
                .withField('profile', json()
                        .withField('owner', json().withField('uid', UID))
                        .withField('languages', [json().withCode('en'), json().withCode('fr')]))

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the customer languages changed in the database'
        getPersistedCustomer().userprofile.readableLanguages.collect { it.isocode } == ['en', 'fr']
        and: 'the old languages still exist'
        getPersistedLanguage 'de'
        getPersistedLanguage 'es'
    }

    @Test
    def 'replaces localized attribute in an item'() {
        given: 'a country exists with german and french name values'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Language; isocode[unique = true]',
                '                      ; de',
                '                      ; es',
                '                      ; fr',
                'INSERT_UPDATE Country; isocode[unique = true]; name[lang = de]; name[lang = fr]',
                '                     ; de                    ; Deutschland    ; Allemagne')
        and: 'the payload patches the name with english and spanish values'
        def content = json()
                .withCode('de')
                .withLocalizedAttributes([language: 'en', name: 'Germany'], [language: 'es', name: 'Alemania'])
        when:
        def response = facade.handleRequest patch('Countries', 'de', content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the new names exist for the customer in the database'
        def item = getPersistedCountry('de')
        item.getName(Locale.ENGLISH) == 'Germany'
        item.getName(Locale.forLanguageTag('es')) == 'Alemania'
        and: 'the old names deleted for the customer in the database'
        !item.getName(Locale.FRENCH)
        !item.getName(Locale.GERMAN)
    }

    @Test
    def 'does not reset an item attribute value when payload does not contain the attribute'() {
        given: 'the IO has an attribute of simple collection type (UserProfile.readableLanguages)'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $SERVICE_NAME                         ; UserProfile        ; UserProfile",
                "                                   ; $SERVICE_NAME                         ; Language           ; Language",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]     ; $name[unique = true]; $attributeDescriptor         ; $attributeType           ; unique[default = false]',
                "                                            ; $SERVICE_NAME:Customer   ; profile             ; User:userprofile             ; $SERVICE_NAME:UserProfile",
                "                                            ; $SERVICE_NAME:UserProfile; owner               ; UserProfile:owner            ; $SERVICE_NAME:Customer   ; true",
                "                                            ; $SERVICE_NAME:UserProfile; languages           ; UserProfile:readableLanguages; $SERVICE_NAME:Language",
                "                                            ; $SERVICE_NAME:Language   ; code                ; Language:isocode")
        and: 'a customer exists with english and spanish languages'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Language; isocode[unique = true]',
                '                      ; en',
                '                      ; es',
                'INSERT_UPDATE Customer; uid[unique = true]; userprofile(&profilePk)',
                "                      ; $UID              ; prof1",
                'INSERT_UPDATE UserProfile; &profilePk; owner(Customer.uid)[unique = true]; readableLanguages(isocode)',
                "                         ; prof1     ; $UID                              ; en, es")
        and: 'the payload does not contain languages attribute'
        def content = json()
                .withField('uid', UID)
                .withField('profile', json().withField('owner', json().withField('uid', UID)))

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the customer languages unchanged in the database'
        getPersistedCustomer().userprofile.readableLanguages.collect { it.isocode } == ['en', 'es']
    }

    @Test
    def 'resets an item attribute value when payload contains explicit [] value for the attribute'() {
        given: 'the IO has an attribute of simple collection type (UserProfile.readableLanguages)'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $SERVICE_NAME                         ; UserProfile        ; UserProfile",
                "                                   ; $SERVICE_NAME                         ; Language           ; Language",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]     ; $name[unique = true]; $attributeDescriptor         ; $attributeType           ; unique[default = false]',
                "                                            ; $SERVICE_NAME:Customer   ; profile             ; User:userprofile             ; $SERVICE_NAME:UserProfile",
                "                                            ; $SERVICE_NAME:UserProfile; owner               ; UserProfile:owner            ; $SERVICE_NAME:Customer   ; true",
                "                                            ; $SERVICE_NAME:UserProfile; languages           ; UserProfile:readableLanguages; $SERVICE_NAME:Language",
                "                                            ; $SERVICE_NAME:Language   ; code                ; Language:isocode")
        and: 'a customer exists with english and spanish languages'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Language; isocode[unique = true]',
                '                      ; en',
                '                      ; es',
                'INSERT_UPDATE Customer; uid[unique = true]; userprofile(&profilePk)',
                "                      ; $UID              ; prof1",
                'INSERT_UPDATE UserProfile; &profilePk; owner(Customer.uid)[unique = true]; readableLanguages(isocode)',
                "                         ; prof1     ; $UID                              ; en, es")
        and: "the payload contains languages attribute value"
        def content = json()
                .withField('uid', UID)
                .withField('profile', json()
                        .withField('owner', json()
                                .withField('uid', UID))
                        .withField('languages', []))

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then: 'the response is successful'
        response.status == HttpStatusCodes.OK
        and: 'the customer languages are reset in the database'
        getPersistedCustomer().userprofile.readableLanguages == []
    }

    @Test
    @Unroll
    def "replaces a primitive localized attribute value with #value"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Customer; uid[unique = true]; name;     userprofile(&profilePk)',
                "                      ; $UID              ; John Doe; prof1",
                'INSERT_UPDATE UserProfile; &profilePk; owner(Customer.uid)[unique = true]',
                "                         ; prof1     ; $UID")

        and:
        def content = json()
                        .withField('uid', UID)
                        .withField('name', value)

        when:
        def response = facade.handleRequest patchCustomers(UID, content)

        then:
        response.status == HttpStatusCodes.OK
        getPersistedCustomer().name == value

        where:
        value << ['Jane Doe', '', null]
    }

    @Test
    def 'updates fields provided in payload when request body is missing key navigation property [Address.country]'() {
        given: 'an Address exists with a Customer as a key navigationProperty'
        final String email = 'kb@sap.com'
        final String code = 'CountryCode'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Customer; uid[unique = true]; name   ; loginDisabled',
                "                      ; $UID              ; Stephen; false",
                'INSERT_UPDATE Address; email[unique = true]; owner(Customer.uid) ; appartment[unique = true]',
                "                     ; $email              ; $UID                ; oldAppartment"
        )

        and: 'the payload is missing a key navigation property'
        def content = json()
                .withField('appartment', 'updatedAddressAppartment')

        when: 'a patch Addresses request is made'
        def response = facade.handleRequest patch('Addresses', "$email|$UID" , content)

        then: 'the product name is updated'
        response.status == HttpStatusCodes.OK
        and:
        getPersistedAddress(email).appartment == "updatedAddressAppartment"
    }
    
    @Test
    def 'updates fields provided in payload when request body is missing key property [Customer.id]'() {
        given: 'a Customer exists'
        final String newName = 'newCustomerName'

        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Customer; uid[unique = true]; name   ; loginDisabled',
                "                      ; $UID              ; Stephen; false"
        )

        and: 'the payload is missing a key property'
        def content = json()
                .withField('name', newName)

        when: 'a patch request is made for a customer'
        def response = facade.handleRequest patchCustomers(UID , content)

        then: 'the product name is updated'
        response.status == HttpStatusCodes.OK
        and:
        getPersistedCustomer().name == newName
    }

    @Test
    def 'required non-key properties do not need to be provided for patch'() {
        given: 'TestIntegrationItem2 with requiredName is defined'
        final String itemKey = 'keyCode'
        IntegrationTestUtil.importImpEx(
        '$item=integrationObjectItem(integrationObject(code), code)',
        "INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique=true]; code[unique = true]  ; type(code)",
        "                                   ; $SERVICE_NAME                       ; TestIntegrationItem2 ; TestIntegrationItem2",
        'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]                ; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
        "                                            ; $SERVICE_NAME:TestIntegrationItem2  ; code                        ; TestIntegrationItem2:code",
        "                                            ; $SERVICE_NAME:TestIntegrationItem2  ; requiredName                ; TestIntegrationItem2:requiredName",
        "                                            ; $SERVICE_NAME:TestIntegrationItem2  ; requiredStringMap           ; TestIntegrationItem2:requiredStringMap",
        "                                            ; $SERVICE_NAME:TestIntegrationItem2  ; optionalSimpleAttr          ; TestIntegrationItem2:optionalSimpleAttr"
        )

        and: 'a TestIntegrationItem2 item exists'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE TestIntegrationItem2; code[unique = true]; requiredName  ; optionalSimpleAttr; requiredStringMap(key, value)[map-delimiter = |]',
                "                                  ; $itemKey           ; nameNameValue ; simpleAttrValue   ; testKey->testVal"
        )

        and: 'the payload does not provide a value for a required non-key attribute'
        final String newSimpleAttrValue = "updatedSimpleAttrValue"
        def content = json()
                .withField('code', itemKey)
                .withField('optionalSimpleAttr', newSimpleAttrValue)

        when: 'a patch request is made for a TestIntegrationItem2s'
        def response = facade.handleRequest patchTestIntegrationItem2s(itemKey , content)

        then: 'the product name is updated'
        response.status == HttpStatusCodes.OK
        and:
        getPersistedTestIntegrationItem2(itemKey).optionalSimpleAttr == newSimpleAttrValue
    }

    private static TestIntegrationItem2Model getPersistedTestIntegrationItem2(String code) {
        IntegrationTestUtil.findAny(TestIntegrationItem2Model, { it.code == code }).orElse(null)
    }

    private static CustomerModel getPersistedCustomer() {
        IntegrationTestUtil.findAny(CustomerModel, { it.uid == UID }).orElse(null)
    }

    private static AddressModel getPersistedAddress(String email) {
        IntegrationTestUtil.findAny(AddressModel, { it.owner.uid == UID && it.email == email }).orElse(null)
    }

    private static CountryModel getPersistedCountry(String code) {
        IntegrationTestUtil.findAny(CountryModel, { it.isocode == code }).orElse(null)
    }

    private static LanguageModel getPersistedLanguage(String code) {
        IntegrationTestUtil.findAny(LanguageModel, { it.isocode == code }).orElse(null)
    }

    static ODataContext patchTestIntegrationItem2s(String key, JsonBuilder body) {
        patch "TestIntegrationItem2s", key, body
    }

    static ODataContext patchCustomers(String key, JsonBuilder body) {
        patch ENTITYSET, key, body
    }

    static ODataContext patch(String entitySet, String key, JsonBuilder body) {
        patch(SERVICE_NAME, entitySet, key, body)
    }

    static ODataContext patch(String serviceName, String entitySet, String key, JsonBuilder body) {
        ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataPatchRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(serviceName)
                        .withEntitySet(entitySet)
                        .withEntityKeys(key))
                .withContentType('application/json')
                .withAccepts('application/json')
                .withBody(body)
    }
}
