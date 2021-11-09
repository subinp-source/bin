/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.yacceleratorcockpits.ticket.email.context;

import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ticket.email.context.CustomerTicketContext;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;

import org.apache.commons.lang.StringUtils;


public class AcceleratorCustomerTicketContext extends CustomerTicketContext
{
	private static final String PIPE = "|";

	public AcceleratorCustomerTicketContext(final CsTicketModel ticket, final CsTicketEventModel event)
	{
		super(ticket, event);
	}

	@Override
	public String getTo()
	{
		if (getTicket().getCustomer() instanceof CustomerModel)
		{
			String result = StringUtils.EMPTY;
			final CustomerModel ticketCustomer = (CustomerModel) getTicket().getCustomer();
			if (CustomerType.GUEST.equals(ticketCustomer.getType()))
			{
				result = StringUtils.substringAfter(getTicket().getCustomer().getUid(), PIPE);
			}
			else
			{
				result = ticketCustomer.getUid();
			}
			return result;
		}
		return super.getTo();
	}
}
