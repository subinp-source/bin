/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsocc.jalo;

import de.hybris.platform.customerinterestsocc.constants.CustomerinterestsoccConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomerinterestsoccManager extends GeneratedCustomerinterestsoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomerinterestsoccManager.class.getName() );
	
	public static final CustomerinterestsoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomerinterestsoccManager) em.getExtension(CustomerinterestsoccConstants.EXTENSIONNAME);
	}
	
}
