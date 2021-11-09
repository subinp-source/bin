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

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import java.util.Optional;
import java.util.Set;

/**
 * Provides methods to interact with Integration Object models.
 */
public interface IntegrationObjectService
{
	/**
	 * Find an integration object with the given code.
	 *
	 * @param integrationObjectCode Integration object's code
	 * @return integration object with the given code
	 */
	IntegrationObjectModel findIntegrationObject(String integrationObjectCode);

	/**
	 * Find all integration object item models
	 *
	 * @param integrationObjectCode Integration object's code
	 * @return Set of integration object items, or empty set if none is found
	 */
	Set<IntegrationObjectItemModel> findAllIntegrationObjectItems(String integrationObjectCode);

	/**
	 * Find all the dependency types, which include dependencies of dependencies,
	 * for the given integration object item code.
	 *
	 * @param integrationObjectItemCode Integration object item's code
	 * @param integrationObjectCode Integration object's code
	 * @return Set of integration object items, or empty set if none is found
	 */
	Set<IntegrationObjectItemModel> findAllDependencyTypes(String integrationObjectItemCode, String integrationObjectCode);

	/**
	 * Find all IntegrationObjectModel associated with IntegrationObjectItemModel,
	 * which associates with the given itemTypeCode.
	 *
	 * @param itemTypeCode ComposedType associated with IntegrationObjectItemModel
	 * @return Set of integration objects, or empty set if none is found
	 */
	Set<IntegrationObjectModel> findAllIntegrationObjects(String itemTypeCode);

	/**
	 * Find an AttributeDescriptor if it exists for the given integrationObjectItemCode and
	 * integrationObjectItemAttributeName
	 * @param integrationObjectCode Integration object's code
	 * @param integrationObjectItemCode Integration object item's code
	 * @param integrationObjectItemAttributeName Integration object item attribute Name
	 * @return the AttributeDescriptorModel for the attribute.
	 * @throws AttributeDescriptorNotFoundException if attribute cannot be found.
	 * @deprecated since 1811.0 - use {@link #findItemAttributeName(String, String, String)} to get attribute name. Then call
	 * {@link de.hybris.platform.servicelayer.type.TypeService#getAttributeDescriptor(String, String)} or
	 * {@link de.hybris.platform.servicelayer.type.TypeService#getAttributeDescriptor(ComposedTypeModel, String)} with the
	 * ItemType and attribute name to get attribute descriptor.
	 */
	@Deprecated(since = "1811.0", forRemoval= true )
	AttributeDescriptorModel findAttributeDescriptor(String integrationObjectCode,
			String integrationObjectItemCode, String integrationObjectItemAttributeName);

	/**
	 * Find the attribute name if it exists for the given integrationObjectCode, integrationObjectItemCode, and
	 * integrationObjectItemAttributeName
	 * @param integrationObjectCode Integration object's code
	 * @param integrationObjectItemCode Integration object item's code
	 * @param integrationObjectItemAttributeName Integration object item attribute Name
	 * @return the name (qualifier) for the attribute as it is called in the Type System.
	 * @throws AttributeDescriptorNotFoundException if attribute cannot be found.
	 */
	String findItemAttributeName(String integrationObjectCode,
			String integrationObjectItemCode, String integrationObjectItemAttributeName);

	/**
	 * Find the type system type code referenced by the given integrationObjectCode and integrationObjectItemCode.
	 * @param integrationObjectCode Integration object's code
	 * @param integrationObjectItemCode Integration object item's code
	 * @return typeCode of the integrationObjectItem or empty String if not found.
	 */
	String findItemTypeCode(String integrationObjectCode, String integrationObjectItemCode);

	/**
	 * Find the integration object item's code referenced by the given integrationObjectCode and item type code.
	 * @param integrationObjectCode Integration object's code
	 * @param typeCode typeCode of the itemModel the IntegrationObjectItem refers to.
	 * @return the integrationObjectItem
	 */
	IntegrationObjectItemModel findIntegrationObjectItemByTypeCode(final String integrationObjectCode, final String typeCode);

	/**
	 * Find the integration object item's code referenced by the given integrationObjectCode and item type code also searching
	 * by the parent of the type code.
	 * @param integrationObjectCode Integration object's code
	 * @param typeCode typeCode of the itemModel the IntegrationObjectItem refers to.
	 * @return the integrationObjectItem
	 */
	IntegrationObjectItemModel findIntegrationObjectItemByParentTypeCode(String integrationObjectCode, String typeCode);

	/**
	 * Searches the integration object definitions for an item with the matching characteristics.
	 * @param integrationObjectCode code of the integration object that should contain the item.
	 * @param integrationObjectItemCode code for an item withing the the specified integration object.
	 * @return an {@code Optional} containing the matching integration object item or an {@code Optional.empty()}, if the specified
	 * integration object does not exist or it exists but does not contain an item with the specified item code.
	 */
	Optional<IntegrationObjectItemModel> findIntegrationObjectItem(String integrationObjectCode, String integrationObjectItemCode);
}
