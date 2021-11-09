/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data;

public class MetadataPrimitiveData
{
	private String value;
	private boolean quoted;

	public MetadataPrimitiveData(final String value, final boolean quoted)
	{
		this.value = value;
		this.quoted = quoted;
	}

	public String getValue()
	{
		return value;
	}

	public boolean isQuoted()
	{
		return quoted;
	}
}
