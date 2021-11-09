/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Collection;

import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;


/**
 * Helper class for security
 */
public class SecurityUtils
{
	private SecurityUtils()
	{
		// empty
	}

	/**
	 * Generates certificate from the given byte array
	 *
	 * @param certBytes
	 *           byte array of certificate
	 * @return X509Certificate
	 * @throws CertificateException
	 */
	public static X509Certificate generateCertificateFromDER(final byte[] certBytes) throws CertificateException
	{
		return (X509Certificate) (new CertificateFactory()).engineGenerateCertificate(new ByteArrayInputStream(certBytes));
	}

	/**
	 * Generates private key from the given byte array
	 *
	 * @param keyBytes
	 *           byte array of private key
	 * @return RSAPrivateKey
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static RSAPrivateKey generatePrivateKeyFromDER(final byte[] keyBytes) throws CredentialException
	{
		final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		try
		{
			final KeyFactory factory = KeyFactory.getInstance("RSA");
			Arrays.fill(keyBytes, (byte) 0);
			return (RSAPrivateKey) factory.generatePrivate(spec);
		}
		catch (final NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			throw new CredentialException(e.getMessage(), e);
		}
	}

	/**
	 * Generates certificate chain from the given byte array
	 *
	 * @param certBytes
	 *           byte array of certificate
	 * @return X509Certificate[]
	 * @throws CertificateException
	 */
	public static X509Certificate[] generateCertificateArrayFromDER(final byte[] certBytes) throws CertificateException
	{
		final Collection<X509Certificate> certificateChain = (new CertificateFactory()).engineGenerateCertificates(new ByteArrayInputStream(certBytes));
		return certificateChain.toArray(new X509Certificate[certificateChain.size()]);
	}


}
