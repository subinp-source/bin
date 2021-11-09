/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.utils;

import static de.hybris.platform.kymaintegrationservices.utils.KymaHttpHelper.getDefaultHeaders;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@UnitTest
public class KymaHttpHelperTest
{
	@Test
	public void testDefaultHeaders()
	{
		final HttpHeaders headers = getDefaultHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		assertEquals(Arrays.asList(MediaType.ALL), headers.getAccept());
		assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
	}
}
