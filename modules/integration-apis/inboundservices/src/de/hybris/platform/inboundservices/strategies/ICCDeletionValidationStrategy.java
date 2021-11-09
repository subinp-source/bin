/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.strategies;

import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

public interface ICCDeletionValidationStrategy
{
	void checkICCLinkedWithExposedDestination(final InboundChannelConfigurationModel inboundChannelConfigurationModel) throws
			InterceptorException;
}
