/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.persistence.polyglot.config;

import java.util.List;

public class MockedPolyglotRepositoryConfigProvider implements PolyglotRepositoriesConfigProvider
{
	private final List<RepositoryConfig> configs;

	public MockedPolyglotRepositoryConfigProvider(final List<RepositoryConfig> configs)
	{
		this.configs = configs;
	}

	@Override
	public List<RepositoryConfig> getConfigs()
	{
		return configs;
	}
}
