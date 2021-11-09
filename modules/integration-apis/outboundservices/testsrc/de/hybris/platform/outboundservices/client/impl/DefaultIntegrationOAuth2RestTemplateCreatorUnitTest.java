/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundservices.client.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.outboundservices.cache.RestTemplateCache;
import de.hybris.platform.outboundservices.cache.impl.DefaultDestinationRestTemplateCacheKey;
import de.hybris.platform.outboundservices.cache.impl.DestinationOauthRestTemplateId;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.http.OAuth2ErrorHandler;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultIntegrationOAuth2RestTemplateCreatorUnitTest
{
	private static final String DESTINATION_URL = "https://localhost:9002/odata2webservices/InboundProduct/Products";
	private static final String OAUTH_URL = "https://localhost:9002/authorizationserver/oauth/token";
	private static final String CLIENT_ID = "admin";
	private static final String CLIENT_SECRET = "nimda";

	@InjectMocks
	private DefaultIntegrationOAuth2RestTemplateCreator oAuth2RestTemplateCreator;

	@Mock
	private ClientHttpRequestFactory clientHttpRequestFactory;
	@Mock
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
	@Mock
	private ClientHttpRequestInterceptor interceptor;
	@Mock
	private RestTemplateCache restTemplateCache;
	@Mock
	private OAuth2ResourceDetailsGeneratorFactory oAuth2ResourceDetailsGeneratorFactory;
	@Mock
	private ExposedOAuth2ResourceDetailsGenerator exposedOAuth2ResourceDetailsGenerator;

	@Before
	public void setup() throws IOException
	{
		final ClientHttpRequest request = httpRequest(httpResponse());
		doReturn(request).when(clientHttpRequestFactory).createRequest(any(URI.class), any(HttpMethod.class));

		oAuth2RestTemplateCreator.setMessageConverters(Collections.singletonList(mappingJackson2HttpMessageConverter));
		oAuth2RestTemplateCreator.setRequestInterceptors(Collections.singletonList(interceptor));

		doReturn(true).when(oAuth2ResourceDetailsGeneratorFactory).isApplicable(any(ExposedOAuthCredentialModel.class));
		doReturn(true).when(oAuth2ResourceDetailsGeneratorFactory).isApplicable(any(ConsumedOAuthCredentialModel.class));
		doReturn(exposedOAuth2ResourceDetailsGenerator).when(oAuth2ResourceDetailsGeneratorFactory).getGenerator(any(ExposedOAuthCredentialModel.class));

		doReturn(resourecDetails()).when(exposedOAuth2ResourceDetailsGenerator).createResourceDetails(any());
	}

	private ClientHttpRequest httpRequest(final ClientHttpResponse response) throws IOException
	{
		final ClientHttpRequest request = mock(ClientHttpRequest.class);
		when(request.getHeaders()).thenReturn(new HttpHeaders());
		when(request.getBody()).thenReturn(mock(OutputStream.class));
		when(request.execute()).thenReturn(response);
		return request;
	}

	private ClientHttpResponse httpResponse() throws IOException
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);

		final ClientHttpResponse response = mock(ClientHttpResponse.class);
		when(response.getHeaders()).thenReturn(headers);
		when(response.getBody()).thenReturn(new ByteArrayInputStream("access_token=123".getBytes()));
		return response;
	}

	@Test
	public void shouldCreateRestTemplateIfItIsNotAvailableInCache()
	{
		final RestOperations restOperations = oAuth2RestTemplateCreator.create(consumedDestination());

		assertThat(restOperations)
				.isExactlyInstanceOf(OAuth2RestTemplate.class)
				.hasFieldOrPropertyWithValue("resource.clientId", CLIENT_ID)
				.hasFieldOrPropertyWithValue("resource.clientSecret", CLIENT_SECRET)
				.hasFieldOrPropertyWithValue("resource.accessTokenUri", OAUTH_URL);

		final OAuth2RestTemplate restTemplate = (OAuth2RestTemplate) restOperations;
		assertThat(restTemplate.getMessageConverters()).contains(mappingJackson2HttpMessageConverter);
		assertThat(restTemplate.getInterceptors()).contains(interceptor);
		assertThat(restTemplate.getErrorHandler()).isInstanceOf(OAuth2ErrorHandler.class);
	}

	@Test
	public void doesNotCreateRestTemplateIfItIsAvailableInCache()
	{
		final ConsumedDestinationModel destination = consumedDestination();
		final RestTemplate cachedTemplate = givenCacheContainsTemplateForDestination(destination);

		final RestOperations createdTemplate = oAuth2RestTemplateCreator.create(destination);

		assertThat(createdTemplate).isSameAs(cachedTemplate);
	}

	@Test
	public void createRestTemplateIfCacheContainsTemplatesOnlyForDifferentDestination()
	{
		final ConsumedDestinationModel destination1 = consumedDestination();
		final ConsumedDestinationModel destination2 = consumedDestination();
		final RestTemplate cachedTemplate = givenCacheContainsTemplateForDestination(destination1);

		final RestOperations createdTemplate = oAuth2RestTemplateCreator.create(destination2);

		assertThat(createdTemplate).isNotSameAs(cachedTemplate);
	}

	@Test
	public void shouldThrowUnsupportedRestTemplateExceptionWhenConsumedDestinationIsNotOAuth()
	{
		doThrow(UnsupportedRestTemplateException.class).when(oAuth2ResourceDetailsGeneratorFactory).isApplicable(any(BasicCredentialModel.class));

		final ConsumedDestinationModel destination = consumedDestination(mock(BasicCredentialModel.class));

		assertThatThrownBy(() -> oAuth2RestTemplateCreator.create(destination))
				.isInstanceOf(UnsupportedRestTemplateException.class);
	}

	@Test
	public void mayNotHaveInterceptors()
	{
		oAuth2RestTemplateCreator.setRequestInterceptors(null);

		final OAuth2RestTemplate restTemplate = (OAuth2RestTemplate) oAuth2RestTemplateCreator.create(consumedDestination());

		assertThat(restTemplate.getInterceptors()).isEmpty();
	}

	private RestTemplate givenCacheContainsTemplateForDestination(final ConsumedDestinationModel dest)
	{
		final RestTemplate cachedTemplate = mock(RestTemplate.class);
		doReturn(cachedTemplate).when(restTemplateCache).get(cacheKey(dest));
		return cachedTemplate;
	}

	private static DefaultDestinationRestTemplateCacheKey cacheKey(final ConsumedDestinationModel destination)
	{
		return DefaultDestinationRestTemplateCacheKey.from(DestinationOauthRestTemplateId.from(destination));
	}

	private static ConsumedDestinationModel consumedDestination()
	{
		final OAuthClientDetailsModel client = mock(OAuthClientDetailsModel.class);
		when(client.getOAuthUrl()).thenReturn(OAUTH_URL);
		when(client.getClientId()).thenReturn(CLIENT_ID);

		final ExposedOAuthCredentialModel credential = mock(ExposedOAuthCredentialModel.class);
		when(credential.getOAuthClientDetails()).thenReturn(client);
		when(credential.getPassword()).thenReturn(CLIENT_SECRET);

		return consumedDestination(credential);
	}

	private static OAuth2ProtectedResourceDetails resourecDetails()
	{
		ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
		resourceDetails.setAccessTokenUri(OAUTH_URL);
		resourceDetails.setClientId(CLIENT_ID);
		resourceDetails.setClientSecret(CLIENT_SECRET);

		return resourceDetails;
	}

	private static ConsumedDestinationModel consumedDestination(final AbstractCredentialModel credential)
	{
		final ConsumedDestinationModel destination = mock(ConsumedDestinationModel.class);
		when(destination.getUrl()).thenReturn(DESTINATION_URL);
		when(destination.getCredential()).thenReturn(credential);
		return destination;
	}
}
