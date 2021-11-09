/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync;

import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.outboundsync.job.impl.InMemoryGettableChangesCollector;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.rules.ExternalResource;

/**
 * A jUnit test rule for verifying item change conditions and creating/managing the change streams associated with
 * the items changes.
 */
public class TestItemChangeDetector extends ExternalResource
{
	private static final String STREAM_CONTAINER = "outboundSyncDataStreams";

	private ChangeDetectionService deltaDetect;
	private Collection<OutboundSyncStreamConfigurationModel> allChangeStreams = new ArrayList<>();

	@Override
	protected void after()
	{
		reset();
	}

	/**
	 * Resets its state by consuming all changes in all created change streams and deletes the created change streams.
	 * This method does not affect streams created outside of this rule class.
	 */
	public void reset()
	{
		consumeAllCurrentChanges();
		IntegrationTestUtil.removeAll(OutboundSyncStreamConfigurationModel.class);
		allChangeStreams.clear();
	}

	/**
	 * Consumes all changes, which possibly present in all so far created streams.
	 *
	 * @see #createChangeStream(String, String, String)
	 */
	public void consumeAllCurrentChanges()
	{
		getDeltaDetect().consumeChanges(getAllCurrentChanges());
	}

	/**
	 * Determines whether any of the created change streams has unconsumed item changes.
	 *
	 * @return {@code true}, if all changes are consumed; {@code false}, if at least one unconsumed item change is present in
	 * at least of of the created streams.
	 * @see #createChangeStream(String, String, String)
	 * @see #getAllCurrentChanges()
	 */
	public boolean hasAllChangesConsumed()
	{
		return getAllCurrentChanges().isEmpty();
	}

	/**
	 * Retrieves all changes from all created change stream, which have not been consumed yet.
	 *
	 * @return a list of item changes or an empty list, if there are no outstanding unconsumed changes.
	 */
	public List<ItemChangeDTO> getAllCurrentChanges()
	{
		return allChangeStreams.stream()
		                       .map(this::getChangesFromStream)
		                       .flatMap(Collection::stream)
		                       .collect(Collectors.toList());
	}

	private List<ItemChangeDTO> getChangesFromStream(final OutboundSyncStreamConfigurationModel deltaStream)
	{
		final var changesCollector = new InMemoryGettableChangesCollector();
		final var configuration = StreamConfiguration.buildFor(deltaStream.getStreamId())
		                                             .withItemSelector(deltaStream.getWhereClause());

		getDeltaDetect().collectChangesForType(deltaStream.getItemTypeForStream(), configuration, changesCollector);
		return changesCollector.getChanges();
	}

	/**
	 * Creates an outbound channel configuration
	 *
	 * @param channelCode code for the channel to create
	 * @param io          integration object code to associate with the channel
	 * @param destination a consumed destination model to associate with the channel
	 */
	public void createChannel(final String channelCode, final String io, final ConsumedDestinationModel destination)
			throws ImpExException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE OutboundChannelConfiguration	; code[unique = true] ; integrationObject(code) ; destination",
				"											; " + channelCode + " ; " + io + "              ; " + destination.getPk());
	}


	/**
	 * Creates new delta detect stream and guarantees it has no outstanding changes in it.
	 *
	 * @param channel  a delta detect channel, the new stream should associated with.
	 * @param typecode type code of a type system item, the stream should contain changes for.
	 * @return the created change stream
	 */
	public OutboundSyncStreamConfigurationModel createChangeStream(final OutboundChannelConfigurationModel channel,
	                                                               final String typecode)
			throws ImpExException
	{
		return createChangeStream(channel, typecode, "");
	}

	/**
	 * Creates new delta detect stream and guarantees it has no outstanding changes in it.
	 *
	 * @param channel  a delta detect channel, the new stream should associated with.
	 * @param typecode type code of a type system item, the stream should contain changes for.
	 * @param filter   additional {@code where} clause condition to be applied to the items in the stream. Only changes for the items
	 *                 matching this conditions will be reported by the stream. Changes done to the non-matching items will no be contained in this stream.
	 * @return the created change stream
	 */
	public OutboundSyncStreamConfigurationModel createChangeStream(final OutboundChannelConfigurationModel channel,
	                                                               final String typecode,
	                                                               final String filter) throws ImpExException
	{
		return createChangeStream(channel.getCode(), typecode, filter);
	}

	/**
	 * Creates new delta detect stream and guarantees it has no outstanding changes in it.
	 *
	 * @param channelCode code of the corresponding delta detect channel, the new stream should associated with.
	 * @param typecode    type code of a type system item, the stream should contain changes for.
	 * @return the created change stream
	 */
	public OutboundSyncStreamConfigurationModel createChangeStream(final String channelCode, final String typecode)
			throws ImpExException
	{
		return createChangeStream(channelCode, typecode, "");
	}

	/**
	 * Creates new delta detect stream and guarantees it has no outstanding changes in it.
	 *
	 * @param channelCode code of the corresponding delta detect channel, the new stream should associated with.
	 * @param typecode    type code of a type system item, the stream should contain changes for.
	 * @param filter      additional {@code where} clause condition to be applied to the items in the stream. Only changes for the items
	 *                    matching this conditions will be reported by the stream. Changes done to the non-matching items will no be contained in this stream.
	 * @return the created change stream
	 */
	public OutboundSyncStreamConfigurationModel createChangeStream(final String channelCode, final String typecode,
	                                                               final String filter)
			throws ImpExException
	{
		OutboundSyncStreamConfigurationModel stream = persistStream(typecode, channelCode, filter);
		allChangeStreams.add(stream);
		getDeltaDetect().consumeChanges(getChangesFromStream(stream));
		return stream;
	}

	private static OutboundSyncStreamConfigurationModel persistStream(String typecode, String channelCode, String filter)
			throws ImpExException
	{
		final var streamId = typecode + "Stream";
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE OutboundSyncStreamConfiguration; streamId[unique = true]; container(id)           ; itemTypeForStream(code); outboundChannelConfiguration(code); whereClause",
				"                                             ; " + streamId + "       ; " + STREAM_CONTAINER + "; " + typecode + "       ; " + channelCode + "               ; " + filter);
		return IntegrationTestUtil
				.findAny(OutboundSyncStreamConfigurationModel.class, cfg -> cfg.getStreamId().equals(streamId))
				.orElseThrow(() -> new IllegalStateException(
						"Failed to persist a " + typecode + " change stream for channel " + channelCode));
	}

	private ChangeDetectionService getDeltaDetect()
	{
		if (deltaDetect == null)
		{
			deltaDetect = Registry.getApplicationContext().getBean("changeDetectionService", ChangeDetectionService.class);
		}
		return deltaDetect;
	}
}