/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.errors;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Indicates a problem with the request when the payload contain an attribute that is not defined in the integration object.
 */
public class AttributeNotInIntegrationObjectException extends RuntimeException
{
	private static final String ERROR_CODE = "invalid_property";
	private final transient List<Object> content;

	public AttributeNotInIntegrationObjectException(final List<Object> content)
	{
		super("Entity contains a property that was not defined in the integration object:" + content);
		this.content = content == null || content.isEmpty()
			? Collections.emptyList()
			: content;
	}

	public String getErrorCode()
	{
		return ERROR_CODE;
	}

	public @NotNull List<Object> getContent()
	{
		return content;
	}
}
