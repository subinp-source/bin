/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.jalo.IntegrationObject;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.model.impl.NullIntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.outboundservices.enums.OutboundSource;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.context.ApplicationContext;

import com.google.common.base.Preconditions;

/**
 * Context that is used to hold & transfer data pertaining to outbound requests.
 */
public class DecoratorContext
{
	private ItemModel itemModel;
	private IntegrationObjectDescriptor integrationObject;
	private ConsumedDestinationModel destinationModel;
	private OutboundSource outboundSource;
	private Collection<String> errors;

	/**
	 * @deprecated use {@link #DecoratorContext(ItemModel, IntegrationObjectDescriptor, ConsumedDestinationModel, OutboundSource, Collection)} instead
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	protected DecoratorContext()
	{
		// empty constructor for backwards compatibility
	}

	/**
	 * Instantiates a new context.
	 *
	 * @param item        item model being synchronized to an external system.
	 * @param io          integration object that governs the {@code item} presentation in the external system
	 * @param destination destination where the {@code item} is being sent.
	 * @param source      platform module that is performing the {@code item} synchronization. If value is not provided, i.e. it is
	 *                    {@code null}, then a default value of {@link OutboundSource#UNKNOWN} is used.
	 */
	protected DecoratorContext(@NotNull final ItemModel item,
	                           @NotNull final IntegrationObjectDescriptor io,
	                           @NotNull final ConsumedDestinationModel destination,
	                           final OutboundSource source,
	                           final Collection<String> errors)
	{
		Preconditions.checkArgument(item != null, "ItemModel cannot be null");
		Preconditions.checkArgument(io != null, "IntegrationObjectDescriptor cannot be null");
		Preconditions.checkArgument(destination != null, "ConsumedDestinationModel cannot be null");

		itemModel = item;
		integrationObject = io;
		destinationModel = destination;
		this.errors = errors == null ? new ArrayList<>() : new ArrayList<>(errors);
		outboundSource = source != null ? source : OutboundSource.UNKNOWN;
	}

	public static DecoratorContextBuilder decoratorContextBuilder()
	{
		return new DecoratorContextBuilder();
	}

	/**
	 * Retrieves the item model under concern.
	 *
	 * @return item model
	 */
	@NotNull
	public ItemModel getItemModel()
	{
		return itemModel;
	}

	/**
	 * Retrieves the {@link IntegrationObject#getCode()} value.
	 *
	 * @return integration object code
	 */
	@NotNull
	public IntegrationObjectDescriptor getIntegrationObject()
	{
		return integrationObject;
	}

	/**
	 * A convenience/shortcut method for retrieving the context integration object code. Calling this method is same as
	 * {@code getIntegrationObject().getCode()}
	 *
	 * @return code of the context integration object.
	 */
	public String getIntegrationObjectCode()
	{
		return getIntegrationObject().getCode();
	}

	/**
	 * Retrieves integration object item type descriptor for the context integration object and item model.
	 *
	 * @return an {@code Optional} containing a {@code TypeDescriptor}, if the integration object contains at least one item
	 * for the context item model type. I.e. there is a integration object item model matching the {@code getItemModel().getItemtype()}.
	 * Otherwise, an {@code Optional.empty()} is returned.
	 */
	public Optional<TypeDescriptor> getIntegrationObjectItem()
	{
		return getIntegrationObject().getItemTypeDescriptor(getItemModel());
	}

	/**
	 * @deprecated No longer used. Use {@link #getIntegrationObjectItem()} instead.
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	public String getIntegrationObjectItemCode()
	{
		return getIntegrationObjectItem().map(TypeDescriptor::getItemCode).orElse("");
	}

	/**
	 * Retrieves destination, to which the item should be sent.
	 *
	 * @return consumed destination
	 */
	@NotNull
	public ConsumedDestinationModel getDestinationModel()
	{
		return destinationModel;
	}

	/**
	 * Retrieves a list of errors that indicate why this context is not valid
	 * or returns an empty list.
	 *
	 * @return a list of errors
	 */
	@NotNull
	public Collection<String> getErrors()
	{
		return errors;
	}

	/**
	 * Retrieves a value indicating if this context is valid
	 * based off the presence of errors.
	 *
	 * @return {@code true} if this context has errors, else {@code false}
	 */
	@NotNull
	public boolean hasErrors()
	{
		return !errors.isEmpty();
	}

	/**
	 * Retrieves source of the item synchronization.
	 *
	 * @return a platform module that sends the item to external system.
	 */
	@NotNull
	public OutboundSource getSource()
	{
		return outboundSource;
	}

	public static class DecoratorContextBuilder
	{
		private ItemModel itemModel;
		private IntegrationObjectDescriptor integrationObject;
		private ConsumedDestinationModel destinationModel;
		private OutboundSource outboundSource;
		private Collection<String> errors;

		protected DecoratorContextBuilder()
		{
		}

		public DecoratorContextBuilder withItemModel(final ItemModel itemModel)
		{
			this.itemModel = itemModel;
			return this;
		}

		/**
		 * Specifies an integration object to be placed into the sync context to be created.
		 *
		 * @param io an integration object that will be used for describing data being sent out.
		 * @return a builder with the integration object specified.
		 */
		public DecoratorContextBuilder withIntegrationObject(final IntegrationObjectDescriptor io)
		{
			integrationObject = io;
			return this;
		}

		/**
		 * @deprecated not used anymore, use {@link #withIntegrationObject(IntegrationObjectDescriptor)} instead
		 */
		@Deprecated(since = "2011.0", forRemoval = true)
		public DecoratorContextBuilder withIntegrationObjectCode(final String code)
		{
			final IntegrationObjectModel model;
			try
			{
				final IntegrationObjectService ioService = getService("integrationObjectService", IntegrationObjectService.class);
				model = ioService.findIntegrationObject(code);
				final DescriptorFactory factory = getService("integrationServicesDescriptorFactory", DescriptorFactory.class);
				return withIntegrationObject(factory.createIntegrationObjectDescriptor(model));
			}
			catch (final ModelNotFoundException e)
			{
				withIntegrationObject(new NullIntegrationObjectDescriptor(code));
				return this;
			}
		}

		public DecoratorContextBuilder withDestinationModel(final ConsumedDestinationModel destinationModel)
		{
			this.destinationModel = destinationModel;
			return this;
		}

		/**
		 * @deprecated No longer used. The integration object item is derived from the context integration object and item model.
		 */
		@Deprecated(since = "2011.0", forRemoval = true)
		public DecoratorContextBuilder withIntegrationObjectItemCode(final String integrationObjectItemCode)
		{
			return this;
		}

		public DecoratorContextBuilder withOutboundSource(final OutboundSource source)
		{
			outboundSource = source;
			return this;
		}

		public DecoratorContextBuilder withErrors(final Collection<String> errors)
		{
			this.errors = errors;
			return this;
		}

		public DecoratorContext build()
		{
			return new DecoratorContext(itemModel, integrationObject, destinationModel, outboundSource, errors);
		}

		private static <T> T getService(final String name, final Class<T> type)
		{
			final ApplicationContext context = Registry.getApplicationContext();
			return context.getBean(name, type);
		}
	}
}
