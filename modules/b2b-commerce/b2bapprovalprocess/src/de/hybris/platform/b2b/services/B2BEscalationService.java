/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.services;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * The Interface B2BEscalationService. A service for escalating orders that need approval to the next eligible
 * approvers.
 *
 * @spring.bean b2bEscalationService
 */
public interface B2BEscalationService
{

	/**
	 * Escalate the order's current approver to the next eligible approver.
	 *
	 * @param order
	 *           the order in which the approver will be escalated to the next higher eligible approver
	 * @return true, if successful
	 */
	public abstract boolean escalate(final OrderModel order);

	/**
	 * Escalate to a member of b2badmingroup assigned to a parent unit of a user who created the order.
	 *
	 * @param order
	 *           the order
	 * @return true, if the assignment was successful
	 * @deprecated Since 4.4.
	 */
	@Deprecated(since = "4.4", forRemoval = true)
	public abstract boolean handleEscalationFailure(final OrderModel order);

	/**
	 * Schedule escalation task based on configuration property 'escalationtask.executiontime.milliseconds'
	 *
	 * @param order
	 *           the order in which the approver will be escalated to the next higher eligible approver
	 */
	public abstract void scheduleEscalationTask(final OrderModel order);

	/**
	 * Checks if the order can be escalated to a different approver(s)
	 *
	 * @param order
	 *           An order placed in a b2b store
	 * @return True if an order can be escalated to different approver(s)
	 */
	public abstract boolean canEscalate(OrderModel order);
}
