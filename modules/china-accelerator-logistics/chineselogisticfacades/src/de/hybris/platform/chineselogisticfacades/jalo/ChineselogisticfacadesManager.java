/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticfacades.jalo;

import de.hybris.platform.chineselogisticfacades.constants.ChineselogisticfacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineselogisticfacadesManager extends GeneratedChineselogisticfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineselogisticfacadesManager.class.getName() );
	
	public static final ChineselogisticfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineselogisticfacadesManager) em.getExtension(ChineselogisticfacadesConstants.EXTENSIONNAME);
	}
	
}
