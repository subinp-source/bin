/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.UUID;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.web.client.RestTemplate;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;

/**
 * Default implementation of {@link RestTemplateProvider} for a REST Web Service interface authorized by
 * {@link X509Certificate}.
 * Please do not use this class in your developments as this class will be removed soon.
 */
public class DefaultCertificateCredentialsRestTemplateProvider extends RestTemplateProvider
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCertificateCredentialsRestTemplateProvider.class);

	@Override
	public RestTemplate getRestTemplate(final AbstractCredentialModel abstractCredential) throws CredentialException
	{
		validateCredential(abstractCredential);

		final ConsumedCertificateCredentialModel credential = (ConsumedCertificateCredentialModel) abstractCredential;

		final String randomString = UUID.randomUUID().toString();

		byte[] certBytes = new byte[0];
		byte[] keyBytes = new byte[0];

		try
		{
			final SSLContext context = SSLContext.getInstance("TLSv1.2");
			certBytes = DatatypeConverter.parseBase64Binary(credential.getCertificateData());
			keyBytes = DatatypeConverter.parseBase64Binary(credential.getPrivateKey());

			final X509Certificate[] certChain = SecurityUtils.generateCertificateArrayFromDER(certBytes);
			final RSAPrivateKey key = SecurityUtils.generatePrivateKeyFromDER(keyBytes);

			final KeyStore keystore = KeyStore.getInstance("JKS");
			keystore.load(null);
			keystore.setKeyEntry(randomString, key, randomString.toCharArray(), certChain);

			final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(keystore, randomString.toCharArray());
			context.init(kmf.getKeyManagers(), null, null);


			final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = getRestTemplateHttpClientFactoryProvider()
					.getHttpClient(context);

			return  new RestTemplate(clientHttpRequestFactory);

		}
		catch (final NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException | KeyManagementException
				| CertificateException | IOException | CredentialException e)
		{
			LOG.error(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
			throw new CredentialException(
					String.format("Invalid Certificate Credential with id: [{%s}]", credential.getId()), e);
		}
		finally
		{
			Arrays.fill(certBytes, (byte) 0);
			Arrays.fill(keyBytes, (byte) 0);
		}
	}

	protected void validateCredential(final AbstractCredentialModel abstractCredential) throws CredentialException
	{
		if (!(abstractCredential instanceof ConsumedCertificateCredentialModel))
		{
			final String errorMessage = "Missing Consumed Certificate Credential. Please get a client certificate from an API Registry web-service.";
			LOG.error(errorMessage);
			throw new CredentialException(errorMessage);
		}

		final ConsumedCertificateCredentialModel credential = (ConsumedCertificateCredentialModel) abstractCredential;

		if (StringUtils.isEmpty(credential.getCertificateData()) || StringUtils.isEmpty(credential.getPrivateKey()))
		{
			final String errorMessage = String.format("Invalid Certificate Credential with id: [{%s}]", credential.getId());
			LOG.error(errorMessage);
			throw new CredentialException(errorMessage);
		}
	}

	protected DefaultRestTemplateHttpClientFactoryProvider getRestTemplateHttpClientFactoryProvider()
	{
		return new DefaultRestTemplateHttpClientFactoryProvider();
	}


}
