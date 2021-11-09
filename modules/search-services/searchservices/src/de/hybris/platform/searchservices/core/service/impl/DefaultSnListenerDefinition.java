/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;


/**
 * Class that allows to register global listeners.
 */
public class DefaultSnListenerDefinition implements Comparable<DefaultSnListenerDefinition>
{
	private int priority;
	private Object listener;

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
	 * Returns the listener.
	 *
	 * @return the listener
	 */
	public Object getListener()
	{
		return listener;
	}

	/**
	 * Sets the listener.
	 *
	 * @param listener
	 *           - the listener
	 */
	public void setListener(final Object listener)
	{
		this.listener = listener;
	}

	@Override
	public int compareTo(final DefaultSnListenerDefinition other)
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

		final DefaultSnListenerDefinition that = (DefaultSnListenerDefinition) obj;
		return new EqualsBuilder().append(this.listener, that.listener).append(this.priority, that.priority).isEquals();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.listener, this.priority);
	}
}
