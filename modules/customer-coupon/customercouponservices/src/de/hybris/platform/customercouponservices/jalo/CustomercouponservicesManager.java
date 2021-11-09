/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.jalo;

import de.hybris.platform.customercouponservices.constants.CustomercouponservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomercouponservicesManager extends GeneratedCustomercouponservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomercouponservicesManager.class.getName() );
	
	public static final CustomercouponservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomercouponservicesManager) em.getExtension(CustomercouponservicesConstants.EXTENSIONNAME);
	}
	
}
