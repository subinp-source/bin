package de.hybris.training.core.events;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterItemCreationEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.training.core.job.VehicleCountJob;
import org.springframework.context.ApplicationEvent;

import java.util.logging.Logger;

public class NewEventForRegistrationNumber extends AbstractAcceleratorSiteEventListener<AfterItemCreationEvent> {


    private final static Logger LOG = Logger.getLogger(NewEventForRegistrationNumber.class.getName());




    public void message(){
        LOG.info("event success");
    }


    @Override
    protected void onSiteEvent(AfterItemCreationEvent event) {

    }

   /* @Override
    protected void onEvent(AfterItemCreationEvent afterItemCreationEvent) {

    }*/

    @Override
    protected SiteChannel getSiteChannelForEvent(AfterItemCreationEvent event) {
        return null;
    }
}
