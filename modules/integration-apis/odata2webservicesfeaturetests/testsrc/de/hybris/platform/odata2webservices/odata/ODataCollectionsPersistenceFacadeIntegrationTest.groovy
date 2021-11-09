/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.catalog.model.CompanyModel
import de.hybris.platform.core.model.media.MediaFolderModel
import de.hybris.platform.core.model.media.MediaModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservicesfeaturetests.model.ComplexTestIntegrationItemModel
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItemDetailModel
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.model.ModelService
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataCollectionsPersistenceFacadeIntegrationTest extends ServicelayerSpockSpecification {
    private static final String IO = 'TestIO'
    private static final String COMPANY_ID = 'acme'
    private static final String CATALOG_ID = 'Software'
    private static final String CATALOG_VERSION = 'Test'
    private static final String ITEM_ID = 'test_item'

    @Resource
    private ModelService modelService
    @Resource
    private CatalogVersionService catalogVersionService

    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setupSpec() {
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true];',
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code) ; root[default = false]',
                "                                   ; $IO                                   ; Company            ; Company",
                "                                   ; $IO                                   ; Address            ; Address",
                "                                   ; $IO                                   ; Master             ; ComplexTestIntegrationItem",
                "                                   ; $IO                                   ; Detail             ; TestIntegrationItemDetail",
                "                                   ; $IO                                   ; Media              ; Media",
                "                                   ; $IO                                   ; Catalog            ; Catalog",
                "                                   ; $IO                                   ; CatalogVersion     ; CatalogVersion",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor                               ; $refType   ; unique[default = false]',
                "                                            ; $IO:Company         ; id                          ; Company:uid                               ;",
                "                                            ; $IO:Company         ; medias                      ; Company:medias                            ; $IO:Media",
                "                                            ; $IO:Company         ; addresses                   ; Company:addresses; $IO:Address            ;",
                "                                            ; $IO:Address         ; email                       ; Address:email                             ;            ; true",
                "                                            ; $IO:Master          ; code                        ; ComplexTestIntegrationItem:code           ;",
                "                                            ; $IO:Master          ; requiredDetails             ; ComplexTestIntegrationItem:requiredDetails; $IO:Detail",
                "                                            ; $IO:Detail          ; code                        ; TestIntegrationItemDetail:code            ;",
                "                                            ; $IO:Media           ; code                        ; Media:code                                ;",
                "                                            ; $IO:Media           ; text                        ; Media:altText                             ;",
                "                                            ; $IO:Media           ; catalogVersion              ; Media:catalogVersion                      ; $IO:CatalogVersion",
                "                                            ; $IO:CatalogVersion  ; catalog                     ; CatalogVersion:catalog                    ; $IO:Catalog",
                "                                            ; $IO:CatalogVersion  ; version                     ; CatalogVersion:version",
                "                                            ; $IO:Catalog         ; id                          ; Catalog:id",
                'INSERT_UPDATE Catalog; id[unique = true]',
                "                     ; $CATALOG_ID",
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]',
                "                            ; $CATALOG_ID               ; $CATALOG_VERSION")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll CompanyModel
        IntegrationTestUtil.removeAll MediaModel
        IntegrationTestUtil.removeAll ComplexTestIntegrationItemModel
        IntegrationTestUtil.removeAll TestIntegrationItemDetailModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.remove CatalogVersionModel, { it.version == CATALOG_VERSION }
        IntegrationTestUtil.remove CatalogModel, { it.id == CATALOG_ID }
    }

    @Test
    @Unroll
    def "POST creates item when optional collection attribute is #condition in the payload"() {
        given: 'request with payload'
        def request = post('Companies').withBody(payload)

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the company exists'
        persistedCompany().present

        where:
        condition | payload
        'null'    | company().withField('addresses', null as List)
        'empty'   | company().withField('addresses', [])
        'absent'  | company()
    }

    @Test
    @Unroll
    def "POST does not update collection attribute when payload value is #condition"() {
        given: 'an IO with collection attribute'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor      ; $refType   ; autoCreate',
                "                                            ; $IO:Company         ; addresses                   ; Company:addresses; $IO:Address; false")
        and: 'the company exists with an address'
        importImpEx(
                'INSERT Company; uid[unique = true]; addresses(&addrPk)',
                "              ; $COMPANY_ID       ; addr",
                'INSERT Address; &addrPk; owner(Company.uid); email',
                "              ; addr   ; $COMPANY_ID       ; men@work")
        and: 'request with payload'
        def request = post('Companies').withBody payload

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'item in the database did not change'
        persistedCompany()
                .map({ it.addresses.collect({ addr -> addr.email }) })
                .orElse([]) == ['men@work']

        where:
        condition | payload
        'null'    | company().withField('addresses', null as List)
        'empty'   | company().withField('addresses', [])
        'missing' | company()
    }

    @Test
    @Unroll
    def "POST fails to create item when its required collection attribute is #condition in the payload"() {
        given: 'request with payload'
        def request = post('Masters').withBody(payload)

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'Master'
        json.getString('error.innererror') == ITEM_ID
        and: 'the item not created'
        persistedItem().empty

        where:
        condition | payload
        'null'    | item().withField('requiredDetails', null as List)
        'absent'  | item()
    }

    @Test
    def 'POST creates item when its required collection attribute is empty in the payload'() {
        given: 'request with payload'
        def request = post('Masters').withBody item().withField('requiredDetails', [])

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the item created with empty required details'
        persistedItem().map({ it.requiredDetails }).orElse(null) == [] as Set
    }

    @Test
    @Unroll
    def "POST updates existing item when its required collection attribute is #condition in the payload"() {
        given: 'an IO with required collection attribute'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor                               ; $refType  ; autoCreate',
                "                                            ; $IO:Master          ; requiredDetails             ; ComplexTestIntegrationItem:requiredDetails; $IO:Detail; false")
        and: 'the item already exists'
        importImpEx(
                'INSERT ComplexTestIntegrationItem; code[unique = true]; requiredDetails(&detailPk)',
                "                                 ; $ITEM_ID           ; detail1",
                'INSERT TestIntegrationItemDetail; &detailPk; code; master(code)',
                "                                ; detail1  ; d1  ; $ITEM_ID")
        and: 'request with payload'
        def request = post('Masters').withBody(payload)

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the item is still associated with its detail'
        persistedItem().map({ it.requiredDetails.collect({ it.code }) }).orElse([]) == ['d1']

        where:
        condition | payload
        'null'    | item().withField('requiredDetails', null as List)
        'empty'   | item().withField('requiredDetails', [])
        'absent'  | item()
    }

    @Test
    def 'POST updates collection attribute by appending items referenced in the payload'() {
        given: 'an IO with collection attribute'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor      ; $refType   ; autoCreate',
                "                                            ; $IO:Company         ; addresses                   ; Company:addresses; $IO:Address; false")
        and: 'the company exists with an address'
        importImpEx(
                'INSERT Company; uid[unique = true]; addresses(&addrPk)',
                "              ; $COMPANY_ID       ; addr",
                'INSERT Address; &addrPk; owner(Company.uid); email',
                "              ; addr   ; $COMPANY_ID       ; email@1")
        and: 'request with payload'
        def addresses = [address('email@2'), address('email@1'), address('email@3')]
        def request = post('Companies').withBody company().withField('addresses', addresses)

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'new values from the payload are appended to the item in the database'
        persistedCompany()
                .map({ it.addresses.collect({ addr -> addr.email }) })
                .orElse([])
                .containsAll(['email@1', 'email@2', 'email@3'])
        and: 'owner is assigned in the new addresses'
        persistedCompany()
                .map({ it.addresses.collect({ addr -> addr.owner.uid }) })
                .orElse([]) == [COMPANY_ID, COMPANY_ID, COMPANY_ID]
    }

    @Test
    def 'POST updates items referenced in a collection attribute payload'() {
        given: 'an IO with a collection attribute "medias"'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor   ; $refType   ; autoCreate',
                "                                            ; $IO:Company         ; medias                      ; Company:medias; $IO:Media  ; false")
        and: 'the following media exist'
        importImpEx(
                'INSERT Media; code[unique = true]; catalogVersion(catalog(id), version)',
                "            ; m1                 ; $CATALOG_ID:$CATALOG_VERSION")
        and: 'the company exists without medias'
        importImpEx(
                'INSERT Company; uid[unique = true]',
                "              ; $COMPANY_ID"
        )
        and: 'request payload contains a reference to the existing media, where media has more attributes than in the database'
        def request = post('Companies').withBody(company().withFieldValues('medias', media('m1').withField('text', 'some text')))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the persisted media is updated'
        def media = persistedMedia('m1').orElse(null)
        media?.altText == 'some text'
        and: 'the persisted company is associated to the medias'
        persistedCompany()
                .map({ it.medias })
                .orElse([]) == [media]
    }

    @Test
    def 'POST creates an item when collection attribute references only existing items in the payload'() {
        given: 'an IO with optional collection attribute'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor            ; $refType ; autoCreate',
                "                                            ; $IO:Company         ; medias                      ; Company:medias         ; $IO:Media; false")
        and: 'the following medias exist'
        importImpEx(
                'INSERT_UPDATE Media; code[unique = true]; catalogVersion(catalog(id), version)',
                "                   ; media-100          ; $CATALOG_ID:$CATALOG_VERSION",
                "                   ; media-200          ; $CATALOG_ID:$CATALOG_VERSION")
        and: 'request payload contains references to the existing media'
        def request = post('Companies').withBody(company().withFieldValues('medias', media('media-100'), media('media-200')))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the persisted company is associated to the medias'
        persistedCompany()
                .map({ it.medias.collect({ m -> m.code }) })
                .orElse([])
                .containsAll(['media-100', 'media-200'])
        and: 'medias are not associated with the company'
        IntegrationTestUtil.findAll(MediaModel).forEach {
            assert it.owner == null
        }
    }

    @Test
    def 'POST fails when not a partOf collection attribute references at least one not existing item in the payload'() {
        given: 'an IO with optional collection attribute'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor            ; $refType ; autoCreate',
                "                                            ; $IO:Company         ; medias                      ; Company:medias         ; $IO:Media; false")
        and: 'only one media exists'
        importImpEx(
                'INSERT_UPDATE Media; code[unique = true]; catalogVersion(catalog(id), version)',
                "                   ; media-200          ; $CATALOG_ID:$CATALOG_VERSION")
        and: 'request payload contains references to the existing and not existing media'
        def request = post('Companies').withBody(company().withFieldValues('medias', media('media-100'), media('media-200')))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'Company'
        json.getString('error.message.value').contains 'medias'
        json.getString('error.innererror') == COMPANY_ID
        and: 'the company is not persisted'
        persistedCompany().empty
    }

    @Test
    def 'POST creates not existing items referenced in a collection attribute payload when the attribute is autoCreate'() {
        given: 'an IO with an autoCreate collection attribute'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor            ; $attributeType    ; autoCreate',
                "                                            ; $IO:Company         ; medias                      ; Company:medias         ; $IO:Media         ; true")
        and: 'only one media exists'
        importImpEx(
                'INSERT_UPDATE Media; code[unique = true]; catalogVersion(catalog(id), version)',
                "                   ; media-200          ; $CATALOG_ID:$CATALOG_VERSION")
        and: 'request payload contains references to the existing and not existing media'
        def request = post('Companies').withBody(company().withFieldValues('medias', media('media-100'), media('media-200')))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the non existent media is persisted'
        IntegrationTestUtil.findAny(MediaModel, { it.code == 'media-100' }).present
        and: 'the persisted company is associated with the pre-existing and new media'
        persistedCompany()
                .map({ it.medias.collect({ m -> m.code }) })
                .orElse([])
                .containsAll(['media-100', 'media-200'])
    }

    @Test
    def 'POST creates not existing items deeply nested in a collection attribute payload when the attribute is autoCreate'() {
        given: 'an IO with an autoCreate collection attribute'
        importImpEx(
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $IO                                   ; MediaFolder        ; MediaFolder",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor            ; $attributeType    ; autoCreate[default = false]',
                "                                            ; $IO:MediaFolder     ; qualifier                   ; MediaFolder:qualifier",
                "                                            ; $IO:Company         ; medias                      ; Company:medias         ; $IO:Media         ; true",
                "                                            ; $IO:Media           ; folder                      ; Media:folder           ; $IO:MediaFolder   ; true")
        and: 'request payload contains references to the not existing media and folder'
        def request = post('Companies').withBody company()
                .withFieldValues('medias', media('m1').withField('folder', folder('test-folder')))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the non existent folder is persisted'
        def folder = IntegrationTestUtil.findAny(MediaFolderModel, { it.qualifier == 'test-folder' }).orElse(null)
        and: 'the non existent media is persisted'
        def media = IntegrationTestUtil.findAny(MediaModel, { it.code == 'm1' }).orElse(null)
        media?.folder?.is folder
        and: 'the persisted company is associated with the new media'
        persistedCompany()
                .map({ it.medias })
                .orElse([]) == [media]
    }

    @Test
    def 'POST fails to create item when its required collection attribute references a non existing item in the payload'() {
        given: 'an IO with required collection attribute'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor                               ; $refType  ; autoCreate',
                "                                            ; $IO:Master          ; requiredDetails             ; ComplexTestIntegrationItem:requiredDetails; $IO:Detail; false")
        and: 'request with payload referencing a non-existent detail'
        def request = post('Masters').withBody item().withFieldValues('requiredDetails', detail('non-existent-detail'))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        and: 'error is reported'
        def json = JsonObject.createFrom response.entityAsStream
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains 'requiredDetails'
        json.getString('error.innererror') == ITEM_ID
        and: 'the item is not created'
        persistedItem().empty
    }

    @Test
    def 'POST creates item when its required collection attribute references an existing item in the payload'() {
        given: 'an IO with required collection attribute'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor                               ; $refType  ; autoCreate',
                "                                            ; $IO:Master          ; requiredDetails             ; ComplexTestIntegrationItem:requiredDetails; $IO:Detail; false")
        and: 'a detail already exists'
        importImpEx(
                'INSERT TestIntegrationItemDetail; code[unique = true]',
                "                                ; existing-detail")
        and: 'request with payload'
        def request = post('Masters').withBody item().withFieldValues('requiredDetails', detail('existing-detail'))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the item is associated with the pre-existing detail'
        persistedItem().map({ it.requiredDetails.collect({ it.code }) }).orElse([]) == ['existing-detail']
    }

    @Test
    def 'POST creates item when its required autoCreate collection attribute references a non-existent item in the payload'() {
        given: 'an IO with required collection attribute'
        importImpEx(
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$descriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor                               ; $refType   ; autoCreate',
                "                                            ; $IO:Master          ; requiredDetails             ; ComplexTestIntegrationItem:requiredDetails; $IO:Detail ; true")
        and: 'request with payload'
        def request = post('Masters').withBody item().withFieldValues('requiredDetails', detail('item-detail'))

        when:
        def response = facade.handleRequest createContext(request)

        then:
        response.status == HttpStatusCodes.CREATED
        and: 'the detail is created'
        def detail = persistedDetail('item-detail').orElse(null)
        and: 'the item is associated with the created detail'
        persistedItem().map({ it.requiredDetails }).orElse(null) == [detail] as Set
    }

    private static ODataRequestBuilder post(String entityType) {
        ODataFacadeTestUtils.postRequestBuilder(IO, entityType, APPLICATION_JSON_VALUE)
    }

    private static JsonBuilder company() {
        JsonBuilder.json().withId(COMPANY_ID)
    }

    private static JsonBuilder address(String email) {
        JsonBuilder.json().withField('email', email)
    }

    private static JsonBuilder media(String code) {
        JsonBuilder.json()
                .withCode(code)
                .withField('catalogVersion', catalogVersion())
    }

    private static JsonBuilder catalogVersion() {
        JsonBuilder.json()
                .withField('version', CATALOG_VERSION)
                .withField('catalog', JsonBuilder.json().withId(CATALOG_ID))
    }

    private static JsonBuilder folder(String name) {
        JsonBuilder.json().withField('qualifier', name)
    }

    private static JsonBuilder item() {
        JsonBuilder.json().withCode(ITEM_ID)
    }

    private static JsonBuilder detail(String code) {
        JsonBuilder.json().withCode(code)
    }

    private static Optional<CompanyModel> persistedCompany() {
        IntegrationTestUtil.findAny CompanyModel, { it.uid == COMPANY_ID }
    }

    private static Optional<MediaModel> persistedMedia(String id) {
        IntegrationTestUtil.findAny MediaModel, { it.code == id }
    }

    private static Optional<ComplexTestIntegrationItemModel> persistedItem() {
        IntegrationTestUtil.findAny ComplexTestIntegrationItemModel, { it.code == ITEM_ID }
    }

    private static Optional<TestIntegrationItemDetailModel> persistedDetail(String code) {
        IntegrationTestUtil.findAny TestIntegrationItemDetailModel, { it.code == code }
    }
}
