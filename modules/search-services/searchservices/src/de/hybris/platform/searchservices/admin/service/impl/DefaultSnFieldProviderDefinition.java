/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service.impl;

import de.hybris.platform.searchservices.admin.service.SnFieldProvider;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;


/**
 * Class that allows to register field providers.
 */
public class DefaultSnFieldProviderDefinition implements Comparable<DefaultSnFieldProviderDefinition>
{
	private int priority;
	private SnFieldProvider fieldProvider;

	/**
	 * Returns the priority.
	 *
	 * @return the priority
	 */
	public int getPriority()
	{
		return priority;
	}

	/**
	 * Sets the priority.
	 *
	 * @param priority
	 *           - the priority
	 */
	public void setPriority(final int priority)
	{
		this.priority = priority;
	}

	/**
	 * Returns the field provider.
	 *
	 * @return the field provider
	 */
	public SnFieldProvider getFieldProvider()
	{
		return fieldProvider;
	}

	/**
	 * Sets the fieldProvider.
	 *
	 * @param fieldProvider
	 *           - the field provider
	 */
	public void setFieldProvider(final SnFieldProvider fieldProvider)
	{
		this.fieldProvider = fieldProvider;
	}

	@Override
	public int compareTo(final DefaultSnFieldProviderDefinition other)
	{
		return Integer.compare(other.getPriority(), this.getPriority());
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}

		if (obj == null || this.getClass() != obj.getClass())
		{
			return false;
		}

		final DefaultSnFieldProviderDefinition that = (DefaultSnFieldProviderDefinition) obj;
		return new EqualsBuilder().append(this.fieldProvider, that.fieldProvider).append(this.priority, that.priority).isEquals();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.fieldProvider, this.priority);
	}
}
