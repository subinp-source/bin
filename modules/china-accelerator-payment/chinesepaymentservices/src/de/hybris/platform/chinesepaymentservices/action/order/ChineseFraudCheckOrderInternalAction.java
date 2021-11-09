/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentservices.action.order;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;

import java.util.HashSet;
import java.util.Set;


/**
 * No need to check credit card information for Chinese customers, so hard code the result.
 */
public class ChineseFraudCheckOrderInternalAction extends AbstractAction<OrderProcessModel>
{
	public enum Transition
	{
		OK, POTENTIAL, FRAUD;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();
			for (final Transition transitions : Transition.values())
			{
				res.add(transitions.toString());
			}
			return res;
		}
	}


	public Transition executeAction()
	{
		return Transition.OK;
	}

	@Override
	public String execute(OrderProcessModel process)
	{
		return executeAction().toString();
	}


	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}




}
