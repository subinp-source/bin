/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.daos.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.configurablebundleservices.daos.OrderEntryDao;


/**
 * Default implementation of the {@link OrderEntryDao} for sub-type {@link CartEntryModel}
 * 
 * @deprecated since 1905
 */
@Deprecated(since = "1905", forRemoval = true)
public class DefaultCartEntryDao extends AbstractOrderEntryDao<CartModel, CartEntryModel>
{
	@Override
	public PK getItemType()
	{
		final TypeModel typeModel = getTypeService().getTypeForCode(CartEntryModel._TYPECODE);
		return typeModel.getPk();
	}
}
