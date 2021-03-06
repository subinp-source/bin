/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.punchout.PunchOutCipherException;
import de.hybris.platform.b2b.punchout.PunchOutSession;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Unit test for class {@link DefaultCipherServiceTest}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCipherServiceTest
{
	public static final String EMAIL = "a@b.com";
	public static final String WRONG_EMAIL = "c@d.com";

	private DefaultCipherService cipherService;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration configuration;

	@Before
	public void setup()
	{
		cipherService = new DefaultCipherService();
		cipherService.setConfigurationService(configurationService);
		when(configurationService.getConfiguration()).thenReturn(configuration);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEncryptWithNullArgument()
	{
		cipherService.encrypt(null, null);
	}

	@Test
	public void canGenerateHash()
	{
		final PunchOutSession punchoutSession = new PunchOutSession();
		final String salt = AsymmetricManager.getSalt();
		final String key = SymmetricManager.getKey();
		punchoutSession.setSalt(salt);
		punchoutSession.setKey(key);
		punchoutSession.setTime(new Date());
		final String hash = cipherService.encrypt(EMAIL, punchoutSession);
		assertNotNull(hash);
		assertFalse(EMAIL.equals(hash));
	}

	@Test
	public void encryptSetsPunchOutSessionValues()
	{
		final PunchOutSession punchoutSession = new PunchOutSession();
		final String salt = AsymmetricManager.getSalt();
		final String key = SymmetricManager.getKey();
		punchoutSession.setSalt(salt);
		punchoutSession.setKey(key);
		punchoutSession.setTime(new Date());
		cipherService.encrypt(EMAIL, punchoutSession);
		assertNotNull(punchoutSession.getSalt());
		assertNotNull(punchoutSession.getKey());
	}

	@Test
	public void rightUserPassesVerification()
	{
		final PunchOutSession punchoutSession = new PunchOutSession();
		final String salt = AsymmetricManager.getSalt();
		final String key = SymmetricManager.getKey();
		punchoutSession.setSalt(salt);
		punchoutSession.setKey(key);
		punchoutSession.setTime(new Date());

		final String hash = cipherService.encrypt(EMAIL, punchoutSession);
		// decode hash to simulate external call
		final String decodedHash = decode(hash);

		final String userId = cipherService.retrieveUserId(decodedHash, punchoutSession);
		assertNotNull(userId);
		assertEquals(EMAIL, userId);
	}

	@Test(expected = PunchOutCipherException.class)
	public void badUserFailsVerification()
	{
		final PunchOutSession punchoutSession = new PunchOutSession();
		final String salt = AsymmetricManager.getSalt();
		final String key = SymmetricManager.getKey();
		punchoutSession.setSalt(salt);
		punchoutSession.setKey(key);
		punchoutSession.setTime(new Date());
		final String originalHash = cipherService.encrypt(WRONG_EMAIL, punchoutSession);
		assertNotNull(originalHash);

		// generate another hash (different from the one generated initially)
		final PunchOutSession hackPunchOutSession = new PunchOutSession();
		final String salt1 = AsymmetricManager.getSalt();
		final String key1 = SymmetricManager.getKey();
		hackPunchOutSession.setSalt(salt1);
		hackPunchOutSession.setKey(key1);
		hackPunchOutSession.setTime(new Date());
		final String hackHash = cipherService.encrypt(WRONG_EMAIL, hackPunchOutSession);
		assertNotNull(hackHash);
		assertFalse(originalHash.equals(hackHash));

		// decode hash to simulate external call
		final String decodedHackHash = decode(hackHash);

		// compare new user with old session
		cipherService.retrieveUserId(decodedHackHash, punchoutSession);

	}

	private static String decode(final String encoded)
	{
		try
		{
			return URLDecoder.decode(encoded, DefaultCipherService.CHARACTER_ENCODING);
		}
		catch (final UnsupportedEncodingException e)
		{
			return null;
		}
	}
}
