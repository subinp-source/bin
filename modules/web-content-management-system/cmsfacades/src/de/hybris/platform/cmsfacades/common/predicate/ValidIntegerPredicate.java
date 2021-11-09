/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import java.util.Objects;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;



/**
 * Predicate to test if a given string is parsable into an Integer and that it matches lower and upper boundaries when
 * set
 */
public class ValidIntegerPredicate implements Predicate<String>
{
	private Integer min;
	private Integer max;

	@Override
	public boolean test(final String target)
	{
		boolean result = false;

		if (StringUtils.isNotBlank(target))
		{
			try
			{
				final int num = Integer.parseInt(target);

				if (isGreaterOrEqualToMin(num) || isLessOrEqualToMax(num) || areMinAndMaxNull())
				{
					result = true;
				}
			}
			catch (final NumberFormatException e)
			{
				result = false;
			}
		}

		return result;
	}

	/**
	 * If min is not null, determine if num is greater or equal to min
	 */
	protected boolean isGreaterOrEqualToMin(final int num)
	{
		return Objects.nonNull(getMin()) && num >= getMin();
	}

	/**
	 * If max is not null, determine if num is less or equal to max
	 */
	protected boolean isLessOrEqualToMax(final int num)
	{
		return Objects.nonNull(getMax()) && num <= getMax();
	}

	/**
	 * Determine if both min and max are null
	 */
	protected boolean areMinAndMaxNull()
	{
		return Objects.isNull(getMin()) && Objects.isNull(getMax());
	}

	protected Integer getMin()
	{
		return min;
	}

	public void setMin(final Integer min)
	{
		this.min = min;
	}

	protected Integer getMax()
	{
		return max;
	}

	public void setMax(final Integer max)
	{
		this.max = max;
	}
}
