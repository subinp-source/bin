/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.mapping.mappers;

import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BApprovalProcessWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitWsDTO;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.Optional;

import ma.glasnost.orika.MappingContext;


public class B2BUnitDataMapper extends AbstractCustomMapper<B2BUnitData, B2BUnitWsDTO>
{
	private static final String PARENT_ORG_UNIT = "parentOrgUnit";
	private static final String UNIT = "unit";

	private static final String APPROVAL_PROCESS_CODE = "approvalProcessCode";
	private static final String APPROVAL_PROCESS_NAME = "approvalProcessName";
	private static final String APPROVAL_PROCESS = "approvalProcess";

	@Override
	public void mapAtoB(final B2BUnitData a, final B2BUnitWsDTO b, final MappingContext context)
	{
		// other fields are mapped automatically
		mapAToBParentUnit(a, b, context);
		mapAToBApprovalProcess(a, b, context);
	}

	@Override
	public void mapBtoA(final B2BUnitWsDTO b, final B2BUnitData a, final MappingContext context)
	{
		// other fields are mapped automatically
		mapBtoAParentUnit(b, a, context);
		mapBtoAApprovalProcess(b, a, context);
	}

	private void mapAToBParentUnit(final B2BUnitData a, final B2BUnitWsDTO b, final MappingContext context)
	{
		context.beginMappingField(UNIT, getAType(), a, PARENT_ORG_UNIT, getBType(), b);
		try
		{
			if (shouldMap(a, b, context))
			{
				b.setParentOrgUnit(mapperFacade.map(a.getUnit(), B2BUnitWsDTO.class, context));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	private void mapBtoAParentUnit(final B2BUnitWsDTO b, final B2BUnitData a, final MappingContext context)
	{
		context.beginMappingField(PARENT_ORG_UNIT, getBType(), b, UNIT, getAType(), a);
		try
		{
			if (shouldMap(b, a, context))
			{
				a.setUnit(mapperFacade.map(b.getParentOrgUnit(), B2BUnitData.class, context));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	private void mapAToBApprovalProcess(final B2BUnitData a, final B2BUnitWsDTO b, final MappingContext context)
	{
		context.beginMappingField(APPROVAL_PROCESS_CODE, getAType(), a, APPROVAL_PROCESS, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getApprovalProcessCode() != null)
			{
				B2BApprovalProcessWsDTO approvalProcessWsDTO = Optional.ofNullable(b.getApprovalProcess())
						.orElseGet(B2BApprovalProcessWsDTO::new);
				approvalProcessWsDTO.setCode(a.getApprovalProcessCode());

				b.setApprovalProcess(approvalProcessWsDTO);
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField(APPROVAL_PROCESS_NAME, getAType(), a, APPROVAL_PROCESS, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getApprovalProcessName() != null)
			{
				B2BApprovalProcessWsDTO approvalProcessWsDTO = Optional.ofNullable(b.getApprovalProcess())
						.orElseGet(B2BApprovalProcessWsDTO::new);
				approvalProcessWsDTO.setName(a.getApprovalProcessName());

				b.setApprovalProcess(approvalProcessWsDTO);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	private void mapBtoAApprovalProcess(final B2BUnitWsDTO b, final B2BUnitData a, final MappingContext context)
	{
		context.beginMappingField(APPROVAL_PROCESS, getBType(), b, APPROVAL_PROCESS_CODE, getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && b.getApprovalProcess() != null)
			{
				a.setApprovalProcessCode(b.getApprovalProcess().getCode());
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField(APPROVAL_PROCESS, getBType(), b, APPROVAL_PROCESS_NAME, getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && b.getApprovalProcess() != null)
			{
				a.setApprovalProcessName(b.getApprovalProcess().getName());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}
}
