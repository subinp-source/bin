/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.b2bservices.strategies.lifecycle.impl;

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


/**
 * B2B specific implementation of lifecycle handling. Difference to the standard one is that is takes the b2b unit into
 * account for access checks (users from the same b2b unit are allowed to access their documents)
 */
public class PersistenceConfigurationLifecycleB2BStrategyImpl implements ConfigurationLifecycleStrategy
{
	private final ConfigurationLifecycleStrategy configurationLifecycleStrategy;
	private final UserService userService;
	private final ProductConfigurationPersistenceService persistenceService;
	private final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

	/**
	 * Constructor
	 * 
	 * @param configurationLifecycleStrategy
	 *           Default implementation that we delegate most calls to
	 * @param userService
	 * @param persistenceService
	 * @param b2bUnitService
	 *           Accessing b2b units
	 */
	public PersistenceConfigurationLifecycleB2BStrategyImpl(final ConfigurationLifecycleStrategy configurationLifecycleStrategy,
			final UserService userService, final ProductConfigurationPersistenceService persistenceService,
			final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.configurationLifecycleStrategy = configurationLifecycleStrategy;
		this.userService = userService;
		this.persistenceService = persistenceService;
		this.b2bUnitService = b2bUnitService;
	}

	@Override
	public ConfigModel createDefaultConfiguration(final KBKey kbKey)
	{
		return getConfigurationLifecycleStrategy().createDefaultConfiguration(kbKey);
	}

	@Override
	public boolean updateConfiguration(final ConfigModel model) throws ConfigurationEngineException
	{
		return getConfigurationLifecycleStrategy().updateConfiguration(model);
	}

	@Override
	public void updateUserLinkToConfiguration(final String userSessionId)
	{
		getConfigurationLifecycleStrategy().updateUserLinkToConfiguration(userSessionId);

	}

	@Override
	public ConfigModel retrieveConfigurationModel(final String configId) throws ConfigurationEngineException
	{
		return getConfigurationLifecycleStrategy().retrieveConfigurationModel(configId);
	}

	@Override
	public ConfigModel retrieveConfigurationModel(final String configId, final ConfigurationRetrievalOptions options)
			throws ConfigurationEngineException
	{
		return getConfigurationLifecycleStrategy().retrieveConfigurationModel(configId, options);
	}

	@Override
	public String retrieveExternalConfiguration(final String configId) throws ConfigurationEngineException
	{
		return getConfigurationLifecycleStrategy().retrieveExternalConfiguration(configId);
	}

	@Override
	public ConfigModel createConfigurationFromExternalSource(final Configuration extConfig)
	{
		return getConfigurationLifecycleStrategy().createConfigurationFromExternalSource(extConfig);
	}

	@Override
	public ConfigModel createConfigurationFromExternalSource(final KBKey kbKey, final String extConfig)
	{
		return getConfigurationLifecycleStrategy().createConfigurationFromExternalSource(kbKey, extConfig);
	}

	@Override
	public void releaseSession(final String configId)
	{
		getConfigurationLifecycleStrategy().releaseSession(configId);
	}

	@Override
	public void releaseExpiredSessions(final String userSessionId)
	{
		getConfigurationLifecycleStrategy().releaseExpiredSessions(userSessionId);
	}

	@Override
	public ConfigModel retrieveConfigurationFromVariant(final String baseProductCode, final String variantProductCode)
	{
		return getConfigurationLifecycleStrategy().retrieveConfigurationFromVariant(baseProductCode, variantProductCode);
	}

	@Override
	public boolean isConfigForCurrentUser(final String configId)
	{
		return getConfigurationLifecycleStrategy().isConfigForCurrentUser(configId) || isConfigInB2bUnitForCurrentUser(configId);
	}

	protected boolean isConfigInB2bUnitForCurrentUser(final String configId)
	{
		final ProductConfigurationModel configPersistenceModel = getPersistenceService().getByConfigId(configId);
		final UserModel configUser = configPersistenceModel.getUser();
		final UserModel currentUser = getUserService().getCurrentUser();
		if (configUser instanceof B2BCustomerModel && currentUser instanceof B2BCustomerModel)
		{
			final B2BUnitModel configUserParentB2BUnit = getB2bUnitService().getParent((B2BCustomerModel) configUser);
			final B2BUnitModel currentUserParentB2BUnit = getB2bUnitService().getParent((B2BCustomerModel) currentUser);
			final B2BUnitModel rootUnitConfigUser = getB2bUnitService().getRootUnit(configUserParentB2BUnit);
			final B2BUnitModel rootUnitCurrentUser = getB2bUnitService().getRootUnit(currentUserParentB2BUnit);
			return rootUnitConfigUser.getUid().equals(rootUnitCurrentUser.getUid());
		}
		return false;
	}

	@Override
	public boolean isConfigKnown(final String configId)
	{
		return getConfigurationLifecycleStrategy().isConfigKnown(configId);
	}

	protected ConfigurationLifecycleStrategy getConfigurationLifecycleStrategy()
	{
		return configurationLifecycleStrategy;
	}


	protected UserService getUserService()
	{
		return userService;
	}


	protected ProductConfigurationPersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	protected B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

}
