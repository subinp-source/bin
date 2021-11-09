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
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.test.LocalizationTest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;


@IntegrationTest
public class TextFieldModuleLocalizationTest extends LocalizationTest
{
	private static final Logger LOG = Logger.getLogger(TextFieldModuleLocalizationTest.class);

	protected TextFieldModuleHelper getModuleHelper()
	{
		return new TextFieldModuleHelper();
	}

	@Before
	@Override
	public void setUp() throws ConsistencyCheckException
	{
		final TextFieldModuleHelper helper = getModuleHelper();
		final String moduleExt = helper.getModuleExtensions();
		final String addExt = helper.getAdditionalExtensionsForTypeCodeTest();
		final String ext = StringUtils.isEmpty(addExt) ? moduleExt : moduleExt + TextFieldModuleHelper.EXT_SEPERATOR + addExt;
		prepareTest(ext);
	}

	protected void prepareTest(final String extensionsToCheck) throws ConsistencyCheckException
	{
		System.setProperty("localizationExtensions", extensionsToCheck);
		super.setUp();
		LOG.info("checking extensions: ");
		if (includedExtensions != null)
		{
			for (final String extension : includedExtensions)
			{
				LOG.info(extension);
			}
		}
	}

	@After
	@Override
	public void tearDown() throws ConsistencyCheckException
	{
		System.clearProperty("localizationExtensions");
		super.tearDown();
	}
}
