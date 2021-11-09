/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.b2bservices.strategies.lifecycle.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationEngineException;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.external.Configuration;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.ConfigurationRetrievalOptions;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationPersistenceService;
import de.hybris.platform.sap.productconfig.services.model.ProductConfigurationModel;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationLifecycleStrategy;
import de.hybris.platform.servicelayer.user.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@SuppressWarnings("javadoc")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PersistenceConfigurationLifecycleB2BStrategyImplTest
{
	private static final String VARIANT_PRODUCT_CODE = "variant product code";

	private static final String BASE_PRODUCT_CODE = "base product code";

	private static final String EXTERNAL_CONFIGURATION = "external configuration";

	private static final String CONFIG_ID = "config id";

	private static final String USER_SESSION_ID = "user session id";

	@InjectMocks
	private PersistenceConfigurationLifecycleB2BStrategyImpl classUnderTest;

	@Mock
	private ConfigurationLifecycleStrategy configurationLifecycleStrategy;
	@Mock
	private UserService userService;
	@Mock
	private ProductConfigurationPersistenceService persistenceService;
	@Mock
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

	@Mock
	private KBKey kbKey;
	@Mock
	private ConfigModel configModel;
	@Mock
	private ConfigurationRetrievalOptions options;
	@Mock
	private Configuration configuration;


	private final ProductConfigurationModel productConfiguration = new ProductConfigurationModel();
	@Mock
	private UserModel currentUser;
	@Mock
	private UserModel configUser;
	@Mock
	private B2BCustomerModel b2bConfigUser;
	@Mock
	private B2BCustomerModel b2bCurrentUser;
	@Mock
	private B2BUnitModel configUserB2bUnit;
	@Mock
	private B2BUnitModel currentUserB2bUnit;
	@Mock
	private B2BUnitModel rootB2bUnit;

	@Before
	public void setup()
	{
		when(b2bUnitService.getParent(b2bConfigUser)).thenReturn(configUserB2bUnit);
		when(b2bUnitService.getParent(b2bCurrentUser)).thenReturn(currentUserB2bUnit);
		when(b2bUnitService.getRootUnit(configUserB2bUnit)).thenReturn(rootB2bUnit);
		when(b2bUnitService.getRootUnit(currentUserB2bUnit)).thenReturn(rootB2bUnit);
		when(configUserB2bUnit.getUid()).thenReturn("uid config user b2b unit");
		when(currentUserB2bUnit.getUid()).thenReturn("uid current user b2b unit");
		when(rootB2bUnit.getUid()).thenReturn("uid root b2b unit");
	}

	@Test
	public void testCreateDefaultConfiguration()
	{
		when(configurationLifecycleStrategy.createDefaultConfiguration(kbKey)).thenReturn(configModel);
		final ConfigModel result = classUnderTest.createDefaultConfiguration(kbKey);
		verify(configurationLifecycleStrategy).createDefaultConfiguration(kbKey);
		assertEquals(configModel, result);
	}

	@Test
	public void testUpdateConfiguration() throws ConfigurationEngineException
	{
		when(configurationLifecycleStrategy.updateConfiguration(configModel)).thenReturn(true);
		final boolean result = classUnderTest.updateConfiguration(configModel);
		assertTrue(result);
		verify(configurationLifecycleStrategy).updateConfiguration(configModel);
	}

	@Test
	public void testUpdateUserLinkToConfiguration()
	{
		classUnderTest.updateUserLinkToConfiguration(USER_SESSION_ID);
		verify(configurationLifecycleStrategy).updateUserLinkToConfiguration(USER_SESSION_ID);
	}

	@Test
	public void testRetrieveConfigurationModel() throws ConfigurationEngineException
	{
		when(configurationLifecycleStrategy.retrieveConfigurationModel(CONFIG_ID)).thenReturn(configModel);
		final ConfigModel result = classUnderTest.retrieveConfigurationModel(CONFIG_ID);
		assertEquals(configModel, result);
		verify(configurationLifecycleStrategy).retrieveConfigurationModel(CONFIG_ID);
	}

	@Test
	public void testRetrieveConfigurationModelWithOptions() throws ConfigurationEngineException
	{
		when(configurationLifecycleStrategy.retrieveConfigurationModel(CONFIG_ID, options)).thenReturn(configModel);
		final ConfigModel result = classUnderTest.retrieveConfigurationModel(CONFIG_ID, options);
		assertEquals(configModel, result);
		verify(configurationLifecycleStrategy).retrieveConfigurationModel(CONFIG_ID, options);
	}

	@Test
	public void testRetrieveExternalConfiguration() throws ConfigurationEngineException
	{
		when(configurationLifecycleStrategy.retrieveExternalConfiguration(CONFIG_ID)).thenReturn(EXTERNAL_CONFIGURATION);
		final String result = classUnderTest.retrieveExternalConfiguration(CONFIG_ID);
		assertEquals(EXTERNAL_CONFIGURATION, result);
		verify(configurationLifecycleStrategy).retrieveExternalConfiguration(CONFIG_ID);
	}

	@Test
	public void testCreateConfigurationFromExternalSourceSOM()
	{
		when(configurationLifecycleStrategy.createConfigurationFromExternalSource(configuration)).thenReturn(configModel);
		final ConfigModel result = classUnderTest.createConfigurationFromExternalSource(configuration);
		assertEquals(configModel, result);
		verify(configurationLifecycleStrategy).createConfigurationFromExternalSource(configuration);
	}

	@Test
	public void testCreateConfigurationFromExternalSource()
	{
		when(configurationLifecycleStrategy.createConfigurationFromExternalSource(kbKey, EXTERNAL_CONFIGURATION))
				.thenReturn(configModel);
		final ConfigModel result = classUnderTest.createConfigurationFromExternalSource(kbKey, EXTERNAL_CONFIGURATION);
		assertEquals(configModel, result);
		verify(configurationLifecycleStrategy).createConfigurationFromExternalSource(kbKey, EXTERNAL_CONFIGURATION);
	}

	@Test
	public void testReleaseSession()
	{
		classUnderTest.releaseSession(CONFIG_ID);
		verify(configurationLifecycleStrategy).releaseSession(CONFIG_ID);
	}

	@Test
	public void testReleaseExpiredSessions()
	{
		classUnderTest.releaseExpiredSessions(USER_SESSION_ID);
		verify(configurationLifecycleStrategy).releaseExpiredSessions(USER_SESSION_ID);
	}

	@Test
	public void testRetrieveConfigurationFromVariant()
	{
		when(configurationLifecycleStrategy.retrieveConfigurationFromVariant(BASE_PRODUCT_CODE, VARIANT_PRODUCT_CODE))
				.thenReturn(configModel);
		final ConfigModel result = classUnderTest.retrieveConfigurationFromVariant(BASE_PRODUCT_CODE, VARIANT_PRODUCT_CODE);
		assertEquals(configModel, result);
		verify(configurationLifecycleStrategy).retrieveConfigurationFromVariant(BASE_PRODUCT_CODE, VARIANT_PRODUCT_CODE);
	}

	@Test
	public void testIsConfigForCurrentUserSameRootUnit()
	{
		productConfiguration.setUser(b2bConfigUser);
		when(persistenceService.getByConfigId(CONFIG_ID)).thenReturn(productConfiguration);
		when(userService.getCurrentUser()).thenReturn(b2bCurrentUser);

		when(configurationLifecycleStrategy.isConfigForCurrentUser(CONFIG_ID)).thenReturn(false);
		assertTrue(classUnderTest.isConfigForCurrentUser(CONFIG_ID));
		verify(configurationLifecycleStrategy).isConfigForCurrentUser(CONFIG_ID);
	}

	@Test
	public void testIsConfigForCurrentUserSameUser()
	{
		productConfiguration.setUser(b2bConfigUser);
		when(persistenceService.getByConfigId(CONFIG_ID)).thenReturn(productConfiguration);
		when(userService.getCurrentUser()).thenReturn(b2bCurrentUser);
		when(b2bUnitService.getRootUnit(currentUserB2bUnit)).thenReturn(currentUserB2bUnit);
		when(configurationLifecycleStrategy.isConfigForCurrentUser(CONFIG_ID)).thenReturn(true);
		assertTrue(classUnderTest.isConfigForCurrentUser(CONFIG_ID));
		verify(configurationLifecycleStrategy).isConfigForCurrentUser(CONFIG_ID);
	}

	@Test
	public void testIsConfigForCurrentDifferentUserDifferentRootUnit()
	{
		productConfiguration.setUser(b2bConfigUser);
		when(persistenceService.getByConfigId(CONFIG_ID)).thenReturn(productConfiguration);
		when(userService.getCurrentUser()).thenReturn(b2bCurrentUser);
		when(b2bUnitService.getRootUnit(currentUserB2bUnit)).thenReturn(currentUserB2bUnit);
		when(configurationLifecycleStrategy.isConfigForCurrentUser(CONFIG_ID)).thenReturn(false);
		assertFalse(classUnderTest.isConfigForCurrentUser(CONFIG_ID));
		verify(configurationLifecycleStrategy).isConfigForCurrentUser(CONFIG_ID);
	}

	@Test
	public void testIsConfigInB2bUnitForCurrentUser()
	{
		productConfiguration.setUser(b2bConfigUser);
		when(persistenceService.getByConfigId(CONFIG_ID)).thenReturn(productConfiguration);
		when(userService.getCurrentUser()).thenReturn(b2bCurrentUser);
		assertTrue(classUnderTest.isConfigInB2bUnitForCurrentUser(CONFIG_ID));
	}

	@Test
	public void testIsConfigInB2bUnitForCurrentUserDifferentRootUnit()
	{
		productConfiguration.setUser(b2bConfigUser);
		when(persistenceService.getByConfigId(CONFIG_ID)).thenReturn(productConfiguration);
		when(userService.getCurrentUser()).thenReturn(b2bCurrentUser);
		when(b2bUnitService.getRootUnit(currentUserB2bUnit)).thenReturn(currentUserB2bUnit);
		assertFalse(classUnderTest.isConfigInB2bUnitForCurrentUser(CONFIG_ID));
	}

	@Test
	public void testIsConfigInB2bUnitForCurrentUserNotB2BUser()
	{
		productConfiguration.setUser(b2bConfigUser);
		when(persistenceService.getByConfigId(CONFIG_ID)).thenReturn(productConfiguration);
		when(userService.getCurrentUser()).thenReturn(currentUser);
		when(b2bUnitService.getRootUnit(currentUserB2bUnit)).thenReturn(currentUserB2bUnit);
		assertFalse(classUnderTest.isConfigInB2bUnitForCurrentUser(CONFIG_ID));
	}

	@Test
	public void testIsConfigInB2bUnitForCurrentUserConfigUserNotB2BUser()
	{
		productConfiguration.setUser(configUser);
		when(persistenceService.getByConfigId(CONFIG_ID)).thenReturn(productConfiguration);
		when(userService.getCurrentUser()).thenReturn(b2bCurrentUser);
		when(b2bUnitService.getRootUnit(currentUserB2bUnit)).thenReturn(currentUserB2bUnit);
		assertFalse(classUnderTest.isConfigInB2bUnitForCurrentUser(CONFIG_ID));
	}

	@Test
	public void testIsConfigKnown()
	{
		when(configurationLifecycleStrategy.isConfigKnown(CONFIG_ID)).thenReturn(true);
		assertTrue(classUnderTest.isConfigKnown(CONFIG_ID));
		verify(configurationLifecycleStrategy).isConfigKnown(CONFIG_ID);
	}
}
