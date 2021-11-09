/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.model;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Validation service for one time charge entries.
 */
public interface OneTimeChargeEntryValidationService
{
	/**
	 * Validates the given collection of {@link OneTimeChargeEntryModel} instances.
	 *
	 * @param oneTimeChargeEntries
	 *           the one time charge entries to validate
	 * @return a collection of validation messages
	 */
	@Nonnull
	Collection<String> validate(@Nullable Collection<OneTimeChargeEntryModel> oneTimeChargeEntries);
}
