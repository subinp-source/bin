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
package de.hybris.platform.outboundsync.activator.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.deltadetection.ChangeDetectionService
import de.hybris.deltadetection.ItemChangeDTO
import de.hybris.deltadetection.enums.ChangeType
import de.hybris.platform.outboundsync.dto.OutboundItemDTO
import de.hybris.platform.outboundsync.dto.impl.DeltaDetectionOutboundItemChange
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DeltaDetectionOutboundItemConsumerUnitTest extends Specification {

	def deltaDetectionConsumer = new DeltaDetectionOutboundItemConsumer()

	def changeDetectionService = Mock(ChangeDetectionService)

	def setup() {
		deltaDetectionConsumer.setChangeDetectionService(changeDetectionService)
	}

	@Test
	def "change detection is invoked with the corresponding ItemChangeDTO"() {
		given:
		def itemChangeDTO = new ItemChangeDTO(123, new Date(), ChangeType.NEW, "info", "Product", "stream1")
		def deltaDetectionOutboundItem = new DeltaDetectionOutboundItemChange(itemChangeDTO)
		def outboundItemDTO = Stub(OutboundItemDTO) {
			getItem() >> deltaDetectionOutboundItem
		}

		when:
		deltaDetectionConsumer.consume(outboundItemDTO)

		then:
		1 * changeDetectionService.consumeChanges(_) >> { args ->
			assert args[0].contains(itemChangeDTO)
		}
	}

}
