/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integrationservices.service.impl;

import de.hybris.platform.integrationservices.config.IntegrationServicesConfiguration;
import de.hybris.platform.integrationservices.passport.SapPassportBuilder;
import de.hybris.platform.integrationservices.service.SapPassportService;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Required;

import com.sap.jdsr.passport.DSRPassport;
import com.sap.jdsr.passport.EncodeDecode;

/**
 * Default implementation of {@link SapPassportService}
 */
public class DefaultSapPassportService implements SapPassportService
{
	private IntegrationServicesConfiguration configuration;

	@Override
	public String generate(final String integrationObjectCode)
	{
		final SapPassportBuilder builder = SapPassportBuilder.newSapPassportBuilder();
		final DSRPassport passport = builder.withVersion(3)
				.withTraceFlag(0)
				.withSystemId(getConfiguration().getSapPassportSystemId())
				.withService(getConfiguration().getSapPassportServiceValue())
				.withUser(getConfiguration().getSapPassportUser())
				.withAction("send " + integrationObjectCode)
				.withActionType(11)
				.withPrevSystemId("")
				.withTransId(getUuidAsString())
				.withClientNumber("")
				.withSystemType(1)
				.withRootContextId(getUuidAsBytes())
				.withConnectionId(getUuidAsBytes())
				.withConnectionCounter(0)
				.build();

		final byte[] passportBytes = EncodeDecode.encodeBytePassport(passport);

		return Hex.encodeHexString(passportBytes);
	}

	protected IntegrationServicesConfiguration getConfiguration()
	{
		return configuration;
	}

	/**
	 * Get a UUID
	 * @return UUID
	 */
	protected byte[] getUuidAsBytes()
	{
		final UUID uuid = UUID.randomUUID();
		final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
		byteBuffer.putLong(uuid.getMostSignificantBits());
		byteBuffer.putLong(uuid.getLeastSignificantBits());

		return byteBuffer.array();
	}

	/**
	 * Get a UUID as String
	 * @return UUID as String
	 */
	protected String getUuidAsString()
	{
		final StringBuilder uuidString = new StringBuilder();
		final byte[] uuidBytes = getUuidAsBytes();
		for (final byte uuidByte : uuidBytes)
		{
			uuidString.append(String.format("%02x", uuidByte));
		}
		return uuidString.toString().toUpperCase();
	}

	@Required
	public void setConfiguration(final IntegrationServicesConfiguration config)
	{
		this.configuration = config;
	}
}
