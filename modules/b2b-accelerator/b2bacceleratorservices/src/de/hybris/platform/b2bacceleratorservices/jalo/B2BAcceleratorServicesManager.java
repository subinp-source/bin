/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.jalo;

import de.hybris.platform.b2bacceleratorservices.constants.B2BAcceleratorServicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;


/**
 * This is the extension manager of the B2BAcceleratorServices extension.
 */
public class B2BAcceleratorServicesManager extends GeneratedB2BAcceleratorServicesManager
{
	/**
	 * Get the valid instance of this manager.
	 * 
	 * @return the current instance of this manager
	 */
	public static B2BAcceleratorServicesManager getInstance()
	{
		final ExtensionManager extensionManager = JaloSession.getCurrentSession().getExtensionManager();
		return (B2BAcceleratorServicesManager) extensionManager.getExtension(B2BAcceleratorServicesConstants.EXTENSIONNAME);
	}
}
