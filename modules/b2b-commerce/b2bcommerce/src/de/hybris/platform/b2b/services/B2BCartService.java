/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.services;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;


/**
 * A specialized Cart Service extending functionality of {@link de.hybris.platform.order.impl.DefaultCartService}
 * to address additional needs of a b2b cart functionality
 * 
 * @spring.bean b2bCartService
 */
public interface B2BCartService extends CartService
{
	/**
	 * Clones an order model into cart model by delegating to
	 * {@link CartService#clone(de.hybris.platform.core.model.type.ComposedTypeModel, de.hybris.platform.core.model.type.ComposedTypeModel, de.hybris.platform.core.model.order.AbstractOrderModel, String)}
	 * 
	 * @param order
	 *           An original order model to clone into cart
	 * @return not persisted cart model instance.
	 */
	CartModel createCartFromAbstractOrder(final AbstractOrderModel order);
}
