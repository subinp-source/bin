/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoiceservices.jalo;

import de.hybris.platform.chinesetaxinvoiceservices.constants.ChinesetaxinvoiceservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesetaxinvoiceservicesManager extends GeneratedChinesetaxinvoiceservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesetaxinvoiceservicesManager.class.getName() );
	
	public static final ChinesetaxinvoiceservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesetaxinvoiceservicesManager) em.getExtension(ChinesetaxinvoiceservicesConstants.EXTENSIONNAME);
	}
	
}
