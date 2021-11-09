/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.impex.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class FileLoaderValueTranslatorTest extends ServicelayerTransactionalTest
{
	@Test
	public void testJarFileLoad() throws Exception
	{
		final FileLoaderValueTranslator fileLoaderValueTranslator = new FileLoaderValueTranslator();
		final String file = "jar:de.hybris.platform.commerceservices.jalo.CommerceServicesManager&/commerceservices/test/fileLoaderTranslatorTest.impex";
		final String input = fileLoaderValueTranslator.importData(file);

		Assert.assertTrue("Invalid file length", input.length() > 0);
	}
}
