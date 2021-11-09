/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.selectivecartaddon.constants.SelectivecartaddonConstants;
import org.apache.log4j.Logger;

public class SelectivecartaddonManager extends GeneratedSelectivecartaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( SelectivecartaddonManager.class.getName() );
	
	public static final SelectivecartaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SelectivecartaddonManager) em.getExtension(SelectivecartaddonConstants.EXTENSIONNAME);
	}
	
}
