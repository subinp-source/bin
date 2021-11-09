/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.consent;

import de.hybris.platform.commercefacades.consent.data.AnonymousConsentData;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * Facade interface providing an API for performing operations on consents of anonymous users.
 */
public interface AnonymousConsentFacade
{
	/**
	 * Synchronizes the supplied list of anonymous consents with its current state and forwards the resulted list.
	 *
	 * @param anonymousConsentReader
	 * 		anonymous consent reader to supply the list of anonymous consents to synchronize
	 * @param anonymousConsentWriter
	 * 		anonymous consent writer to consume the result of synchronization
	 */
	void synchronizeAnonymousConsents(Supplier<List<AnonymousConsentData>> anonymousConsentReader,
			Consumer<List<AnonymousConsentData>> anonymousConsentWriter);
}
