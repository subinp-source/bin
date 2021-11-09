/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.process.strategies.impl;

import java.util.Optional;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;


/**
 * Strategy to impersonate site and initialize session context from an instance of ConsignmentProcessModel.
 */
public class ConsignmentProcessContextStrategy extends AbstractOrderProcessContextStrategy
{
	@Override
	protected Optional<AbstractOrderModel> getOrderModel(final BusinessProcessModel businessProcessModel)
	{
		return Optional.of(businessProcessModel).filter(businessProcess -> businessProcess instanceof ConsignmentProcessModel)
				.map(businessProcess -> ((ConsignmentProcessModel) businessProcess).getConsignment()).map(ConsignmentModel::getOrder);
	}
}
