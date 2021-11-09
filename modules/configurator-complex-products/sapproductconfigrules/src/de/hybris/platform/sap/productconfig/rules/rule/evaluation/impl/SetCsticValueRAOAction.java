/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.rules.rule.evaluation.impl;

import de.hybris.platform.ruleengineservices.rao.ProcessStep;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.sap.productconfig.rules.rao.CsticRAO;
import de.hybris.platform.sap.productconfig.rules.rao.CsticValueRAO;
import de.hybris.platform.sap.productconfig.rules.rao.action.SetCsticValueRAO;

import java.util.Map;


/**
 * Encapsulates logic of setting characteristic value as rule action.
 */
public class SetCsticValueRAOAction extends ProductConfigAbstractRAOAction
{


	@Override
	public boolean performActionInternal(final RuleActionContext context)
	{
		final Map<String, Object> parameters = context.getParameters();
		logRuleData(context, parameters, CSTIC_NAME, CSTIC_VALUE);
		boolean processed = false;

		if (validateProcessStep(context, parameters, ProcessStep.CREATE_DEFAULT_CONFIGURATION))
		{
			final CsticRAO csticRao = createCsticRAO(parameters);
			final CsticValueRAO valueRao = createCsticValueRAO(parameters);

			final SetCsticValueRAO setCsticValueRao = new SetCsticValueRAO();
			setCsticValueRao.setAppliedToObject(csticRao);
			setCsticValueRao.setValueNameToSet(valueRao);

			updateContext(context, setCsticValueRao);
			processed = true;
		}
		return processed;
	}

	@Override
	protected String prepareActionLogText(final RuleActionContext context, final Map<String, Object> parameters)
	{
		final String csticName = (String) parameters.get(CSTIC_NAME);
		final String csticValue = (String) parameters.get(CSTIC_VALUE);
		return "Hence skipping setting value " + csticValue + " for cstic " + csticName + ".";
	}
}
