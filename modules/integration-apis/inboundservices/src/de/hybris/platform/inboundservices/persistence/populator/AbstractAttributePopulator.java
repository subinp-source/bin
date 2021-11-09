/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.AttributePopulator;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;

/**
 * A template to be used by {@link AttributePopulator} implementations.
 */
public abstract class AbstractAttributePopulator implements AttributePopulator
{
	private ModelService modelService;

	/**
	 * {@inheritDoc}
	 * <p>This implementation excludes attributes, which are not applicable for this populator, and for the
	 * rest it delegates to {@link #populateAttribute(ItemModel, TypeAttributeDescriptor, PersistenceContext)}</p>
	 * @param model a model to populate attributes in.
	 * @param context a context carrying all information needed to know, which attributes need to be populated and how they should
	 * @see #isApplicable(TypeAttributeDescriptor, PersistenceContext)
	 * @see #populateAttribute(ItemModel, TypeAttributeDescriptor, PersistenceContext)
	 */
	@Override
	public void populate(final ItemModel model, final PersistenceContext context)
	{
		final var item = context.getIntegrationItem();
		item.getAttributes().stream()
				.filter(attr -> isApplicable(attr, context))
				.filter(attr -> attr.isSettable(model))
				.forEach(attr -> handlePopulateAttribute(attr, model, context));
	}

	/**
	 * Determines whether a specific implementation can provide value for the specified attribute.
	 * @param attribute attribute to make the decision about.
	 * @param context context that may be needed to make the decision.
	 * @return {@code true}, if this attribute processor is applicable to the specified attribute and can provide a value for it;
	 * {@code false}, otherwise.
	 */
	protected abstract boolean isApplicable(TypeAttributeDescriptor attribute, final PersistenceContext context);

	private void handlePopulateAttribute(final TypeAttributeDescriptor attr, final ItemModel model, final PersistenceContext context)
	{
		try
		{
			populateAttribute(model, attr, context);
		}
		catch (final AttributeNotSupportedException e)
		{
			throw new UnmodifiableAttributeException(attr, context, e);
		}

	}

	/**
	 * Populates value of the specified item attribute.
	 * @param item item model to be populated.
	 * @param attribute specifies, which attribute should be populated in the {@code item}.
	 * @param context context information necessary to derive correct attribute value from the integration item.
	 */
	protected abstract void populateAttribute(ItemModel item, TypeAttributeDescriptor attribute, PersistenceContext context);

	/**
	 * Determines whether the specified item is new or not.
	 * @param item an item to check
	 * @return {@code true}, if the item is new and has not been persisted yet; {@code false}, if the item already exists in the
	 * persistent storage.
	 */
	protected boolean isNew(final ItemModel item)
	{
		return getModelService().isNew(item);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService service)
	{
		modelService = service;
	}
}
