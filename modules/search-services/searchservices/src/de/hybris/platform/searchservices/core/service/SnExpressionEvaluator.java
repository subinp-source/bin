/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;

import de.hybris.platform.searchservices.core.SnException;

import java.util.List;
import java.util.Locale;


/**
 * Expression evaluator interface to be used to evaluate simple expressions.
 */
public interface SnExpressionEvaluator
{
	/**
	 * Evaluates an expression.
	 *
	 * @param root
	 *           - the root object
	 * @param expression
	 *           - the expression
	 * @param <T>
	 *           the expected value type
	 *
	 * @return value of the evaluated expression
	 *
	 * @throws SnException
	 *            if an error occurs
	 */
	Object evaluate(final Object root, final String expression) throws SnException;

	/**
	 * Evaluates an expression.
	 *
	 * @param root
	 *           - the root object
	 * @param expression
	 *           - the expression
	 * @param <T>
	 *           the expected value type
	 *
	 * @return value of the evaluated expression
	 *
	 * @throws SnException
	 *            if an error occurs
	 */
	Object evaluate(final Object root, final String expression, List<Locale> locales) throws SnException;
}
