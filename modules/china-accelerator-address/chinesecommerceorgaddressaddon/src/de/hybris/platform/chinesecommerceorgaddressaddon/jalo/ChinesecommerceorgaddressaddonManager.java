/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesecommerceorgaddressaddon.jalo;

import de.hybris.platform.chinesecommerceorgaddressaddon.constants.ChinesecommerceorgaddressaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesecommerceorgaddressaddonManager extends GeneratedChinesecommerceorgaddressaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesecommerceorgaddressaddonManager.class.getName() );
	
	public static final ChinesecommerceorgaddressaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesecommerceorgaddressaddonManager) em.getExtension(ChinesecommerceorgaddressaddonConstants.EXTENSIONNAME);
	}
	
}
