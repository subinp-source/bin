/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;

import java.util.List;


/**
 * Implementations of this interface are responsible for resolving and creating instances of listeners.
 */
public interface SnListenerFactory
{
	/**
	 * Returns instances of listeners for a specific type.
	 *
	 * @param context
	 *           - the context
	 * @param listenerType
	 *           - the type of the listeners
	 *
	 * @return the listeners
	 */
	<T> List<T> getListeners(SnContext context, Class<T> listenerType);
}
