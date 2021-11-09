/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseaddressoccaddon.jalo;

import de.hybris.platform.chineseaddressoccaddon.constants.ChineseaddressoccaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseaddressoccaddonManager extends GeneratedChineseaddressoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseaddressoccaddonManager.class.getName() );
	
	public static final ChineseaddressoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseaddressoccaddonManager) em.getExtension(ChineseaddressoccaddonConstants.EXTENSIONNAME);
	}
	
}
