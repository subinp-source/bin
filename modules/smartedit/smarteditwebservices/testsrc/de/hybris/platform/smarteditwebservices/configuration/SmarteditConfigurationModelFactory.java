/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import de.hybris.platform.core.PK;
import de.hybris.platform.smarteditwebservices.model.SmarteditConfigurationModel;

import org.apache.commons.lang.StringUtils;


public class SmarteditConfigurationModelFactory
{

	public static SmarteditConfigurationModel modelBuilder(final String key, final String value)
	{
		return modelBuilder(null, key, value);
	}

	public static SmarteditConfigurationModel modelBuilder(final String id, final String key, final String value)
	{
		final SmarteditConfigurationModel pair = spy(new SmarteditConfigurationModel());
		if (StringUtils.isNotEmpty(id))
		{
			doReturn(PK.fromLong(Long.valueOf(id).longValue())).when(pair).getPk();
		}
		pair.setKey(key);
		pair.setValue(value);
		return pair;
	}

}
