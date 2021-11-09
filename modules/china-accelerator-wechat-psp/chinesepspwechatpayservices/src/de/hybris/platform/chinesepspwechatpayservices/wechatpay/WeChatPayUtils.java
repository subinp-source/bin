/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.wechatpay;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.WordUtils;


/**
 * Utilities functions
 */
public class WeChatPayUtils
{

	/**
	 *
	 */
	private WeChatPayUtils()
	{
		throw new IllegalAccessError("WeChatPayUtils class");
	}

	public static Map<String, String> convertKey2CamelCase(final Map<String, String> snakeCaseMap)
	{
		final Map<String, String> camelCaseMap = new LinkedHashMap<>();
		for (final Map.Entry<String, String> entry : snakeCaseMap.entrySet())
		{
			final String value = entry.getValue();
			final String key = entry.getKey();
			String camelKey = WordUtils.capitalizeFully(key, new char[]
			{ '_' }).replace("_", "");
			camelKey = WordUtils.uncapitalize(camelKey);
			camelCaseMap.put(camelKey, value);
		}
		return camelCaseMap;
	}
}
