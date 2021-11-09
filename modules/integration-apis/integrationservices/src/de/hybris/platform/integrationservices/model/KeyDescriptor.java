/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.model;

import de.hybris.platform.integrationservices.integrationkey.KeyValue;

import java.util.List;
import java.util.Map;

/**
 * Descriptor of an {@link IntegrationObjectItemModel} key. Each integration object item must have at least one (a simple key) or
 * several (composite key) attributes, which identify instances of the item type and therefore define its key.
 */
public interface KeyDescriptor
{
	/**
	 * Calculates key value for the specified data item.
	 * @param item Map presentation of a data item, for which the key value needs to be calculated.
	 * @return the calculated key value.
	 */
	KeyValue calculateKey(Map<String, Object> item);

	/**
	 * Queries whether the specified attribute name is a name of the key attribute.
	 * @param attr name of the attribute to enquire about
	 * @return {@code true}, if the specified attribute is a key attribute in this key descriptor; {@code false} otherwise.
	 */
	boolean isKeyAttribute(String attr);

	/**
	 * Finds all key attributes for the descriptor, including key attributes for the
	 * descriptor and all of its referenced key attributes
	 * @return a list of key attributes
	 */
	List<KeyAttribute> getKeyAttributes();
}
