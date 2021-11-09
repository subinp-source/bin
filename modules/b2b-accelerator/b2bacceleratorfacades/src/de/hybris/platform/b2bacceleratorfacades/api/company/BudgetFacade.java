/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.api.company;

import de.hybris.platform.b2bcommercefacades.company.B2BBudgetFacade;


/**
 * Facade for managing budgets.
 *
 * @deprecated Since 6.0. Use {@link B2BBudgetFacade} instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface BudgetFacade extends B2BBudgetFacade
{
	// Keep interface for backwards compatibility
}
