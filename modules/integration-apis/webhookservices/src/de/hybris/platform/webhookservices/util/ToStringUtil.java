/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.util;

import de.hybris.platform.webhookservices.model.WebhookConfigurationModel;

/**
 * A utility for converting classes generated in the build process, which have no suitable toString() implementation, to a string.
 */
public class ToStringUtil
{
	private static final String WEBHOOK_TEMPLATE = "Webhook {destination=%s, integrationObject=%s, filterLocation=%s}";

	private ToStringUtil()
	{
		// non-instantiable utility class
	}

	/**
	 * Converts to a string
	 *
	 * @param model a webhook instance to present as a string
	 * @return string presentation of the model instance.
	 */
	public static String toString(final WebhookConfigurationModel model)
	{
		return model == null
				? "null"
				: String.format(WEBHOOK_TEMPLATE,
						model.getDestination().getId(), model.getIntegrationObject().getCode(),	model.getFilterLocation());
	}
}
