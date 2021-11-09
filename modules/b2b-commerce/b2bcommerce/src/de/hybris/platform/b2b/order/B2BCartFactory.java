/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.order;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartFactory;

public interface B2BCartFactory extends CartFactory {
	boolean isB2BCart(CartModel cart);
}
