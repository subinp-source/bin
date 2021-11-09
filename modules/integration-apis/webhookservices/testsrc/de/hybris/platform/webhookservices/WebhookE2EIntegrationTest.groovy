/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices

import com.github.tomakehurst.wiremock.junit.WireMockRule
import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.enums.DestinationChannel
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.security.UserRightModel
import de.hybris.platform.core.model.user.CustomerModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.scripting.model.ScriptModel
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants
import de.hybris.platform.webhookservices.constants.WebhookservicesConstants
import de.hybris.platform.webhookservices.event.ItemSavedEvent
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import org.junit.Rule
import org.junit.Test
import spock.lang.Issue

import javax.annotation.Resource
import java.time.Duration

import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath
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
@Issue('https://cxjira.sap.com/browse/IAPI-4552')
class WebhookE2EIntegrationTest extends ServicelayerSpockSpecification {
    private static final def IO = 'WebHookIO'
    private static final def UID = 'tester'
    private static final def CURRENCY = 'CAD'
    private static final def REASONABLE_TIME = Duration.ofSeconds(7)
    private static final def TIME_FOR_EVENT_TO_PASS = Duration.ofSeconds(1)
    private static final def WEBHOOK_ENDPOINT = 'webhook'
    private static final def DESTINATION_ID = 'TEST_WEB_HOOK'

    @Resource
    private ModelService modelService

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .dynamicHttpsPort()
            .keystorePath("resources/devcerts/platform.jks")
            .keystorePassword('123456'))

    def setupSpec() {
        importCsv '/impex/essentialdata-webhookservices.impex', 'UTF-8'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $IO                                   ; Customer           ; Customer  ; true",
                "                                   ; $IO                                   ; Currency           ; Currency",
                "                                   ; $IO                                   ; Email              ; Address",
                '$ioItem = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $ioItem[unique = true]; attributeName[unique = true]; $descriptor             ; $attributeType; unique[default = false]',
                "                                            ; $IO:Customer          ; id                          ; Customer:uid",
                "                                            ; $IO:Customer          ; forbidden                   ; Customer:loginDisabled",
                "                                            ; $IO:Customer          ; currency                    ; Customer:sessionCurrency; $IO:Currency",
                "                                            ; $IO:Customer          ; addresses                   ; Customer:addresses      ; $IO:Email",
                "                                            ; $IO:Currency          ; code                        ; Currency:isocode",
                "                                            ; $IO:Currency          ; symbol                      ; Currency:symbol",
                "                                            ; $IO:Email             ; address                     ; Address:email           ;              ; true",
                "                                            ; $IO:Email             ; company                     ; Address:company",
                "                                            ; $IO:Email             ; owner                       ; Address:owner           ; $IO:Customer ; true")
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.remove EventConfigurationModel, {
            it.extensionName == WebhookservicesConstants.EXTENSIONNAME
        }
        IntegrationTestUtil.remove DestinationTargetModel, {
            it.destinationChannel == DestinationChannel.WEBHOOKSERVICES
        }
        IntegrationTestUtil.removeSafely(UserRightModel, {
            it.code == PermissionsConstants.READ ||
                    PermissionsConstants.CREATE ||
                    PermissionsConstants.CHANGE ||
                    PermissionsConstants.REMOVE
        })
    }

    def setup() {
        stubFor(post(anyUrl()).willReturn(ok()))
    }

    def cleanup() {
        IntegrationTestUtil.removeAll WebhookConfigurationModel
        IntegrationTestUtil.remove(ConsumedDestinationModel) { it.id == DESTINATION_ID }
        IntegrationTestUtil.remove(CurrencyModel) { it.isocode == CURRENCY }
        IntegrationTestUtil.remove(CustomerModel) { it.uid == UID }
    }

    @Test
    def 'event is not sent when webhook is not configured'() {
        given: 'there is no a webhook configuration'
        assert IntegrationTestUtil.findAll(WebhookConfigurationModel).empty

        when: 'new integration object root item is saved'
        IntegrationTestUtil.importImpEx(
                'INSERT Customer; uid[unique = true]',
                "               ; $UID")

        then: 'no webhook notification is performed'
        eventualCondition()
                .within(REASONABLE_TIME)
                .retains { verify(0, postRequestedFor(anyUrl())) }
    }

    @Test
    def 'notification is sent to the webhook when root integration object item is created'() {
        given: 'webhook configuration exists'
        webhookConfiguration()
                .withDestination(webhookDestination(WEBHOOK_ENDPOINT))
                .withIntegrationObject(IO)
                .build()

        when: 'an item of the integration object root type is created'
        IntegrationTestUtil.importImpEx(
                'INSERT Currency; isocode[unique = true]; symbol',
                "               ; $CURRENCY             ; CAD",
                'INSERT Customer; uid[unique = true]; sessionCurrency(isocode); addresses(&addrPk)',
                "               ; $UID              ; $CURRENCY               ; email",
                'INSERT Address; &addrPk; owner(Customer.uid)[unique = true]; email[unique = true]',
                "              ; email  ; $UID                              ; $UID@company.net"
        )

        then: 'the web hook is notified'
        eventualCondition().within(REASONABLE_TIME).expect {
            verify postRequestedFor(urlPathEqualTo("/$WEBHOOK_ENDPOINT"))
                    .withRequestBody(matchingJsonPath("\$.[?(@.id == '$UID')]"))
                    .withRequestBody(matchingJsonPath("\$.[?(@.currency.code == '$CURRENCY')]"))
                    .withRequestBody(matchingJsonPath("\$.[?(@.addresses[0].address == '$UID@company.net')]"))
        }
    }

    @Test
    def 'notification is not sent to the webhook when non-root integration object item is created'() {
        given: 'webhook configuration exists'
        webhookConfiguration()
                .withDestination(webhookDestination(WEBHOOK_ENDPOINT))
                .withIntegrationObject(IO)
                .build()

        when: 'an item of the non-root integration object type is created'
        IntegrationTestUtil.importImpEx(
                'INSERT Currency; isocode[unique = true]; symbol',
                "               ; $CURRENCY             ; \$")

        then: 'the web hook is not notified'
        eventualCondition()
                .within(REASONABLE_TIME)
                .retains { verify(0, postRequestedFor(anyUrl())) }
    }

    @Test
    def 'notification is sent to the webhook when root integration object item is updated'() {
        given: 'item of root integration object type already exists'
        IntegrationTestUtil.importImpEx(
                'INSERT Customer; uid[unique = true]',
                "               ; $UID")
        and: 'later webhook configuration created'
        webhookConfiguration()
                .withDestination(webhookDestination(WEBHOOK_ENDPOINT))
                .withIntegrationObject(IO)
                .build()

        when: 'the existing item is updated'
        IntegrationTestUtil.importImpEx(
                'UPDATE Customer; uid[unique = true]; loginDisabled',
                "               ; $UID              ; true")

        then: 'the web hook is notified'
        eventualCondition().within(REASONABLE_TIME).expect {
            verify postRequestedFor(urlPathEqualTo("/$WEBHOOK_ENDPOINT"))
                    .withRequestBody(matchingJsonPath("\$.[?(@.id == '$UID')]"))
                    .withRequestBody(matchingJsonPath("\$.[?(@.forbidden == true)]"))
        }
    }

    @Test
    def 'notification is not sent to the webhook when non-root and non-partOf integration object item is updated'() {
        given: 'ItemSavedEvent is not exported'
        def eventConfig = contextEventConfig ItemSavedEvent
        eventConfig.exportFlag = false
        modelService.save(eventConfig)

        and: 'both root and non-root integration object items already exist'
        IntegrationTestUtil.importImpEx(
                'INSERT Currency; isocode[unique = true]; symbol',
                "               ; $CURRENCY             ; \$",
                'INSERT Customer; uid[unique = true]; sessionCurrency(isocode)',
                "               ; $UID              ; $CURRENCY")

        and: 'ensure ItemSavedEvent passes'
        eventualCondition()
                .within(TIME_FOR_EVENT_TO_PASS)
                .retains { eventConfig.exportFlag }

        and: 'later webhook configuration is created'
        webhookConfiguration()
                .withDestination(webhookDestination(WEBHOOK_ENDPOINT))
                .withIntegrationObject(IO)
                .build()

        and: 'ItemSavedEvent is exported again'
        eventConfig.exportFlag = true
        modelService.save(eventConfig)

        when: 'the non-root item is updated'
        IntegrationTestUtil.importImpEx(
                'UPDATE Currency; isocode[unique = true]; symbol',
                "               ; $CURRENCY             ; CA\$")

        then: 'the web hook is not notified'
        eventualCondition()
                .within(REASONABLE_TIME)
                .retains { verify(0, postRequestedFor(anyUrl())) }
    }

    @Test
    def 'notification is not sent to the webhook when non-root and partOf integration object item is updated'() {
        given: 'ItemSavedEvent is not exported'
        def eventConfig = contextEventConfig ItemSavedEvent
        eventConfig.exportFlag = false
        modelService.save(eventConfig)

        and: 'both root and non-root partOf integration object items already exist'
        IntegrationTestUtil.importImpEx(
                'INSERT Customer; uid[unique = true]; addresses(&addrPk)',
                "               ; $UID              ; email",
                'INSERT Address; &addrPk; owner(Customer.uid)[unique = true]; email[unique = true]',
                "              ; email  ; $UID                              ; $UID@sap.com")

        and: 'ensure ItemSavedEvent passes'
        eventualCondition()
                .within(TIME_FOR_EVENT_TO_PASS)
                .retains { eventConfig.exportFlag }

        and: 'later webhook configuration is created'
        webhookConfiguration()
                .withDestination(webhookDestination(WEBHOOK_ENDPOINT))
                .withIntegrationObject(IO)
                .build()

        and: 'ItemSavedEvent is exported again'
        eventConfig.exportFlag = true
        modelService.save(eventConfig)

        when: 'the partOf item is updated'
        IntegrationTestUtil.importImpEx(
                'UPDATE Address; owner(Customer.uid)[unique = true]; email[unique = true]; company',
                "              ; $UID                              ; $UID@sap.com        ; SAP")

        then: 'the web hook is not notified'
        eventualCondition()
                .within(REASONABLE_TIME)
                .retains { verify(0, postRequestedFor(anyUrl())) }
    }

    @Test
    def 'notification is not sent to the webhook when the item is removed'() {
        given: 'ItemSavedEvent is not exported'
        def eventConfig = contextEventConfig ItemSavedEvent
        eventConfig.exportFlag = false
        modelService.save(eventConfig)

        and: 'integration object item already exist'
        IntegrationTestUtil.importImpEx(
                'INSERT Customer; uid[unique = true]')

        and: 'ensure ItemSavedEvent passes'
        eventualCondition()
                .within(TIME_FOR_EVENT_TO_PASS)
                .retains { eventConfig.exportFlag }

        and: 'later webhook configuration is created'
        webhookConfiguration()
                .withDestination(webhookDestination(WEBHOOK_ENDPOINT))
                .withIntegrationObject(IO)
                .build()

        and: 'ItemSavedEvent is exported again'
        eventConfig.exportFlag = true
        modelService.save(eventConfig)

        when: 'the item is removed'
        IntegrationTestUtil.importImpEx(
                'REMOVE Customer; uid[unique = true]',
                "               ; $UID")

        then: 'the web hook is not notified'
        eventualCondition()
                .within(REASONABLE_TIME)
                .retains { verify(0, postRequestedFor(anyUrl())) }
    }

    @Test
    def 'notification can be sent to multiple webhooks'() {
        given: 'multiple webhook configurations exist'
        webhookConfiguration()
                .withDestination(webhookDestination('webhook1', 'webhookE2EDestination1'))
                .withIntegrationObject(IO)
                .build()
        webhookConfiguration()
                .withDestination(webhookDestination('webhook2', 'webhookE2EDestination2'))
                .withIntegrationObject(IO)
                .build()

        when: 'a qualifying item is created'
        IntegrationTestUtil.importImpEx(
                'INSERT Customer; uid[unique = true]',
                "               ; $UID")

        then: 'the web hook is notified'
        eventualCondition().within(REASONABLE_TIME).expect {
            verify postRequestedFor(urlPathEqualTo('/webhook1'))
            verify postRequestedFor(urlPathEqualTo('/webhook2'))
        }
    }

    @Test
    @Issue('https://cxjira.sap.com/browse/IAPI-4619')
    def 'webhook filter passes matching items through to the webhook'() {
        given: 'a filter that allows any item through'
        def scriptCode = 'itemFilter'
        def script = '''"
        import de.hybris.platform.core.model.ItemModel
        import de.hybris.platform.webhookservices.filter.WebhookFilter
        import java.util.Optional
        
        class TestFilter implements WebhookFilter {
            public <T extends ItemModel> Optional<T> filter(T item) {
                Optional.ofNullable(item)
            }
        }
        new TestFilter()
        "'''
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Script; code[unique = true]; scriptType(code); content',
                "                    ; $scriptCode        ; GROOVY          ; $script"
        )
        and: 'a webhook configuration with the filter'
        webhookConfiguration()
                .withDestination(webhookDestination(WEBHOOK_ENDPOINT))
                .withIntegrationObject(IO)
                .withScriptFilter(scriptCode)
                .build()

        when: 'a qualifying item is created'
        IntegrationTestUtil.importImpEx(
                'INSERT Customer; uid[unique = true]',
                "               ; $UID")

        then: 'the web hook is notified for the matching item'
        eventualCondition().expect {
            verify postRequestedFor(urlPathEqualTo("/$WEBHOOK_ENDPOINT"))
        }

        cleanup:
        IntegrationTestUtil.remove(ScriptModel) {it.code == scriptCode }
    }

    @Test
    @Issue('https://cxjira.sap.com/browse/IAPI-4619')
    def 'webhook filter blocks a non-matching item from sending it to the webhook'() {
        given: 'a filter that blocks any item from being notified'
        def scriptCode = 'blockingFilter'
        def script = '''"
        import de.hybris.platform.core.model.ItemModel
        import de.hybris.platform.webhookservices.filter.WebhookFilter
        import java.util.Optional
        
        class TestFilter implements WebhookFilter {
            public <T extends ItemModel> Optional<T> filter(T item) {
                Optional.empty()
            }
        }
        new TestFilter()
        "'''
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Script; code[unique = true]; scriptType(code); content',
                "                    ; $scriptCode        ; GROOVY          ; $script"
        )
        and: 'a webhook configuration with the filter'
        webhookConfiguration()
                .withDestination(webhookDestination(WEBHOOK_ENDPOINT))
                .withIntegrationObject(IO)
                .withScriptFilter(scriptCode)
                .build()

        when: 'an item is created'
        IntegrationTestUtil.importImpEx(
                'INSERT Customer; uid[unique = true]',
                "               ; $UID")
        then: 'the filtered out item is not sent to the webhook'
        eventualCondition().within(REASONABLE_TIME).retains {
            verify(0, postRequestedFor(anyUrl()))
        }

        cleanup:
        IntegrationTestUtil.remove ScriptModel, {it.code == scriptCode }
    }

    @Test
    @Issue('https://cxjira.sap.com/browse/IAPI-4769')
    def 'Event is filtered out early when there is no WebhookConfiguration that has an Integration Object root item that supports it'() {
        given:
        'a webhook configuration that has Customer as the root item'
        webhookConfiguration()
                .withDestination(webhookDestination(WEBHOOK_ENDPOINT))
                .withIntegrationObject(IO)
                .build()

        when: 'a Catalog is created'
        def catalogId = 'WebhookE2ECatalog'
        IntegrationTestUtil.importImpEx(
                'INSERT Catalog; id[unique = true]',
                "              ; $catalogId")

        then: 'No item is sent out'
        eventualCondition().within(REASONABLE_TIME).retains {
            verify(0, postRequestedFor(anyUrl()))
        }

        cleanup:
        IntegrationTestUtil.remove CatalogModel, {it.id == catalogId }
    }

    def webhookDestination(String uri, String id = DESTINATION_ID) {
        consumedDestinationBuilder()
                .withId(id)
                .withUrl("https://localhost:${wireMockRule.httpsPort()}/$uri")
                .withDestinationTarget('webhookServices') // created in essential data
    }

    EventConfigurationModel contextEventConfig(Class eventType) {
        def eventConfigs = IntegrationTestUtil.findAny(DestinationTargetModel, { it.destinationChannel == DestinationChannel.WEBHOOKSERVICES })
                .map({ it.eventConfigurations })
                .orElse([])
        eventConfigs.find {it.eventClass == eventType.canonicalName }
    }
}
