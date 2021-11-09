/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.consent.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.commercefacades.consent.data.AnonymousConsentData;
import de.hybris.platform.commercefacades.consent.data.ConsentData;
import de.hybris.platform.commercefacades.consent.data.ConsentTemplateData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.consent.AnonymousConsentChangeEventFactory;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.CONSENT_GIVEN;
import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.CONSENT_TEMPLATES;
import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.CONSENT_WITHDRAWN;
import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.PREVIOUS_CONSENT_LANGUAGE;
import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.USER_CONSENTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultAnonymousConsentFacadeTest
{
	public static final String PREVIOUS_CONSENT_LANGUAGE_ISO = "de";
	public static final String TEMPLATE_CODE_GIVEN = "templateCodeGiven";
	public static final String TEMPLATE_CODE_WITHDRAWN = "templateCodeWithdrawn";

	@InjectMocks
	private DefaultAnonymousConsentFacade anonymousConsentFacade;

	@Mock
	private ConsentFacade consentFacade;
	@Mock
	private SessionService sessionService;
	@Mock
	private UserFacade userFacade;
	@Mock
	private StoreSessionFacade storeSessionFacade;
	@Mock
	private EventService eventService;
	@Mock
	private AnonymousConsentChangeEventFactory anonymousConsentChangeEventFactory;
	@Mock
	private Supplier<List<AnonymousConsentData>> anonymousConsentReader;
	@Mock
	private Consumer<List<AnonymousConsentData>> anonymousConsentWriter;

	@Captor
	private ArgumentCaptor<List<AnonymousConsentData>> anonymousConsentCaptor;
	@Captor
	private ArgumentCaptor<Map<String, String>> sessionConsentCaptor;

	private List<AnonymousConsentData> anonymousConsents;
	private List<ConsentTemplateData> consentTemplate;

	private ConsentTemplateData givenTemplate;
	private ConsentTemplateData withdrawnTemplate;

	@Before
	public void setUp()
	{
		// anonymous consent data setup
		final AnonymousConsentData given = createAnonymousConsent(TEMPLATE_CODE_GIVEN, 1, CONSENT_GIVEN);
		final AnonymousConsentData withdrawn = createAnonymousConsent(TEMPLATE_CODE_WITHDRAWN, 1, CONSENT_WITHDRAWN);
		anonymousConsents = Arrays.asList(given, withdrawn);

		final LanguageData currentLanguage = new LanguageData();
		currentLanguage.setIsocode("en");

		// consent template data setup
		givenTemplate = createConsentTemplate(TEMPLATE_CODE_GIVEN, 1, "given", new Date(), null);
		withdrawnTemplate = createConsentTemplate(TEMPLATE_CODE_WITHDRAWN, 1, "withdrawn", new Date(), new Date());
		consentTemplate = Arrays.asList(givenTemplate, withdrawnTemplate);

		// other methods
		when(Boolean.valueOf(userFacade.isAnonymousUser())).thenReturn(Boolean.TRUE);
		when(sessionService.getAttribute(CONSENT_TEMPLATES)).thenReturn(consentTemplate);
		when(sessionService.getAttribute(PREVIOUS_CONSENT_LANGUAGE)).thenReturn(PREVIOUS_CONSENT_LANGUAGE_ISO);
		when(storeSessionFacade.getCurrentLanguage()).thenReturn(currentLanguage);
		when(anonymousConsentFacade.getConsentTemplates()).thenReturn(consentTemplate);
	}

	@Test
	public void shouldWorkOnlyForAnonymousUser()
	{
		// given
		when(Boolean.valueOf(userFacade.isAnonymousUser())).thenReturn(Boolean.FALSE);

		// when
		anonymousConsentFacade.synchronizeAnonymousConsents(anonymousConsentReader, anonymousConsentWriter);

		// then
		verify(anonymousConsentReader, times(0)).get();
		verify(anonymousConsentWriter, times(0)).accept(any());
		verify(sessionService, times(0)).setAttribute(any(), any());
	}

	@Test
	public void shouldCreateAnonymousConsentsIfNotAlreadyExist()
	{
		// given
		when(anonymousConsentReader.get()).thenReturn(Collections.emptyList());

		// when
		anonymousConsentFacade.synchronizeAnonymousConsents(anonymousConsentReader, anonymousConsentWriter);

		// then
		final List<AnonymousConsentData> anonymousConsents = captureAnonymousConsents();
		assertFalse(anonymousConsents.isEmpty());
	}

	@Test
	public void shouldCreateAnonymousConsentWhenAnonymousUserVisitsPageFirstTime()
	{
		// given
		when(anonymousConsentReader.get()).thenReturn(Collections.emptyList());

		// when
		anonymousConsentFacade.synchronizeAnonymousConsents(anonymousConsentReader, anonymousConsentWriter);

		// then
		assertAllAnonymousConsentsPresent(TEMPLATE_CODE_GIVEN, TEMPLATE_CODE_WITHDRAWN);
	}

	@Test
	public void shouldUpdateAnonymousConsentWithNewVersion()
	{
		// given
		when(anonymousConsentReader.get()).thenReturn(anonymousConsents);

		// adding templates
		final ConsentTemplateData v2 = createConsentTemplate(TEMPLATE_CODE_WITHDRAWN, 2, "withdrawn", null, null);
		when(anonymousConsentFacade.getConsentTemplates()).thenReturn(Arrays.asList(v2, givenTemplate));

		// when
		anonymousConsentFacade.synchronizeAnonymousConsents(anonymousConsentReader, anonymousConsentWriter);

		// then
		final List<AnonymousConsentData> capturedAnonymousConsents = captureAnonymousConsents();
		assertEquals(2, capturedAnonymousConsents.size());

		assertTrue(capturedAnonymousConsents.stream().anyMatch(c -> TEMPLATE_CODE_GIVEN.equals(c.getTemplateCode()) //
				&& c.getTemplateVersion() == 1 //
				&& CONSENT_GIVEN.equals(c.getConsentState())));

		assertTrue(capturedAnonymousConsents.stream().anyMatch(c -> TEMPLATE_CODE_WITHDRAWN.equals(c.getTemplateCode()) //
				&& c.getTemplateVersion() == 2 //
				&& c.getConsentState() == null));
	}

	@Test
	public void shouldRemoveConsentTemplatesFromSessionOnLanguageChange()
	{
		// given
		final String currentLanguageIso = storeSessionFacade.getCurrentLanguage().getIsocode();
		final String previousLanguageIso = sessionService.getAttribute(PREVIOUS_CONSENT_LANGUAGE);
		assertNotNull(sessionService.getAttribute(CONSENT_TEMPLATES));
		assertNotEquals(currentLanguageIso, previousLanguageIso);

		// when
		anonymousConsentFacade.checkLanguageChange();

		// then
		// verify that consent templates attribute was removed
		verify(sessionService).removeAttribute(CONSENT_TEMPLATES);
		verify(sessionService).setAttribute(PREVIOUS_CONSENT_LANGUAGE, currentLanguageIso);
	}

	@Test
	public void shouldPopulateSessionWithAnonymousConsents()
	{
		// given
		when(anonymousConsentReader.get()).thenReturn(anonymousConsents);

		// when
		anonymousConsentFacade.synchronizeAnonymousConsents(anonymousConsentReader, anonymousConsentWriter);

		// then
		final Map<String, String> capturedSessionConsents = captureSessionConsents();
		assertEquals(2, capturedSessionConsents.size());
		assertTrue(capturedSessionConsents.containsKey(TEMPLATE_CODE_GIVEN));
		assertTrue(capturedSessionConsents.containsKey(TEMPLATE_CODE_WITHDRAWN));
		assertEquals(CONSENT_GIVEN, capturedSessionConsents.get(TEMPLATE_CODE_GIVEN));
		assertEquals(CONSENT_WITHDRAWN, capturedSessionConsents.get(TEMPLATE_CODE_WITHDRAWN));
	}

	@Test
	public void shouldGetTemplatesFromDBOnlyWhenNoTemplatesInSession()
	{
		// given
		given(sessionService.getAttribute(CONSENT_TEMPLATES)).willReturn(null);
		given(consentFacade.getConsentTemplatesWithConsents()).willReturn(consentTemplate);
		given(anonymousConsentReader.get()).willReturn(Collections.emptyList());

		// when
		anonymousConsentFacade.synchronizeAnonymousConsents(anonymousConsentReader, anonymousConsentWriter);

		// then
		verify(consentFacade, atLeastOnce()).getConsentTemplatesWithConsents();
		verify(sessionService, atLeastOnce()).setAttribute(CONSENT_TEMPLATES, consentTemplate);
	}

	@Test
	public void shouldSendEventIfConsentChanged()
	{
		// given
		final Map<String, String> sessionConsents = new HashMap<>();
		sessionConsents.put(TEMPLATE_CODE_GIVEN, CONSENT_GIVEN);
		sessionConsents.put(TEMPLATE_CODE_WITHDRAWN, CONSENT_WITHDRAWN);
		when(sessionService.getAttribute(USER_CONSENTS)).thenReturn(sessionConsents);

		final AnonymousConsentData changedGiven = createAnonymousConsent(TEMPLATE_CODE_GIVEN, 1, CONSENT_WITHDRAWN);
		when(anonymousConsentReader.get()).thenReturn(Collections.singletonList(changedGiven));

		// when
		anonymousConsentFacade.synchronizeAnonymousConsents(anonymousConsentReader, anonymousConsentWriter);

		// then
		verify(anonymousConsentChangeEventFactory, times(1))
				.buildEvent(eq(TEMPLATE_CODE_GIVEN), eq(CONSENT_GIVEN), eq(CONSENT_WITHDRAWN), any());
		verify(anonymousConsentChangeEventFactory, times(1))
				.buildEvent(eq(TEMPLATE_CODE_WITHDRAWN), eq(CONSENT_WITHDRAWN), eq(null), any());
		verify(eventService, times(2)).publishEvent(any());
	}

	protected AnonymousConsentData createAnonymousConsent(final String templateCode, final int templateVersion,
			final String consentState)
	{
		final AnonymousConsentData anonymousConsent = new AnonymousConsentData();
		anonymousConsent.setConsentState(consentState);
		anonymousConsent.setTemplateCode(templateCode);
		anonymousConsent.setTemplateVersion(templateVersion);
		return anonymousConsent;
	}

	protected ConsentTemplateData createConsentTemplate(final String templateCode, final int version, final String consentCode,
			final Date givenDate, final Date withdrawnDate)
	{
		final ConsentTemplateData template = new ConsentTemplateData();
		final ConsentData consent = new ConsentData();
		consent.setCode(consentCode);
		consent.setConsentGivenDate(givenDate);
		consent.setConsentWithdrawnDate(withdrawnDate);
		template.setConsentData(consent);
		template.setVersion(Integer.valueOf(version));
		template.setDescription(templateCode);
		template.setExposed(true);
		template.setId(templateCode);
		return template;
	}

	protected void assertAllAnonymousConsentsPresent(final String... consentsId)
	{
		final List<AnonymousConsentData> capturedAnonymousConsents = captureAnonymousConsents();
		assertEquals(capturedAnonymousConsents.size(), consentsId.length);
		for (int i = 0; i < consentsId.length; i++)
		{
			assertEquals(consentsId[i], capturedAnonymousConsents.get(i).getTemplateCode());
		}
	}

	protected final List<AnonymousConsentData> captureAnonymousConsents()
	{
		verify(anonymousConsentWriter).accept(anonymousConsentCaptor.capture());
		return anonymousConsentCaptor.getValue();
	}

	protected final Map<String, String> captureSessionConsents()
	{
		verify(sessionService).setAttribute(eq(USER_CONSENTS), sessionConsentCaptor.capture());
		return sessionConsentCaptor.getValue();
	}
}
