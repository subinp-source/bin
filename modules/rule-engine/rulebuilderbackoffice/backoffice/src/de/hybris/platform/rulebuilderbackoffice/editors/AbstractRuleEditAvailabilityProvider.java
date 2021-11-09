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
package de.hybris.platform.rulebuilderbackoffice.editors;

import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditAvailabilityProvider;


/**
 * Default implementation of {@link EditAvailabilityProvider} for {@link AbstractRuleModel}
 *
 */
public class AbstractRuleEditAvailabilityProvider implements EditAvailabilityProvider<AbstractRuleModel>
{
	private Predicate<AbstractRuleModel> ruleAllowedToEditPredicate;

	@Override
	public boolean isAllowedToEdit(final DataAttribute attribute, final AbstractRuleModel ruleInstance)
	{
		return getRuleAllowedToEditPredicate().test(ruleInstance);
	}

	protected Predicate<AbstractRuleModel> getRuleAllowedToEditPredicate()
	{
		return ruleAllowedToEditPredicate;
	}

	@Required
	public void setRuleAllowedToEditPredicate(final Predicate<AbstractRuleModel> ruleAllowedToEditPredicate)
	{
		this.ruleAllowedToEditPredicate = ruleAllowedToEditPredicate;
	}
}
