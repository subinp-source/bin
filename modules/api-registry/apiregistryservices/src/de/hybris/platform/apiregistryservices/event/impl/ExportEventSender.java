/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.event.impl;

import de.hybris.platform.apiregistryservices.dao.EventConfigurationDao;
import de.hybris.platform.apiregistryservices.dto.EventSourceData;
import de.hybris.platform.apiregistryservices.event.DynamicProcessEvent;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.apiregistryservices.utils.EventExportUtils;
import de.hybris.platform.servicelayer.event.EventSender;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.tx.Transaction;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.support.MutableMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * One of additional event senders
 * Map fired AbstractEvent to existing EventConfigurationModels, wrap it in EventSourceData
 * and send to spring integration channel, default is eventSourceDataChannel.
 */
public class ExportEventSender implements EventSender
{
    private MessageChannel channel;
    private EventConfigurationDao eventConfigurationDao;
    private static final String ERROR_CHANNEL = "errorChannel";


    @Override
    public void sendEvent(final AbstractEvent abstractEvent)
    {
        if (!EventExportUtils.isEventExportActive())
        {
            return;
        }

        for (final String blacklisted : EventExportUtils.getBlacklist())
        {
            if (abstractEvent.getClass().getCanonicalName().equalsIgnoreCase(blacklisted)
                  || abstractEvent.getClass().getPackage().getName().equalsIgnoreCase(blacklisted))
            {
                return;
            }
        }

        if (!Transaction.isInCommitOrRollback())
        {
            final List<EventConfigurationModel> configurationModels = getEventConfigurationDao()
                  .findActiveEventConfigsByClass(getEventClass(abstractEvent));

            for (final EventConfigurationModel ecm : configurationModels)
            {
                final EventSourceData data = new EventSourceData();
                data.setEventConfig(ecm);
                data.setEvent(abstractEvent);
                getChannel().send(createGenericMessage(data));
            }
        }
    }

    protected String getEventClass(final AbstractEvent abstractEvent)
    {
        final String eventClass;

        if (abstractEvent instanceof DynamicProcessEvent)
        {
            eventClass = ((DynamicProcessEvent) abstractEvent).getBusinessEvent();
        }
        else
        {
            eventClass = abstractEvent.getClass().getCanonicalName();
        }

        return eventClass;
    }

    /**
     * @deprecated since 2005, please use the method {@link ExportEventSender#createGenericMessage(EventSourceData)}
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected Message wrapData(final EventSourceData data)
    {
        final MutableMessage message = new MutableMessage(data);
        message.getHeaders().put(MessageHeaders.REPLY_CHANNEL, ERROR_CHANNEL);
        message.getHeaders().put(MessageHeaders.ERROR_CHANNEL, ERROR_CHANNEL);
        return message;
    }

    protected GenericMessage<EventSourceData> createGenericMessage(final EventSourceData data)
    {
        final Map<String, Object> headersMap = new HashMap<>();
        headersMap.put(MessageHeaders.REPLY_CHANNEL, ERROR_CHANNEL);
        headersMap.put(MessageHeaders.ERROR_CHANNEL, ERROR_CHANNEL);
        return new GenericMessage<>(data, headersMap);
    }

    protected MessageChannel getChannel()
    {
        return channel;
    }

    @Required
    public void setChannel(final MessageChannel channel)
    {
        this.channel = channel;
    }

    protected EventConfigurationDao getEventConfigurationDao()
    {
        return eventConfigurationDao;
    }

    @Required
    public void setEventConfigurationDao(final EventConfigurationDao eventConfigurationDao)
    {
        this.eventConfigurationDao = eventConfigurationDao;
    }
}
