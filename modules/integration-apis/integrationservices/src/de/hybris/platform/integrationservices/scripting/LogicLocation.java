/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.scripting;

import de.hybris.platform.integrationservices.util.Log;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

/**
 * Contains the information required to execute externally hosted logic.
 * The scheme of this logic and the location will be present.
 * This object is built from a URI.
 */
public class LogicLocation
{
	private static final Logger LOGGER = Log.getLogger(LogicLocation.class);
	private static final Pattern LOCATION_PATTERN = Pattern.compile("^(\\w+)://\\S+$");

	private final String location;
	private final LogicLocationScheme scheme;

	private LogicLocation(final LogicLocationScheme scheme, final String location)
	{
		this.scheme = scheme;
		this.location = location;
	}

	/**
	 * Creates a {@link LogicLocation} from the given URI (e.g. model://someModelScript)
	 * @param uri URI to create from
	 * @return LogicLocation
	 * @throws CannotCreateLogicLocationException when the provided URI can't be parsed to create a LogicLocation
	 */
	public static LogicLocation from(final String uri) throws CannotCreateLogicLocationException
	{
		final var matcher = LOCATION_PATTERN.matcher(String.valueOf(uri));
		if (matcher.matches())
		{
			try
			{
				final var scheme = LogicLocationScheme.from(matcher.group(1));
				return new LogicLocation(scheme, uri);
			}
			catch (final UnsupportedLogicLocationSchemeException e)
			{
				LOGGER.trace("Scheme is unsupported", e);
			}
		}
		throw new CannotCreateLogicLocationException(uri);
	}

	/**
	 * Tests whether the given URI is a valid logic location
	 *
	 * @param uri URI to test
	 * @return {@code true} if the URL is valid, else {@code false}
	 */
	public static boolean isValid(final String uri)
	{
		try
		{
			from(uri);
			return true;
		}
		catch (final CannotCreateLogicLocationException e)
		{
			LOGGER.trace("{} is invalid", uri, e);
			return false;
		}
	}

	public @NotNull LogicLocationScheme getScheme()
	{
		return scheme;
	}

	public @NotBlank String getLocation()
	{
		return location;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (this.getClass() == o.getClass())
		{
			final LogicLocation that = (LogicLocation) o;
			return Objects.equals(getLocation(), that.getLocation()) &&
					getScheme() == that.getScheme();
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getLocation(), getScheme());
	}

	@Override
	public String toString()
	{
		return "LogicLocation{" +
				"location='" + location + '\'' +
				", scheme=" + scheme +
				'}';
	}
}