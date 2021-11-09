/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.mapping.converter;


import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionResultData;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionTypeData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionTypeWsDTO;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;


/**
 * Bidirectional converter between {@link B2BPermissionResultConverter} and {@link B2BPermissionTypeWsDTO}
 */
@WsDTOMapping
public class B2BPermissionResultConverter extends BidirectionalConverter<B2BPermissionResultData, B2BPermissionTypeWsDTO>
{
	@Override
	public B2BPermissionResultData convertFrom(final B2BPermissionTypeWsDTO source,
			final Type<B2BPermissionResultData> destinationType,
			final MappingContext mappingContext)
	{
		B2BPermissionResultData permissionResult = new B2BPermissionResultData();
		permissionResult.setPermissionTypeData(mapperFacade.map(source, B2BPermissionTypeData.class, mappingContext));
		return permissionResult;
	}

	@Override
	public B2BPermissionTypeWsDTO convertTo(final B2BPermissionResultData source,
			final Type<B2BPermissionTypeWsDTO> destinationType,
			final MappingContext mappingContext)
	{
		return mapperFacade.map(source.getPermissionTypeData(), B2BPermissionTypeWsDTO.class, mappingContext);
	}
}
