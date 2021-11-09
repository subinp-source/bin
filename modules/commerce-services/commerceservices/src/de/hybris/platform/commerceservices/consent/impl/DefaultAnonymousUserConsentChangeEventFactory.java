/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.consent.impl;

import de.hybris.platform.commerceservices.consent.AnonymousConsentChangeEventDataProvider;
import de.hybris.platform.commerceservices.consent.AnonymousConsentChangeEventFactory;
import de.hybris.platform.commerceservices.event.AnonymousConsentChangeEvent;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class DefaultAnonymousUserConsentChangeEventFactory implements AnonymousConsentChangeEventFactory
{
	List<AnonymousConsentChangeEventDataProvider> providers = Collections.emptyList();

	@Override
	public AnonymousConsentChangeEvent buildEvent(final String templateCode, final String previousState, final String currentState,
			final Map<String, String> consents)
	{
		final AnonymousConsentChangeEvent event = new AnonymousConsentChangeEvent(templateCode, previousState, currentState,
				consents);

		providers.forEach(p -> event.addData(p.getData()));

		return event;
	}

	public void setProviders(final List<AnonymousConsentChangeEventDataProvider> providers)
	{
		this.providers = providers;
	}
}
