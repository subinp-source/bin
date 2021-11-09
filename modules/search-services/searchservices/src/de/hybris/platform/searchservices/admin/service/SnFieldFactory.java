/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service;

import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnIndexType;

import java.util.List;


/**
 * Factory for default fields, it collects the fields provided by all the registered implementations of
 * {@link SnFieldProvider}.
 */
public interface SnFieldFactory
{
	/**
	 * Returns default fields to be added to an index type.
	 *
	 * @param indexType
	 *           - the index type
	 *
	 * @return the default fields
	 */
	List<SnField> getDefaultFields(SnIndexType indexType);
}
