/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.textfieldconfiguratortemplateocctests;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.test.ExtensionRequirementsNotAwareAboutAddonsTest;
import de.hybris.platform.test.ExtensionRequirementsTest;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;


@IntegrationTest
public class TextFieldModuleExtensionRequirementsTest extends ExtensionRequirementsNotAwareAboutAddonsTest
{
	private static final String REQ_INCLUDED_EXTENSIONS = "reqIncludedExtensions";
	private static final Logger LOG = Logger.getLogger(TextFieldModuleExtensionRequirementsTest.class);


	@BeforeClass
	public static void setUp()
	{
		final TextFieldModuleHelper helper = new TextFieldModuleHelper();
		prepareTest(helper, LOG);
	}

	public static void prepareTest(final TextFieldModuleHelper helper, final Logger LOG)
	{
		final String moduleExtensions = helper.getModuleExtensions();
		System.setProperty(REQ_INCLUDED_EXTENSIONS, moduleExtensions);
		LOG.info("checking extensions: " + moduleExtensions);
		ExtensionRequirementsTest.setUp();
		ExtensionRequirementsNotAwareAboutAddonsTest.setUp();
	}

	@AfterClass
	public static void tearDown()
	{
		System.clearProperty(REQ_INCLUDED_EXTENSIONS);
	}

}
