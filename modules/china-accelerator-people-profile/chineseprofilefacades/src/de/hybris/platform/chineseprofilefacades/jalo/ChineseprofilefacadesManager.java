/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofilefacades.jalo;

import de.hybris.platform.chineseprofilefacades.constants.ChineseprofilefacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseprofilefacadesManager extends GeneratedChineseprofilefacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseprofilefacadesManager.class.getName() );
	
	public static final ChineseprofilefacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseprofilefacadesManager) em.getExtension(ChineseprofilefacadesConstants.EXTENSIONNAME);
	}
	
}
