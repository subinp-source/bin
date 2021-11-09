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
package de.hybris.platform.ruleenginebackoffice.setup.impl;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengine.setup.AbstractRuleEngineSystemSetup;
import de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants;


@SystemSetup(extension = RuleEngineBackofficeConstants.EXTENSIONNAME)
public class RuleEngineBackofficeSystemSetup extends AbstractRuleEngineSystemSetup
{

	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(@SuppressWarnings("unused") final SystemSetupContext context)
	{
		importImpexFile("/ruleenginebackoffice/import/projectdata-validation.impex", true, false);
	}

}
