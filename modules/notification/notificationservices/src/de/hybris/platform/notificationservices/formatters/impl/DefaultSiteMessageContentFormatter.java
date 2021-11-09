/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.formatters.impl;

import de.hybris.platform.notificationservices.formatters.SiteMessageContentFormatter;


/**
 * Default implementation of {@link SiteMessageContentFormatter}
 */
public class DefaultSiteMessageContentFormatter implements SiteMessageContentFormatter
{

	@Override
	public String format(final String source)
	{
		return source;
	}

}
