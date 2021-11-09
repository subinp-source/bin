/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingfacades.jalo;

import de.hybris.platform.consignmenttrackingfacades.constants.ConsignmenttrackingfacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ConsignmenttrackingfacadesManager extends GeneratedConsignmenttrackingfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ConsignmenttrackingfacadesManager.class.getName() );
	
	public static final ConsignmenttrackingfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ConsignmenttrackingfacadesManager) em.getExtension(ConsignmenttrackingfacadesConstants.EXTENSIONNAME);
	}
	
}
