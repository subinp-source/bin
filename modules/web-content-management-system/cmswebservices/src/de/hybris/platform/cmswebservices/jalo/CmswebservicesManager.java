/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.jalo;

import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class CmswebservicesManager extends GeneratedCmswebservicesManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( CmswebservicesManager.class.getName() );
	
	public static final CmswebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CmswebservicesManager) em.getExtension(CmswebservicesConstants.EXTENSIONNAME);
	}
	
}
