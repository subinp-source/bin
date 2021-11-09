/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;


public class SSLIssuesIgnoringHttpClientFactory
{
	public static HttpClient createHttpClient()
	{
		try
		{
			final TrustManager[] trustAllCerts = { new DummyTrustManager() };

			final SSLContext sslContext = SSLContext.getDefault();
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

			final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

			httpClientBuilder.setSSLHostnameVerifier(new DummyHostnameVerifier());
			httpClientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, new DummyHostnameVerifier()));

			return httpClientBuilder.build();
		}
		catch (KeyManagementException | NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
}
