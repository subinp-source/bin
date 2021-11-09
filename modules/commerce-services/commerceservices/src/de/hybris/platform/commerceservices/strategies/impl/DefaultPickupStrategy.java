/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.commerceservices.enums.PickupInStoreMode;
import de.hybris.platform.commerceservices.strategies.PickupStrategy;
import de.hybris.platform.store.services.BaseStoreService;

import org.springframework.beans.factory.annotation.Required;


public class DefaultPickupStrategy implements PickupStrategy
{
	private BaseStoreService baseStoreService;
	private PickupInStoreMode defaultPickupInStoreMode = PickupInStoreMode.DISABLED;

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected PickupInStoreMode getDefaultPickupInStoreMode()
	{
		return defaultPickupInStoreMode;
	}

	// Optional
	public void setDefaultPickupInStoreMode(final PickupInStoreMode defaultPickupInStoreMode)
	{
		this.defaultPickupInStoreMode = defaultPickupInStoreMode;
	}

	@Override
	public PickupInStoreMode getPickupInStoreMode()
	{
		if (getBaseStoreService().getCurrentBaseStore() != null
				&& getBaseStoreService().getCurrentBaseStore().getPickupInStoreMode() != null)
		{
			return getBaseStoreService().getCurrentBaseStore().getPickupInStoreMode();
		}
		else
		{
			return getDefaultPickupInStoreMode();
		}
	}

}
