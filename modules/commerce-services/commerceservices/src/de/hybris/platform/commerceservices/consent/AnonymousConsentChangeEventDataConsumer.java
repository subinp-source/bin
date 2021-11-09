/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.consent;

import de.hybris.platform.commerceservices.event.AnonymousConsentChangeEvent;

import java.util.Map;


/**
 * Interface for consuming data created by {@link AnonymousConsentChangeEventDataProvider}
 */
public interface AnonymousConsentChangeEventDataConsumer
{
	/**
	 * Consumes data form {@link AnonymousConsentChangeEvent}
	 * 
	 * @param data
	 *           data to consume
	 */
	void process(Map<String, String> data);

}
