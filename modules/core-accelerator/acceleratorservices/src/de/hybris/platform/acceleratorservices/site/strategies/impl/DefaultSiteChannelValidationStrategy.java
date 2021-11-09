/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.site.strategies.impl;

import de.hybris.platform.acceleratorservices.site.strategies.SiteChannelValidationStrategy;
import de.hybris.platform.commerceservices.enums.SiteChannel;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link SiteChannelValidationStrategy} which validated against a configured list of
 * supported {@link SiteChannel} objects.
 */
public class DefaultSiteChannelValidationStrategy implements SiteChannelValidationStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultSiteChannelValidationStrategy.class);
	private Set<SiteChannel> supportedSiteChannels;

	@Override
	public boolean validateSiteChannel(final SiteChannel siteChannel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Validating site channel [%s] against supported channels [%s]", siteChannel,
					getSupportedSiteChannels()));
		}
		return getSupportedSiteChannels().contains(siteChannel);
	}

	protected Set<SiteChannel> getSupportedSiteChannels()
	{
		return supportedSiteChannels;
	}

	@Required
	public void setSupportedSiteChannels(final Set<SiteChannel> supportedSiteChannels)
	{
		this.supportedSiteChannels = supportedSiteChannels;
	}

}
