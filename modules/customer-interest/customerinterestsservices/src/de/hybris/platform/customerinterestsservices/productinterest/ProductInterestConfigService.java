/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.customerinterestsservices.productinterest;

import java.util.Date;


/**
 * Service to deal with the product interests configuration.
 */
public interface ProductInterestConfigService
{
	/**
	 * Finds product interest expiry day.
	 *
	 * @return
	 */
	int getProductInterestExpiryDay();

	/**
	 * Finds product interest expiry date according to the creation date.
	 *
	 * @param creationDate
	 *           product interest creation date
	 * @return product interest expiry date
	 */
	Date getProductInterestExpiryDate(Date creationDate);

}
