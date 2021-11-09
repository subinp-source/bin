/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.converters;

import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;


/**
 * Bidirectional converter between {@link GroupType} and String
 */
@WsDTOMapping
public class GroupTypeConverter extends BidirectionalConverter<GroupType, String>
{
	@Override
	public GroupType convertFrom(final String source, final Type<GroupType> destinationType, final MappingContext mappingContext)
	{
		return GroupType.valueOf(source);
	}

	@Override
	public String convertTo(final GroupType source, final Type<String> destinationType, final MappingContext mappingContext)
	{
		return source.toString();
	}
}
