/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruledefinitions.conditions;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.configuration.Switch;
import de.hybris.platform.ruleengineservices.configuration.SwitchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultRuleConditionConsumptionSupportTest
{
	private static final String ORDER_ENTRY_RAO_VAR = "orderEntryRaoVariable";
	private static final String AVAILABLE_QUANTITY_VAR = "availableQuantity";

	@Mock
	private SwitchService switchService;
	@Mock
	private RuleCompilerContext context;

	@InjectMocks
	private DefaultRuleConditionConsumptionSupport support;

	@Test
	public void shouldReturnEmptyListWhenConsumptionIsDisabled()
	{
		when(switchService.isEnabled(Switch.CONSUMPTION)).thenReturn(false);
		assertTrue(support.newProductConsumedCondition(context, ORDER_ENTRY_RAO_VAR).isEmpty());
	}

	@Test
	public void shouldReturnConsumedConditionsWhenConsumptionIsEnabled()
	{
		when(switchService.isEnabled(Switch.CONSUMPTION)).thenReturn(true);

		final List<RuleIrCondition> ruleIrConditions = support.newProductConsumedCondition(context, ORDER_ENTRY_RAO_VAR);

		assertThat(ruleIrConditions.get(0), instanceOf(RuleIrAttributeCondition.class));
		final RuleIrAttributeCondition ruleIrAvailableQtyCondition = (RuleIrAttributeCondition) ruleIrConditions.get(0);
		assertEquals(ORDER_ENTRY_RAO_VAR, ruleIrAvailableQtyCondition.getVariable());
		assertEquals(RuleIrAttributeOperator.GREATER_THAN_OR_EQUAL, ruleIrAvailableQtyCondition.getOperator());
		assertEquals(1, ruleIrAvailableQtyCondition.getValue());
		assertEquals(AVAILABLE_QUANTITY_VAR, ruleIrAvailableQtyCondition.getAttribute());
	}
}
