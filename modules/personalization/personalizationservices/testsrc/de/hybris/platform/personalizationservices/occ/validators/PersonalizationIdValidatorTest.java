/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.occ.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
public class PersonalizationIdValidatorTest
{
	protected PersonalizationIdValidator validator = new PersonalizationIdValidator();

	@Test
	public void validateNullTest()
	{
		//given
		final Errors errors = createErrors(null, "null");

		//when
		validator.validate(null, errors);

		//then
		assertTrue(errors.hasErrors());
	}

	@Test
	public void validateEmptyStringTest()
	{
		//given
		final String emptyString = "";
		final Errors errors = createErrors(emptyString, "emptyString");

		//when
		validator.validate(emptyString, errors);

		//then
		assertTrue(errors.hasErrors());
	}

	@Test
	public void validateCorrectValueTest()
	{
		//given
		final String correctUUID = "0c724ea0-1495-427e-b641-c0a5a7efe9a7";
		final Errors errors = createErrors(correctUUID, "correctUUID");

		//when
		validator.validate(correctUUID, errors);

		//then
		assertFalse(errors.hasErrors());
	}

	@Test
	public void validateIncorrectValueTest()
	{
		//given
		final String incorrectValue = "incorrectValue";
		final Errors errors = createErrors(incorrectValue, "incorrectValue");

		//when
		validator.validate(incorrectValue, errors);

		//then
		assertTrue(errors.hasErrors());
	}

	private Errors createErrors(final Object object, final String name)
	{
		return new BeanPropertyBindingResult(object, name);
	}

}
