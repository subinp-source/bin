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

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.CockpitAction;


/**
 * InteractiveAction is the interface defining methods for the common functionality of interactive actions.
 */
public interface InteractiveAction<I, O> extends CockpitAction<I, O>
{
	/**
	 * Shows alert dialog with title, message and button Ok.
	 *
	 * @param context
	 *           ActionContext to get localized labels by the codes
	 * @param title
	 *           title code
	 * @param message
	 *           message code
	 */
	void showAlertDialog(ActionContext<I> context, String title, String message);

	/**
	 * Sends data object into specified socket.
	 *
	 * @param socketId
	 *           Socket identifier to send data
	 * @param data
	 *           data object to send
	 */
	void sendOutputDataToSocket(String socketId, Object data);
}
