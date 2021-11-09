/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.services.impl;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.kymaintegrationservices.exceptions.SSLContextFactoryCreationException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import java.io.IOException;
import java.security.cert.CertificateRevokedException;
import java.util.Arrays;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import static org.mockito.Mockito.spy;

import static org.junit.Assert.assertTrue;


@Ignore
@UnitTest
public class SSLContextFactoryServiceTest
{
	private static final String REVOKED_CERTIFICATE_URL = "https://revoked.badssl.com/";

	private HttpEntity<Object> httpEntity;

	@Before
	public void setUp()
	{
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Arrays.asList(MediaType.ALL));
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpEntity = new HttpEntity<>(httpHeaders);
	}


	@Test
	public void testCertificateWithTrustManagers() throws SSLContextFactoryCreationException
	{
		final DefaultSSLContextFactoryService spy = spy(DefaultSSLContextFactoryService.class);
		doReturn(new KeyManager[0]).when(spy).createKeyManagerFactory(any(), any());

		final SSLContext context = spy.createSSLContext(null, null);

		final HttpClient client = HttpClients.custom().setSSLContext(context).build();

		final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				client);

		final RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		try
		{
			restTemplate.exchange(REVOKED_CERTIFICATE_URL, HttpMethod.GET, httpEntity, String.class);
		}
		catch (ResourceAccessException e)
		{
			assertTrue(e.getRootCause() instanceof CertificateRevokedException);
		}

	}


	@Test
	public void testCertificateWithOutTrustManagers() throws SSLContextFactoryCreationException, IOException
	{
		final DefaultSSLContextFactoryService spy = spy(DefaultSSLContextFactoryService.class);
		doReturn(new TrustManager[0]).when(spy).createTrustManagerFactory();
		doReturn(new KeyManager[0]).when(spy).createKeyManagerFactory(any(), any());

		final SSLContext context = spy.createSSLContext(null, null);

		final HttpClient client = HttpClients.custom().setSSLContext(context).build();

		final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				client);

		final RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		restTemplate.exchange(REVOKED_CERTIFICATE_URL, HttpMethod.GET, httpEntity, String.class);
	}
}
