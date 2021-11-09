/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.order.dao.impl;

import static java.util.stream.Collectors.toList;

import de.hybris.platform.acceleratorservices.order.dao.AcceleratorConsignmentDao;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Default implementation
 */
public class DefaultAcceleratorConsignmentDao extends DefaultGenericDao<ConsignmentModel> implements AcceleratorConsignmentDao
{

	/**
	 * DefaultGenericDao is only usable when typecode is set.
	 */
	public DefaultAcceleratorConsignmentDao()
	{
		super(ConsignmentModel._TYPECODE);
	}

	@Override
	public List<ConsignmentModel> findConsignmentsForStatus(final List<ConsignmentStatus> statuses,
			final Collection<BaseSiteModel> sites)
	{


		final List<ConsignmentModel> consignmentModels = getConsignmentsWithGivenStatusAndJoinedWithOrder(statuses);
		{
			return consignmentModels.stream().filter(consignment -> orderContainsSite(consignment.getOrder(), sites))
					.collect(toList());

		}
	}

	private static boolean orderContainsSite(final AbstractOrderModel order, final Collection<BaseSiteModel> sites)
	{
		if (sites.isEmpty())
		{
			return true;
		}

		if (order.getSite() == null)
		{
			return false;
		}

		return sites.contains(order.getSite());
	}

	private List<ConsignmentModel> getConsignmentsWithGivenStatusAndJoinedWithOrder(final Collection<ConsignmentStatus> statuses)
	{
		final String queryString = "GET {" + ConsignmentModel._TYPECODE + "} WHERE {" + ConsignmentModel.ORDER + "} IS NOT NULL"
				+ (statuses.isEmpty() ? ""
						: (" AND (" + statuses.stream()
								.map(status -> "{" + ConsignmentModel.STATUS + "} = ?status" + getStatusHashCode(status.hashCode()))
								.collect(Collectors.joining(" OR "))) + " )");

		final Map<String, Object> attributes = statuses.stream()
				.collect(Collectors.toMap(status -> "status" + getStatusHashCode(status.hashCode()), status -> status));

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, attributes);

		final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);

		return result.getResult();
	}

	private String getStatusHashCode(final int hash)
	{
		if (hash == Integer.MIN_VALUE)
		{
			return Integer.toString(Integer.MAX_VALUE);
		}
		else
		{
			return Integer.toString(Math.abs(hash));
		}
	}

}
