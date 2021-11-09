/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.dao.impl;

import de.hybris.platform.b2b.dao.B2BBudgetDao;
import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.Collections;
import java.util.List;


/**
 * A data access object around {@link B2BBudgetModel}
 *
 * @spring.bean b2bBudgetDao
 */
public class DefaultB2BBudgetDao extends DefaultGenericDao<B2BBudgetModel> implements B2BBudgetDao
{

	public DefaultB2BBudgetDao()
	{
		super(B2BBudgetModel._TYPECODE);
	}

	@Override
	public B2BBudgetModel findBudgetByCode(final String code)
	{
		final List<B2BBudgetModel> budgets = this.find(Collections.singletonMap(B2BBudgetModel.CODE, code));
		return (budgets.iterator().hasNext() ? budgets.iterator().next() : null);
	}
}
