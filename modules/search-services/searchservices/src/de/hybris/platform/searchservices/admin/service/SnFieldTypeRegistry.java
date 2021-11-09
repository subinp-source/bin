/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service;

import de.hybris.platform.searchservices.admin.data.SnFieldTypeInfo;
import de.hybris.platform.searchservices.enums.SnFieldType;


/**
 * Registry for field types.
 */
public interface SnFieldTypeRegistry
{
	/**
	 * Returns field type information.
	 *
	 * @param fieldType
	 *           - the field type
	 *
	 * @return field type information
	 */
	SnFieldTypeInfo getFieldTypeInfo(SnFieldType fieldType);
}
