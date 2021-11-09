/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingoccaddon.jalo;

import de.hybris.platform.consignmenttrackingoccaddon.constants.ConsignmenttrackingoccaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ConsignmenttrackingoccaddonManager extends GeneratedConsignmenttrackingoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ConsignmenttrackingoccaddonManager.class.getName() );
	
	public static final ConsignmenttrackingoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ConsignmenttrackingoccaddonManager) em.getExtension(ConsignmenttrackingoccaddonConstants.EXTENSIONNAME);
	}
	
}
