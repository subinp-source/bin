/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services;

import java.util.Map;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;


/**
 * Interface responsible for configuration build strategy for concrete Credential type
 */
public interface ClientCredentialPopulatingStrategy
{
    /**
     * Method which populates the config with credential data for client build by Charon.
     * @param credentialModel creds
     * @param config config to be updated
     */
	void populateConfig(AbstractCredentialModel credentialModel, Map<String, String> config) throws CredentialException;
}
