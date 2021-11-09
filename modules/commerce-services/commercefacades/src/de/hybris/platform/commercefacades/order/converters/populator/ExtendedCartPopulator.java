/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;


/**
 * Converter for {@link de.hybris.platform.commercefacades.order.data.CartData}, that populates additionally :<br>
 * 
 * <ul>
 * <li>delivery address</li>
 * <li>payment info</li>
 * <li>delivery mode</li>
 * </ul>
 * 
 *
 */
public class ExtendedCartPopulator extends CartPopulator
{

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		super.populate(source, target);
		addDeliveryAddress(source, target);
		addPaymentInformation(source, target);
		addDeliveryMethod(source, target);
		addPrincipalInformation(source, target);
	}
}
