/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.validation.validators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.validation.annotations.RegExp;

import javax.validation.ConstraintValidatorContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

@UnitTest
public class RegExpValidatorTest
{
    private static final String CORRECT_REGEXP = ".*";
    private static final String INCORRECT_REGEXP = "[";

    protected RegExpValidator regExpValidator = new RegExpValidator();

    @Mock
    protected ConstraintValidatorContext validatorContext;

    @Mock
    protected RegExp regExpAnnotation;

	@Before
    public void init()
	{
        MockitoAnnotations.initMocks(this);
        when(regExpAnnotation.notEmpty()).thenReturn(false);
        regExpValidator.initialize(regExpAnnotation);
	}

    @Test
    public void testIsValid()
    {
        //when
        final boolean isValid = regExpValidator.isValid(CORRECT_REGEXP,validatorContext);

        //then
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidForIncorrectRegexp()
    {
        //when
        final boolean isValid = regExpValidator.isValid(INCORRECT_REGEXP,validatorContext);

        //then
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidForNull()
    {
        //when
        final boolean isValid = regExpValidator.isValid(null,validatorContext);

        //then
        Assert.assertTrue(isValid);
    }

    @Test
	public void testIsValidForEmptyRegexp()
    {
        //when
        final boolean isValid = regExpValidator.isValid("",validatorContext);

        //then
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidForBlankRegexp()
    {
        //when
        final boolean isValid = regExpValidator.isValid("   ",validatorContext);

        //then
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidForNullWhenNotEmptyIsTrue()
    {
        //given
        when(regExpAnnotation.notEmpty()).thenReturn(true);
        regExpValidator.initialize(regExpAnnotation);

        //when
        final boolean isValid = regExpValidator.isValid(null,validatorContext);

        //then
        Assert.assertFalse(isValid);
    }


    @Test
    public void testIsValidForEmptyRegexpWhenNotEmptyIsTrue()
    {
        //given
        when(regExpAnnotation.notEmpty()).thenReturn(true);
        regExpValidator.initialize(regExpAnnotation);

        //when
        final boolean isValid = regExpValidator.isValid("",validatorContext);

        //then
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidForBlankRegexpWhenNotEmptyIsTrue()
    {
        //given
        when(regExpAnnotation.notEmpty()).thenReturn(true);
        regExpValidator.initialize(regExpAnnotation);

        //when
        final boolean isValid = regExpValidator.isValid("    ",validatorContext);

        //then
        Assert.assertFalse(isValid);
    }
}
