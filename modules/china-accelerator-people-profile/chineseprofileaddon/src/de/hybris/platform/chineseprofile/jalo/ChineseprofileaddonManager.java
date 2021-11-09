/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofile.jalo;

import de.hybris.platform.chineseprofile.constants.ChineseprofileaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseprofileaddonManager extends GeneratedChineseprofileaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseprofileaddonManager.class.getName() );
	
	public static final ChineseprofileaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseprofileaddonManager) em.getExtension(ChineseprofileaddonConstants.EXTENSIONNAME);
	}
	
}
