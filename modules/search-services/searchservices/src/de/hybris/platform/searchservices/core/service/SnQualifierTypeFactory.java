/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;


import de.hybris.platform.searchservices.admin.data.SnField;

import java.util.List;
import java.util.Optional;


/**
 * Factory for qualifier types.
 */
public interface SnQualifierTypeFactory
{
	/**
	 * Returns all qualifier type ids.
	 *
	 * @return all qualifier type ids
	 */
	List<String> getAllQualifierTypeIds();

	/**
	 * Returns all qualifier types.
	 *
	 * @return all qualifier types
	 */
	List<SnQualifierType> getAllQualifierTypes();

	/**
	 * Returns the qualifier type for a given context and field.
	 *
	 * @param context
	 *           - the context
	 * @param field
	 *           - the field
	 *
	 * @return the qualifier type
	 */
	Optional<SnQualifierType> getQualifierType(SnContext context, SnField field);
}
