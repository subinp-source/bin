/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.inboundservices.config;

import de.hybris.platform.integrationservices.monitoring.MonitoringConfiguration;

/**
 * Configuration for Inbound Services extension
 */
public interface InboundServicesConfiguration extends MonitoringConfiguration
{
	/**
	 * Determines if the system security is using legacy security.
	 *
	 * @return true or false depending if the property is true or not.
	 */
	boolean isLegacySecurity();
}
