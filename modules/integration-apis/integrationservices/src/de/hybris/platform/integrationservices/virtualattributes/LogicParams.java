/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.virtualattributes;

import de.hybris.platform.core.model.ItemModel;

import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * Value object that stores the parameters for some logic to be executed.
 */
public class LogicParams
{
	private static final String MODEL_PARAM_FOR_LOGIC = "itemModel";
	
	private final ItemModel item;

	private LogicParams(final ItemModel item)
	{
		this.item = item;
	}

	/**
	 * Creates a {@link LogicParams}
	 *
	 * @return an instance of the {@link LogicParams}
	 */
	public static LogicParams create(final ItemModel item)
	{
		return new LogicParams(item);
	}

	/**
	 * Gets the {@link ItemModel}
	 *
	 * @return An instance of an ItemModel
	 */
	public ItemModel getItem()
	{
		return item;
	}

	/**
	 * Converts this parameters to a {@code Map}.
	 *
	 * @return a {@code Map} that has parameter names in its keys, and parameter values in its values. If there are no parameter
	 * specified, an empty map is returned.
	 */
	public @NotNull Map<String, Object> asMap()
	{
		return item != null
				? Map.of(MODEL_PARAM_FOR_LOGIC, item)
				: Map.of();
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
			final LogicParams that = (LogicParams) o;
			return Objects.equals(getItem(), that.getItem());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getItem());
	}

	@Override
	public String toString()
	{
		return "LogicParams{" +
				"item=" + item +
				'}';
	}
}
