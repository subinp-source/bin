/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.util;

import de.hybris.platform.core.Registry;

import java.text.MessageFormat;

import com.codahale.metrics.MetricRegistry;

public class MetricUtils
{
	private MetricUtils()
	{
		//empty
	}

	public static String metricName(final String repositoryName, final String timerName, final String moduleName)
	{
		final String tenantId = Registry.getCurrentTenantNoFallback().getTenantID();
		final String name = MetricRegistry.name("persistence.polyglot", repositoryName, timerName);
		return MessageFormat.format("tenant={0},extension=documentcart,module={1},name={2}", tenantId, moduleName, name);
	}
}
