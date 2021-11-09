/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.stocknotificationservices.constants.StocknotificationservicesConstants;
import org.apache.log4j.Logger;

public class StocknotificationservicesManager extends GeneratedStocknotificationservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( StocknotificationservicesManager.class.getName() );
	
	public static final StocknotificationservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (StocknotificationservicesManager) em.getExtension(StocknotificationservicesConstants.EXTENSIONNAME);
	}
	
}
