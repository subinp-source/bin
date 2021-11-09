/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.filter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationservices.handlers.PersonalizationHandler;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CxOccPersonalizationFilterTest
{
	@Mock
	private HttpServletRequest httpServletRequest;
	@Mock
	private HttpServletResponse httpServletResponse;
	@Mock
	private FilterChain filterChain;
	@Mock
	PersonalizationHandler personalizationHandler;

	private CxOccPersonalizationFilter filter;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		filter = new CxOccPersonalizationFilter();
		filter.setPersonalizationHandler(personalizationHandler);

	}

	@Test
	public void doFilterInternalShouldCallDoFilterWhenExceptionHappensInHandlePersonalization()
			throws ServletException, IOException
	{
		//given

		doThrow(new RuntimeException()).when(personalizationHandler).handlePersonalization(httpServletRequest, httpServletResponse);

		//when

		filter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

		//then

		verify(filterChain, Mockito.times(1)).doFilter(any(), any());
	}

	@Test
	public void doFilterInternalShouldCallDoFilterWithoutExceptionInHandlePersonalization() throws ServletException, IOException
	{
		//when

		filter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

		//then

		verify(filterChain, Mockito.times(1)).doFilter(any(), any());
	}

}
