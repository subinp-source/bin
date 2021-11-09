/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpaysamplesaddon.jalo;

import de.hybris.platform.chinesepspwechatpaysamplesaddon.constants.ChinesepspwechatpaysamplesaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesepspwechatpaysamplesaddonManager extends GeneratedChinesepspwechatpaysamplesaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesepspwechatpaysamplesaddonManager.class.getName() );
	
	public static final ChinesepspwechatpaysamplesaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesepspwechatpaysamplesaddonManager) em.getExtension(ChinesepspwechatpaysamplesaddonConstants.EXTENSIONNAME);
	}
	
}
