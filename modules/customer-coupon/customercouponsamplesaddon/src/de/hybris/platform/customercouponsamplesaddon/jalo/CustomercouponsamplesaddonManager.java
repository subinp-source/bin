/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponsamplesaddon.jalo;

import de.hybris.platform.customercouponsamplesaddon.constants.CustomercouponsamplesaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomercouponsamplesaddonManager extends GeneratedCustomercouponsamplesaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomercouponsamplesaddonManager.class.getName() );
	
	public static final CustomercouponsamplesaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomercouponsamplesaddonManager) em.getExtension(CustomercouponsamplesaddonConstants.EXTENSIONNAME);
	}
	
}
