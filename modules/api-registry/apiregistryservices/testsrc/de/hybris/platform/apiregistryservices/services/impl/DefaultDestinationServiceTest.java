/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.exceptions.DestinationNotFoundException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;

import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.util.Config;

import java.net.SocketTimeoutException;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@UnitTest
public class DefaultDestinationServiceTest
{

	private static final String ERROR_CODES = "apiregistryservices.testConsumedDestinationUrl.httpstatus.error.codes";
	private static final String TEST_URL = "testing url";
	private static final String TEST_HTTP_STATUS_ERROR_CODES = "400,404,403,407,401,500";

	private DefaultDestinationService<AbstractDestinationModel> destinationService = new DefaultDestinationService<>();

	@Mock
	private RestTemplate restTemplate;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private ConsumedDestinationModel consumedDestination;

	private ExposedDestinationModel exposedDestination;

	@Before
	public void before()
	{
		MockitoAnnotations.initMocks(this);
		Config.setParameter(ERROR_CODES, TEST_HTTP_STATUS_ERROR_CODES);
		consumedDestination = new ConsumedDestinationModel();
		consumedDestination.setUrl(TEST_URL);

		exposedDestination = new ExposedDestinationModel();
		exposedDestination.setUrl(TEST_URL);
	}

	@Test
	public void pingExposedDestinationUrlBadRequestException() throws DestinationNotFoundException
	{
		final DefaultDestinationService<AbstractDestinationModel> destinationServiceSpy = spy(destinationService);
		try
		{
			doReturn(restTemplate).when(destinationServiceSpy).getRestTemplate(any());
		}
		catch (CredentialException e)
		{
			throw new DestinationNotFoundException(e.getMessage(), e);
		}

		when(restTemplate.exchange(Matchers.anyString(), Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
		expectedException.expect(DestinationNotFoundException.class);
		expectedException.expectCause(IsInstanceOf.instanceOf(HttpClientErrorException.class));

		destinationServiceSpy.testDestinationUrl(exposedDestination);
	}

	@Test
	public void pingExposedDestinationUrlUnauthorizedException() throws DestinationNotFoundException
	{
		final DefaultDestinationService<AbstractDestinationModel> destinationServiceSpy = spy(destinationService);
		try
		{
			doReturn(restTemplate).when(destinationServiceSpy).getRestTemplate(any());
		}
		catch (CredentialException e)
		{
			throw new DestinationNotFoundException(e.getMessage(), e);
		}

		when(restTemplate.exchange(Matchers.anyString(), Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any())).thenThrow(new HttpServerErrorException(HttpStatus.UNAUTHORIZED));
		expectedException.expect(DestinationNotFoundException.class);
		expectedException.expectCause(IsInstanceOf.instanceOf(HttpServerErrorException.class));

		destinationServiceSpy.testDestinationUrl(exposedDestination);
	}

	@Test
	public void pingExposedDestinationUrlForbiddenException() throws DestinationNotFoundException
	{
		final DefaultDestinationService<AbstractDestinationModel> destinationServiceSpy = spy(destinationService);
		try
		{
			doReturn(restTemplate).when(destinationServiceSpy).getRestTemplate(any());
		}
		catch (CredentialException e)
		{
			throw new DestinationNotFoundException(e.getMessage(), e);
		}

		when(restTemplate.exchange(Matchers.anyString(), Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any())).thenThrow(new HttpServerErrorException(HttpStatus.FORBIDDEN));
		expectedException.expect(DestinationNotFoundException.class);
		expectedException.expectCause(IsInstanceOf.instanceOf(HttpServerErrorException.class));

		destinationServiceSpy.testDestinationUrl(exposedDestination);
	}

	@Test
	public void pingExposedDestinationUrlSuccess() throws DestinationNotFoundException
	{
		final DefaultDestinationService<AbstractDestinationModel> destinationServiceSpy = spy(destinationService);
		try
		{
			doReturn(restTemplate).when(destinationServiceSpy).getRestTemplate(any());
		}
		catch (CredentialException e)
		{
			throw new DestinationNotFoundException(e.getMessage(), e);
		}

		when(restTemplate.exchange(Matchers.anyString(), Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any())).thenReturn(new ResponseEntity<String>(HttpStatus.OK));

		destinationServiceSpy.testDestinationUrl(exposedDestination);
	}

	@Test
	public void pingConsumedDestinationUrlNotFoundTest() throws DestinationNotFoundException
	{
		final DefaultDestinationService<AbstractDestinationModel> destinationServiceSpy = spy(destinationService);
		try
		{
			doReturn(restTemplate).when(destinationServiceSpy).getRestTemplate(any());
		}
		catch (CredentialException e)
		{
			throw new DestinationNotFoundException(e.getMessage(), e);
		}

		when(restTemplate.exchange(Matchers.anyString(), Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		expectedException.expect(DestinationNotFoundException.class);
		expectedException.expectCause(IsInstanceOf.instanceOf(HttpClientErrorException.class));

		destinationServiceSpy.testDestinationUrl(consumedDestination);
	}

	@Test
	public void pingConsumedDestinationUrlTimedOut() throws DestinationNotFoundException
	{
		final DefaultDestinationService<AbstractDestinationModel> destinationServiceSpy = spy(destinationService);
		try
		{
			doReturn(restTemplate).when(destinationServiceSpy).getRestTemplate(any());
		}
		catch (CredentialException e)
		{
			throw new DestinationNotFoundException(e.getMessage(), e);
		}

		when(restTemplate.exchange(Matchers.anyString(), Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any())).thenThrow(new ResourceAccessException("timed out", new SocketTimeoutException()));
		expectedException.expect(DestinationNotFoundException.class);
		expectedException.expectCause(IsInstanceOf.instanceOf(SocketTimeoutException.class));

		destinationServiceSpy.testDestinationUrl(consumedDestination);
	}

	@Test
	public void pingConsumedDestinationUrlSuccess() throws DestinationNotFoundException
	{
		final DefaultDestinationService<AbstractDestinationModel> destinationServiceSpy = spy(destinationService);
		try
		{
			doReturn(restTemplate).when(destinationServiceSpy).getRestTemplate(any());
		}
		catch (CredentialException e)
		{
			throw new DestinationNotFoundException(e.getMessage(), e);
		}
		when(restTemplate.exchange(Matchers.anyString(), Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any())).thenReturn(new ResponseEntity<String>(HttpStatus.OK));

		destinationServiceSpy.testDestinationUrl(consumedDestination);
	}

	@Test
	public void pingConsumedDestinationUrlInternalServerException() throws DestinationNotFoundException
	{
		final DefaultDestinationService<AbstractDestinationModel> destinationServiceSpy = spy(destinationService);
		try
		{
			doReturn(restTemplate).when(destinationServiceSpy).getRestTemplate(any());
		}
		catch (CredentialException e)
		{
			throw new DestinationNotFoundException(e.getMessage(), e);
		}

		when(restTemplate.exchange(Matchers.anyString(), Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any())).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
		expectedException.expect(DestinationNotFoundException.class);
		expectedException.expectCause(IsInstanceOf.instanceOf(HttpServerErrorException.class));

		destinationServiceSpy.testDestinationUrl(consumedDestination);
	}

}
