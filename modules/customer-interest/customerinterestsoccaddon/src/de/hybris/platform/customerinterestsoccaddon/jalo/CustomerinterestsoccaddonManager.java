/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsoccaddon.jalo;

import de.hybris.platform.customerinterestsoccaddon.constants.CustomerinterestsoccaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomerinterestsoccaddonManager extends GeneratedCustomerinterestsoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomerinterestsoccaddonManager.class.getName() );
	
	public static final CustomerinterestsoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomerinterestsoccaddonManager) em.getExtension(CustomerinterestsoccaddonConstants.EXTENSIONNAME);
	}
	
}
