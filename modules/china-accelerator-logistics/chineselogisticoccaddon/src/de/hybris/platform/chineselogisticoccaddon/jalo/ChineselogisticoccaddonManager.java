/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticoccaddon.jalo;

import de.hybris.platform.chineselogisticoccaddon.constants.ChineselogisticoccaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineselogisticoccaddonManager extends GeneratedChineselogisticoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineselogisticoccaddonManager.class.getName() );
	
	public static final ChineselogisticoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineselogisticoccaddonManager) em.getExtension(ChineselogisticoccaddonConstants.EXTENSIONNAME);
	}
	
}
