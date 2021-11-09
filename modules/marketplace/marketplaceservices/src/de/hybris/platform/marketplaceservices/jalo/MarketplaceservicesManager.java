/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.marketplaceservices.constants.MarketplaceservicesConstants;
import org.apache.log4j.Logger;

public class MarketplaceservicesManager extends GeneratedMarketplaceservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MarketplaceservicesManager.class.getName() );
	
	public static final MarketplaceservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplaceservicesManager) em.getExtension(MarketplaceservicesConstants.EXTENSIONNAME);
	}
	
}
