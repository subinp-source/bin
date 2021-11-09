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
package de.hybris.platform.ruleenginebackoffice.renderer;

import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;
import org.zkoss.zk.ui.Component;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPositioned;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Section;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.editorarea.sections.UnboundSectionRenderer;


/**
 * The renderer is responsible for rendering the unbound section on the Adminstration Tab of the Engine Rule
 *
 */
public class EngineRuleUnboundSectionRenderer extends UnboundSectionRenderer
{
	private RuleType ruleType;

	private Map<String, String> ruleTypeAttributeMapping;

	@Override
	public void render(final Component parent, final AbstractSection abstractSectionConfiguration, final Object object,
			final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
	{
		if (object instanceof AbstractRuleEngineRuleModel)
		{
			ruleType = ((AbstractRuleEngineRuleModel) object).getRuleType();
			super.render(parent, abstractSectionConfiguration, object, dataType, widgetInstanceManager);
		}

	}

	@Override
	protected Section getUnboundSection(final WidgetInstanceManager widgetInstanceManager)
	{
		final Section unboundSection = super.getUnboundSection(widgetInstanceManager);

		if (!CollectionUtils.isEmpty(getRuleTypeAttributeMapping()))
		{
			final List<AbstractPositioned> attributeList = new ArrayList(unboundSection.getAttributeOrCustom());

			getRuleTypeAttributeMapping().entrySet().stream().filter(entry -> !RuleType.valueOf(entry.getKey()).equals(ruleType))
					.flatMap(entry -> attributeList.stream().map(attr -> (Attribute) attr)
							.filter(attr -> attr.getQualifier().equalsIgnoreCase(entry.getValue())))
					.forEach(a -> unboundSection.getAttributeOrCustom().remove(a));
		}
		return unboundSection;
	}

	protected Map<String, String> getRuleTypeAttributeMapping()
	{
		return ruleTypeAttributeMapping;
	}

	@Required
	public void setRuleTypeAttributeMapping(final Map<String, String> ruleTypeAttributeMapping)
	{
		this.ruleTypeAttributeMapping = ruleTypeAttributeMapping;
	}
}
