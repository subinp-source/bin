/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponoccaddon.jalo;

import de.hybris.platform.customercouponoccaddon.constants.CustomercouponoccaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomercouponoccaddonManager extends GeneratedCustomercouponoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomercouponoccaddonManager.class.getName() );
	
	public static final CustomercouponoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomercouponoccaddonManager) em.getExtension(CustomercouponoccaddonConstants.EXTENSIONNAME);
	}
	
}
