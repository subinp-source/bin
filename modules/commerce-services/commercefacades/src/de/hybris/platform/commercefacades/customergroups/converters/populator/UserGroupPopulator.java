/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.customergroups.converters.populator;

import de.hybris.platform.commercefacades.user.data.UserGroupData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Default populator, convert UserGroupModel to UserGroupData
 */
public class UserGroupPopulator implements Populator<UserGroupModel, UserGroupData>
{

	@Override
	public void populate(final UserGroupModel source, final UserGroupData target) throws ConversionException
	{
		target.setName(source.getLocName());
		target.setUid(source.getUid());
	}

}
