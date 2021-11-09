/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;


import de.hybris.platform.commerceservices.service.data.CommerceOrderParameter;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.List;


/**
 * Strategy tp calculate a hash value based on the abstractOrderModel state plus the additionalValues
 */
public interface CommerceCartHashCalculationStrategy
{
	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #buildHashForAbstractOrder(de.hybris.platform.commerceservices.service.data.CommerceOrderParameter)}
	 *             instead Calculate a hash for the order + additional values passed as a parameter
	 *
	 * @param abstractOrderModel
	 *           the order to calculate the hash for
	 * @param additionalValues
	 *           the additional values
	 * @return the calculated hash
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	String buildHashForAbstractOrder(AbstractOrderModel abstractOrderModel, List<String> additionalValues);


	/**
	 * Calculate a hash for the order + additional values passed as a parameter
	 * 
	 * @param parameter
	 *           A parameter object holding the following values - abstractOrderModel the order to calculate the hash for
	 *           - additionalValues the additional values
	 * @return the calculated hash
	 */
	String buildHashForAbstractOrder(final CommerceOrderParameter parameter);


}
