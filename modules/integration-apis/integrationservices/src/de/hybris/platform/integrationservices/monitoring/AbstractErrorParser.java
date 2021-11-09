/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integrationservices.monitoring;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.MonitoredRequestErrorModel;
import de.hybris.platform.integrationservices.util.HttpStatus;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * Base implementation of the {@link MonitoredRequestErrorParser}
 *
 * @param <T> type of the error item model extracted by this parser
 */
public abstract class AbstractErrorParser<T extends MonitoredRequestErrorModel> implements MonitoredRequestErrorParser<T>
{
	protected static final int ERROR_MSG_MAX_LENGTH = 255;
	private static final String UNKNOWN_ERROR_CODE = "unknown_error";
	private static final String UNKNOWN_ERROR_MSG = "Please check the log for details.";
	private static final Logger LOG = Log.getLogger(AbstractErrorParser.class);

	@Override
	public boolean isApplicable(final String contentType, final int statusCode)
	{
		return hasErrorStatusCode(statusCode) &&
				getSupportedMediaType().contains(contentType);
	}

	protected T error(final Class<T> errorClass, final String code, final String message)
	{
		Preconditions.checkArgument(errorClass != null, "Error cannot be null");
		final T error = createInstance(errorClass);
		error.setCode(StringUtils.isNotBlank(code) ? StringUtils.abbreviate(code, ERROR_MSG_MAX_LENGTH) : UNKNOWN_ERROR_CODE);
		error.setMessage(
				StringUtils.isNotBlank(message) ? StringUtils.abbreviate(message, ERROR_MSG_MAX_LENGTH) : UNKNOWN_ERROR_MSG);
		return error;
	}

	protected T createInstance(final Class<T> klazz)
	{
		try
		{
			return klazz.getDeclaredConstructor().newInstance();
		}
		catch (final IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e)
		{
			throw new MonitoredRequestException(e);
		}
	}

	protected T handleParserException(final Class<T> errorClass, final Exception e)
	{
		LOG.error("An exception occurred while parsing the error", e);
		return error(errorClass, UNKNOWN_ERROR_CODE, UNKNOWN_ERROR_MSG);
	}

	protected boolean hasErrorStatusCode(final int code)
	{
		return HttpStatus.valueOf(code).isError();
	}

	protected abstract Collection<String> getSupportedMediaType();
}
