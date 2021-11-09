/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.virtualattributes;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.scripting.LogicLocation;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * Value object that provides the context to a {@link LogicExecutor} about the script to execute and the attribute, for which
 * the script is executed.
 */
public class LogicExecutorContext
{
	private final LogicLocation logicLocation;
	private final TypeAttributeDescriptor descriptor;

	private LogicExecutorContext(final LogicLocation logicLocation, final TypeAttributeDescriptor descriptor)
	{
		this.logicLocation = logicLocation;
		this.descriptor = descriptor;
	}

	public static LogicExecutorContext create(@NotNull final LogicLocation logicLocation,
	                                          @NotNull final TypeAttributeDescriptor descriptor)
	{
		Preconditions.checkArgument(logicLocation != null, "Logic location must be provided");
		Preconditions.checkArgument(descriptor != null, "Attribute descriptor must be provided");
		return new LogicExecutorContext(logicLocation, descriptor);
	}

	public LogicLocation getLogicLocation()
	{
		return logicLocation;
	}

	public TypeAttributeDescriptor getDescriptor()
	{
		return descriptor;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (this.getClass() == o.getClass())
		{
			final LogicExecutorContext that = (LogicExecutorContext) o;
			return Objects.equals(getLogicLocation(), that.getLogicLocation()) &&
					Objects.equals(getDescriptor(), that.getDescriptor());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getLogicLocation(), getDescriptor());
	}

	@Override
	public String toString()
	{
		return "LogicExecutorContext{" +
				"logicLocation=" + logicLocation +
				", descriptor=" + descriptor +
				'}';
	}
}
