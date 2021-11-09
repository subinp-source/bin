/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentservices.order.dao;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.daos.OrderDao;
import de.hybris.platform.task.TaskModel;

import java.util.List;


/**
 * The Dao for search Order
 */
public interface ChineseOrderDao extends OrderDao
{
	/**
	 * Finding the orders which are not paid for X milliseconds.
	 *
	 * @param millisecond
	 *           The expired time
	 * @return List<AbstractOrderModel>
	 */
	List<AbstractOrderModel> findUnpaidOrders(final long millisecond);

	/**
	 * Finding the order by code
	 *
	 * @param orderCode
	 *           the code of the order
	 * @return AbstractOrderModel
	 */
	AbstractOrderModel findOrderByCode(final String orderCode);

	/**
	 * Finds SubmitOrderTask by orderCode
	 * 
	 * @param orderCode
	 *           the order code
	 * @return the task model
	 */
	TaskModel findSubmitOrderEventTask(String orderCode);
}
