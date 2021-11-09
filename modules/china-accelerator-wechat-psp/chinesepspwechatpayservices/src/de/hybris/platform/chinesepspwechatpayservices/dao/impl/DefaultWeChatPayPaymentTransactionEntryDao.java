/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.chinesepspwechatpayservices.dao.WeChatPayPaymentTransactionEntryDao;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.WeChatPayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.WeChatPayPaymentTransactionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;


public class DefaultWeChatPayPaymentTransactionEntryDao extends DefaultGenericDao<WeChatPayPaymentTransactionEntryModel>
implements WeChatPayPaymentTransactionEntryDao
{
	private static final Logger LOG = Logger.getLogger(DefaultWeChatPayPaymentTransactionEntryDao.class);
	private static final String TRANSACTION_STATUS = "transactionstatus";
	private static final String PAYMENT_TRANSACTION = "transaction";

	public DefaultWeChatPayPaymentTransactionEntryDao()
	{
		super(WeChatPayPaymentTransactionEntryModel._TYPECODE);
	}

	@Override
	public List<WeChatPayPaymentTransactionEntryModel> findPaymentTransactionEntryByTypeAndStatus(
			final PaymentTransactionType capture, final TransactionStatus status,
			final WeChatPayPaymentTransactionModel weChatPayPaymentTransaction)
	{
		validateParameterNotNull(capture, "PaymentTransactionType cannot be null");
		validateParameterNotNull(status, "status cannot be null");
		validateParameterNotNull(weChatPayPaymentTransaction, "WeChatPaymentTransaction cannot be null");

		final StringBuilder fQuery = new StringBuilder("GET {");
		fQuery.append(WeChatPayPaymentTransactionEntryModel._TYPECODE);
		fQuery.append("} WHERE {");
		fQuery.append(WeChatPayPaymentTransactionEntryModel.TRANSACTIONSTATUS);
		fQuery.append("} = ?transactionstatus AND {");
		fQuery.append(WeChatPayPaymentTransactionEntryModel.PAYMENTTRANSACTION);
		fQuery.append("} = ?transaction ORDER BY {");
		fQuery.append(WeChatPayPaymentTransactionEntryModel.TIME);
		fQuery.append("} DESC");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(fQuery.toString());
		query.addQueryParameter(TRANSACTION_STATUS, status.name());
		query.addQueryParameter(PAYMENT_TRANSACTION, weChatPayPaymentTransaction.getPk().getLongValueAsString());

		final List<WeChatPayPaymentTransactionEntryModel> entries = getFlexibleSearchService()
				.<WeChatPayPaymentTransactionEntryModel> search(query).getResult();
		if (entries == null)
		{
			LOG.error("There is no WeChat payment transaction entry with type: " + capture + " and status: " + status);
			return Collections.emptyList();
		}
		return entries.stream().filter(e -> e.getType() == capture).collect(Collectors.toList());
	}
}
