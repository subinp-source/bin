/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.services.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link SymmetricManager}.
 */
public class SymmetricManagerTest
{
	private final static String TEXT = "Banana";
	private String key;

	@Before
	public void setUp()
	{
		this.key = SymmetricManager.getKey();
	}

	@Test
	public void testEncryptDecrypt()
	{
		final String encrypted = SymmetricManager.encrypt(TEXT, key);
		Assert.assertNotNull(encrypted);
		final String decrypted = SymmetricManager.decrypt(encrypted, key);
		Assert.assertNotNull(decrypted);
		Assert.assertEquals(TEXT, decrypted);
	}
}
