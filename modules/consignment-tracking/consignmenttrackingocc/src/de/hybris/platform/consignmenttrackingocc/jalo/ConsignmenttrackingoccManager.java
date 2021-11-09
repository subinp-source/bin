/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingocc.jalo;

import de.hybris.platform.consignmenttrackingocc.constants.ConsignmenttrackingoccConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ConsignmenttrackingoccManager extends GeneratedConsignmenttrackingoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ConsignmenttrackingoccManager.class.getName() );
	
	public static final ConsignmenttrackingoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ConsignmenttrackingoccManager) em.getExtension(ConsignmenttrackingoccConstants.EXTENSIONNAME);
	}
	
}
