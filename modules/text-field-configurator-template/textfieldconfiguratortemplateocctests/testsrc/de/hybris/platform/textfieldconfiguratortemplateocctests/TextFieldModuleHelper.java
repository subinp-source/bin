/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.textfieldconfiguratortemplateocctests;

import org.apache.log4j.Logger;


public class TextFieldModuleHelper
{
	private static final Logger LOG = Logger.getLogger(TextFieldModuleHelper.class);

	public static final String EXT_SEPERATOR = ",";
	public static final String TEAMTIGER_MODULE_EXTENSIONS_KEY = "teamtiger.module.extensions";

	public String getModuleExtensions()
	{
		LOG.info(String.format("system property: '%s' = '%s'", TEAMTIGER_MODULE_EXTENSIONS_KEY,
				System.getProperty(TEAMTIGER_MODULE_EXTENSIONS_KEY)));
		return System.getProperty(TEAMTIGER_MODULE_EXTENSIONS_KEY, getDefaultExtensions());
	}

	public String getAdditionalExtensionsForTypeCodeTest()
	{
		final StringBuilder builder = new StringBuilder();
		//builder.append("catalog");//.append(EXT_SEPERATOR);
		return builder.toString();
	}

	protected String getDefaultExtensions()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("textfieldconfiguratortemplateaddon").append(EXT_SEPERATOR);
		builder.append("textfieldconfiguratortemplatebackoffice").append(EXT_SEPERATOR);
		builder.append("textfieldconfiguratortemplatefacades").append(EXT_SEPERATOR);
		builder.append("textfieldconfiguratortemplateocc").append(EXT_SEPERATOR);
		builder.append("textfieldconfiguratortemplateoccaddon").append(EXT_SEPERATOR);
		builder.append("textfieldconfiguratortemplateocctest").append(EXT_SEPERATOR);
		builder.append("textfieldconfiguratortemplateocctests").append(EXT_SEPERATOR);
		builder.append("textfieldconfiguratortemplateservices");//.append(EXT_SEPERATOR);
		return builder.toString();
	}
}
