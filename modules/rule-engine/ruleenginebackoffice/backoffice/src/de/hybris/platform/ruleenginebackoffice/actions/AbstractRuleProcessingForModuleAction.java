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
package de.hybris.platform.ruleenginebackoffice.actions;

import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

import java.util.List;

import org.zkoss.zul.Window;

import com.hybris.cockpitng.actions.ActionContext;


/**
 * AbstractRuleProcessingForModuleAction is the abstract class for the common functionality of rule processing
 * actions which could perform in 2 ways: as a set of items and as a single item.
 */
public abstract class AbstractRuleProcessingForModuleAction<I, O> extends AbstractInteractiveAction<I, O>
{
	private static final String RULES_TO_PROCESS_CTX_ATTRIBUTE_NAME = "rulesToProcess";

	protected abstract List<AbstractRuleModel> getRulesToProcess(final ActionContext<I> context);

	@Override
	protected void addDialogWindowAttribute(final ActionContext<I> context, final Window window)
	{
		window.setAttribute(getRulesToProcessCtxAttributeName(), getRulesToProcess(context));
	}

	protected String getRulesToProcessCtxAttributeName()
	{
		return RULES_TO_PROCESS_CTX_ATTRIBUTE_NAME;
	}
}
