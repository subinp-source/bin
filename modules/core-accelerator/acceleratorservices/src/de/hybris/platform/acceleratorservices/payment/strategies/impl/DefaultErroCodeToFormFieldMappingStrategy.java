/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.strategies.impl;

import de.hybris.platform.acceleratorservices.payment.strategies.ErroCodeToFormFieldMappingStrategy;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;


public class DefaultErroCodeToFormFieldMappingStrategy implements ErroCodeToFormFieldMappingStrategy
{
	private Map<Integer, List<String>> errorCodeToFieldMapping;

	@Override
	public List<String> getFieldForErrorCode(final Integer code)
	{
		return this.errorCodeToFieldMapping.get(code);
	}

	protected Map<Integer, List<String>> getErrorCodeToFieldMapping()
	{
		return errorCodeToFieldMapping;
	}

	@Required
	public void setErrorCodeToFieldMapping(final Map<Integer, List<String>> errorCodeToFieldMapping)
	{
		this.errorCodeToFieldMapping = errorCodeToFieldMapping;
	}

}
