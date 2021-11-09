/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.validators.classification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelSingleMediaValidator;


@RunWith(MockitoJUnitRunner.class)
public class ExcelMediaClassificationValidatorTest
{

	@Mock
	private TypeService typeService;

	@Mock
	private ExcelSingleMediaValidator excelSingleMediaValidator;

	@InjectMocks
	ExcelMediaClassificationValidator excelMediaClassificationValidator;

	@Before
	public void setup()
	{
		excelMediaClassificationValidator.setSingleMediaValidators(Collections.singletonList(excelSingleMediaValidator));
	}

	@Test
	public void shouldHandleRequestWhenAttributeIsMedia()
	{
		// given
		final ExcelClassificationAttribute attribute = mock(ExcelClassificationAttribute.class);
		final ClassAttributeAssignmentModel classAttributeAssignmentModel = mock(ClassAttributeAssignmentModel.class);
		final ComposedTypeModel mediaComposedType = mock(ComposedTypeModel.class);
		final ImportParameters importParameters = mock(ImportParameters.class);
		given(importParameters.isCellValueNotBlank()).willReturn(true);
		given(mediaComposedType.getCode()).willReturn(MediaModel._TYPECODE);
		given(attribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
		given(classAttributeAssignmentModel.getReferenceType()).willReturn(mediaComposedType);
		given(typeService.isAssignableFrom(MediaModel._TYPECODE, MediaModel._TYPECODE)).willReturn(true);

		// when
		final boolean result = excelMediaClassificationValidator.canHandleSingle(attribute, importParameters);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldInvokeAllMediaValidatorsAndCollectValidationErrors()
	{
		// given
		final HashMap<String, Object> ctx = new HashMap<>();
		final ImportParameters importParameters = mock(ImportParameters.class);
		final ExcelSingleMediaValidator firstSingleMediaValidator = mock(ExcelSingleMediaValidator.class);
		final ExcelSingleMediaValidator secondSingleMediaValidator = mock(ExcelSingleMediaValidator.class);
		final Map<String, String> firstParams = new HashMap<>();
		final Map<String, String> secondParams = new HashMap<>();
		final ExcelClassificationAttribute excelClassificationAttribute = mock(ExcelClassificationAttribute.class);
		final ValidationMessage firstValidationMessage = new ValidationMessage("first");
		final ValidationMessage secondValidationMessage = new ValidationMessage("second");
		final ValidationMessage thirdValidationMessage = new ValidationMessage("third");
		excelMediaClassificationValidator.setSingleMediaValidators(Arrays.asList(firstSingleMediaValidator, secondSingleMediaValidator));
		given(importParameters.getMultiValueParameters()).willReturn(Arrays.asList(firstParams, secondParams));
		given(firstSingleMediaValidator.validateSingleValue(eq(ctx), same(firstParams))).willReturn(Collections.emptyList());
		given(firstSingleMediaValidator.validateSingleValue(eq(ctx), same(secondParams)))
				.willReturn(Collections.singletonList(firstValidationMessage));
		given(secondSingleMediaValidator.validateSingleValue(eq(ctx), same(firstParams)))
				.willReturn(Collections.singletonList(secondValidationMessage));
		given(secondSingleMediaValidator.validateSingleValue(eq(ctx), same(secondParams)))
				.willReturn(Collections.singletonList(thirdValidationMessage));

		// when
		final ExcelValidationResult validationResult = excelMediaClassificationValidator.validate(excelClassificationAttribute, importParameters, ctx);

		// then
		assertThat(validationResult.getValidationErrors()).hasSize(3);
		assertThat(validationResult.getValidationErrors()).containsExactlyInAnyOrder(firstValidationMessage,
				secondValidationMessage, thirdValidationMessage);
	}
}
