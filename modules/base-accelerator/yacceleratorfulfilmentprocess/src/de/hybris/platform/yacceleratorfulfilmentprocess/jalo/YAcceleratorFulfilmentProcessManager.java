/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.yacceleratorfulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.yacceleratorfulfilmentprocess.constants.YAcceleratorFulfilmentProcessConstants;

public class YAcceleratorFulfilmentProcessManager extends GeneratedYAcceleratorFulfilmentProcessManager
{
	public static final YAcceleratorFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YAcceleratorFulfilmentProcessManager) em.getExtension(YAcceleratorFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
