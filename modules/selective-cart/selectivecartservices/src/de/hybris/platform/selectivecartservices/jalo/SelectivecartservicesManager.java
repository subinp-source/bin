/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.selectivecartservices.constants.SelectivecartservicesConstants;
import org.apache.log4j.Logger;

public class SelectivecartservicesManager extends GeneratedSelectivecartservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( SelectivecartservicesManager.class.getName() );
	
	public static final SelectivecartservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SelectivecartservicesManager) em.getExtension(SelectivecartservicesConstants.EXTENSIONNAME);
	}
	
}
