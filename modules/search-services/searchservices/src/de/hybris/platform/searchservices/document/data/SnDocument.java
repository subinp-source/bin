/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.document.data;

import de.hybris.platform.searchservices.admin.data.SnField;

import java.util.HashMap;
import java.util.Map;


public class SnDocument
{
	private String id;

	private final Map<String, Object> fields = new HashMap<>();

	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public Map<String, Object> getFields()
	{
		return fields;
	}

	public void setFieldValue(final SnField field, final Object value)
	{
		fields.put(field.getId(), value);
	}
}
