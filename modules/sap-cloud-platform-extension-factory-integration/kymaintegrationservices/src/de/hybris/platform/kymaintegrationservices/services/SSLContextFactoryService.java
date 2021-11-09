/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.services;

import de.hybris.platform.kymaintegrationservices.exceptions.SSLContextFactoryCreationException;

import javax.net.ssl.KeyManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import java.security.SecureRandom;


/**
 * Interface which can be used to create the SSL context
 */
public interface SSLContextFactoryService
{
	/**
	 * Creates the ssl context
	 * @param certBytes
	 *    byte array of the client certificate data
	 * @param keyBytes
	 *    byte array of the private key
	 * @return SSL context which is initialized with key managers and Trust managers
	 * @throws SSLContextFactoryCreationException thrown when there is a problem in forming Key managers, Trust mangers or SSL context
	 */
	SSLContext createSSLContext(byte[] certBytes, byte[] keyBytes) throws SSLContextFactoryCreationException;

	/**
	 * Creates the array of key managers. Used keystore type is "JKS" and key manager factory algorithm is "SunX509"
	 * @param certBytes
	 *    byte array of the client certificate data
	 * @param keyBytes
	 *    byte array of the private key
	 * @return the array of KeyManagers
	 * @throws SSLContextFactoryCreationException thrown when there is a problem in forming Key managers
	 */
	KeyManager[] createKeyManagerFactory(byte[] certBytes, byte[] keyBytes) throws SSLContextFactoryCreationException;

	/**
	 * Creates the array of Trust managers. In order to check the revocation status of the certificate,
	 * either java.home or customised path to the cacerts must be set. In case both of them are present then the customised path is considered.
	 * If neither is set then the certificate revocation check is not performed.
	 * @return array of TrustManagers, returns an empty array in case either java.home or customised path to the truststore cacerts is not configured.
	 * @throws SSLContextFactoryCreationException thrown when there is a problem in forming trust managers
	 */
	TrustManager[] createTrustManagerFactory() throws SSLContextFactoryCreationException;

	/**
	 * Creates a {@link SecureRandom}
	 * @return
	 *    newly created {@link SecureRandom}
	 */
	SecureRandom createSecureRandom();


}
