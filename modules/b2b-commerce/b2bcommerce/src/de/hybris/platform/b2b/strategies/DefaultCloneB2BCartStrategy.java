/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies;

import de.hybris.platform.order.AbstractOrderEntryTypeService;
import de.hybris.platform.order.strategies.ordercloning.impl.DefaultCloneAbstractOrderStrategy;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator;
import de.hybris.platform.servicelayer.type.TypeService;

public class DefaultCloneB2BCartStrategy extends DefaultCloneAbstractOrderStrategy {


	public DefaultCloneB2BCartStrategy(final TypeService typeService, final ItemModelCloneCreator
			itemModelCloneCreator,
			final AbstractOrderEntryTypeService abstractOrderEntryTypeService) {
		super(typeService, itemModelCloneCreator, abstractOrderEntryTypeService);
	}
}
