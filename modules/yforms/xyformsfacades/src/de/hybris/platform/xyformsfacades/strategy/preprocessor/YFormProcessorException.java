/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsfacades.strategy.preprocessor;

/**
 * Exception that is thrown during the execution of a {@link YFormPreprocessorStrategy}
 */
public class YFormProcessorException extends Exception
{
	public YFormProcessorException()
	{
		super();
	}

	public YFormProcessorException(final String msg)
	{
		super(msg);
	}

	public YFormProcessorException(final Throwable t)
	{
		super(t);
	}

	public YFormProcessorException(final String msg, final Throwable t)
	{
		super(msg, t);
	}
}
