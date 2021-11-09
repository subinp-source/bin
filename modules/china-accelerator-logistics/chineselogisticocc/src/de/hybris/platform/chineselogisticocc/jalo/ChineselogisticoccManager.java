/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticocc.jalo;

import de.hybris.platform.chineselogisticocc.constants.ChineselogisticoccConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineselogisticoccManager extends GeneratedChineselogisticoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineselogisticoccManager.class.getName() );
	
	public static final ChineselogisticoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineselogisticoccManager) em.getExtension(ChineselogisticoccConstants.EXTENSIONNAME);
	}
	
}
