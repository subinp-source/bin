/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingmock.adaptors.impl;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.consignmenttrackingmock.service.impl.MockConsignmentTrackingService;
import de.hybris.platform.consignmenttrackingservices.adaptors.CarrierAdaptor;
import de.hybris.platform.consignmenttrackingservices.delivery.data.ConsignmentEventData;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.site.BaseSiteService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class MockCarrierAdaptor implements CarrierAdaptor
{
	private static final Logger LOG = LoggerFactory.getLogger(MockCarrierAdaptor.class);
	private static final String TRACKING_URL_KEY = "mock.carrier.tracking.url";
	private static final String DELIVERY_LEAD_TIME_KEY = "default.delivery.lead.time";
	private ConfigurationService configurationService;
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	private BaseSiteService baseSiteService;
	private MockConsignmentTrackingService mockConsignmentTrackingService;

	@Override
	public List<ConsignmentEventData> getConsignmentEvents(final String trackingId)
	{
		return mockConsignmentTrackingService.getConsignmentEvents(trackingId);
	}

	@Override
	public URL getTrackingUrl(final String trackingID)
	{
		final Configuration config = getConfigurationService().getConfiguration();
		final String trackingUrl = config.getString(TRACKING_URL_KEY, StringUtils.EMPTY);
		if(StringUtils.isNotBlank(trackingUrl))
		{
			try
			{
				return new URL(trackingUrl);
			}
			catch (final MalformedURLException e)
			{
				LOG.error("Invalid Tracking URL");
			}
		}

		return null;
	}

	@Override
	public int getDeliveryLeadTime(final ConsignmentModel consignment)
	{
		final Configuration config = getConfigurationService().getConfiguration();
		return config.getInt(DELIVERY_LEAD_TIME_KEY, 0);
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	@Required
	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}


	public MockConsignmentTrackingService getMockConsignmentTrackingService()
	{
		return mockConsignmentTrackingService;
	}
	@Required
	public void setMockConsignmentTrackingService(final MockConsignmentTrackingService mockConsignmentTrackingService)
	{
		this.mockConsignmentTrackingService = mockConsignmentTrackingService;
	}
}
