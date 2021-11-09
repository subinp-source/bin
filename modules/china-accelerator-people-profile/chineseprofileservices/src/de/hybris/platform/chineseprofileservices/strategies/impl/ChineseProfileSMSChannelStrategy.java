/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofileservices.strategies.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.service.strategies.impl.DefaultSmsChannelStrategy;


/**
 * 
 * @deprecated since 19.05 . use {@link DefaultChineseProfileSMSChannelStrategy}
 */
@Deprecated(since = "1905", forRemoval= true )
public class ChineseProfileSMSChannelStrategy extends DefaultSmsChannelStrategy
{
	@Override
	public String getChannelValue(final CustomerModel customer)
	{
		return customer.getMobileNumber();
	}

}
