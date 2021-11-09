/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.servicelayer.web;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.config.FastHashMapConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

@UnitTest
public class CustomMediaHeaderConfiguratorTest extends ServicelayerBaseTest
{
	CustomMediaHeaderConfigurator customMediaHeaderConfigurator;
	HttpServletResponse httpServletResponse;

	@Before
	public void setUp()
	{
		httpServletResponse = mock(HttpServletResponse.class);
		customMediaHeaderConfigurator = spy(CustomMediaHeaderConfigurator.class);
	}

	@Test
	public void testIfPreconfiguredHeadersAreSetToResponse()
	{
		when(customMediaHeaderConfigurator.getPreConfiguredHeaders()).thenReturn(dummyPreConfiguredHeaders());

		customMediaHeaderConfigurator.modifyResponseWithConfiguredHeaders(httpServletResponse);

		dummyPreConfiguredHeaders().forEach((header, value) ->
				verify(httpServletResponse, times(1)).setHeader(header, value));
	}

	@Test
	public void testIfPreconfiguredAndBlacklistedHeadersAreNotSetToResponse()
	{
		when(customMediaHeaderConfigurator.getPreConfiguredHeaders()).thenReturn(dummyPreConfiguredHeaders());
		when(customMediaHeaderConfigurator.getDisabledHeaders()).thenReturn(preConfigRelatedHeadersBlacklist());

		customMediaHeaderConfigurator.modifyResponseWithConfiguredHeaders(httpServletResponse);

		verify(httpServletResponse, never()).setHeader(anyString(), anyString());
	}

	@Test
	public void checkIfConfiguredHeadersAreSetToResponse()
	{
		when(customMediaHeaderConfigurator.getTenantConfig()).thenReturn(dummyConfig());

		customMediaHeaderConfigurator.modifyResponseWithConfiguredHeaders(httpServletResponse);

		Headers.getConfigHeaders().forEach((header, value) ->
				verify(httpServletResponse, times(1)).setHeader(header, value));
	}

	@Test
	public void testIfConfiguredAndBlacklistedHeadersAreNotSetToResponse()
	{
		when(customMediaHeaderConfigurator.getPreConfiguredHeaders()).thenReturn(new HashMap<>());
		when(customMediaHeaderConfigurator.getTenantConfig()).thenReturn(dummyConfig());
		when(customMediaHeaderConfigurator.getDisabledHeaders()).thenReturn(dummyConfigRelatedHeadersBlacklist());

		customMediaHeaderConfigurator.modifyResponseWithConfiguredHeaders(httpServletResponse);

		verify(httpServletResponse, never()).setHeader(anyString(), anyString());
	}

	@Test
	public void testIfConfigTakesPrecedenceOverPreConfigForResponseHeaders()
	{
		when(customMediaHeaderConfigurator.getTenantConfig()).thenReturn(dummyConfig());
		when(customMediaHeaderConfigurator.getPreConfiguredHeaders()).thenReturn(
				dummyPreConfiguredHeadersBasedOnDummyConfigWithDifferentValues());

		customMediaHeaderConfigurator.modifyResponseWithConfiguredHeaders(httpServletResponse);

		Headers.getConfigHeaders().forEach((header, value) ->
				verify(httpServletResponse, times(1)).setHeader(header, value));
	}

	private ConfigIntf dummyConfig()
	{
		final Map<String, String> properties = new HashMap<>();
		properties.put("media.set.header." + Headers.CUSTOM_HEADER_1.getHeaderName(), Headers.CUSTOM_HEADER_1.getHeaderValue());
		properties.put("media.set.header." + Headers.CUSTOM_HEADER_2.getHeaderName(), Headers.CUSTOM_HEADER_2.getHeaderValue());
		properties.put("media.set.header." + Headers.CUSTOM_HEADER_3.getHeaderName(), Headers.CUSTOM_HEADER_3.getHeaderValue());
		return new FastHashMapConfig(properties);
	}

	private Map<String, String> dummyPreConfiguredHeadersBasedOnDummyConfigWithDifferentValues()
	{
		final Map<String, String> dummyHeaders = new HashMap<>();
		dummyHeaders.put(Headers.CUSTOM_HEADER_1.getHeaderName(), "diffValue1");
		dummyHeaders.put(Headers.CUSTOM_HEADER_2.getHeaderName(), "diffValue2");
		dummyHeaders.put(Headers.CUSTOM_HEADER_3.getHeaderName(), "diffValue3");
		return dummyHeaders;
	}

	private Set<String> dummyConfigRelatedHeadersBlacklist()
	{
		return Headers.getConfigHeaders().keySet();
	}

	private Set<String> preConfigRelatedHeadersBlacklist()
	{
		return dummyPreConfiguredHeaders().keySet();
	}

	private Map<String, String> dummyPreConfiguredHeaders()
	{
		final Map<String, String> dummyHeaders = new HashMap<>();
		dummyHeaders.put("preHeader1", "preHeaderValue1");
		dummyHeaders.put("preHeader2", "preHeaderValue2");
		dummyHeaders.put("preHeader3", "preHeaderValue3");
		return dummyHeaders;
	}

	enum Headers
	{
		CUSTOM_HEADER_1("customheader-1", "customvalue-1"),
		CUSTOM_HEADER_2("customheader-2", "customvalue-2"),
		CUSTOM_HEADER_3("customheader-3", "customvalue-3");

		String headerName;
		String headerValue;

		Headers(final String headerName, final String headerValue)
		{
			this.headerName = headerName;
			this.headerValue = headerValue;
		}

		static Map<String, String> getConfigHeaders()
		{
			return Stream.of(Headers.values()).collect(
					Collectors.toMap(h -> h.headerName, h -> h.headerValue));
		}

		String getHeaderName()
		{
			return headerName;
		}

		String getHeaderValue()
		{
			return headerValue;
		}
	}
}
