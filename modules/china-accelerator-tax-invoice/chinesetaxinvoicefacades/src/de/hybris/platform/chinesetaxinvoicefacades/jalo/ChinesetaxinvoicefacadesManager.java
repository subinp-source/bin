/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoicefacades.jalo;

import de.hybris.platform.chinesetaxinvoicefacades.constants.ChinesetaxinvoicefacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesetaxinvoicefacadesManager extends GeneratedChinesetaxinvoicefacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesetaxinvoicefacadesManager.class.getName() );
	
	public static final ChinesetaxinvoicefacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesetaxinvoicefacadesManager) em.getExtension(ChinesetaxinvoicefacadesConstants.EXTENSIONNAME);
	}
	
}
