/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleocctests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.configurablebundleocctests.controllers.ConfigurableBundleControllerTest
import de.hybris.platform.configurablebundleocctests.controllers.ProductControllerTest
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses([ConfigurableBundleControllerTest, ProductControllerTest])
@IntegrationTest
class AllBundlesOCCGroovyTests {

    @BeforeClass
    static void setUpClass() {
        TestSetupStandalone.loadData()
        TestSetupStandalone.startServer()
    }

    @AfterClass
    static void tearDown() {
        TestSetupStandalone.stopServer()
        TestSetupStandalone.cleanData();
    }

    @Test
    static void testing() {
        //dummy test class
    }
}
