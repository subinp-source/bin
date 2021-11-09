/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.web.payment.validation;

import static de.hybris.platform.acceleratorservices.web.payment.validation.HopMockUrlValidator.ALLOWED_PAYMENT_HOSTS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class HopMockUrlValidatorTest
{
	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration configuration;

	@Mock
	private BaseSiteService baseSiteService;

	@Mock
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Mock
	private BaseSiteModel baseSiteModel;

	@Spy
	@InjectMocks
	private HopMockUrlValidator hopMockUrlValidator;

	@Before
	public void setUp()
	{
		final String allowedPaymentHosts = "http://electronics.local:9001, https://electronics.local:9002, http://localhost:4200";
		final List<String> defaultAllowedHosts = Arrays.asList("https://apparel.default:9002");

		doReturn(configuration).when(configurationService).getConfiguration();
		doReturn(allowedPaymentHosts).when(configuration).getString(ALLOWED_PAYMENT_HOSTS, StringUtils.EMPTY);
		doReturn(defaultAllowedHosts).when(hopMockUrlValidator).getDefaultAllowedSchemeHostAndPortUrls();
	}

	@Test
	public void shouldNotAllowEveryPaymentUrlWhenConfigIsTrue()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(true);

		// test for allowed URLs
		final List<String> allowedUrls = Arrays
				.asList("http://electronics.local:9001/yacceleratorstorefront/electronics/en/checkout/multi/hop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/en/checkout/multi/hop/response",
						"http://electronics.local:9001/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/hop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/hop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/en/checkout/multi/hop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/hop/response");

		for (final String url : allowedUrls)
		{
			assertTrue(hopMockUrlValidator.validatePaymentResponseUrl(url));
		}

		// now test if someone is trying to misuse and use url redirection attacks.

		final List<String> notAllowedUrls = Arrays
				.asList(null, "", " ", "http://www.hacker.com/electronics/en/checkout/multi/hop/response",
						"https://www.hacker.com/electronics/en/checkout/multi/hop/response",
						"http://www.hacker.com:9001/electronics/en/checkout/multi/hop/response",
						"https://www.hacker.com:9002/electronics/en/checkout/multi/hop/response",
						"http://www.hacker.com/somepath/checkout/multi/hop/response",
						"https://www.hacker.com/somepath/checkout/multi/hop/response",
						"http://www.hacker.com:9001/somepath/checkout/multi/hop/response",
						"https://www.hacker.com:9002/somepath/checkout/multi/hop/response",
						"http://www.hacker.com:9001/somepath?param=checkout/multi/hop/response",
						"https://www.hacker.com:9002/somepath?param=/integration/merchant_callback",
						"http://www.hacker.com:9001/somepath?redirect=http://www.abc.com",
						"https://www.hacker.com:9002/somepath?redirect=http://www.abc.com");

		for (final String url : notAllowedUrls)
		{
			assertFalse(hopMockUrlValidator.validatePaymentResponseUrl(url));
		}

	}

	@Test
	public void shouldAllowEveryPaymentUrlWhenConfigIsFalse()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(false);

		// test for allowed URLs
		final List<String> allowedUrls = Arrays
				.asList("http://electronics.local:9001/yacceleratorstorefront/electronics/en/checkout/multi/hop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/en/checkout/multi/hop/response",
						"http://electronics.local:9001/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/hop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/hop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/en/checkout/multi/hop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/hop/response");

		for (final String url : allowedUrls)
		{
			assertTrue(hopMockUrlValidator.validatePaymentResponseUrl(url));
		}

		// now test that system should also work with any valid url when config is false.

		final List<String> outSideUrls = Arrays.asList("http://www.hacker.com/electronics/en/checkout/multi/hop/response",
				"https://www.hacker.com/electronics/en/checkout/multi/hop/response",
				"http://www.hacker.com:9001/electronics/en/checkout/multi/hop/response",
				"https://www.hacker.com:9002/electronics/en/checkout/multi/hop/response",
				"http://www.hacker.com/somepath/checkout/multi/hop/response",
				"https://www.hacker.com/somepath/checkout/multi/hop/response",
				"http://www.hacker.com:9001/somepath/checkout/multi/hop/response",
				"https://www.hacker.com:9002/somepath/checkout/multi/hop/response",
				"http://www.hacker.com:9001/somepath?param=checkout/multi/hop/response",
				"https://www.hacker.com:9002/somepath?param=checkout/multi/hop/response",
				"http://www.hacker.com:9001/somepath?redirect=http://www.abc.com",
				"https://www.hacker.com:9002/somepath?redirect=http://www.abc.com");

		for (final String url : outSideUrls)
		{
			assertTrue(hopMockUrlValidator.validatePaymentResponseUrl(url));
		}

	}

	@Test
	public void shouldFallbackToDefaultHostsIfConfigNotDefinedForPaymentHosts()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(true); // url check enabled
		when(configuration.getString(ALLOWED_PAYMENT_HOSTS, StringUtils.EMPTY)).thenReturn(StringUtils.EMPTY);

		// test that default values are populated
		final List<String> allowedHosts = hopMockUrlValidator.getAllowedHosts(ALLOWED_PAYMENT_HOSTS);
		assertThat(allowedHosts, contains("https://apparel.default:9002"));
		assertThat(allowedHosts, hasSize(1)); // no values from config

	}

	@Test
	public void shouldNotContainDefaultHostsIfConfigIsDefinedForPaymentHosts()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(true); // url check enabled

		// test that default values are populated
		final List<String> allowedHosts = hopMockUrlValidator.getAllowedHosts(ALLOWED_PAYMENT_HOSTS);
		assertThat(allowedHosts,
				containsInAnyOrder("http://electronics.local:9001", "https://electronics.local:9002", "http://localhost:4200"));
		assertThat(allowedHosts, hasSize(3)); // no default values

	}

	@Test
	public void shouldReturnValidAllowedSchemeHostAndPortUrls()
	{
		when(hopMockUrlValidator.getDefaultAllowedSchemeHostAndPortUrls()).thenCallRealMethod();
		when(baseSiteService.getAllBaseSites()).thenReturn(Lists.newArrayList(baseSiteModel));
		when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel, false, "")).thenReturn("http://my.url.com");
		when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel, true, "")).thenReturn("https://my.url.com");

		final Collection<String> hosts = hopMockUrlValidator.getDefaultAllowedSchemeHostAndPortUrls();
		Assertions.assertThat(hosts).isNotEmpty().containsExactly("http://my.url.com", "https://my.url.com");
	}

	@Test
	public void shouldReturnValidAllowedSchemeHostAndPortUrls_nonValid_http()
	{
		when(hopMockUrlValidator.getDefaultAllowedSchemeHostAndPortUrls()).thenCallRealMethod();
		when(baseSiteService.getAllBaseSites()).thenReturn(Lists.newArrayList(baseSiteModel));
		// ideally getSiteBaseUrlResolutionService().getWebsiteUrlForSite() should work properly, but we still show current behaviour if it dint.
		when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel, false, "")).thenReturn("non-valid-url");
		when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel, true, "")).thenReturn("");

		final Collection<String> hosts = hopMockUrlValidator.getDefaultAllowedSchemeHostAndPortUrls();
		Assertions.assertThat(hosts).isNotEmpty().containsExactly("null://null");  // URISyntaxException is never thrown, so we don't get ""
	}

	@Test
	public void shouldReturnValidAllowedSchemeHostAndPortUrls_nonValid_http_2()
	{
		when(hopMockUrlValidator.getDefaultAllowedSchemeHostAndPortUrls()).thenCallRealMethod();
		when(baseSiteService.getAllBaseSites()).thenReturn(Lists.newArrayList(baseSiteModel));
		// ideally getSiteBaseUrlResolutionService().getWebsiteUrlForSite() should work properly, but we still show current behaviour if it dint.
		when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel, false, "")).thenReturn("non valid url");
		when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel, true, "")).thenReturn("");

		final Collection<String> hosts = hopMockUrlValidator.getDefaultAllowedSchemeHostAndPortUrls();
		Assertions.assertThat(hosts).isNotEmpty().containsExactly("");
	}

	@Test
	public void shouldReturnValidAllowedSchemeHostAndPortUrls_nonValid_https()
	{
		when(hopMockUrlValidator.getDefaultAllowedSchemeHostAndPortUrls()).thenCallRealMethod();
		when(baseSiteService.getAllBaseSites()).thenReturn(Lists.newArrayList(baseSiteModel));
		// ideally getSiteBaseUrlResolutionService().getWebsiteUrlForSite() should work properly, but we still show current behaviour if it dint.
		when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel, false, "")).thenReturn("");
		when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel, true, "")).thenReturn("non-valid-https-url");

		final Collection<String> hosts = hopMockUrlValidator.getDefaultAllowedSchemeHostAndPortUrls();
		Assertions.assertThat(hosts).isNotEmpty().containsExactly("null://null");   // URISyntaxException is never thrown, so we don't get ""
	}

	@Test
	public void shouldReturnValidAllowedSchemeHostAndPortUrls_nonValid_protocol()
	{
		when(hopMockUrlValidator.getDefaultAllowedSchemeHostAndPortUrls()).thenCallRealMethod();
		when(baseSiteService.getAllBaseSites()).thenReturn(Lists.newArrayList(baseSiteModel));
		// ideally getSiteBaseUrlResolutionService().getWebsiteUrlForSite() should work properly, but we still show current behaviour if it dint.
		when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel, false, "")).thenReturn("ftp://my.ftp.com");
		when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel, true, "")).thenReturn("");

		final Collection<String> hosts = hopMockUrlValidator.getDefaultAllowedSchemeHostAndPortUrls();
		Assertions.assertThat(hosts).isNotEmpty().containsExactly("ftp://my.ftp.com");
	}

	@Test
	public void testIsValidUrlSyntax()
	{
		Assertions.assertThat(hopMockUrlValidator.isValidUrlSyntax(null)).isFalse();
		Assertions.assertThat(hopMockUrlValidator.isValidUrlSyntax("")).isFalse();
		Assertions.assertThat(hopMockUrlValidator.isValidUrlSyntax("non valid url")).isFalse();
		// this is valid because it thinks of it as relative url.
		Assertions.assertThat(hopMockUrlValidator.isValidUrlSyntax("non-valid-url")).isTrue();
	}
}
