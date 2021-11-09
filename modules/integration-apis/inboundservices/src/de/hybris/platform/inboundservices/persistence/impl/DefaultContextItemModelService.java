/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.ContextItemModelService;
import de.hybris.platform.inboundservices.persistence.ItemModelFactory;
import de.hybris.platform.inboundservices.persistence.ItemModelPopulator;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.populator.ItemCreationException;
import de.hybris.platform.inboundservices.persistence.validation.ItemPersistRequestValidator;
import de.hybris.platform.inboundservices.persistence.validation.PersistenceContextValidator;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;
import de.hybris.platform.integrationservices.search.ItemSearchService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.exceptions.ModelCreationException;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation for {@link ContextItemModelService}
 */
public class DefaultContextItemModelService implements ContextItemModelService
{
	private static final Logger LOG = Log.getLogger(DefaultContextItemModelService.class);
	private ItemSearchService searchService;
	private ItemModelFactory itemFactory;
	private ItemModelPopulator itemModelPopulator;
	private List<PersistenceContextValidator> persistenceContextValidators = Collections.emptyList();
	private List<ItemPersistRequestValidator> itemPersistRequestValidators = Collections.emptyList();

	@Override
	public ItemModel findOrCreateItem(final PersistenceContext context)
	{
		final ItemModel item = context.getContextItem()
		                              .orElseGet(() -> getItem(context));
		if (item != null)
		{
			LOG.trace("Converting IntegrationItem To ItemModel: {}", context.getIntegrationItem());
			populateItem(context, item);
		}
		return item;
	}

	private void populateItem(final PersistenceContext context, final ItemModel item)
	{
		itemModelPopulator.populate(item, context);
		itemPersistRequestValidators.forEach(validator -> validator.validate(context, item));
	}

	private ItemModel getItem(final PersistenceContext context)
	{
		ItemModel item = lookup(context.toItemSearchRequest());
		if (item == null && context.isItemCanBeCreated())
		{
			item = createItem(context);
		}
		context.putItem(item);
		return item;
	}

	private ItemModel lookup(final ItemSearchRequest searchRequest)
	{
		return searchService.findUniqueItem(searchRequest).orElse(null);
	}

	private ItemModel createItem(final PersistenceContext context)
	{
		persistenceContextValidators.forEach(v -> v.validate(context));

		final String entityName = context.getIntegrationItem().getItemType().getItemCode();
		LOG.trace("Item '{}' -> '{}' does not exist, trying to create a new one.", entityName, context.getIntegrationItem());
		try
		{
			return this.itemFactory.createItem(context);
		}
		catch (final ModelInitializationException | ModelCreationException e)
		{
			throw new ItemCreationException(e, context.getIntegrationItem().getItemType());
		}
	}

	public void setPersistenceContextValidators(final List<PersistenceContextValidator> persistenceContextValidators)
	{
		this.persistenceContextValidators = persistenceContextValidators;
	}

	public void setItemPersistRequestValidators(final List<ItemPersistRequestValidator> itemPersistRequestValidators)
	{
		this.itemPersistRequestValidators = itemPersistRequestValidators;
	}

	@Required
	public void setSearchService(final ItemSearchService searchService)
	{
		this.searchService = searchService;
	}

	@Required
	public void setItemFactory(final ItemModelFactory itemFactory)
	{
		this.itemFactory = itemFactory;
	}

	@Required
	public void setItemModelPopulator(final ItemModelPopulator itemModelPopulator)
	{
		this.itemModelPopulator = itemModelPopulator;
	}
}
