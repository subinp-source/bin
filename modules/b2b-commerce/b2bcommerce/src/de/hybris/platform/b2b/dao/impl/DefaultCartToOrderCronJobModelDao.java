/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.dao.impl;

import de.hybris.platform.b2b.dao.CartToOrderCronJobModelDao;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Default implementation of the {@link CartToOrderCronJobModelDao}
 *
 *
 * @spring.bean cartToOrderCronJobModelDao
 */
public class DefaultCartToOrderCronJobModelDao extends DefaultGenericDao<CartToOrderCronJobModel> implements
		CartToOrderCronJobModelDao
{
	/**
	 * DefaultGenericDao is only usable when typecode is set.
	 */
	public DefaultCartToOrderCronJobModelDao()
	{
		super(CartToOrderCronJobModel._TYPECODE);
	}

	@Override
	public CartToOrderCronJobModel findCartToOrderCronJob(final String code)
	{
		final List<CartToOrderCronJobModel> jobs = this.find(Collections.singletonMap(CartToOrderCronJobModel.CODE, code));
		return (jobs.iterator().hasNext() ? jobs.iterator().next() : null);
	}

	@Override
	public List<CartToOrderCronJobModel> findCartToOrderCronJobs(final UserModel user)
	{
		final Map<String, Object> attr = new HashMap<>();
		attr.put(OrderModel.USER, user);
		final String sql = "GET {" + CartModel._TYPECODE + "} WHERE {user} = ?user ORDER BY {date}";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql);
		query.getQueryParameters().putAll(attr);

		final SearchResult<CartModel> result = this.getFlexibleSearchService().search(query);
		return result.getResult().stream().flatMap(c -> CollectionUtils.emptyIfNull(c.getCartToOrderCronJob()).stream()).filter(
				CronJobModel::getActive).collect(Collectors.toList());
	}
}
