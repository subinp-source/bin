/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl;

import de.hybris.platform.outboundservices.client.OutboundHttpClientFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the {@link OutboundHttpClientFactory}
 */
public class DefaultOutboundHttpClientFactory implements OutboundHttpClientFactory
{
	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private int maxConnections;
	private int keepAlive;
	private int timeout;
	private int validity;

	@Override
	public HttpClient create()
	{
		final RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(getTimeout())
				.setConnectionRequestTimeout(getTimeout())
				.setSocketTimeout(getTimeout())
				.setCookieSpec(CookieSpecs.IGNORE_COOKIES)
				.build();

		final PoolingHttpClientConnectionManager connectionManager = createPoolingHttpClientConnectionManager();

		return getHttpClientBuilder()
				.setDefaultRequestConfig(config)
				.setConnectionManager(connectionManager)
				.setKeepAliveStrategy((httpResponse, httpContext) -> getKeepAlive())
				.build();
	}

	private PoolingHttpClientConnectionManager createPoolingHttpClientConnectionManager()
	{
		final PoolingHttpClientConnectionManager connectionManager = getConnectionManager();
		connectionManager.setDefaultMaxPerRoute(getMaxConnections());
		connectionManager.setMaxTotal(getMaxConnections());
		connectionManager.setValidateAfterInactivity(getValidity());
		return connectionManager;
	}

	private PoolingHttpClientConnectionManager getConnectionManager()
	{
		return new PoolingHttpClientConnectionManager(
					RegistryBuilder.<ConnectionSocketFactory>create()
							.register(HTTP, PlainConnectionSocketFactory.getSocketFactory())
							.register(HTTPS, SSLConnectionSocketFactory.getSystemSocketFactory())
							.build());
	}

	protected HttpClientBuilder getHttpClientBuilder()
	{
		return HttpClientBuilder.create().useSystemProperties();
	}

	protected int getMaxConnections()
	{
		return maxConnections;
	}

	@Required
	public void setMaxConnections(final int maxConnections)
	{
		this.maxConnections = maxConnections;
	}

	public int getKeepAlive()
	{
		return keepAlive;
	}

	@Required
	public void setKeepAlive(final int keepAlive)
	{
		this.keepAlive = keepAlive;
	}

	public int getTimeout()
	{
		return timeout;
	}

	@Required
	public void setTimeout(final int timeout)
	{
		this.timeout = timeout;
	}

	public int getValidity()
	{
		return validity;
	}

	@Required
	public void setValidity(final int validity)
	{
		this.validity = validity;
	}
}
