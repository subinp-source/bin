/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.mock;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BEmailService;
import de.hybris.platform.core.model.order.OrderModel;
import javax.mail.internet.InternetAddress;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.junit.Ignore;


@Ignore
public class MockB2BEmailService extends DefaultB2BEmailService
{
	private static final Logger LOG = Logger.getLogger(MockB2BEmailService.class);

	@Override
	public void sendEmail(final HtmlEmail email) throws EmailException
	{
		if (LOG.isInfoEnabled())
		{
			LOG.info(String.format("Sending email %s", email));
		}
	}

	@Override
	public HtmlEmail createOrderApprovalEmail(final String emailTemplateCode, final OrderModel order, final B2BCustomerModel user,
			final InternetAddress from, final String subject) throws EmailException
	{
		return new HtmlEmail();
	}

	@Override
	public HtmlEmail createOrderRejectionEmail(final String emailTemplateCode, final OrderModel order,
			final B2BCustomerModel user, final InternetAddress from, final String subject) throws EmailException
	{
		return new HtmlEmail();
	}
}
