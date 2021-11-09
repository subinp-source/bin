/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.chinesepspalipayservices.dao.AlipayPaymentTransactionEntryDao;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;


/**
 * Default implementation of {@link AlipayPaymentTransactionEntryDao}
 */
public class DefaultAlipayPaymentTransactionEntryDao extends DefaultGenericDao<AlipayPaymentTransactionEntryModel>
implements AlipayPaymentTransactionEntryDao
{
	private static final Logger LOG = Logger.getLogger(DefaultAlipayPaymentTransactionEntryDao.class);
	private static final String TRANSACTION_STATUS = "transactionstatus";
	private static final String PAYMENT_TRANSACTION = "transaction";


	public DefaultAlipayPaymentTransactionEntryDao()
	{
		super(AlipayPaymentTransactionEntryModel._TYPECODE);
	}


	@Override
	public List<AlipayPaymentTransactionEntryModel> findPaymentTransactionEntryByTypeAndStatus(
			final PaymentTransactionType capture, final TransactionStatus status,
			final AlipayPaymentTransactionModel alipayPaymentTransaction)
	{
		validateParameterNotNull(capture, "PaymentTransactionType cannot be null");
		validateParameterNotNull(status, "status cannot be null");
		validateParameterNotNull(alipayPaymentTransaction, "AlipayPaymentTransaction cannot be null");

		final StringBuilder fQuery = new StringBuilder();
		fQuery.append("GET {");
		fQuery.append(AlipayPaymentTransactionEntryModel._TYPECODE);
		fQuery.append("} WHERE {");
		fQuery.append(AlipayPaymentTransactionEntryModel.TRANSACTIONSTATUS);
		fQuery.append("} = ?transactionstatus AND {");
		fQuery.append(AlipayPaymentTransactionEntryModel.PAYMENTTRANSACTION);
		fQuery.append("} = ?transaction ORDER BY {");
		fQuery.append(AlipayPaymentTransactionEntryModel.TIME);
		fQuery.append("} DESC");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(fQuery.toString());
		query.addQueryParameter(TRANSACTION_STATUS, status.name());
		query.addQueryParameter(PAYMENT_TRANSACTION, alipayPaymentTransaction.getPk().getLongValueAsString());

		final List<AlipayPaymentTransactionEntryModel> entries = getFlexibleSearchService()
				.<AlipayPaymentTransactionEntryModel> search(query).getResult();

		if (entries == null)
		{
			LOG.error("There is no Alipay payment transaction entry with type: " + capture + " and status: " + status);
			return Collections.emptyList();
		}

		return entries.stream().filter(e -> e.getType() == capture).collect(Collectors.toList());
	}

}
