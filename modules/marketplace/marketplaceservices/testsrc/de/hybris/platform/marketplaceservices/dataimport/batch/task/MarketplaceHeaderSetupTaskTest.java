/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dataimport.batch.task;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.MockitoAnnotations;


@UnitTest
public class MarketplaceHeaderSetupTaskTest
{
	private static final String VENDOR_CODE = "vendor1";

	private static final String FILE_NAME = "base_product-en-35.csv";

	private MarketplaceHeaderSetupTask marketplaceHeaderSetupTask;

	private File file;

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private String storeBaseDirectory;

	@Before
	public void prepare() throws IOException
	{
		MockitoAnnotations.initMocks(this);

		storeBaseDirectory = tempFolder.getRoot().getAbsolutePath();
		marketplaceHeaderSetupTask = new MarketplaceHeaderSetupTask();
		marketplaceHeaderSetupTask.setStoreBaseDirectory(storeBaseDirectory);

		final File folder = tempFolder.newFolder(VENDOR_CODE, "processing");
		file = new File(folder, FILE_NAME);
	}

	@Test
	public void testExecute()
	{
		final BatchHeader header = marketplaceHeaderSetupTask.execute(file);
		assertEquals(storeBaseDirectory + File.separator + VENDOR_CODE, header.getStoreBaseDirectory());
	}


}
