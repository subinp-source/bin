/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests.setup;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.user.UserService;


/**
 * Utility class to be used in test suites to manage tests (e.g. start server, load data).
 */
public class TestSetupUtils extends de.hybris.platform.commercewebservicestests.setup.TestSetupUtils
{
	public static void loadExtensionDataInJunit()
	{
		Registry.setCurrentTenantByID("junit");
		loginAdmin();
		loadExtensionData();
	}

	public static void loadExtensionData()
	{
		final B2BOCCTestSetup b2bOccTestSetup = Registry.getApplicationContext().getBean("b2BOCCTestSetup", B2BOCCTestSetup.class);
		b2bOccTestSetup.loadData();
	}

	private static void loginAdmin()
	{
		final UserService userService = Registry.getApplicationContext().getBean("userService", UserService.class);
		userService.setCurrentUser(userService.getAdminUser());
	}

}
