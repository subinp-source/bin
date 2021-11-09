/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patches.actions.data.setup

import de.hybris.platform.core.initialization.SystemSetup
import de.hybris.platform.core.initialization.SystemSetupContext
import de.hybris.platform.patches.AbstractPatchesSystemSetup
import de.hybris.platform.util.SystemSetupUtils

/**
 * Test system setup for executing mocked patches
 */
public class TestPatchesSystemSetup extends AbstractPatchesSystemSetup {
    public void createProjectData() {
        SystemSetupUtils.setInitMethodInHttpSession("INIT")
        super.createProjectData(new SystemSetupContext(null, SystemSetup.Type.PROJECT, SystemSetup.Process.INIT, null))
    }
}
