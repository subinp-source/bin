/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.organization.strategies;

import de.hybris.platform.commerceservices.model.OrgUnitModel;


/**
 * Strategy for activating and deactivating an {@link OrgUnitModel}.
 */
public interface OrgUnitActivationStrategy<T extends OrgUnitModel>
{
	/**
	 * Active the given unit.
	 *
	 * @param unit
	 *           the {@link OrgUnitModel} to activate
	 */
	void activateUnit(T unit);

	/**
	 * Deactivate the given unit and all of its child units.
	 *
	 * @param unit
	 *           the {@link OrgUnitModel} to deactivate
	 */
	void deactivateUnit(T unit);
}
