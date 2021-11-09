/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.EntryMergeFilter;
import de.hybris.platform.commerceservices.strategies.ModifiableChecker;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Required;


/**
 * Do not merge entries that are not updatable.
 */
public class EntryMergeFilterIsEntryUpdatable implements EntryMergeFilter
{
	private ModifiableChecker<AbstractOrderEntryModel> entryOrderChecker;

	@Override
	public Boolean apply(@Nonnull final AbstractOrderEntryModel candidate, @Nonnull final AbstractOrderEntryModel target)
	{
		return Boolean.valueOf(getEntryOrderChecker().canModify(target));
	}

	protected ModifiableChecker<AbstractOrderEntryModel> getEntryOrderChecker()
	{
		return entryOrderChecker;
	}

	@Required
	public void setEntryOrderChecker(final ModifiableChecker<AbstractOrderEntryModel> entryOrderChecker)
	{
		this.entryOrderChecker = entryOrderChecker;
	}
}
