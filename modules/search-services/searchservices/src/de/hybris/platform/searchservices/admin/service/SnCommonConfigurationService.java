/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.searchservices.admin.data.SnExpressionInfo;

import java.util.List;


/**
 * Service for configuration related functionality.
 */
public interface SnCommonConfigurationService
{
	/**
	 * Returns the user for the given index type.
	 *
	 * @param indexTypeId
	 *           - the index type
	 *
	 * @return the user
	 */
	UserModel getUser(String indexTypeId);

	/**
	 * Returns the languages for the given index type.
	 *
	 * @param indexTypeId
	 *           - the index type
	 *
	 * @return the languages
	 */
	List<LanguageModel> getLanguages(String indexTypeId);

	/**
	 * Returns the currencies for the given index type.
	 *
	 * @param indexTypeId
	 *           - the index type
	 *
	 * @return the currencies
	 */
	List<CurrencyModel> getCurrencies(String indexTypeId);


	/**
	 * Returns the catalog versions for the given index type.
	 *
	 * @param indexTypeId
	 *           - the index type
	 *
	 * @return the catalog versions
	 */
	List<CatalogVersionModel> getCatalogVersions(String indexTypeId);

	/**
	 * Returns the valid facet expressions.
	 *
	 * @param indexTypeId
	 *           - the index type
	 *
	 * @return the facet expressions
	 */
	List<SnExpressionInfo> getFacetExpressions(String indexTypeId);

	/**
	 * Checks if a given expression is a valid facet expression.
	 *
	 * @param indexTypeId
	 *           - the index type
	 * @param expression
	 *           - the expression
	 *
	 * @return <code>true</code> if it is a valid facet expression, <code>false</code> otherwise
	 */
	boolean isValidFacetExpression(String indexTypeId, String expression);

	/**
	 * Returns the valid sort expressions.
	 *
	 * @param indexTypeId
	 *           - the index type
	 *
	 * @return the sort expressions
	 */
	List<SnExpressionInfo> getSortExpressions(String indexTypeId);

	/**
	 * Checks if a given expression is a valid sort expression.
	 *
	 * @param indexTypeId
	 *           - the index type
	 * @param expression
	 *           - the expression
	 *
	 * @return <code>true</code> if it is a valid sort expression, <code>false</code> otherwise
	 */
	boolean isValidSortExpression(String indexTypeId, String expression);

	/**
	 * Returns the valid group expressions.
	 *
	 * @param indexTypeId
	 *           - the index type
	 *
	 * @return the group expressions
	 */
	List<SnExpressionInfo> getGroupExpressions(String indexTypeId);

	/**
	 * Checks if a given expression is a valid group expression.
	 *
	 * @param indexTypeId
	 *           - the index type
	 * @param expression
	 *           - the expression
	 *
	 * @return <code>true</code> if it is a valid group expression, <code>false</code> otherwise
	 */
	boolean isValidGroupExpression(String indexTypeId, String expression);
}
