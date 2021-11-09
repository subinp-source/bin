/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesecommerceorgaddressfacades.jalo;

import de.hybris.platform.chinesecommerceorgaddressfacades.constants.ChinesecommerceorgaddressfacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesecommerceorgaddressfacadesManager extends GeneratedChinesecommerceorgaddressfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesecommerceorgaddressfacadesManager.class.getName() );
	
	public static final ChinesecommerceorgaddressfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesecommerceorgaddressfacadesManager) em.getExtension(ChinesecommerceorgaddressfacadesConstants.EXTENSIONNAME);
	}
	
}
