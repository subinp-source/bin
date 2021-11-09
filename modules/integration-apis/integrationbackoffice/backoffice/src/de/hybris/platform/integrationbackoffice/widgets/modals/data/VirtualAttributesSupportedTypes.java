/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data;

public enum VirtualAttributesSupportedTypes
{
	INTEGER("java.lang.Integer"),
	BOOLEAN("java.lang.Boolean"),
	BYTE("java.lang.Byte"),
	DOUBLE("java.lang.Double"),
	FLOAT("java.lang.Float"),
	LONG("java.lang.Long"),
	SHORT("java.lang.Short"),
	STRING("java.lang.String"),
	CHARACTER("java.lang.Character"),
	DATE("java.util.Date"),
	BIG_INTEGER("java.math.BigInteger"),
	BIG_DECIMAL("java.math.BigDecimal");

	private final String label;

	VirtualAttributesSupportedTypes(final String label)
	{
		this.label = label;
	}

	public String getLabel(){
		return label;
	}
}
