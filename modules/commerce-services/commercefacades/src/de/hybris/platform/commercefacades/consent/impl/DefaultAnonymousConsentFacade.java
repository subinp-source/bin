/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.consent.impl;

import de.hybris.platform.commercefacades.consent.AnonymousConsentFacade;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.commercefacades.consent.data.AnonymousConsentData;
import de.hybris.platform.commercefacades.consent.data.ConsentTemplateData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.consent.AnonymousConsentChangeEventFactory;
import de.hybris.platform.commerceservices.event.AnonymousConsentChangeEvent;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.CONSENT_TEMPLATES;
import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.PREVIOUS_CONSENT_LANGUAGE;
import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.USER_CONSENTS;


/**
 * Default implementation of {@link AnonymousConsentFacade}.
 */
public class DefaultAnonymousConsentFacade implements AnonymousConsentFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultAnonymousConsentFacade.class);

	private ConsentFacade consentFacade;
	private SessionService sessionService;
	private UserFacade userFacade;
	private StoreSessionFacade storeSessionFacade;
	private EventService eventService;
	private AnonymousConsentChangeEventFactory anonymousConsentChangeEventFactory;

	@Override
	public void synchronizeAnonymousConsents(final Supplier<List<AnonymousConsentData>> anonymousConsentReader,
			final Consumer<List<AnonymousConsentData>> anonymousConsentWriter)
	{
		if (!userFacade.isAnonymousUser())
		{
			return;
		}

		checkLanguageChange();

		// Synchronize anonymous consents by filtering old templates and adding new ones
		final List<AnonymousConsentData> anonymousConsents = new ArrayList<>(anonymousConsentReader.get());
		final List<AnonymousConsentData> updatedAnonymousConsents = updateAnonymousConsents(anonymousConsents);

		// Update client anonymous consents
		anonymousConsentWriter.accept(updatedAnonymousConsents);
		updateAnonymousConsentsInSession(updatedAnonymousConsents);
	}

	/**
	 * Erase CONSENT_TEMPLATES from session on language change
	 */
	protected void checkLanguageChange()
	{
		final String currentLanguage = storeSessionFacade.getCurrentLanguage().getIsocode();
		final String previousLanguage = sessionService.getAttribute(PREVIOUS_CONSENT_LANGUAGE);
		if (StringUtils.isEmpty(previousLanguage) || !currentLanguage.equals(previousLanguage))
		{
			sessionService.removeAttribute(CONSENT_TEMPLATES);
			sessionService.setAttribute(PREVIOUS_CONSENT_LANGUAGE, currentLanguage);
		}
	}

	protected List<AnonymousConsentData> updateAnonymousConsents(final List<AnonymousConsentData> anonymousConsents)
	{
		final List<ConsentTemplateData> consentTemplates = getConsentTemplates();

		// Remove stale
		final List<AnonymousConsentData> anonymousConsentsToRemove = new ArrayList<>();
		for (final AnonymousConsentData anonymousConsent : anonymousConsents)
		{
			final Optional<ConsentTemplateData> templateData = getConsentTemplateById(consentTemplates,
					anonymousConsent.getTemplateCode());

			if (templateData.isEmpty() || !templateData.get().getVersion()
					.equals(Integer.valueOf(anonymousConsent.getTemplateVersion())))
			{
				anonymousConsentsToRemove.add(anonymousConsent);
			}
		}
		anonymousConsents.removeAll(anonymousConsentsToRemove);

		// Add new
		final List<String> anonymousConsentCodes = anonymousConsents.stream() //
				.map(AnonymousConsentData::getTemplateCode) //
				.collect(Collectors.toList());
		for (final ConsentTemplateData consentTemplate : consentTemplates)
		{
			if (!anonymousConsentCodes.contains(consentTemplate.getId()))
			{
				final AnonymousConsentData anonymousConsent = populateAnonymousConsentFromTemplate(consentTemplate);
				anonymousConsents.add(anonymousConsent);
			}
		}
		return anonymousConsents;
	}

	protected Optional<ConsentTemplateData> getConsentTemplateById(final List<ConsentTemplateData> consentTemplates,
			final String id)
	{
		return consentTemplates.stream().filter(template -> id.equals(template.getId())).findFirst();
	}

	protected AnonymousConsentData populateAnonymousConsentFromTemplate(final ConsentTemplateData template)
	{
		final AnonymousConsentData anonymousConsent = new AnonymousConsentData();
		anonymousConsent.setTemplateCode(template.getId());
		anonymousConsent.setTemplateVersion(template.getVersion().intValue());
		return anonymousConsent;
	}

	protected List<ConsentTemplateData> getConsentTemplates()
	{
		List<ConsentTemplateData> consentTemplates = sessionService.getAttribute(CONSENT_TEMPLATES);
		if (consentTemplates == null)
		{
			consentTemplates = consentFacade.getConsentTemplatesWithConsents().stream()//
					.filter(ConsentTemplateData::isExposed)//
					.collect(Collectors.toList());
			sessionService.setAttribute(CONSENT_TEMPLATES, consentTemplates);
		}
		return consentTemplates;
	}

	protected void updateAnonymousConsentsInSession(final List<AnonymousConsentData> anonymousConsents)
	{
		final Map<String, String> previousConsentMap = sessionService.getAttribute(USER_CONSENTS);
		final Map<String, String> currentConsentMap = populateAnonymousConsentsIntoSession(anonymousConsents);
		if (previousConsentMap != null && currentConsentMap != null)
		{
			for (final Map.Entry<String, String> template : currentConsentMap.entrySet())
			{
				final String previousValue = previousConsentMap.get(template.getKey());
				final String currentValue = template.getValue();
				//send event if previous consent value is different than current one
				if (!Objects.equals(currentValue, previousValue))
				{
					publishConsentEvent(template.getKey(), previousValue, currentValue, currentConsentMap);
				}
			}
		}
	}

	protected void publishConsentEvent(final String template, final String previousValue, final String currentValue,
			final Map<String, String> currentConsentMap)
	{
		try
		{
			final AnonymousConsentChangeEvent event = anonymousConsentChangeEventFactory
					.buildEvent(template, previousValue, currentValue, currentConsentMap);
			eventService.publishEvent(event);
		}
		catch (final RuntimeException e)
		{
			LOG.warn("Event publishing failed for anonymous user consent change", e);
		}
	}

	protected Map<String, String> populateAnonymousConsentsIntoSession(final List<AnonymousConsentData> anonymousConsents)
	{
		final Map<String, String> consentsMap = new HashMap<>();
		for (final AnonymousConsentData anonymousConsent : anonymousConsents)
		{
			consentsMap.put(anonymousConsent.getTemplateCode(), anonymousConsent.getConsentState());
		}
		sessionService.setAttribute(USER_CONSENTS, consentsMap);
		return consentsMap;
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

	protected StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	@Required
	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

	protected UserFacade getUserFacade()
	{
		return userFacade;
	}

	@Required
	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	protected AnonymousConsentChangeEventFactory getAnonymousConsentChangeEventFactory()
	{
		return anonymousConsentChangeEventFactory;
	}

	@Required
	public void setAnonymousConsentChangeEventFactory(final AnonymousConsentChangeEventFactory anonymousConsentChangeEventFactory)
	{
		this.anonymousConsentChangeEventFactory = anonymousConsentChangeEventFactory;
	}
}
