/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

/**
 * Describes logic to determine whether an attribute is settable. Also contains an isApplicable call to determine
 * whether the criteria should be considered in the first place.
 */
public interface SettableAttributeHandler
{
	/**
	 * Determines if the handler's logic should be considered
	 *
	 * @param item Item model to be evaluated
	 * @return If the given handler should run
	 */
	boolean isApplicable(final ItemModel item);

	/**
	 * Determines if the given attribute descriptor is settable.
	 *
	 * @param descriptor Attribute descriptor to be evaluated
	 * @return Whether or not the attribute descriptor is settable
	 */
	boolean isSettable(final AttributeDescriptorModel descriptor);
}
