/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service;

import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnIndexType;

import java.util.List;


/**
 * Provider for default fields.
 */
public interface SnFieldProvider
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
