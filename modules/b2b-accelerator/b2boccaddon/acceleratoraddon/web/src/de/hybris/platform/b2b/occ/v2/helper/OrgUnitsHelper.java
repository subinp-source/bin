/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.helper;

import de.hybris.platform.b2bapprovalprocessfacades.company.B2BApprovalProcessFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BApprovalProcessListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BApprovalProcessWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitNodeListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitNodeWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUsersData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;


@Component
public class OrgUnitsHelper extends AbstractHelper
{

	@Resource(name = "b2bUnitFacade")
	protected B2BUnitFacade b2bUnitFacade;

	@Resource(name = "b2bApprovalProcessFacade")
	protected B2BApprovalProcessFacade b2bApprovalProcessFacade;

	public B2BUnitNodeListWsDTO getAvailableOrgUnitNodes(final String fields)
	{
		final List<B2BUnitNodeData> branchNodes = b2bUnitFacade.getBranchNodes();

		final B2BUnitNodeListWsDTO b2BUnitNodeListWsDTO = new B2BUnitNodeListWsDTO();
		b2BUnitNodeListWsDTO.setUnitNodes(getDataMapper().mapAsList(branchNodes, B2BUnitNodeWsDTO.class, fields));

		return b2BUnitNodeListWsDTO;
	}

	public B2BUnitNodeListWsDTO getAvailableParentUnits(final String uid, final String fields)
	{
		final List<B2BUnitNodeData> units = b2bUnitFacade.getAllowedParentUnits(uid);

		final B2BUnitNodeListWsDTO b2BUnitNodeListWsDTO = new B2BUnitNodeListWsDTO();
		b2BUnitNodeListWsDTO.setUnitNodes(getDataMapper().mapAsList(units, B2BUnitNodeWsDTO.class, fields));

		return b2BUnitNodeListWsDTO;
	}

	public OrgUnitUserListWsDTO convertPagedUsersForUnit(final SearchPageData<CustomerData> result, final String fields)
	{
		final OrgUnitUsersData usersData = createUserList(result);
		return getDataMapper().map(usersData, OrgUnitUserListWsDTO.class, fields);
	}

	public B2BApprovalProcessListWsDTO getApprovalProcesses()
	{
		final List<B2BApprovalProcessWsDTO> approvalProcesses = b2bApprovalProcessFacade.getProcesses().entrySet().stream()
				.map(this::mapToB2BApprovalProcess).collect(Collectors.toList());

		final B2BApprovalProcessListWsDTO b2BApprovalProcessList = new B2BApprovalProcessListWsDTO();
		b2BApprovalProcessList.setApprovalProcesses(approvalProcesses);
		return b2BApprovalProcessList;
	}

	private B2BApprovalProcessWsDTO mapToB2BApprovalProcess(final Map.Entry<String, String> process)
	{
		final B2BApprovalProcessWsDTO b2BApprovalProcess = new B2BApprovalProcessWsDTO();
		b2BApprovalProcess.setCode(process.getKey());
		b2BApprovalProcess.setName(process.getValue());

		return b2BApprovalProcess;
	}


	protected OrgUnitUsersData createUserList(final SearchPageData<CustomerData> result)
	{
		final OrgUnitUsersData usersData = new OrgUnitUsersData();

		usersData.setUsers(result.getResults());
		usersData.setSorts(result.getSorts());
		usersData.setPagination(result.getPagination());

		return usersData;
	}
}
