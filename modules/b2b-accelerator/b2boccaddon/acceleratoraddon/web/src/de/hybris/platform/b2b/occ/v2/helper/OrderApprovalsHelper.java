/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.helper;

import de.hybris.platform.b2bacceleratorfacades.order.B2BOrderFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BOrderApprovalData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BOrderApprovalsData;
import de.hybris.platform.b2bwebservicescommons.dto.order.OrderApprovalListWsDTO;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.workflow.enums.WorkflowActionType;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


@Component
public class OrderApprovalsHelper extends AbstractHelper
{

	@Resource(name = "b2bOrderFacade")
	private B2BOrderFacade orderFacade;

	public OrderApprovalListWsDTO searchApprovals(final int currentPage, final int pageSize, final String sort,
			final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);

		final B2BOrderApprovalsData approvalsData = creatOrderApprovalsList(
				orderFacade.getPagedOrdersForApproval(new WorkflowActionType[]
						{ WorkflowActionType.START }, pageableData));

		return getDataMapper().map(approvalsData, OrderApprovalListWsDTO.class, fields);
	}

	protected B2BOrderApprovalsData creatOrderApprovalsList(final SearchPageData<B2BOrderApprovalData> result)
	{
		final B2BOrderApprovalsData approvalsData = new B2BOrderApprovalsData();

		approvalsData.setOrderApprovals(result.getResults());
		approvalsData.setSorts(result.getSorts());
		approvalsData.setPagination(result.getPagination());

		return approvalsData;
	}
}
