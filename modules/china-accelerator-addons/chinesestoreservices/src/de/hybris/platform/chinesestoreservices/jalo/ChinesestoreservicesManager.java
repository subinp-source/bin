/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesestoreservices.jalo;

import de.hybris.platform.chinesestoreservices.constants.ChinesestoreservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesestoreservicesManager extends GeneratedChinesestoreservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesestoreservicesManager.class.getName() );
	
	public static final ChinesestoreservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesestoreservicesManager) em.getExtension(ChinesestoreservicesConstants.EXTENSIONNAME);
	}
	
}
