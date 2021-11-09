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
package de.hybris.platform.droolsruleengineservices.compiler.impl;

import com.google.common.collect.Maps;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleGeneratorContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import de.hybris.platform.ruleengineservices.util.DroolsStringUtils;
import org.junit.Before;

import static org.assertj.core.api.Assertions.assertThat;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultDroolsRuleValueFormatterTest
{
	private DefaultDroolsRuleValueFormatter formatter = new DefaultDroolsRuleValueFormatter();

	@Mock
	private DroolsRuleGeneratorContext droolsRuleGeneratorContext;
	private DroolsStringUtils droolsStringUtils;

	@Before
	public void setUp()
	{
		droolsStringUtils = new DroolsStringUtils();
		formatter.setDroolsStringUtils(droolsStringUtils);
		formatter.initFormatters();
	}

	@Test
	public void shouldConsiderEmptyMapAsNullValue()
	{
		assertThat(formatter.formatValue(droolsRuleGeneratorContext, Maps.newHashMap())).isEqualTo("null");
	}

	@Test
	public void shouldConsiderEmptyCollectionAsNullValue()
	{
		assertThat(formatter.formatValue(droolsRuleGeneratorContext, Collections.emptyList())).isEqualTo("null");
	}

	@Test
	public void testEncodingForStringTypeValue()
	{
		assertThat(formatter.formatValue(droolsRuleGeneratorContext, "testen\"coding")).isEqualTo("\"testen\\u0022coding\"");
		assertThat(formatter.formatValue(droolsRuleGeneratorContext, "testen\'coding")).isEqualTo("\"testen\\u0027coding\"");
	}

	@Test
	public void testEncodingForCharacterTypeValue()
	{
		assertThat(formatter.formatValue(droolsRuleGeneratorContext, '\141')).isEqualTo("'\141'");
	}
}
