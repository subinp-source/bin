/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests;



import de.hybris.platform.b2bocctests.setup.B2BOCCTestSetup;
import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.testsupport.server.EmbeddedServerController;
import de.hybris.platform.commercewebservices.core.constants.YcommercewebservicesConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestSetupStandalone
{
	private static boolean serverStarted = false;
	private static final Logger LOG = LoggerFactory.getLogger(TestSetupStandalone.class);
	private static final String[] EXTENSIONS_TO_START = new String[]
			{ YcommercewebservicesConstants.EXTENSIONNAME };

	public static void loadData()
	{
		loginAdmin();
		loadSampleData();
	}

	public static void startServer()
	{
		if (!serverStarted)
		{
			final String[] ext = EXTENSIONS_TO_START;
			if (!Config.getBoolean("commercewebservicestests.embedded.server.enabled", true))
			{
				LOG.info("Ignoring embedded server");
				return;
			}

			LOG.info("Starting embedded server");
			final EmbeddedServerController controller = Registry.getApplicationContext().getBean("embeddedServerController",
					EmbeddedServerController.class);
			controller.start(ext);
			LOG.info("Embedded server is running");
			serverStarted = true;
		}
		else
		{
			LOG.info("Embedded server already running");
		}

	}

	public static void stopServer()
	{
		if (!Config.getBoolean("commercewebservicestests.embedded.server.enabled", true))
		{
			LOG.info("Ignoring embedded server");
			return;
		}

		LOG.info("Stopping embedded server");
		final EmbeddedServerController controller = Registry.getApplicationContext().getBean("embeddedServerController",
				EmbeddedServerController.class);
		controller.stop();
		LOG.info("Stopped embedded server");
	}

	private static void loginAdmin()
	{
		final UserService userService = Registry.getApplicationContext().getBean("userService", UserService.class);
		userService.setCurrentUser(userService.getAdminUser());
	}

	private static void loadSampleData()
	{
		final B2BOCCTestSetup b2bOccTestSetup = Registry.getApplicationContext()
															 .getBean("b2BOCCTestSetup", B2BOCCTestSetup.class);
		b2bOccTestSetup.loadData();
	}

	public static void cleanData() throws Exception
	{
		LOG.info("Clean data created for test");


		Initialization.initializeTestSystem();
	}
}
