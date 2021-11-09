/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Default implementation for {@link SnIndexerFieldWrapper}.
 */
public class DefaultSnIndexerFieldWrapper implements SnIndexerFieldWrapper
{
	private SnField field;
	private String valueProviderId;
	private Map<String, String> valueProviderParameters;
	private List<SnQualifier> qualifiers;

	@Override
	public String getFieldId()
	{
		return field.getId();
	}

	@Override
	public SnField getField()
	{
		return field;
	}

	public void setField(final SnField field)
	{
		this.field = field;
	}

	@Override
	public String getValueProviderId()
	{
		return valueProviderId;
	}

	public void setValueProviderId(final String valueProviderId)
	{
		this.valueProviderId = valueProviderId;
	}

	@Override
	public Map<String, String> getValueProviderParameters()
	{
		return valueProviderParameters;
	}

	public void setValueProviderParameters(final Map<String, String> valueProviderParameters)
	{
		this.valueProviderParameters = valueProviderParameters;
	}

	@Override
	public boolean isLocalized()
	{
		return BooleanUtils.isTrue(field.getLocalized());
	}

	@Override
	public boolean isQualified()
	{
		return StringUtils.isNotBlank(field.getQualifierTypeId());
	}

	@Override
	public boolean isMultiValued()
	{
		return BooleanUtils.isTrue(field.getMultiValued());
	}

	@Override
	public List<SnQualifier> getQualifiers()
	{
		return qualifiers;
	}

	public void setQualifiers(final List<SnQualifier> qualifiers)
	{
		this.qualifiers = qualifiers;
	}
}
