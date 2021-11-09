/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoiceocc.jalo;

import de.hybris.platform.chinesetaxinvoiceocc.constants.ChinesetaxinvoiceoccConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesetaxinvoiceoccManager extends GeneratedChinesetaxinvoiceoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesetaxinvoiceoccManager.class.getName() );
	
	public static final ChinesetaxinvoiceoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesetaxinvoiceoccManager) em.getExtension(ChinesetaxinvoiceoccConstants.EXTENSIONNAME);
	}
	
}
