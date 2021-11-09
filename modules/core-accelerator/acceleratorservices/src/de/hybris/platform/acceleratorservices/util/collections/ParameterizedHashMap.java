/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.util.collections;

import java.util.HashMap;


/**
 *
 */
public class ParameterizedHashMap<K, V> extends HashMap<K, V>
{
	public String getMessage(final K key, final V... parameters)
	{
		final Object initValue = get(key);

		String message = String.valueOf(initValue);
		if (parameters != null && parameters.length > 0)
		{
			for (int counter = 0; counter < parameters.length; counter++)
			{
				message = message.replace("{" + counter + "}", String.valueOf(parameters[counter]).trim());
			}
		}

		// Return the property name if no message found so it can be looked up.
		if (message == null || "null".equalsIgnoreCase(message))
		{
			message = String.valueOf("${" + key + "}");
		}
		return message;
	}
}
