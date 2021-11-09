/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.dao;


import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;


/**
 * A data access object around {@link B2BBudgetModel}
 * 
 */
public interface B2BBudgetDao extends GenericDao<B2BBudgetModel>
{

	/**
	 * @param code
	 *           the code
	 * @return budget matching the code
	 */
	B2BBudgetModel findBudgetByCode(String code);
}
