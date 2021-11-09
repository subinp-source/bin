/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.utility;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.services.ReadService;


import java.util.Set;
import java.util.stream.Collectors;
/**
 * Editor Filtering Service is a bean used to filter attribute models.
 */
public class DefaultEditorAttributesFilteringService implements EditorAttributesFilteringService
{
	private final ReadService readService;

	/**
	 * Constructor
	 *
	 * @param readService readService bean passed in.
	 * @return Instantiated bean, using readService
	 */
	public DefaultEditorAttributesFilteringService(final ReadService readService)
	{
		this.readService = readService;
	}

	/**
	 * Filters a set of attribute descriptors for Composed and Enumeration types that are not on the blacklist
	 *
	 * @param parentType  the parent ComposedTypeModel to get the attribute descriptors to filter
	 * @return a filtered set of AttributeDescriptorModel
	 */
	@Override
	public Set<AttributeDescriptorModel> filterAttributesForTree(final ComposedTypeModel parentType)
	{
		return readService.getAttributesForType(parentType).stream()
		                  .filter(attributeDescriptor -> !EditorBlacklists.getAttributeBlackList()
		                                                                  .contains(attributeDescriptor.getQualifier()))
		                  .filter(attributeDescriptor -> readService.isComplexType(attributeDescriptor.getAttributeType()))
		                  .collect(Collectors.toSet());
	}

	/**
	 * Filters a set of attribute descriptors for types that are not on the blacklist
	 *
	 * @param parentType  the parent ComposedTypeModel to get the attribute descriptors to filter
	 * @return a filtered and sorted set of AttributeDescriptorModel
	 */
	@Override
	public Set<AttributeDescriptorModel> filterAttributesForAttributesMap(final ComposedTypeModel parentType)
	{
		final Set<AttributeDescriptorModel> attributes = readService
				.getAttributesForType(parentType)
				.stream()
				.filter(attribute -> {
					final String itemType = attribute
							.getAttributeType()
							.getItemtype();
					return isValidType(readService, attribute, itemType);
				})
				.filter(attribute -> !EditorBlacklists.getTypesBlackList()
				                                      .contains(attribute.getItemtype()))
				.filter(attribute -> !EditorBlacklists.getAttributeBlackList()
				                                      .contains(attribute.getQualifier()))
				.collect(Collectors.toSet());

		attributes.addAll(readService.getReadOnlyAttributesAsAttributeDescriptorModels(parentType));

		return attributes;

	}

	private boolean isValidType(final ReadService readService, final AttributeDescriptorModel attribute,
	                                  final String itemType)
	{
		return readService.isComplexType(attribute.getAttributeType()) ||
				readService.isAtomicType(itemType) ||
				readService.isMapType(itemType) ||
				readService.isCollectionType(itemType);
	}


}
