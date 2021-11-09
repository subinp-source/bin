/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceoccaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.marketplaceoccaddon.constants.MarketplaceoccaddonConstants;
import org.apache.log4j.Logger;

public class MarketplaceoccaddonManager extends GeneratedMarketplaceoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MarketplaceoccaddonManager.class.getName() );
	
	public static final MarketplaceoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplaceoccaddonManager) em.getExtension(MarketplaceoccaddonConstants.EXTENSIONNAME);
	}
	
}
