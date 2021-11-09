/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsyncbackoffice.widgets.models.builders;

import de.hybris.platform.integrationbackoffice.widgets.modals.builders.AbstractAuditReportBuilder;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;

/**
 * A audit report builder that is specialized on OutboundChannelConfiguration object.
 */
public class OutboundChannelConfigurationAuditReportBuilder extends AbstractAuditReportBuilder
{
	/**
	 * Payloads will be sent to AuditReportBuilderTemplate and shown in a html file.
	 * Each payload has the structure described in outboundsyncbackoffice-OutboundChannelConfiguration-audit.xml which the logic in
	 * this class is based on .
	 * Each payload will go through this method. This class is meant to reformat the payload for better readability.
	 * Each payload is represented by a Map. Its attribute could be string, map or list.
	 */
	@Override
	public void traversePayload(final Map eachPayload)
	{
		convertList(eachPayload);
		flattenMap(eachPayload);
	}

	/**
	 * If the audited object has an attribute as a list, it is converted to a Map for better readability of the report.
	 * If list consists of maps which has a "_id", extract them and use them as the keys.
	 * Remove the list if it is converted.
	 */
	private void convertList(final Map<String, Object> map)
	{
		final HashSet<String> keysToRemove = new HashSet<>();
		final HashMap<String, Map<String, Object>> keysToAdd = new HashMap<>();
		map.forEach((keyInMap, value) ->
		{
			if (value instanceof List && !((List) value).isEmpty())
			{
				if (isListConverted((List) value, keysToAdd))
				{
					keysToRemove.add(keyInMap);
				}
			}
			else if (value instanceof Map)
			{
				convertList((Map) value);
			}
		});

		keysToRemove.forEach(map::remove);
		keysToAdd.forEach(map::put);
	}

	/**
	 * Any element converted means the list is converted. So the list could be removed.
	 */
	private boolean isListConverted(final List<?> list, final Map<String, Map<String, Object>> keysToAdd)
	{
		list.stream()
		    .filter(obj -> obj instanceof Map)
		    .map(Map.class::cast)
		    .forEach(this::convertList);
		return list.stream()
		           .filter(object -> object instanceof Map)
		           .map(object -> isMapContainsId((Map) object, keysToAdd))
		           .reduce(false, (isConverted, result) -> isConverted || result);
	}

	/**
	 * if Map consists of key and valueObject with a "_id", extract them as a key and use the map as a value.
	 * Return if one element is converted.
	 */
	private boolean isMapContainsId(final Map<String, Object> mapInList, final Map<String, Map<String, Object>> keyToAdd)
	{
		final Iterator<String> iterator = mapInList.keySet().iterator();  // use iterator to remove key from map while iterating
		while (iterator.hasNext())
		{
			final String key = iterator.next();
			if (StringUtils.endsWith(key, "_id")) //  see key names in fields "displayName" in the config file.
			{
				final String newKey = key + " : " + mapInList.get(key);
				iterator.remove();
				keyToAdd.put(newKey, mapInList);
				return true;
			}
		}
		return false;
	}

	/**
	 * if Map consists of key and valueObject with a "_id", extract them.
	 */
	private void flattenMap(final Map<String, Object> map)
	{
		final HashSet<String> keysToRemove = new HashSet<>();
		final HashMap<String, Map<String, Object>> keysToAdd = new HashMap<>();
		map.forEach((key, value) -> {
			if (value instanceof Map && !((Map) value).isEmpty())
			{
				final Map<String, Object> subMap = (Map) value;
				flattenMap(subMap);
				final AtomicReference<String> keyToRemove = new AtomicReference<>("");
				final AtomicReference<String> newkey = new AtomicReference<>("");
				subMap.keySet().forEach(subMapKey -> {
					if (StringUtils.equals(subMapKey, key + "_id") && subMap.get(
							subMapKey) instanceof String)
					{
						keyToRemove.set(subMapKey);
						newkey.set(subMapKey + " : " + subMap.get(subMapKey));
					}
				});
				if (StringUtils.isNotEmpty(keyToRemove.get()))
				{
					subMap.remove(keyToRemove.get());
					keysToRemove.add(key);
					keysToAdd.put(newkey.get(), subMap);
				}
			}
		});
		keysToRemove.forEach(map::remove);
		keysToAdd.forEach(map::put);
	}

	@Override
	public String getDownloadFileName()
	{
		return ((OutboundChannelConfigurationModel) this.getSelectedModel()).getCode();
	}
}
