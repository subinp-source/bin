/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.validators;

import de.hybris.platform.commerceservices.strategies.DeliveryAddressesLookupStrategy;
import de.hybris.platform.order.CartService;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 *
 */
public class B2BCartAddressValidator implements Validator
{
	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "b2bDeliveryAddressesLookupStrategy")
	private DeliveryAddressesLookupStrategy b2bDeliveryAddressesLookupStrategy;


	@Override
	public boolean supports(final Class<?> arg0)
	{
		return String.class.isAssignableFrom(arg0);
	}


	@Override
	public void validate(final Object obj, final Errors error)
	{
		final String addressId = (String) obj;
		if (StringUtils.isBlank(addressId) || CollectionUtils
				.isEmpty(b2bDeliveryAddressesLookupStrategy.getDeliveryAddressesForOrder(cartService.getSessionCart(), true)))
		{
			error.reject("cart.deliveryAddressInvalid");
		}
	}

}
