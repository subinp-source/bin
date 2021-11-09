/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.selectivecartservices.order;

import de.hybris.platform.core.model.order.CartModel;


/**
 * Selective cart factory that creates selective cart of {@link CartModel} type.
 */
public interface SelectiveCartFactory
{
	/**
	 * Creates the invisible cart model, but not saves it.
	 * 
	 * @return the CartModel
	 */
	CartModel createSelectiveCartModel();
}
