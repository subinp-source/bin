/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.consent.filters;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hybris.yprofile.consent.services.ConsentService;
import com.hybris.yprofile.services.ProfileConfigurationService;

import static com.hybris.yprofile.constants.ProfileservicesConstants.DEBUG_HEADER_NAME;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
public class OCCConsentLayerFilterTest {

    private static final String REQUESTEDURL = "http://mySite.profile.com";
    private static final String SERVLET_PATH = "/c/584";
    public static final String DEBUG = "x-profile-tag-debug";

    private OCCConsentLayerFilter occConsentLayerFilter;

    @Mock
    private ProfileConfigurationService profileConfigurationService;

    @Mock
    private ConsentService consentService;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserModel userModel;

    @Mock
    private Configuration configuration;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        occConsentLayerFilter = new OCCConsentLayerFilter();
        occConsentLayerFilter.setProfileConfigurationService(profileConfigurationService);
        occConsentLayerFilter.setConsentService(consentService);
        occConsentLayerFilter.setUserService(userService);
        occConsentLayerFilter.setConfigurationService(configurationService);

        final StringBuffer requestUrlSb = new StringBuffer();
        requestUrlSb.append(REQUESTEDURL);
        when(httpServletRequest.getRequestURL()).thenReturn(requestUrlSb);
        when(httpServletRequest.getRequestURI()).thenReturn(requestUrlSb.toString());
        when(httpServletRequest.getServletPath()).thenReturn(SERVLET_PATH);

    }

    @Test
    public void shouldTrackWhenProfileTrackingConsentIsGiven() throws ServletException, IOException {


        when(configurationService.getConfiguration()).thenReturn(configuration);
        when(configuration.getString(eq(DEBUG_HEADER_NAME))).thenReturn(DEBUG);
        when(httpServletRequest.getHeader(eq(DEBUG))).thenReturn("true");
        when(consentService.isProfileTrackingConsentGiven(httpServletRequest)).thenReturn(true);
        when(userModel.getDeactivationDate()).thenReturn(null);
        when(userService.isAnonymousUser(any(UserModel.class))).thenReturn(false);
        when(userService.getCurrentUser()).thenReturn(userModel);

        occConsentLayerFilter.doFilterInternal(httpServletRequest,httpServletResponse, filterChain);

        //verify(profileConfigurationService, times(1)).setProfileTagDebugFlagInSession(eq(Boolean.TRUE));
        verify(consentService, times(1)).saveConsentReferenceInSessionAndCurrentUserModel(any(HttpServletRequest.class));
    }


    @Test
    public void shouldNotTrackWhenProfileTrackingConsentIsWithdrawnAndRemoveCookie() throws ServletException, IOException {

        when(consentService.isProfileTrackingConsentGiven(httpServletRequest)).thenReturn(false);

        occConsentLayerFilter.doFilterInternal(httpServletRequest,httpServletResponse, filterChain);

        verify(profileConfigurationService, never()).setProfileTagDebugFlagInSession(anyBoolean());
        verify(consentService, never()).saveConsentReferenceInSessionAndCurrentUserModel(any(HttpServletRequest.class));
    }

    @Test
    public void shouldNotTrackWhenAccountIsDeactivated() throws ServletException, IOException {

        when(consentService.isProfileTrackingConsentGiven(httpServletRequest)).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(userModel);
        when(userModel.getDeactivationDate()).thenReturn(mock(Date.class));

        occConsentLayerFilter.doFilterInternal(httpServletRequest,httpServletResponse, filterChain);

        verify(profileConfigurationService, never()).setProfileTagDebugFlagInSession(anyBoolean());
        verify(consentService, never()).saveConsentReferenceInSessionAndCurrentUserModel(any(HttpServletRequest.class));
        verify(consentService, times(1)).removeConsentReferenceInSession(any(HttpServletResponse.class));
    }

}
