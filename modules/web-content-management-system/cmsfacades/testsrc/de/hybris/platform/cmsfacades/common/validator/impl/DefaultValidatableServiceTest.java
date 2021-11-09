/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.validator.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrors;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.validator.data.ValidationError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultValidatableServiceTest
{
	@Mock
	private ValidationErrorsProvider validationErrorsProvider;
	
	@InjectMocks
	private DefaultValidatableService cmsItemValidator;
	
	@Mock
	private ValidationErrors validationErrors;

	@Before
	public void setup()
	{
		when(validationErrorsProvider.getCurrentValidationErrors()).thenReturn(validationErrors);
		when(validationErrorsProvider.initializeValidationErrors()).thenReturn(validationErrors);
	}

	@Test
	public void testWhenSupplierSucceedsAndNoErrorsAreFound_shouldReturnMap()
	{
		final Map<String, Object> outputMap = new HashMap<>();
		final Map<String, Object> apply = cmsItemValidator.execute(() -> outputMap);

		assertThat(apply, is(outputMap));
		verify(validationErrors).getValidationErrors();
	}


	@Test(expected = ValidationException.class)
	public void testWhenSupplierSucceedsAndErrorsAreFound_shouldReturnMap()
	{
		final List<ValidationError> errors = Arrays.asList(new ValidationError());
		when(validationErrors.getValidationErrors()).thenReturn(errors);
		
		final Map<String, Object> outputMap = new HashMap<>();
		cmsItemValidator.execute(() -> outputMap);
	}

}
