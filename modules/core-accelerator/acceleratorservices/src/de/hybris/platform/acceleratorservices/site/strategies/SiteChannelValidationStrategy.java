/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.site.strategies;

import de.hybris.platform.commerceservices.enums.SiteChannel;


/**
 * Strategy interface for site channel validation.
 */
public interface SiteChannelValidationStrategy
{
	/**
	 * Validates the {@link SiteChannel}.
	 * 
	 * @param siteChannel
	 *           the {@link SiteChannel} to validate.
	 * @return <code>true</code> if the {@link SiteChannel} is supported, <code>false</code> otherwise
	 */
	boolean validateSiteChannel(SiteChannel siteChannel);
}
