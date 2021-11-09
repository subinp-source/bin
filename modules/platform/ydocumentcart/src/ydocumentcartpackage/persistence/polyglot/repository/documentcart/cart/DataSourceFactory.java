/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.cart;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.FactoryBean;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class DataSourceFactory implements FactoryBean<DataSource>
{
	private final ConfigurationService configurationService;
	private final MetricRegistry metricRegistry;

	/**
	 * @deprecated use {@link #DataSourceFactory(ConfigurationService, MetricRegistry)}
	 */
	@Deprecated(since = "2005", forRemoval = true)
	public DataSourceFactory(final ConfigurationService configurationService)
	{
		this(configurationService, null);
	}

	public DataSourceFactory(final ConfigurationService configurationService, final MetricRegistry metricRegistry)
	{
		this.configurationService = Objects.requireNonNull(configurationService, "configurationService mustn't be null.");
		this.metricRegistry = metricRegistry;
	}

	@Override
	public DataSource getObject() throws Exception
	{
		final Configuration config = configurationService.getConfiguration();
		final HikariConfig poolConfig = new HikariConfig();

		poolConfig.setDriverClassName(config.getString("ydocumentcart.storage.jdbc.driver"));
		poolConfig.setJdbcUrl(config.getString("ydocumentcart.storage.jdbc.url"));
		poolConfig.setUsername(config.getString("ydocumentcart.storage.jdbc.user"));
		poolConfig.setPassword(config.getString("ydocumentcart.storage.jdbc.password"));
		poolConfig.setPoolName(getCurrentTenantId());
		poolConfig.setMaximumPoolSize(config.getInt("ydocumentcart.storage.jdbc.pool.size"));

		if (metricRegistry != null)
		{
			poolConfig.setMetricRegistry(metricRegistry);
		}

		addNamedProperties(config, poolConfig);

		return new HikariDataSource(poolConfig);
	}

	private String getCurrentTenantId()
	{
		return Registry.getCurrentTenant().getTenantID();
	}

	private void addNamedProperties(final Configuration config, final HikariConfig poolConfig)
	{
		final String propsPrefix = "ydocumentcart.storage.jdbc.props";
		config.getKeys("ydocumentcart.storage.jdbc.props").forEachRemaining(key -> {
			if (key.length() <= propsPrefix.length())
			{
				return;
			}
			final String propertyName = key.substring(propsPrefix.length() + 1);
			poolConfig.addDataSourceProperty(propertyName, config.getString(key));
		});
	}

	@Override
	public Class<?> getObjectType()
	{
		return DataSource.class;
	}
}
