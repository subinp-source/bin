/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.core.service.SnIdentityProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnIndexType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnIdentityProvider}.
 */
public class DefaultSnIdentityProvider implements SnIdentityProvider<ItemModel>
{
	public static final String EXPRESSION_PARAM = "expression";
	public static final String EXPRESSION_PARAM_DEFAULT_VALUE = null;

	private SnExpressionEvaluator snExpressionEvaluator;

	@Override
	public String getIdentifier(final SnContext context, final ItemModel item) throws SnException
	{
		final SnIndexType type = context.getIndexType();
		final String expression = ParameterUtils.getString(type.getIdentityProviderParameters(), EXPRESSION_PARAM,
				EXPRESSION_PARAM_DEFAULT_VALUE);

		if (StringUtils.isNotBlank(expression))
		{
			final Object value = snExpressionEvaluator.evaluate(item, expression);
			if (value == null)
			{
				return null;
			}

			return String.valueOf(value);
		}
		else
		{
			return item.getPk().getLongValueAsString();
		}
	}

	protected String resolveExpression(final SnField field)
	{
		final String expression = ParameterUtils.getString(field.getValueProviderParameters(), EXPRESSION_PARAM,
				EXPRESSION_PARAM_DEFAULT_VALUE);

		if (StringUtils.isNotBlank(expression))
		{
			return expression;
		}

		return field.getId();
	}

	public SnExpressionEvaluator getSnExpressionEvaluator()
	{
		return snExpressionEvaluator;
	}

	@Required
	public void setSnExpressionEvaluator(final SnExpressionEvaluator snExpressionEvaluator)
	{
		this.snExpressionEvaluator = snExpressionEvaluator;
	}
}
