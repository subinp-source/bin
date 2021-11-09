/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.filter.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.scripting.LogicLocation;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.webhookservices.filter.WebhookFilter;
import de.hybris.platform.webhookservices.filter.WebhookFilterService;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * A default implementation of the {@code WebhookFilterService} that uses the platform's scripting engine for
 * executing the {@link de.hybris.platform.webhookservices.filter.WebhookFilter} logic.
 */
public class DefaultWebhookFilterService implements WebhookFilterService
{
	private static final Logger LOGGER = Log.getLogger(DefaultWebhookFilterService.class);

	private final ScriptingLanguagesService scriptingLanguagesService;

	/**
	 * Constructs the DefaultWebhookFilterService
	 *
	 * @param scriptingLanguagesService Service to run scripts
	 */
	public DefaultWebhookFilterService(@NotNull final ScriptingLanguagesService scriptingLanguagesService)
	{
		Preconditions.checkArgument(scriptingLanguagesService != null, "Scripting language service must be provided");
		this.scriptingLanguagesService = scriptingLanguagesService;
	}

	/**
	 * {@inheritDoc}
	 * <p>It's important that the script referenced by the {@code scriptUri} would implement
	 * {@link de.hybris.platform.webhookservices.filter.WebhookFilter} interface. Otherwise,
	 * the filtering won't work, i.e. all items should be filtered out by the implementations.</p>
	 * <p>Any exception thrown by the filter script eliminates the item from being sent to the webhook.</p>
	 * <p>If the script returns an ItemModel type that differs from the input {@code item} type,
	 * the item will be filtered out. However, if the returned ItemModel type is a subtype of the
	 * input {@code item} type, the item will be sent.</p>
	 */
	@Override
	public <T extends ItemModel> Optional<T> filter(final T item, final String scriptUri)
	{
		return StringUtils.isBlank(scriptUri) ?
				Optional.ofNullable(item) :
				executeScript(item, scriptUri);
	}

	private <T extends ItemModel> Optional<T> executeScript(final T item, final String scriptUri)
	{
		Optional<T> returnValue = Optional.empty();
		if (canProceedWithFilter(item, scriptUri))
		{
			try
			{
				returnValue = scriptingLanguagesService.getExecutableByURI(scriptUri)
				                                       .getAsInterface(WebhookFilter.class)
				                                       .filter(item);
			}
			catch (final RuntimeException e)
			{
				LOGGER.debug("An exception occurred while executing the script", e);
			}
		}
		else
		{
			LOGGER.debug("Cannot proceed with filtering because item {} item and/or scriptUri {} are invalid", item, scriptUri);
		}
		return isReturnable(item, returnValue) ? returnValue : Optional.empty();
	}

	private boolean canProceedWithFilter(final ItemModel item, final String scriptUri)
	{
		return item != null && LogicLocation.isValid(scriptUri);
	}

	private <T extends ItemModel> boolean isReturnable(final ItemModel item, final Optional<T> returnValue)
	{
		final var returnable = returnValue != null && returnValue
				.map(val -> item.getClass().isAssignableFrom(val.getClass()))
				.orElse(false);
		if (!returnable)
		{
			LOGGER.debug("Return value from the script's WebhookFilter is not returnable");
		}
		return returnable;
	}
}
