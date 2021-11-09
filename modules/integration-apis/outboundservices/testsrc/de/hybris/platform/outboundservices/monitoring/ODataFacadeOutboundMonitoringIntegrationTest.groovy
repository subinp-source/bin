/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.monitoring

import com.github.tomakehurst.wiremock.junit.WireMockRule
import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.integrationservices.enums.IntegrationRequestStatus
import de.hybris.platform.integrationservices.model.IntegrationApiMediaModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder
import de.hybris.platform.outboundservices.config.DefaultOutboundServicesConfiguration
import de.hybris.platform.outboundservices.enums.OutboundSource
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade
import de.hybris.platform.outboundservices.facade.SyncParameters
import de.hybris.platform.outboundservices.model.OutboundRequestModel
import de.hybris.platform.outboundservices.util.OutboundMonitoringRule
import de.hybris.platform.outboundservices.util.OutboundRequestPersistenceContext
import de.hybris.platform.servicelayer.ServicelayerBaseSpecification
import de.hybris.platform.servicelayer.config.ConfigurationService
import org.junit.Rule
import org.junit.Test
import org.springframework.http.ResponseEntity
import rx.observers.TestSubscriber
import spock.lang.Issue
import spock.lang.Unroll

import javax.annotation.Resource

import static com.github.tomakehurst.wiremock.client.WireMock.badRequest
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.github.tomakehurst.wiremock.client.WireMock.verify
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.findAny
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.remove
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder

@IntegrationTest
class ODataFacadeOutboundMonitoringIntegrationTest extends ServicelayerBaseSpecification {
    private static final String IO = 'ODataFacadeOutboundMonitoringIntegrationTestIO'
    private static final String DESTINATION_ENDPOINT = "/odata2webservices/$IO/Catalogs"
    private static final String DESTINATION_ID = 'facadetestdest'
    private static final String CATALOG_ID = 'facadeTestCatalog'
    private static final CatalogModel catalog = new CatalogModel(id: CATALOG_ID)
    private static final def THREE_INDIVIDUAL_PARAMETERS = [catalog, IO, DESTINATION_ID]
    private static final String ERROR_PAYLOAD_RETENTION_PROPERTY = "outboundservices.monitoring.error.payload.retention"
    private static final String MAX_RESPONSE_BODY_SIZE_PROPERTY = "outboundservices.response.payload.max.size.bytes"

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .dynamicHttpsPort()
            .keystorePath("resources/devcerts/platform.jks")
            .keystorePassword('123456'))
    @Rule
    public OutboundRequestPersistenceContext requestPersistenceContext = OutboundRequestPersistenceContext.create()
    @Rule
    public OutboundMonitoringRule outboundMonitoringRule = OutboundMonitoringRule.enabled()

    @Resource
    private OutboundServiceFacade outboundServiceFacade
    @Resource
    private ConfigurationService configurationService
    @Resource
    private DefaultOutboundServicesConfiguration outboundServicesConfiguration

    private TestSubscriber<ResponseEntity<Map>> subscriber = new TestSubscriber<>()
    private boolean originalPayloadRetention;
    private int originalMaxResponsePayloadSize;

    def setupSpec() {
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)     ; root',
                "                                   ; $IO                                   ; Catalog            ; Catalog        ; true",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $IO:Catalog                                                        ; id                          ; Catalog:id")
    }

    def setup() {
        consumedDestinationBuilder()
                .withId(DESTINATION_ID)
                .withUrl("https://localhost:${wireMockRule.httpsPort()}/$DESTINATION_ENDPOINT")
                .build()
        originalPayloadRetention = configurationService.getConfiguration().getProperty(ERROR_PAYLOAD_RETENTION_PROPERTY)
        originalMaxResponsePayloadSize = outboundServicesConfiguration.getMaximumResponsePayloadSize()
    }

    def cleanup() {
        ConsumedDestinationBuilder.cleanup()
        remove OutboundRequestModel, { it.integrationKey == CATALOG_ID }
        configurationService.getConfiguration().setProperty(ERROR_PAYLOAD_RETENTION_PROPERTY, String.valueOf(originalPayloadRetention))
        configurationService.getConfiguration().setProperty(MAX_RESPONSE_BODY_SIZE_PROPERTY, String.valueOf(originalMaxResponsePayloadSize))
        outboundServicesConfiguration.setMaximumResponsePayloadSize(originalMaxResponsePayloadSize)
    }

    def cleanupSpec() {
        remove IntegrationObjectModel, { it.code == IO }
    }

    @Test
    @Unroll
    def "outbound request is logged with payload when monitoring and outbound payload retention is enabled with parameters #params"() {
        given: 'stub destination server to return BAD REQUEST'
        logErrorPayload(true)
        stubFor(post(urlEqualTo(DESTINATION_ENDPOINT)).willReturn(badRequest()))

        when:
        outboundServiceFacade.send(*params).subscribe(subscriber)

        then: "destination server stub is called"
        verify(postRequestedFor(urlEqualTo(DESTINATION_ENDPOINT)))

        and:
        def persistedModel = findOutboundRequestWithIntegrationKey(CATALOG_ID)
        with(persistedModel) {
            getStatus() == IntegrationRequestStatus.ERROR
            getMediaContent(getPayload()).contains("$CATALOG_ID")
            getUser() == null
            getSapPassport() != null
            getDestination().contains(DESTINATION_ENDPOINT)
            getSource() == source
            getError() == "HttpStatus: 400: "
        }

        where:
        params                                           | source
        THREE_INDIVIDUAL_PARAMETERS                      | OutboundSource.UNKNOWN
        [syncParameters(OutboundSource.OUTBOUNDSYNC)]    | OutboundSource.OUTBOUNDSYNC
        [syncParameters(OutboundSource.WEBHOOKSERVICES)] | OutboundSource.WEBHOOKSERVICES
    }

    @Issue('https://cxjira.sap.com/browse/IAPI-4649')
    @Test
    def "Internal error message is persisted in the OutboundRequest when the error response body exceeds maximum size that can be read in memory"() {
        given: 'stub destination server to return BAD REQUEST'
        def responseBody = "someBodyThatIsLarger than $MAX_RESPONSE_BODY_SIZE_PROPERTY int value set"
        stubFor(post(urlEqualTo(DESTINATION_ENDPOINT)).willReturn(badRequest().withBody(responseBody)))

        and: 'max permitted response body size is smaller than the error response body'
        configurationService.getConfiguration().setProperty(MAX_RESPONSE_BODY_SIZE_PROPERTY, "1")

        when:
        outboundServiceFacade.send(syncParameters()).subscribe(subscriber)

        then: "destination server stub is called"
        verify(postRequestedFor(urlEqualTo(DESTINATION_ENDPOINT)))

        and:
        def persistedOutboundRequest = findOutboundRequestWithIntegrationKey(CATALOG_ID)
        with(persistedOutboundRequest) {
            getStatus() == IntegrationRequestStatus.ERROR
            getError().contains("The error response message exceeded")
            !getError().contains(responseBody)
        }
    }

    @Issue('https://cxjira.sap.com/browse/IAPI-4649')
    @Test
    @Unroll
    def "OutboundRequest error is extracted from the response body when response body is #msg the size that can be read in memory"() {
        given: 'stub destination server to return BAD REQUEST'
        stubFor(post(urlEqualTo(DESTINATION_ENDPOINT)).willReturn(badRequest().withBody(responseBody)))

        and: 'max permitted response body size is smaller than the error response body'
        configurationService.getConfiguration().setProperty(MAX_RESPONSE_BODY_SIZE_PROPERTY, maxResponsePayloadSize)

        when:
        outboundServiceFacade.send(syncParameters(OutboundSource.OUTBOUNDSYNC)).subscribe(subscriber)

        then: "destination server stub is called"
        verify(postRequestedFor(urlEqualTo(DESTINATION_ENDPOINT)))

        and:
        def persistedOutboundRequest = findOutboundRequestWithIntegrationKey(CATALOG_ID)
        with(persistedOutboundRequest) {
            getStatus() == IntegrationRequestStatus.ERROR
            getError().contains(responseBody)
        }

        where:
        msg         | responseBody                                                       | maxResponsePayloadSize
        "less than" | "content less than $MAX_RESPONSE_BODY_SIZE_PROPERTY int value set" | (responseBody.getBytes().size() + 1).toString()
        "equal to"  | "content equal to $MAX_RESPONSE_BODY_SIZE_PROPERTY int value set"  | responseBody.getBytes().size().toString()
    }

    private static OutboundRequestModel findOutboundRequestWithIntegrationKey(final String key) {
        findAny(OutboundRequestModel, { it -> it.integrationKey == key }).orElse(null)
    }

    private static SyncParameters syncParameters(final OutboundSource source = OutboundSource.OUTBOUNDSYNC) {
        SyncParameters.syncParametersBuilder()
                .withItem(catalog)
                .withSource(source)
                .withDestinationId(DESTINATION_ID)
                .withIntegrationObjectCode(IO)
                .build()
    }

    void logErrorPayload(boolean logPayload) {
        configurationService.getConfiguration().setProperty(ERROR_PAYLOAD_RETENTION_PROPERTY, String.valueOf(logPayload))
    }

    def getMediaContent(final IntegrationApiMediaModel payload) {
        requestPersistenceContext.getMediaContentAsString(payload)
    }
}
