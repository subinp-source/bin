/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchbackoffice.common;

import java.util.List;
import java.util.Map;


/**
 * Provides data to be used in widgets, editors or actions.
 *
 * @param <D>
 *           - The type of the data
 * @param <V>
 *           - The type of the value
 */
public interface SnDataProvider<D, V>
{
	/**
	 * Returns the data.
	 *
	 * @param parameters
	 *           - the parameters
	 *
	 * @return the data
	 */
	List<D> getData(Map<String, Object> parameters);

	/**
	 * Returns the value for a data object.
	 *
	 * @param data
	 *           - the data object
	 * @param parameters
	 *           - the parameters
	 *
	 * @return the value object
	 */
	V getValue(D data);

	/**
	 * Returns the label for a data object.
	 *
	 * @param data
	 *           - the data object
	 * @param parameters
	 *           - the parameters
	 *
	 * @return the label
	 */
	String getLabel(D data);
}
