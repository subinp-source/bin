/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2b.B2BIntegrationTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultB2BApprovalProcessServiceIntegrationTest extends ServicelayerTest
{

	@Resource
	private SessionService sessionService;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	@Resource
	private DefaultB2BApprovalProcessService b2bApprovalProcessService;


	@Before
	public void before() throws Exception
	{
		B2BIntegrationTest.loadTestData();
		importCsv("/b2bapprovalprocess/test/b2borganizations.csv", "UTF-8");
	}
	@Test
	public void testGetProcesses() throws Exception
	{
		// For now the baseStore value doesn't matter
		final Map<String, String> processes = b2bApprovalProcessService.getProcesses(null);
		Assert.assertNotNull(processes);
		Assert.assertEquals(1, processes.size());
		Assert.assertEquals("Escalation Approval with Merchant Check", processes.get("accApproval"));
	}

	@Test
	public void testGetApprovalProcessCode() throws Exception
	{
		final String userId = "IC CEO";
		String approvalCode = null;
		login(userId);

		final B2BUnitModel unit = b2bUnitService.getUnitForUid("IC");
		Assert.assertNotNull(unit);
		approvalCode = b2bApprovalProcessService.getApprovalProcessCodeForUnit(unit);
		Assert.assertNull(approvalCode);

		unit.setApprovalProcessCode("simpleapproval");
		modelService.save(unit);


		approvalCode = b2bApprovalProcessService.getApprovalProcessCodeForUnit(unit);
		Assert.assertNotNull(approvalCode);
		final B2BUnitModel unitSales = b2bUnitService.getUnitForUid("IC Sales");
		Assert.assertNotNull(unitSales);
		approvalCode = b2bApprovalProcessService.getApprovalProcessCodeForUnit(unitSales);
		Assert.assertNotNull(approvalCode);

		unit.setApprovalProcessCode(null);
		modelService.save(unit);
		approvalCode = b2bApprovalProcessService.getApprovalProcessCodeForUnit(unitSales);
		Assert.assertNull(approvalCode);


	}

	protected B2BCustomerModel login(final String userId)
	{
		final B2BCustomerModel user = userService.getUserForUID(userId, B2BCustomerModel.class);
		org.junit.Assert.assertNotNull(userId + " user is null", user);
		userService.setCurrentUser(user);
		b2bUnitService.updateBranchInSession(sessionService.getCurrentSession(), user);
		return user;
	}

}
