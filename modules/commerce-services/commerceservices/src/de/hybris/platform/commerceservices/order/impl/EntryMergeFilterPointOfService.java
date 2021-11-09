/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.EntryMergeFilter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.Objects;
import javax.annotation.Nonnull;


/**
 * Merge only entries that have the same point of service.
 */
public class EntryMergeFilterPointOfService implements EntryMergeFilter
{
	@Override
	public Boolean apply(@Nonnull final AbstractOrderEntryModel candidate, @Nonnull final AbstractOrderEntryModel target)
	{
		return Boolean.valueOf(Objects.equals(candidate.getDeliveryPointOfService(), target.getDeliveryPointOfService()));
	}
}


