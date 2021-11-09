/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;


import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;


/**
 * A strategy for validating the cart
 */
public interface CartValidationStrategy
{
	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #validateCart(de.hybris.platform.commerceservices.service.data.CommerceCartParameter)} instead.
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	List<CommerceCartModification> validateCart(CartModel cartModel);

	List<CommerceCartModification> validateCart(final CommerceCartParameter parameter);

}
