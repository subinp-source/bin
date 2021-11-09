/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.dao.impl;

import de.hybris.platform.chinesepspwechatpayservices.dao.WeChatPayPaymentTransactionDao;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.WeChatPayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.WeChatPayPaymentTransactionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.Collections;
import java.util.Optional;



public class DefaultWeChatPayPaymentTransactionDao extends DefaultGenericDao<WeChatPayPaymentTransactionModel>
		implements WeChatPayPaymentTransactionDao
{

	public DefaultWeChatPayPaymentTransactionDao()
	{
		super(WeChatPayPaymentTransactionModel._TYPECODE);
	}

	@Override
	public Optional<WeChatPayPaymentTransactionModel> findTransactionByLatestRequestEntry(final OrderModel orderModel,
			final boolean limit)
	{
		final StringBuilder entryQuery = new StringBuilder("GET {");
		entryQuery.append(WeChatPayPaymentTransactionEntryModel._TYPECODE);
		entryQuery.append("} WHERE {");
		entryQuery.append(WeChatPayPaymentTransactionEntryModel.TYPE);
		entryQuery.append("} = ?type ORDER BY {");
		entryQuery.append(WeChatPayPaymentTransactionEntryModel.TIME);
		entryQuery.append("} DESC");

		return getFlexibleSearchService()
				.<WeChatPayPaymentTransactionEntryModel> search(entryQuery.toString(),
						Collections.singletonMap("type", PaymentTransactionType.WECHAT_REQUEST))
				.getResult().stream().filter(e -> e.getPaymentTransaction().getOrder().equals(orderModel))
				.filter(e -> limit ? (e.getPaymentTransaction().getEntries().size() == 1) : !limit)
				.map(WeChatPayPaymentTransactionEntryModel::getPaymentTransaction).findFirst()
				.map(t -> (WeChatPayPaymentTransactionModel) t);
	}


	@Override
	public Optional<WeChatPayPaymentTransactionModel> findTransactionByWeChatPayCode(final String weChatPayCode)
	{
		final StringBuilder fQuery = new StringBuilder("GET {");
		fQuery.append(WeChatPayPaymentTransactionModel._TYPECODE);
		fQuery.append("} WHERE {");
		fQuery.append(WeChatPayPaymentTransactionModel.WECHATPAYCODE);
		fQuery.append("} = ?weChatPayCode");

		return getFlexibleSearchService().<WeChatPayPaymentTransactionModel> search(fQuery.toString(),
				Collections.singletonMap("weChatPayCode", weChatPayCode)).getResult().stream().findFirst();
	}

}
