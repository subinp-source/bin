/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.web.payment.validation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component("sopMockUrlValidator")
public class SopMockUrlValidator extends AbstractMockUrlValidator
{
	protected static final String ALLOWED_PAYMENT_HOSTS = "acceleratorservices.payment.response.url.allowlist";
	protected static final String ALLOWED_MERCHANT_HOSTS = "acceleratorservices.merchant.callback.url.allowlist";

	protected static final String PAYMENT_RESPONSE_END_RELATIVE_URL = "/sop/response";
	protected static final String MERCHANT_CALLBACK_END_RELATIVE_URL = "/integration/merchant_callback";
	protected static final String EXTENDED_MERCHANT_CALLBACK_END_RELATIVE_URL = "/payment/sop/response";

	private static final Logger LOG = LoggerFactory.getLogger(SopMockUrlValidator.class);

	/**
	 * Validate the passed payment response url, if it is allowed or not.
	 * For a Url to be allowed it must be only containing allowed scheme, host and port.
	 * Allowed hosts are picked up from config property acceleratorservices.payment.response.url.allowlist
	 * Also the url must contain the relative path (/sop/response).
	 * <p>
	 * If the property acceleratorservices.payment.url.strict.enabled is set to false this method only does scheme validation {@link UrlSchemeValidator},
	 * and skips other validations.
	 *
	 * @param url url to be validated.
	 * @return true if url is allowed, otherwise false.
	 */
	public boolean validatePaymentResponseUrl(final String url)
	{

		// do some basic validations like null and scheme check
		if (!isValidUrlSyntax(url))
		{
			return false;
		}

		// if strict check is disabled then simply return true
		if (!isStrictCheckEnabled())
		{
			return true;
		}

		// strict check is enabled, do further validation

		// check for at least making sure the mandatory relative path is present in the url
		if (!extractPathFromUrl(url).endsWith(PAYMENT_RESPONSE_END_RELATIVE_URL))
		{
			if (LOG.isErrorEnabled())
			{
				LOG.error("Expected relative part: {}", PAYMENT_RESPONSE_END_RELATIVE_URL);
				LOG.error("Payment response URL not allowed(relative part is not as expected), malicious attempt? Url checked: {}",
						URLEncoder.encode(url, StandardCharsets.UTF_8));
			}
			return false;
		}

		// check for scheme, host and port
		final List<String> allowedPaymentHosts = getAllowedHosts(ALLOWED_PAYMENT_HOSTS);
		if (!allowedPaymentHosts.contains(extractSchemeHostAndPortFromUrl(url)))
		{
			if (LOG.isErrorEnabled())
			{
				LOG.error("Allowed payment scheme,host and port urls: {}", allowedPaymentHosts);
				LOG.error(
						"Payment response URL not allowed(scheme,host and port not as expected), malicious attempt? Url checked: {}",
						URLEncoder.encode(url, StandardCharsets.UTF_8));
			}
			return false;
		}

		return true;
	}

	/**
	 * Validate the passed merchant callback url, if it is allowed or not.
	 * For a Url to be allowed it must be only containing allowed scheme, host and port.
	 * Allowed hosts are picked up from config property acceleratorservices.merchant.callback.url.allowlist
	 * Also the url must contain the relative path (/integration/merchant_callback OR /payment/sop/response).
	 * <p>
	 * If the property acceleratorservices.payment.url.strict.enabled is set to false this method only does scheme validation {@link UrlSchemeValidator},
	 * and skips other validations.
	 *
	 * @param url url to be validated.
	 * @return true if url is allowed, otherwise false.
	 */
	public boolean validateMerchantCallBackUrl(final String url)
	{

		// do some basic validations like null and scheme check
		if (!isValidUrlSyntax(url))
		{
			return false;
		}

		// if strict check is disabled then simply return true
		if (!isStrictCheckEnabled())
		{
			return true;
		}

		// strict check is enabled, do further validation

		// check for at least making sure the mandatory relative path is present in the url
		if (!(extractPathFromUrl(url).endsWith(MERCHANT_CALLBACK_END_RELATIVE_URL) || extractPathFromUrl(url)
				.endsWith(EXTENDED_MERCHANT_CALLBACK_END_RELATIVE_URL)))
		{
			if (LOG.isErrorEnabled())
			{
				LOG.error("Expected relative part: {} OR {}", MERCHANT_CALLBACK_END_RELATIVE_URL,
						EXTENDED_MERCHANT_CALLBACK_END_RELATIVE_URL);
				LOG.error("Merchant URL not allowed(relative part is not as expected), malicious attempt? Url checked: {}",
						URLEncoder.encode(url, StandardCharsets.UTF_8));
			}
			return false;
		}

		// check for scheme, host and port
		final List<String> allowedMerchantHosts = getAllowedHosts(ALLOWED_MERCHANT_HOSTS);
		if (!allowedMerchantHosts.contains(extractSchemeHostAndPortFromUrl(url)))
		{
			if (LOG.isErrorEnabled())
			{
				LOG.error("Allowed merchant scheme,host and port urls: {}", allowedMerchantHosts);
				LOG.error("Merchant URL not allowed(scheme,host and port not as expected), malicious attempt? Url checked: {}",
						URLEncoder.encode(url, StandardCharsets.UTF_8));
			}
			return false;
		}


		return true;
	}


}
