/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsservices.jalo;

import de.hybris.platform.customerinterestsservices.constants.CustomerinterestsservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomerinterestsservicesManager extends GeneratedCustomerinterestsservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomerinterestsservicesManager.class.getName() );
	
	public static final CustomerinterestsservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomerinterestsservicesManager) em.getExtension(CustomerinterestsservicesConstants.EXTENSIONNAME);
	}
	
}
