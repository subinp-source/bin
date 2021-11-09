/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cmsitems.validators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrors;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.common.validator.impl.DefaultValidationErrors;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Locale;

import static de.hybris.platform.acceleratorfacades.constants.AcceleratorFacadesConstants.FIELD_NOT_EMAIL;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED_L10N;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultEmailPageValidatorTest
{
	private ValidationErrors validationErrors;

	@Mock
	private ValidationErrorsProvider validationErrorsProvider;

	@Mock
	private LanguageFacade languageFacade;

	@InjectMocks
	private DefaultEmailPageValidator validator;

	@Mock
	private EmailPageModel emailPageModel;

	@Before
	public void setup()
	{
		validationErrors = new DefaultValidationErrors();

		LanguageData en = new LanguageData();
		en.setIsocode("en");
		en.setRequired(true);

		LanguageData de = new LanguageData();
		de.setIsocode("de");
		de.setRequired(false);

		when(validationErrorsProvider.getCurrentValidationErrors()).thenReturn(validationErrors);
		when(languageFacade.getLanguages()).thenReturn(Arrays.asList(en, de));
	}

	@Test
	public void shouldHaveNoErrorsWhenAllFieldsAreValid()
	{
		when(emailPageModel.getFromName(new Locale("en"))).thenReturn("John Smith");
		when(emailPageModel.getFromEmail(new Locale("en"))).thenReturn("john.smith@example.com");

		validator.validate(emailPageModel);

		assertThat(validationErrors.getValidationErrors(), empty());
	}

	@Test
	public void shouldHaveOneErrorWhenEmailFieldIsInvalid()
	{
		when(emailPageModel.getFromName(new Locale("en"))).thenReturn("John Smith");
		when(emailPageModel.getFromEmail(new Locale("en"))).thenReturn("johnsmith");

		validator.validate(emailPageModel);

		assertThat(validationErrors.getValidationErrors(), hasSize(1));
		assertThat(validationErrors.getValidationErrors().get(0).getErrorCode(), is(FIELD_NOT_EMAIL));
		assertThat(validationErrors.getValidationErrors().get(0).getLanguage(), is("en"));
		assertThat(validationErrors.getValidationErrors().get(0).getField(), is(EmailPageModel.FROMEMAIL));
	}

	@Test
	public void shouldHaveTwoErrorsWhenAllFieldsAreEmpty()
	{
		when(emailPageModel.getFromName(new Locale("en"))).thenReturn("");
		when(emailPageModel.getFromEmail(new Locale("en"))).thenReturn(null);

		validator.validate(emailPageModel);

		assertThat(validationErrors.getValidationErrors(), hasSize(2));
		assertThat(validationErrors.getValidationErrors().get(0).getErrorCode(), is(FIELD_REQUIRED_L10N));
		assertThat(validationErrors.getValidationErrors().get(0).getLanguage(), is("en"));
		assertThat(validationErrors.getValidationErrors().get(0).getField(), is(EmailPageModel.FROMNAME));
		assertThat(validationErrors.getValidationErrors().get(1).getErrorCode(), is(FIELD_REQUIRED_L10N));
		assertThat(validationErrors.getValidationErrors().get(1).getLanguage(), is("en"));
		assertThat(validationErrors.getValidationErrors().get(1).getField(), is(EmailPageModel.FROMEMAIL));
	}

	@Test
	public void showThrowErrorForNonRequiredLocalField()
	{
		when(emailPageModel.getFromName(new Locale("en"))).thenReturn("John Smith");
		when(emailPageModel.getFromEmail(new Locale("en"))).thenReturn("john.smith@example.com");
		when(emailPageModel.getFromEmail(new Locale("de"))).thenReturn("Jonz.Smiz");

		validator.validate(emailPageModel);

		assertThat(validationErrors.getValidationErrors(), hasSize(1));
		assertThat(validationErrors.getValidationErrors().get(0).getErrorCode(), is(FIELD_NOT_EMAIL));
		assertThat(validationErrors.getValidationErrors().get(0).getLanguage(), is("de"));
		assertThat(validationErrors.getValidationErrors().get(0).getField(), is(EmailPageModel.FROMEMAIL));
	}
}
