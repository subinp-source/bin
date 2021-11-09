/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.dao.impl;

import de.hybris.platform.chinesepspalipayservices.dao.AlipayPaymentTransactionDao;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.Collections;


/**
 * Default implementation of {@link AlipayPaymentTransactionDao}
 */
public class DefaultAlipayPaymentTransactionDao extends DefaultGenericDao<AlipayPaymentTransactionModel>
		implements AlipayPaymentTransactionDao
{

	public DefaultAlipayPaymentTransactionDao()
	{
		super(AlipayPaymentTransactionModel._TYPECODE);
	}

	@Override
	public AlipayPaymentTransactionModel findTransactionByLatestRequestEntry(final OrderModel orderModel, final boolean limit)
	{
		final StringBuilder entryQuery = new StringBuilder("GET {");
		entryQuery.append(AlipayPaymentTransactionEntryModel._TYPECODE);
		entryQuery.append("} WHERE {");
		entryQuery.append(AlipayPaymentTransactionEntryModel.TYPE);
		entryQuery.append("} = ?type ORDER BY {");
		entryQuery.append(AlipayPaymentTransactionEntryModel.TIME);
		entryQuery.append("} DESC");

		return getFlexibleSearchService()
				.<AlipayPaymentTransactionEntryModel> search(entryQuery.toString(),
						Collections.singletonMap("type", PaymentTransactionType.REQUEST))
				.getResult().stream().filter(entry -> entry.getPaymentTransaction().getOrder().equals(orderModel))
				.filter(e -> limit ? (e.getPaymentTransaction().getEntries().size() == 1) : !limit)
				.map(AlipayPaymentTransactionEntryModel::getPaymentTransaction).findFirst()
				.map(t -> (AlipayPaymentTransactionModel) t).orElse(null);
	}

	@Override
	public AlipayPaymentTransactionModel findTransactionByAlipayCode(final String alipayCode)
	{
		final StringBuilder fQuery = new StringBuilder("GET {");
		fQuery.append(AlipayPaymentTransactionModel._TYPECODE);
		fQuery.append("} WHERE {");
		fQuery.append(AlipayPaymentTransactionModel.ALIPAYCODE);
		fQuery.append("} = ?alipayCode");

		return getFlexibleSearchService().<AlipayPaymentTransactionModel> search(fQuery.toString(),
				Collections.singletonMap("alipayCode", alipayCode)).getResult().stream().findFirst().orElse(null);
	}

}
