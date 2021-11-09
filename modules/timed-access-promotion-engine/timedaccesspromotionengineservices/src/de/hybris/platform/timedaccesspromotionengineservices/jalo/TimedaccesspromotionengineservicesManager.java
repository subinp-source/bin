/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.timedaccesspromotionengineservices.constants.TimedaccesspromotionengineservicesConstants;
import org.apache.log4j.Logger;

public class TimedaccesspromotionengineservicesManager extends GeneratedTimedaccesspromotionengineservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( TimedaccesspromotionengineservicesManager.class.getName() );
	
	public static final TimedaccesspromotionengineservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (TimedaccesspromotionengineservicesManager) em.getExtension(TimedaccesspromotionengineservicesConstants.EXTENSIONNAME);
	}
	
}
