/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package org.drools.core.phreak;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;

import org.drools.core.common.TupleSets;
import org.drools.core.reteoo.BetaMemory;
import org.drools.core.reteoo.RightTuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Tests a custom patch for ECP-5550 (infinite loop in RuleNetworkEvaluator.doUpdatesReorderRightMemory() ensuring that
 * a rule evaluation throws a runtime exception after a configurable timeout has been exceeded
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RuleNetworkEvaluatorUnitTest
{

	@InjectMocks
	private RuleNetworkEvaluator evaluator;

	@Mock
	private BetaMemory bm;

	@Mock
	private RightTuple tuple;

	@Mock
	private TupleSets tupleSets;

	private long oldTimeout;

	@Before
	public void setup()
	{
		oldTimeout = RuleNetworkEvaluator.timeout;
	}

	@After
	public void teardown()
	{
		RuleNetworkEvaluator.timeout = oldTimeout;
	}

	@Test(expected = RuntimeException.class, timeout = 5000)
	public void testDetectInfiniteLoopInDoUpdatesReorderRightMemory()
	{
		RuleNetworkEvaluator.timeout = 3l;

		// we set a single tuple set with itself as stagedNext causing an infinite loop
		// ensuring that we throw a runtime exception
		when(tuple.getStagedNext()).thenReturn(tuple);
		when(tupleSets.getUpdateFirst()).thenReturn(tuple);

		RuleNetworkEvaluator.doUpdatesReorderRightMemory(bm, tupleSets);

	}
}
