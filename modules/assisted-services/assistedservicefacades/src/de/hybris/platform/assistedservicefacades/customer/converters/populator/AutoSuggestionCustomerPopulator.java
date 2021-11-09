/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicefacades.customer.converters.populator;

import de.hybris.platform.assistedservicefacades.user.data.AutoSuggestionCustomerData;
import de.hybris.platform.assistedserviceservices.AssistedServiceService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Class used for populating AutoSuggestionCustomerData
 *
 */

public class AutoSuggestionCustomerPopulator implements Populator<CustomerModel, AutoSuggestionCustomerData>
{
	private AssistedServiceService assistedServiceService;

	@Override
	public void populate(final CustomerModel customer, final AutoSuggestionCustomerData suggestionData)
	{
		populateCustomerJSON(customer, suggestionData);
		final List<String> cartCodes = getAssistedServiceService().getCartsForCustomer(customer).stream().map(CartModel::getCode)
				.collect(Collectors.toList());
		suggestionData.setCarts(cartCodes);
		populateCustomerJSON(customer, suggestionData);
	}

	protected void populateCustomerJSON(final CustomerModel customer, final AutoSuggestionCustomerData autoSuggestionCustomerData)
	{
		final String cardNumber = customer.getDefaultPaymentInfo() instanceof CreditCardPaymentInfoModel
				? ((CreditCardPaymentInfoModel) customer.getDefaultPaymentInfo()).getNumber()
				: null;
		final String last4Digits = cardNumber == null ? "----"
				: cardNumber.subSequence(cardNumber.length() >= 4 ? (cardNumber.length() - 4) : 0, cardNumber.length()).toString();
		final String formattedCreationDate = customer.getCreationtime() != null
				? new SimpleDateFormat("dd/MM/yyyy").format(customer.getCreationtime())
				: "--/--/----";
		autoSuggestionCustomerData.setEmail(customer.getUid());
		autoSuggestionCustomerData.setDate(formattedCreationDate);
		autoSuggestionCustomerData.setValue(customer.getName());
		autoSuggestionCustomerData.setCard(last4Digits);
	}

	protected AssistedServiceService getAssistedServiceService()
	{
		return assistedServiceService;
	}

	@Required
	public void setAssistedServiceService(final AssistedServiceService assistedServiceService)
	{
		this.assistedServiceService = assistedServiceService;
	}
}
