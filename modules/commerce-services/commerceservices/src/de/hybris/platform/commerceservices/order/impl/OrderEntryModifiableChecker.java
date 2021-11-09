/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.strategies.ModifiableChecker;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import org.apache.commons.lang.BooleanUtils;

public class OrderEntryModifiableChecker implements ModifiableChecker<AbstractOrderEntryModel>
{
	@Override
	public boolean canModify(final AbstractOrderEntryModel given)
	{
		return BooleanUtils.isNotTrue(given.getGiveAway());
	}
}
