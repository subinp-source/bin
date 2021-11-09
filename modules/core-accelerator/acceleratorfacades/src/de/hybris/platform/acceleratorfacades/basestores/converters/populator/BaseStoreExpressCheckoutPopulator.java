/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.basestores.converters.populator;

import de.hybris.platform.commercefacades.basestore.data.BaseStoreData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.store.BaseStoreModel;


/**
 * Populates express checkout value in {@link BaseStoreData} from {@link BaseStoreModel}
 */
public class BaseStoreExpressCheckoutPopulator implements Populator<BaseStoreModel, BaseStoreData>
{

	@Override
	public void populate(final BaseStoreModel source, final BaseStoreData target)
	{
		if (source != null && target != null)
		{
			target.setExpressCheckoutEnabled(source.getExpressCheckoutEnabled());
		}
	}
}
