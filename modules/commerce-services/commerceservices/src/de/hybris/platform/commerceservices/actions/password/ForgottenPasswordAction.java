/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.actions.password;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.model.process.ForgottenPasswordProcessModel;
import de.hybris.platform.commerceservices.user.UserMatchingService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.webservicescommons.util.YSanitizer;
import org.apache.log4j.Logger;

import static de.hybris.platform.core.model.security.PrincipalModel.UID;


public class ForgottenPasswordAction extends AbstractSimpleDecisionAction<ForgottenPasswordProcessModel>
{

	private static final Logger LOG = Logger.getLogger(ForgottenPasswordAction.class);

	private UserMatchingService userMatchingService;
	private SessionService sessionService;
	private CustomerAccountService customerAccountService;

	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public UserMatchingService getUserMatchingService()
	{
		return userMatchingService;
	}

	public void setUserMatchingService(final UserMatchingService userMatchingService)
	{
		this.userMatchingService = userMatchingService;
	}

	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	@Override
	public Transition executeAction(final ForgottenPasswordProcessModel forgottenPasswordProcessModel)
	{

		if (getProcessParameterHelper().containsParameter(forgottenPasswordProcessModel, UID))
		{
			final String customerUid = (String) getProcessParameterHelper()
					.getProcessParameterByName(UID, forgottenPasswordProcessModel.getContextParameters()).getValue();
			getSessionService().getCurrentSession().setAttribute("currentSite", forgottenPasswordProcessModel.getSite());

			try
			{
				final CustomerModel customerModel = getUserMatchingService().getUserByProperty(customerUid, CustomerModel.class);
				getCustomerAccountService().forgottenPassword(customerModel);
				return Transition.OK;
			}
			catch (final UnknownIdentifierException uie)
			{
				LOG.warn(String.format("User with unique property: %s does not exist in the database.",
						YSanitizer.sanitize(customerUid)));
				return Transition.NOK;
			}
		}
		else
		{
			LOG.warn("The field [uid] cannot be empty");
			return Transition.NOK;
		}
	}
}
