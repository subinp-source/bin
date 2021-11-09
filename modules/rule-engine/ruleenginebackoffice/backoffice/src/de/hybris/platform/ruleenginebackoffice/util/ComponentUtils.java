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
package de.hybris.platform.ruleenginebackoffice.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;


/**
 * Contract with utility methods for cockpit components
 */
public interface ComponentUtils
{
	/**
	 * Searches for methods marked with {@link GlobalCockpitEvent} annotation within provided composer and registers them
	 * within global listeners map
	 *
	 * @param composer
	 *           composer to look for global event listeners in
	 * @param component
	 *           cockpit component to be used as a reference for the global listeners mapping
	 */
	void setupGlobalEventListeners(GenericForwardComposer<Component> composer, Component component);

	/**
	 * Unregisters global event listeners bound to the given widget identified by the provided id
	 *
	 * @param widgetId
	 *           id of the widget to remove global event listeners from
	 */
	void removeGlobalEventListeners(String widgetId);
}
