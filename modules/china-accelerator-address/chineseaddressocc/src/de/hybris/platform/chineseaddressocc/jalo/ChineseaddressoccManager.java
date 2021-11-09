/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseaddressocc.jalo;

import de.hybris.platform.chineseaddressocc.constants.ChineseaddressoccConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseaddressoccManager extends GeneratedChineseaddressoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseaddressoccManager.class.getName() );
	
	public static final ChineseaddressoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseaddressoccManager) em.getExtension(ChineseaddressoccConstants.EXTENSIONNAME);
	}
	
}
