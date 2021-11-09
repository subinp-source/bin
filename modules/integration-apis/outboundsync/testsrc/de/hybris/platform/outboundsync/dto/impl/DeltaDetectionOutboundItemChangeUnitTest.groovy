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

package de.hybris.platform.outboundsync.dto.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.deltadetection.ItemChangeDTO
import de.hybris.deltadetection.enums.ChangeType
import de.hybris.platform.outboundsync.dto.OutboundChangeType
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DeltaDetectionOutboundItemChangeUnitTest extends Specification {

	public static final long PK = 123L

	@Test
	def "outbound item return pk after creation"() {
		given:
		def item = Stub(ItemChangeDTO) {
			getItemPK() >> PK
		}
		when:
		def outboundItem = new DeltaDetectionOutboundItemChange(item)

		then:
		outboundItem.getPK() == item.getItemPK()
	}

	@Test
	@Unroll
	def "when ItemChangeDTO has a change type of #originalChangeType outboundItem has change type of #changeType"() {
		given:
		def item = Stub(ItemChangeDTO) {
			getItemPK() >> PK
			getChangeType() >> originalChangeType
		}

		when:
		def outboundItem = new DeltaDetectionOutboundItemChange(item)

		then:
		outboundItem.getChangeType() == changeType

		where:
		originalChangeType  | changeType
		ChangeType.NEW      | OutboundChangeType.CREATED
		ChangeType.MODIFIED | OutboundChangeType.MODIFIED
		ChangeType.DELETED  | OutboundChangeType.DELETED
	}


	@Test
	@Unroll
	def "an exception is thrown when #condition"() {
		when:
		def outboundItem = new DeltaDetectionOutboundItemChange(item)
		outboundItem.getChangeType()

		then:
		thrown(IllegalArgumentException)

		where:
		condition 							| item
		"itemChangeDTO has no change type" 	| itemWithNoChangeType()
		"item is null" 						| null
	}

	def itemWithNoChangeType()
	{
		Stub(ItemChangeDTO) {
			getItemPK() >> PK
			getChangeType() >> null
		}
	}
}
