package de.hybris.platform.webhookservices.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.enums.DestinationChannel
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.core.model.security.UserRightModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants
import de.hybris.platform.tx.AfterSaveEvent
import de.hybris.platform.variants.model.VariantProductModel
import de.hybris.platform.webhookservices.event.ItemSavedEvent
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import org.junit.Test
import spock.lang.Shared

import javax.annotation.Resource

import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder
import static de.hybris.platform.webhookservices.WebhookConfigurationBuilder.webhookConfiguration

@IntegrationTest
class WebhookConfigurationServiceIntegrationTest extends ServicelayerSpockSpecification {

    private static final String IO = "WebhookConfigurationServiceTest"

    @Resource
    private WebhookConfigurationService webhookConfigurationService

    @Shared
    def itemSavedEvent = new ItemSavedEvent(new AfterSaveEvent(PK.fromLong(1), AfterSaveEvent.CREATE))

    def setupSpec() {
        importCsv '/impex/essentialdata-webhookservices.impex', 'UTF-8'
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove(EventConfigurationModel) {
            it.destinationTarget?.destinationChannel == DestinationChannel.WEBHOOKSERVICES
        }
        IntegrationTestUtil.remove(DestinationTargetModel) { it.destinationChannel == DestinationChannel.WEBHOOKSERVICES }
        consumedDestinationBuilder().cleanup()
    }

    def cleanup() {
        IntegrationTestUtil.removeAll WebhookConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeSafely(UserRightModel, {
            it.code == PermissionsConstants.READ ||
                    PermissionsConstants.CREATE ||
                    PermissionsConstants.CHANGE ||
                    PermissionsConstants.REMOVE
        })
    }

    @Test
    def "getWebhookConfigurationsByEventAndItemModel only returns WebhookConfigurations matching event and item model"() {
        given:
        def orderIOCode = "${IO}_ORDER"
        def intObjOrder = IntegrationObjectTestUtil.createIntegrationObject orderIOCode
        IntegrationObjectTestUtil.createIntegrationObjectRootItem intObjOrder, 'Order'

        and:
        def productIOCode = "${IO}_PRODUCT"
        def intObjProduct = IntegrationObjectTestUtil.createIntegrationObject productIOCode
        IntegrationObjectTestUtil.createIntegrationObjectRootItem intObjProduct, 'Product'

        and:
        webhookConfiguration()
                .withIntegrationObject(orderIOCode)
                .withDestination(consumedDestination('webhookOrderDestination'))
                .build()
        webhookConfiguration()
                .withIntegrationObject(productIOCode)
                .withDestination(consumedDestination('webhookProductDestination'))
                .build()

        when:
        def result = webhookConfigurationService.getWebhookConfigurationsByEventAndItemModel(itemSavedEvent, new OrderModel())

        then:
        result.size() == 1
        result.count {
            it.integrationObject.code == orderIOCode && it.eventType == ItemSavedEvent.canonicalName
        } == 1
    }

    @Test
    def "getWebhookConfigurationsByEventAndItemModel returns results when item is a subtype of the integration object's root item"() {
        given:
        def productIOCode = "${IO}_PRODUCT"
        def intObjProduct = IntegrationObjectTestUtil.createIntegrationObject productIOCode
        IntegrationObjectTestUtil.createIntegrationObjectRootItem intObjProduct, 'Product'

        and:
        def productDest = consumedDestination('webhookProductDestination')

        and:
        webhookConfiguration()
                .withIntegrationObject(productIOCode)
                .withDestination(productDest)
                .build()

        when:
        def result = webhookConfigurationService.getWebhookConfigurationsByEventAndItemModel(itemSavedEvent, new VariantProductModel())

        then:
        result.size() == 1
        result.count {
            it.integrationObject.code == productIOCode && it.eventType == ItemSavedEvent.canonicalName
        } == 1
    }

    ConsumedDestinationModel consumedDestination(String id) {
        def target = IntegrationTestUtil.findAny(DestinationTargetModel, {
            it.destinationChannel == DestinationChannel.WEBHOOKSERVICES
        }).orElseThrow { new IllegalStateException('A webhook destination target not found') }
        consumedDestinationBuilder()
                .withId(id)
                .withUrl('https://does.not/matter')
                .withDestinationTarget(target)
                .build()
    }
}