/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsaddon.jalo;

import de.hybris.platform.customerinterestsaddon.constants.CustomerinterestsaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomerinterestsaddonManager extends GeneratedCustomerinterestsaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomerinterestsaddonManager.class.getName() );
	
	public static final CustomerinterestsaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomerinterestsaddonManager) em.getExtension(CustomerinterestsaddonConstants.EXTENSIONNAME);
	}
	
}
