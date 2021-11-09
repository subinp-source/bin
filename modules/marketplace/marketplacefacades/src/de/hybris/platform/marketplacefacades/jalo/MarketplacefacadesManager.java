/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplacefacades.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.marketplacefacades.constants.MarketplacefacadesConstants;
import org.apache.log4j.Logger;

public class MarketplacefacadesManager extends GeneratedMarketplacefacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MarketplacefacadesManager.class.getName() );
	
	public static final MarketplacefacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplacefacadesManager) em.getExtension(MarketplacefacadesConstants.EXTENSIONNAME);
	}
	
}
