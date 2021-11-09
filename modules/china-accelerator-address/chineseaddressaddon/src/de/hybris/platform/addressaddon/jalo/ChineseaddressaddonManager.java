/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressaddon.jalo;

import de.hybris.platform.addressaddon.constants.ChineseaddressaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseaddressaddonManager extends GeneratedChineseaddressaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseaddressaddonManager.class.getName() );
	
	public static final ChineseaddressaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseaddressaddonManager) em.getExtension(ChineseaddressaddonConstants.EXTENSIONNAME);
	}
	
}
