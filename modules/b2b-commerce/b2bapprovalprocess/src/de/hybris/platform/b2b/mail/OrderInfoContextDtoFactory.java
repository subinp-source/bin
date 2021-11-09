/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.mail;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * A factory for creating {@link de.hybris.platform.b2b.mail.impl.OrderInfoContextDto} and populating the Dto with data
 * based on the order
 * 
 * @param <T>
 *           A paramatarized dto
 */
public interface OrderInfoContextDtoFactory<T>
{
	public T createOrderInfoContextDto(final OrderModel order);
}
