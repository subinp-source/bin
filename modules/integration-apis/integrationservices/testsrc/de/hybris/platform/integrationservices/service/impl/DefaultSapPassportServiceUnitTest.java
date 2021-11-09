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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.integrationservices.config.IntegrationServicesConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.sap.jdsr.passport.DSRPassport;
import com.sap.jdsr.passport.EncodeDecode;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultSapPassportServiceUnitTest
{
	@InjectMocks
	private DefaultSapPassportService service;
	@Mock
	private IntegrationServicesConfiguration integrationServicesConfiguration;

	@Before
	public void setUp()
	{
		when(integrationServicesConfiguration.getSapPassportSystemId()).thenReturn("My SAP Commerce");
		when(integrationServicesConfiguration.getSapPassportServiceValue()).thenReturn(139);
		when(integrationServicesConfiguration.getSapPassportUser()).thenReturn("My SAP User");
	}

	@Test
	public void testGenerate() throws Exception
	{
		final String passport = service.generate("MY_CODE");
		final byte[] passportBytes = Hex.decodeHex(passport.toCharArray());
		final DSRPassport dsrPassport = EncodeDecode.decodeBytePassport(passportBytes);

		assertThat(dsrPassport).hasFieldOrPropertyWithValue("version", 3)
				.hasFieldOrPropertyWithValue("traceFlag", 0)
				.hasFieldOrPropertyWithValue("systemIdByte", getBytes("My SAP Commerce"))
				.hasFieldOrPropertyWithValue("service", 139)
				.hasFieldOrPropertyWithValue("userIdByte", getBytes("My SAP User"))
				.hasFieldOrPropertyWithValue("actionByte", getBytes("send MY_CODE"))
				.hasFieldOrPropertyWithValue("actionType", 11)
				.hasFieldOrPropertyWithValue("prevSystemIdByte", getBytes(""))
				.hasFieldOrPropertyWithValue("clientByte", getBytes("", 3))  // ""
				.hasFieldOrPropertyWithValue("systemType", 1)
				.hasFieldOrPropertyWithValue("connectionCounter", 0)
				.hasFieldOrProperty("rootContextIdByte")
				.hasFieldOrProperty("connectionIdByte")
				.hasFieldOrProperty("transIdByte");

		verify(integrationServicesConfiguration).getSapPassportSystemId();
		verify(integrationServicesConfiguration).getSapPassportServiceValue();
		verify(integrationServicesConfiguration).getSapPassportUser();
	}

	@Test
	public void testGetUuidAsBytes()
	{
		final byte[] uuidBytes = service.getUuidAsBytes();
		assertThat(uuidBytes).hasSize(16);
	}

	@Test
	public void testGetUuidAsString()
	{
		final String uuidString = service.getUuidAsString();
		assertThat(uuidString).hasSize(32);
		assertThat(uuidString.toUpperCase()).isEqualTo(uuidString);
	}

	private byte[] getBytes(final String value, final int length)
	{
		final byte[] newArray = new byte[length];
		final byte[] array = value.getBytes(StandardCharsets.UTF_8);

		System.arraycopy(array, 0, newArray, 0, Math.min(array.length, newArray.length));
		return newArray;
	}

	private byte[] getBytes(final String value)
	{
		return value.getBytes(StandardCharsets.UTF_8);
	}
}
