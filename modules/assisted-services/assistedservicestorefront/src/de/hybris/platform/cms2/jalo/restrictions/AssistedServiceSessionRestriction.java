/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.localization.Localization;

import org.apache.log4j.Logger;


public class AssistedServiceSessionRestriction extends GeneratedAssistedServiceSessionRestriction
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AssistedServiceSessionRestriction.class.getName());

	/**
	 * @deprecated since 5.4 use
	 *             {@link de.hybris.platform.cms2.model.restrictions.AssistedServiceSessionRestrictionModel#getDescription()}
	 *             instead.
	 */
	@Deprecated(since = "5.4", forRemoval = true)
	@Override
	public String getDescription(final SessionContext ctx)
	{
		final StringBuilder result = new StringBuilder();

		final String localizedString = Localization.getLocalizedString("type.AMSSessionRestriction.description.text");
		result.append(localizedString == null ? "ASM Session Restriction" : localizedString);

		return result.toString();
	}

	@Override
	public String getDescription()
	{
		final StringBuilder result = new StringBuilder();

		final String localizedString = Localization.getLocalizedString("type.AMSSessionRestriction.description.text");
		result.append(localizedString == null ? "ASM Session Restriction" : localizedString);

		return result.toString();
	}
}
