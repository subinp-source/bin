/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationfacades.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.stocknotificationfacades.constants.StocknotificationfacadesConstants;
import org.apache.log4j.Logger;

public class StocknotificationfacadesManager extends GeneratedStocknotificationfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( StocknotificationfacadesManager.class.getName() );
	
	public static final StocknotificationfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (StocknotificationfacadesManager) em.getExtension(StocknotificationfacadesConstants.EXTENSIONNAME);
	}
	
}
