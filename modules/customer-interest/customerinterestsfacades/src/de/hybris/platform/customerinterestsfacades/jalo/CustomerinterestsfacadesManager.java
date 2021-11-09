/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.jalo;

import de.hybris.platform.customerinterestsfacades.constants.CustomerinterestsfacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomerinterestsfacadesManager extends GeneratedCustomerinterestsfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomerinterestsfacadesManager.class.getName() );
	
	public static final CustomerinterestsfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomerinterestsfacadesManager) em.getExtension(CustomerinterestsfacadesConstants.EXTENSIONNAME);
	}
	
}
