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
package de.hybris.platform.ruleenginebackoffice.constants;

/**
 * Global class for all RuleEngineBackoffice constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings("squid:S1214")
public final class RuleEngineBackofficeConstants extends GeneratedRuleEngineBackofficeConstants
{
	private RuleEngineBackofficeConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
	public interface NotificationSource
	{
		String MESSAGE_SOURCE = EXTENSIONNAME + "-ruleComposer";

		interface EventType
		{
			String EXCEPTION = "Exception";
			String TRIGGER = "Trigger";
		}

		interface CreateFromTemplate
		{
			String MESSAGE_SOURCE = EXTENSIONNAME + "-createFromTemplate";

			interface EventType
			{
				String CREATE = "Create";
			}
		}

		interface Clone
		{
			String MESSAGE_SOURCE = EXTENSIONNAME + "-ruleClone";

			interface EventType
			{
				String CLONE = "Clone";
			}
		}

	}
}
