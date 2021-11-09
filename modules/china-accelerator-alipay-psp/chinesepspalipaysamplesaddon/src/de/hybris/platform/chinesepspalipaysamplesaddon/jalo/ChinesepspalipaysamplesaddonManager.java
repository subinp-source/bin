/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipaysamplesaddon.jalo;

import de.hybris.platform.chinesepspalipaysamplesaddon.constants.ChinesepspalipaysamplesaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesepspalipaysamplesaddonManager extends GeneratedChinesepspalipaysamplesaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesepspalipaysamplesaddonManager.class.getName() );
	
	public static final ChinesepspalipaysamplesaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesepspalipaysamplesaddonManager) em.getExtension(ChinesepspalipaysamplesaddonConstants.EXTENSIONNAME);
	}
	
}
