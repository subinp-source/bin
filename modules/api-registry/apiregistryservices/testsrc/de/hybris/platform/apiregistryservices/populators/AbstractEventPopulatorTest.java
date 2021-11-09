/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.populators;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.dto.EventSourceData;
import de.hybris.platform.apiregistryservices.event.DynamicProcessEvent;
import de.hybris.platform.apiregistryservices.model.ProcessEventConfigurationModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.apiregistryservices.model.events.EventPropertyConfigurationModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.events.SubmitOrderEvent;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

@IntegrationTest
public class AbstractEventPopulatorTest extends ServicelayerTransactionalTest
{
    private static final String ORDER_CODE_KEY = "orderCode";
    private static final String ORDER_TOTAL_PRICE_KEY = "orderTotalPrice";
    private static final String ORDER_CODE = "Test orderCode";
    private static final Double ORDER_TOTAL_PRICE = 123.4;
    private static final String PROCESS_CODE_KEY = "processCode";
    private static final String PROCESS_CODE = "testProcess";

    @Resource
    private ModelService modelService;

    private AbstractEventPopulator populator = new AbstractEventPopulator()
    {
        @Override
        public void populate(Object o, Object o2) throws ConversionException
        {
        }
    };

    private EventSourceData eventSourceData;


    @Test
    public void getValuesFromEvent() throws Exception
    {
        prepareEventConfiguration(prepareOrderModel(ORDER_CODE, ORDER_TOTAL_PRICE));
		 final Map<String, Object> valuesFromEvent = populator.getValuesFromEvent(eventSourceData.getEvent(),
				 eventSourceData.getEventConfig());

		 assertTrue("not correct size of result data", valuesFromEvent.size() == 2);
		 assertTrue("no totalPrice", valuesFromEvent.keySet().contains(ORDER_TOTAL_PRICE_KEY));
		 assertTrue("totalPrice not correct",
				 ORDER_TOTAL_PRICE.compareTo((Double) valuesFromEvent.get(ORDER_TOTAL_PRICE_KEY)) == 0);
		 assertTrue("no orderCode", valuesFromEvent.keySet().contains(ORDER_CODE_KEY));
		 assertTrue("orderCode not correct", valuesFromEvent.get(ORDER_CODE_KEY).equals(ORDER_CODE));
    }
    
    @Test
    public void getValuesFromEventWithNullOrderMember() throws Exception
    {
        prepareEventConfiguration(prepareOrderModel(ORDER_CODE, null));
		 final Map<String, Object> valuesFromEvent = populator.getValuesFromEvent(eventSourceData.getEvent(),
				 eventSourceData.getEventConfig());

		 assertTrue("not correct size of result data", valuesFromEvent.size() == 2);
		 assertTrue("no totalPrice", valuesFromEvent.keySet().contains(ORDER_TOTAL_PRICE_KEY));
		 assertNull("totalPrice should be null", valuesFromEvent.get(ORDER_TOTAL_PRICE_KEY));
		 assertTrue("no orderCode", valuesFromEvent.keySet().contains(ORDER_CODE_KEY));
		 assertTrue("orderCode not correct", valuesFromEvent.get(ORDER_CODE_KEY).equals(ORDER_CODE));
    }
    
    @Test
    public void getValuesFromEventWithNullOrder() throws Exception
    {
        prepareEventConfiguration(null);
		 final Map<String, Object> valuesFromEvent = populator.getValuesFromEvent(eventSourceData.getEvent(),
				 eventSourceData.getEventConfig());

		 assertTrue("not correct size of result data", valuesFromEvent.size() == 2);
		 assertTrue("no totalPrice", valuesFromEvent.keySet().contains(ORDER_TOTAL_PRICE_KEY));
		 assertNull("totalPrice should be null", valuesFromEvent.get(ORDER_TOTAL_PRICE_KEY));
		 assertTrue("no orderCode", valuesFromEvent.keySet().contains(ORDER_CODE_KEY));
		 assertNull("orderCode should be null", valuesFromEvent.get(ORDER_CODE_KEY));
    }

    @Test
    public void testProcessEventConfiguration()
    {
        prepareProcessEventConfiguration();
        final Map<String, Object> valuesFromEvent = populator
              .getValuesFromEvent(eventSourceData.getEvent(), eventSourceData.getEventConfig());

        assertTrue("not correct size of result data", valuesFromEvent.size() == 1);
        assertTrue("no process code", valuesFromEvent.keySet().contains(PROCESS_CODE_KEY));
        assertTrue("process code not correct", valuesFromEvent.get(PROCESS_CODE_KEY).equals(PROCESS_CODE));
    }

    protected OrderModel prepareOrderModel(final String code, final Double totalPrice)
    {
   	 final OrderModel order = modelService.create(OrderModel.class);
       order.setCode(code);
       order.setTotalPrice(totalPrice);
       return order;
    }
    
    protected void prepareEventConfiguration(final OrderModel order)
    {
        final EventConfigurationModel configuration = modelService.create(EventConfigurationModel.class);
        configuration.setEventClass(SubmitOrderEvent.class.getCanonicalName());
        configuration.setVersion(1);
        configuration.setExportName("ECSubmitOrderEvent");
        configuration.setEventPropertyConfigurations(Arrays.asList(buildEventPCM("event.order.code", ORDER_CODE_KEY),
              buildEventPCM("event.order.totalPrice", ORDER_TOTAL_PRICE_KEY)));

        final SubmitOrderEvent event = new SubmitOrderEvent();
        event.setOrder(order);

        eventSourceData = new EventSourceData();
        eventSourceData.setEventConfig(configuration);
        eventSourceData.setEvent(event);
    }

    protected void prepareProcessEventConfiguration()
    {
        final EventConfigurationModel configuration = modelService.create(ProcessEventConfigurationModel.class);
        configuration.setEventClass("Test Event");
        configuration.setVersion(1);
        configuration.setExportName("test-event");
        configuration.setEventPropertyConfigurations(Arrays.asList(buildEventPCM("process.code", PROCESS_CODE_KEY)));

        final BusinessProcessModel businessProcessModel = modelService.create(BusinessProcessModel.class);
        businessProcessModel.setCode(PROCESS_CODE);

        final DynamicProcessEvent event = new DynamicProcessEvent();
        event.setBusinessProcess(businessProcessModel);

        eventSourceData = new EventSourceData();
        eventSourceData.setEventConfig(configuration);
        eventSourceData.setEvent(event);
    }

    protected EventPropertyConfigurationModel buildEventPCM(final String mapping, final String name)
    {
        final EventPropertyConfigurationModel eventPCM = new EventPropertyConfigurationModel();
        eventPCM.setPropertyMapping(mapping);
        eventPCM.setPropertyName(name);
        eventPCM.setType("string");
        eventPCM.setTitle("test");
        return eventPCM;
    }
}
