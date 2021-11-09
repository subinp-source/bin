/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceorgaddon.forms;

import javax.validation.constraints.NotNull;


/**
 * Pojo for 'B2b permission type selection' form.
 */
public class B2BPermissionTypeSelectionForm
{
	private String b2BPermissionType;

	@NotNull(message = "{general.required}")
	public String getB2BPermissionType()
	{
		return b2BPermissionType;
	}

	public void setB2BPermissionType(final String b2bPermissionType)
	{
		b2BPermissionType = b2bPermissionType;
	}
}
