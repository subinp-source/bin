/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoiceoccaddon.jalo;

import de.hybris.platform.chinesetaxinvoiceoccaddon.constants.ChinesetaxinvoiceoccaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesetaxinvoiceoccaddonManager extends GeneratedChinesetaxinvoiceoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesetaxinvoiceoccaddonManager.class.getName() );
	
	public static final ChinesetaxinvoiceoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesetaxinvoiceoccaddonManager) em.getExtension(ChinesetaxinvoiceoccaddonConstants.EXTENSIONNAME);
	}
	
}
