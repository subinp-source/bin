/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.validation;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;

/**
 * A validator for {@link ItemModel}s that is requested for persistence, which ensures certain conditions
 * are met before saving the {@link ItemModel}. If conditions are not met the validator can throw an
 * instance of {@link RuntimeException} and to veto the search.
 */
public interface ItemPersistRequestValidator
{
	/**
	 * Validates the item can be persisted with the given context and rejects it by throwing an exception,
	 * if the specified request does not meet certain criteria.
	 *
	 * @param context   a persistence context containing information about the item being persisted.
	 * @param itemModel an item to be persisted
	 * @throws RuntimeException if request is not valid and cannot be corrected.
	 */
	void validate(PersistenceContext context, ItemModel itemModel);
}
