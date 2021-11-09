/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.occ.impl;

import static de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants.PERSONALIZATION_DEFAULT_ID_COOKIE;
import static de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants.PERSONALIZATION_DEFAULT_ID_HEADER;
import static de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants.PERSONALIZATION_DEFAULT_TIME_COOKIE;
import static de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants.PERSONALIZATION_DEFAULT_TIME_HEADER;
import static de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants.PERSONALIZATION_ID_COOKIE;
import static de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants.PERSONALIZATION_ID_HEADER;
import static de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants.PERSONALIZATION_TIME_COOKIE;
import static de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants.PERSONALIZATION_TIME_HEADER;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.stub.MockTimeService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCxOccAttributesStrategyTest
{
	private static final String PERSONALIZATION_ID = "personalizationId";
	private static final String CUSTOM_PERSONALIZATION_ID_HEADER = "customHeaderPersonalizationId";
	private static final Long PERSONALIZATION_TIME = 12345L;
	private static final String CUSTOM_PERSONALIZATION_TIME_HEADER = "customHeaderPersonalizationTime";

	private final TimeService timeService = new MockTimeService();

	@Mock
	private ConfigurationService configurationService;
	@Mock
	private CxConfigurationService cxConfigurationService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	Configuration configuration;

	@Mock
	CxConfigModel configModel;

	private final DefaultCxOccAttributesStrategy defaultCxOccAttributesStrategy = new DefaultCxOccAttributesStrategy();

	@Before
	public void setupTest()
	{
		MockitoAnnotations.initMocks(this);

		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(cxConfigurationService.getConfiguration()).thenReturn(Optional.of(configModel));

		defaultCxOccAttributesStrategy.setConfigurationService(configurationService);
		defaultCxOccAttributesStrategy.setCxConfigurationService(cxConfigurationService);
		defaultCxOccAttributesStrategy.setTimeService(timeService);


		doReturn(PERSONALIZATION_DEFAULT_ID_HEADER)//
				.when(configuration).getString(PERSONALIZATION_ID_HEADER, PERSONALIZATION_DEFAULT_ID_HEADER);
		doReturn(PERSONALIZATION_DEFAULT_ID_COOKIE)//
				.when(configuration).getString(PERSONALIZATION_ID_COOKIE, PERSONALIZATION_DEFAULT_ID_COOKIE);
		doReturn(PERSONALIZATION_DEFAULT_TIME_HEADER)//
				.when(configuration).getString(PERSONALIZATION_TIME_HEADER, PERSONALIZATION_DEFAULT_TIME_HEADER);
		doReturn(PERSONALIZATION_DEFAULT_TIME_COOKIE)//
				.when(configuration).getString(PERSONALIZATION_TIME_COOKIE, PERSONALIZATION_DEFAULT_TIME_COOKIE);

		timeService.setCurrentTime(new Date(PERSONALIZATION_TIME));
	}

	protected void customPersonalizationIdHeader()
	{
		doReturn(CUSTOM_PERSONALIZATION_ID_HEADER)//
				.when(configuration).getString(PERSONALIZATION_ID_HEADER, PERSONALIZATION_DEFAULT_ID_HEADER);
		doReturn(PERSONALIZATION_ID).when(request).getHeader(CUSTOM_PERSONALIZATION_ID_HEADER);
	}

	protected void customPersonalizationTimeHeader()
	{
		doReturn(CUSTOM_PERSONALIZATION_TIME_HEADER)//
				.when(configuration).getString(PERSONALIZATION_TIME_HEADER, PERSONALIZATION_DEFAULT_TIME_HEADER);
		doReturn(PERSONALIZATION_TIME.toString()).when(request).getHeader(CUSTOM_PERSONALIZATION_TIME_HEADER);
	}

	protected void cookieIsEnabled()
	{
		when(configModel.getOccPersonalizationIdCookieEnabled()).thenReturn(Boolean.TRUE);
	}

	@Test
	public void shouldReturnEmptyPersonalizationIdIfRequestNull()
	{
		//when
		final Optional<String> personalizationId = defaultCxOccAttributesStrategy.readPersonalizationId(null);
		//then
		assertFalse(personalizationId.isPresent());
	}

	@Test
	public void shouldReturnEmptyPersonalizationIdIfRequestEmpty()
	{
		//when
		final Optional<String> personalizationId = defaultCxOccAttributesStrategy.readPersonalizationId(request);
		//then
		assertFalse(personalizationId.isPresent());
	}

	@Test
	public void shouldReadPersonalizationIdFromHeaderIfItsNotEmpty()
	{
		//given
		customPersonalizationIdHeader();

		//when
		final Optional<String> personalizationId = defaultCxOccAttributesStrategy.readPersonalizationId(request);
		//then
		assertTrue(personalizationId.isPresent() && PERSONALIZATION_ID.equals(personalizationId.get()));
	}


	@Test
	public void shouldNotSearchForIdCookieIfPersonalizationCookieDisabled()
	{
		//given

		//when
		final Optional<String> personalizationId = defaultCxOccAttributesStrategy.readPersonalizationId(request);

		//then
		verify(request, times(0)).getCookies();
	}

	@Test
	public void shouldSetIdCookieIfPersonalizationCookieEnabled()
	{
		//given
		customPersonalizationIdHeader();
		cookieIsEnabled();
		//when
		defaultCxOccAttributesStrategy.setPersonalizationId(PERSONALIZATION_ID, request, response);
		//then
		verify(response, times(1)).addCookie(any());
	}

	@Test
	public void shouldSetDefaultCookieIfPersonalizationIdCustomCookieEmpty()
	{
		//given
		cookieIsEnabled();

		//when
		defaultCxOccAttributesStrategy.setPersonalizationId(PERSONALIZATION_ID, request, response);
		//then
		verify(response, times(1)).addCookie(any());
	}


	@Test
	public void shouldNotSetIdCookieIfPersonalizationCookieDisabled()
	{
		//given
		customPersonalizationIdHeader();

		//when
		defaultCxOccAttributesStrategy.setPersonalizationId(PERSONALIZATION_ID, request, response);
		//then
		verify(response, times(0)).addCookie(any());
	}

	@Test
	public void shouldSetDefaultPersonalizationIdHeaderIfCustomIsEmpty()
	{
		//given

		//when
		defaultCxOccAttributesStrategy.setPersonalizationId(PERSONALIZATION_ID, request, response);
		//then
		verify(response, times(1)).setHeader(PERSONALIZATION_DEFAULT_ID_HEADER, PERSONALIZATION_ID);
	}

	////////////////////////////
	@Test
	public void shouldReturnEmptyPersonalizationTimeIfRequestNull()
	{
		//when
		final Optional<Long> time = defaultCxOccAttributesStrategy.readPersonalizationCalculationTime(null);
		//then
		assertFalse(time.isPresent());
	}

	@Test
	public void shouldReturnEmptyPersonalizationTimeIfRequestEmpty()
	{
		//when
		final Optional<Long> time = defaultCxOccAttributesStrategy.readPersonalizationCalculationTime(request);
		//then
		assertFalse(time.isPresent());
	}

	@Test
	public void shouldReadPersonalizationTimeFromHeaderIfItsNotEmpty()
	{
		//given
		customPersonalizationTimeHeader();

		//when
		final Optional<Long> time = defaultCxOccAttributesStrategy.readPersonalizationCalculationTime(request);
		//then
		assertTrue(time.isPresent());
		assertEquals(PERSONALIZATION_TIME, time.get());
	}


	@Test
	public void shouldNotSearchForTimeCookieIfPersonalizationCookieDisabled()
	{
		//given

		//when
		final Optional<Long> time = defaultCxOccAttributesStrategy.readPersonalizationCalculationTime(request);

		//then
		verify(request, times(0)).getCookies();
	}

	@Test
	public void shouldSetTimeCookieIfPersonalizationCookieEnabled()
	{
		//given
		customPersonalizationTimeHeader();
		cookieIsEnabled();
		//when
		defaultCxOccAttributesStrategy.setPersonalizationCalculationTime(request, response);
		//then
		verify(response, times(1)).addCookie(any());
	}

	@Test
	public void shouldSetDefaultCookieIfPersonalizationTimeCustomCookieEmpty()
	{
		//given
		cookieIsEnabled();

		//when
		defaultCxOccAttributesStrategy.setPersonalizationCalculationTime(request, response);
		//then
		verify(response, times(1)).addCookie(any());
	}


	@Test
	public void shouldNotSetCookieIfPersonalizationTimeCookieDisabled()
	{
		//given
		customPersonalizationTimeHeader();

		//when
		defaultCxOccAttributesStrategy.setPersonalizationCalculationTime(request, response);
		//then
		verify(response, times(0)).addCookie(any());
	}

	@Test
	public void shouldSetDefaultPersonalizationTimeHeaderIfCustomIsEmpty()
	{
		//given

		//when
		defaultCxOccAttributesStrategy.setPersonalizationCalculationTime(request, response);
		//then
		verify(response, times(1)).setHeader(PERSONALIZATION_DEFAULT_TIME_HEADER, PERSONALIZATION_TIME.toString());
	}
}
