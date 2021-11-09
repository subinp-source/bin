/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.services;

import de.hybris.platform.kymaintegrationservices.dto.KymaRegistrationRequest;

/**
 * One-click Kyma registration service interface to pair SAP Commerce Cloud with a Kyma instance.
 */
public interface KymaDestinationTargetRegistrationService
{
	/**
	 * Register a Kyma destination target.
	 *
	 * @param kymaRegistrationRequest  Kyma registration request @{@link KymaRegistrationRequest }.
	 * @param isReRegistrationAllowed If it is true, the re-registration is allowed. This flag is set to false when it is called from the backoffice controller
	 *                                 and it is set to true when it is called form the AdminAPI controller.
	 */
	void registerDestinationTarget(final KymaRegistrationRequest kymaRegistrationRequest, final boolean isReRegistrationAllowed);
}
