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

import static java.util.Objects.nonNull;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;

import com.hybris.cockpitng.actions.ActionContext;

import org.zkoss.zul.Window;


/**
 * Action to synchronize a rules module with a user-specified one.
 */
public class RulesModuleSyncAction extends AbstractInteractiveAction<AbstractRulesModuleModel, Object>
{
	private static final String DEFAULT_DIALOG_TEMPLATE = "/rulesmodulesync.zul";
	private static final String TITLE_RULECOMPILEACTION = "title.rulesmodulesyncaction";

	@Override
	protected String getDialogTemplate(final ActionContext<AbstractRulesModuleModel> context)
	{
		return DEFAULT_DIALOG_TEMPLATE;
	}

	@Override
	protected String getDialogTitle(final ActionContext<AbstractRulesModuleModel> context)
	{
		return TITLE_RULECOMPILEACTION;
	}

	@Override
	protected void addDialogWindowAttribute(final ActionContext<AbstractRulesModuleModel> context, final Window window)
	{
		window.setAttribute("sourceModule", context.getData());
	}

	@Override
	public boolean canPerform(final ActionContext context)
	{
		boolean canSynchronize = false;
		if (context.getData() instanceof AbstractRulesModuleModel)
		{
			final AbstractRulesModuleModel data = (AbstractRulesModuleModel) context.getData();
			canSynchronize = nonNull(data) && nonNull(data.getRuleType());
			if(canSynchronize && data instanceof DroolsKIEModuleModel)
			{
				canSynchronize = isNotEmpty(((DroolsKIEModuleModel)data).getKieBases());
			}
		}
		return canSynchronize;
	}
}

