/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.services.constants.SapproductconfigservicesConstants;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProductConfigurationOCCSessionAttributesFilterTest
{
	private final ProductConfigurationOCCSessionAttributesFilter classUnderTest = new ProductConfigurationOCCSessionAttributesFilter();
	@Mock
	private HttpServletRequest servletRequest;
	@Mock
	private HttpServletResponse servletResponse;
	@Mock
	private FilterChain filterChain;
	@Mock
	private SessionService sessionService;

	@Before
	public void initialize()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest.setSessionService(sessionService);
	}

	@Test
	public void testDoFilterInternal() throws ServletException, IOException
	{
		classUnderTest.doFilterInternal(servletRequest, servletResponse, filterChain);
		verify(sessionService).setAttribute(SapproductconfigservicesConstants.SESSION_NOT_BOUND_TO_CONFIGURATIONS, true);
	}

	@Test
	public void testSessionService()
	{
		assertEquals(sessionService, classUnderTest.getSessionService());
	}
}
