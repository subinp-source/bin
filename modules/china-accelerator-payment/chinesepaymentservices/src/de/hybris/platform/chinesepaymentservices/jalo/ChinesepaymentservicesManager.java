/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentservices.jalo;

import de.hybris.platform.chinesepaymentservices.constants.ChinesepaymentservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesepaymentservicesManager extends GeneratedChinesepaymentservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesepaymentservicesManager.class.getName() );
	
	public static final ChinesepaymentservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesepaymentservicesManager) em.getExtension(ChinesepaymentservicesConstants.EXTENSIONNAME);
	}
	
}
