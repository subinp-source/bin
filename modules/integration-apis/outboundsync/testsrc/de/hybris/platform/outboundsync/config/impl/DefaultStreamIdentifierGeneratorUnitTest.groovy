/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.config.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultStreamIdentifierGeneratorUnitTest extends Specification {

	def identifierGenerator = new DefaultStreamIdentifierGenerator()

	@Test
	def "generated name contains channel and integration object item code with _ characters removed to create unique name"() {
		given:
		def itemCode = 'Test_Item'
		def channelCode = "testChannelCode"

		when:
		def name = identifierGenerator.generate(channelWithCode(channelCode), ioItemModel(itemCode))

		then:
		name.contains channelCode
		name.contains 'TestItem'
		name.endsWith 'Stream'
	}

	@Test
	@Unroll
	def "generated name is unique even for channel:#ch1 and item:#item1 vs channel:#ch2 and item:#item2"() {
		expect:
		identifierGenerator.generate(channelWithCode(ch1), ioItemModel(item1)) != identifierGenerator.generate(channelWithCode(ch2), ioItemModel(item2))

		where:
		ch1           | item1  | ch2    | item2
		'DD'          | 'DDD'  | 'DDD'  | 'DD'
		'hah'         | 'ah'   | 'ha'   | 'hah'
		'TestObject'  | 'Item' | 'Test' | 'ObjectItem'
		'Test_Object' | 'Item' | 'Test' | 'Object_Item'
	}

	private OutboundChannelConfigurationModel channelWithCode(channelCode) {
		Stub(OutboundChannelConfigurationModel) {
			getCode() >> channelCode
		}
	}

	private IntegrationObjectItemModel ioItemModel(itemTypeCode) {
		def type = Stub(ComposedTypeModel) {
			getCode() >> itemTypeCode
		}
		Stub(IntegrationObjectItemModel) {
			getType() >> type
		}
	}
}
