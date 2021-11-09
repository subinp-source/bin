/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.event;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.util.lifecycle.TenantLifecycle;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.servicelayer.event.EventSender;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel;
import de.hybris.platform.webhookservices.service.WebhookConfigurationService;

import java.util.Collection;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * An AfterSaveEvent listener that converts the {@link AfterSaveEvent} to an {@link ItemSavedEvent}
 * before sending it to the {@link EventSender}
 */
public class WebhookServicesAfterSaveEventListener implements AfterSaveListener
{
	private static final Logger LOGGER = Log.getLogger(WebhookServicesAfterSaveEventListener.class);
	private final EventSender eventSender;
	private final TenantLifecycle tenantLifecycle;
	private WebhookConfigurationService webhookConfigurationService;
	private ModelService modelService;

	/**
	 * Instantiates a {@link WebhookServicesAfterSaveEventListener}
	 *
	 * @param eventSender     An event sender that will send {@link ItemSavedEvent}
	 * @param tenantLifecycle TenantLifecycle to regulate the operation of this listener
	 */
	public WebhookServicesAfterSaveEventListener(@NotNull final EventSender eventSender,
	                                             @NotNull final TenantLifecycle tenantLifecycle)
	{
		Preconditions.checkArgument(eventSender != null, "eventSender cannot be null");
		Preconditions.checkArgument(tenantLifecycle != null, "tenantLifecycle cannot be null");
		this.eventSender = eventSender;
		this.tenantLifecycle = tenantLifecycle;
	}

	/**
	 * {@inheritDoc}
	 * Converts each {@link AfterSaveEvent} into {@link ItemSavedEvent}.
	 *
	 * @param events A collection of {@link AfterSaveEvent}
	 */
	@Override
	public void afterSave(final Collection<AfterSaveEvent> events)
	{
		if (tenantLifecycle.isOperational())
		{
			events.forEach(this::convertAndSendEvent);
		}
	}

	private void convertAndSendEvent(final AfterSaveEvent event)
	{
		try
		{
			LOGGER.debug("Event {}", event);
			final ItemSavedEvent afterSaveEvent = new ItemSavedEvent(event);
			if (isSendEvent(afterSaveEvent))
			{
				eventSender.sendEvent(afterSaveEvent);
			}
			else
			{
				LOGGER.debug("Event is not sent because no WebhookConfiguration supports the item in the event {}", event);
			}
		}
		catch (final InvalidEventTypeException e)
		{
			LOGGER.debug("The AfterSaveEvent is not converted to an ItemSavedEvent because {}", e.getMessage());
		}
	}

	private boolean isSendEvent(final ItemSavedEvent event)
	{
		if (webhookConfigurationService != null && modelService != null)
		{
			final Optional<ItemModel> item = getItem(event);
			return item
					.map(model -> isWebhookConfigurationExistForItem(event, model))
					.orElse(false);
		}
		return true;
	}

	private Boolean isWebhookConfigurationExistForItem(final ItemSavedEvent event, final ItemModel model)
	{
		LOGGER.debug("Event {} is for item type {}", event, model.getItemtype());
		final Collection<WebhookConfigurationModel> configs =
				webhookConfigurationService.getWebhookConfigurationsByEventAndItemModel(event, model);
		return !configs.isEmpty();
	}

	private Optional<ItemModel> getItem(final ItemSavedEvent event)
	{
		try
		{
			final Object object = modelService.get(event.getSavedItemPk());
			if (object instanceof ItemModel)
			{
				return Optional.of((ItemModel) object);
			}
			else
			{
				LOGGER.debug("Saved object for event {} is not of ItemModel type", event);
			}
		}
		catch (final ModelLoadingException | JaloObjectNoLongerValidException e)
		{
			logException(event, e);
		}
		catch (final RuntimeException e)
		{
			logException(event, e);
		}
		return Optional.empty();
	}

	private void logException(final ItemSavedEvent event, final RuntimeException e)
	{
		LOGGER.debug("Cannot retrieve the item for event {}", event, e);
	}


	/**
	 * Sets the WebhookConfigurationService
	 *
	 * @param webhookConfigurationService WebhookConfigurationService to search for WebhookConfigurations
	 */
	public void setWebhookConfigurationService(final WebhookConfigurationService webhookConfigurationService)
	{
		this.webhookConfigurationService = webhookConfigurationService;
	}

	/**
	 * Sets the ModelService
	 *
	 * @param modelService ModelService to search for items
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
