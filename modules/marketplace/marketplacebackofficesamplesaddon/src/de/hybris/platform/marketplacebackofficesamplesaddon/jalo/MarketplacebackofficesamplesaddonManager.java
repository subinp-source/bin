/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplacebackofficesamplesaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.marketplacebackofficesamplesaddon.constants.MarketplacebackofficesamplesaddonConstants;
import org.apache.log4j.Logger;

public class MarketplacebackofficesamplesaddonManager extends GeneratedMarketplacebackofficesamplesaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MarketplacebackofficesamplesaddonManager.class.getName() );
	
	public static final MarketplacebackofficesamplesaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplacebackofficesamplesaddonManager) em.getExtension(MarketplacebackofficesamplesaddonConstants.EXTENSIONNAME);
	}
	
}
