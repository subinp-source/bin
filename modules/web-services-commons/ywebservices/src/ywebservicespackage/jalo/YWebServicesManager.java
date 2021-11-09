/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ywebservicespackage.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;
import ywebservicespackage.constants.YWebServicesConstants;

public class YWebServicesManager extends GeneratedYWebServicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger( YWebServicesManager.class.getName() );
	
	public static final YWebServicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YWebServicesManager) em.getExtension(YWebServicesConstants.EXTENSIONNAME);
	}
	
}
