/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.chineseprofileoccaddon.validator;

import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.Collection;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates the email language iso code.
 */
public class EmailLanguageValidator implements Validator
{

	private final StoreSessionFacade storeSessionFacade;

	public EmailLanguageValidator(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return String.class == (clazz);
	}


	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final String emailLanguageIso = (String) obj;
		Assert.notNull(errors, "Errors object must not be null.");

		final Collection<LanguageData> languages = getLanguages();

		if (!languages.stream().anyMatch(language -> language.getIsocode().equals(emailLanguageIso)))
		{
			errors.reject("emailLanguage.invalid", "The email language ISO code is not found.");
		}

	}

	protected Collection<LanguageData> getLanguages()
	{
		return storeSessionFacade.getAllLanguages();
	}

}
