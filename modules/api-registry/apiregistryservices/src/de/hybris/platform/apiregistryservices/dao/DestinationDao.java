/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.dao;

import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;

import java.util.List;


/**
 * DAO for the {@link AbstractDestinationModel}
 *
 * @param <T>
 *           the type parameter which extends the {@link AbstractDestinationModel} type
 *
 */
public interface DestinationDao<T extends AbstractDestinationModel>
{
	/**
	 * Find destinations for specific destinationTarget
	 *
	 * @param destinationTargetId
	 *           the id of the DestinationTarget
	 * @return List of destinations for the given DestinationTarget
	 */
	List<T> getDestinationsByDestinationTargetId(String destinationTargetId);

	/**
	 * Find destinations for specific channel
	 *
	 * @param channel
	 *           the DestinationChannel
	 * @return List of destinations for the given channel
	 * @deprecated since 1905. Use {@link DestinationDao#getDestinationsByDestinationTargetId(String)}
	 */
	@Deprecated(since = "1905", forRemoval = true)
	List<T> getDestinationsByChannel(DestinationChannel channel);

	/**
	 * Get the destination for a specific id
	 *
	 * @param id
	 *           The unique identifier of destination
	 * @return AbstractDestinationModel
	 * @deprecated since 1905. Use {@link DestinationDao#findDestinationByIdAndByDestinationTargetId(String, String)}'
	 */
	@Deprecated(since = "1905", forRemoval = true)
	T getDestinationById(String id);

	/**
	 * Find all destinations
	 *
	 * @return the list of destinations
	 */
	List<T> findAllDestinations();

	/**
	 * Find the list of active destinations for specific clientId
	 *
	 * @param clientId
	 *           The clientId of OAuthClientDetails
	 * @return a List of Destinations by the ExposedOAuthCredential clientId
	 */
	List<ExposedDestinationModel> findActiveExposedDestinationsByClientId(String clientId);

	/**
	 * Find the list of active destinations for specific channel
	 *
	 * @param channel
	 *           The channel assigned to Destinations
	 * @return a List of Destinations by the credential
	 * @deprecated since 1905. Use {@link DestinationDao#findActiveExposedDestinationsByDestinationTargetId(String)}
	 */
	@Deprecated(since = "1905", forRemoval = true)
	List<ExposedDestinationModel> findActiveExposedDestinationsByChannel(DestinationChannel channel);

	/**
	 * Find the list of active exposed destinations for a specific destination target
	 *
	 * @param destinationTargetId
	 * 		id of the destination target
	 * @return a list of exposed destinations
	 */
	List<ExposedDestinationModel> findActiveExposedDestinationsByDestinationTargetId(final String destinationTargetId);

	/**
	 * Find the destination for a specific destination and a specific destination id
	 *
	 * @param destinationId
	 * 		id of the destination
	 * @param destinationTargetId
	 * 		id of the destination target
	 * @return the destination
	 */
	T findDestinationByIdAndByDestinationTargetId(String destinationId, String destinationTargetId);

	/**
	 * Find all consumed destinations
	 * @return the list of consumed destinations
	 */
	List<ConsumedDestinationModel> findAllConsumedDestinations();

	/**
	 * Find all consumed destinations
	 * @return the list of consumed destinations
	 */
	List<ExposedDestinationModel> findExposedDestinationsByCredentialId(String credentialId);

}
