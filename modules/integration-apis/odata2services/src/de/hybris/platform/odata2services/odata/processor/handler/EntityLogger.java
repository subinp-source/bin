/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler;

import de.hybris.platform.integrationservices.util.Log;

import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.slf4j.Logger;

import com.google.common.base.Strings;

public class EntityLogger
{
	private static final Logger LOG = Log.getLogger(EntityLogger.class);

	private EntityLogger()
	{
		// prevent instantiation
	}

	public static void logRequestEntity(final EdmEntityType entityType, final ODataEntry entry) throws EdmException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(createLogStatements(entityType, entry, 0));
		}
	}

	private static String createLogStatements(
			final EdmEntityType entityType, final ODataEntry oDataEntry,
			final int level) throws EdmException
	{
		final StringBuilder logBuilder = new StringBuilder();

		if (level < 1 && entityType != null)
		{
			logBuilder.append(String.format("Payload: %s%n", entityType.getName()));
			logBuilder.append("================\n");
		}

		final String indent = Strings.repeat("   ", level);
		for (final Map.Entry<String, Object> entry : oDataEntry.getProperties().entrySet())
		{
			final String propertyName = entry.getKey();
			final Object propertyValue = entry.getValue();
			if (propertyValue instanceof ODataEntry)
			{
				logBuilder.append(String.format("%s+ %s%n", indent, propertyName));
				logBuilder.append(createLogStatements(null, (ODataEntry) propertyValue, level + 1));
			}
			else if (propertyValue instanceof ODataFeed)
			{
				final List<ODataEntry> entries = ((ODataFeed) propertyValue).getEntries();
				int i = 0;
				for (final ODataEntry e : entries)
				{
					logBuilder.append(String.format("%s+ %s[%s]:%n", indent, propertyName, i++));
					logBuilder.append(createLogStatements(null, e, level + 1));
				}
			}
			else
			{
				final String value = propertyValue != null ? propertyValue.toString() : "";
				logBuilder.append(String.format("%s- %s: %s%n", indent, propertyName, value.replace("\n", "\\n")
						.replace("\r", "\\r")
						.replace("\t", "\\t")));
			}
		}

		if (level < 1 && entityType != null)
		{
			logBuilder.append("================");
		}
		return logBuilder.toString();
	}
}
