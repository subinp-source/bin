/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticservices.jalo;

import de.hybris.platform.chineselogisticservices.constants.ChineselogisticservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineselogisticservicesManager extends GeneratedChineselogisticservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineselogisticservicesManager.class.getName() );
	
	public static final ChineselogisticservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineselogisticservicesManager) em.getExtension(ChineselogisticservicesConstants.EXTENSIONNAME);
	}
	
}
