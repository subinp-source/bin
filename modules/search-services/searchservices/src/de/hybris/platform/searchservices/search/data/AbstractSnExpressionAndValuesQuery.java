/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import java.util.List;


public abstract class AbstractSnExpressionAndValuesQuery extends AbstractSnExpressionQuery
{
	private List<Object> values;

	public List<Object> getValues()
	{
		return values;
	}

	public void setValues(final List<Object> values)
	{
		this.values = values;
	}
}
