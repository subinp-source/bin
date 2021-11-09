/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingservices.daos;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.List;
import java.util.Optional;


/**
 * A interface for querying consignment
 */
public interface ConsignmentDao
{

	/**
	 * @param orderCode
	 *           code of this consignment's order
	 * @param consignmentCode
	 *           code of ConsignmentModel
	 * @return An optional containing the consignment if it exists and an empty optional otherwise
	 */
	Optional<ConsignmentModel> findConsignmentByCode(String orderCode, String consignmentCode);

	/**
	 *
	 * @param orderCode
	 *           order code for the consignments
	 * @return all the consignments belong to this order
	 */
	List<ConsignmentModel> findConsignmentsByOrder(String orderCode);
}
