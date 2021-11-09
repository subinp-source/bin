/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.order.populators;

import de.hybris.platform.b2b.enums.B2BPermissionTypeEnum;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPermissionTypeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.type.TypeService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link B2BPermissionTypeEnum} to {@link B2BPermissionTypeData}.
 *
 * @deprecated Since 6.0. Use
 *             {@link de.hybris.platform.b2bapprovalprocessfacades.company.converters.populators.B2BPermissionTypePopulator}
 *             instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public class B2BPermissionTypePopulator implements Populator<B2BPermissionTypeEnum, B2BPermissionTypeData>
{
	private TypeService typeService;

	@Override
	public void populate(final B2BPermissionTypeEnum source, final B2BPermissionTypeData target)
	{
		target.setCode(source.getCode());
		target.setName(getTypeService().getEnumerationValue(source).getName());
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}
}
