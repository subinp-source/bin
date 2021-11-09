/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.listeners;

import com.hybris.yprofile.services.ProfileTransactionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.events.SubmitOrderEvent;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Event listener for order submit event.
 */
public class NewOrderEventListener extends AbstractSiteEventListener<SubmitOrderEvent>
{
    private static final Logger LOG = Logger.getLogger(NewOrderEventListener.class);
    private ProfileTransactionService profileTransactionService;
    private BaseSiteService baseSiteService;

    @Override
    protected void onSiteEvent(final SubmitOrderEvent event) {
        try {
            final OrderModel order = event.getOrder();
            ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);

            if (order == null) {
                LOG.warn("Order is null. Unable to send order to yProfile");
            } else {
                setCurrentBaseSite(event);
                getProfileTransactionService().sendSubmitOrderEvent(order);
            }
        } catch (Exception e) {
            LOG.error("Error sending New Order event: " + e.getMessage());
            LOG.debug("Error sending New Order event: ", e);
        }
    }

    protected void setCurrentBaseSite(final SubmitOrderEvent event) {

        if (getBaseSiteService().getCurrentBaseSite() == null) {
            getBaseSiteService().setCurrentBaseSite(event.getOrder().getSite(), true);
        }
    }

    @Override
    protected boolean shouldHandleEvent(final SubmitOrderEvent event) {
        final OrderModel order = event.getOrder();
        ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
        final BaseSiteModel site = order.getSite();
        ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
        return SiteChannel.B2C.equals(site.getChannel());
    }


    protected ProfileTransactionService getProfileTransactionService() {
        return profileTransactionService;
    }

    @Required
    public void setProfileTransactionService(final ProfileTransactionService profileTransactionService) {
        this.profileTransactionService = profileTransactionService;
    }

    public BaseSiteService getBaseSiteService() {
        return baseSiteService;
    }

    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }
}
