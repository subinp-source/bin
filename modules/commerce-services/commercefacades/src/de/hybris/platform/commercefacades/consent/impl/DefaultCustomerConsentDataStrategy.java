/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.consent.impl;

import de.hybris.platform.commercefacades.consent.CustomerConsentDataStrategy;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.commercefacades.consent.data.ConsentTemplateData;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.CONSENT_GIVEN;
import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.CONSENT_WITHDRAWN;
import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.USER_CONSENTS;


/**
 * Default implementation of @{@link CustomerConsentDataStrategy}
 */
public class DefaultCustomerConsentDataStrategy implements CustomerConsentDataStrategy
{
	private SessionService sessionService;
	private ConsentFacade consentFacade;

	@Override
	public void populateCustomerConsentDataInSession()
	{
		final Stream<ConsentTemplateData> activeConsentData = getConsentFacade().getConsentTemplatesWithConsents().stream()
				.filter(ConsentTemplateData::isExposed);

		final Map<String, String> consentsMap = activeConsentData
				.collect(Collectors.toMap(ConsentTemplateData::getId, this::getConsentState));

		getSessionService().setAttribute(USER_CONSENTS, consentsMap);
	}

	protected String getConsentState(final ConsentTemplateData consent)
	{
		return consent.getConsentData() == null || consent.getConsentData().getConsentWithdrawnDate() != null ?
				CONSENT_WITHDRAWN :
				CONSENT_GIVEN;
	}

	protected ConsentFacade getConsentFacade()
	{
		return consentFacade;
	}

	@Required
	public void setConsentFacade(final ConsentFacade consentFacade)
	{
		this.consentFacade = consentFacade;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
