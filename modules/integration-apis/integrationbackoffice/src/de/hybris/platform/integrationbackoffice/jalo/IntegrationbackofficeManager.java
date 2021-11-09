/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.jalo;

import de.hybris.platform.integrationbackoffice.constants.IntegrationbackofficeConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class IntegrationbackofficeManager extends GeneratedIntegrationbackofficeManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( IntegrationbackofficeManager.class.getName() );
	
	public static final IntegrationbackofficeManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (IntegrationbackofficeManager) em.getExtension(IntegrationbackofficeConstants.EXTENSIONNAME);
	}
	
}
