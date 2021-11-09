/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorocc.payment.dao;


import de.hybris.platform.commercewebservicescommons.model.payment.PaymentSubscriptionResultModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Date;
import java.util.List;


/**
 * Dao for {@link PaymentSubscriptionResultModel} access.
 *
 * @spring.bean paymentSubscriptionResultDao
 */
public interface PaymentSubscriptionResultDao extends Dao
{
	/**
	 * Find payment subscription result for given cart
	 *
	 * @param cartId
	 * 		- cart identifier (code or guid)
	 * @return payment subscription result
	 * @throws ModelNotFoundException
	 * 		if payment subscription result for given cart doesn't exist
	 */
	PaymentSubscriptionResultModel findPaymentSubscriptionResultByCart(String cartId);

	/**
	 * Find payment subscription result for cart
	 *
	 * @param cartCode
	 * 		Cart code
	 * @param cartGuid
	 * 		Cart guid
	 * @return list with found objects
	 */
	List<PaymentSubscriptionResultModel> findPaymentSubscriptionResultByCart(String cartCode, String cartGuid);

	/**
	 * Find payment subscription result modified before given date
	 *
	 * @param modifiedBefore
	 * 		Date before which subscription result should be modified
	 * @return list with found objects
	 */
	List<PaymentSubscriptionResultModel> findOldPaymentSubscriptionResult(Date modifiedBefore);
}
