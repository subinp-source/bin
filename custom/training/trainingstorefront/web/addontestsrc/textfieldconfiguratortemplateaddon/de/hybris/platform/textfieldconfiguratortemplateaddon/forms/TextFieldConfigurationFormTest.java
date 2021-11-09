/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.textfieldconfiguratortemplateaddon.forms;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ConfiguratorType;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


@UnitTest
public class TextFieldConfigurationFormTest
{
	TextFieldConfigurationForm classUnderTest = new TextFieldConfigurationForm();

	@Test
	public void testQuantity()
	{
		final Long quantity = Long.valueOf(1);
		classUnderTest.setQuantity(quantity);
		assertEquals(quantity, classUnderTest.getQuantity());
	}

	@Test
	public void testConfigurationsKeyValueMap()
	{

		final Map<ConfiguratorType, Map<String, String>> configurationsKeyValueMap = new HashMap<ConfiguratorType, Map<String, String>>();
		classUnderTest.setConfigurationsKeyValueMap(configurationsKeyValueMap);
		assertEquals(configurationsKeyValueMap, classUnderTest.getConfigurationsKeyValueMap());
	}
}
