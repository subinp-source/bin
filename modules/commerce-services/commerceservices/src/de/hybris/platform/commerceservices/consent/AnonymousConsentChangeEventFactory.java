/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.consent;

import de.hybris.platform.commerceservices.event.AnonymousConsentChangeEvent;

import java.util.Map;


/**
 * Factory for AnonymousConsentChangeEvent
 */
public interface AnonymousConsentChangeEventFactory
{
	AnonymousConsentChangeEvent buildEvent(String templateCode, String previousState, String currentState,
			final Map<String, String> consents);
}
