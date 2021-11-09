/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * Captures all parameters related to persistence of an item data.
 * While persisting a complex item with nested item(s), a context is created for each item being persisted.
 */
public interface PersistenceContext
{
	/**
	 * Retrieves item data to be persisted.
	 *
	 * @return item data to be persisted into an {@link ItemModel}.
	 */
	@Nonnull
	IntegrationItem getIntegrationItem();

	/**
	 * Retrieves the persistence context for an item referenced by the specified attribute
	 *
	 * @param attribute specifies referenced item
	 * @return the context containing information about the referenced item
	 */
	PersistenceContext getReferencedContext(TypeAttributeDescriptor attribute);

	/**
	 * Retrieves the persistence contexts for the items referenced by the specified attribute
	 *
	 * @param attribute specifies referenced items
	 * @return the context containing information about the referenced items
	 */
	Collection<PersistenceContext> getReferencedContexts(TypeAttributeDescriptor attribute);

	/**
	 * Retrieves the persistence context that the referenced context was gotten from
	 *
	 * @return parent context, from which this context was retrieved. If this context is the root item context, Optional.empty is returned
	 */
	Optional<PersistenceContext> getSourceContext();

	/**
	 * Determines whether the payload item that can be retrieved by {@link #getIntegrationItem()} method has been persisted
	 * already in the course of the root persistent context processing and retrieves the persisted item from the context. This
	 * is the case when a nested item refers back to another item present earlier on the payload graph. For example, an Order
	 * contains OrderEntries and OrderEntry refers back to its container Order. The already persisted item match should be done
	 * by the item type and its key value. Only when those two characteristics match, the context item was already persisted and
	 * can be retrieved by this method.
	 *
	 * @return an item, if this persistent context represents an already persisted payload item from higher levels of the root
	 * item graph or an empty {@code Optional}, if the item was not persisted yet.
	 * @see #getIntegrationItem()
	 * @see #putItem(ItemModel)
	 */
	Optional<ItemModel> getContextItem();

	/**
	 * Adds the item model corresponding to the payload, i.e. {@link #getIntegrationItem()}, into this context for being persisted.
	 * Every single payload item that is converted to an {@code ItemModel} must be put into this context explicitly.
	 */
	void putItem(ItemModel item);

	/**
	 * Retrieves the top most persistence context
	 *
	 * @return the root context
	 */
	@Nonnull
	PersistenceContext getRootContext();

	/**
	 * Creates a {@link ItemSearchRequest} from this {@link PersistenceContext}
	 *
	 * @return a newly constructed ItemSearchRequest
	 */
	@Nonnull
	ItemSearchRequest toItemSearchRequest();

	/**
	 * Indicates the persistence is to replace the item attributes with what's provided in the {@link IntegrationItem}.
	 * This is primarily applicable to collections. The default behavior is to append to the collection.
	 * With this method being true, the item's collection attribute will be replaced instead of appended.
	 *
	 * @return true means to replace attributes, otherwise false.
	 */
	default boolean isReplaceAttributes()
	{
		return false;
	}

	/**
	 * Determines whether a new item model can be created for the context payload.
	 *
	 * @return {@code true}, when new item can be create, if an item matching the context was not found in the persistent storage;
	 * {@code false}, if the context implies update only and therefore the item should not be created, if it does not exist yet.
	 */
	boolean isItemCanBeCreated();

	/**
	 * Indicates locale, in which data must be returned in the response to the persistence request.
	 * @return locale to be used for localized attribute values in the response. The locale should be always
	 * explicitly specified in this context, so that clients would not need to duplicate logic for determining
	 * a default value to be used for returned data.
	 */
	@Nonnull
	Locale getAcceptLocale();

	/**
	 * Indicates the language, in which localized attributes content is provided.
	 *
	 * @return content locale that must be always explicitly specified in this context, so that clients would not need to duplicate
	 * logic for determining a locale for the localized attributes in the payload.
	 */
	@Nonnull
	default Locale getContentLocale()
	{
		return Locale.getDefault();
	}
}
