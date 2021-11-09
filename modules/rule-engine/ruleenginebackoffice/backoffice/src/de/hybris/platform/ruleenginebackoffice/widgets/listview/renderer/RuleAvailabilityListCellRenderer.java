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
package de.hybris.platform.ruleenginebackoffice.widgets.listview.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

import static java.util.stream.Collectors.joining;


public class RuleAvailabilityListCellRenderer<R extends AbstractRuleModel>
		extends AbstractWidgetComponentRenderer<Listcell, ListColumn, R>
{
	@Override
	public void render(final Listcell parent, final ListColumn columnConfiguration, final R rule, final DataType dataType,
			final WidgetInstanceManager widgetInstanceManager)
	{
		final Label label = new Label(getLabelText(rule));

		UITools.modifySClass(parent, "yw-listview-cell-restricted", true);
		UITools.modifySClass(label, "yw-listview-cell-label", true);

		parent.appendChild(label);

		fireComponentRendered(label, parent, columnConfiguration, rule);
		fireComponentRendered(parent, parent, columnConfiguration, rule);
	}

	/**
	 * Finds all the rule modules for the given rule and concatenates their names to obtain a comma separated string
	 * value
	 */
	protected String getLabelText(final AbstractRuleModel rule)
	{
		return rule.getRulesModules().stream().map(AbstractRulesModuleModel::getName).distinct()
				.sorted().collect(joining(", "));
	}
}
