/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices;

import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.webhookservices.event.ItemSavedEvent;
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.rules.ExternalResource;

import com.google.common.base.Preconditions;

/**
 * A helper to be used in integration tests for creating and persisting {@code WebhookConfiguration}s
 */
public class WebhookConfigurationBuilder extends ExternalResource
{
	private final Set<PK> createdWebhooks;
	private final Set<ConsumedDestinationBuilder> createdDestinations;
	private Class<? extends AbstractEvent> eventType;
	private ConsumedDestinationModel destination;
	private ConsumedDestinationBuilder destinationBuilder;
	private String integrationObjectCode;
	private String filterLocation = "";

	private WebhookConfigurationBuilder()
	{
		createdWebhooks = new HashSet<>();
		createdDestinations = new HashSet<>();
	}

	/**
	 * Instantiates a web hook configuration builder, that is initialized to the default state.
	 *
	 * @return a builder ready for further specifications
	 */
	public static WebhookConfigurationBuilder webhookConfiguration()
	{
		return new WebhookConfigurationBuilder();
	}

	/**
	 * Specifies event type for the web hook configuration being built
	 *
	 * @param type type of the event that will be processed by the web hook configuration
	 * @return a builder with the event type specified
	 */
	public WebhookConfigurationBuilder withEvent(final Class<? extends AbstractEvent> type)
	{
		eventType = type;
		return this;
	}

	/**
	 * Specifies destination for the web hook notifications.
	 *
	 * @param builder a builder specifying the destination
	 * @return a builder with the destination specified
	 */
	public WebhookConfigurationBuilder withDestination(final ConsumedDestinationBuilder builder)
	{
		destinationBuilder = builder;
		destination = null;
		return this;
	}

	/**
	 * Specifies destination for the web hook notifications.
	 *
	 * @param dest a destination to be used by the web hook configuration
	 * @return a builder with the destination specified
	 */
	public WebhookConfigurationBuilder withDestination(final ConsumedDestinationModel dest)
	{
		if (dest.getPk() != null)
		{
			destination = dest;
			destinationBuilder = null;
			return this;
		}
		return withDestination(consumedDestinationBuilder()
				.withId(dest.getId())
				.withUrl(dest.getUrl())
				.withEndpoint(dest.getEndpoint())
				.withDestinationTarget(dest.getDestinationTarget())
				.withCredential(dest.getCredential())
				.withAdditionalParameters(dest.getAdditionalProperties()));
	}

	/**
	 * Specifies integration object to be used for payload creation of the web hook notification
	 *
	 * @param ioCode code of the integration object to be associated with the web hook configuration
	 * @return a builder with the integration object specified
	 */
	public WebhookConfigurationBuilder withIntegrationObject(final String ioCode)
	{
		integrationObjectCode = ioCode;
		return this;
	}

	/**
	 * Specifies a script that will be used as a filter for the items processed by the webhook being built.
	 * @param scriptCode code of the script existing in the system, that will be used as a filter for the webhook. If {@code null},
	 *                   or blank or this method was not called, then no filter will be used.
	 *
	 * @return a builder with the filter specified
	 */
	public WebhookConfigurationBuilder withScriptFilter(final String scriptCode)
	{
		filterLocation = StringUtils.isNotBlank(scriptCode) ? "model://" + scriptCode : "";
		return this;
	}

	/**
	 * Creates and persists a web hook configuration according to the specification performed so far on this builder.
	 *
	 * @return The created WebhookConfigurationModel
	 * @throws ImpExException if the created {@code WebhookConfiguration} failed to persist
	 */
	public WebhookConfigurationModel build() throws ImpExException
	{
		Preconditions.checkState(integrationObjectCode != null, "Integration object has not been specified");

		final var eventTypeHeader = eventType != null ? " eventType[unique = true]; " : " ";
		final var eventTypeValue = eventType != null ? "" + eventType.getCanonicalName() + "; " : " ";
		final var dest = deriveDestination();
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE WebhookConfiguration;" + eventTypeHeader + "destination[unique = true] ; integrationObject(code)[unique = true]; filterLocation",
				"                                  ;" + eventTypeValue + dest.getPk() + "             ; " + integrationObjectCode + "         ; " + filterLocation);
		final var webhook = lookupWebhookConfiguration(new Key(eventType, dest, integrationObjectCode));
		createdWebhooks.add(webhook.getPk());
		return webhook;
	}

	private ConsumedDestinationModel deriveDestination()
	{
		return destination != null
				? destination
				: buildDestination();
	}

	private ConsumedDestinationModel buildDestination()
	{
		Preconditions.checkState(destinationBuilder != null, "Consumed destination has not been specified");
		createdDestinations.add(destinationBuilder);
		withDestination(destinationBuilder.build());
		return destination;
	}

	private WebhookConfigurationModel lookupWebhookConfiguration(final Key key)
	{
		return IntegrationTestUtil.findAny(WebhookConfigurationModel.class, key::matches).orElse(null);
	}

	@Override
	protected void after()
	{
		cleanup();
	}

	/**
	 * Deletes all items persisted by this builder.
	 */
	public void cleanup() {
		createdWebhooks.forEach(this::deleteWebhook);
		createdWebhooks.clear();
		ConsumedDestinationBuilder.cleanup();
		createdDestinations.clear();
	}

	private void deleteWebhook(final PK key)
	{
		IntegrationTestUtil.remove(WebhookConfigurationModel.class, m -> m.getPk().equals(key));
	}

	private static final class Key
	{
		private final String event;
		private final PK destination;
		private final String integrationObject;

		private Key(final Class<? extends AbstractEvent> ev, final ConsumedDestinationModel dest, final String io)
		{
			event = (ev != null ? ev : ItemSavedEvent.class).getCanonicalName();
			destination = dest.getPk();
			integrationObject = io;
		}

		private boolean matches(final WebhookConfigurationModel cfg)
		{
			return cfg != null
					&& cfg.getDestination() != null
					&& cfg.getIntegrationObject() != null
					&& event.equals(cfg.getEventType())
					&& destination.equals(cfg.getDestination().getPk())
					&& integrationObject.equals(cfg.getIntegrationObject().getCode());
		}
	}
}