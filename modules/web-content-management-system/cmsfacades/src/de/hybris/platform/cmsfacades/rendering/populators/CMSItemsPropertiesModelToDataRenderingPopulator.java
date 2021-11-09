/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import de.hybris.platform.cms2.common.service.CollectionHelper;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplier;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplierProvider;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator used to add "properties" attribute to the result data object.
 */
public class CMSItemsPropertiesModelToDataRenderingPopulator implements Populator<CMSItemModel, Map<String, Object>>
{
	public static final String PROPERTIES_ATTRIBUTE = "properties";

	private CMSItemPropertiesSupplierProvider cmsItemPropertiesSupplierProvider;

	private CollectionHelper collectionHelper;

	@Override
	public void populate(final CMSItemModel itemModel, final Map<String, Object> resultData) throws ConversionException
	{
		final List<CMSItemPropertiesSupplier> suppliers = getCmsItemPropertiesSupplierProvider().getSuppliers(itemModel);

		final List<Map<String, Map<String, Object>>> properties = getProperties(suppliers, itemModel);

		final Map<String, List<Map<String, Object>>> groupedProperties = groupProperties(properties);

		final Map<String, Object> result = mergeGroupedProperties(groupedProperties);

		if (!result.entrySet().isEmpty())
		{
			resultData.put(PROPERTIES_ATTRIBUTE, result);
		}
	}

	/**
	 * Groups the list of properties  by group name.
	 * @param properties the list of properties in the format List<Map<groupName, Map<propertyKey, propertyValue>>>
	 * @return the grouped list of properties in the format: Map<groupName, List<Map<propertyKey, propertyValue>>>
	 */
	protected Map<String, List<Map<String, Object>>> groupProperties(final List<Map<String, Map<String, Object>>> properties)
	{
		return properties.stream() //
				.flatMap(p -> p.entrySet().stream()) //
				.collect(groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
	}

	/**
	 * Returns the list of properties provided by suppliers.
	 *
	 * @param suppliers the list of {@link CMSItemPropertiesSupplier}.
	 * @param itemModel the {@link CMSItemModel} for which the properties are returned.
	 * @return the list of properties in the format List<Map<groupName, Map<propertyKey, propertyValue>>>
	 */
	protected List<Map<String, Map<String, Object>>> getProperties(final List<CMSItemPropertiesSupplier> suppliers,
			final CMSItemModel itemModel)
	{
		return suppliers.stream() //
				.filter(supplier -> supplier.isEnabled(itemModel)) //
				.map(supplier -> getGroupProperties(supplier, itemModel)) //
				.collect(Collectors.toList());
	}

	/**
	 * Merges grouped properties using {@link CollectionHelper#mergeMaps(List)}.
	 * @param groupedProperties the grouped properties to merge
	 * @return the resulted Map<groupName, groupedProperties>
	 */
	protected Map<String, Object> mergeGroupedProperties(final Map<String, List<Map<String, Object>>> groupedProperties)
	{
		return groupedProperties.entrySet().stream() //
			.collect(toMap( //
					Map.Entry::getKey, //
					e -> getCollectionHelper().mergeMaps(e.getValue()) //
			));
	}

	/**
	 * Returns a {@link Map} of group name as a key and a {@link} Map of properties as a value.
	 * The group name represents a standalone group of properties. For example, the group of
	 * properties for SmartEdit application will have a group name "smartedit".
	 *
	 * @param supplier
	 * 		the {@link CMSItemPropertiesSupplier} that returns a group name and properties.
	 * @param itemModel
	 * 		the {@link CMSItemModel} to get the properties for.
	 * @return a {@link Map} of group name as a key and a {@link Map} of properties as a value.
	 */
	protected Map<String, Map<String, Object>> getGroupProperties(final CMSItemPropertiesSupplier supplier,
			final CMSItemModel itemModel)
	{
		final Map<String, Object> properties = supplier.getProperties(itemModel);
		final String groupName = supplier.groupName();
		final Map<String, Map<String, Object>> result = new HashMap<>();
		result.put(groupName, properties);
		return result;
	}

	protected CollectionHelper getCollectionHelper()
	{
		return collectionHelper;
	}

	@Required
	public void setCollectionHelper(final CollectionHelper collectionHelper)
	{
		this.collectionHelper = collectionHelper;
	}

	protected CMSItemPropertiesSupplierProvider getCmsItemPropertiesSupplierProvider()
	{
		return cmsItemPropertiesSupplierProvider;
	}

	@Required
	public void setCmsItemPropertiesSupplierProvider(
			final CMSItemPropertiesSupplierProvider cmsItemPropertiesSupplierProvider)
	{
		this.cmsItemPropertiesSupplierProvider = cmsItemPropertiesSupplierProvider;
	}
}
