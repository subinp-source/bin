/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.utils;

import static de.hybris.platform.kymaintegrationservices.utils.KymaConfigurationUtils.getTargetName;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.kymaintegrationservices.exceptions.SSLContextFactoryCreationException;
import de.hybris.platform.kymaintegrationservices.services.SSLContextFactoryService;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


/**
 * Wrapper for RestTemplate to handle ssl connections
 */
public class RestTemplateWrapper
{
	private static final int DEFAULT_CONNECTION_KEEP_ALIVE_MS = 60000;
	private static final String MAX_REDIRECTS = "kymaintegrationservices.max_redirects";
	private static final Logger LOG = LoggerFactory.getLogger(RestTemplateWrapper.class);
	private RestTemplate restTemplate;
	private DestinationService<AbstractDestinationModel> destinationService;
	private SSLContextFactoryService sslContextFactoryService;
	private List<HttpMessageConverter<Object>> messageConverters;
	private int timeout;

	private final Map<String, RestTemplate> restTemplateFactoryCache = new ConcurrentHashMap<>();

	/**
	 * @deprecated since 1905
	 *             This method returns the first RestTemplate from the restTemplateFactorycache, if no RestTemplate
	 *             found then a new RestTemplate Object will be returned.
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public RestTemplate getUpdatedRestTemplate()
	{
		return getRestTemplate();
	}

	public RestTemplate getRestTemplate(final AbstractCredentialModel abstractCredentialModel) throws CredentialException
	{
		updateCredential(abstractCredentialModel);
		return restTemplateFactoryCache.get(abstractCredentialModel.getId());
	}

	/**
	 * @deprecated since 1905
	 *             Use the protected version of this method("updateCredential") instead.
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public void updateCredentials(final AbstractDestinationModel destination) throws CredentialException
	{
		updateCredential(destination.getCredential());
	}

	protected void updateCredential(final AbstractCredentialModel credential) throws CredentialException
	{
		validateCredential(credential);

		if (!restTemplateFactoryCache.containsKey(credential.getId()))
		{
			updateRequestFactory((ConsumedCertificateCredentialModel) credential);
		}

		LOG.debug("Current Multiple RestTemplates cache Status : {} ", restTemplateFactoryCache);
	}

	public void invalidateAndUpdateCache(final ConsumedCertificateCredentialModel certificateCredential)
	{
		invalidateTheCache(certificateCredential.getId());
	}

	public void invalidateTheCache(final String credentialId)
	{
		try
		{
			final RestTemplate cachedRestTemplate = restTemplateFactoryCache.get(credentialId);

			if (cachedRestTemplate != null)
			{
				final HttpComponentsClientHttpRequestFactory requestFactory = (HttpComponentsClientHttpRequestFactory) cachedRestTemplate
						.getRequestFactory();
				requestFactory.destroy();

				restTemplateFactoryCache.remove(credentialId);
			}
		}
		catch (final Exception e)
		{
			LOG.error(String.format("Something bad happened during cache invalidation during credentials update, cause: %s",
					e.getMessage()));
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
		}
	}

	protected void updateRequestFactory(final ConsumedCertificateCredentialModel credential) throws CredentialException
	{
		byte[] certBytes = new byte[0];
		byte[] keyBytes = new byte[0];

		try
		{
			certBytes = DatatypeConverter.parseBase64Binary(credential.getCertificateData());
			keyBytes = DatatypeConverter.parseBase64Binary(credential.getPrivateKey());

			final SSLContext context = getSslContextFactoryService().createSSLContext(certBytes, keyBytes);

			final RequestConfig requestConfig = RequestConfig.custom().setMaxRedirects(Config.getInt(MAX_REDIRECTS, 10)).build();

			final HttpClient client = HttpClients.custom()
					.setKeepAliveStrategy((httpResponse, httpContext) -> getKeepAlive()).setDefaultRequestConfig(requestConfig)
					.setConnectionTimeToLive(getKeepAlive(), TimeUnit.MILLISECONDS)
					.setSSLSocketFactory(new SSLConnectionSocketFactory(context)
					{
						@Override
						protected void prepareSocket(final SSLSocket socket) throws IOException
						{
							super.prepareSocket(socket);
							socket.setEnabledCipherSuites(KymaHttpHelper.getCiphers());
						}
					}).build();
			final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
					client);
			clientHttpRequestFactory.setConnectTimeout(timeout);

			final RestTemplate newRestTemplate = new RestTemplate(clientHttpRequestFactory);
			final List<HttpMessageConverter<?>> messageConverterList = new ArrayList<>();
			messageConverterList.addAll(getMessageConverters());
			newRestTemplate.setMessageConverters(messageConverterList);
			restTemplateFactoryCache.putIfAbsent(credential.getId(), newRestTemplate);

		}
		catch (final SSLContextFactoryCreationException e)
		{
			final String errorMessage = String.format("Exception while creating the restTemplate for the  Certificate Credential with id: [{%s}] and with the cause : [{%s}] ", credential.getId(), e.getMessage());
			LOG.error(errorMessage);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
			throw new CredentialException(errorMessage, e);
		}
		finally
		{
			Arrays.fill(certBytes, (byte) 0);
			Arrays.fill(keyBytes, (byte) 0);
		}
	}

	/**
	 * @throws CredentialException
	 * @deprecated since 1905
	 *             This method returns the first RestTemplate from the restTemplateFactorycache, if no RestTemplate
	 *             found then a new RestTemplate Object will be returned.
	 */
	@Deprecated(since = "1905", forRemoval = true)
	protected RestTemplate getRestTemplate()
	{
		final Optional<String> abstractCredentialId = restTemplateFactoryCache.keySet().stream().findFirst();
		return abstractCredentialId.isPresent() ? restTemplateFactoryCache.get(abstractCredentialId.get()) : restTemplate;
	}

	protected int getKeepAlive()
	{
		return Config.getInt("kymaintegrationservices.connections.keep-alive", DEFAULT_CONNECTION_KEEP_ALIVE_MS);
	}

	/**
	 * @deprecated since 1905
	 * This method practically does nothing as it is not following the caching concept while storing the
	 * RestTemplate for the given credentials. It is there only for the backward compatibility.
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Required
	public void setRestTemplate(final RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
	}

	protected DestinationService<AbstractDestinationModel> getDestinationService()
	{
		return destinationService;
	}

	@Required
	public void setDestinationService(final DestinationService<AbstractDestinationModel> destinationService)
	{
		this.destinationService = destinationService;
	}

	/**
	 * @deprecated since 1905
	 *             This method has been replaced by validateCredential(AbstractCredentialModel) for convention.
	 */
	@Deprecated(since = "1905", forRemoval = true)
	protected void validateCredential(final AbstractDestinationModel destination) throws CredentialException
	{
		validateCredential(destination.getCredential());
	}

	protected void validateCredential(final AbstractCredentialModel credential) throws CredentialException
	{
		if (!(credential instanceof ConsumedCertificateCredentialModel))
		{
			final String errorMessage = String.format(
					"Missing Consumed Certificate Credential. Please get a client certificate from %s.",
					getTargetName());
			LOG.error(errorMessage);
			throw new CredentialException(errorMessage);
		}

		final ConsumedCertificateCredentialModel consumedCertificateCredential = (ConsumedCertificateCredentialModel) credential;

		if (StringUtils.isEmpty(consumedCertificateCredential.getCertificateData())
				|| StringUtils.isEmpty(consumedCertificateCredential.getPrivateKey()))
		{
			final String errorMessage = String.format("Invalid Certificate Credential with id: [{%s}]",
					consumedCertificateCredential.getId());
			LOG.error(errorMessage);
			throw new CredentialException(errorMessage);
		}
	}

	protected int getTimeout()
	{
		return timeout;
	}

	@Required
	public void setTimeout(final int timeout)
	{
		this.timeout = timeout;
	}

	@Required
	public void setMessageConverters(final List<HttpMessageConverter<Object>> messageConverters)
	{
		this.messageConverters = messageConverters;
	}

	protected List<HttpMessageConverter<Object>> getMessageConverters()
	{
		return messageConverters;
	}


	protected SSLContextFactoryService getSslContextFactoryService()
	{
		return sslContextFactoryService;
	}

	@Required
	public void setSslContextFactoryService(final SSLContextFactoryService sslContextFactoryService)
	{
		this.sslContextFactoryService = sslContextFactoryService;
	}

}
