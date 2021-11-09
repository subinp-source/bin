/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.stocknotificationaddon.constants.StocknotificationaddonConstants;
import org.apache.log4j.Logger;

public class StocknotificationaddonManager extends GeneratedStocknotificationaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( StocknotificationaddonManager.class.getName() );
	
	public static final StocknotificationaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (StocknotificationaddonManager) em.getExtension(StocknotificationaddonConstants.EXTENSIONNAME);
	}
	
}
