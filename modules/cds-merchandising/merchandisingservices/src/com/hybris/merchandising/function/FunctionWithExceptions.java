/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.function;

import java.util.function.Function;

/**
 * Functional interface used to wrap lambdas to add support for Exception handling.
 *
 * @param <T> the argument being passed to the function.
 * @param <R> the result of the function.
 * @param <E> the Exception thrown by the function.
 */
@FunctionalInterface
public interface FunctionWithExceptions<T, R, E extends Exception>
{
	/**
	 * Calls the function.
	 * @param t the argument being passed to the function.
	 * @return the result of the function.
	 * @throws E in case of Exception.
	 */
	R apply(T t) throws E;

	/**
	 * Applies the function.
	 * @param function the function to apply.
	 * @return the result of the function.
	 * @throws E in case of Exception.
	 */
	static <T, R, E extends Exception> Function<T, R> rethrowFunction(final FunctionWithExceptions<T, R, E> function)
			throws E
	{
		return t ->
		{
			try
			{
				return function.apply(t);
			}
			catch (final Exception exception)
			{
				throwAsUnchecked(exception);
				return null;
			}
		};
	}

	/**
	 * Applies the function.
	 * @param function the function to apply.
	 * @param t the parameter of the function.
	 * @return the result of the function.
	 */
	static <T, R, E extends Exception> R uncheck(final FunctionWithExceptions<T, R, E> function, final T t)
	{
		try
		{
			return function.apply(t);
		}
		catch (final Exception exception)
		{
			throwAsUnchecked(exception);
			return null;
		}
	}

	/**
	 * Wrapper for Exception handling.
	 * @param exception the original Exception.
	 * @throws E , the Exception cast to the class we wish to return.
	 */
	@SuppressWarnings("unchecked")
	static <E extends Throwable> void throwAsUnchecked(final Exception exception) throws E
	{
		throw (E) exception;
	}
}
