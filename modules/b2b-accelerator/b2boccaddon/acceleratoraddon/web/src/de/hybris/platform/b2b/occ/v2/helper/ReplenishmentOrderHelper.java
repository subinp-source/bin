/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.helper;

import de.hybris.platform.b2bacceleratorfacades.order.B2BOrderFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.ScheduledCartData;
import de.hybris.platform.b2bwebservicescommons.dto.order.ReplenishmentOrderData;
import de.hybris.platform.b2bwebservicescommons.dto.order.ReplenishmentOrderListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.order.ReplenishmentOrderWsDTO;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryListWsDTO;

import java.util.List;
import javax.annotation.Resource;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ReplenishmentOrderHelper extends AbstractHelper
{

	private static final Logger LOG = LoggerFactory.getLogger(ReplenishmentOrderHelper.class);

	private static final String REPLENISHMENT_ORDER_NOT_FOUND = "Replenishment order for code %s not found";
	private static final String REPLENISHMENT_ORDER_NOT_CANCELED = "Replenishment order for code %s not cancelled";

	@Resource(name = "b2bOrderFacade")
	private B2BOrderFacade b2bOrderFacade;

	@Resource(name = "b2bOrderFacade")
	private B2BOrderFacade orderFacade;

	@Resource(name = "b2bCustomerFacade")
	protected CustomerFacade customerFacade;

	public ReplenishmentOrderListWsDTO searchReplenishments(final int currentPage, final int pageSize, final String sort,
			final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final SearchPageData<ScheduledCartData> searchPageData = b2bOrderFacade.getPagedReplenishmentHistory(pageableData);

		final ReplenishmentOrderData replenishmentOrderData = createReplenishmentOrderData(searchPageData);

		return getDataMapper().map(replenishmentOrderData, ReplenishmentOrderListWsDTO.class, fields);
	}

	public ReplenishmentOrderWsDTO searchReplenishment(final String replenishmentOrderCode, final String fields)
	{
		final ScheduledCartData scheduledCartData = searchReplenishmentForCurrentCustomer(replenishmentOrderCode);
		return getDataMapper().map(scheduledCartData, ReplenishmentOrderWsDTO.class, fields);
	}

	public ReplenishmentOrderWsDTO cancelReplenishment(final String replenishmentOrderCode, final String fields)
	{
		final CustomerData currentCustomer = customerFacade.getCurrentCustomer();
		orderFacade.cancelReplenishment(replenishmentOrderCode, currentCustomer.getUid());
		final ReplenishmentOrderWsDTO replenishmentOrderWsDTO = searchReplenishment(replenishmentOrderCode, fields);
		if (replenishmentOrderWsDTO.getActive())
		{
			throwReplenishmentOrderNotCanceled(replenishmentOrderCode);
		}
		return replenishmentOrderWsDTO;
	}

	public OrderHistoryListWsDTO searchOrderHistories(final String replenishmentOrderCode, final int currentPage,
			final int pageSize, final String sort, final String fields)
	{
		searchReplenishmentForCurrentCustomer(replenishmentOrderCode);
		return searchOrderHistoriesInternal(replenishmentOrderCode, currentPage, pageSize, sort, fields);
	}

	private ReplenishmentOrderData createReplenishmentOrderData(SearchPageData<ScheduledCartData> result)
	{
		final ReplenishmentOrderData replenishmentOrderData = new ReplenishmentOrderData();

		replenishmentOrderData.setReplenishmentOrders(result.getResults());
		replenishmentOrderData.setSorts(result.getSorts());
		replenishmentOrderData.setPagination(result.getPagination());

		return replenishmentOrderData;
	}

	private ScheduledCartData searchReplenishmentForCurrentCustomer(final String replenishmentOrderCode)
	{
		final CustomerData currentCustomer = customerFacade.getCurrentCustomer();
		final ScheduledCartData replenishmentOrder = orderFacade.getReplenishmentOrderDetailsForCode(replenishmentOrderCode,
				currentCustomer.getUid());
		if (replenishmentOrder == null)
		{
			throwReplenishmentOrderNotFound(replenishmentOrderCode);
		}
		return replenishmentOrder;
	}

	private void throwReplenishmentOrderNotFound(final String replenishmentOrderCode)
	{
		final String message = String.format(REPLENISHMENT_ORDER_NOT_FOUND, sanitize(replenishmentOrderCode));
		LOG.error(message);
		throw new NotFoundException(message);
	}

	private void throwReplenishmentOrderNotCanceled(final String replenishmentOrderCode)
	{
		final String message = String.format(REPLENISHMENT_ORDER_NOT_CANCELED, sanitize(replenishmentOrderCode));
		LOG.error(message);
		throw new InternalServerErrorException(message);
	}

	private OrderHistoryListWsDTO searchOrderHistoriesInternal(final String replenishmentOrderCode, final int currentPage,
			final int pageSize, final String sort, final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final SearchPageData<? extends OrderHistoryData> searchPageData = orderFacade
				.getPagedReplenishmentOrderHistory(replenishmentOrderCode, pageableData);
		final OrderHistoriesData orderHistoriesData = createOrderHistoriesData(searchPageData);
		return getDataMapper().map(orderHistoriesData, OrderHistoryListWsDTO.class, fields);
	}

	private OrderHistoriesData createOrderHistoriesData(final SearchPageData<? extends OrderHistoryData> result)
	{
		final OrderHistoriesData orderHistoriesData = new OrderHistoriesData();
		orderHistoriesData.setOrders((List<OrderHistoryData>) result.getResults());
		orderHistoriesData.setSorts(result.getSorts());
		orderHistoriesData.setPagination(result.getPagination());
		return orderHistoriesData;
	}

}
