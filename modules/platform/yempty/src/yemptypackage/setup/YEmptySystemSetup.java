/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package yemptypackage.setup;

import static yemptypackage.constants.YEmptyConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import yemptypackage.constants.YEmptyConstants;
import yemptypackage.service.YEmptyService;


@SystemSetup(extension = YEmptyConstants.EXTENSIONNAME)
public class YEmptySystemSetup
{
	private final YEmptyService yemptyService;

	public YEmptySystemSetup(final YEmptyService yemptyService)
	{
		this.yemptyService = yemptyService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		yemptyService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return YEmptySystemSetup.class.getResourceAsStream("/yempty/sap-hybris-platform.png");
	}
}
