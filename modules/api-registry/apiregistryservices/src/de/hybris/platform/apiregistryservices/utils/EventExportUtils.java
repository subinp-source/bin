/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.Config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Helper class.
 */
public class EventExportUtils
{
	private static final String NOT_EMPTY_REGEXP = ".+";

	public static final String DELIMITER_PROP = "apiregistryservices.eventPropertyConfiguration.delimiter";
	public static final String EXPORTING_PROP = "apiregistryservices.events.exporting";
	public static final String EXPORTING_OVERRIDDEN_PROP = "apiregistryservices.events.exporting.overridden";
	public static final String BLACKLIST_PROP = "apiregistryservices.events.blacklist";
	private static final String ALLOWED_URL_PROTOCOLS = "apiregistryservices.allowedUrlProtocols";
	private static final Pattern PROPERTY_PLACEHOLDER_PATTERN = Pattern.compile("\\{(.*?)\\}");
	private static final Logger LOG = LoggerFactory.getLogger(EventExportUtils.class);
	private static final int REFERENCE_SPLIT_LIMIT = 2;

	private EventExportUtils()
	{
	}

	/**
	 * Reads apiregistryservices.eventPropertyConfiguration.delimiter property
	 * @return delimiter, default value is .
	 */
	public static final String getDelimiter()
	{
		return Config.getString(DELIMITER_PROP, "\\.");
	}

	/**
	 * reads the kymaintegrationservices.events.exporting property false by default
	 */
	public static boolean isEventExportActive()
	{
		return Config.getBoolean(EXPORTING_PROP, false);
	}

	/**
	 * reads kymaintegrationservices.events.blacklist property return empty array if not defined
	 */
	public static String[] getBlacklist()
	{
		final String property = Config.getString(BLACKLIST_PROP, "");
		if (StringUtils.isNotBlank(property))
		{
			return property.split(",");
		}
		else
		{
			return new String[0];
		}
	}

	/**
	 * Checks if it possible to split the original string with delimiter.
	 * @param reference
	 * @param delimiter
	 * @return boolean
	 */
	public static boolean canSplitReference(final String reference, final String delimiter)
	{
		return reference.matches(NOT_EMPTY_REGEXP + delimiter + NOT_EMPTY_REGEXP);
	}

	/**
	 * Splits the original string to 2 parts
	 * @param reference
	 * @param delimiter
	 * @return array of 2 strings
	 */
	public static String[] splitReference(final String reference, final String delimiter)
	{
		return EventExportUtils.canSplitReference(reference, delimiter) ? reference.split(delimiter, REFERENCE_SPLIT_LIMIT) : new String[]
		{ reference };
	}

	/**
	 * Checks if the url string is a valid URL regarding to allowed protocols
	 * @param urlString
	 *           url string that is validated
	 * @return boolean
	 */
	public static boolean isUrlValid(final String urlString)
	{
		return urlString != null && isUrlValidInternal(urlString);
	}

	protected static boolean isUrlValidInternal(final String urlString)
	{
		boolean isValid;
		final List<String> allowedUrlProtocols = Arrays.asList(Config.getString(ALLOWED_URL_PROTOCOLS, "https").split("\\s*,\\s*"));

		try
		{
			final URI uri = new URI(urlString);
			isValid = allowedUrlProtocols.stream().anyMatch(uri.getScheme()::equalsIgnoreCase);
			if (!isValid)
			{
				LOG.error("Protocol {} is not allowed. To enable it, it needs to be added to property '{}'.", uri.getScheme(),
						ALLOWED_URL_PROTOCOLS);
			}
		}
		catch (final URISyntaxException e)
		{
			isValid = false;
			LOG.debug(String.format("Malformed Url: %s", urlString), e);
		}
		return isValid;
	}


	/**
	 * Replace the ccv2 end point placeholder with the configured value in the given url.
	 * expected input with the placeholder
	 * format:"{system property name}/rest of the url"
	 *
	 * @param url
	 *          url which contains the deployment end point place holder
	 * @return String
	 *          if the input string url is empty, null or does not start with "{" then the method
	 *          returns the given url
	 * @throws ConversionException
	 *          in case the method can't find the placeholder in the system property or can't extract
	 *          the placeholder variable from the given string
	 * @deprecated since 2005. Use {@link EventExportUtils#replacePropertyPlaceholders(String)}
	 */
	@Deprecated(since = "2005", forRemoval = true)
	public static String getUrlWithDeploymentAddress(final String url)
	{
		if (StringUtils.isNotBlank(url) && StringUtils.startsWith(url, "{"))
		{
			try
			{
				final String hostAddressPlaceHolder = url.substring(url.indexOf('{') + 1, url.indexOf('}'));
				final String hostAddressRealValue = Config.getParameter(hostAddressPlaceHolder);
				if (StringUtils.isBlank(hostAddressRealValue))
				{
					LOG.error("Unable to find the system property {}", hostAddressPlaceHolder);
				}
				else
				{
					return url.replace("{" + hostAddressPlaceHolder + "}", hostAddressRealValue);
				}
			}
			catch (final RuntimeException e)
			{
				throw new ConversionException(
						String.format("Unable to convert the url : %s", url), e);
			}
		}

		return url;
	}

	/**
	 * Replaces the placeholders (in curly brackets) in the argument with the configured system property values.
	 */
	public static String replacePropertyPlaceholders(final String argument)
	{
		if (StringUtils.isBlank(argument))
		{
			return argument;
		}
		try
		{
			final Matcher matcher = PROPERTY_PLACEHOLDER_PATTERN.matcher(argument);
			final StringBuffer stringBuffer = new StringBuffer();
			while (matcher.find())
			{
				final String propertyKey = matcher.group(1);
				String propertyToInsert = Config.getParameter(propertyKey);
				if (StringUtils.isBlank(propertyToInsert))
				{
					LOG.error("Unable to find the system property {}", propertyKey);
					propertyToInsert = matcher.group(0);
				}
				matcher.appendReplacement(stringBuffer, propertyToInsert);
			}
			matcher.appendTail(stringBuffer);
			return stringBuffer.toString();
		}
		catch (final RuntimeException e)
		{
			throw new ConversionException(
					String.format("Unable to convert the expression : %s", argument), e);
		}
	}
}
