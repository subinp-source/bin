/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.savedorderformsocc.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.savedorderformsocc.constants.SavedorderformsoccConstants;
import org.apache.log4j.Logger;

public class SavedorderformsoccManager extends GeneratedSavedorderformsoccManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( SavedorderformsoccManager.class.getName() );
	
	public static final SavedorderformsoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SavedorderformsoccManager) em.getExtension(SavedorderformsoccConstants.EXTENSIONNAME);
	}
	
}
