/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.filter;

import de.hybris.platform.scripting.engine.exception.DisabledScriptException;

/**
 * Indicates a problem with execution of the script used in a {@link de.hybris.platform.webhookservices.jalo.WebhookConfiguration}.
 * This means that the attribute configuration is valid, i.e. the script is found and loaded, but the script crashed during
 * execution.
 */
public class WebhookFilterException extends RuntimeException
{
	private static final String MSG_TEMPLATE = "Failed execution of script located at %s for filtering of webhook triggered by event %s. %s";

	/**
	 * Instantiates the exception with the information about the script that failed to execute.
	 *
	 * @param logicLocation String indicating where the script being executed is located
	 * @param eventType     Type of event that triggered the webhook where the script failed
	 * @param e             Exception with the cause of the failure
	 */
	public WebhookFilterException(final String logicLocation,
	                              final String eventType,
	                              final RuntimeException e)
	{
		super(message(logicLocation, eventType, e));
	}

	private static String message(final String logicLocation,
	                              final String eventType,
	                              final RuntimeException e)
	{
		final String details = e instanceof DisabledScriptException
				? "The script is disable and must be re-enabled."
				: "The script may be disabled now.";
		return String.format(MSG_TEMPLATE, logicLocation, eventType, details);
	}
}
