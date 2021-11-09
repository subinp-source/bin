/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.converters.Populator;

import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import org.apache.commons.lang.StringUtils;


/**
 * Default credit card payment info populator.
 */
public class DefaultCCPaymentInfoDataPopulator implements Populator<Map<String, String>, CCPaymentInfoData>
{
	public static final int EXPIRY_MONTH_END_INDEX = 2;
	public static final int EXPIRY_YEAR_BEGIN_INDEX = 3;
	public static final int EXPIRY_YEAR_END_INDEX = 7;
	
	@Override
	public void populate(final Map<String, String> source, final CCPaymentInfoData target) throws IllegalArgumentException
	{
		validateParameterNotNullStandardMessage("target", source);
		validateParameterNotNullStandardMessage("source", target);

		target.setAccountHolderName(source.get("accountHolderName"));
		target.setCardNumber(source.get("cardNumber"));

		populateCardType(source, target);

		final String expirationDate = source.get("expiryDate");
		if (StringUtils.isNotEmpty(expirationDate))
		{
			// assuming date of format MM/yyyy
			target.setExpiryMonth(expirationDate.substring(0, EXPIRY_MONTH_END_INDEX));
			target.setExpiryYear(expirationDate.substring(EXPIRY_YEAR_BEGIN_INDEX, EXPIRY_YEAR_END_INDEX));
		}

		final AddressData billingAddress = new AddressData();

		populateBillingAddress(billingAddress, source);

		target.setBillingAddress(billingAddress);
	}

	private void populateCardType(final Map<String, String> source, final CCPaymentInfoData target)
	{
		switch (source.get("cardType"))
		{
			case "Visa":
				target.setCardType("visa");
				break;
			case "American Express":
				target.setCardType("amex");
				break;
			case "MasterCard":
				target.setCardType("master");
				break;
			default:
				target.setCardType(source.get("cardType"));
				break;
		}

	}

	protected void populateBillingAddress(final AddressData billingAddress, final Map<String, String> source)
	{
		billingAddress.setFirstName(source.get("firstName"));
		billingAddress.setLastName(source.get("lastName"));
		billingAddress.setTitleCode(source.get("titleCode"));
		billingAddress.setLine1(source.get("addr1"));
		billingAddress.setLine2(source.get("addr2"));
		final CountryData country = new CountryData();
		country.setIsocode(source.get("country"));
		billingAddress.setCountry(country);
		billingAddress.setPostalCode(source.get("postalCode"));
		billingAddress.setTown(source.get("city"));
	}

}
