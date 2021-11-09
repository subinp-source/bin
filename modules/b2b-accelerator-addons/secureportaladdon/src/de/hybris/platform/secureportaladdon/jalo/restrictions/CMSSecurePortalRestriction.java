/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.jalo.restrictions;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.localization.Localization;

import org.apache.log4j.Logger;


public class CMSSecurePortalRestriction extends GeneratedCMSSecurePortalRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CMSSecurePortalRestriction.class.getName());

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		return super.createItem(ctx, type, allAttributes);
	}

	/**
	 * @deprecated Since 5.4. use
	 *             {@link de.hybris.platform.secureportaladdon.model.restrictions.CMSSecurePortalRestrictionModel#getDescription()}
	 *             instead.
	 */
	@Deprecated(since = "5.4")
	@Override
	public String getDescription(SessionContext sessionContext)
	{
		return Localization.getLocalizedString("type.CMSSecurePortalRestriction.description.text");
	}
}
