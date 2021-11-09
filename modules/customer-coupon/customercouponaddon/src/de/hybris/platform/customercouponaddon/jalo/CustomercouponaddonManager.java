/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponaddon.jalo;

import de.hybris.platform.customercouponaddon.constants.CustomercouponaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomercouponaddonManager extends GeneratedCustomercouponaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomercouponaddonManager.class.getName() );
	
	public static final CustomercouponaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomercouponaddonManager) em.getExtension(CustomercouponaddonConstants.EXTENSIONNAME);
	}
	
}
