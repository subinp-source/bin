/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.services.customer;

import com.hybris.yprofile.consent.services.ConsentService;
import com.hybris.yprofile.services.ProfileConfigurationService;
import com.hybris.yprofile.services.ProfileTransactionService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

@UnitTest
public class ProfileCustomerFacadeTest {

    private static final String CONSENT_REFERENCE_ID = "1410d071-4efb-44e8-b33c-9ba30a03f24a";

    private static final String CONSENT_REFERENCE_COOKIE_NAME = "electronics-consentReference";

    @Mock
    private ProfileTransactionService profileTransactionService;

    @Mock
    private ConsentService consentService;

    @Mock
    private DefaultSessionTokenService defaultSessionTokenService;

    @Mock
    private ProfileConfigurationService profileConfigurationService;

    @Mock
    private UserService userService;

    @Mock
    private CustomerModel userModel;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private CartService cartService;

    @Mock
    private UserFacade userFacade;

    @Mock
    private EventService eventService;

    @Mock
    private StoreSessionFacade storeSessionFacade;

    @Mock
    private BaseStoreService baseStoreService;

    @Mock
    private BaseSiteService baseSiteService;

    @Mock
    private CommonI18NService commonI18NService;

    /** The instance under test. */
    private ProfileCustomerFacade profileCustomerFacade;

    @Before
    public void setUp() {

        final String sessionId = "Token";
        final String siteId = "electronics";

        MockitoAnnotations.initMocks(this);

        profileCustomerFacade = spy(new ProfileCustomerFacade());
        profileCustomerFacade.setProfileTransactionService(this.profileTransactionService);
        profileCustomerFacade.setConsentService(this.consentService);
        profileCustomerFacade.setDefaultSessionTokenService(this.defaultSessionTokenService);
        profileCustomerFacade.setProfileConfigurationService(this.profileConfigurationService);
        profileCustomerFacade.setUserService(this.userService);
        profileCustomerFacade.setCartService(this.cartService);
        profileCustomerFacade.setUserFacade(this.userFacade);
        profileCustomerFacade.setEventService(this.eventService);
        profileCustomerFacade.setStoreSessionFacade(this.storeSessionFacade);
        profileCustomerFacade.setBaseSiteService(this.baseSiteService);
        profileCustomerFacade.setBaseStoreService(this.baseStoreService);
        profileCustomerFacade.setCommonI18NService(this.commonI18NService);

        when(userService.getCurrentUser()).thenReturn(this.userModel);
        when(this.profileConfigurationService.isProfileTrackingPaused()).thenReturn(false);
        when(this.defaultSessionTokenService.getOrCreateSessionToken()).thenReturn(sessionId);
        when(this.cartService.hasSessionCart()).thenReturn(false);
        when(this.storeSessionFacade.getDefaultCurrency()).thenReturn(new CurrencyData());
        when(this.profileConfigurationService.isProfileTagDebugEnabledInSession()).thenReturn(true);

        doReturn(siteId).when(profileCustomerFacade).getSiteId();
        doReturn(new CustomerData()).when(profileCustomerFacade).getCurrentCustomer();
        doReturn(this.httpServletRequest).when(profileCustomerFacade).getHttpServletRequest();
    }

    @Test
    public void testLoginSuccess() {
        //given
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(CONSENT_REFERENCE_COOKIE_NAME, CONSENT_REFERENCE_ID);
        when(this.httpServletRequest.getCookies()).thenReturn(cookies);
        when(this.consentService.getConsentReferenceFromSession()).thenReturn(CONSENT_REFERENCE_ID);
        //when
        this.profileCustomerFacade.loginSuccess();
        //verify
        verify(this.profileTransactionService, times(1)).sendLoginEvent(any(CustomerModel.class), eq(CONSENT_REFERENCE_ID), anyString(), anyString());
    }

    @Test
    public void testLoginSuccessWhenConsentReferenceIsNull() {
        //given
        Cookie[] cookies = new Cookie[0];
        when(this.httpServletRequest.getCookies()).thenReturn(cookies);
        when(this.consentService.getConsentReferenceFromSession()).thenReturn(null);
        //when
        this.profileCustomerFacade.loginSuccess();
        //verify
        verify(this.profileTransactionService, never()).sendLoginEvent(any(CustomerModel.class), eq(CONSENT_REFERENCE_ID), anyString(), anyString());
    }

    @Test
    public void testLoginSuccessWhenUserModelConsentReferenceIsNotNull() {
        //given
        Cookie[] cookies = new Cookie[0];
        when(this.httpServletRequest.getCookies()).thenReturn(cookies);
        when(this.consentService.getConsentReferenceFromSession()).thenReturn(null);
        when(this.userModel.getConsentReference()).thenReturn(CONSENT_REFERENCE_ID);
        //when
        this.profileCustomerFacade.loginSuccess();
        //verify
        verify(this.profileTransactionService, times(1)).sendLoginEvent(any(CustomerModel.class), eq(CONSENT_REFERENCE_ID), anyString(), anyString());
    }
}