/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.model;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.integrationservices.scripting.LogicLocation;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.webhookservices.event.ItemSavedEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

/**
 * This ValidateInterceptor validates the {@link WebhookConfigurationModel} is proper before saving it
 */
public final class WebhookConfigurationValidateInterceptor implements ValidateInterceptor<WebhookConfigurationModel>
{
	private static final String SUPPORTED_EVENT = ItemSavedEvent.class.getCanonicalName();
	private static final String ERROR_MSG_LOCATION = "WebhookConfigurationModel is misconfigured: Filter location '%s' provided does not meet the pattern model://<script_code>";
	private static final String ERROR_MSG_UNSUPPORTED = "%s event type is not supported. Supported type is " + SUPPORTED_EVENT + ".";
	private static final String ERROR_MSG_UNREGISTERED = "WebhookConfigurationModel is misconfigured: %s is not registered with the destination target";

	@Override
	public void onValidate(final WebhookConfigurationModel config, final InterceptorContext context) throws InterceptorException
	{
		if (!isSupportedEvent(config))
		{
			throw new InterceptorException(errorMessage(ERROR_MSG_UNSUPPORTED, config), this);
		}
		if (!isRegisteredEvent(config))
		{
			throw new InterceptorException(errorMessage(ERROR_MSG_UNREGISTERED, config), this);
		}
		if (!isValidScriptLocation(config))
		{
			throw new InterceptorException(String.format(ERROR_MSG_LOCATION, config.getFilterLocation()), this);
		}
	}

	private static boolean isSupportedEvent(final WebhookConfigurationModel config)
	{
		return SUPPORTED_EVENT.equals(config.getEventType());
	}

	private static boolean isRegisteredEvent(final WebhookConfigurationModel config)
	{
		final Collection<EventConfigurationModel> events = Optional.of(config)
		                                                           .map(WebhookConfigurationModel::getDestination)
		                                                           .map(ConsumedDestinationModel::getDestinationTarget)
		                                                           .map(DestinationTargetModel::getEventConfigurations)
		                                                           .orElseGet(Collections::emptyList);
		return events.stream()
		             .map(EventConfigurationModel::getEventClass)
		             .anyMatch(clName -> clName.equals(config.getEventType()));
	}

	private static boolean isValidScriptLocation(final WebhookConfigurationModel config)
	{
		return StringUtils.isEmpty(config.getFilterLocation()) || LogicLocation.isValid(config.getFilterLocation());
	}

	private static String errorMessage(final String template, final WebhookConfigurationModel config)
	{
		return String.format(template, config.getEventType());
	}
}
