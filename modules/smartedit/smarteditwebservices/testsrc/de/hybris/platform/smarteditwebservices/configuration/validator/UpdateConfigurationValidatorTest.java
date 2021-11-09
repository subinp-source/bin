/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration.validator;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.dto.UpdateConfigurationDto;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@UnitTest
public class UpdateConfigurationValidatorTest
{

	private UpdateConfigurationValidator validator = new UpdateConfigurationValidator();


	@Test
	public void testValidConfigurationData()
	{
		final UpdateConfigurationDto data = new UpdateConfigurationDto();
		data.setUid("key");
		data.setKey("key");
		data.setValue("1");
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertEquals(0, errors.getErrorCount());
	}


	@Test
	public void testInvalidConfigurationData()
	{
		final UpdateConfigurationDto data = new UpdateConfigurationDto();
		data.setUid("KEY");
		data.setKey("key");
		data.setValue("1");
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertEquals(1, errors.getErrorCount());
	}

}
