/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.consent;

import java.util.Map;


/**
 * Provides additional data for AnonymousConsentChangeEvent
 */
public interface AnonymousConsentChangeEventDataProvider
{
	/**
	 * Data to add to AnonymousConsentChangeEvent
	 *
	 * @return data to add
	 */
	Map<String, String> getData();
}
