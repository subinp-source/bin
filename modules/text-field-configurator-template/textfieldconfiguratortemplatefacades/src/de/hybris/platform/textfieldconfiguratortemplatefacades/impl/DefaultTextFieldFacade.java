/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.textfieldconfiguratortemplatefacades.impl;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.textfieldconfiguratortemplatefacades.TextFieldFacade;

import java.util.List;
import java.util.Optional;


/**
 * Default implementation of {@link TextFieldFacade}
 */
public class DefaultTextFieldFacade implements TextFieldFacade
{
	@Override
	public OrderEntryData getAbstractOrderEntry(final int entryNumber, final AbstractOrderData abstractOrder)
			throws CommerceCartModificationException
	{
		final List<OrderEntryData> entries = abstractOrder.getEntries();
		if (entries == null)
		{
			throw new CommerceCartModificationException("Cart is empty");
		}

		final Optional<OrderEntryData> entryOptional = entries.stream().filter(e -> e != null)
				.filter(e -> e.getEntryNumber() == entryNumber).findAny();
		if (entryOptional.isPresent())
		{
			return entryOptional.get();
		}
		else
		{
			throw new CommerceCartModificationException("Cart entry #" + entryNumber + " does not exist");
		}
	}
}
