/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service;

import de.hybris.platform.searchservices.suggest.SnSuggestException;

/**
 * Listener for suggest operations.
 */
public interface SnSuggestListener
{
	/**
	 * Handles a notification that the suggest operation is about to begin execution.
	 *
	 * @param context
	 *           - the suggest context
	 *
	 * @throws SnSuggestException
	 *            if an error occurs
	 */
	void beforeSuggest(SnSuggestContext context) throws SnSuggestException;

	/**
	 * Handles a notification that the suggest operation has just completed.
	 *
	 * @param context
	 *           - the suggest context
	 *
	 * @throws SnSuggestException
	 *            if an error occurs
	 */
	void afterSuggest(SnSuggestContext context) throws SnSuggestException;

	/**
	 * Handles a notification that the suggest operation failed (this may also be due to listeners failing).
	 *
	 * @param context
	 *           - the suggest context
	 *
	 * @throws SnSuggestException
	 *            if an error occurs
	 */
	void afterSuggestError(SnSuggestContext context) throws SnSuggestException;
}
