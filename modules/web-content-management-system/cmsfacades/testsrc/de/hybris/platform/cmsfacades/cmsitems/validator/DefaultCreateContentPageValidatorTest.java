/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.DEFAULT_PAGE_DOES_NOT_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.DEFAULT_PAGE_LABEL_ALREADY_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_ALREADY_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrors;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.common.validator.impl.DefaultValidationErrors;
import de.hybris.platform.cmsfacades.validator.data.ValidationError;

import java.util.List;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCreateContentPageValidatorTest
{

	private static final String PAGE_UID = "some page uid";
	private static final String PAGE_LABEL = "/some page label";

	@Mock
	private Predicate<String> pageExistsPredicate;

	@Mock
	private Predicate<String> primaryPageWithLabelExistsPredicate;

	@Mock
	private Predicate<Object> cloneContextSameAsActiveCatalogVersionPredicate;

	@Mock
	private ContentPageModel pageToValidate;

	@Mock
	private ValidationErrorsProvider validationErrorsProvider;

	@InjectMocks
	private DefaultCreateContentPageValidator defaultCreateContentPageValidator;

	private final ValidationErrors validationErrors = new DefaultValidationErrors();

	@Before
	public void setUp()
	{
		when(cloneContextSameAsActiveCatalogVersionPredicate.test(pageToValidate)).thenReturn(true);

		when(pageToValidate.getUid()).thenReturn(PAGE_UID);
		when(pageToValidate.getLabel()).thenReturn(PAGE_LABEL);
		when(pageToValidate.getDefaultPage()).thenReturn(true);

		when(pageExistsPredicate.test(PAGE_UID)).thenReturn(false);
		when(primaryPageWithLabelExistsPredicate.test(PAGE_LABEL)).thenReturn(false);

		when(validationErrorsProvider.getCurrentValidationErrors()).thenReturn(validationErrors);
	}

	@Test
	public void givenPageAlreadyExists_WhenValidated_ThenItMustFail()
	{
		// GIVEN
		when(pageExistsPredicate.test(PAGE_UID)).thenReturn(true);

		// WHEN
		defaultCreateContentPageValidator.validate(pageToValidate);

		// THEN
		assertHasError(ContentPageModel.UID, FIELD_ALREADY_EXIST, null);
	}

	@Test
	public void givenPageWithEmptyLabel_WhenValidated_ThenItMustFail()
	{
		// GIVEN
		when(pageToValidate.getLabel()).thenReturn(null);

		// WHEN
		defaultCreateContentPageValidator.validate(pageToValidate);

		// THEN
		assertHasError(ContentPageModel.LABEL, FIELD_REQUIRED, null);
	}

	@Test
	public void givenNewPageIsPrimary_AndAnotherPageWithTheSameLabelExists_WhenValidated_ThenItMustFail()
	{
		// GIVEN
		when(primaryPageWithLabelExistsPredicate.test(PAGE_LABEL)).thenReturn(true);

		// WHEN
		defaultCreateContentPageValidator.validate(pageToValidate);

		// THEN
		assertHasError(ContentPageModel.LABEL, DEFAULT_PAGE_LABEL_ALREADY_EXIST, new Object[]
		{ pageToValidate.getLabel() });
	}

	@Test
	public void givenNewPageIsVariation_AndNoPrimaryExistsForTheGivenLabel_WhenValidated_ThenItMustFail()
	{
		// GIVEN
		when(pageToValidate.getDefaultPage()).thenReturn(false);

		// WHEN
		defaultCreateContentPageValidator.validate(pageToValidate);

		// THEN
		assertHasError(ContentPageModel.LABEL, DEFAULT_PAGE_DOES_NOT_EXIST, new Object[]
		{ pageToValidate.getLabel() });
	}

	@Test
	public void givenValidPage_WhenValidated_ThenItMustPass()
	{
		// WHEN
		defaultCreateContentPageValidator.validate(pageToValidate);

		// THEN
		assertHasNoErrors();
	}

	// ---------------------------------------------------------------------------------------------------
	// Helper Methods
	// ---------------------------------------------------------------------------------------------------
	protected void assertHasNoErrors()
	{
		final List<ValidationError> errors = validationErrors.getValidationErrors();
		assertTrue(errors.isEmpty());
	}

	protected void assertHasError(final String field, final String errorCode, final Object[] errorArgs)
	{
		final List<ValidationError> errors = validationErrors.getValidationErrors();

		assertThat(errors.get(0).getField(), is(field));
		assertThat(errors.get(0).getErrorCode(), is(errorCode));
		assertThat(errors.get(0).getErrorArgs(), is(errorArgs));
	}
}
