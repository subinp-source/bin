/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.config.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import org.junit.Test
import spock.lang.Specification

@UnitTest
class JobIdentifierGeneratorUnitTest extends Specification {

	def identifierGenerator = new JobIdentifierGenerator()

	@Test
	def "generate"() {
		given:
		def channelCode = "testChannelCode"
		def channel = Stub(OutboundChannelConfigurationModel)
				{
					getCode() >> channelCode
				}

		expect:
		identifierGenerator.generate(channel) == channelCode + "Job"
	}
}
