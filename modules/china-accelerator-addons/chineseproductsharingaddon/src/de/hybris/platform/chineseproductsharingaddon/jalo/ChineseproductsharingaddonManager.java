/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseproductsharingaddon.jalo;

import de.hybris.platform.chineseproductsharingaddon.constants.ChineseproductsharingaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseproductsharingaddonManager extends GeneratedChineseproductsharingaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseproductsharingaddonManager.class.getName() );
	
	public static final ChineseproductsharingaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseproductsharingaddonManager) em.getExtension(ChineseproductsharingaddonConstants.EXTENSIONNAME);
	}
	
}
