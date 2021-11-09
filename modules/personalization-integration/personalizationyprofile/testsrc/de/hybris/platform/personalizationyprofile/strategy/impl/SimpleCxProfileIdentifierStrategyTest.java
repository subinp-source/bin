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
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.personalizationyprofile.yaas.ProfileReference;
import de.hybris.platform.personalizationyprofile.yaas.client.CxIdentityServiceClient;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hybris.charon.exp.NotFoundException;
import com.hybris.yprofile.consent.services.ConsentService;


@UnitTest
public class SimpleCxProfileIdentifierStrategyTest
{
	private static final String USER_ID = "userId";
	private static final String PROFILE_ID = "profileId";
	private static final String PROFILE_ID_FOR_REGISTERED = "profileIdForRegistered";
	private static final String CONSENT_REFERENCE_VALUE = "consentReference";
	private static final String BOOLEAN_PAUSE_TRACKING_PARAMETER = "booleanPauseTrackingParameter";
	private static final String SECOND_BOOLEAN_PAUSE_TRACKING_PARAMETER = "secondBooleanPauseTrackingParameter";
	private static final String NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER = "noneBooleanPauseTrackingParameter";

	private SimpleCxProfileIdentifierStrategy strategy;

	@Mock
	private CxIdentityServiceClient cxIdentityServiceClient;

	@Mock
	private SessionService sessionService;

	@Mock
	private UserService userService;

	@Mock
	private CustomerModel user;

	@Mock
	private CustomerModel anonymousUser;

	@Mock
	private ConsentService consentService;

	private List<String> pauseConsentReferenceUseParameters;

	private String sessionAttrKey;

	private final ProfileReference profileReference = new ProfileReference();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		strategy = new SimpleCxProfileIdentifierStrategy();
		strategy.setCxIdentityServiceClient(cxIdentityServiceClient);
		strategy.setSessionService(sessionService);
		strategy.setConsentService(consentService);
		strategy.setUserService(userService);
		pauseConsentReferenceUseParameters = Collections.emptyList();
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(user.getUid()).thenReturn(USER_ID);
		sessionAttrKey = strategy.getSessionAttributeKey(user);
		profileReference.setProfileId(PROFILE_ID_FOR_REGISTERED);
		when(userService.isAnonymousUser(user)).thenReturn(Boolean.FALSE);
		when(userService.isAnonymousUser(anonymousUser)).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testGetProfileIdentifierFromSession()
	{
		//given
		pauseConsentReferenceUseParameters = Collections.singletonList(BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(Boolean.TRUE);
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
		when(consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_VALUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertEquals(CONSENT_REFERENCE_VALUE, profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForAnonymousUser()
	{
		//given
		when(consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_VALUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(anonymousUser);

		//then
		verify(cxIdentityServiceClient, times(0)).getProfileReferences(any(), any(), any());
		assertEquals(CONSENT_REFERENCE_VALUE, profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForProfileTrackingPausedOneBooleanFalse()
	{
		//given
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
		pauseConsentReferenceUseParameters = Collections.singletonList(BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(Boolean.TRUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(anonymousUser);

		//then
		assertNull(profileIdentifier);
		verify(consentService, times(0)).getConsentReferenceFromSession();
	}

	@Test
	public void testGetProfileIdentifierForRegisteredUserProfileTrackingPausedOneBooleanTrue()
	{
		//given
		pauseConsentReferenceUseParameters = Collections.singletonList(BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(Boolean.TRUE);

		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		when(cxIdentityServiceClient.getProfileReferences(USER_ID, IDENTITY_TYPE_EMAIL, IDENTITY_ORIGIN_USER_ACCOUNT))
				.thenReturn(List.of(profileReference));

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertEquals(PROFILE_ID_FOR_REGISTERED, profileIdentifier);
		verify(consentService, times(0)).getConsentReferenceFromSession();
	}

	@Test
	public void testGetProfileIdentifierForProfileTrackingPausedOneNoneBoolean()
	{
		//given
		pauseConsentReferenceUseParameters = Collections.singletonList(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(StringUtils.SPACE);
		when(consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_VALUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertEquals(CONSENT_REFERENCE_VALUE, profileIdentifier);
	}

	@Test
	public void testGetProfileIdentifierForProfileTrackingPausedOneNoneBooleanNotExists()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		pauseConsentReferenceUseParameters = Collections.singletonList(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(null);
		when(consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_VALUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

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
	public void testGetProfileIdentifierForProfileTrackingPausedOneBooleanAndOneNoneBooleanNotExist()
	{
		//given
		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		pauseConsentReferenceUseParameters = Arrays.asList(BOOLEAN_PAUSE_TRACKING_PARAMETER, NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(null);
		when(sessionService.getAttribute(NONE_BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(null);
		when(consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_VALUE);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertEquals(CONSENT_REFERENCE_VALUE, profileIdentifier);
	}


	@Test
	public void testNoProfileIdentifier()
	{
		//given
		when(consentService.getConsentReferenceFromSession()).thenReturn(null);

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertNull(profileIdentifier);
	}

	@Test
	public void testNoProfileIdentifierForRegisteredUser()
	{
		//given
		pauseConsentReferenceUseParameters = Collections.singletonList(BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(Boolean.TRUE);

		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		when(cxIdentityServiceClient.getProfileReferences(USER_ID, IDENTITY_TYPE_EMAIL, IDENTITY_ORIGIN_USER_ACCOUNT))
				.thenThrow(new NotFoundException(404, "Not found"));

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
		pauseConsentReferenceUseParameters = Collections.singletonList(BOOLEAN_PAUSE_TRACKING_PARAMETER);
		strategy.setPauseConsentReferenceUseParameters(pauseConsentReferenceUseParameters);
		when(sessionService.getAttribute(BOOLEAN_PAUSE_TRACKING_PARAMETER)).thenReturn(Boolean.TRUE);

		when(sessionService.getAttribute(sessionAttrKey)).thenReturn(null);
		when(cxIdentityServiceClient.getProfileReferences(USER_ID, IDENTITY_TYPE_EMAIL, IDENTITY_ORIGIN_USER_ACCOUNT))
				.thenThrow(new RuntimeException("Unknown exception"));

		//when
		final String profileIdentifier = strategy.getProfileIdentifier(user);

		//then
		assertNull(profileIdentifier);
		verify(cxIdentityServiceClient, times(1)).getProfileReferences(any(), any(), any());
	}
}
