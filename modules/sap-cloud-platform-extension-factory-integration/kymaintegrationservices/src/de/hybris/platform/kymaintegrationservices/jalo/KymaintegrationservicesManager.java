/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.kymaintegrationservices.constants.KymaintegrationservicesConstants;


public class KymaintegrationservicesManager extends GeneratedKymaintegrationservicesManager
{
	
	public static final KymaintegrationservicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (KymaintegrationservicesManager) em.getExtension(KymaintegrationservicesConstants.EXTENSIONNAME);
	}
	
}
