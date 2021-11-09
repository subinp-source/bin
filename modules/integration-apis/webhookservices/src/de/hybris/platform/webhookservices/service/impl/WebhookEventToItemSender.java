/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.service.impl;

import de.hybris.platform.apiregistryservices.dto.EventSourceData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.service.ItemModelSearchService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundservices.enums.OutboundSource;
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade;
import de.hybris.platform.outboundservices.facade.SyncParameters;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webhookservices.event.ItemSavedEvent;
import de.hybris.platform.webhookservices.filter.WebhookFilterService;
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel;
import de.hybris.platform.webhookservices.service.WebhookConfigurationService;
import de.hybris.platform.webhookservices.service.WebhookEventSender;
import de.hybris.platform.webhookservices.util.ToStringUtil;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * Default implementation of the {@link WebhookEventSender}. This implementation uses the {@link OutboundServiceFacade}
 * to do the actual posting to the destination because the OutboundServiceFacade already provides item to JSON conversion, and
 * monitoring.
 */
public final class WebhookEventToItemSender implements WebhookEventSender
{
	private static final Logger LOGGER = Log.getLogger(WebhookEventToItemSender.class);

	private final WebhookConfigurationService webhookConfigurationService;
	private ItemModelSearchService itemModelSearchService;
	private ModelService modelService;
	private OutboundServiceFacade outboundServiceFacade;
	@NotNull
	private WebhookFilterService filterService = nullFilterService();

	/**
	 * Instantiates the WebhookEventToItemSender
	 *
	 * @param outboundServiceFacade       OutboundServiceFacade used to send the event out to the destination
	 * @param webhookConfigurationService WebhookConfigurationService used to look up {@link WebhookConfigurationModel}
	 * @param modelService                ModelService used to look up {@link ItemModel}
	 * @deprecated use {@link #WebhookEventToItemSender(OutboundServiceFacade, WebhookConfigurationService, ItemModelSearchService)} } instead
	 */
	@Deprecated(since = "2105.0", forRemoval = true)
	public WebhookEventToItemSender(@NotNull final OutboundServiceFacade outboundServiceFacade,
	                                @NotNull final WebhookConfigurationService webhookConfigurationService,
	                                @NotNull final ModelService modelService)
	{
		Preconditions.checkArgument(outboundServiceFacade != null, "OutboundServiceFacade cannot be null");
		Preconditions.checkArgument(webhookConfigurationService != null, "WebhookConfigurationService cannot be null");
		Preconditions.checkArgument(modelService != null, "ModelService cannot be null");

		this.outboundServiceFacade = outboundServiceFacade;
		this.webhookConfigurationService = webhookConfigurationService;
		this.modelService = modelService;
	}

	/**
	 * Instantiates the WebhookEventToItemSender
	 *
	 * @param outboundServiceFacade       OutboundServiceFacade used to send the event out to the destination
	 * @param webhookConfigurationService WebhookConfigurationService used to look up {@link WebhookConfigurationModel}
	 * @param itemModelSearchService      ItemModelSearchService used to look up {@link ItemModel}
	 */
	public WebhookEventToItemSender(@NotNull final OutboundServiceFacade outboundServiceFacade,
	                                @NotNull final WebhookConfigurationService webhookConfigurationService,
	                                @NotNull final ItemModelSearchService itemModelSearchService)
	{
		Preconditions.checkArgument(outboundServiceFacade != null, "OutboundServiceFacade cannot be null");
		Preconditions.checkArgument(webhookConfigurationService != null, "WebhookConfigurationService cannot be null");
		Preconditions.checkArgument(itemModelSearchService != null, "ItemModelSearchService cannot be null");

		this.outboundServiceFacade = outboundServiceFacade;
		this.webhookConfigurationService = webhookConfigurationService;
		this.itemModelSearchService = itemModelSearchService;
	}

	@Override
	public void send(final EventSourceData eventSourceData)
	{
		Preconditions.checkArgument(eventSourceData != null, "EventSourceData cannot be null");

		final var event = eventSourceData.getEvent();
		final var item = findItem(event);
		item.ifPresent(i -> sendItem(i, event));
	}

	private Optional<ItemModel> findItem(final AbstractEvent event)
	{
		if (isItemSavedEvent(event))
		{
			return getItemModelSearchService().nonCachingFindByPk(((ItemSavedEvent) event).getSavedItemPk());
		}
		return Optional.empty();
	}

	private boolean isItemSavedEvent(final AbstractEvent event)
	{
		return event instanceof ItemSavedEvent;
	}

	private void sendItem(final ItemModel item, final AbstractEvent event)
	{
		final var webhookConfigs = webhookConfigurationService.getWebhookConfigurationsByEventAndItemModel(event, item);
		webhookConfigs.forEach(config -> filterAndSend(config, item));
	}

	private void filterAndSend(final WebhookConfigurationModel config, final ItemModel item)
	{
		logDebug(item, config);
		filterService.filter(item, config.getFilterLocation())
		             .ifPresentOrElse(it -> sendToWebhook(config, it),
				             () -> LOGGER.trace("Item {} was filtered out from being sent to {}", item,
						             ToStringUtil.toString(config)));
	}

	private void sendToWebhook(final WebhookConfigurationModel webhookConfig, final ItemModel item)
	{
		final SyncParameters params = SyncParameters.syncParametersBuilder()
		                                            .withItem(item)
		                                            .withSource(OutboundSource.WEBHOOKSERVICES)
		                                            .withIntegrationObject(webhookConfig.getIntegrationObject())
		                                            .withDestination(webhookConfig.getDestination())
		                                            .build();

		final var observable = outboundServiceFacade.send(params);
		observable.subscribe(response -> logInfo(item, webhookConfig),
				error -> logError(item, webhookConfig, error));
	}

	private void logDebug(final ItemModel item, final WebhookConfigurationModel config)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Sending item '{}' to {}", item.getItemtype(), ToStringUtil.toString(config));
		}
	}

	private void logInfo(final ItemModel item, final WebhookConfigurationModel cfg)
	{
		if (LOGGER.isInfoEnabled())
		{
			LOGGER.info("Successfully sent item of type '{}' to {}", item.getItemtype(), ToStringUtil.toString(cfg));
		}
	}

	private void logError(final ItemModel item, final WebhookConfigurationModel cfg, final Throwable ex)
	{
		if (LOGGER.isErrorEnabled())
		{
			LOGGER.error("Failed to sent item of type '{}' to {}", item.getItemtype(), ToStringUtil.toString(cfg), ex);
		}
	}

	private WebhookFilterService nullFilterService()
	{
		return new WebhookFilterService()
		{
			@Override
			public <T extends ItemModel> Optional<T> filter(final T item, final String scriptUri)
			{
				return Optional.of(item);
			}
		};
	}

	ItemModelSearchService getBeanFromApplicationContext(final String beanName)
	{
		return Registry.getApplicationContext().getBean(beanName, ItemModelSearchService.class);
	}

	void setOutboundServiceFacade(final OutboundServiceFacade outboundServiceFacade)
	{
		this.outboundServiceFacade = outboundServiceFacade;
	}

	/**
	 * Injects an optional filter service to be used by this item sender. If the webhook filter service is not injected, no item
	 * filtering will be performed.
	 *
	 * @param service an implementation of the {@code WebhookFilterService} that will execute filtering logic for the items being
	 *                sent by this item sender.
	 */
	public void setFilterService(final WebhookFilterService service)
	{
		filterService = service != null ? service : nullFilterService();
	}

	ItemModelSearchService getItemModelSearchService()
	{
		if (itemModelSearchService == null)
		{
			itemModelSearchService = getBeanFromApplicationContext("itemModelSearchService");
		}
		return itemModelSearchService;
	}
}
