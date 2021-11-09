/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.dto;

import de.hybris.platform.core.PK;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

@Immutable
public class OutboundItemDTO
{
	private OutboundItemChange item;
	private Long integrationObjectPK;
	private Long channelConfigurationPK;
	private Long rootItemPK;
	private PK cronJobPK;

	public OutboundItemChange getItem()
	{
		return item;
	}

	public Long getIntegrationObjectPK()
	{
		return integrationObjectPK;
	}

	public Long getChannelConfigurationPK()
	{
		return channelConfigurationPK;
	}

	public Long getRootItemPK()
	{
		return rootItemPK;
	}

	public PK getCronJobPK()
	{
		return cronJobPK;
	}

	@Override
	public String toString()
	{
		return "OutboundItemChange{" +
				"item=" + item +
				", rootItemPK=" + rootItemPK +
				", integrationObjectPK=" + integrationObjectPK +
				", channelConfigurationPK=" + channelConfigurationPK +
				", cronJobPK=" + cronJobPK +
				'}';
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

		final OutboundItemDTO that = (OutboundItemDTO) o;

		return new EqualsBuilder()
				.append(item, that.item)
				.append(integrationObjectPK, that.integrationObjectPK)
				.append(channelConfigurationPK, that.channelConfigurationPK)
				.append(rootItemPK, that.rootItemPK)
				.append(cronJobPK, that.cronJobPK)
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(item)
				.append(integrationObjectPK)
				.append(channelConfigurationPK)
				.append(rootItemPK)
				.append(cronJobPK)
				.toHashCode();
	}

	public static final class Builder
	{
		private final OutboundItemDTO outboundItemDTO = new OutboundItemDTO();

		private Builder()
		{
		}

		public static Builder item()
		{
			return new Builder();
		}

		public static Builder from(final OutboundItemDTO dto)
		{
			return new Builder()
					.withItem(dto.getItem())
					.withIntegrationObjectPK(dto.getIntegrationObjectPK())
					.withChannelConfigurationPK(dto.getChannelConfigurationPK())
					.withRootItemPK(dto.getRootItemPK())
					.withCronJobPK(dto.getCronJobPK());
		}

		public Builder withItem(final OutboundItemChange item)
		{
			outboundItemDTO.item = item;
			return this;
		}

		public Builder withIntegrationObjectPK(final Long pk)
		{
			outboundItemDTO.integrationObjectPK = pk;
			return this;
		}

		public Builder withChannelConfigurationPK(final Long pk)
		{
			outboundItemDTO.channelConfigurationPK = pk;
			return this;
		}

		public Builder withRootItemPK(final Long pk)
		{
			outboundItemDTO.rootItemPK = pk;
			return this;
		}

		public Builder withCronJobPK(final PK pk)
		{
			outboundItemDTO.cronJobPK = pk;
			return this;
		}

		public OutboundItemDTO build()
		{
			validateItem();
			return outboundItemDTO;
		}

		private void validateItem()
		{
			Preconditions.checkArgument(outboundItemDTO.item != null, "item cannot be null");
			Preconditions.checkArgument(outboundItemDTO.integrationObjectPK != null, "integrationObject PK cannot be null");
			Preconditions.checkArgument(outboundItemDTO.channelConfigurationPK != null, "channelConfiguration PK cannot be null");
			Preconditions.checkArgument(outboundItemDTO.cronJobPK != null, "cronJob PK cannot be null");
		}
	}
}
