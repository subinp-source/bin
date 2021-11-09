/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cart.action;

/**
 * Registry for cart entry action handlers.
 */
public interface CartEntryActionHandlerRegistry
{
	/**
	 * Returns the configured handler for the given action type.
	 * 
	 * @param action
	 *           the action to get the handler implementation for
	 * @return the matching handler for the goven action
	 */
	CartEntryActionHandler getHandler(CartEntryAction action);
}
