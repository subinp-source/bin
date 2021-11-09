/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators.response.AbstractResultPopulator;
import de.hybris.platform.acceleratorservices.payment.data.PaymentInfoData;
import de.hybris.platform.acceleratorservices.payment.cybersource.enums.CardTypeEnum;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class PaymentInfoDataPopulator extends AbstractResultPopulator<CreditCardPaymentInfoModel, PaymentInfoData>
{
	@Override
	public void populate(final CreditCardPaymentInfoModel source, final PaymentInfoData target) throws ConversionException
	{
		//We may not have any existing credit card details
		if (source == null)
		{
			return;
		}

		validateParameterNotNull(target, "Parameter [PaymentInfoData] target cannot be null");

		target.setCardAccountNumber(source.getNumber());
		final CardTypeEnum cardType = CardTypeEnum.valueOf(source.getType().name().toLowerCase());
		if (cardType != null)
		{
			target.setCardCardType(cardType.getStringValue());
		}
		target.setCardExpirationMonth(getIntegerForString(source.getValidToMonth()));
		target.setCardExpirationYear(getIntegerForString(source.getValidToYear()));
		target.setCardIssueNumber(String.valueOf(source.getIssueNumber()));
		target.setCardStartMonth(source.getValidFromMonth());
		target.setCardStartYear(source.getValidFromYear());
		target.setCardAccountHolderName(source.getCcOwner());
	}
}
