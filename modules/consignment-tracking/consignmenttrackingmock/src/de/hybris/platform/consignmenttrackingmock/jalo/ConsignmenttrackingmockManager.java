/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingmock.jalo;

import de.hybris.platform.consignmenttrackingmock.constants.ConsignmenttrackingmockConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ConsignmenttrackingmockManager extends GeneratedConsignmenttrackingmockManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ConsignmenttrackingmockManager.class.getName() );
	
	public static final ConsignmenttrackingmockManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ConsignmenttrackingmockManager) em.getExtension(ConsignmenttrackingmockConstants.EXTENSIONNAME);
	}
	
}
