/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.context;
import java.util.Enumeration;

import com.hybris.merchandising.model.ContextMap;



/**
 * Interface for ContextRepository to manage ContextMap.
 *
 */
public interface ContextRepository
{

	/**
	 * Return a single ContextMap object for the given name
	 *
	 * @param name
	 * @return
	 */
	ContextMap get(String name);

	/**
	 * Add a single ContextMap object to the context store
	 *
	 * @param name
	 * @param context
	 */
	void put(String name, ContextMap context);

	/**
	 * Clear out the entire context store
	 */
	void clear();

	/**
	 * return the size of the current context store
	 *
	 * @return
	 */
	int size();

	/**
	 * Returns an enumeration of all keys.
	 *
	 * @return
	 */
	Enumeration<String> keys();
}
