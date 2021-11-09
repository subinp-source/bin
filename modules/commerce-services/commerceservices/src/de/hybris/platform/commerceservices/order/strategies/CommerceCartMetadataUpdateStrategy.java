/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies;

import de.hybris.platform.commerceservices.service.data.CommerceCartMetadataParameter;

import java.util.Optional;


/**
 * Strategy to update cart metadata fields (i.e. name)
 */
public interface CommerceCartMetadataUpdateStrategy
{
	/**
	 * Updates cart metadata fields (i.e. name). Most of the attributes from the input bean are of {@link Optional} type
	 * and they will only be used when a value is present for them. An empty string for an {@link Optional<String>} will
	 * trigger the field to be stored as null.
	 *
	 * @param parameter
	 *           a bean holding any number of additional attributes a client may want to pass to the method
	 */
	void updateCartMetadata(CommerceCartMetadataParameter parameter);
}
