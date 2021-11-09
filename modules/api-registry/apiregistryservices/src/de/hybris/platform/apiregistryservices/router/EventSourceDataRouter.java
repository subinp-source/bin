/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.router;

import de.hybris.platform.apiregistryservices.dto.EventSourceData;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.messaging.Message;

import java.util.Map;

public class EventSourceDataRouter
{
    private Map<String, String> eventRoutingMap;

    public String route(Message<EventSourceData> msg)
    {
        final EventConfigurationModel eventConfig = msg.getPayload().getEventConfig();
        if (eventConfig.getDestinationTarget() != null && eventConfig.getDestinationTarget().getDestinationChannel() != null)
        {
            return getEventRoutingMap().get(eventConfig.getDestinationTarget().getDestinationChannel().getCode());
        }
        else
        {
            return msg.getHeaders().getErrorChannel().toString();
        }
    }

    protected Map<String, String> getEventRoutingMap()
    {
        return eventRoutingMap;
    }

    @Required
    public void setEventRoutingMap(Map<String, String> eventRoutingMap)
    {
        this.eventRoutingMap = eventRoutingMap;
    }
}
