/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.web.payment.validation;

import static de.hybris.platform.acceleratorservices.web.payment.validation.SopMockUrlValidator.ALLOWED_MERCHANT_HOSTS;
import static de.hybris.platform.acceleratorservices.web.payment.validation.SopMockUrlValidator.ALLOWED_PAYMENT_HOSTS;
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
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SopMockUrlValidatorTest
{
	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration configuration;

	@Spy
	@InjectMocks
	private SopMockUrlValidator sopMockUrlValidator;

	@Before
	public void setUp()
	{
		final String allowedPaymentHosts = "http://electronics.local:9001, https://electronics.local:9002, http://localhost:4200";
		final String allowedMerchantHosts = "http://electronics.local:9001, https://electronics.local:9002, http://localhost:4200";
		final List<String> defaultAllowedHosts = Arrays.asList("https://apparel.default:9002");

		doReturn(configuration).when(configurationService).getConfiguration();
		doReturn(allowedPaymentHosts).when(configuration).getString(ALLOWED_PAYMENT_HOSTS, StringUtils.EMPTY);
		doReturn(allowedMerchantHosts).when(configuration).getString(ALLOWED_MERCHANT_HOSTS, StringUtils.EMPTY);
		doReturn(defaultAllowedHosts).when(sopMockUrlValidator).getDefaultAllowedSchemeHostAndPortUrls();

	}

	@Test
	public void shouldNotAllowEveryPaymentUrlWhenConfigIsTrue()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(true);

		// test for allowed URLs
		final List<String> allowedUrls = Arrays
				.asList("http://electronics.local:9001/yacceleratorstorefront/electronics/en/checkout/multi/sop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/en/checkout/multi/sop/response",
						"http://electronics.local:9001/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/sop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/sop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/en/checkout/multi/sop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/sop/response");

		for (final String url : allowedUrls)
		{
			assertTrue(sopMockUrlValidator.validatePaymentResponseUrl(url));
		}

		// now test if someone is trying to misuse and use url redirection attacks.

		final List<String> notAllowedUrls = Arrays
				.asList(null, "", " ", "http://www.hacker.com/electronics/en/checkout/multi/sop/response",
						"https://www.hacker.com/electronics/en/checkout/multi/sop/response",
						"http://www.hacker.com:9001/electronics/en/checkout/multi/sop/response",
						"https://www.hacker.com:9002/electronics/en/checkout/multi/sop/response",
						"http://www.hacker.com/somepath/checkout/multi/sop/response",
						"https://www.hacker.com/somepath/checkout/multi/sop/response",
						"http://www.hacker.com:9001/somepath/checkout/multi/sop/response",
						"https://www.hacker.com:9002/somepath/checkout/multi/sop/response",
						"http://www.hacker.com:9001/somepath?param=checkout/multi/sop/response",
						"https://www.hacker.com:9002/somepath?param=/integration/merchant_callback",
						"http://www.hacker.com:9001/somepath?redirect=http://www.abc.com",
						"https://www.hacker.com:9002/somepath?redirect=http://www.abc.com");

		for (final String url : notAllowedUrls)
		{
			assertFalse(sopMockUrlValidator.validatePaymentResponseUrl(url));
		}

	}

	@Test
	public void shouldAllowEveryPaymentUrlWhenConfigIsFalse()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(false);

		// test for allowed URLs
		final List<String> allowedUrls = Arrays
				.asList("http://electronics.local:9001/yacceleratorstorefront/electronics/en/checkout/multi/sop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/en/checkout/multi/sop/response",
						"http://electronics.local:9001/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/sop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/sop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/en/checkout/multi/sop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/somelocale/somepath/checkout/multi/sop/response");

		for (final String url : allowedUrls)
		{
			assertTrue(sopMockUrlValidator.validatePaymentResponseUrl(url));
		}

		// now test that system should also work with any valid url when config is false.

		final List<String> outSideUrls = Arrays.asList("http://www.hacker.com/electronics/en/checkout/multi/sop/response",
				"https://www.hacker.com/electronics/en/checkout/multi/sop/response",
				"http://www.hacker.com:9001/electronics/en/checkout/multi/sop/response",
				"https://www.hacker.com:9002/electronics/en/checkout/multi/sop/response",
				"http://www.hacker.com/somepath/checkout/multi/sop/response",
				"https://www.hacker.com/somepath/checkout/multi/sop/response",
				"http://www.hacker.com:9001/somepath/checkout/multi/sop/response",
				"https://www.hacker.com:9002/somepath/checkout/multi/sop/response",
				"http://www.hacker.com:9001/somepath?param=checkout/multi/sop/response",
				"https://www.hacker.com:9002/somepath?param=checkout/multi/sop/response",
				"http://www.hacker.com:9001/somepath?redirect=http://www.abc.com",
				"https://www.hacker.com:9002/somepath?redirect=http://www.abc.com");

		for (final String url : outSideUrls)
		{
			assertTrue(sopMockUrlValidator.validatePaymentResponseUrl(url));
		}

	}


	@Test
	public void shouldNotAllowEveryMerchantCallBackUrlWhenConfigIsTrue()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(true);

		// test for allowed URLs
		final List<String> allowedUrls = Arrays
				.asList("http://electronics.local:9001/yacceleratorstorefront/electronics/en/integration/merchant_callback",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/en/integration/merchant_callback",
						"http://electronics.local:9001/yacceleratorstorefront/electronics/somelocale/somepath/integration/merchant_callback",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/somelocale/somepath/integration/merchant_callback",
						"http://localhost:4200/yacceleratorstorefront/electronics/en/integration/merchant_callback",
						"http://localhost:4200/yacceleratorstorefront/electronics/somelocale/somepath/integration/merchant_callback");

		for (final String url : allowedUrls)
		{
			assertTrue(sopMockUrlValidator.validateMerchantCallBackUrl(url));
		}

		// now test if someone is trying to misuse and use url redirection attacks.

		final List<String> notAllowedUrls = Arrays
				.asList(null, "", " ", "http://www.hacker.com/electronics/en/integration/merchant_callback",
						"https://www.hacker.com/electronics/en/integration/merchant_callback",
						"http://www.hacker.com:9001/electronics/en/integration/merchant_callback",
						"https://www.hacker.com:9002/electronics/en/integration/merchant_callback",
						"http://www.hacker.com/somepath/integration/merchant_callback",
						"https://www.hacker.com/somepath/integration/merchant_callback",
						"http://www.hacker.com:9001/somepath/integration/merchant_callback",
						"https://www.hacker.com:9002/somepath/integration/merchant_callback",
						"http://www.hacker.com:9001/somepath?param=checkout/multi/sop/response",
						"https://www.hacker.com:9002/somepath?param=checkout/multi/sop/response",
						"http://www.hacker.com:9001/somepath?redirect=http://www.abc.com",
						"https://www.hacker.com:9002/somepath?redirect=http://www.abc.com",
						// for the DDoS situation when url is same as the form submit url
						"http://electronics.local:9001/acceleratorservices/sop-mock/process",
						"https://electronics.local:9002/acceleratorservices/sop-mock/process");

		for (final String url : notAllowedUrls)
		{
			assertFalse(sopMockUrlValidator.validateMerchantCallBackUrl(url));
		}

	}

	@Test
	public void shouldAllowEveryMerchantCallBackUrlWhenConfigIsFalse()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(false);

		// test for allowed URLs
		final List<String> allowedUrls = Arrays
				.asList("http://electronics.local:9001/yacceleratorstorefront/electronics/en/integration/merchant_callback",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/en/integration/merchant_callback",
						"http://electronics.local:9001/yacceleratorstorefront/electronics/somelocale/somepath/integration/merchant_callback",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/somelocale/somepath/integration/merchant_callback",
						"http://localhost:4200/yacceleratorstorefront/electronics/en/integration/merchant_callback",
						"http://localhost:4200/yacceleratorstorefront/electronics/somelocale/somepath/integration/merchant_callback");

		for (final String url : allowedUrls)
		{
			assertTrue(sopMockUrlValidator.validateMerchantCallBackUrl(url));
		}

		// now test that system should also work with any valid url when config is false.

		final List<String> outSideUrls = Arrays.asList("http://www.hacker.com/electronics/en/integration/merchant_callback",
				"https://www.hacker.com/electronics/en/integration/merchant_callback",
				"http://www.hacker.com:9001/electronics/en/integration/merchant_callback",
				"https://www.hacker.com:9002/electronics/en/integration/merchant_callback",
				"http://www.hacker.com/somepath/integration/merchant_callback",
				"https://www.hacker.com/somepath/integration/merchant_callback",
				"http://www.hacker.com:9001/somepath/integration/merchant_callback",
				"https://www.hacker.com:9002/somepath/integration/merchant_callback",
				"http://www.hacker.com:9001/somepath?param=/integration/merchant_callback",
				"https://www.hacker.com:9002/somepath?param=/integration/merchant_callback",
				"http://www.hacker.com:9001/somepath?redirect=http://www.abc.com",
				"https://www.hacker.com:9002/somepath?redirect=http://www.abc.com",
				// for the DDoS situation when url is same as the form submit url
				// PS: Note that DDoS can happen when config is false.
				"http://electronics.local:9001/acceleratorservices/sop-mock/process",
				"https://electronics.local:9002/acceleratorservices/sop-mock/process");

		for (final String url : outSideUrls)
		{
			assertTrue(sopMockUrlValidator.validateMerchantCallBackUrl(url));
		}

	}

	@Test
	public void shouldNotAllowEveryExtendedMerchantCallBackUrlWhenConfigIsTrue()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(true);

		// test for allowed URLs
		final List<String> allowedUrls = Arrays
				.asList("http://electronics.local:9001/yacceleratorstorefront/electronics/en/payment/sop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/en/payment/sop/response",
						"http://electronics.local:9001/yacceleratorstorefront/electronics/somelocale/somepath/payment/sop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/somelocale/somepath/payment/sop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/en/payment/sop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/somelocale/somepath/payment/sop/response");

		for (final String url : allowedUrls)
		{
			assertTrue(sopMockUrlValidator.validateMerchantCallBackUrl(url));
		}

		// now test if someone is trying to misuse and use url redirection attacks.

		final List<String> notAllowedUrls = Arrays
				.asList(null, "", " ", "http://www.hacker.com/electronics/en/payment/sop/response",
						"https://www.hacker.com/electronics/en/payment/sop/response",
						"http://www.hacker.com:9001/electronics/en/payment/sop/response",
						"https://www.hacker.com:9002/electronics/en/payment/sop/response",
						"http://www.hacker.com/somepath/payment/sop/response", "https://www.hacker.com/somepath/payment/sop/response",
						"http://www.hacker.com:9001/somepath/payment/sop/response",
						"https://www.hacker.com:9002/somepath/payment/sop/response",
						"http://www.hacker.com:9001/somepath?param=/payment/sop/response",
						"https://www.hacker.com:9002/somepath?param=/payment/sop/response",
						"http://www.hacker.com:9001/somepath?redirect=http://www.abc.com",
						"https://www.hacker.com:9002/somepath?redirect=http://www.abc.com",
						// for the DDoS situation when url is same as the form submit url
						"http://electronics.local:9001/acceleratorservices/sop-mock/process",
						"https://electronics.local:9002/acceleratorservices/sop-mock/process");

		for (final String url : notAllowedUrls)
		{
			assertFalse(sopMockUrlValidator.validateMerchantCallBackUrl(url));
		}

	}

	@Test
	public void shouldAllowEveryExtendedMerchantCallBackUrlWhenConfigIsFalse()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(false);

		// test for allowed URLs
		final List<String> allowedUrls = Arrays
				.asList("http://electronics.local:9001/yacceleratorstorefront/electronics/en/payment/sop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/en/payment/sop/response",
						"http://electronics.local:9001/yacceleratorstorefront/electronics/somelocale/somepath/payment/sop/response",
						"https://electronics.local:9002/yacceleratorstorefront/electronics/somelocale/somepath/payment/sop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/en/payment/sop/response",
						"http://localhost:4200/yacceleratorstorefront/electronics/somelocale/somepath/payment/sop/response");

		for (final String url : allowedUrls)
		{
			assertTrue(sopMockUrlValidator.validateMerchantCallBackUrl(url));
		}

		// now test that system should also work with any valid url when config is false.

		final List<String> outSideUrls = Arrays.asList("http://www.hacker.com/electronics/en/payment/sop/response",
				"https://www.hacker.com/electronics/en/payment/sop/response",
				"http://www.hacker.com:9001/electronics/en/payment/sop/response",
				"https://www.hacker.com:9002/electronics/en/payment/sop/response",
				"http://www.hacker.com/somepath/payment/sop/response", "https://www.hacker.com/somepath/payment/sop/response",
				"http://www.hacker.com:9001/somepath/payment/sop/response",
				"https://www.hacker.com:9002/somepath/payment/sop/response",
				"http://www.hacker.com:9001/somepath?param=/payment/sop/response",
				"https://www.hacker.com:9002/somepath?param=/payment/sop/response",
				"http://www.hacker.com:9001/somepath?redirect=http://www.abc.com",
				"https://www.hacker.com:9002/somepath?redirect=http://www.abc.com",
				// for the DDoS situation when url is same as the form submit url
				// PS: Note that DDoS can happen when config is false.
				"http://electronics.local:9001/acceleratorservices/sop-mock/process",
				"https://electronics.local:9002/acceleratorservices/sop-mock/process");

		for (final String url : outSideUrls)
		{
			assertTrue(sopMockUrlValidator.validateMerchantCallBackUrl(url));
		}

	}

	@Test
	public void shouldFallbackToDefaultHostsIfConfigNotDefinedForPaymentHosts()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(true); // url check enabled
		when(configuration.getString(ALLOWED_PAYMENT_HOSTS, StringUtils.EMPTY)).thenReturn(StringUtils.EMPTY);

		// test that default values are populated
		final List<String> allowedHosts = sopMockUrlValidator.getAllowedHosts(ALLOWED_PAYMENT_HOSTS);
		assertThat(allowedHosts, contains("https://apparel.default:9002"));
		assertThat(allowedHosts, hasSize(1)); // no values from config

	}

	@Test
	public void shouldNotContainDefaultHostsIfConfigIsDefinedForPaymentHosts()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(true); // url check enabled

		// test that default values are populated
		final List<String> allowedHosts = sopMockUrlValidator.getAllowedHosts(ALLOWED_PAYMENT_HOSTS);
		assertThat(allowedHosts,
				containsInAnyOrder("http://electronics.local:9001", "https://electronics.local:9002", "http://localhost:4200"));
		assertThat(allowedHosts, hasSize(3)); // no default values

	}

	@Test
	public void shouldFallbackToDefaultHostsIfConfigNotDefinedForMerchantHosts()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(true); // url check enabled
		when(configuration.getString(ALLOWED_MERCHANT_HOSTS, StringUtils.EMPTY)).thenReturn(StringUtils.EMPTY);

		// test that default values are populated
		final List<String> allowedHosts = sopMockUrlValidator.getAllowedHosts(ALLOWED_MERCHANT_HOSTS);
		assertThat(allowedHosts, contains("https://apparel.default:9002"));
		assertThat(allowedHosts, hasSize(1)); // no values from config

	}

	@Test
	public void shouldNotContainDefaultHostsIfConfigIsDefinedForMerchantHosts()
	{
		when(configuration.getBoolean(anyString(), anyBoolean())).thenReturn(true); // url check enabled

		// test that default values are populated
		final List<String> allowedHosts = sopMockUrlValidator.getAllowedHosts(ALLOWED_MERCHANT_HOSTS);
		assertThat(allowedHosts,
				containsInAnyOrder("http://electronics.local:9001", "https://electronics.local:9002", "http://localhost:4200"));
		assertThat(allowedHosts, hasSize(3)); // no default values

	}

}
