/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationyprofile.strategy.impl;

import static de.hybris.platform.personalizationyprofile.constants.PersonalizationyprofileConstants.IDENTITY_ORIGIN_USER_ACCOUNT;
import static de.hybris.platform.personalizationyprofile.constants.PersonalizationyprofileConstants.IDENTITY_TYPE_EMAIL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.commercefacades.consent.data.ConsentTemplateData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants;
import de.hybris.platform.personalizationyprofile.yaas.ProfileReference;
import de.hybris.platform.personalizationyprofile.yaas.client.CxIdentityServiceClient;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hybris.charon.exp.HttpException;
import com.hybris.yprofile.consent.services.ConsentService;
import com.hybris.yprofile.constants.ProfileservicesConstants;


@UnitTest
public class DefaultCxProfileIdentifierStrategyTest
{
	private static final String USER_ID = "userId";
	private static final String PROFILE_ID = "profileId";
	private static final String PROFILE_ID_FOR_REGISTERED = "profileIdForRegistered";
	private static final String CONSENT_REFERENCE_VALUE = "consentReference";
	private static final String BOOLEAN_PAUSE_TRACKING_PARAMETER = "booleanPauseTrackingParameter";
	private static final String SECOND_BOOLEAN_PAUSE_TRACKING_PARAMETER = "secondBooleanPauseTrackingParameter";
	private static final String NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER = "noneBooleanPauseTrackingParameter";

	private DefaultCxProfileIdentifierStrategy strategy;

	@Mock
	private CxIdentityServiceClient cxIdentityServiceClient;

	@Mock
	private SessionService sessionService;

	@Mock
	private DefaultSessionTokenService defaultSessionTokenService;

	@Mock
	private UserService userService;

	@Mock
	private CustomerModel user;

	@Mock
	private CustomerModel anonymousUser;

	@Mock
	private ConsentService consentService;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	Configuration configuration;

	@Mock
	private ConsentFacade commerceConsentFacade;

	@Mock
	private ConsentTemplateData consentTemplateData;

	private List<String> pauseConsentReferenceUseParameters;

	private String sessionAttrKey;

	private final ProfileReference profileReference = new ProfileReference();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		strategy = new DefaultCxProfileIdentifierStrategy();
		strategy.setCxIdentityServiceClient(cxIdentityServiceClient);
		strategy.setSessionService(sessionService);
		strategy.setConsentService(consentService);
		strategy.setUserService(userService);
		strategy.setConfigurationService(configurationService);
		strategy.setCommerceConsentFacade(commerceConsentFacade);
		pauseConsentReferenceUseParameters = Collections.emptyList();
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(user.getUid()).thenReturn(USER_ID);
		sessionAttrKey = strategy.getSessionAttributeKey(user);
		profileReference.setProfileId(PROFILE_ID_FOR_REGISTERED);
		when(userService.isAnonymousUser(user)).thenReturn(Boolean.FALSE);
		when(userService.isAnonymousUser(anonymousUser)).thenReturn(Boolean.TRUE);
		when(consentService.isProfileTrackingConsentGiven()).thenReturn(true);
	}

	@Test
	public void testGetProfileIdentifierFromSession()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(PROFILE_ID);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertEquals(PROFILE_ID, profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForRegisteredUser()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		when(cxIdentityServiceClient.getProfileReferences(USER_ID, IDENTITY_TYPE_EMAIL, IDENTITY_ORIGIN_USER_ACCOUNT))
				.thenReturn(Collections.singletonList(profileReference));

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		verify(cxIdentityServiceClient, times(1)).getProfileReferences(any(), any(), any());
		assertEquals(PROFILE_ID_FOR_REGISTERED, profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForAnonymousUser()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		when(consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_VALUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(anonymousUser);

		//then
		verify(cxIdentityServiceClient, times(0)).getProfileReferences(any(), any(), any());
		assertEquals(CONSENT_REFERENCE_VALUE, profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForAnonymousUserProfileTrackingPausedOneBooleanFalse()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		pauseConsentReferenceUseParameters = Collections.singletonList(BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(Boolean.FALSE);
		when(consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_VALUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(anonymousUser);

		//then
		assertEquals(CONSENT_REFERENCE_VALUE, profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForAnonymousUserProfileTrackingPausedOneBooleanTrue()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		pauseConsentReferenceUseParameters = Collections.singletonList(BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(Boolean.TRUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(anonymousUser);

		//then
		assertNull(profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForAnonymousUserProfileTrackingPausedOneNoneBoolean()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		pauseConsentReferenceUseParameters = Collections.singletonList(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(StringUtils.SPACE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(anonymousUser);

		//then
		assertNull(profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForAnonymousUserProfileTrackingPausedOneNoneBooleanNotExists()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		pauseConsentReferenceUseParameters = Collections.singletonList(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(null);
		when(consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_VALUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(anonymousUser);

		//then
		assertEquals(CONSENT_REFERENCE_VALUE, profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForAnonymousUserProfileTrackingPausedOneBooleanAndOneNoneBoolean()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		pauseConsentReferenceUseParameters = Arrays.asList(BOOLEAN_PAUSE_TRACKING_PARAMETER, NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(Boolean.TRUE);
		when(sessionService.getAttribute(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(StringUtils.SPACE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(anonymousUser);

		//then
		assertNull(profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForAnonymousUserProfileTrackingPausedTwoBooleanAndOneNoneBoolean()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		pauseConsentReferenceUseParameters = Arrays.asList(BOOLEAN_PAUSE_TRACKING_PARAMETER, NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(Boolean.FALSE);
		when(sessionService.getAttribute(SECOND_BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(Boolean.TRUE);
		when(sessionService.getAttribute(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(StringUtils.SPACE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(anonymousUser);

		//then
		assertNull(profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForAnonymousUserProfileTrackingPausedOneBooleanAndOneNoneBooleanNotExist()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		pauseConsentReferenceUseParameters = Arrays.asList(BOOLEAN_PAUSE_TRACKING_PARAMETER, NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(null);
		when(sessionService.getAttribute(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(null);
		when(consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_VALUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(anonymousUser);

		//then
		assertEquals(CONSENT_REFERENCE_VALUE, profileIdentifier);
	}


	@Test
	public void testGetProfileIdentifierFallbackValueForRegisteredUser()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		when(cxIdentityServiceClient.getProfileReferences(USER_ID, IDENTITY_TYPE_EMAIL, IDENTITY_ORIGIN_USER_ACCOUNT))
				.thenReturn(Collections.emptyList());
		when(consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_VALUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		verify(cxIdentityServiceClient, times(1)).getProfileReferences(any(), any(), any());
		assertEquals(CONSENT_REFERENCE_VALUE, profileIdentifier);
	}

	@Test
	public void testNoProfileIdentifier()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		when(cxIdentityServiceClient.getProfileReferences(USER_ID, IDENTITY_TYPE_EMAIL, IDENTITY_ORIGIN_USER_ACCOUNT))
				.thenThrow(new HttpException(400, "Bad request"));
		when(consentService.getConsentReferenceFromSession()).thenReturn(null);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertNull(profileIdentifier);
		verify(cxIdentityServiceClient, times(1)).getProfileReferences(any(), any(), any());
	}

	@Test
	public void testRuntimeExceptionFromIdentityService()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		when(cxIdentityServiceClient.getProfileReferences(USER_ID, IDENTITY_TYPE_EMAIL, IDENTITY_ORIGIN_USER_ACCOUNT))
				.thenThrow(new RuntimeException("Unknown exception"));
		when(consentService.getConsentReferenceFromSession()).thenReturn(null);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertNull(profileIdentifier);
		verify(cxIdentityServiceClient, times(1)).getProfileReferences(any(), any(), any());
	}

	@Test
	public void testNoConsentGiven()
	{
		//given
		when(consentService.isProfileTrackingConsentGiven()).thenReturn(false);
		when(commerceConsentFacade.getLatestConsentTemplate(ProfileservicesConstants.PROFILE_CONSENT))
				.thenReturn(consentTemplateData);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertNull(profileIdentifier);
	}

	@Test
	public void testNoConsentTemplateForProfile()
	{
		//given
		when(consentService.isProfileTrackingConsentGiven()).thenReturn(false);
		when(commerceConsentFacade.getLatestConsentTemplate(ProfileservicesConstants.PROFILE_CONSENT))
				.thenThrow(new ModelNotFoundException("Template not found"));
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getBoolean(PersonalizationservicesConstants.IGNORE_CONSENT_CHECK_WHEN_NO_CONSENT_TEMPLATE, Boolean.TRUE))
				.thenReturn(false);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertNull(profileIdentifier);
	}

	@Test
	public void testNoConsentTemplateForProfileWhenIgnoreTrue()
	{
		//given
		when(consentService.isProfileTrackingConsentGiven()).thenReturn(false);
		when(commerceConsentFacade.getLatestConsentTemplate(ProfileservicesConstants.PROFILE_CONSENT))
				.thenThrow(new ModelNotFoundException("Template not found"));
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getBoolean(PersonalizationservicesConstants.IGNORE_CONSENT_CHECK_WHEN_NO_CONSENT_TEMPLATE, Boolean.TRUE))
				.thenReturn(true);
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(PROFILE_ID);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertEquals(PROFILE_ID, profileIdentifier);
	}

}
