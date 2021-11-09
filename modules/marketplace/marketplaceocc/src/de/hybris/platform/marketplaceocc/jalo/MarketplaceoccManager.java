/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceocc.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.marketplaceocc.constants.MarketplaceoccConstants;
import org.apache.log4j.Logger;

public class MarketplaceoccManager extends GeneratedMarketplaceoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MarketplaceoccManager.class.getName() );
	
	public static final MarketplaceoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplaceoccManager) em.getExtension(MarketplaceoccConstants.EXTENSIONNAME);
	}
	
}
