/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webhookservices.service

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.order.events.SubmitOrderEvent
import de.hybris.platform.servicelayer.event.events.AbstractEvent
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel
import de.hybris.platform.webhookservices.service.impl.DefaultWebhookConfigurationService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultWebhookConfigurationServiceUnitTest extends Specification {

    def flexibleSearch = Stub FlexibleSearchService
    def descriptorFactory = Stub DescriptorFactory
    def service = new DefaultWebhookConfigurationService(flexibleSearch, descriptorFactory)

    @Test
    @Unroll
    def 'WebhookConfigurationService fails to instantiate when flexibleSearchService is #searchService and descriptorFactory is #factory'() {
        when:
        new DefaultWebhookConfigurationService(searchService, factory)

        then:
        def e = thrown IllegalArgumentException
        e.message == msg

        where:
        searchService               | factory                 | msg
        null                        | null                    | 'FlexibleSearchService cannot be null'
        null                        | Stub(DescriptorFactory) | 'FlexibleSearchService cannot be null'
        Stub(FlexibleSearchService) | null                    | 'DescriptorFactory cannot be null'
    }

    @Test
    @Unroll
    def "getWebhookConfigurationsByEventAndItemModel returns an empty collection when the event is #event and item is #item"() {
        expect:
        service.getWebhookConfigurationsByEventAndItemModel(event, item).empty

        where:
        event               | item
        null                | null
        null                | Stub(ItemModel)
        Stub(AbstractEvent) | null
    }

    @Test
    def "getWebhookConfigurationsByEventAndItemModel returns an empty collection when no configurations match event"() {
        given:
        def event = new SubmitOrderEvent()

        when:
        def configs = service.getWebhookConfigurationsByEventAndItemModel event, Stub(ItemModel)

        then:
        configs.empty

        and: 'Query contains the event class to search for'
        flexibleSearch.search(_ as FlexibleSearchQuery) >> { args ->
            with(args[0] as FlexibleSearchQuery) {
                queryParameters == ['eventClass':event.class.canonicalName]
            }
            Stub(SearchResult)
        }
    }

    @Test
    def "getWebhookConfigurationsByEventAndItemModel returns an empty collection when event matches but integration object's root item is null"() {
        given: "Found event but integration object's root item is null"
        def config = webhookConfigWithIntObjRootItem(null)
        flexibleSearch.search(_ as FlexibleSearchQuery) >> Stub(SearchResult) {
            getResult() >> [config]
        }

        when:
        def configs = service.getWebhookConfigurationsByEventAndItemModel Stub(AbstractEvent), new OrderModel()

        then: 'No events found with an Order Integration Object'
        configs.empty
    }

    @Test
    @Unroll
    def "getWebhookConfigurationsByEventAndItemModel only returns configuration matching event and item model"() {
        given: "Found multiple configurations with Integration Object of Product and Order root items"
        def configOfProduct = webhookConfigWithIntObjRootItem(rootItemOfType('Product'))
        def configOfOrder = webhookConfigWithIntObjRootItem(rootItemOfType('Order'))
        flexibleSearch.search(_ as FlexibleSearchQuery) >> Stub(SearchResult) {
            getResult() >> [configOfProduct, configOfOrder]
        }
        descriptorFactory.createItemTypeDescriptor({it.type.code == 'Order'}) >> Stub(TypeDescriptor) {
            isInstance(_ as OrderModel) >> true
        }
        descriptorFactory.createItemTypeDescriptor({it.type.code == 'Product'}) >> Stub(TypeDescriptor) {
            isInstance(_ as OrderModel) >> false
        }

        when: 'Searching for an event with an Integration Object root item of Order type'
        def configs = service.getWebhookConfigurationsByEventAndItemModel Stub(AbstractEvent), new OrderModel()

        then: 'Only config matching Order is returned'
        configs == [configOfOrder]
    }

    def rootItemOfType(def type) {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> type
            }
        }
    }

    def webhookConfigWithIntObjRootItem(IntegrationObjectItemModel rootItem) {
        def intObj = Stub(IntegrationObjectModel) {
            getRootItem() >> rootItem
        }
        def config = new WebhookConfigurationModel()
        config.setIntegrationObject(intObj)
        config
    }
}
