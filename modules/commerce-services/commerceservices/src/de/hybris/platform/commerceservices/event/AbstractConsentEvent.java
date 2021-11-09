/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.event;

import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * Abstract Consent event, implementation of {@link AbstractCommerceUserEvent}
 */
public class AbstractConsentEvent extends AbstractEvent
{

	private ConsentModel consent;

	/**
	 * @return the consent
	 */
	public ConsentModel getConsent()
	{
		return consent;
	}

	/**
	 * @param consent
	 *           the consent to set
	 */
	public void setConsent(final ConsentModel consent)
	{
		this.consent = consent;
	}
}
