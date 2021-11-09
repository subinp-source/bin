/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.dao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.workflow.model.WorkflowModel;



/**
 * The Interface B2BWorkflowDao.
 *
 * @spring.bean b2bWorkflowDao
 */
public interface B2BWorkflowDao
{

	/**
	 * @deprecated As of hybris 4.4, replaced by {@link #findWorkflowByOrder(OrderModel)}
	 */
	@Deprecated(since = "4.4", forRemoval = true)
	public abstract WorkflowModel findWorkflowForOrder(final OrderModel order);

	/**
	 * Find the workflow of an order.
	 *
	 * @param order
	 *           the order
	 * @return the WorkflowModel
	 */
	public abstract WorkflowModel findWorkflowByOrder(final OrderModel order);

}
