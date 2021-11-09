/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.process.strategies.impl;

import de.hybris.platform.acceleratorservices.process.strategies.impl.DefaultProcessContextResolutionStrategy;
import de.hybris.platform.b2bacceleratorservices.model.process.ReplenishmentProcessModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;


/**
 * @deprecated since 6.4, use the {@link B2BAcceleratorProcessContextStrategy} instead
 */
@Deprecated(since = "6.4", forRemoval = true)
public class B2BAcceleratorProcessContextResolutionStrategy extends DefaultProcessContextResolutionStrategy
{
	@Override
	public BaseSiteModel getCmsSite(final BusinessProcessModel businessProcessModel)
	{
		if (businessProcessModel instanceof ReplenishmentProcessModel)
		{
			return ((ReplenishmentProcessModel) businessProcessModel).getCartToOrderCronJob().getCart().getSite();
		}

		return super.getCmsSite(businessProcessModel);
	}
}
