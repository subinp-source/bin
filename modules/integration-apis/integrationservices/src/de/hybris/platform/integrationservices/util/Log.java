/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class Log implements Logger
{
	private final Logger logger;

	private Log(final Class<?> type)
	{
		logger = LoggerFactory.getLogger(type);
	}

	@Override
	public String getName()
	{
		return logger.getName();
	}

	@Override
	public boolean isTraceEnabled()
	{
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(final String s)
	{
		logger.trace(s);
	}

	@Override
	public void trace(final String s, final Object o)
	{
		logger.trace(s, o);
	}

	@Override
	public void trace(final String s, final Object o, final Object o1)
	{
		logger.trace(s, o, o1);
	}

	@Override
	public void trace(final String s, final Object... objects)
	{
		logger.trace(s, objects);
	}

	@Override
	public void trace(final String s, final Throwable throwable)
	{
		logger.trace(s, throwable);
	}

	@Override
	public boolean isTraceEnabled(final Marker marker)
	{
		return logger.isTraceEnabled(marker);
	}

	@Override
	public void trace(final Marker marker, final String s)
	{
		logger.trace(marker, s);
	}

	@Override
	public void trace(final Marker marker, final String s, final Object o)
	{
		logger.trace(marker, s, o);
	}

	@Override
	public void trace(final Marker marker, final String s, final Object o, final Object o1)
	{
		logger.trace(marker, s, o, o1);
	}

	@Override
	public void trace(final Marker marker, final String s, final Object... objects)
	{
		logger.trace(marker, s, objects);
	}

	@Override
	public void trace(final Marker marker, final String s, final Throwable throwable)
	{
		logger.trace(marker, s, throwable);
	}

	@Override
	public boolean isDebugEnabled()
	{
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(final String s)
	{
	   logger.debug(s);
	}

	@Override
	public void debug(final String s, final Object o)
	{
		logger.debug(s, o);
	}

	@Override
	public void debug(final String s, final Object o, final Object o1)
	{
		logger.debug(s, o, o1);
	}

	@Override
	public void debug(final String s, final Object... objects)
	{
		logger.debug(s, objects);
	}

	@Override
	public void debug(final String s, final Throwable throwable)
	{
		logger.debug(s, throwable);
	}

	@Override
	public boolean isDebugEnabled(final Marker marker)
	{
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(final Marker marker, final String s)
	{
		logger.debug(marker, s);
	}

	@Override
	public void debug(final Marker marker, final String s, final Object o)
	{
		logger.debug(marker, s, o);
	}

	@Override
	public void debug(final Marker marker, final String s, final Object o, final Object o1)
	{
		logger.debug(marker, s, o, o1);
	}

	@Override
	public void debug(final Marker marker, final String s, final Object... objects)
	{
		logger.debug(marker, s, objects);
	}

	@Override
	public void debug(final Marker marker, final String s, final Throwable throwable)
	{
		logger.debug(marker, s, throwable);
	}

	@Override
	public boolean isInfoEnabled()
	{
		return logger.isInfoEnabled();
	}

	@Override
	public void info(final String s)
	{
		logger.info(s);
	}

	@Override
	public void info(final String s, final Object o)
	{
		logger.info(s, o);
	}

	@Override
	public void info(final String s, final Object o, final Object o1)
	{
		logger.info(s, o, o1);
	}

	@Override
	public void info(final String s, final Object... objects)
	{
		logger.info(s, objects);
	}

	@Override
	public void info(final String s, final Throwable throwable)
	{
		logger.info(s, throwable);
	}

	@Override
	public boolean isInfoEnabled(final Marker marker)
	{
		return logger.isInfoEnabled(marker);
	}

	@Override
	public void info(final Marker marker, final String s)
	{
		logger.info(marker, s);
	}

	@Override
	public void info(final Marker marker, final String s, final Object o)
	{
		logger.info(marker, s, o);
	}

	@Override
	public void info(final Marker marker, final String s, final Object o, final Object o1)
	{
		logger.info(marker, s, o, o1);
	}

	@Override
	public void info(final Marker marker, final String s, final Object... objects)
	{
		logger.info(marker, s, objects);
	}

	@Override
	public void info(final Marker marker, final String s, final Throwable throwable)
	{
		logger.info(marker, s, throwable);
	}

	@Override
	public boolean isWarnEnabled()
	{
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(final String s)
	{
		logger.warn(s);
	}

	@Override
	public void warn(final String s, final Object o)
	{
		logger.warn(s, o);
	}

	@Override
	public void warn(final String s, final Object... objects)
	{
		logger.warn(s, objects);
	}

	@Override
	public void warn(final String s, final Object o, final Object o1)
	{
		logger.warn(s, o, o1);
	}

	@Override
	public void warn(final String s, final Throwable throwable)
	{
		logger.warn(s, throwable.getMessage());
		logger.debug("",throwable);
	}

	@Override
	public boolean isWarnEnabled(final Marker marker)
	{
		return logger.isWarnEnabled(marker);
	}

	@Override
	public void warn(final Marker marker, final String s)
	{
		logger.warn(marker, s);
	}

	@Override
	public void warn(final Marker marker, final String s, final Object o)
	{
		logger.warn(marker, s, o);
	}

	@Override
	public void warn(final Marker marker, final String s, final Object o, final Object o1)
	{
		logger.warn(marker, s, o, o1);
	}

	@Override
	public void warn(final Marker marker, final String s, final Object... objects)
	{
		logger.warn(marker, s, objects);
	}

	@Override
	public void warn(final Marker marker, final String s, final Throwable throwable)
	{
		logger.warn(marker, s, throwable);
	}

	@Override
	public boolean isErrorEnabled()
	{
		return logger.isErrorEnabled();
	}

	@Override
	public void error(final String s)
	{
		logger.error(s);
	}

	@Override
	public void error(final String s, final Object o)
	{
		logger.error(s, o);
	}

	@Override
	public void error(final String s, final Object o, final Object o1)
	{
		logger.error(s, o, o1);
	}

	@Override
	public void error(final String s, final Object... objects)
	{
		logger.error(s, objects);
	}

	@Override
	public void error(final String s, final Throwable throwable)
	{
		logger.error(s, throwable.getMessage());
		logger.debug("", throwable);
	}

	@Override
	public boolean isErrorEnabled(final Marker marker)
	{
		return logger.isErrorEnabled(marker);
	}

	@Override
	public void error(final Marker marker, final String s)
	{
		logger.error(marker, s);
	}

	@Override
	public void error(final Marker marker, final String s, final Object o)
	{
		logger.error(marker, s, o);
	}

	@Override
	public void error(final Marker marker, final String s, final Object o, final Object o1)
	{
		logger.error(marker, s, o, o1);
	}

	@Override
	public void error(final Marker marker, final String s, final Object... objects)
	{
		logger.error(marker, s, objects);
	}

	@Override
	public void error(final Marker marker, final String s, final Throwable throwable)
	{
		logger.error(marker, s, throwable.getMessage());
		logger.debug(marker, "", throwable);
	}

	public static Log getLogger(final Class<?> type)
	{
		return new Log(type);
	}
}
