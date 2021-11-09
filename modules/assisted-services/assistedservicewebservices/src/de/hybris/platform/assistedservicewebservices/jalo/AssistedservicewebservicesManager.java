/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicewebservices.jalo;

import de.hybris.platform.assistedservicewebservices.constants.AssistedservicewebservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class AssistedservicewebservicesManager extends GeneratedAssistedservicewebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( AssistedservicewebservicesManager.class.getName() );
	
	public static final AssistedservicewebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (AssistedservicewebservicesManager) em.getExtension(AssistedservicewebservicesConstants.EXTENSIONNAME);
	}
	
}
