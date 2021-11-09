/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicecustomerinterestsaddon.jalo;

import de.hybris.platform.assistedservicecustomerinterestsaddon.constants.AssistedservicecustomerinterestsaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class AssistedservicecustomerinterestsaddonManager extends GeneratedAssistedservicecustomerinterestsaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( AssistedservicecustomerinterestsaddonManager.class.getName() );
	
	public static final AssistedservicecustomerinterestsaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (AssistedservicecustomerinterestsaddonManager) em.getExtension(AssistedservicecustomerinterestsaddonConstants.EXTENSIONNAME);
	}
	
}
