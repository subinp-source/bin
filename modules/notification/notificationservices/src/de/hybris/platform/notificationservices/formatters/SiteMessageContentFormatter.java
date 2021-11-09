/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.formatters;

/**
 * Formatter to format message content.
 */
public interface SiteMessageContentFormatter
{

	/**
	 * Format source content
	 *
	 * @param source
	 *           the message content source to be formatted
	 * @return the formatted content
	 */
	String format(String source);
}
