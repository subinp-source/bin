/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.outboundservices.config.OutboundServicesConfiguration;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.junit.rules.ExternalResource;

/**
 * A rule for enabling/disabling outbound request monitoring in integration tests.
 */
public class OutboundMonitoringRule extends ExternalResource
{
	private final ConfigurationService configuration;
	private final boolean originalMonitoringState;

	private OutboundMonitoringRule()
	{
		configuration = getConfigurationService();
		originalMonitoringState = getOutboundServicesConfiguration().isMonitoringEnabled();
	}

	/**
	 * Creates a rule that disables outbound request monitoring feature.
	 * @return a monitoring rule with monitoring disabled.
	 */
	public static OutboundMonitoringRule disabled()
	{
		return new OutboundMonitoringRule().disableMonitoring();
	}

	/**
	 * Creates a rule that enables outbound request monitoring feature.
	 * @return a monitoring rule with monitoring enabled.
	 */
	public static OutboundMonitoringRule enabled()
	{
		return new OutboundMonitoringRule().enableMonitoring();
	}

	private OutboundMonitoringRule enableMonitoring()
	{
		setMonitoring(true);
		return this;
	}

	private OutboundMonitoringRule disableMonitoring()
	{
		setMonitoring(false);
		return this;
	}

	private void setMonitoring(final boolean value)
	{
		configuration.getConfiguration().setProperty("outboundservices.monitoring.enabled", String.valueOf(value));
	}

	@Override
	protected void after()
	{
		setMonitoring(originalMonitoringState);
	}

	private static ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext()
				.getBean("configurationService", ConfigurationService.class);
	}

	private static OutboundServicesConfiguration getOutboundServicesConfiguration()
	{
		return Registry.getApplicationContext()
				.getBean("outboundServicesConfiguration", OutboundServicesConfiguration.class);
	}
}
