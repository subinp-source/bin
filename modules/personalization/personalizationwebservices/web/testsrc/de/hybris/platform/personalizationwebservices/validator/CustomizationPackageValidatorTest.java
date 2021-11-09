/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationwebservices.validator;

import static de.hybris.platform.personalizationfacades.customization.CustomizationTestUtils.creteCustomizationData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.personalizationfacades.data.SegmentTriggerData;
import de.hybris.platform.webservicescommons.validators.FieldNotEmptyOrTooLongValidator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@UnitTest
public class CustomizationPackageValidatorTest
{
	protected static final String CUSTOMIZATION = "customization";
	protected static final String CUSTOMIZATION_ID = "customization";
	protected static final String CUSTOMIZATION_NAME = "customization";
	protected static final String SEGMENT_ID = "segment0";
	protected static final String VARIATION_ID = "variation";
	protected static final String VARIATION_NAME = "variation";
	protected static final String TRIGGER_ID = "trigger";
	protected static final String FIELD_REQUIRED_AND_NOT_TOO_LONG = "field.requiredAndNotTooLong";
	protected static final String TOO_LONG_NAME = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

	protected CustomizationPackageValidator validator = new CustomizationPackageValidator();
	protected CustomizationData customizationData = creteCustomizationData(CUSTOMIZATION_ID, CUSTOMIZATION_NAME, VARIATION_ID, VARIATION_NAME, TRIGGER_ID, SEGMENT_ID);
	protected Errors errors = new BeanPropertyBindingResult(customizationData, CUSTOMIZATION);

	protected CustomizationDataValidator customizationDataValidator;
	protected VariationDataValidator variationDataValidator;

	@Before
	public void setUp()
	{
		final SegmentTriggerDataValidator triggerDataValidator = new SegmentTriggerDataValidator();
		triggerDataValidator.setSegmentValidator(new SegmentDataValidator());

		final FieldNotEmptyOrTooLongValidator lengthNameValidator = new FieldNotEmptyOrTooLongValidator();
		lengthNameValidator.setFieldPath("name");
		lengthNameValidator.setMaxLength(255);
		final Validator[] validators =
		{ lengthNameValidator };

		variationDataValidator = new VariationDataValidator();
		variationDataValidator.setValidators(validators);
		customizationDataValidator = new CustomizationDataValidator();
		customizationDataValidator.setValidators(validators);

		validator.setTriggerValidator(triggerDataValidator);
		validator.setVariationValidator(variationDataValidator);
		validator.setCustomizationValidator(customizationDataValidator);

		customizationData = creteCustomizationData(CUSTOMIZATION_ID, CUSTOMIZATION_NAME, VARIATION_ID, VARIATION_NAME, TRIGGER_ID, SEGMENT_ID);
		errors = new BeanPropertyBindingResult(customizationData, CUSTOMIZATION);
	}

	@Test
	public void validateCustomizationTest()
	{
		//when
		validator.validate(customizationData, errors);

		//then
		assertFalse(errors.hasErrors());
	}

	@Test
	public void validateCustomizationWithoutNameTest()
	{
		//given
		customizationData.setName(null);

		//when
		validator.validate(customizationData, errors);

		//then
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getErrorCount());
		assertEquals(1, errors.getFieldErrorCount());
		assertEquals(FIELD_REQUIRED_AND_NOT_TOO_LONG, errors.getFieldError().getCode());
		assertEquals("name", errors.getFieldError().getField());
	}

	@Test
	public void validateCustomizationWithTooLongNameTest()
	{
		//given
		customizationData.setName(TOO_LONG_NAME);

		//when
		validator.validate(customizationData, errors);

		//then
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getErrorCount());
		assertEquals(1, errors.getFieldErrorCount());
		assertEquals(FIELD_REQUIRED_AND_NOT_TOO_LONG, errors.getFieldError().getCode());
		assertEquals("name", errors.getFieldError().getField());
	}

	@Test
	public void validateVariationWithoutNameTest()
	{
		//given
		customizationData.getVariations().get(0).setName(null);

		//when
		validator.validate(customizationData, errors);

		//then
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getErrorCount());
		assertEquals(FIELD_REQUIRED_AND_NOT_TOO_LONG, errors.getFieldError().getCode());
		assertEquals("variations[0].name", errors.getFieldError().getField());
	}

	@Test
	public void validateVariationWithTooLongNameTest()
	{
		//given
		customizationData.getVariations().get(0).setName(TOO_LONG_NAME);

		//when
		validator.validate(customizationData, errors);

		//then
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getErrorCount());
		assertEquals(FIELD_REQUIRED_AND_NOT_TOO_LONG, errors.getFieldError().getCode());
		assertEquals("variations[0].name", errors.getFieldError().getField());
	}

	@Test
	public void validateTriggerWithoutSegmentsTest()
	{
		//given
		final SegmentTriggerData segmentTrigger = (SegmentTriggerData) customizationData.getVariations().get(0).getTriggers().get(0);
		segmentTrigger.setSegments(null);

		//when
		validator.validate(customizationData, errors);

		//then
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getErrorCount());
		assertEquals("field.required", errors.getFieldError().getCode());
		assertEquals("variations[0].triggers[0].segments", errors.getFieldError().getField());
	}

	@Test
	public void validateSegmentWithoutCodeTest()
	{
		//given
		final SegmentTriggerData segmentTrigger = (SegmentTriggerData) customizationData.getVariations().get(0).getTriggers().get(0);
		segmentTrigger.getSegments().get(0).setCode(null);

		//when
		validator.validate(customizationData, errors);

		//then
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getErrorCount());
		assertEquals("field.required", errors.getFieldError().getCode());
		assertEquals("variations[0].triggers[0].segments[0].code", errors.getFieldError().getField());
	}
}
