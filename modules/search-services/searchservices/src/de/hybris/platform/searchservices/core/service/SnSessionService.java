/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;

/**
 * Service for session related functionality.
 * <p>
 * Code creating/destroying session contexts should normally use the following pattern:
 *
 * <pre>
 * try
 * {
 * 	snSessionService.initializeSession(...);
 *
 * 	// put your logic here
 * }
 * finally
 * {
 * 	snSessionService.destroySession();
 * }
 * </pre>
 */
public interface SnSessionService
{
	/**
	 * Initializes a new local session.
	 */
	void initializeSession();

	/**
	 * Initializes a new local session for the given context, it uses session attributes from the context (e.g.: user,
	 * catalog versions).
	 *
	 * @param context
	 *           - the context
	 */
	void initializeSessionForContext(SnContext context);

	/**
	 * Destroys the current local session.
	 */
	void destroySession();

	/**
	 * Enables search restrictions in current session.
	 */
	void enableSearchRestrictions();

	/**
	 * Disables search restrictions in current session.
	 */
	void disableSearchRestrictions();
}
