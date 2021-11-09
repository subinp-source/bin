/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.services.impl;

import de.hybris.platform.b2b.punchout.PunchOutCipherException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;


/**
 * Utility class used for symmetric encryption.
 */
public class SymmetricManager
{

	private static final String ALGORITHM = "AES";
	private static final String FULL_ALGORITHM = "AES/GCM/NoPadding";
	private static final int KEY_SIZE = 128;
	private static final int IV_SIZE = 12;
	private static final Logger LOG = Logger.getLogger(SymmetricManager.class);

	private SymmetricManager()
	{
		throw new IllegalStateException("Cannot Instantiate an Utility Class");
	}

	public static String encrypt(final String unsecureText, final String key) throws PunchOutCipherException
	{
		String encrypted = null;
		try
		{
			// Initialization vector
			final byte[] iv = new byte[IV_SIZE];
			new SecureRandom().nextBytes(iv);

			// Initialize Cipher instance
			final Key skeySpec = new SecretKeySpec(new Base64().decode(key), ALGORITHM);
			final Cipher cipher = Cipher.getInstance(FULL_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new GCMParameterSpec(KEY_SIZE, iv));

			// Fill byte array with encrypted message
			final byte[] encryptedValue = cipher.doFinal(unsecureText.getBytes(StandardCharsets.UTF_8));
			final ByteBuffer byteBuffer = ByteBuffer.allocate((Integer.SIZE / Byte.SIZE) + iv.length + encryptedValue.length);
			byteBuffer.putInt(iv.length);
			byteBuffer.put(iv);
			byteBuffer.put(encryptedValue);

			encrypted = new Base64().encodeAsString(byteBuffer.array());
		}
		catch (final NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e) // NOSONAR
		{
			// should never happen
			LOG.error("System was unable instantiate Cipher.");
		}
		catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e)
		{
			final String msg = "Error occured during encryption.";
			LOG.error(msg);
			throw new PunchOutCipherException(msg, e);
		}

		return encrypted;
	}

	public static String decrypt(final String encrypted, final String key) throws PunchOutCipherException
	{
		String decrypted = null;

		try
		{
			// Retrieve IV and encrypted message
			final ByteBuffer byteBuffer = ByteBuffer.wrap(new Base64().decode(encrypted));
			final int ivLength = byteBuffer.getInt();
			final byte[] iv = new byte[ivLength];
			byteBuffer.get(iv);
			final byte[] encryptedValue = new byte[byteBuffer.remaining()];
			byteBuffer.get(encryptedValue);

			// Initialize Cipher instance
			final Key skeySpec = new SecretKeySpec(new Base64().decode(key), ALGORITHM);
			final Cipher cipher = Cipher.getInstance(FULL_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, new GCMParameterSpec(KEY_SIZE, iv));

			// Decrypt message
			final byte[] decryptedValue = cipher.doFinal(encryptedValue);
			decrypted = new String(decryptedValue, StandardCharsets.UTF_8);
		}
		catch (final NoSuchAlgorithmException | NoSuchPaddingException e) // NOSONAR
		{
			// should never happen
			LOG.error("System was unable instantiate Cipher.");
		}
		catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e)
		{
			final String msg = "Error occured during decryption.";
			LOG.info(msg);
			throw new PunchOutCipherException(msg, e);
		}
		return decrypted;
	}

	public static String getKey()
	{
		String key = null;
		try
		{
			final KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
			kgen.init(KEY_SIZE);
			// Generate the secret key specs.
			final SecretKey skey = kgen.generateKey();
			final byte[] raw = skey.getEncoded();
			key = new Base64().encodeAsString(raw);
		}
		catch (final NoSuchAlgorithmException e) // NOSONAR
		{
			// should never happen
			LOG.error("System was unable to generate the key.");
		}
		return key;
	}
}
