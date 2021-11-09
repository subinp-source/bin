/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.jalo.restrictions;

import de.hybris.platform.jalo.SessionContext;


public class CMSActionRestriction extends GeneratedCMSActionRestriction
{
	/**
	 * @deprecated Since 5.2. use
	 *             {@link de.hybris.platform.acceleratorcms.model.restrictions.CMSUiExperienceRestrictionModel#getDescription()}
	 *             instead.
	 */
	@Override
	@Deprecated(since = "5.2")
	public String getDescription(final SessionContext sessionContext)
	{
		return "CMSCatalogRestriction";
	}
}
