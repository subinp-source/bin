/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.utils;

import java.util.Arrays;

import de.hybris.platform.util.Config;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


/**
 * Helper class for creating http communication with kyma.
 */
public class KymaHttpHelper
{
	private static final String CIPHERS_PROP = "kymaintegrationservices.cypher.suites";
	private static final String DEFAULT_CIPHERS = "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256";

	private KymaHttpHelper()
	{
	}

	/**
	 * Makes some default http headers for communication with kyma
	 *
	 * @return default headers
	 */
	public static HttpHeaders getDefaultHeaders()
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public static String[] getCiphers()
	{
		final String value = Config.getString(CIPHERS_PROP, DEFAULT_CIPHERS);
		return value.split(",");
	}
}
