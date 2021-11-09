/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;

import de.hybris.platform.b2bocctests.constants.B2bocctestsConstants;


@SuppressWarnings("PMD")
public class B2bocctestsManager extends GeneratedB2bocctestsManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(B2bocctestsManager.class.getName());

	public static final B2bocctestsManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2bocctestsManager) em.getExtension(B2bocctestsConstants.EXTENSIONNAME);
	}

}
