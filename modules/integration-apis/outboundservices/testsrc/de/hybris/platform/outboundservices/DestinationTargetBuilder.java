/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices;

import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;

import java.util.Optional;

/**
 * A helper to be used in integration tests for creating and persisting {@link de.hybris.platform.apiregistryservices.model.DestinationTargetModel}
 */
public class DestinationTargetBuilder
{
	private static final String DEFAULT_ID = "stoutoutboundtest";

	private String destinationId;
	private DestinationChannel destinationChannel;
	private RegistrationStatus registrationStatus;

	private DestinationTargetBuilder()
	{
		// non-instantiable
	}

	/**
	 * Instantiates a new builder initialized to default state.
	 *
	 * @return new destination target builder
	 */
	public static DestinationTargetBuilder destinationTarget()
	{
		return new DestinationTargetBuilder();
	}

	/**
	 * Specifies ID for the destination target to build.
	 *
	 * @param id identifier of the destination target to create. If the value is
	 * @return the builder with the ID specified
	 */
	public DestinationTargetBuilder withId(final String id)
	{
		destinationId = id;
		return this;
	}

	/**
	 * Specifies the destination channel for the destination target being built.
	 *
	 * @param channel destination channel to be used by the destination target. If the channel value is {@code null} or this method
	 *                was not called, then the platform default channel will be used.
	 * @return a builder with the destination channel specified.
	 */
	public DestinationTargetBuilder withDestinationChannel(final DestinationChannel channel)
	{
		destinationChannel = channel;
		return this;
	}

	/**
	 * Specifies registration status for the destination target.
	 *
	 * @param status a status to be used by the destination target being built by this builder. If the value is {@code null} or
	 *               this method was not called, then destination target will be created without the registration status.
	 * @return a builder with the registration status specified.
	 */
	public DestinationTargetBuilder withRegistrationStatus(final RegistrationStatus status)
	{
		registrationStatus = status;
		return this;
	}

	public DestinationTargetModel build() throws ImpExException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE DestinationTarget; id[unique = true] ; destinationChannel(code); registrationStatus(code)",
				"                               ; " + deriveId() + "; " + deriveChannel() + " ; " + deriveStatus());
		return getDestinationTargetById(deriveId()).orElse(null);
	}

	private String deriveId()
	{
		return destinationId != null ? destinationId : DEFAULT_ID;
	}

	private String deriveChannel()
	{
		return destinationChannel != null ? destinationChannel.getCode() : "";
	}

	private String deriveStatus()
	{
		return registrationStatus != null ? registrationStatus.getCode() : "";
	}

	/**
	 * Looks up a destination target in the persistence storage.
	 *
	 * @param id ID of the destination target to find.
	 * @return an optional containing the matching destination target or {@code Optional.empty()}, if such destination target does
	 * not exist.
	 */
	public static Optional<DestinationTargetModel> getDestinationTargetById(final String id)
	{
		return IntegrationTestUtil.findAny(DestinationTargetModel.class, m -> m.getId().equals(id));
	}
}
