/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressaddon.forms.validation;

import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.AddressValidator;
import de.hybris.platform.addressaddon.forms.ChineseAddressForm;
import de.hybris.platform.addressfacades.address.AddressFacade;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


/**
 * Validator for Chinese address form
 */
@Component("chineseAddressValidator")
public class ChineseAddressValidator extends AddressValidator
{
	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	private static final String[] IGNORE_FIELDS = new String[]
	{ "firstName", "lastName", "townCity", "postcode" }; // ignore the validation annotations defined in AddressForm
	private static final String CN = "CN";

	@Resource(name = "chineseAddressFacade")
	private AddressFacade addressFacade;

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return ChineseAddressForm.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final ChineseAddressForm addressForm = (ChineseAddressForm) object;
		for (final ConstraintViolation<ChineseAddressForm> e : VALIDATOR.validate(addressForm))
		{
			if (!ArrayUtils.contains(IGNORE_FIELDS, e.getPropertyPath().toString()))
			{
				final String msgKey = StringUtils.substringBetween(e.getMessage(), "{", "}");
				errors.rejectValue(e.getPropertyPath().toString(), msgKey);
			}
		}

		final String postcode = addressForm.getPostcode();
		if (StringUtils.isNotBlank(postcode) && !addressFacade.validatePostcode(postcode))
		{
			errors.rejectValue("postcode", "address.post.code.invalid");
		}

		final String regionIso = addressForm.getRegionIso();
		if (StringUtils.isEmpty(regionIso))
		{
			if (CN.equals(addressForm.getCountryIso()))
			{
				errors.rejectValue("regionIso", "address.province.required");
			}
			else
			{
				errors.rejectValue("regionIso", "address.region.required");
			}
		}

		final String cellphone = addressForm.getCellphone();
		if (!errors.hasFieldErrors(ChineseAddressForm.ATTR_CELLPHONE) && addressFacade.isInvalidCellphone(cellphone))
		{
			errors.rejectValue(ChineseAddressForm.ATTR_CELLPHONE, "address.cellphone.invalid");
		}

	}

}
