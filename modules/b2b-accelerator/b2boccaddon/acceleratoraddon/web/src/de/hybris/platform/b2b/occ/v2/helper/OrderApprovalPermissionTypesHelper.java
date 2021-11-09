/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.helper;

import de.hybris.platform.b2bapprovalprocessfacades.company.B2BPermissionFacade;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionTypeData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionTypeListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionTypeWsDTO;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class OrderApprovalPermissionTypesHelper extends AbstractHelper
{
	@Resource(name = "b2bPermissionFacade")
	protected B2BPermissionFacade b2bPermissionFacade;

	public B2BPermissionTypeListWsDTO getPermissionTypes(final String fields)
	{
		Collection<B2BPermissionTypeData> b2BPermissionTypes = b2bPermissionFacade.getB2BPermissionTypes();

		B2BPermissionTypeListWsDTO b2BPermissionTypeListWsDTO = new B2BPermissionTypeListWsDTO();
		b2BPermissionTypeListWsDTO
				.setOrderApprovalPermissionTypes(getDataMapper().mapAsList(b2BPermissionTypes, B2BPermissionTypeWsDTO.class, fields));

		return b2BPermissionTypeListWsDTO;
	}
}
