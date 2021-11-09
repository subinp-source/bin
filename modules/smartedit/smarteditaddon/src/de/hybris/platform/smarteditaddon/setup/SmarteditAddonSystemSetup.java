/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditaddon.setup;

import java.util.Collections;
import java.util.List;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.smarteditaddon.constants.SmarteditaddonConstants;


@SystemSetup(extension = SmarteditaddonConstants.EXTENSIONNAME)
public class SmarteditAddonSystemSetup extends AbstractSystemSetup
{

	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		return Collections.emptyList();
	}

	@SystemSetup(type = Type.ESSENTIAL, process = Process.UPDATE)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/smarteditaddon/import/common/user-groups.impex");
	}

}
