/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.yacceleratorcore.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.yacceleratorcore.constants.YAcceleratorCoreConstants;
import de.hybris.platform.yacceleratorcore.setup.CoreSystemSetup;


/**
 * Do not use, please use {@link CoreSystemSetup} instead.
 * 
 */
public class YAcceleratorCoreManager extends GeneratedYAcceleratorCoreManager
{
	public static final YAcceleratorCoreManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YAcceleratorCoreManager) em.getExtension(YAcceleratorCoreConstants.EXTENSIONNAME);
	}
}
