/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationyprofile.strategy.impl;


import static com.hybris.yprofile.constants.ProfileservicesConstants.PROFILE_CONSENT_GIVEN;
import static de.hybris.platform.personalizationyprofile.constants.PersonalizationyprofileConstants.CONSENT_REFERENCE_SESSION_ATTR_KEY;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.personalizationservices.process.strategies.BaseCxProcessParameterStrategyTest;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.servicelayer.session.SessionService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hybris.yprofile.consent.services.ConsentService;


@UnitTest
public class CxProcessParameterConsentReferenceStrategyTest extends BaseCxProcessParameterStrategyTest
{
	private static final String CONSENT_REFERENCE_KEY = "consentReferenceKey";

	private final CxProcessParameterConsentReferenceStrategy strategy = new CxProcessParameterConsentReferenceStrategy();

	@Mock
	private SessionService sessionService;

	@Mock
	private ConsentService consentService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		strategy.setProcessParameterHelper(processParameterHelper);
		strategy.setSessionService(sessionService);
		strategy.setConsentService(consentService);
		BDDMockito.given(consentService.getConsentReferenceFromSession()).willReturn(CONSENT_REFERENCE_KEY);
	}

	@Test
	public void shouldLoadConsentReferenceFromProcess()
	{
		//given
		final CxPersonalizationProcessModel process = new CxPersonalizationProcessModel();
		final BusinessProcessParameterModel processParameter = createBusinessProcessParameterModel(
				CONSENT_REFERENCE_SESSION_ATTR_KEY, CONSENT_REFERENCE_KEY);

		given(processParameterHelper.containsParameter(process, CONSENT_REFERENCE_SESSION_ATTR_KEY)).willReturn(Boolean.TRUE);
		given(processParameterHelper.getProcessParameterByName(process, CONSENT_REFERENCE_SESSION_ATTR_KEY))
				.willReturn(processParameter);

		//when
		strategy.load(process);

		//then
		verify(consentService).saveConsentReferenceInSession(CONSENT_REFERENCE_KEY);
	}


	@Test
	public void shouldStoreConsentReferenceInProcess()
	{
		//given
		final CxPersonalizationProcessModel process = new CxPersonalizationProcessModel();
		given(consentService.getConsentReferenceFromSession()).willReturn(CONSENT_REFERENCE_KEY);

		//when
		strategy.store(process);

		//then
		verify(processParameterHelper).setProcessParameter(process, CONSENT_REFERENCE_SESSION_ATTR_KEY, CONSENT_REFERENCE_KEY);
	}

	@Test
	public void shouldNotStoreConsentReferenceInProcess()
	{
		//given
		final CxPersonalizationProcessModel process = new CxPersonalizationProcessModel();
		given(consentService.getConsentReferenceFromSession()).willReturn(null);

		//when
		strategy.store(process);

		//then
		verify(processParameterHelper, times(0)).setProcessParameter(process, CONSENT_REFERENCE_SESSION_ATTR_KEY,
				CONSENT_REFERENCE_KEY);
	}

	@Test
	public void shouldLoadConsentGivenFromProcess()
	{
		//given
		final CxPersonalizationProcessModel process = new CxPersonalizationProcessModel();
		final BusinessProcessParameterModel consentReferenceParameter = createBusinessProcessParameterModel(
				CONSENT_REFERENCE_SESSION_ATTR_KEY, CONSENT_REFERENCE_KEY);
		final BusinessProcessParameterModel consentGivenParameter = createBusinessProcessParameterModel(PROFILE_CONSENT_GIVEN,
				Boolean.TRUE);


		given(processParameterHelper.containsParameter(process, CONSENT_REFERENCE_SESSION_ATTR_KEY)).willReturn(Boolean.TRUE);
		given(processParameterHelper.getProcessParameterByName(process, CONSENT_REFERENCE_SESSION_ATTR_KEY))
				.willReturn(consentReferenceParameter);
		given(processParameterHelper.containsParameter(process, PROFILE_CONSENT_GIVEN)).willReturn(Boolean.TRUE);
		given(processParameterHelper.getProcessParameterByName(process, PROFILE_CONSENT_GIVEN)).willReturn(consentGivenParameter);

		//when
		strategy.load(process);

		//then
		verify(consentService).saveConsentReferenceInSession(CONSENT_REFERENCE_KEY);
		verify(sessionService).setAttribute(PROFILE_CONSENT_GIVEN, Boolean.TRUE);
	}


	@Test
	public void shouldStoreConsentGivenInProcess()
	{
		//given
		final CxPersonalizationProcessModel process = new CxPersonalizationProcessModel();
		given(consentService.getConsentReferenceFromSession()).willReturn(CONSENT_REFERENCE_KEY);
		given(sessionService.getAttribute(PROFILE_CONSENT_GIVEN)).willReturn(Boolean.TRUE);

		//when
		strategy.store(process);

		//then
		verify(processParameterHelper).setProcessParameter(process, PROFILE_CONSENT_GIVEN, Boolean.TRUE);
	}

	@Test
	public void shouldNotStoreConsentGivenInProcess()
	{
		//given
		final CxPersonalizationProcessModel process = new CxPersonalizationProcessModel();
		given(consentService.getConsentReferenceFromSession()).willReturn(null);
		given(sessionService.getAttribute(PROFILE_CONSENT_GIVEN)).willReturn(null);

		//when
		strategy.store(process);

		//then
		verify(processParameterHelper, times(0)).setProcessParameter(process, PROFILE_CONSENT_GIVEN, Boolean.TRUE);

	}

}
