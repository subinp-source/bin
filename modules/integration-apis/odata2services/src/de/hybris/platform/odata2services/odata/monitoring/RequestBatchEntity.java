/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.monitoring;

import de.hybris.platform.integrationservices.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Information about a batch present in the request body.
 */
public class RequestBatchEntity
{
	private static final Logger LOG = Log.getLogger(RequestBatchEntity.class);
	private static final byte[] NO_CONTENT = new byte[0];

	private final byte[] content;
	private final String messageId;
	private final String integrationObjectType;
	private final String integrationKey;
	private final int numberOfChangeSets;

	/**
	 * Instantiates this {@code RequestBatchEntity}
	 *
	 * @param msgId message ID of the inbound request. It's common for all batches.
	 * @param payload the batch content from the request body
	 * @param objType integration object type. This is common for all batches.
	 * @param cnt number of change sets in this batch
	 * @deprecated Use {@link #RequestBatchEntity(String, InputStream, String, int, String)} instead
	 */
	@Deprecated(since = "1905", forRemoval = true)
	protected RequestBatchEntity(final String msgId, final InputStream payload, final String objType, final int cnt)
	{
		this(msgId, payload, objType, cnt, "");
	}

	/**
	 * Instantiates this {@code RequestBatchEntity}
	 *
	 * @param msgId message ID of the inbound request. It's common for all batches.
	 * @param payload the batch content from the request body
	 * @param objType integration object type. This is common for all batches.
	 * @param cnt number of change sets in this batch
	 * @param key integration key of the request
	 */
	protected RequestBatchEntity(final String msgId, final InputStream payload, final String objType, final int cnt, final String key)
	{
		Preconditions.checkArgument(cnt > 0, "Number of change sets must be a positive number");
		messageId = Strings.nullToEmpty(msgId);
		integrationObjectType = Strings.nullToEmpty(objType);
		content = toByteArray(payload);
		numberOfChangeSets = cnt;
		integrationKey = Strings.nullToEmpty(key);
	}

	private byte[] toByteArray(final InputStream payload)
	{
		try
		{
			return payload != null ? IOUtils.toByteArray(payload) : NO_CONTENT;
		}
		catch (final IOException e)
		{
			LOG.warn("Failed to read payload for {} message with ID {} and integration key {}",
					integrationObjectType, messageId, integrationKey, e);
			return NO_CONTENT;
		}
	}

	public InputStream getContent()
	{
		return content == NO_CONTENT
				? null
				: new ByteArrayInputStream(content);
	}

	public String getMessageId()
	{
		return messageId;
	}

	public String getIntegrationObjectType()
	{
		return integrationObjectType;
	}

	public String getIntegrationKey()
	{
		return integrationKey;
	}

	public int getNumberOfChangeSets()
	{
		return numberOfChangeSets;
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}

		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final RequestBatchEntity that = (RequestBatchEntity) o;

		return new EqualsBuilder()
				.append(numberOfChangeSets, that.numberOfChangeSets)
				.append(content, that.content)
				.append(messageId, that.messageId)
				.append(integrationObjectType, that.integrationObjectType)
				.append(integrationKey, that.integrationKey)
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(content)
				.append(messageId)
				.append(integrationObjectType)
				.append(integrationKey)
				.append(numberOfChangeSets)
				.toHashCode();
	}

	@Override
	public String toString()
	{
		return "RequestBatchEntity{" +
				"messageId='" + messageId + '\'' +
				", integrationObjectType='" + integrationObjectType + '\'' +
				", integrationKey=" + integrationKey +
				", numberOfChangeSets=" + numberOfChangeSets +
				'}';
	}
}
