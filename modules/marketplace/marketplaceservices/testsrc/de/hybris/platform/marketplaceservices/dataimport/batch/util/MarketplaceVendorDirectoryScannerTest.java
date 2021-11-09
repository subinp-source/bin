/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dataimport.batch.util;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


@UnitTest
public class MarketplaceVendorDirectoryScannerTest
{
	private static final String FILE_VALID_NAME = "base_product-en-1.csv";
	private static final String FILE_INVALID_NAME = "base_product-en-1.csv1";

	private final MarketplaceVendorDirectoryScanner scanner = new MarketplaceVendorDirectoryScanner();

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();

	@Before
	public void prepare() throws IOException
	{
		scanner.setFileNamePatten("^(.*)-(\\d+)\\.csv");
	}

	@Test
	public void testFileInVendorDirectory() throws IOException
	{
		final File vendorFolder = rootFolder.newFolder("vendor1");
		new File(vendorFolder, FILE_VALID_NAME).createNewFile();

		final File[] result = scanner.listEligibleFiles(rootFolder.getRoot());
		assertEquals(result.length, 1);
		assertEquals(result[0].getName(), FILE_VALID_NAME);
	}

	@Test
	public void testFileInVendorChildDirectory() throws IOException
	{
		final File vendorArchiveFolder = rootFolder.newFolder("vendor1", "archive");
		vendorArchiveFolder.mkdir();
		new File(vendorArchiveFolder, FILE_VALID_NAME).createNewFile();

		final File[] result = scanner.listEligibleFiles(rootFolder.getRoot());
		assertEquals(result.length, 0);
	}

	@Test
	public void testFileInVendorParentDirectory() throws IOException
	{
		new File(rootFolder.getRoot(), FILE_VALID_NAME).createNewFile();

		final File[] result = scanner.listEligibleFiles(rootFolder.getRoot());
		assertEquals(result.length, 0);
	}

	@Test
	public void testFileMatching() throws IOException
	{
		final File vendorFolder = rootFolder.newFolder("vendor1");
		new File(vendorFolder, FILE_INVALID_NAME).createNewFile();

		final File[] result = scanner.listEligibleFiles(rootFolder.getRoot());
		assertEquals(result.length, 0);
	}

}
