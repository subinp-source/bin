/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.user.UserModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.inboundservices.util.InboundMonitoringRule
import de.hybris.platform.inboundservices.util.InboundRequestPersistenceContext
import de.hybris.platform.integrationservices.enums.HttpMethod
import de.hybris.platform.integrationservices.enums.IntegrationRequestStatus
import de.hybris.platform.integrationservices.model.IntegrationApiMediaModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2services.config.ODataServicesConfiguration
import de.hybris.platform.odata2services.odata.content.ODataAtomProductBuilder
import de.hybris.platform.odata2services.odata.content.ODataJsonProductBuilder
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import de.hybris.platform.servicelayer.config.ConfigurationService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.user.UserService
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Rule
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.SAP_PASSPORT_HEADER_NAME
import static de.hybris.platform.odata2services.odata.content.ODataBatchBuilder.batchBuilder
import static de.hybris.platform.odata2services.odata.content.ODataChangeSetBuilder.changeSetBuilder
import static de.hybris.platform.odata2services.odata.content.ODataJsonProductBuilder.product
import static org.springframework.http.MediaType.APPLICATION_ATOM_XML_VALUE
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataFacadeInboundMonitoringIntegrationTest extends ServicelayerTransactionalSpockSpecification {
    private static final def SERVICE_NAME = 'ODataFacadeInboundMonitoringIntegrationTestIO'
    private static final def ENTITY_SET = 'Products'
    private static final String USER_ID = 'testadmin'
    private static final String SAP_PASSPORT = 'my-sap-passport'

    @Rule
    public InboundRequestPersistenceContext requestPersistenceContext = InboundRequestPersistenceContext.create()
    @Rule
    public InboundMonitoringRule monitoring = InboundMonitoringRule.enabled()

    @Resource(name = 'oDataServicesConfiguration')
    private ODataServicesConfiguration configuration
    @Resource(name = 'oDataWebMonitoringFacade')
    private ODataFacade facade
    @Resource
    private ConfigurationService configurationService
    @Resource
    private UserService userService
    @Resource
    private FlexibleSearchService flexibleSearchService


    def setupSpec() {
        importCsv("/impex/essentialdata-odata2services.impex", "UTF-8") // for the integrationadmingroup
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $SERVICE_NAME",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique=true]; code[unique = true]; type(code)',
                "                                   ; $SERVICE_NAME                           ; Product            ; Product",
                "                                   ; $SERVICE_NAME                           ; Catalog            ; Catalog",
                "                                   ; $SERVICE_NAME                           ; CatalogVersion     ; CatalogVersion",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$name=attributeName',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]        ; $name[unique = true]; $attributeDescriptor  ; $attributeType',
                "                                            ; $SERVICE_NAME:Product       ; code                ; Product:code",
                "                                            ; $SERVICE_NAME:Product       ; name                ; Product:name",
                "                                            ; $SERVICE_NAME:Product       ; catalogVersion      ; Product:catalogVersion; $SERVICE_NAME:CatalogVersion",
                "                                            ; $SERVICE_NAME:Catalog       ; id                  ; Catalog:id",
                "                                            ; $SERVICE_NAME:CatalogVersion; catalog             ; CatalogVersion:catalog; $SERVICE_NAME:Catalog",
                "                                            ; $SERVICE_NAME:CatalogVersion; version             ; CatalogVersion:version",

                'INSERT_UPDATE Catalog; id[unique=true]; name[lang=en]; defaultCatalog',
                '                     ;Default         ;Default       ;true',

                'INSERT_UPDATE CatalogVersion; catalog(id)[unique=true]; version[unique=true]; active',
                '                            ; Default                 ; Staged              ;true',

                'INSERT_UPDATE Language; isocode[unique=true]; name[lang=de]; name[lang=en]',
                '                      ; de                  ; Deutsch      ; German',

                'INSERT_UPDATE Employee; UID[unique = true]; groups(uid)',
                "                      ; $USER_ID          ; integrationadmingroup"
        )
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove UserModel, { it.uid == USER_ID }
        IntegrationTestUtil.remove LanguageModel, { it.isocode == 'de' }
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == "IntegrationService" }
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == SERVICE_NAME || it.code == "IntegrationService" }
    }

    @Test
    def "inbound request contains response error when request content is invalid"() {
        when:
        def response = facade.handleRequest post('<invalid_content />')

        then:
        response.status == HttpStatusCodes.BAD_REQUEST

        and:
        def inboundRequest = getInboundRequest()
        with(inboundRequest) {
            getStatus() == IntegrationRequestStatus.ERROR
            getPayload() == null
            getErrors().size() == 1
            errors[0].code == 'odata_error'
            getUser().uid == USER_ID
            getSapPassport() == null
        }
    }

    @Test
    @Unroll
    def "inbound request contains #contentType request payload when success retention is enabled"() {
        given:
        logSuccessRequestPayload(true)

        and:
        def request = post content, contentType
        request.requestHeaders[SAP_PASSPORT_HEADER_NAME] = [SAP_PASSPORT]

        when:
        def response = facade.handleRequest request

        then:
        response.status == HttpStatusCodes.CREATED

        and:
        def inboundRequest = getInboundRequest()
        with(inboundRequest) {
            getStatus() == IntegrationRequestStatus.SUCCESS
            getMediaContent(getPayload()) == content
            getPayload().getMime().equalsIgnoreCase mimeType
            getErrors().isEmpty()
            getUser().uid == USER_ID
            getSapPassport() == SAP_PASSPORT
        }

        where:
        contentType                | content                                   | mimeType
        APPLICATION_JSON_VALUE     | product().build()                         | 'application/octet-stream'
        APPLICATION_ATOM_XML_VALUE | ODataAtomProductBuilder.product().build() | 'text/xml'
    }

    @Test
    def "inbound request does not contain request payload when success retention is disabled"() {
        given:
        logSuccessRequestPayload(false)

        when:
        def response = facade.handleRequest post(product())

        then:
        response.status == HttpStatusCodes.CREATED

        and:
        def inboundRequest = getInboundRequest()
        with(inboundRequest) {
            getStatus() == IntegrationRequestStatus.SUCCESS
            getPayload() == null
            getErrors().isEmpty()
            getUser().uid == USER_ID
            getSapPassport() == null
        }
    }

    @Test
    def "inbound request is not logged when monitoring is off"() {
        given:
        requestPersistenceContext.turnMonitoringOff()

        when:
        facade.handleRequest post('{ "code": "InvalidProduct" }')

        then:
        requestPersistenceContext.getAllMedia().isEmpty()
        requestPersistenceContext.searchAllInboundRequest().isEmpty()
        requestPersistenceContext.searchAllInboundRequestErrors().isEmpty()
    }

    @Test
    def "inbound request contains multiple batch changesets when success retention is enabled"() {
        given:
        logSuccessRequestPayload(true)

        and:
        def englishProduct = product().withName('a product')
        def germanProduct = product().withName('ein Produkt')
        def content = batchBuilder()
                .withChangeSet(changeSetBuilder()
                        .withUri('Products')
                        .withPart(Locale.ENGLISH, englishProduct)
                        .withPart(Locale.GERMAN, germanProduct))
                .build()

        when:
        facade.handleRequest batchODataPostRequest(content)

        then:
        def request = getInboundRequest()
        with(request) {
            getStatus() == IntegrationRequestStatus.SUCCESS
            def payload = getMediaContent getPayload()
            payload.contains 'a product'
            payload.contains 'ein Produkt'
            getErrors().isEmpty()
        }
    }

    @Test
    def "inbound request does not contain multiple batch changesets when success retention is disabled"() {
        given:
        logSuccessRequestPayload(false)

        and:
        def englishProduct = product().withName('a product')
        def germanProduct = product().withName('ein Produkt')
        def content = batchBuilder()
                .withChangeSet(changeSetBuilder()
                        .withUri('Products')
                        .withPart(Locale.ENGLISH, englishProduct)
                        .withPart(Locale.GERMAN, germanProduct))
                .build()

        when:
        facade.handleRequest batchODataPostRequest(content)

        then:
        def request = getInboundRequest()
        with(request) {
            getStatus() == IntegrationRequestStatus.SUCCESS
            getPayload() == null
            getErrors().isEmpty()
        }
    }

    @Test
    def "inbound request contains multiple batch changesets and errors when success and error retentions are enabled"() {
        given:
        logSuccessRequestPayload(true)
        logErrorPayload(true)

        and:
        def content = batchBuilder()
                .withChangeSet(changeSetBuilder()
                        .withUri("Products")
                        .withPart(Locale.GERMAN, product().withCode("Prod-1").withName("ein gutes Produkt"))
                        .withPart(Locale.ENGLISH, product().withCode("Prod-1").withName("invalid product").withCatalog(null)))
                .withChangeSet(changeSetBuilder()
                        .withUri("Products")
                        .withPart(Locale.ENGLISH, product().withCode("Prod@2").withName("a product")))
                .build()

        and:
        def request = batchODataPostRequest(content)
        request.getRequestHeaders().put SAP_PASSPORT_HEADER_NAME, [SAP_PASSPORT]

        when:
        facade.handleRequest request

        then:
        def requests = requestPersistenceContext.searchAllInboundRequest()
        requests.size() == 2

        and: 'error inbound request'
        def errorRequest = requests.find { it.status == IntegrationRequestStatus.ERROR }
        with(errorRequest) {
            errors
            def payload = getMediaContent getPayload()
            with(payload) {
                contains 'Prod-1'
                contains 'invalid product'
                contains 'ein gutes Produkt'
                !contains('Prod@2')
                !contains('a product')
            }
            user.uid == USER_ID
            sapPassport == SAP_PASSPORT
        }

        and: 'success inbound request'
        def successRequest = requests.find { it.status == IntegrationRequestStatus.SUCCESS }
        with(successRequest) {
            integrationKey == 'Staged|Default|Prod@2'
            !errors
            def payload = getMediaContent getPayload()
            with(payload) {
                contains 'Prod@2'
                contains 'a product'
                !contains('Prod-1')
                !contains('ein gutes Produkt')
                !contains('invalid product')
            }
            user.uid == USER_ID
            sapPassport == SAP_PASSPORT
        }
    }

    @Test
    def "inbound request does not contain multiple batch changesets and errors when success and error retentions are disabled"() {
        given:
        logSuccessRequestPayload(false)
        logErrorPayload(false)

        and:
        def content = batchBuilder()
                .withChangeSet(changeSetBuilder()
                        .withUri("Products")
                        .withPart(Locale.GERMAN, product().withCode("Prod-1").withName("ein gutes Produkt"))
                        .withPart(Locale.ENGLISH, product().withCode("Prod-1").withName("invalid product").withCatalog(null)))
                .withChangeSet(changeSetBuilder()
                        .withUri("Products")
                        .withPart(Locale.ENGLISH, product().withCode("Prod-2").withName("a product")))
                .build()

        when:
        facade.handleRequest batchODataPostRequest(content)

        then:
        def requests = requestPersistenceContext.searchAllInboundRequest()
        requests.size() == 2

        and: 'error inbound request'
        def errorRequest = requests.find { it.status == IntegrationRequestStatus.ERROR }
        with(errorRequest) {
            !getErrors().isEmpty()
            getPayload() == null
            getUser().uid == USER_ID
            getSapPassport() == null
        }

        and: 'success inbound request'
        def successRequest = requests.find { it.status == IntegrationRequestStatus.SUCCESS }
        with(successRequest) {
            getIntegrationKey() == 'Staged|Default|Prod-2'
            getErrors().isEmpty()
            getPayload() == null
            getUser().uid == USER_ID
            getSapPassport() == null
        }
    }

    @Test
    def "inbound request logs errors when number of batches exceeds the changeset limit"() {
        given:
        configuration.setBatchLimit(4)

        and:
        def content = batchBuilder()
                .withChangeSet(changeSetBuilder().withUri("Products").withPart(Locale.ENGLISH, product()))
                .withChangeSet(changeSetBuilder().withUri("Products").withPart(Locale.ENGLISH, product()))
                .withChangeSet(changeSetBuilder().withUri("Products").withPart(Locale.ENGLISH, product()))
                .withChangeSet(changeSetBuilder().withUri("Products").withPart(Locale.ENGLISH, product()))
                .withChangeSet(changeSetBuilder().withUri("Products").withPart(Locale.ENGLISH, product()))
                .build()

        when:
        facade.handleRequest batchODataPostRequest(content)

        then:
        def requests = requestPersistenceContext.searchAllInboundRequest()
        requests.size() == 1

        and:
        def request = requests.find { it.status == IntegrationRequestStatus.ERROR }
        with(request) {
            !getErrors().isEmpty()
            getPayload() == null
            getUser().uid == USER_ID
            getSapPassport() == null
        }
    }

    @Test
    def "inbound request has the correct http method and response status when PATCH request is made"() {
        given: 'a product exists'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; name    ; catalogVersion(version, catalog(id))',
                '                     ; abc                ; original; Staged:Default'
        )

        when:
        facade.handleRequest patch('Staged|Default|abc', product().withCode('abc').withName('new'))

        then:
        def inboundRequest = getInboundRequest()
        with(inboundRequest) {
            status == IntegrationRequestStatus.SUCCESS
            httpMethod == HttpMethod.PATCH
        }
    }

    @Test
    def "inbound request has the correct http method and response status when DELETE request is made"() {
        given: 'a product exists'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; name    ; catalogVersion(version, catalog(id))',
                '                     ; abc                ; original; Staged:Default'
        )

        when:
        facade.handleRequest delete('Staged|Default|abc')

        then:
        def inboundRequest = getInboundRequest()
        with(inboundRequest) {
            status == IntegrationRequestStatus.SUCCESS
            httpMethod == HttpMethod.DELETE
        }
    }

    @Test
    def "inbound request has the correct http method and response status when POST request is made"() {
        given: 'a product exists'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; name    ; catalogVersion(version, catalog(id))',
                '                     ; abc                ; original; Staged:Default'
        )

        when:
        facade.handleRequest post(product())

        then:
        def inboundRequest = getInboundRequest()
        with(inboundRequest) {
            status == IntegrationRequestStatus.SUCCESS
            httpMethod == HttpMethod.POST
        }
    }

    def getInboundRequest() {
        def inboundRequests = requestPersistenceContext.searchAllInboundRequest()
        assert inboundRequests.size() == 1
        inboundRequests.first()
    }

    def batchODataPostRequest(String content) {
        setUserInSession()
        def request = ODataFacadeTestUtils.batchODataPostRequest(SERVICE_NAME, content)
        ODataFacadeTestUtils.createContext request
    }

    def getMediaContent(final IntegrationApiMediaModel payload) {
        requestPersistenceContext.getMediaContentAsString(payload)
    }

    void logSuccessRequestPayload(boolean logPayload) {
        configurationService.getConfiguration().setProperty("inboundservices.monitoring.success.payload.retention", String.valueOf(logPayload))
    }

    void logErrorPayload(boolean logPayload) {
        configurationService.getConfiguration().setProperty("inboundservices.monitoring.error.payload.retention", String.valueOf(logPayload))
    }

    ODataContext patch(String key, ODataJsonProductBuilder body) {
        setUserInSession()
        ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataPatchRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(ENTITY_SET)
                        .withEntityKeys(key))
                .withContentType(APPLICATION_JSON_VALUE)
                .withBody(body.build())
    }

    ODataContext delete(String key) {
        setUserInSession()
        ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataDeleteRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(ENTITY_SET)
                        .withEntityKeys(key))
                .withAccepts(APPLICATION_JSON_VALUE)
    }

    ODataContext post(ODataJsonProductBuilder body, String contentType = APPLICATION_JSON_VALUE) {
        post body.build(), contentType
    }

    ODataContext post(String body, String contentType = APPLICATION_JSON_VALUE) {
        setUserInSession()
        ODataFacadeTestUtils.createContext ODataRequestBuilder.oDataPostRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(ENTITY_SET))
                .withContentType(contentType)
                .withBody(body)
    }

    UserModel testUser() {
        UserModel userModel = new UserModel()
        userModel.setUid(USER_ID)
        userModel
    }

    def setUserInSession() {
        UserModel testUser = flexibleSearchService.getModelByExample(testUser())
        userService.setCurrentUser(testUser)
    }
}