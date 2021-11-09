/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests.test.groovy.webservicetests.v2

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.b2bocctests.setup.TestSetupUtils
import de.hybris.platform.b2bocctests.test.groovy.webservicetests.v2.controllers.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite.class)
@Suite.SuiteClasses([B2BOrdersControllerReplenishmentPostTest, B2BCostCentersControllerTest, OrderApprovalPermissionsControllerTest, OrderApprovalPermissionTypesControllerTest, BudgetManagementControllerTest, OrgUnitsControllerTest, OrderApprovalsControllerTest, OrgUnitUserGroupsControllerTest, B2BOrdersControllerReplenishmentTest, OrgCustomerManagementControllerTest, B2BUsersControllerTest, B2BProductsControllerTest, B2BOrdersControllerTest])
@IntegrationTest
class AllB2BOCCSpockTests {

	@BeforeClass
	static void setUpClass() {
		TestSetupUtils.loadExtensionDataInJunit()
		TestSetupUtils.startServer()
	}

	@AfterClass
	static void tearDown() {
		TestSetupUtils.stopServer()
		TestSetupUtils.cleanData()
	}

	@Test
	static void testing() {
		//dummy test class
	}
}
