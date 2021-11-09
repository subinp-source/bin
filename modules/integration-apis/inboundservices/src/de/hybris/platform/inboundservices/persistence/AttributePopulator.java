/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence;

import de.hybris.platform.core.model.ItemModel;

/**
 * Populates attributes of a given {@code ItemModel}.
 */
public interface AttributePopulator
{
	/**
	 * Populates attributes of the specified {@code ItemModel}.
	 * @param model a model to populate attributes in.
	 * @param context a context carrying all information needed to know, which attributes need to be populated and how they should
	 * be populated by specific implementations of this attribute populator.
	 */
	void populate(ItemModel model, PersistenceContext context);
}
