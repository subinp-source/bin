/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.jalo;

import de.hybris.platform.addressservices.constants.ChineseaddressservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseaddressservicesManager extends GeneratedChineseaddressservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseaddressservicesManager.class.getName() );
	
	public static final ChineseaddressservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseaddressservicesManager) em.getExtension(ChineseaddressservicesConstants.EXTENSIONNAME);
	}
	
}
