/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.jalo;

import de.hybris.platform.chineseprofileservices.constants.ChineseprofileservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseprofileservicesManager extends GeneratedChineseprofileservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseprofileservicesManager.class.getName() );
	
	public static final ChineseprofileservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseprofileservicesManager) em.getExtension(ChineseprofileservicesConstants.EXTENSIONNAME);
	}
	
}
