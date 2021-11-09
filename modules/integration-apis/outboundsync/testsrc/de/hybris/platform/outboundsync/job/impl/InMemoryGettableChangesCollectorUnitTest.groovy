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
package de.hybris.platform.outboundsync.job.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.deltadetection.ItemChangeDTO
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class InMemoryGettableChangesCollectorUnitTest extends Specification
{
	def changesCollector = new InMemoryGettableChangesCollector()

	@Test
	@Unroll
	def "collect changes should return #collected when itemChange is #itemChange"()
	{
		expect:
		changesCollector.collect(itemChange) == collected

		where:
		itemChange 			| collected
		null				| false
		Stub(ItemChangeDTO) | true
	}

	@Test
	@Unroll
	def "get changes should have #count items when itemChanges are #itemChanges"()
	{
		given:
		itemChanges.each { change -> changesCollector.collect(change) }

		expect:
		changesCollector.getChanges().size() == count

		where:
		itemChanges 								| count
		[] 											| 0
		[null, Stub(ItemChangeDTO)] 				| 1
		[Stub(ItemChangeDTO), Stub(ItemChangeDTO)] 	| 2
	}
}
