/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleocctests.setup;

import de.hybris.platform.commercewebservicestests.setup.TestSetupUtils;
import de.hybris.platform.core.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility class to be used in test suites to manage tests (e.g. start server, load data).
 */
public class BundleTestSetupUtils extends TestSetupUtils
{
	private static final Logger LOG = LoggerFactory.getLogger(BundleTestSetupUtils.class);

	public static void loadExtensionDataInJunit() throws Exception
	{
		Registry.setCurrentTenantByID("junit");
		loadExtensionData();
	}

	public static void loadExtensionData() throws Exception
	{
		// implement your OCC extension test data loading logic here
		loadData();

		LOG.info("No data for current OCC extension.");
		final ConfigurableBundleOCCTestSetup productConfigOCCTestSetup = Registry.getApplicationContext()
				.getBean("configurableBundleOCCTestSetup", ConfigurableBundleOCCTestSetup.class);
		productConfigOCCTestSetup.loadData();
	}
}
