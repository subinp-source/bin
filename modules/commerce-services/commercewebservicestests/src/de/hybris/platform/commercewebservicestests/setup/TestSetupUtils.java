/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.setup;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.payment.commands.factory.CommandFactoryRegistry;
import de.hybris.platform.payment.commands.factory.impl.DefaultCommandFactoryImpl;
import de.hybris.platform.payment.commands.factory.impl.DefaultCommandFactoryRegistryImpl;
import de.hybris.platform.payment.methods.impl.DefaultCardPaymentServiceImpl;
import de.hybris.platform.servicelayer.datasetup.ServiceLayerDataSetup;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.testframework.ItemCreationLifecycleListener;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.webservicescommons.testsupport.server.EmbeddedServerController;
import de.hybris.platform.commercewebservices.core.constants.YcommercewebservicesConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import static de.hybris.platform.testframework.runlistener.ItemCreationListener.ITEM_CREATION_LIFECYCLE_LISTENER;


@SuppressWarnings("deprecation")
public class TestSetupUtils
{
	private static final String[] EXTENSIONS_TO_START = new String[] { YcommercewebservicesConstants.EXTENSIONNAME,
			OAuth2Constants.EXTENSIONNAME };
	private static final Pattern CONFIG_DELIMITER = Pattern.compile("[\\s,]+");

	private static final Logger LOG = LoggerFactory.getLogger(TestSetupUtils.class);

	private static CommandFactoryRegistry oryginalCommandFactoryRegistry;

	public static void loadDataInJunit() throws Exception
	{
		Registry.setCurrentTenantByID("junit");
		loadData();
	}

	public static void loadData() throws Exception
	{
		if (shouldLoadData())
		{
			loginAdmin();
			setupCore();
			localizeTypes();
			initPaymentCommandFactory();
			setupCommerce();
		}
		else
		{
			LOG.info("Data are already loaded");
		}
	}

	private static boolean shouldLoadData()
	{
		final BaseSiteService baseSiteService = Registry.getApplicationContext().getBean("baseSiteService", BaseSiteService.class);
		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID("wsTest");
		return baseSite == null;
	}

	private static void setupCommerce()
	{
		setupCommerce((systemsetup, context) -> systemsetup.createProjectData(context));
	}

	private static void setupCommerce(final BiConsumer<CommercewebservicesTestSetup, SystemSetupContext> triggerEqualsValidator)
	{
		final CommercewebservicesTestSetup setup = getCommerceWebServicesTestSetup();
		final SystemSetupContext context = createSystemSetupContext();
		triggerEqualsValidator.accept(setup, context);
	}

	private static void loginAdmin()
	{
		final UserService userService = Registry.getApplicationContext().getBean("userService", UserService.class);
		userService.setCurrentUser(userService.getAdminUser());
	}

	private static void setupCore() throws Exception
	{
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
		Registry.getApplicationContext().getBean("serviceLayerDataSetup", ServiceLayerDataSetup.class).setup();
		new CatalogManager().createEssentialData(Collections.singletonMap("initmethod", "init"), null);
	}

	private static void localizeTypes()
	{
		de.hybris.platform.util.localization.TypeLocalization.getInstance().localizeTypes();
	}

	private static void initPaymentCommandFactory()
	{
		final DefaultCommandFactoryRegistryImpl commandFactoryReg = Registry.getApplicationContext()
				.getBean("commandFactoryRegistry", DefaultCommandFactoryRegistryImpl.class);
		// set command factory because tests from yacceleratorfulfilmentprocess remove it
		commandFactoryReg.setCommandFactoryList(Collections.singletonList(createPaymentCommandFactory()));

		//set command factory registry because some tests change it and not restore (QuotesProcessIntegrationTest,AnonymousCheckoutIntegrationTest,DefaultExpressCheckoutIntegrationCheckoutTest)
		final DefaultCardPaymentServiceImpl cartPaymentService = Registry.getApplicationContext()
				.getBean("cardPaymentService", DefaultCardPaymentServiceImpl.class);
		oryginalCommandFactoryRegistry = cartPaymentService.getCommandFactoryRegistry();
		cartPaymentService.setCommandFactoryRegistry(commandFactoryReg);
	}

	private static DefaultCommandFactoryImpl createPaymentCommandFactory()
	{
		final Map<Object, Object> commands = new HashMap<>();
		commands.put(de.hybris.platform.payment.commands.IsApplicableCommand.class,
				new de.hybris.platform.payment.commands.impl.IsApplicableMockCommand());
		commands.put(de.hybris.platform.payment.commands.AuthorizationCommand.class,
				new de.hybris.platform.payment.commands.impl.AuthorizationMockCommand());
		commands.put(de.hybris.platform.payment.commands.SubscriptionAuthorizationCommand.class,
				new de.hybris.platform.payment.commands.impl.SubscriptionAuthorizationMockCommand());
		commands.put(de.hybris.platform.payment.commands.CaptureCommand.class,
				new de.hybris.platform.payment.commands.impl.CaptureMockCommand());
		commands.put(de.hybris.platform.payment.commands.PartialCaptureCommand.class,
				new de.hybris.platform.payment.commands.impl.PartialCaptureMockCommand());
		commands.put(de.hybris.platform.payment.commands.EnrollmentCheckCommand.class,
				new de.hybris.platform.payment.commands.impl.EnrollmentCheckMockCommand());
		commands.put(de.hybris.platform.payment.commands.VoidCommand.class,
				new de.hybris.platform.payment.commands.impl.VoidMockCommand());
		commands.put(de.hybris.platform.payment.commands.FollowOnRefundCommand.class,
				new de.hybris.platform.payment.commands.impl.FollowOnRefundMockCommand());
		commands.put(de.hybris.platform.payment.commands.StandaloneRefundCommand.class,
				new de.hybris.platform.payment.commands.impl.StandaloneRefundMockCommand());
		commands.put(de.hybris.platform.payment.commands.CreateSubscriptionCommand.class,
				new de.hybris.platform.payment.commands.impl.CreateSubscriptionMockCommand());
		commands.put(de.hybris.platform.payment.commands.DeleteSubscriptionCommand.class,
				new de.hybris.platform.payment.commands.impl.DeleteSubscriptionMockCommand());
		commands.put(de.hybris.platform.payment.commands.GetSubscriptionDataCommand.class,
				new de.hybris.platform.payment.commands.impl.GetSubscriptionDataMockCommand());
		commands.put(de.hybris.platform.payment.commands.UpdateSubscriptionCommand.class,
				new de.hybris.platform.payment.commands.impl.UpdateSubscriptionMockCommand());

		final DefaultCommandFactoryImpl commandFactory = new DefaultCommandFactoryImpl();
		commandFactory.setCommands((Map) commands);
		commandFactory.setPaymentProvider("Mockup");

		return commandFactory;
	}



	private static CommercewebservicesTestSetup getCommerceWebServicesTestSetup()
	{
		return Registry.getApplicationContext().getBean("yCommerceWebServicesTestSetup", CommercewebservicesTestSetup.class);
	}

	private static SystemSetupContext createSystemSetupContext()
	{
		final Map<String, String[]> params = new HashMap<>();
		params.put("init", new String[] { "Go" });
		params.put("commercewebservicestests_sample", new String[] { "true" });
		params.put("lucenesearch_rebuild", new String[] { "true" });
		params.put("lucenesearch_update.index.configuration", new String[] { "true" });

		return new SystemSetupContext(params, Type.ALL, Process.ALL, "commercewebservicestests");
	}

	public static void cleanData() throws Exception
	{
		LOG.info("Clean data created for test");
		//restore oryginal values
		final DefaultCommandFactoryRegistryImpl commandFactoryReg = Registry.getApplicationContext()
				.getBean("commandFactoryRegistry", DefaultCommandFactoryRegistryImpl.class);
		commandFactoryReg.afterPropertiesSet();
		if (oryginalCommandFactoryRegistry != null)
		{
			final DefaultCardPaymentServiceImpl cartPaymentService = Registry.getApplicationContext()
					.getBean("cardPaymentService", DefaultCardPaymentServiceImpl.class);
			cartPaymentService.setCommandFactoryRegistry(oryginalCommandFactoryRegistry);
		}

		Initialization.initializeTestSystem();
		afterInitializeTestSystem();
	}

	/**
	 * Re-register the ItemCreationLifecycleListener in the persistence pool after test system initialization
	 */
	public static void afterInitializeTestSystem()
	{
		final ApplicationContext ctx = Registry.getCoreApplicationContext();
		if (ctx.containsBean(ITEM_CREATION_LIFECYCLE_LISTENER))
		{
			final ItemCreationLifecycleListener itemCreationLifecycleListener = (ItemCreationLifecycleListener) ctx
					.getBean(ITEM_CREATION_LIFECYCLE_LISTENER);
			// re-register listener, unregister first to prevent duplication
			Registry.getCurrentTenant().getPersistencePool().unregisterPersistenceListener(itemCreationLifecycleListener);
			Registry.getCurrentTenant().getPersistencePool().registerPersistenceListener(itemCreationLifecycleListener);
		}
	}

	public static boolean isServerStarted()
	{
		final EmbeddedServerController controller = Registry.getApplicationContext()
				.getBean("embeddedServerController", EmbeddedServerController.class);
		return controller.ensureWebAppsAreStarted(EXTENSIONS_TO_START);
	}

	public static void startServer()
	{
		startServer(EXTENSIONS_TO_START);
		if (isNewPromotionEngineTurnedOn())
		{
			final CommercewebservicesTestSetup setup = getCommerceWebServicesTestSetup();
			setup.initializePromotionEngineModule();
		}
	}

	public static void startServer(final String[] ext)
	{
		if (!Config.getBoolean("commercewebservicestests.embedded.server.enabled", true))
		{
			LOG.info("Ignoring embedded server");
			return;
		}

		LOG.info("Starting embedded server");
		final EmbeddedServerController controller = Registry.getApplicationContext()
				.getBean("embeddedServerController", EmbeddedServerController.class);
		controller.start(ext);
		LOG.info("embedded server is running");

	}

	public static void stopServer()
	{
		if (!Config.getBoolean("commercewebservicestests.embedded.server.enabled", true))
		{
			LOG.info("Ignoring embedded server");
			return;
		}

		LOG.info("Stopping embedded server");
		final EmbeddedServerController controller = Registry.getApplicationContext()
				.getBean("embeddedServerController", EmbeddedServerController.class);
		controller.stop();
		LOG.info("Stopped embedded server");
	}

	public static boolean isNewPromotionEngineTurnedOn()
	{
		return Objects.nonNull(Utilities.getExtensionInfo("promotionengineservices"));
	}

	public static boolean isExtensionPresent(final String name)
	{
		final boolean present = Utilities.getExtensionInfo(name) != null;
		LOG.info("isExtensionPresent={} for extension {}", present, name);
		return present;
	}

	public static boolean anyExtensionPresent(final String configKey)
	{
		final String extensions = Config.getString(configKey, "");
		LOG.info("Property key {} returned value {}", configKey, extensions);
		return CONFIG_DELIMITER.splitAsStream(extensions) //
				.anyMatch(TestSetupUtils::isExtensionPresent);
	}

	public static boolean allExtensionsPresent(final String configKey)
	{
		final String extensions = Config.getString(configKey, "");
		LOG.info("Property key {} returned value {}", configKey, extensions);
		return CONFIG_DELIMITER.splitAsStream(extensions) //
				.allMatch(TestSetupUtils::isExtensionPresent);
	}
}
