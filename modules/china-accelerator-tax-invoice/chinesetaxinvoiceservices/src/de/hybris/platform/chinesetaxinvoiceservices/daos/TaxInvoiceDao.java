/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoiceservices.daos;

import de.hybris.platform.chinesetaxinvoiceservices.model.TaxInvoiceModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;


public interface TaxInvoiceDao extends Dao
{

	/**
	 * Query by PK.
	 *
	 * @param code
	 *           PK
	 * @return TaxInvoiceModel
	 */
	TaxInvoiceModel findInvoiceByCode(String code);

	/**
	 * Query by serialCode
	 *
	 * @param serialCode
	 *           SerialCode of TaxInvoiceModel.
	 * @return TaxInvoiceModel
	 */
	TaxInvoiceModel findInvoiceBySerialCode(String serialCode);

	/**
	 * Query by order.
	 *
	 * @param orderCode
	 *           OrderCode of an order.
	 * @return TaxInvoiceModel
	 */
	TaxInvoiceModel findInvoiceByOrder(String orderCode);

	/**
	 * Query all TaxInvoiceModel of a Customer.
	 *
	 * @param customer
	 * @return List<TaxInvoiceModel>
	 */
	List<TaxInvoiceModel> findInvoicesByCustomer(CustomerModel customer);
}
