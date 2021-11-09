/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.facade.impl;

import de.hybris.platform.util.Config;
import de.hybris.smartedit.facade.SettingsFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;

/**
 * Implementation of {@link SettingsFacade}
 */
public class DefaultSettingsFacade implements SettingsFacade
{
	private final String mimeTypesKey = "smartedit.validImageMimeTypeCodes";
	private final List<String> properties = Arrays
			.asList("smartedit.sso.enabled", "smartedit.globalBasePath", "cms.components.allowUnsafeJavaScript", mimeTypesKey);

	/**
	 * @return Values from the project.properties file for the following keys:
	 * - smartedit.validImageMimeTypeCodes
	 * - smartedit.sso.enabled
	 * - smartedit.globalBasePath
	 * - cms.components.allowUnsafeJavaScript
	 */
	@Override
	public Map<String, Object> getSettings()
	{
		final Map<String, Object> settings = properties
				.stream()
				.collect(HashMap::new, (m, v) -> m.put(v, getSetting(v)), HashMap::putAll);
		settings.entrySet().removeIf(entry -> entry.getValue() == null);

		final String commaSeparatedMimeTypes = getSetting(mimeTypesKey);
		if (commaSeparatedMimeTypes != null)
		{
			List<String> mimeTypes = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(commaSeparatedMimeTypes);
			if (!mimeTypes.isEmpty())
			{
				settings.put(mimeTypesKey, mimeTypes);
			}
		}
		return settings;
	}

	/**
	 * @param key - the key of the desired property, from the project.properties file
	 * @return value - the value associated with the key
	 */
	public String getSetting(String key)
	{
		return Config.getString(key, null);
	}
}