/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;


@IntegrationTest
@ContextConfiguration(locations =
{ "classpath:/b2bapprovalprocess-spring-test.xml" })
public class DefaultB2BApprovalProcessLookUpStrategyIntegrationTest extends ServicelayerTest
{
	@Resource
	private DefaultB2BApprovalProcessLookUpStrategy b2bApprovalProcessLookUpStrategy;

	@Test
	public void testGetProcesses() throws Exception
	{
		// For now the baseStore value doesn't matter
		final Map<String, String> processes = b2bApprovalProcessLookUpStrategy.getProcesses(null);
		Assert.assertNotNull(processes);
		Assert.assertEquals(1, processes.size());
		Assert.assertEquals("Escalation Approval with Merchant Check", processes.get("accApproval"));
	}
}
