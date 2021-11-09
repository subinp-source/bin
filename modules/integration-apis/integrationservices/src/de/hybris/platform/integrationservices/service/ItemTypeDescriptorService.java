/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.service;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Optional;

/**
 * A service for retrieving Integration Object item type descriptors.
 */
public interface ItemTypeDescriptorService
{
	/**
	 * Retrieves an item type descriptor.
	 * @param objCode code of the Integration Object containing the item type that needs to be retrieved. This value should match
	 * value of an existing {@link IntegrationObjectModel#getCode()}.
	 * @param objItemCode code of the integration object item type to be retrieved. This value should match a value of an existing
	 * {@link IntegrationObjectItemModel#getCode()}
	 * @return an {@code Optional} containing the specified type descriptor or an {@code Optional.empty()}, if such descriptor is
	 * not found.
	 */
	Optional<TypeDescriptor> getTypeDescriptor(String objCode, String objItemCode);

	/**
	 * Retrieves an item type descriptor for the specified objectItemCode or a supertype of the objectItemCode.
	 *
	 * @param objCode - integration object code
	 * @param itemTypeCode - integration object item code
	 * @return - optional type descriptor for the specified objCode and objItemCode or it's parent type descriptor.
	 */
	Optional<TypeDescriptor> getTypeDescriptorByTypeCode(final String objCode, final String itemTypeCode);
}
