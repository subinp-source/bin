/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.mappers;

import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.occ.CsticValueWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticWsDTO;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.List;

import ma.glasnost.orika.MappingContext;


public class CsticDataMapper extends AbstractCustomMapper<CsticData, CsticWsDTO>
{

	protected static final String FIELD_OCC_DOMAIN_VALUES = "domainValues";
	protected static final String FIELD_OCC_LAND_DEP_NAME = "landDepName";
	protected static final String FIELD_OCC_NEGATIVE_ALLOWED = "negativeAllowed";

	protected static final String FIELD_DOMAINVALUES = "domainvalues";
	protected static final String FIELD_LANDDEPNAME = "landdepname";
	protected static final String FIELD_MASK = "entryFieldMask";

	@Override
	public void mapAtoB(final CsticData csticData, final CsticWsDTO csticWsDto, final MappingContext context)
	{

		// other fields are mapped automatically
		context.beginMappingField(FIELD_MASK, getAType(), csticData, FIELD_OCC_NEGATIVE_ALLOWED, getBType(), csticWsDto);
		try
		{
			final String entryFieldMask = csticData.getEntryFieldMask();
			if (shouldMap(csticData, csticWsDto, context) && entryFieldMask != null && (!entryFieldMask.isEmpty()))
			{
				csticWsDto.setNegativeAllowed(entryFieldMask.charAt(0) == '-');
			}
		}
		finally
		{
			context.endMappingField();
		}
		context.beginMappingField(FIELD_LANDDEPNAME, getAType(), csticData, FIELD_OCC_LAND_DEP_NAME, getBType(), csticWsDto);
		try
		{
			if (shouldMap(csticData, csticWsDto, context))
			{
				csticWsDto.setLangDepName(csticData.getLangdepname());
			}
		}
		finally
		{
			context.endMappingField();
		}
		context.beginMappingField(FIELD_DOMAINVALUES, getAType(), csticData, FIELD_OCC_DOMAIN_VALUES, getBType(), csticWsDto);
		try
		{
			final List<CsticValueData> domainvalues = csticData.getDomainvalues();
			if (shouldMap(csticData, csticWsDto, context) && domainvalues != null)
			{
				csticWsDto.setDomainValues(mapperFacade.mapAsList(domainvalues, CsticValueWsDTO.class, context));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final CsticWsDTO csticWsDto, final CsticData csticData, final MappingContext context)
	{

		context.beginMappingField(FIELD_OCC_NEGATIVE_ALLOWED, getBType(), csticWsDto, FIELD_MASK, getAType(), csticData);
		try
		{
			if (shouldMap(csticWsDto, csticData, context))
			{
				csticData.setEntryFieldMask(null);
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField(FIELD_OCC_LAND_DEP_NAME, getBType(), csticWsDto, FIELD_LANDDEPNAME, getAType(), csticData);
		try
		{
			if (shouldMap(csticWsDto, csticData, context))
			{
				csticData.setLangdepname(csticWsDto.getLangDepName());
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField(FIELD_OCC_DOMAIN_VALUES, getBType(), csticWsDto, FIELD_DOMAINVALUES, getAType(), csticData);
		try
		{
			final List<CsticValueWsDTO> domainValues = csticWsDto.getDomainValues();
			if (shouldMap(csticWsDto, csticData, context) && domainValues != null)
			{
				csticData.setDomainvalues(mapperFacade.mapAsList(domainValues, CsticValueData.class, context));
			}
		}
		finally
		{
			context.endMappingField();
		}

	}
}
