/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.process.strategies.impl;

import java.util.Optional;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;


/**
 * Strategy to impersonate site and initialize session context from an instance of ReturnProcessModel.
 */
public class ReturnProcessContextStrategy extends AbstractOrderProcessContextStrategy
{
	@Override
	protected Optional<AbstractOrderModel> getOrderModel(final BusinessProcessModel businessProcessModel)
	{
		return Optional.of(businessProcessModel)
				.filter(businessProcess -> businessProcess instanceof ReturnProcessModel)
				.map(businessProcess -> ((ReturnProcessModel) businessProcess).getReturnRequest())
				.map(ReturnRequestModel::getOrder);
	}
}
