/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.header;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LocationHeaderResourceTest
{
	public static final String INVALID = "invalid";
	public static final String IDENTIFIER = "test-id";
	public static final String REQUEST_URL = "/api/someresource";
	public static final String RESPONSE_URL = "/api/someresource/";

	private final LocationHeaderResource locationHeaderResource = new LocationHeaderResource();

	@Mock
	private HttpServletRequest request;

	@Before
	public void setUp()
	{
		when(request.getRequestURL()).thenReturn(new StringBuffer(REQUEST_URL));
	}

	@Test
	public void shouldReturnLocationResourceWithIdentifier()
	{
		final String url = locationHeaderResource.createLocationForChildResource(request, IDENTIFIER);
		assertEquals(RESPONSE_URL + IDENTIFIER, url);
	}

	@Test
	public void shouldReturnLocationResourceWithoutIdentifier()
	{
		final String url = locationHeaderResource.createLocationForChildResource(request, null);
		assertEquals(RESPONSE_URL, url);
	}
}
