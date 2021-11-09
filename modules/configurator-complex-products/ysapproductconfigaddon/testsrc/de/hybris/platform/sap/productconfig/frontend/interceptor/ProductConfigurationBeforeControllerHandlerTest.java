/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.frontend.interceptor;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.sap.productconfig.facades.ConfigurationExpertModeFacade;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.annotation.RequestMethod;


@UnitTest
public class ProductConfigurationBeforeControllerHandlerTest
{

	public static final String CONFIG_URL = "/configuratorPage/" + ConfiguratorType.CPQCONFIGURATOR.toString();
	public static final String HTTP_DUMMY_URL = "http://dummy.com/something";

	@Mock
	private HttpServletRequest request;

	@Mock
	private ConfigurationExpertModeFacade configurationExpertModeFacade;

	@InjectMocks
	private ProductConfigurationBeforeControllerHandler classUnderTest;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBeforeController() throws Exception
	{
		when(request.getRequestURI()).thenReturn(CONFIG_URL);
		when(request.getMethod()).thenReturn(RequestMethod.GET.name());
		when(request.getQueryString()).thenReturn("someotherParameter=abd&expmode=TRUE");

		assertTrue(classUnderTest.beforeController(request, null, null));

		verify(configurationExpertModeFacade).enableExpertMode();
		verify(configurationExpertModeFacade, times(0)).disableExpertMode();
	}

	@Test
	public void testBeforeControllerNotExecuted() throws Exception
	{
		when(request.getRequestURI()).thenReturn(HTTP_DUMMY_URL);
		when(request.getMethod()).thenReturn(RequestMethod.GET.name());

		assertTrue(classUnderTest.beforeController(request, null, null));

		when(request.getRequestURI()).thenReturn(HTTP_DUMMY_URL);
		when(request.getMethod()).thenReturn(RequestMethod.POST.name());

		assertTrue(classUnderTest.beforeController(request, null, null));

		when(request.getRequestURI()).thenReturn(CONFIG_URL);
		when(request.getMethod()).thenReturn(RequestMethod.POST.name());

		assertTrue(classUnderTest.beforeController(request, null, null));

		verify(configurationExpertModeFacade, times(0)).enableExpertMode();
		verify(configurationExpertModeFacade, times(0)).disableExpertMode();
	}

	@Test
	public void testIsProductConfigCall()
	{
		when(request.getRequestURI()).thenReturn(CONFIG_URL);
		assertTrue(classUnderTest.isProductConfigCall(request));
	}

	@Test
	public void testIsProductConfigCallNotProductConfig()
	{
		when(request.getRequestURI()).thenReturn(HTTP_DUMMY_URL);
		assertFalse(classUnderTest.isProductConfigCall(request));
	}

	@Test
	public void testIsGetMethod()
	{
		when(request.getMethod()).thenReturn(RequestMethod.GET.name());
		assertTrue(classUnderTest.isGetMethod(request));
	}

	@Test
	public void testIsGetMethodNotGet()
	{
		when(request.getMethod()).thenReturn(RequestMethod.POST.name());
		assertFalse(classUnderTest.isGetMethod(request));
	}

	@Test
	public void testHandleExpertModeEnableNoParameter()
	{
		classUnderTest.handleExpertMode("someotherParameter=abd&someMore=TRUE");
		classUnderTest.handleExpertMode(null);

		verify(configurationExpertModeFacade, times(0)).enableExpertMode();
		verify(configurationExpertModeFacade, times(0)).disableExpertMode();
	}

	@Test
	public void testHandleExpertModeEnable()
	{
		classUnderTest.handleExpertMode("someotherParameter=abd&expmode=TRUE");

		verify(configurationExpertModeFacade).enableExpertMode();
		verify(configurationExpertModeFacade, times(0)).disableExpertMode();
	}

	@Test
	public void testHandleExpertModeDisable()
	{
		classUnderTest.handleExpertMode("someotherParameter=abd&expmode=false");

		verify(configurationExpertModeFacade, times(0)).enableExpertMode();
		verify(configurationExpertModeFacade).disableExpertMode();
	}


	@Test
	public void testIsExpModeNoParameter()
	{
		assertFalse(classUnderTest.isExpMode("someotherParameter=abd&someMore=TRUE", "true"));
	}

	@Test
	public void testIsExpModeEnable()
	{
		assertTrue(classUnderTest.isExpMode("someotherParameter=abd&expmode=TRUE", "true"));
	}

	@Test
	public void testIsExpModeDisable()
	{
		assertTrue(classUnderTest.isExpMode("someotherParameter=abd&expmode=False", "false"));
	}
}
