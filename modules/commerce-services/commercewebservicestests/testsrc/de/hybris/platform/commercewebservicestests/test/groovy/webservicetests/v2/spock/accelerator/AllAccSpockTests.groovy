/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.accelerator


import de.hybris.platform.oauth2.constants.OAuth2Constants
import de.hybris.platform.util.Config
import de.hybris.platform.commercewebservicestests.constants.YcommercewebservicestestsConstants
import de.hybris.platform.commercewebservicestests.setup.TestSetupUtils

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite.class)
@Suite.SuiteClasses([SopTest, ExtendedCartV2Tests, ExtendedBaseSitesTest])
class AllAccSpockTests {
	@BeforeClass
	public static void setUpClass() {
		if (Config.getBoolean("commercewebservicestests.enableAccTest", false)) {
			TestSetupUtils.loadData();
			String[] ext = [
					YcommercewebservicestestsConstants.EXTENSIONNAME - "tests",
					OAuth2Constants.EXTENSIONNAME,
					"acceleratorservices"
			]
			TestSetupUtils.startServer(ext);
		}
	}

	@AfterClass
	public static void tearDown() {
		if (Config.getBoolean("commercewebservicestests.enableAccTest", false)) {
			TestSetupUtils.stopServer();
			TestSetupUtils.cleanData();
		}
	}

	@Test
	public static void testing() {
		//dummy test class
	}
}
