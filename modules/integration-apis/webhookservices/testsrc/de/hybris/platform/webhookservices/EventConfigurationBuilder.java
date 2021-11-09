/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices;

import static de.hybris.platform.outboundservices.DestinationTargetBuilder.*;

import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.enums.EventPriority;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.outboundservices.DestinationTargetBuilder;
import de.hybris.platform.webhookservices.event.ItemSavedEvent;

import java.util.Optional;

/**
 * A helper to be used in integration tests for creating and persisting {@code EventConfiguration}s
 */
public class EventConfigurationBuilder
{
	private String eventClass;
	private int version;
	private String exportName;
	private String extensionName;
	private DestinationTargetModel destinationTarget;
	private EventPriority priority;
	private boolean exportFlag = true;

	private EventConfigurationBuilder()
	{
		// non-instantiable
	}

	/**
	 * Instantiates this builder.
	 *
	 * @return a builder instance to be used for further specification.
	 */
	public static EventConfigurationBuilder eventConfiguration()
	{
		return new EventConfigurationBuilder();
	}

	/**
	 * Specifies event class, for which the configuration has to be created. If this method is not called, the event class will
	 * default to {@link de.hybris.platform.tx.AfterSaveEvent}
	 *
	 * @param c an event class to build configuration for.
	 * @return a builder with the event class specified
	 */
	public EventConfigurationBuilder withEventClass(final Class<?> c)
	{
		return withEventClass(c.getName());
	}

	/**
	 * Specifies event class, for which the configuration has to be created. If this method is not called, the event class will
	 * default to {@link ItemSavedEvent}
	 *
	 * @param className class name, i.e. {@code Class.getName()}, to be used for the event configuration.
	 * @return a builder with the event class specified
	 */
	public EventConfigurationBuilder withEventClass(final String className)
	{
		eventClass = className;
		return this;
	}

	private String getEventClass()
	{
		return eventClass != null ? eventClass : ItemSavedEvent.class.getName();
	}

	/**
	 * Specifies version of the event configuration.
	 *
	 * @param v version number to use for the event configuration being built. If the specified version has a non-positive value
	 *          or this method is not called, default value of 1 will be used.
	 * @return a builder with the event configuration version specified
	 */
	public EventConfigurationBuilder withVersion(final int v)
	{
		version = v;
		return this;
	}

	private int getVersion()
	{
		return version;
	}

	/**
	 * Specifies name of the event in the export. If this method is not called, default value of {@code "webhookservices.<event_class_simple_name>"}
	 * will be used. For example, if event class is {@code de.hybris.platform.tx.AfterSaveEvent}, then the default export name will
	 * be {@code "webhookservices.AfterSaveEvent"}.
	 *
	 * @param name export name to be used for the event configuration being built
	 * @return a builder with the event export name specified
	 */
	public EventConfigurationBuilder withExportName(final String name)
	{
		exportName = name;
		return this;
	}

	private String getExportName()
	{
		return exportName != null
				? exportName
				: "webhookservices." + getSimpleClassName();
	}

	private String getSimpleClassName()
	{
		final String className = getEventClass();
		return className.contains(".")
				? className.substring(className.lastIndexOf(".") + 1)
				: className;
	}

	/**
	 * Specifies destination for event export. If this method is not called, default destination will be used.
	 *
	 * @param builder a builder specifying destination target parameters
	 * @return a builder with the event export destination specified
	 */
	public EventConfigurationBuilder withDestination(final DestinationTargetBuilder builder) throws ImpExException
	{
		return withDestination(builder.build());
	}

	/**
	 * Specifies destination for event export. If this method is not called, default destination will be used.
	 *
	 * @param code a destination target to use for the event configuration being built
	 * @return a builder with the event export destination specified
	 * @throws ImpExException when code does not correspond to an already persited destination target and the creation of the new
	 *                        destination target with that code fails.
	 */
	public EventConfigurationBuilder withDestination(final String code) throws ImpExException
	{
		final Optional<DestinationTargetModel> destination = getDestinationTargetById(code);
		return destination.isPresent()
				? withDestination(destination.get())
				: withDestination(destinationTarget().withId(code));
	}

	/**
	 * Specifies destination for event export. If this method is not called, default destination will be used.
	 *
	 * @param model a destination target to use for the event configuration being built
	 * @return a builder with the event export destination specified
	 * @throws ImpExException if the model was not persisted and fails while this method attempts to persist it.
	 */
	public EventConfigurationBuilder withDestination(final DestinationTargetModel model) throws ImpExException
	{
		if (model.getPk() != null)
		{
			destinationTarget = model;
			return this;
		}
		return withDestination(destinationTarget()
				.withId(model.getId())
				.withDestinationChannel(model.getDestinationChannel())
				.withRegistrationStatus(model.getRegistrationStatus()));
	}

	private DestinationTargetModel getDestination() throws ImpExException
	{
		if (destinationTarget == null)
		{
			destinationTarget = destinationTarget().withId("webhookservicesDestination")
			                                       .withDestinationChannel(DestinationChannel.WEBHOOKSERVICES)
			                                       .withRegistrationStatus(RegistrationStatus.REGISTERED)
			                                       .build();
		}
		return destinationTarget;
	}

	/**
	 * Specifies that the event should not be exported. If this method is not called, then the {@code exportFlag} of the built
	 * configuration will have value of {@code true}.
	 *
	 * @return a builder with {@code false} export flag specified
	 */
	public EventConfigurationBuilder withoutExport()
	{
		return withExport(false);
	}

	/**
	 * Specifies whether the event should be exported. If this method is not called, then the {@code exportFlag} of the built
	 * configuration will have value of {@code true}.
	 * @param flag {@code true} or {@code null}, if the event should be exported; {@code false}, otherwise.
	 *
	 * @return a builder with {@code false} export flag specified
	 */
	public EventConfigurationBuilder withExport(final Boolean flag)
	{
		if (flag != null)
		{
			exportFlag = flag;
		}
		return this;
	}

	/**
	 * Specifies name of the extension that owns the event configuration. If this method is not called, then the extension name
	 * will be defaulted to {@code "webhookservices"}.
	 *
	 * @return a builder with the extension name specified
	 */
	public EventConfigurationBuilder withExtensionName(final String name)
	{
		extensionName = name;
		return this;
	}

	private String getExtensionName()
	{
		return extensionName != null ? extensionName : "webhookservices";
	}

	/**
	 * Specifies priority of the event export. If this method is not called, default {@link EventPriority#LOW} will be used.
	 *
	 * @return a builder with the priority specified
	 */
	public EventConfigurationBuilder withPriority(final EventPriority p)
	{
		priority = p;
		return this;
	}

	private EventPriority getPriority()
	{
		return priority != null ? priority : EventPriority.LOW;
	}

	/**
	 * Persists an {@code EventConfigurationModel} according to the specifications done so far in this builder.
	 *
	 * @return the persisted event configuration or {@code null}, if the created configuration was not saved in the database for
	 * some reason.
	 * @throws ImpExException if persistence crashes
	 */
	public EventConfigurationModel build() throws ImpExException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE EventConfiguration; eventClass[unique = true]; exportName             ; exportFlag        ; extensionName             ; destinationTarget[unique = true]; version[unique = true]; priority(code)",
				"                                ;" + getEventClass() + "   ; " + getExportName() + "; " + exportFlag + "; " + getExtensionName() + "; " + getDestination().getPk() + "; " + getVersion() + "  ; " + getPriority());
		return IntegrationTestUtil.findAny(EventConfigurationModel.class, this::matchConfiguration)
		                          .orElse(null);
	}

	private boolean matchConfiguration(final EventConfigurationModel candidate)
	{
		try
		{
			return candidate.getEventClass().equals(getEventClass())
					&& candidate.getVersion() == getVersion()
					&& candidate.getDestinationTarget().equals(getDestination());
		}
		catch (final ImpExException e)
		{
			return false;
		}
	}
}
