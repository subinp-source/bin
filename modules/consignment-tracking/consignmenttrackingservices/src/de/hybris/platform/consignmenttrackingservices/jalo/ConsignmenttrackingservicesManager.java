/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingservices.jalo;

import de.hybris.platform.consignmenttrackingservices.constants.ConsignmenttrackingservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ConsignmenttrackingservicesManager extends GeneratedConsignmenttrackingservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ConsignmenttrackingservicesManager.class.getName() );
	
	public static final ConsignmenttrackingservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ConsignmenttrackingservicesManager) em.getExtension(ConsignmenttrackingservicesConstants.EXTENSIONNAME);
	}
	
}
