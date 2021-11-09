/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.facade.impl

import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario
import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.timeout.IntegrationTimeoutException
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder
import de.hybris.platform.outboundservices.config.DefaultOutboundServicesConfiguration
import de.hybris.platform.outboundservices.decorator.DecoratorContextErrorException
import de.hybris.platform.outboundservices.enums.OutboundSource
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade
import de.hybris.platform.outboundservices.facade.SyncParameters
import de.hybris.platform.outboundservices.model.OutboundRequestModel
import de.hybris.platform.outboundservices.util.OutboundMonitoringRule
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.junit.Rule
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import rx.observers.TestSubscriber
import spock.lang.Unroll

import javax.annotation.Resource

import static com.github.tomakehurst.wiremock.client.WireMock.badRequest
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.exactly
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.matching
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath
import static com.github.tomakehurst.wiremock.client.WireMock.notFound
import static com.github.tomakehurst.wiremock.client.WireMock.ok
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.github.tomakehurst.wiremock.client.WireMock.verify
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.SAP_PASSPORT_HEADER_NAME
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.assertModelDoesNotExist
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.assertModelExists
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.removeAll
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder

@IntegrationTest
class OutboundServiceFacadeIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    private static final String DESTINATION_ENDPOINT = '/odata2webservices/OutboundCatalogVersion/CatalogVersions'
    private static final String DESTINATION_ID = 'facadetestdest'
    private static final String CATALOG_VERSION_IO = 'OutboundCatalogVersion'
    private static final String CATALOG_VERSION_VERSION = 'facadeTestVersion'
    private static final String CATALOG_ID = 'facadeTestCatalog'
    private static final CatalogVersionModel catalogVersion = new CatalogVersionModel(version: CATALOG_VERSION_VERSION, catalog: new CatalogModel(id: CATALOG_ID))
    private static final def THREE_INDIVIDUAL_PARAMETERS = [catalogVersion, CATALOG_VERSION_IO, DESTINATION_ID]
    private static final def ONE_PARAMETER_OBJECT = [syncParameters()]

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .dynamicHttpsPort()
            .keystorePath("resources/devcerts/platform.jks")
            .keystorePassword('123456'))
    @Rule
    public OutboundMonitoringRule outboundMonitoringRule = OutboundMonitoringRule.enabled()

    @Resource
    private OutboundServiceFacade outboundServiceFacade
    @Resource(name = "defaultOutboundServicesConfiguration")
    private DefaultOutboundServicesConfiguration configurationService
    private long originalRequestExecutionTimeout

    private TestSubscriber<ResponseEntity<Map>> subscriber = new TestSubscriber<>()

    def setupSpec() {
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $CATALOG_VERSION_IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)     ; root[default = false]',
                "                                   ; $CATALOG_VERSION_IO                   ; Catalog            ; Catalog        ;",
                "                                   ; $CATALOG_VERSION_IO                   ; CatalogVersion     ; CatalogVersion ; true",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $CATALOG_VERSION_IO:Catalog                                        ; id                          ; Catalog:id",
                "                                            ; $CATALOG_VERSION_IO:CatalogVersion                                 ; catalog                     ; CatalogVersion:catalog                             ; $CATALOG_VERSION_IO:Catalog",
                "                                            ; $CATALOG_VERSION_IO:CatalogVersion                                 ; version                     ; CatalogVersion:version                             ;")
    }

    def setup() {
        consumedDestinationBuilder()
                .withId(DESTINATION_ID)
                .withUrl("https://localhost:${wireMockRule.httpsPort()}/$DESTINATION_ENDPOINT")
                .build()

        originalRequestExecutionTimeout = configurationService.requestExecutionTimeout
    }

    def cleanup() {
        ConsumedDestinationBuilder.cleanup()
        removeAll OutboundRequestModel
        configurationService.requestExecutionTimeout = originalRequestExecutionTimeout
    }

    def cleanupSpec() {
        IntegrationObjectTestUtil.cleanup()
    }

    @Test
    @Unroll
    def "send a catalog version out with parameters #params succeeds when the destination respond within timeout limit"() {
        given: 'request timeout set to 1000 ms'
        configurationService.requestExecutionTimeout = 1000

        and: 'stub destination server to return OK after 500 ms'
        stubFor(post(urlEqualTo(DESTINATION_ENDPOINT))
                .willReturn(ok().withFixedDelay(500)))

        when:
        outboundServiceFacade.send(*params).subscribe(subscriber)

        then: "destination server stub is called"
        verify(postRequestedFor(urlEqualTo(DESTINATION_ENDPOINT))
                .withRequestBody(matchVersion(CATALOG_VERSION_VERSION))
                .withRequestBody(matchCatalogId(CATALOG_ID))
                .withHeader(SAP_PASSPORT_HEADER_NAME, matchPassport()))

        and: "observable contains response with OK HTTP status"
        subscriber.getOnNextEvents()[0].getStatusCode() == HttpStatus.OK

        where:
        params << [THREE_INDIVIDUAL_PARAMETERS, ONE_PARAMETER_OBJECT]
    }

    @Test
    @Unroll
    def "send a catalog version out with parameters #params fails when the destination exceeds the timeout limit to respond"() {
        given: 'request timeout set to 500 ms'
        configurationService.requestExecutionTimeout = 500

        and: 'stub destination server to return OK after 1000 ms'
        stubFor(post(urlEqualTo(DESTINATION_ENDPOINT))
                .willReturn(ok().withFixedDelay(1000)))

        when:
        outboundServiceFacade.send(*params).subscribe(subscriber)

        then: "destination server stub is called"
        verify(postRequestedFor(urlEqualTo(DESTINATION_ENDPOINT)))

        and: "observable contains an error"
        subscriber.assertError(IntegrationTimeoutException)

        and: 'only one outbound request that contains a timeout error'
        def outboundRequests = IntegrationTestUtil.findAll(OutboundRequestModel)
        outboundRequests.size() == 1
        with(outboundRequests[0] as OutboundRequestModel) {
            error.contains 'Request timed out'
        }

        where:
        params << [THREE_INDIVIDUAL_PARAMETERS, ONE_PARAMETER_OBJECT]
    }

    @Test
    @Unroll
    def "outbound request is logged when monitoring is enabled with parameters #params"() {
        given: 'stub destination server to return BAD REQUEST'
        stubFor(post(urlEqualTo(DESTINATION_ENDPOINT)).willReturn(badRequest()))

        and:
        def outboundRequestSample = new OutboundRequestModel()
        outboundRequestSample.setIntegrationKey("$CATALOG_VERSION_VERSION|$CATALOG_ID")
        assertModelDoesNotExist(outboundRequestSample)

        when:
        outboundServiceFacade.send(*params).subscribe(subscriber)

        then: "destination server stub is called"
        verify(postRequestedFor(urlEqualTo(DESTINATION_ENDPOINT)))

        and:
        assertModelExists(outboundRequestSample)

        where:
        params << [THREE_INDIVIDUAL_PARAMETERS, ONE_PARAMETER_OBJECT]
    }

    @Test
    def "outbound request is logged when monitoring is enabled and destination does not exist with parameters for ItemModel, IO code and Destination Id"() {
        given:
        def nonExistingDestination = 'non-existing-destination'
        noOutboundRequestExists()

        when:
        outboundServiceFacade.send(catalogVersion, CATALOG_VERSION_IO, nonExistingDestination).subscribe(subscriber)

        then:
        thrown DecoratorContextErrorException
        and: 'only one outbound request that contains the destination ID'
        def outboundRequests = IntegrationTestUtil.findAll(OutboundRequestModel)
        outboundRequests.size() == 1
        with(outboundRequests[0] as OutboundRequestModel) {
            destination.contains nonExistingDestination
            error.contains nonExistingDestination
        }
    }

    @Test
    def "outbound request is logged when monitoring is enabled and destination does not exist with parameters as SyncParameters"() {
        given:
        def nonExistingDestination = 'non-existing-destination'
        noOutboundRequestExists()

        when:
        def params = SyncParameters.syncParametersBuilder()
                .withItem(catalogVersion)
                .withIntegrationObjectCode(CATALOG_VERSION_IO)
                .withDestinationId(nonExistingDestination)
                .build();
        outboundServiceFacade.send(params).subscribe(subscriber)

        then: 'only one outbound request that contains the destination ID'
        def outboundRequests = IntegrationTestUtil.findAll(OutboundRequestModel)
        outboundRequests.size() == 1
        with(outboundRequests[0] as OutboundRequestModel) {
            destination.contains nonExistingDestination
            error.contains nonExistingDestination
        }
    }

    @Test
    def "outbound request is logged when monitoring is enabled and integration object does not exist with parameters as SyncParameters"() {
        given:
        noOutboundRequestExists()

        when:
        def params = SyncParameters.syncParametersBuilder()
                .withItem(catalogVersion)
                .withIntegrationObject(null)
                .withDestinationId(DESTINATION_ID)
                .build();
        outboundServiceFacade.send(params).subscribe(subscriber)

        then: 'only one outbound request that contains the decorator context error'
        def outboundRequests = IntegrationTestUtil.findAll(OutboundRequestModel)
        outboundRequests.size() == 1
        with(outboundRequests[0] as OutboundRequestModel) {
            error.contains "[Provided integration object 'null' was not found.]"
        }
    }

    @Test
    @Unroll
    def "CSRF token should be retrieved and injected into the request if the destination is configured so with parameters #params"() {
        given: 'a destination with the CSRF URL specified'
        consumedDestinationBuilder()
                .withId(DESTINATION_ID)
                .withAdditionalParameters([csrfURL: "https://localhost:${wireMockRule.httpsPort()}/csrf"])
                .withUrl("https://localhost:${wireMockRule.httpsPort()}$DESTINATION_ENDPOINT")
                .build()
        and: 'CSRF request returns valid token'
        stubFor get(urlEqualTo('/csrf'))
                .willReturn(ok().withHeader('X-CSRF-Token', 'x-token').withHeader('Set-Cookie', 'trusted', 'alive'))
        and: 'POST to destination is successful'
        stubFor post(urlEqualTo(DESTINATION_ENDPOINT)).willReturn(ok())

        when:
        outboundServiceFacade.send(*params).subscribe(subscriber)

        then: 'retrieved CSRF token is sent to the destination'
        verify postRequestedFor(urlEqualTo(DESTINATION_ENDPOINT))
                .withHeader('X-CSRF-Token', equalTo('x-token'))
                .withCookie('trusted', equalTo(''))
                .withCookie('alive', equalTo(''))

        where:
        params << [THREE_INDIVIDUAL_PARAMETERS, ONE_PARAMETER_OBJECT]
    }

    @Test
    @Unroll
    def "CSRF token should be cached for subsequent calls to the same destination with parameters #params"() {
        def scenario = 'caching test'
        given: 'a destination with the CSRF URL specified'
        consumedDestinationBuilder()
                .withId(DESTINATION_ID)
                .withAdditionalParameters([csrfURL: "https://localhost:${wireMockRule.httpsPort()}/csrf"])
                .withUrl("https://localhost:${wireMockRule.httpsPort()}$DESTINATION_ENDPOINT")
                .build()
        and: 'first CSRF request is successful'
        stubFor get(urlEqualTo('/csrf')).inScenario(scenario).whenScenarioStateIs(Scenario.STARTED)
                .willReturn(ok().withHeader('X-CSRF-Token', 'received').withHeader('Set-Cookie', 'token=cached'))
        and: 'first POST to destination is successful'
        stubFor post(urlEqualTo(DESTINATION_ENDPOINT)).inScenario(scenario).whenScenarioStateIs(Scenario.STARTED)
                .willReturn(ok())
                .willSetStateTo('csrf returned')
        and: 'second CSRF request returns different token'
        stubFor get(urlEqualTo('/csrf')).inScenario(scenario).whenScenarioStateIs('csrf returned')
                .willReturn(ok().withHeader('X-CSRF-Token', 'never called'))
        and: 'second POST to the destination is successful'
        stubFor post(urlEqualTo(DESTINATION_ENDPOINT)).inScenario(scenario).whenScenarioStateIs('csrf returned')
                .willReturn(ok())

        when:
        outboundServiceFacade.send(*params).subscribe TestSubscriber.create()
        and:
        outboundServiceFacade.send(*params).subscribe TestSubscriber.create()

        then: 'first CSRF token is sent with both destination request'
        verify exactly(2), postRequestedFor(urlEqualTo(DESTINATION_ENDPOINT))
                .withHeader('X-CSRF-Token', equalTo('received'))
                .withCookie('token', equalTo('cached'))

        where:
        params << [THREE_INDIVIDUAL_PARAMETERS, ONE_PARAMETER_OBJECT]
    }

    @Test
    @Unroll
    def "cached CSRF token must not be used for calls to a different destination with parameters #params1 and then #params2"() {
        def scenario = 'caching test'
        def nextState = 'destination 2'
        given: 'two different destinations with the same CSRF URL'
        consumedDestinationBuilder()
                .withId('system-one')
                .withAdditionalParameters([csrfURL: "https://localhost:${wireMockRule.httpsPort()}/csrf"])
                .withUrl("https://localhost:${wireMockRule.httpsPort()}/system1")
                .build()
        consumedDestinationBuilder()
                .withId('system-two')
                .withAdditionalParameters([csrfURL: "https://localhost:${wireMockRule.httpsPort()}/csrf"])
                .withUrl("https://localhost:${wireMockRule.httpsPort()}/system2")
                .build()
        and: 'first CSRF request is successful'
        stubFor get(urlEqualTo('/csrf')).inScenario(scenario).whenScenarioStateIs(Scenario.STARTED)
                .willReturn(ok().withHeader('X-CSRF-Token', 'token1').withHeader('Set-Cookie', 'token1=fresh'))
        and: 'first POST to destination is successful'
        stubFor post(urlEqualTo('/system1')).inScenario(scenario).whenScenarioStateIs(Scenario.STARTED)
                .willReturn(ok())
                .willSetStateTo(nextState)
        and: 'second CSRF request returns different token'
        stubFor get(urlEqualTo('/csrf')).inScenario(scenario).whenScenarioStateIs(nextState)
                .willReturn(ok().withHeader('X-CSRF-Token', 'token2').withHeader('Set-Cookie', 'token2=fresh'))
        and: 'second POST to the destination is successful'
        stubFor post(urlEqualTo('/system2')).inScenario(scenario).whenScenarioStateIs(nextState)
                .willReturn(ok())

        when:
        outboundServiceFacade.send(*params1).subscribe TestSubscriber.create()
        and:
        outboundServiceFacade.send(*params2).subscribe TestSubscriber.create()

        then: 'first CSRF token is sent with both destination request'
        verify postRequestedFor(urlEqualTo('/system1'))
                .withHeader('X-CSRF-Token', equalTo('token1'))
                .withCookie('token1', equalTo('fresh'))
        verify postRequestedFor(urlEqualTo('/system2'))
                .withHeader('X-CSRF-Token', equalTo('token2'))
                .withCookie('token2', equalTo('fresh'))

        where:
        params1                                            | params2
        [catalogVersion, CATALOG_VERSION_IO, 'system-one'] | [catalogVersion, CATALOG_VERSION_IO, 'system-two']
        [syncParameters('system-one')]                     | [syncParameters('system-two')]
    }

    @Test
    @Unroll
    def "item is not sent to the destination when CSRF token not retrieved and outbound request is logged with parameters #params"() {
        given:
        noOutboundRequestExists()
        and: 'a destination with the CSRF URL specified'
        consumedDestinationBuilder()
                .withId(DESTINATION_ID)
                .withAdditionalParameters([csrfURL: "https://localhost:${wireMockRule.httpsPort()}/csrf"])
                .withUrl("https://localhost:${wireMockRule.httpsPort()}$DESTINATION_ENDPOINT")
                .build()
        and: 'CSRF request fails'
        stubFor get(urlEqualTo('/csrf')).willReturn(notFound())

        when:
        outboundServiceFacade.send(*params).subscribe(subscriber)

        then: 'CSRF response status code is reported back'
        subscriber.onErrorEvents[0].statusCode == HttpStatus.NOT_FOUND
        and: 'only one outbound request that contains the destination'
        IntegrationTestUtil.findAll(OutboundRequestModel).findAll {
            (it as OutboundRequestModel).destination.contains(DESTINATION_ENDPOINT)
        }.size() == 1

        where:
        params << [THREE_INDIVIDUAL_PARAMETERS, ONE_PARAMETER_OBJECT]
    }

    private static SyncParameters syncParameters(def destination = DESTINATION_ID) {
        SyncParameters.syncParametersBuilder()
                .withItem(catalogVersion)
                .withSource(OutboundSource.OUTBOUNDSYNC)
                .withDestinationId(destination)
                .withIntegrationObjectCode(CATALOG_VERSION_IO)
                .build()
    }

    def matchVersion(String version) {
        matchingJsonPath("\$.[?(@.version == '$version')]")
    }

    def matchCatalogId(String catalogId) {
        matchingJsonPath("\$.catalog[?(@.id == '$catalogId')]")
    }

    def matchPassport() {
        matching('[\\w]+')
    }

    def noOutboundRequestExists() {
        IntegrationTestUtil.findAll(OutboundRequestModel).isEmpty()
    }
}
