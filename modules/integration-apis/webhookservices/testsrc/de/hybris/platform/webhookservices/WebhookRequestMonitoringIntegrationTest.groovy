package de.hybris.platform.webhookservices

import com.github.tomakehurst.wiremock.junit.WireMockRule
import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.core.model.security.UserRightModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundservices.enums.OutboundSource
import de.hybris.platform.outboundservices.model.OutboundRequestModel
import de.hybris.platform.outboundservices.util.OutboundMonitoringRule
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants
import de.hybris.platform.webhookservices.event.ItemSavedEvent
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import org.junit.Rule
import org.junit.Test
import spock.lang.Shared

import java.time.Duration

import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl
import static com.github.tomakehurst.wiremock.client.WireMock.ok
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import static com.github.tomakehurst.wiremock.client.WireMock.verify
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static de.hybris.platform.integrationservices.util.EventualCondition.eventualCondition
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder
import static de.hybris.platform.webhookservices.WebhookConfigurationBuilder.webhookConfiguration

@IntegrationTest
class WebhookRequestMonitoringIntegrationTest extends ServicelayerSpockSpecification {
    private static final def IO = 'CategoryIO'
    private static final def WEBHOOK_ENDPOINT = 'categoryWebhookEndpoint'
    private static final def CATEGORY = 'Category'
    private static final def TEST_CATALOG = 'TestCatalog:Staged'
    private static final def DESTINATION_ID = 'category_webhook_destination_id'
    private static final def REASONABLE_TIME = Duration.ofSeconds(7)

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .dynamicHttpsPort()
            .keystorePath("resources/devcerts/platform.jks")
            .keystorePassword('123456'))

    @Rule
    public OutboundMonitoringRule outboundMonitoring = OutboundMonitoringRule.enabled()

    @Shared
    private EventConfigurationModel eventConfig

    def setup() {
        stubFor(post(anyUrl()).willReturn(ok()))
    }

    def cleanup() {
        IntegrationTestUtil.remove(WebhookConfigurationModel) { it.integrationObject.code == IO }
        IntegrationTestUtil.removeAll(OutboundRequestModel)
    }

    def setupSpec() {
        importCsv '/impex/essentialdata-webhookservices.impex', 'UTF-8'
        IntegrationTestUtil.importCatalogVersion('Staged', 'TestCatalog', true)
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; $CATEGORY          ; $TEST_CATALOG",

                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root',
                "                                   ; $IO                                   ; Category           ; Category  ; true",

                '$ioItem = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $ioItem[unique = true]; attributeName[unique = true]; $descriptor',
                "                                            ; $IO:Category          ; code                        ; Category:code"
        )

        eventConfig = IntegrationTestUtil.findAny(EventConfigurationModel,
                { it.eventClass == ItemSavedEvent.canonicalName }).get()
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove(IntegrationObjectModel) { it.code == IO }

        IntegrationTestUtil.remove(CategoryModel) { it.code == CATEGORY }
        IntegrationTestUtil.removeSafely(CatalogVersionModel) { it.catalog.name == 'TestCatalog' }
        IntegrationTestUtil.remove(DestinationTargetModel) { it.id == 'webhookServices' }

        IntegrationTestUtil.remove eventConfig

        IntegrationTestUtil.removeSafely(UserRightModel, {
            it.code == PermissionsConstants.READ ||
                    PermissionsConstants.CREATE ||
                    PermissionsConstants.CHANGE ||
                    PermissionsConstants.REMOVE
        })
    }


    @Test
    def 'outbound request monitoring entry is created when webhook is sent'() {
        given: 'a webhook is configured'
        webhookConfiguration()
                .withEvent(ItemSavedEvent)
                .withDestination(webhookDestination(WEBHOOK_ENDPOINT))
                .withIntegrationObject(IO)
                .build()

        when: 'an item of Category type is updated'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; name[lang=en]; catalogVersion(catalog(id), version)',
                "                      ;$CATEGORY           ; CategoryName ; $TEST_CATALOG"
        )

        then: 'the outbound request is created'
        eventualCondition().within(REASONABLE_TIME).expect {
            verify postRequestedFor(urlPathEqualTo("/$WEBHOOK_ENDPOINT"))
            def outboundRequest = IntegrationTestUtil.findAny OutboundRequestModel,
                    { it.type == IO }
            !outboundRequest.empty
            with(outboundRequest.get()) {
                source == OutboundSource.WEBHOOKSERVICES
                destination == "https://localhost:${wireMockRule.httpsPort()}/$WEBHOOK_ENDPOINT"
                !integrationKey.isEmpty()
                !sapPassport.isEmpty()
            }
        }
    }

    def webhookDestination(String uri, String id = DESTINATION_ID) {
        def destinationTargetModel = IntegrationTestUtil.findAny(DestinationTargetModel, { it.id == "webhookServices" }).get()
        eventConfig.setDestinationTarget(destinationTargetModel)
        consumedDestinationBuilder()
                .withId(id)
                .withUrl("https://localhost:${wireMockRule.httpsPort()}/$uri")
                .withDestinationTarget(destinationTargetModel)
    }

}