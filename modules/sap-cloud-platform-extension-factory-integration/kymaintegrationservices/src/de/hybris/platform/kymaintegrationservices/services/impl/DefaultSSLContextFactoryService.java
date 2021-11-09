/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.services.impl;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.kymaintegrationservices.exceptions.SSLContextFactoryCreationException;
import de.hybris.platform.apiregistryservices.utils.SecurityUtils;
import de.hybris.platform.kymaintegrationservices.services.SSLContextFactoryService;
import de.hybris.platform.util.Config;

import javax.net.ssl.CertPathTrustManagerParameters;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertificateException;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.EnumSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default implementation of {@link SSLContextFactoryService}.
 */
public class DefaultSSLContextFactoryService implements SSLContextFactoryService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSSLContextFactoryService.class);

	private static final String TRUSTSTORE_PASS = "kymaintegrationservices.truststore.password";
	private static final String ACTIVATE_REVOCATION_CHECK = "kymaintegrationservices.revocation.check.enabled";
	private static final String ACTIVATE_CRL_PROTOCOL = "kymaintegrationservices.revocation.check.CRL";
	private static final String TRUSTSTORE_CACERTS_PATH = "kymaintegrationservices.truststore.cacerts.path";

	@Override
	public SSLContext createSSLContext(final byte[] certBytes, final byte[] keyBytes) throws SSLContextFactoryCreationException
	{
		try
		{
			final SSLContext context = SSLContext.getInstance("TLSv1.2");
			final KeyManager[] keyManagers = createKeyManagerFactory(certBytes, keyBytes);
			TrustManager[] trustManagers = null;
			if(Config.getBoolean(ACTIVATE_REVOCATION_CHECK, true))
			{
				final TrustManager[] returnedTrustManager = createTrustManagerFactory();
				trustManagers = returnedTrustManager.length == 0 ? null : returnedTrustManager;
			}
			context.init(keyManagers, trustManagers, createSecureRandom());
			return context;
		}

		catch (SSLContextFactoryCreationException | NoSuchAlgorithmException | KeyManagementException e)
		{
			LOG.error(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
			throw new SSLContextFactoryCreationException("Exception while creating the SSL context ", e);
		}
	}

	@Override
	public KeyManager[] createKeyManagerFactory(final byte[] certBytes, final byte[] keyBytes) throws SSLContextFactoryCreationException
	{
		try
		{
			final String randomString = UUID.randomUUID().toString();
			final X509Certificate[] certChain = SecurityUtils.generateCertificateArrayFromDER(certBytes);
			final RSAPrivateKey key = SecurityUtils.generatePrivateKeyFromDER(keyBytes);

			final KeyStore keystore = KeyStore.getInstance("JKS");
			keystore.load(null);

			keystore.setKeyEntry(randomString, key, randomString.toCharArray(), certChain);

			final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			keyManagerFactory.init(keystore, randomString.toCharArray());

			return keyManagerFactory.getKeyManagers();
		}
		catch (CertificateException | IOException | NoSuchAlgorithmException | UnrecoverableKeyException
				| CredentialException | KeyStoreException e)
		{
			LOG.error(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
			throw new SSLContextFactoryCreationException("Exception when creating the keyManagers", e);
		}
	}

	@Override
	public TrustManager[] createTrustManagerFactory() throws SSLContextFactoryCreationException
	{
		final char SEP = File.separatorChar;
		if(System.getProperty("java.home") == null && Config.getParameter(TRUSTSTORE_CACERTS_PATH) == null)
		{
			return new TrustManager[0];
		}
		final String fileName = System.getProperty("java.home") + SEP + "lib" + SEP + "security" + SEP + "cacerts";

		final String cacertsPath = Config.getString(TRUSTSTORE_CACERTS_PATH, fileName);
		try(final InputStream fileInputStream = new FileInputStream(cacertsPath))
		{
			final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			final String trustStorePassword = Config.getString(TRUSTSTORE_PASS, "changeit");
			trustStore.load(fileInputStream, trustStorePassword.toCharArray());

			final CertPathBuilder certificatePathBuilder = CertPathBuilder.getInstance("PKIX");
			final PKIXRevocationChecker revocationChecker = (PKIXRevocationChecker)certificatePathBuilder.getRevocationChecker();
			if(!Config.getBoolean(ACTIVATE_CRL_PROTOCOL,false))
			{
				revocationChecker.setOptions(EnumSet.of(
						PKIXRevocationChecker.Option.NO_FALLBACK,
						PKIXRevocationChecker.Option.SOFT_FAIL));
			}
			else
			{
				revocationChecker.setOptions(EnumSet.of(PKIXRevocationChecker.Option.SOFT_FAIL));
			}

			final PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustStore, new X509CertSelector());
			pkixParams.addCertPathChecker(revocationChecker);

			final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(new CertPathTrustManagerParameters(pkixParams));
			return trustManagerFactory.getTrustManagers();
		}

		catch (InvalidAlgorithmParameterException | CertificateException | IOException |  NoSuchAlgorithmException | KeyStoreException e)
		{
			LOG.error(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
			throw new SSLContextFactoryCreationException("Exception while creating the TrustMangers", e);
		}

	}

	@Override
	public SecureRandom createSecureRandom()
	{
		return null;
	}

}
