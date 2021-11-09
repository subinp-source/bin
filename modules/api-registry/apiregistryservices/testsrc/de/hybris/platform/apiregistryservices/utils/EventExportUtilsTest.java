/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.Config;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class EventExportUtilsTest
{
	private static final String ARGUMENT_BODY = "/check/test/";
	private static final String TEST_DEPLOYMENT_END_POINT="deployment.end.point.test";
	private static final String TEST_DEPLOYMENT_END_POINT_URL="https://localhost.some.url";

	private static final String TEST_PROPERTY = "test.property.value";
	private static final String TEST_PROPERTY_TWO = "test.property.valuetwo";
	private static final String TEST_PROPERTY_VALUE = "testValue";
	private static final String TEST_PROPERTY_TWO_VALUE = "testValueTwo";

	private static final String ALLOWED_URL_PROTOCOLS = "apiregistryservices.allowedUrlProtocols";

	@Before
	public void before()
	{
		Config.setParameter(ALLOWED_URL_PROTOCOLS, "https,foo");
	}

	@Test
	public void convertUrlSuccess()
	{
		Config.setParameter(TEST_DEPLOYMENT_END_POINT, TEST_DEPLOYMENT_END_POINT_URL);
		final String url = "{"+TEST_DEPLOYMENT_END_POINT+"}/check/test";
		final String urlWithDeploymentAddress = EventExportUtils.getUrlWithDeploymentAddress(url);
		final String expectedUrl = TEST_DEPLOYMENT_END_POINT_URL+"/check/test";
		assertTrue(expectedUrl.equals(urlWithDeploymentAddress));

	}

	@Test(expected = ConversionException.class)
	public void convertUrlWithException()
	{
		final String url = "{deployment/check/test";
		EventExportUtils.getUrlWithDeploymentAddress(url);
	}

	@Test
	public void convertUrlWithOpenCurlyBracesMissing()
	{
		final String url = TEST_DEPLOYMENT_END_POINT+"}/check/test";
		final String urlAfterReplacement = EventExportUtils.getUrlWithDeploymentAddress(url);
		assertTrue(url.equals(urlAfterReplacement));
	}

	@Test(expected = ConversionException.class)
	public void convertUrlWithClosedCurlyBracesMissing()
	{
		final String url = "{"+TEST_DEPLOYMENT_END_POINT+"/check/test";
		final String urlAfterReplacement = EventExportUtils.getUrlWithDeploymentAddress(url);
		assertNull(urlAfterReplacement);
	}

	@Test
	public void convertUrlWithValidUrlAsInput()
	{
		final String url = "https://localhost:xyz/check/test";
		final String urlAfterReplacement = EventExportUtils.getUrlWithDeploymentAddress(url);
		assertTrue(url.equals(urlAfterReplacement));
	}

	@Test
	public void replacePlaceholders()
	{
		Config.setParameter(TEST_PROPERTY, TEST_PROPERTY_VALUE);
		Config.setParameter(TEST_PROPERTY_TWO, TEST_PROPERTY_TWO_VALUE);
		String argument = "{" + TEST_PROPERTY + "}" + ARGUMENT_BODY;
		String convertedArgument = EventExportUtils.replacePropertyPlaceholders(argument);
		String expectedResult = TEST_PROPERTY_VALUE + ARGUMENT_BODY;
		assertTrue(expectedResult.equals(convertedArgument));

		argument = ARGUMENT_BODY + "{" + TEST_PROPERTY + "}";
		convertedArgument = EventExportUtils.replacePropertyPlaceholders(argument);
		expectedResult = ARGUMENT_BODY + TEST_PROPERTY_VALUE;
		assertTrue(expectedResult.equals(convertedArgument));

		argument = "{" + TEST_PROPERTY + "}" + ARGUMENT_BODY + "{" + TEST_PROPERTY + "}";
		convertedArgument = EventExportUtils.replacePropertyPlaceholders(argument);
		expectedResult = TEST_PROPERTY_VALUE + ARGUMENT_BODY + TEST_PROPERTY_VALUE;
		assertTrue(expectedResult.equals(convertedArgument));

		argument = ARGUMENT_BODY;
		convertedArgument = EventExportUtils.replacePropertyPlaceholders(argument);
		expectedResult = ARGUMENT_BODY;
		assertTrue(expectedResult.equals(convertedArgument));

		argument = ARGUMENT_BODY + "{no.such.property}";
		convertedArgument = EventExportUtils.replacePropertyPlaceholders(argument);
		expectedResult = ARGUMENT_BODY + "{no.such.property}";
		assertTrue(expectedResult.equals(convertedArgument));

		argument = "{" + TEST_PROPERTY + "}" + ARGUMENT_BODY + "{" + TEST_PROPERTY_TWO + "}";
		convertedArgument = EventExportUtils.replacePropertyPlaceholders(argument);
		expectedResult = TEST_PROPERTY_VALUE + ARGUMENT_BODY + TEST_PROPERTY_TWO_VALUE;
		assertTrue(expectedResult.equals(convertedArgument));

	}

	@Test
	public void validdateHTTPUrl()
	{
		final String url = "https://localhost.some.url";
		assertTrue(EventExportUtils.isUrlValid(url));
	}

	@Test
	public void validateCustomProtocolUrl()
	{
		final String url = "foo://localhost.some.url";
		assertTrue(EventExportUtils.isUrlValid(url));
	}
}
