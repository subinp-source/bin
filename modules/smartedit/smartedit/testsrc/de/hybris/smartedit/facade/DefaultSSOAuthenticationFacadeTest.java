/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.facade;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.webservicescommons.oauth2.token.provider.HybrisOAuthTokenServices;
import de.hybris.smartedit.facade.impl.DefaultSSOAuthenticationFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;

import java.util.Collection;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultSSOAuthenticationFacadeTest {

    @Mock
    private HybrisOAuthTokenServices hybrisOAuthTokenServices;
    @Mock
    private OAuth2RequestFactory smarteditOAuth2RequestFactory;
    @Mock
    private ClientDetailsService clientDetailsService;
    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private DefaultSSOAuthenticationFacade defaultSSOAuthenticationFacade;


    private String oAuth2ClientId = "oAuth2ClientId";
    final String userId = "userId";

    @Mock
    private ClientDetails clientDetails;
    @Mock
    private TokenRequest storedRequest;
    @Mock
    private OAuth2Request oAuth2Request;
    @Mock
    private UserDetails userDetails;
    @Mock
    private OAuth2AccessToken oAuth2AccessToken;

    private Collection<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList("someRole");

    @Captor
    private ArgumentCaptor<Map> mapCaptor;

    @Captor
    private ArgumentCaptor<OAuth2Authentication> oAuth2AuthenticationCaptor;

    @Test
    public void willGeneratedOAuth2AccessTokenFromClientIdAndUserId()
    {
        when(clientDetailsService.loadClientByClientId((oAuth2ClientId))).thenReturn(clientDetails);

        when(smarteditOAuth2RequestFactory.createTokenRequest(mapCaptor.capture(), Matchers.eq(clientDetails))).thenReturn(storedRequest);

        when(storedRequest.createOAuth2Request(clientDetails)).thenReturn(oAuth2Request);

        when(userDetailsService.loadUserByUsername(userId)).thenReturn(userDetails);

        doReturn(grantedAuthorities).when(userDetails).getAuthorities();

        when(hybrisOAuthTokenServices.createAccessToken(oAuth2AuthenticationCaptor.capture())).thenReturn(oAuth2AccessToken);

        OAuth2AccessToken token = defaultSSOAuthenticationFacade.generateOAuthTokenForUser(oAuth2ClientId, userId);

        assertThat(token, is(oAuth2AccessToken));

        OAuth2Authentication oAuth2Authentication = oAuth2AuthenticationCaptor.getValue();

        assertThat(oAuth2Authentication.getOAuth2Request(), is(oAuth2Request));
        assertThat(oAuth2Authentication.getUserAuthentication().getAuthorities(), is(grantedAuthorities));
        assertThat(oAuth2Authentication.getUserAuthentication().getPrincipal(), is(userId));

        assertThat(mapCaptor.getValue().size(), is(0));


    }
}