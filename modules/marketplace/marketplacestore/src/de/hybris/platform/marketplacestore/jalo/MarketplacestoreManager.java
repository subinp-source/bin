/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplacestore.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.marketplacestore.constants.MarketplacestoreConstants;
import org.apache.log4j.Logger;

public class MarketplacestoreManager extends GeneratedMarketplacestoreManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MarketplacestoreManager.class.getName() );
	
	public static final MarketplacestoreManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplacestoreManager) em.getExtension(MarketplacestoreConstants.EXTENSIONNAME);
	}
	
}
