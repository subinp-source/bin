/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressfacades.jalo;

import de.hybris.platform.addressfacades.constants.ChineseaddressfacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseaddressfacadesManager extends GeneratedChineseaddressfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseaddressfacadesManager.class.getName() );
	
	public static final ChineseaddressfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseaddressfacadesManager) em.getExtension(ChineseaddressfacadesConstants.EXTENSIONNAME);
	}
	
}
