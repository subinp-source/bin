/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineocc.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.timedaccesspromotionengineocc.constants.TimedaccesspromotionengineoccConstants;
import org.apache.log4j.Logger;

public class TimedaccesspromotionengineoccManager extends GeneratedTimedaccesspromotionengineoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( TimedaccesspromotionengineoccManager.class.getName() );
	
	public static final TimedaccesspromotionengineoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (TimedaccesspromotionengineoccManager) em.getExtension(TimedaccesspromotionengineoccConstants.EXTENSIONNAME);
	}
	
}
