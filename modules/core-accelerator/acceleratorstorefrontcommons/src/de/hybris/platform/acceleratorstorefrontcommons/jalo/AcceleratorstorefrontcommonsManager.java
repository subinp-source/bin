/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.jalo;

import de.hybris.platform.acceleratorstorefrontcommons.constants.AcceleratorstorefrontcommonsConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class AcceleratorstorefrontcommonsManager extends GeneratedAcceleratorstorefrontcommonsManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( AcceleratorstorefrontcommonsManager.class.getName() );
	
	public static final AcceleratorstorefrontcommonsManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (AcceleratorstorefrontcommonsManager) em.getExtension(AcceleratorstorefrontcommonsConstants.EXTENSIONNAME);
	}
	
}
