/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.dao;

import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;


/**
 * DAO for the {@link DestinationTargetModel}
 */
public interface DestinationTargetDao
{

	/**
	 * Find the destination target for a specific credential id
	 * @param credentialId
	 * 		id of the credential
	 * @return a destination target
	 */
	DestinationTargetModel findDestinationTargetByCredentialId (String credentialId);

	/**
	 * Find the destination target for a specific id
	 * @param id
	 * 		id of the destination target
	 * @return a destination target
	 */
	DestinationTargetModel findDestinationTargetById (String id);

}
