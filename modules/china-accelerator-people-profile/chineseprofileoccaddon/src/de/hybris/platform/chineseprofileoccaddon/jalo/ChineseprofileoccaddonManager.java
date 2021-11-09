/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileoccaddon.jalo;

import de.hybris.platform.chineseprofileoccaddon.constants.ChineseprofileoccaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseprofileoccaddonManager extends GeneratedChineseprofileoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseprofileoccaddonManager.class.getName() );
	
	public static final ChineseprofileoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseprofileoccaddonManager) em.getExtension(ChineseprofileoccaddonConstants.EXTENSIONNAME);
	}
	
}
