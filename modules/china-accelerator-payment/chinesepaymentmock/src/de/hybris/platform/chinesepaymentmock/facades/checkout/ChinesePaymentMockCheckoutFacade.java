/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentmock.facades.checkout;

import de.hybris.platform.chinesepaymentfacades.checkout.ChineseCheckoutFacade;
import de.hybris.platform.chinesepaymentfacades.checkout.impl.DefaultChineseCheckoutFacade;
import de.hybris.platform.chinesepaymentfacades.payment.data.ChinesePaymentRequestData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

/**
 * The mock facade of ChineseCheckout
 */
public class ChinesePaymentMockCheckoutFacade extends DefaultChineseCheckoutFacade implements ChineseCheckoutFacade
{
	private static final String CHINESE_ORDER_CONFIRMATION_URL = "/checkout/orderConfirmation/";

	private KeyGenerator paymentTransactionKeyGenerator;


	@Override
	public ChinesePaymentRequestData createChinesePaymentRequestData(final String orderCode)
	{
		final String url = buildPaymentRequestUrl(orderCode);
		submitOrder(orderCode);
		return buildChinesePaymentRequestData(url, null, orderCode);
	}

	@Override
	public String buildPaymentRequestUrl(final String orderCode)
	{
		final OrderModel orderModel = getChineseCheckoutService().getOrderByCode(orderCode);
		Assert.notNull(orderModel, "orderModel cannot be null");
		createTransacionForOrder(orderModel);
		syncOrderPaymentStatus(orderModel);
		return CHINESE_ORDER_CONFIRMATION_URL
				+ (getCheckoutCustomerStrategy().isAnonymousCheckout() ? orderModel.getGuid() : orderModel.getCode());
	}

	@Override
	protected ChinesePaymentRequestData buildChinesePaymentRequestData(final String url, final Date expiredDate,
			final String orderCode)
	{
		final ChinesePaymentRequestData chinesePaymentRequest = new ChinesePaymentRequestData();
		if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(orderCode))
		{
			chinesePaymentRequest.setUrl(url);
			chinesePaymentRequest.setExpiredDate(expiredDate);
			chinesePaymentRequest.setOrder(getOrderFacade().getOrderDetailsForCodeWithoutUser(orderCode));
		}
		return chinesePaymentRequest;
	}

	@Override
	public OrderData syncPaymentStatusForOrder(final String orderCode)
	{
		return getOrderFacade().getOrderDetailsForCode(orderCode);
	}

	protected void createTransacionForOrder(final OrderModel orderModel)
	{
		final PaymentTransactionModel transaction = getModelService().create(PaymentTransactionModel.class);
		transaction.setOrder(orderModel);
		transaction.setCode(orderModel.getCode() + "_" + UUID.randomUUID());
		transaction.setRequestId(orderModel.getCode());
		transaction.setPaymentProvider(orderModel.getChinesePaymentInfo().getPaymentProvider());
		final List<PaymentTransactionEntryModel> transactionEntries = createTransactionEntriesForOrder(orderModel, transaction);
		transaction.setEntries(transactionEntries);
		getModelService().save(transaction);
	}

	protected List<PaymentTransactionEntryModel> createTransactionEntriesForOrder(final OrderModel orderModel,
			final PaymentTransactionModel transaction)
	{
		final List<PaymentTransactionEntryModel> entries = new ArrayList<>();
		entries.add(createTransactionEntry(orderModel, transaction, PaymentTransactionType.REQUEST));
		entries.add(createTransactionEntry(orderModel, transaction, PaymentTransactionType.CAPTURE));
		return entries;
	}

	protected PaymentTransactionEntryModel createTransactionEntry(final OrderModel orderModel,
			final PaymentTransactionModel transaction, final PaymentTransactionType paymentTransactionType)
	{
		final PaymentTransactionEntryModel entry = getModelService().create(PaymentTransactionEntryModel.class);
		entry.setAmount(BigDecimal.valueOf(orderModel.getTotalPrice().doubleValue()));
		if (orderModel.getCurrency() != null)
		{
			entry.setCurrency(orderModel.getCurrency());
		}
		entry.setType(paymentTransactionType);
		entry.setTime(new Date());
		entry.setPaymentTransaction(transaction);
		entry.setRequestId(transaction.getRequestId());
		entry.setTransactionStatus(TransactionStatus.ACCEPTED.name());
		entry.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL.name());
		entry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()));
		return entry;
	}

	protected void syncOrderPaymentStatus(final OrderModel orderModel)
	{
		if (CollectionUtils.isNotEmpty(orderModel.getPaymentTransactions())
				&& CollectionUtils.isNotEmpty(orderModel.getPaymentTransactions().get(0).getEntries()))
		{
			orderModel.setPaymentStatus(PaymentStatus.PAID);
			getModelService().save(orderModel);
			getModelService().refresh(orderModel);
		}
	}

	/**
	 * @deprecated since 2005
	 */
	@Deprecated(since = "2005", forRemoval = true)
	@Override
	protected ChinesePaymentRequestData buildChinesePaymentRequestData(final String url, final Date expiredDate)
	{
		final ChinesePaymentRequestData chinesePaymentRequest = new ChinesePaymentRequestData();
		if (StringUtils.isNotBlank(url))
		{
			chinesePaymentRequest.setUrl(url);
			chinesePaymentRequest.setExpiredDate(expiredDate);
		}
		return chinesePaymentRequest;
	}

	protected KeyGenerator getPaymentTransactionKeyGenerator()
	{
		return paymentTransactionKeyGenerator;
	}

	@Required
	public void setPaymentTransactionKeyGenerator(final KeyGenerator paymentTransactionKeyGenerator)
	{
		this.paymentTransactionKeyGenerator = paymentTransactionKeyGenerator;
	}



}
