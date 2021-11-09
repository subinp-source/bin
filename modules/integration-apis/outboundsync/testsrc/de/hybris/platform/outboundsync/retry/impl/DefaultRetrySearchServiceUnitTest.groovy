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

package de.hybris.platform.outboundsync.retry.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.outboundsync.dto.OutboundItemDTOGroup
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.outboundsync.model.OutboundSyncRetryModel
import de.hybris.platform.outboundsync.retry.SyncRetryNotFoundException
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultRetrySearchServiceUnitTest extends Specification {
	public static final String CHANNEL_CODE = 'test-channel'
	public static final Long ROOT_ITEM_PK = 123L
	def service = new DefaultRetrySearchService()
	def flexSearch = Stub(FlexibleSearchService)
	OutboundItemDTOGroup outboundItemDTOGroup

	def setup() {
		service.setFlexibleSearchService(flexSearch)
		outboundItemDTOGroup = itemDTOGroup()
	}

	@Test
	def "retry already exists"() {
		given:
		retryForItemExists()

		when:
		def retry = service.findRetry(outboundItemDTOGroup)

		then:
		retry != null
	}

	@Test
	def "retry does not exist"() {
		given:
		retryDoesNotExist()

		when:
		service.findRetry(outboundItemDTOGroup)

		then:
		def e = thrown(SyncRetryNotFoundException)
		e.getItemPk() == ROOT_ITEM_PK
		e.getChannelConfigurationCode() == CHANNEL_CODE
	}

	def retryDoesNotExist() {
		flexSearch.getModelByExample(_) >> { throw new ModelNotFoundException('test exception')}
	}

	def retryForItemExists() {
		flexSearch.getModelByExample(_) >> Stub(OutboundSyncRetryModel)
	}

	def itemDTOGroup() {
		Stub(OutboundItemDTOGroup) {
			getChannelConfiguration() >> Stub(OutboundChannelConfigurationModel) {
				getCode() >> CHANNEL_CODE
			}
			getRootItemPk() >> ROOT_ITEM_PK
		}
	}
}
