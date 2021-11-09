/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponocc.jalo;

import de.hybris.platform.customercouponocc.constants.CustomercouponoccConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomercouponoccManager extends GeneratedCustomercouponoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomercouponoccManager.class.getName() );
	
	public static final CustomercouponoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomercouponoccManager) em.getExtension(CustomercouponoccConstants.EXTENSIONNAME);
	}
	
}
