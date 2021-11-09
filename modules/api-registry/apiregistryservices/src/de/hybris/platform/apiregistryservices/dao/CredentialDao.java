/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.dao;

import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;

import java.util.List;


/**
 * DAO for the {@link ExposedOAuthCredentialModel}
 */
public interface CredentialDao
{
	/**
	 * Find the list of ExposedOAuthCredentials for specific clientId
	 *
	 * @param clientId
	 *           The clientId of OAuthClientDetails
	 * @return a List of ExposedOAuthCredentials by the clientId
	 */
	List<ExposedOAuthCredentialModel> getAllExposedOAuthCredentialsByClientId(String clientId);
}
