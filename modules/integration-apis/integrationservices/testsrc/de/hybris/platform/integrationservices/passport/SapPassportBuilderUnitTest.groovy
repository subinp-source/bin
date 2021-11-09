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
package de.hybris.platform.integrationservices.passport

import com.sap.jdsr.passport.DSRPassport
import de.hybris.bootstrap.annotations.UnitTest
import groovy.json.JsonOutput
import org.junit.Test
import spock.lang.Specification

@UnitTest
class SapPassportBuilderUnitTest extends Specification
{
	private static final int VERSION = 4
	private static final int TRACE_FLAG = 10
	private static final String SYSTEM_ID = "someSystemId"
	private static final int SERVICE = 100
	private static final String USER = "someUser"
	private static final String ACTION = "someAction"
	private static final int ACTION_TYPE = 101
	private static final String PREV_SYSTEM_ID = "somePrevSystemId"
	private static final String TRANS_ID = "someTransId"
	private static final String CLIENT_NUMBER = "someClientNumber"
	private static final int SYSTEM_TYPE = 102
	private static final byte[] ROOT_CONTEXT_ID = [1, 2, 3] as byte[]
	private static final byte[] CONNECTION_ID = [4, 5, 6] as byte[]
	private static final int CONNECTION_COUNTER = 103

	private SapPassportBuilder builder = SapPassportBuilder.newSapPassportBuilder()

	def setup()
	{
		builder.withVersion(VERSION)
				.withTraceFlag(TRACE_FLAG)
				.withSystemId(SYSTEM_ID)
				.withService(SERVICE)
				.withUser(USER)
				.withAction(ACTION)
				.withActionType(ACTION_TYPE)
				.withPrevSystemId(PREV_SYSTEM_ID)
				.withTransId(TRANS_ID)
				.withClientNumber(CLIENT_NUMBER)
				.withSystemType(SYSTEM_TYPE)
				.withRootContextId(ROOT_CONTEXT_ID)
				.withConnectionId(CONNECTION_ID)
				.withConnectionCounter(CONNECTION_COUNTER)
	}

	@Test
	def "build"()
	{
		when:
		def expectedPassport = new DSRPassport(
				VERSION,
				TRACE_FLAG,
				SYSTEM_ID,
				SERVICE, USER,
				ACTION,
				ACTION_TYPE,
				PREV_SYSTEM_ID,
				TRANS_ID,
				CLIENT_NUMBER,
				SYSTEM_TYPE,
				ROOT_CONTEXT_ID,
				CONNECTION_ID,
				CONNECTION_COUNTER
		)
		def actualPassport = builder.build()

		then:
		new JsonOutput().toJson(actualPassport) == new JsonOutput().toJson(expectedPassport)
	}

	@Test
	def "Version cannot be null"()
	{
		when:
		builder.withVersion(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "version cannot be null"
	}

	@Test
	def "TraceFlag cannot be null"()
	{
		when:
		builder.withTraceFlag(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "traceFlag cannot be null"
	}

	@Test
	def "SystemId cannot be null"()
	{
		when:
		builder.withSystemId(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "systemId cannot be null"
	}

	@Test
	def "Service cannot be null"()
	{
		when:
		builder.withService(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "service cannot be null"
	}

	@Test
	def "User cannot be null"()
	{
		when:
		builder.withUser(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "user cannot be null"
	}

	@Test
	def "Action cannot be null"()
	{
		when:
		builder.withAction(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "action cannot be null"
	}

	@Test
	def "ActionType cannot be null"()
	{
		when:
		builder.withActionType(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "actionType cannot be null"
	}

	@Test
	def "PrevSystemId cannot be null"()
	{
		when:
		builder.withPrevSystemId(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "prevSystemId cannot be null"
	}

	@Test
	def "TransId cannot be null"()
	{
		when:
		builder.withTransId(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "transId cannot be null"
	}

	@Test
	def "ClientNumber cannot be null"()
	{
		when:
		builder.withClientNumber(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "clientNumber cannot be null"
	}

	@Test
	def "SystemType cannot be null"()
	{
		when:
		builder.withSystemType(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "systemType cannot be null"
	}

	@Test
	def "RootContextId cannot be null"()
	{
		when:
		builder.withRootContextId(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "rootContextId cannot be null"
	}

	@Test
	def "ConnectionId cannot be null"()
	{
		when:
		builder.withConnectionId(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "connectionId cannot be null"
	}

	@Test
	def "ConnectionCounter cannot be null"()
	{
		when:
		builder.withConnectionCounter(null).build()

		then:
		def exception = thrown(IllegalArgumentException)
		exception.getMessage() ==  "connectionCounter cannot be null"
	}
}
