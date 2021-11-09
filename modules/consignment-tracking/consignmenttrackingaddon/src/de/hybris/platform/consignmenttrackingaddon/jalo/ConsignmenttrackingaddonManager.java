/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingaddon.jalo;

import de.hybris.platform.consignmenttrackingaddon.constants.ConsignmenttrackingaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ConsignmenttrackingaddonManager extends GeneratedConsignmenttrackingaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ConsignmenttrackingaddonManager.class.getName() );
	
	public static final ConsignmenttrackingaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ConsignmenttrackingaddonManager) em.getExtension(ConsignmenttrackingaddonConstants.EXTENSIONNAME);
	}
	
}
