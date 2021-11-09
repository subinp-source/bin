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
package de.hybris.platform.promotionenginebackoffice.actions;

import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleTemplateModel;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


@UnitTest
public class RuleCreatePromotionFromTemplateActionTest extends AbstractActionUnitTest<RuleCreatePromotionFromTemplateAction>
{
	@InjectMocks
	private RuleCreatePromotionFromTemplateAction action;

	@Mock
	private PromotionSourceRuleTemplateModel ruleTemplateModel;

	@Override
	public RuleCreatePromotionFromTemplateAction getActionInstance()
	{
		return action;
	}

	@Before
	public void setUp()
	{
		CockpitTestUtil.mockZkEnvironment();
	}

	@Test
	public void testTemplateNullCannotPerform()
	{
		final ActionContext<PromotionSourceRuleTemplateModel> context = new ActionContext<PromotionSourceRuleTemplateModel>(null,
				null, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
		assertFalse(getActionInstance().canPerform(context));
	}


}
