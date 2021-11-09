/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators.response.AbstractResultPopulator;
import de.hybris.platform.acceleratorservices.payment.data.AuthReplyData;
import de.hybris.platform.acceleratorservices.payment.data.CreateSubscriptionResult;

import java.util.Map;


public class CreateSubscriptionResultPopulator extends AbstractResultPopulator<Map<String, String>, CreateSubscriptionResult>
{
	@Override
	public void populate(final Map<String, String> source, final CreateSubscriptionResult target)
	{
		//Validate parameters and related data
		validateParameterNotNull(source, "Parameter source (Map<String, String>) cannot be null");
		validateParameterNotNull(target, "Parameter target (CreateSubscriptionResult) cannot be null");

		target.setDecision(source.get("decision"));
		target.setDecisionPublicSignature(source.get("decision_publicSignature"));
		target.setReasonCode(getIntegerForString(source.get("reasonCode")));
		target.setRequestId(source.get("requestID"));

		final AuthReplyData authReplyData = new AuthReplyData();
		authReplyData.setCvnDecision(Boolean.TRUE);
		target.setAuthReplyData(authReplyData);
	}
}
