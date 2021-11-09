/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticaddon.jalo;

import de.hybris.platform.chineselogisticaddon.constants.ChineselogisticaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineselogisticaddonManager extends GeneratedChineselogisticaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineselogisticaddonManager.class.getName() );
	
	public static final ChineselogisticaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineselogisticaddonManager) em.getExtension(ChineselogisticaddonConstants.EXTENSIONNAME);
	}
	
}
