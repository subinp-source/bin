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
package de.hybris.platform.integrationservices.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

/**
 * @deprecated use {@link AbstractItemToMapPopulator} instead
 * Abstract class to populate given item model to a Map which is representation of the Integration Object.
 * V - type of the value handled by this populator
 */
@Deprecated(since="20.11.0-RC5")
public abstract class AbstractItem2MapPopulator<V> implements Populator<ItemToMapConversionContext, Map<String, Object>>
{
	private ModelService modelService;

	@Override
	public void populate(final ItemToMapConversionContext source, final Map<String, Object> target)
	{
		source.getTypeDescriptor().getAttributes().stream()
				.filter(this::isApplicable)
				.forEach(desc -> populateToMap(desc, source, target));
	}

	/**
	 * Implements the logic to populate the target
	 * @param attr descriptor of the attribute to be populated
	 * @param source Source used in the implementation to populate the target
	 * @param target Populate the target with the result
	 */
	protected abstract void populateToMap(TypeAttributeDescriptor attr, ItemToMapConversionContext source, Map<String, Object> target);

	/**
	 * Indicates whether this Populator is applicable to process the attribute
	 *
	 * @param attributeDescriptor descriptor of the attribute to make the decision about
	 * @return {@code true}, if the Populator is applicable; otherwise {@code false}
	 */
	protected abstract boolean isApplicable(final TypeAttributeDescriptor attributeDescriptor);

	/**
	 * Gets a reference to the {@link ModelService}
	 *
	 * @return The ModelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
