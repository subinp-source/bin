/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoiceaddon.jalo;

import de.hybris.platform.chinesetaxinvoiceaddon.constants.ChinesetaxinvoiceaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesetaxinvoiceaddonManager extends GeneratedChinesetaxinvoiceaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesetaxinvoiceaddonManager.class.getName() );
	
	public static final ChinesetaxinvoiceaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesetaxinvoiceaddonManager) em.getExtension(ChinesetaxinvoiceaddonConstants.EXTENSIONNAME);
	}
	
}
