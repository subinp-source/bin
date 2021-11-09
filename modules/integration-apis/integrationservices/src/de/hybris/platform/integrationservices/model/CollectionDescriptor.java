/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import java.util.Collection;

/**
 * Describes a collection attribute
 */
public interface CollectionDescriptor
{
	/**
	 * Gets a new collection instance that is described by the underlying attribute
	 * @return A new collection instance that is empty
	 */
	Collection<Object> newCollection();
}
