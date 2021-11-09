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

import de.hybris.platform.rulebuilderbackoffice.editors.actionseditor.ActionModel;
import de.hybris.platform.rulebuilderbackoffice.editors.conditionseditor.ConditionModel;

import java.io.Serializable;
import java.util.Map;


/**
 * The interface defines common methods for {@link ConditionModel} and {@link ActionModel}.
 *
 */
public interface RuleItemModel extends Serializable
{
	/**
	 * Returns parameters of the Rule Item.
	 *
	 * @return
	 *         {@link Map} where keys are identifiers of the parameters (returned by {@link ParameterModel#getId()} and the values
	 *         are the ParameterModel itself.
	 */
	Map<String, ParameterModel> getParameters();
}
