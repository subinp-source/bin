/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_ATTACHMENTS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_INVALID_ATTACHMENTS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;
import java.util.function.BiPredicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class EditWorkflowValidatorTest
{

	private static final String STATUS = "someStatus";
	private static final String WORKFLOW_CODE = "workflowCode";
	private static final String OTHER_WORKFLOW_CODE = "otherWorkflowCode";
	private static final String PAGE_UUID_1 = "page uuid 1";
	private static final String PAGE_UUID_2 = "page uuid 2";

	@InjectMocks
	private EditWorkflowValidator editWorkflowValidator;

	@Mock
	private CMSWorkflowService cmsWorkflowService;

	@Mock
	private WorkflowService workflowService;

	@Mock
	private CMSWorkflowData cmsWorkflowData;

	@Mock
	private WorkflowModel workflowModel;

	@Mock
	private BiPredicate<String, CatalogVersionModel> cmsItemExistsInCatalogVersionPredicate;

	@Mock
	private BiPredicate<String, CatalogVersionModel> cmsItemDoesNotExistInCatalogVersionPredicate;

	@Mock
	private CMSAdminSiteService cmsAdminSiteService;

	@Mock
	private CatalogVersionModel catalogVersion;

	private Errors errors;

	@Before
	public void setUp()
	{
		errors = new BeanPropertyBindingResult(cmsWorkflowData, cmsWorkflowData.getClass().getSimpleName());
		when(cmsWorkflowService.getWorkflowForCode(any())).thenReturn(workflowModel);

		when(cmsWorkflowData.getOriginalWorkflowCode()).thenReturn(WORKFLOW_CODE);
		when(cmsWorkflowData.getWorkflowCode()).thenReturn(WORKFLOW_CODE);
		when(cmsWorkflowData.getAttachments()).thenReturn(Arrays.asList(PAGE_UUID_1, PAGE_UUID_2));
		when(workflowService.isPlanned(workflowModel)).thenReturn(true);

		when(cmsItemExistsInCatalogVersionPredicate.negate()).thenReturn(cmsItemDoesNotExistInCatalogVersionPredicate);
		when(cmsItemDoesNotExistInCatalogVersionPredicate.test(PAGE_UUID_1, catalogVersion)).thenReturn(false);
		when(cmsItemDoesNotExistInCatalogVersionPredicate.test(PAGE_UUID_2, catalogVersion)).thenReturn(false);

		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);

	}

	@Test
	public void givenAMismatchedWorkflowCodePassed_WhenEditWorkflowValidatorIsCalled_ThenItThrowsValidationError()
	{
		// WHEN
		when(cmsWorkflowData.getWorkflowCode()).thenReturn(OTHER_WORKFLOW_CODE);
		editWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(CmsfacadesConstants.WORKFLOW_CODE_MISMATCH, CmsfacadesConstants.FIELD_WORKFLOW_CODE);
	}

	@Test
	public void givenNewWorkflowStatusIsPassed_WhenEditWorkflowValidatorIsCalled_ThenItThrowsValidationError()
	{

		// GIVEN
		when(cmsWorkflowData.getStatus()).thenReturn(CronJobStatus.ABORTED.name());
		when(workflowModel.getStatus()).thenReturn(CronJobStatus.PAUSED);

		// WHEN
		editWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(CmsfacadesConstants.WORKFLOW_CANNOT_UPDATE_STATUS, CmsfacadesConstants.FIELD_WORKFLOW_STATUS);
	}

	@Test
	public void givenNewAttachementsArePassedWhenWorkflowStarted_WhenEditWorkflowValidatorIsCalled_ThenItThrowsValidationError()
	{
		// GIVEN
		when(workflowModel.getStatus()).thenReturn(CronJobStatus.PAUSED);
		when(workflowService.isPlanned(workflowModel)).thenReturn(false);

		// WHEN
		editWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(CmsfacadesConstants.WORKFLOW_CANNOT_UPDATE_ATTACHMENTS, CmsfacadesConstants.FIELD_ATTACHMENTS);
	}

	@Test
	public void givenAttachmentOutsideCurrentCatalogVersion_WhenEditWorkflowValidatorIsCalled_ThenItThrowsValidationError()
	{
		// GIVEN
		when(cmsItemDoesNotExistInCatalogVersionPredicate.test(PAGE_UUID_2, catalogVersion)).thenReturn(true);

		// WHEN
		editWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(WORKFLOW_INVALID_ATTACHMENTS, FIELD_ATTACHMENTS);
	}

	@Test
	public void givenValidPayload_WhenEditWorkflowValidatorIsCalled_ThenItPasses()
	{
		// WHEN
		editWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasNoErrors();
	}

	protected void assertHasError(final String expectedErrorCode, final String fieldName)
	{
		assertThat(errors.getFieldErrorCount(), is(1));
		assertThat(errors.getFieldErrors().get(0).getCode(), is(expectedErrorCode));
		assertThat(errors.getFieldErrors().get(0).getField(), is(fieldName));
	}

	protected void assertHasNoErrors()
	{
		assertFalse(errors.hasErrors());
	}

}
