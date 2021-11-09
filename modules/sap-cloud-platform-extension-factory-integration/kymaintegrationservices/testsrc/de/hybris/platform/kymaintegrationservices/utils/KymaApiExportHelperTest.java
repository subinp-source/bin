/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.utils;

import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.getDestinationId;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.isUrlsEqualIgnoringQuery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.model.EndpointModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;

import java.net.URISyntaxException;

import org.junit.Test;


@UnitTest
public class KymaApiExportHelperTest
{
	@Test
	public void getDestinationIdTest()
	{
		final ExposedDestinationModel destination = mock(ExposedDestinationModel.class);
		final EndpointModel endpoint = mock(EndpointModel.class);
		when(endpoint.getVersion()).thenReturn("testVersion");
		when(destination.getId()).thenReturn("testId");
		when(destination.getEndpoint()).thenReturn(endpoint);
		final String expectedDestinationId = destination.getId() + "-" + endpoint.getVersion();
		assertEquals(expectedDestinationId, getDestinationId(destination));
	}

	@Test
	public void isUrlsEqualIgnoringQueryTest() throws URISyntaxException
	{
		assertTrue(isUrlsEqualIgnoringQuery("https://myurl.com", "https://myurl.com"));
		assertTrue(isUrlsEqualIgnoringQuery("https://myurl.com", "https://myurl.com/"));
		assertTrue(isUrlsEqualIgnoringQuery("https://myurl.com/path", "https://myurl.com/path/"));
		assertFalse(isUrlsEqualIgnoringQuery("https://myurl.com", "http://myurl.com"));
		assertTrue(isUrlsEqualIgnoringQuery("https://myurl.com", "https://myurl.com?arg1=a&arg2=b"));
		assertTrue(isUrlsEqualIgnoringQuery("https://myurl1.com", "https://myurl1.com"));
	}
}
