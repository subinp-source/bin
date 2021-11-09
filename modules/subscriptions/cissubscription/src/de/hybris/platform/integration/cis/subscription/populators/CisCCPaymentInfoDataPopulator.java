/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.converters.Populator;

import org.apache.commons.lang.StringUtils;

import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.subscription.model.CisPaymentMethod;
import com.hybris.cis.api.subscription.model.CisPaymentMethodResult;
import org.springframework.http.ResponseEntity;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates a {@link CCPaymentInfoData} from a {@link ResponseEntity}.
 */
public class CisCCPaymentInfoDataPopulator implements Populator<ResponseEntity, CCPaymentInfoData>
{
	public static final int PAD_SIZE = 2;
	
	@Override
	public void populate(final ResponseEntity source, final CCPaymentInfoData target) throws IllegalArgumentException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		if (source.getBody() instanceof CisPaymentMethodResult)
		{
			final CisPaymentMethod cisPaymentMethod = ((CisPaymentMethodResult) source.getBody()).getPaymentMethod();

			if (cisPaymentMethod != null)
			{
				target.setAccountHolderName(cisPaymentMethod.getCardHolder());
				target.setCardNumber(cisPaymentMethod.getCcNumber());
				target.setSubscriptionId(cisPaymentMethod.getMerchantPaymentMethodId());
				target.setSaved(true);

				populateCardType(cisPaymentMethod.getCardType(), target);

				target.setExpiryMonth(StringUtils.leftPad(Integer.toString(cisPaymentMethod.getExpirationMonth()), PAD_SIZE, "0"));
				target.setExpiryYear(Integer.toString(cisPaymentMethod.getExpirationYear()));

				final AddressData billingAddress = new AddressData();

				populateBillingAddress(billingAddress, cisPaymentMethod.getBillingAddress());

				target.setBillingAddress(billingAddress);
			}
		}
	}

	protected void populateCardType(final String source, final CCPaymentInfoData target)
	{
		switch (source)
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
				target.setCardType(source);
				break;
		}

	}

	protected void populateBillingAddress(final AddressData billingAddress, final CisAddress source)
	{
		billingAddress.setTitleCode(source.getTitle());
		billingAddress.setFirstName(source.getFirstName());
		billingAddress.setLastName(source.getLastName());
		billingAddress.setLine1(source.getAddressLine1());
		billingAddress.setLine2(source.getAddressLine2());
		final CountryData country = new CountryData();
		country.setIsocode(source.getCountry());
		billingAddress.setCountry(country);
		billingAddress.setPostalCode(source.getZipCode());
		billingAddress.setTown(source.getCity());
	}
}
