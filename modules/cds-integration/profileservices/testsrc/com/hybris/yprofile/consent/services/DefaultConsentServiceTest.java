/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.consent.services;

import com.hybris.charon.RawResponse;
import com.hybris.yprofile.consent.cookie.EnhancedCookieGenerator;
import com.hybris.yprofile.rest.clients.ConsentServiceClient;
import com.hybris.yprofile.services.ProfileConfigurationService;
import com.hybris.yprofile.services.RetrieveRestClientStrategy;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
public class DefaultConsentServiceTest {

    private static final String TENANT_ID = "tenant";
    private static final String CONSENT_REFERENCE = "myConsentReference";
    private static final String SITE_ID = "mySite";
    public static final String CONSENT_REFERENCE_COOKIE_NAME = "mySite-consentReference";
    public static final String CONSENT_REFERENCE_SESSION_KEY = "consent-reference";
    public static final String PROFILE_ID_COOKIE_NAME = SITE_ID + "-profileId";

    private DefaultConsentService defaultConsentService;

    @Mock
    private ConsentServiceClient consentServiceClient;

    @Mock
    private EnhancedCookieGenerator cookieGenerator;

    @Mock
    private SessionService sessionService;

    @Mock
    private BaseSiteService baseSiteService;

    @Mock
    private UserService userService;

    @Mock
    private ProfileConfigurationService profileConfigurationService;

    @Mock
    private RetrieveRestClientStrategy retrieveRestClientStrategy;

    @Mock
    private BaseSiteModel baseSiteModel;

    @Mock
    private ModelService modelService;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private UserModel userModel;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Cookie cookie;

    @Mock
    private RawResponse rawResponse;

    @Mock
    private Configuration configuration;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        defaultConsentService = new DefaultConsentService();
        defaultConsentService.setSessionService(sessionService);
        defaultConsentService.setUserService(userService);
        defaultConsentService.setProfileConfigurationService(profileConfigurationService);
        defaultConsentService.setRetrieveRestClientStrategy(retrieveRestClientStrategy);
        defaultConsentService.setBaseSiteService(baseSiteService);
        defaultConsentService.setCookieGenerator(cookieGenerator);
        defaultConsentService.setModelService(modelService);
        defaultConsentService.setConfigurationService(configurationService);

        when(baseSiteModel.getUid()).thenReturn(SITE_ID);
        when(userService.getCurrentUser()).thenReturn(this.userModel);
        when(userService.isAnonymousUser(any(UserModel.class))).thenReturn(false);
        when(retrieveRestClientStrategy.getConsentServiceRestClient()).thenReturn(consentServiceClient);
        when(profileConfigurationService.getTenant(SITE_ID)).thenReturn(TENANT_ID);
        when(baseSiteService.getCurrentBaseSite()).thenReturn(baseSiteModel);
    }


    @Test
    public void assertWhenProfileAnonymousUserConsentCookieIsNullShouldReturnFalse() throws Exception {

        Cookie cookie = new Cookie("anonymous-consents", "%5B%7B%22templateCode%22%3A%22PROFILE%22%2C%22templateVersion%22%3A1%2C%22consentState%22%3Anull%7D%5D");
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[1];
        cookies[0] = cookie;
        when(request.getCookies()).thenReturn(cookies);

        boolean result  = defaultConsentService.isProfileTrackingConsentGivenForAnonymousUser(request);

        assertFalse(result);
    }

    @Test
    public void assertWhenProfileAnonymousUserConsentHeaderIsNullShouldReturnFalse() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("X-Anonymous-Consents"))).thenReturn("%5B%7B%22templateCode%22%3A%22PROFILE%22%2C%22templateVersion%22%3A1%2C%22consentState%22%3Anull%7D%5D");

        boolean result  = defaultConsentService.isProfileTrackingConsentGivenForAnonymousUser(request);

        assertFalse(result);
    }

    @Test
    public void assertWhenProfileAnonymousUserConsentCookieIsWithdrawnShouldReturnFalse() throws Exception {

        Cookie cookie = new Cookie("anonymous-consents", "%5B%7B%22templateCode%22%3A%22PROFILE%22%2C%22templateVersion%22%3A1%2C%22consentState%22%3A%22WITHDRAWN%22%7D%5D");

        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[1];
        cookies[0] = cookie;
        when(request.getCookies()).thenReturn(cookies);

        boolean result  = defaultConsentService.isProfileTrackingConsentGivenForAnonymousUser(request);

        assertFalse(result);
    }

    @Test
    public void assertWhenProfileAnonymousUserConsentHeaderIsWithdrawnShouldReturnFalse() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("X-Anonymous-Consents"))).thenReturn("%5B%7B%22templateCode%22%3A%22PROFILE%22%2C%22templateVersion%22%3A1%2C%22consentState%22%3A%22WITHDRAWN%22%7D%5D");

        boolean result  = defaultConsentService.isProfileTrackingConsentGivenForAnonymousUser(request);

        assertFalse(result);
    }

    @Test
    public void assertWhenProfileAnonymousUserConsentCookieIsGivenShouldReturnTrue() throws Exception {

        Cookie cookie = new Cookie("anonymous-consents", "%5B%7B%22templateCode%22%3A%22PROFILE%22%2C%22templateVersion%22%3A1%2C%22consentState%22%3A%22GIVEN%22%7D%5D");

        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[1];
        cookies[0] = cookie;
        when(request.getCookies()).thenReturn(cookies);

        boolean result  = defaultConsentService.isProfileTrackingConsentGivenForAnonymousUser(request);

        assertTrue(result);
    }

    @Test
    public void assertWhenProfileAnonymousUserConsentHeaderIsGivenShouldReturnTrue() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(eq("X-Anonymous-Consents"))).thenReturn("%5B%7B%22templateCode%22%3A%22PROFILE%22%2C%22templateVersion%22%3A1%2C%22consentState%22%3A%22GIVEN%22%7D%5D");

        boolean result  = defaultConsentService.isProfileTrackingConsentGivenForAnonymousUser(request);

        assertTrue(result);
    }


    @Test
    public void assertWhenProfileLoggedInUserConsentIsNullShouldReturnFalse() throws Exception {

        Map<String, String> userConsents = new HashMap<>();
        userConsents.put("PROFILE", null);

        when(sessionService.getAttribute("user-consents")).thenReturn(userConsents);

        boolean result  = defaultConsentService.isProfileTrackingConsentGivenForLoggedInUser();

        assertFalse(result);
    }

    @Test
    public void assertWhenProfileLoggedInUserConsentIsWithdrawnShouldReturnFalse() throws Exception {

        Map<String, String> userConsents = new HashMap<>();
        userConsents.put("PROFILE", "WITHDRAWN");

        when(sessionService.getAttribute("user-consents")).thenReturn(userConsents);

        boolean result  = defaultConsentService.isProfileTrackingConsentGivenForLoggedInUser();

        assertFalse(result);
    }

    @Test
    public void assertWhenProfileLoggedInUserConsentIsGivenShouldReturnTrue() throws Exception {


        Map<String, String> userConsents = new HashMap<>();
        userConsents.put("PROFILE", "GIVEN");

        when(sessionService.getAttribute("user-consents")).thenReturn(userConsents);

        boolean result  = defaultConsentService.isProfileTrackingConsentGivenForLoggedInUser();

        assertTrue(result);
    }

    @Test
    public void verifyConsentReferenceFromCookieIsSavedInSessionAndCurrentUserModel() {

        when(cookie.getName()).thenReturn(CONSENT_REFERENCE_COOKIE_NAME);
        when(cookie.getValue()).thenReturn(CONSENT_REFERENCE);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        when(userService.getCurrentUser()).thenReturn(userModel);
        when(userService.isAnonymousUser(any(UserModel.class))).thenReturn(false);
        when(userModel.getConsentReference()).thenReturn("anotherConsentReference");

        defaultConsentService.saveConsentReferenceInSessionAndCurrentUserModel(httpServletRequest);

        verify(sessionService, times(1)).setAttribute(matches(CONSENT_REFERENCE_SESSION_KEY), matches(CONSENT_REFERENCE));
        verify(modelService, times(1)).save(any(UserModel.class));
        verify(modelService, times(1)).refresh(any(UserModel.class));
    }

    @Test
    public void verifyConsentReferenceFromHeaderIsSavedInSessionAndCurrentUserModel() {

        when(configurationService.getConfiguration()).thenReturn(configuration);
        when(configuration.getString(anyString())).thenReturn("x-consent-reference");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(anyString())).thenReturn(CONSENT_REFERENCE);
        when(userService.getCurrentUser()).thenReturn(userModel);
        when(userService.isAnonymousUser(any(UserModel.class))).thenReturn(false);
        when(userModel.getConsentReference()).thenReturn("anotherConsentReference");

        defaultConsentService.saveConsentReferenceInSessionAndCurrentUserModel(request);

        verify(sessionService, times(1)).setAttribute(matches(CONSENT_REFERENCE_SESSION_KEY), matches(CONSENT_REFERENCE));
        verify(modelService, times(1)).save(any(UserModel.class));
        verify(modelService, times(1)).refresh(any(UserModel.class));
    }
    
    @Test
    public void verifyConsentReferenceIsSavedInSession() {
        defaultConsentService.saveConsentReferenceInSession(CONSENT_REFERENCE);

        verify(sessionService, times(1)).setAttribute(matches(CONSENT_REFERENCE_SESSION_KEY), matches(CONSENT_REFERENCE));
    }

    @Test
    public void verifyDeleteConsentReferenceRequestIsSentToProfileAndDeletedFromUserModel() {

        when(profileConfigurationService.isProfileTrackingPaused()).thenReturn(false);
        when(profileConfigurationService.isConfigurationPresent()).thenReturn(true);
        when(userModel.getConsentReference()).thenReturn(CONSENT_REFERENCE);
        when(consentServiceClient.deleteConsentReference(anyString(), anyString())).thenReturn(Observable.just(rawResponse));
        when(rawResponse.getStatusCode()).thenReturn(204);

        defaultConsentService.deleteConsentReferenceInConsentServiceAndInUserModel(userModel, CONSENT_REFERENCE);

        verify(consentServiceClient, times(1)).deleteConsentReference(matches(CONSENT_REFERENCE), anyString());
        verify(modelService, times(1)).save(any(UserModel.class));

    }

    @Test
    public void verifyDeleteConsentReferenceRequestIsNotSentToProfileWhenProfileTrackingIsPaused() {
        when(profileConfigurationService.isProfileTrackingPaused()).thenReturn(true);
        when(profileConfigurationService.isConfigurationPresent()).thenReturn(true);

        defaultConsentService.deleteConsentReferenceInConsentServiceAndInUserModel(userModel, CONSENT_REFERENCE);

        verify(consentServiceClient, never()).deleteConsentReference(anyString(), anyString());
        verify(modelService, never()).save(any(UserModel.class));
    }

    @Test
    public void verifyDeleteConsentReferenceRequestIsNotSentToProfileWhenYaasConfigurationIsNotPresent() {

        when(profileConfigurationService.isProfileTrackingPaused()).thenReturn(false);
        when(profileConfigurationService.isConfigurationPresent()).thenReturn(false);

        defaultConsentService.deleteConsentReferenceInConsentServiceAndInUserModel(userModel, CONSENT_REFERENCE);

        verify(consentServiceClient, never()).deleteConsentReference(anyString(), anyString());
        verify(modelService, never()).save(any(UserModel.class));

    }

    @Test
    public void verifyDeleteConsentReferenceRequestIsNotSentToProfileWhenConsentReferenceIsNotPresent() {

        when(profileConfigurationService.isProfileTrackingPaused()).thenReturn(true);
        when(profileConfigurationService.isConfigurationPresent()).thenReturn(true);
        when(consentServiceClient.deleteConsentReference(anyString(), anyString())).thenReturn(Observable.just(rawResponse));
        when(rawResponse.getStatusCode()).thenReturn(204);

        defaultConsentService.deleteConsentReferenceInConsentServiceAndInUserModel(userModel, null);

        verify(consentServiceClient, never()).deleteConsentReference(anyString(), anyString());
        verify(modelService, never()).save(any(UserModel.class));

    }

    @Test
    public void testSetProfileIdCookieWhenRequestHasNoCRCookie(){
        // given
        final String consentReferenceId = "7bdc6562-abf6-4dbb-8267-c20232bdadf5";
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[]{});
        // when
        defaultConsentService.setProfileIdCookie(httpServletRequest, httpServletResponse, consentReferenceId);
        // verify
        verify(cookieGenerator, times(1)).addCookie(eq(httpServletResponse), eq(consentReferenceId), eq(true));
    }

    @Test
    public void testSetProfileIdCookieWhenRequestAlreadyHasCRCookie(){
        // given
        final String consentReferenceId = "7bdc6562-abf6-4dbb-8267-c20232bdadf5";
        when(cookie.getName()).thenReturn(PROFILE_ID_COOKIE_NAME);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        // when
        defaultConsentService.setProfileIdCookie(httpServletRequest,httpServletResponse,consentReferenceId);
        // verify
        verify(httpServletResponse, never()).addCookie(any(Cookie.class));
    }

}