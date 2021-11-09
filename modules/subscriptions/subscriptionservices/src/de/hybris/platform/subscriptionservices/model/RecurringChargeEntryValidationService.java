/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.model;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Validation service for recurring charge entry.
 */
public interface RecurringChargeEntryValidationService
{
	/**
	 * Validates the given collection of {@link RecurringChargeEntryModel} instances.
	 * 
	 * @param recurringChargeEntries
	 *           the recurring charge entries to validate
	 * @return a collection of validation messages
	 */
	@Nonnull
	Collection<String> validate(@Nullable Collection<RecurringChargeEntryModel> recurringChargeEntries);
}
