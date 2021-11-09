/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.hooks.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponfacades.hooks.NotificationChannelHook;

import org.apache.commons.lang3.BooleanUtils;


/**
 * Email channel implementation of {@link NotificationChannelHook}.
 *
 * @deprecated since 6.7.
 */
@Deprecated(since = "6.7", forRemoval= true )
public class EmailChannelHook implements NotificationChannelHook
{

	@Override
	public Boolean isChannelOn(final CustomerModel customer)
	{
		return BooleanUtils.isTrue(customer.getEmailPreference());
	}

}