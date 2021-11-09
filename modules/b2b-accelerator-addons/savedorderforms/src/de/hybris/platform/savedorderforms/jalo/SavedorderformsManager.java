/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.savedorderforms.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.savedorderforms.constants.SavedorderformsConstants;
import org.apache.log4j.Logger;

public class SavedorderformsManager extends GeneratedSavedorderformsManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( SavedorderformsManager.class.getName() );
	
	public static final SavedorderformsManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SavedorderformsManager) em.getExtension(SavedorderformsConstants.EXTENSIONNAME);
	}
	
}
