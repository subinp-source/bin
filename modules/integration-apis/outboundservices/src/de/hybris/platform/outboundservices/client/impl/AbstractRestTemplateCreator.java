/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundservices.client.impl;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundservices.cache.DestinationRestTemplateCacheKey;
import de.hybris.platform.outboundservices.cache.DestinationRestTemplateId;
import de.hybris.platform.outboundservices.cache.RestTemplateCache;
import de.hybris.platform.outboundservices.cache.impl.DefaultDestinationRestTemplateCacheKey;
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateCreator;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

/**
 * The abstract RestTemplate creator.
 */
public abstract class AbstractRestTemplateCreator implements IntegrationRestTemplateCreator
{
	private static final Logger LOG = Log.getLogger(AbstractRestTemplateCreator.class);
	private final Object syncObj = new Object();

	private List<HttpMessageConverter<Object>> messageConverters = Lists.newArrayList();
	private List<ClientHttpRequestInterceptor> requestInterceptors = Lists.newArrayList();
	private ClientHttpRequestFactory clientHttpRequestFactory;
	private RestTemplateCache cache;

	@Override
	public RestOperations create(final ConsumedDestinationModel destination)
	{
		if (isApplicable(destination))
		{
			final DestinationRestTemplateCacheKey key = createDestinationRestTemplateCacheKey(destination);
			RestTemplate restTemplate;
			synchronized (syncObj)
			{
				restTemplate = getCache().get(key);
				if (restTemplate == null)
				{
					LOG.debug("Cache doesn't contain the rest template for '{}', creating and caching a rest template", key.getDestinationId());
					restTemplate = createRestTemplate(destination);
					getCache().put(key, restTemplate);
				}
			}
			return restTemplate;
		}
		throw new UnsupportedRestTemplateException();
	}

	protected abstract RestTemplate createRestTemplate(final ConsumedDestinationModel destination);

	protected DestinationRestTemplateCacheKey createDestinationRestTemplateCacheKey(final ConsumedDestinationModel destination)
	{
		return DefaultDestinationRestTemplateCacheKey.from(getDestinationRestTemplateId(destination));
	}

	protected abstract DestinationRestTemplateId getDestinationRestTemplateId(final ConsumedDestinationModel destinationModel);

	protected void addInterceptors(final RestTemplate template, final ClientHttpRequestInterceptor... interceptors)
	{
		final List<ClientHttpRequestInterceptor> list = Lists.newArrayList(interceptors);
		if (getRequestInterceptors() != null)
		{
			list.addAll(getRequestInterceptors());
		}
		list.addAll(template.getInterceptors());
		template.setInterceptors(list);
	}

	protected void addMessageConverters(final RestTemplate template, final HttpMessageConverter<?>... converters)
	{
		final List<HttpMessageConverter<?>> list = Lists.newArrayList(converters);
		if (getMessageConverters() != null)
		{
			list.addAll(getMessageConverters());
		}
		list.addAll(template.getMessageConverters());
		template.setMessageConverters(list);
	}

	protected List<HttpMessageConverter<Object>> getMessageConverters()
	{
		return messageConverters;
	}

	@Required
	public void setMessageConverters(final List<HttpMessageConverter<Object>> messageConverters)
	{
		this.messageConverters = messageConverters;
	}

	protected List<ClientHttpRequestInterceptor> getRequestInterceptors()
	{
		return requestInterceptors;
	}

	@Required
	public void setRequestInterceptors(final List<ClientHttpRequestInterceptor> requestInterceptors)
	{
		this.requestInterceptors = requestInterceptors;
	}

	@Required
	public void setClientHttpRequestFactory(final ClientHttpRequestFactory clientHttpRequestFactory)
	{
		this.clientHttpRequestFactory = clientHttpRequestFactory;
	}

	protected ClientHttpRequestFactory getClientHttpRequestFactory()
	{
		return this.clientHttpRequestFactory;
	}

	@Required
	public void setCache(final RestTemplateCache cache)
	{
		this.cache = cache;
	}

	protected RestTemplateCache getCache()
	{
		return cache;
	}
}
