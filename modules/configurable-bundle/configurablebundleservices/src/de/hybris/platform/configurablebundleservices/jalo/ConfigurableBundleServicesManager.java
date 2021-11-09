/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.configurablebundleservices.constants.ConfigurableBundleServicesConstants;

import org.apache.log4j.Logger;


@SuppressWarnings("PMD")
public class ConfigurableBundleServicesManager extends GeneratedConfigurableBundleServicesManager
{
	@SuppressWarnings("unused")
	private static Logger LOG = Logger.getLogger(ConfigurableBundleServicesManager.class.getName());

	public static ConfigurableBundleServicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ConfigurableBundleServicesManager) em.getExtension(ConfigurableBundleServicesConstants.EXTENSIONNAME);
	}

}
