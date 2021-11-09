/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplacepromotionenginesamplesaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.marketplacepromotionenginesamplesaddon.constants.MarketplacepromotionenginesamplesaddonConstants;
import org.apache.log4j.Logger;

public class MarketplacepromotionenginesamplesaddonManager extends GeneratedMarketplacepromotionenginesamplesaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MarketplacepromotionenginesamplesaddonManager.class.getName() );
	
	public static final MarketplacepromotionenginesamplesaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplacepromotionenginesamplesaddonManager) em.getExtension(MarketplacepromotionenginesamplesaddonConstants.EXTENSIONNAME);
	}
	
}
