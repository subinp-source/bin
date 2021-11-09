/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartfacades.strategies;


/**
 * Updates selective cart when when displaying the cart page
 */
public interface SelectiveCartUpdateStrategy
{

	/**
	 * Orders and regroups cart data entries
	 */
	void update();
}