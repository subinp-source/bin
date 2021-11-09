/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.model;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Validation service for usage charge.
 */
public interface UsageChargeValidationService
{
	/**
	 * Validates given usage charge models.
	 * 
	 * @param usageCharges
	 *           Usage charge models to be validated
	 * @return Validation messages
	 */
	@Nonnull
	Collection<String> validate(@Nullable Collection<UsageChargeModel> usageCharges);
}
