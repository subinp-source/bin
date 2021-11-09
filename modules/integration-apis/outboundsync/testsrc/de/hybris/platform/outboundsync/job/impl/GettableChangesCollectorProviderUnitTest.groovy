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
import de.hybris.platform.outboundsync.job.GettableChangesCollector
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class GettableChangesCollectorProviderUnitTest extends Specification
{
	def provider = new GettableChangesCollectorProvider()

	@Test
	@Unroll
	def "get #collectorClassName results in #expectedCollectorClass"()
	{
		given:
		provider.setCollectorClass collectorClassName

		expect:
		expectedCollectorClass.isInstance provider.getCollector()

		where:
		collectorClassName | expectedCollectorClass
		TestGettableChangesCollector.getName() 				| TestGettableChangesCollector
		"non.existing.collector" 							| InMemoryGettableChangesCollector
		Integer.getName() 									| InMemoryGettableChangesCollector
		ExceptionalTestGettableChangesCollector.getName() 	| InMemoryGettableChangesCollector
	}

	static class TestGettableChangesCollector implements GettableChangesCollector
	{

		@Override
		List<ItemChangeDTO> getChanges()
		{
			return null
		}

		@Override
		boolean collect(final ItemChangeDTO change)
		{
			return false
		}

		@Override
		void finish()
		{
		}
	}

	static class ExceptionalTestGettableChangesCollector extends TestGettableChangesCollector
	{
		ExceptionalTestGettableChangesCollector()
		{
			throw new InstantiationException("Testing instantiation exception")
		}
	}
}
