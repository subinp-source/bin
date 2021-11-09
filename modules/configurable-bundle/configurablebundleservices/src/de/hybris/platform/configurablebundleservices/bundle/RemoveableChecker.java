/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import javax.annotation.Nonnull;


/**
 * Checks if T can be removed from the {@link AbstractOrderModel}
 */
public interface RemoveableChecker<T extends AbstractOrderEntryModel>
{
	/**
	 * Test if the {@link AbstractOrderEntryModel} can be removed from the {@link AbstractOrderModel}
	 * 
	 * @param given
	 *           {@link AbstractOrderEntryModel} that is tested
	 * @return <code>true</code> if it can be removed. Else <code>false</code>
	 */
	boolean canRemove(@Nonnull T given);
}
