/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.jalo;

import de.hybris.platform.acceleratorcms.constants.AcceleratorCmsConstants;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;


/**
 * Do not use. Please use {@link SystemSetup}.
 */
public class AcceleratorCmsManager extends GeneratedAcceleratorCmsManager
{
	public static final AcceleratorCmsManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (AcceleratorCmsManager) em.getExtension(AcceleratorCmsConstants.EXTENSIONNAME);
	}

}
