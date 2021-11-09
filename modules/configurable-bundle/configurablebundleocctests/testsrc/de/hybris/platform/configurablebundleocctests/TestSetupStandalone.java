/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleocctests;

import de.hybris.platform.commercewebservices.core.constants.YcommercewebservicesConstants;
import de.hybris.platform.commercewebservicestests.setup.TestSetupUtils;
import de.hybris.platform.configurablebundleocctests.setup.ConfigurableBundleOCCTestSetup;
import de.hybris.platform.configurablebundleocctests.setup.BundleTestSetupUtils;
import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.Registry;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.payment.commands.factory.impl.DefaultCommandFactoryRegistryImpl;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.testsupport.server.EmbeddedServerController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestSetupStandalone
{
	private static boolean serverStarted = false;
	private static final Logger LOG = LoggerFactory.getLogger(TestSetupStandalone.class);
	private static final String[] EXTENSIONS_TO_START = new String[]
			{ YcommercewebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME };

	public static void loadData() throws Exception
	{
		TestSetupUtils.loadData();
		final ConfigurableBundleOCCTestSetup productConfigOCCTestSetup = Registry.getApplicationContext()
				.getBean("configurableBundleOCCTestSetup", ConfigurableBundleOCCTestSetup.class);
		productConfigOCCTestSetup.loadData();

	}

	public static void cleanData() throws Exception
	{
		LOG.info("Clean data created for test");
		final DefaultCommandFactoryRegistryImpl commandFactoryReg = Registry.getApplicationContext()
				.getBean("commandFactoryRegistry", DefaultCommandFactoryRegistryImpl.class);
		commandFactoryReg.afterPropertiesSet();
		// import, this cleans up the testdata created - if removed you will see failiures in pipeline were more tests are executed.
		Initialization.initializeTestSystem();
		BundleTestSetupUtils.afterInitializeTestSystem();
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
			LOG.info("embedded server is running");
			serverStarted = true;
		}
		else
		{
			LOG.info("embedded server already running");
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
}
