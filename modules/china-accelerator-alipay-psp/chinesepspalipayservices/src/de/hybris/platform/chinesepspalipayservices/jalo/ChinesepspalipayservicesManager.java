/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.jalo;

import de.hybris.platform.chinesepspalipayservices.constants.ChinesepspalipayservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesepspalipayservicesManager extends GeneratedChinesepspalipayservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesepspalipayservicesManager.class.getName() );
	
	public static final ChinesepspalipayservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesepspalipayservicesManager) em.getExtension(ChinesepspalipayservicesConstants.EXTENSIONNAME);
	}
	
}
