/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_ALREADY_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_ATTACHMENTS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_DOES_NOT_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_TEMPLATE_CODE;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_VERSION_LABEL;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_ATTACHMENTS_ALREADY_IN_WORKFLOW;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_INVALID_ATTACHMENTS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_MISSING_ATTACHMENTS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

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
public class CreateWorkflowValidatorTest
{
	private static final String TEMPLATE_CODE = "some template code";
	private static final String PAGE_UUID_1 = "page uuid 1";
	private static final String PAGE_UUID_2 = "page uuid 2";
	private static final String NON_EXISTENT_PAGE_UUID = "non existent page uuid";
	private static final String DESCRIPTION = "";
	private static final String VERSION_LABEL = "some valid label";
	private static final String DUPLICATE_VERSION_LABEL = "some duplicate version label";

	private Errors errors;
	private CMSWorkflowData cmsWorkflowData;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private CMSAdminSiteService cmsAdminSiteService;

	@Mock
	private BiPredicate<String, Class<?>> itemModelExistsPredicate;

	@Mock
	private BiPredicate<String, Class<?>> itemModelDoesNotExistPredicate;

	@Mock
	private BiPredicate<String, CatalogVersionModel> cmsItemExistsInCatalogVersionPredicate;

	@Mock
	private BiPredicate<String, CatalogVersionModel> cmsItemDoesNotExistInCatalogVersionPredicate;

	@Mock
	private Predicate<List<String>> isAnyItemAlreadyInWorkflowPredicate;

	@Mock
	private BiPredicate<String, String> labelExistsInCMSVersionsPredicate;

	@Mock
	private Predicate<String> cmsWorkflowTemplateExistsPredicate;

	@Mock
	private Predicate<String> cmsWorkflowTemplateDoesNotExistPredicate;

	@InjectMocks
	private CreateWorkflowValidator createWorkflowValidator;

	@Before
	public void setUp()
	{
		cmsWorkflowData = createValidCmsWorkflowData();
		errors = new BeanPropertyBindingResult(cmsWorkflowData, cmsWorkflowData.getClass().getSimpleName());

		when(itemModelExistsPredicate.negate()).thenReturn(itemModelDoesNotExistPredicate);
		when(itemModelDoesNotExistPredicate.test(PAGE_UUID_1, CMSItemModel.class)).thenReturn(false);
		when(itemModelDoesNotExistPredicate.test(PAGE_UUID_2, CMSItemModel.class)).thenReturn(false);
		when(itemModelDoesNotExistPredicate.test(NON_EXISTENT_PAGE_UUID, CMSItemModel.class)).thenReturn(true);

		when(cmsItemExistsInCatalogVersionPredicate.negate()).thenReturn(cmsItemDoesNotExistInCatalogVersionPredicate);
		when(cmsItemDoesNotExistInCatalogVersionPredicate.test(PAGE_UUID_1, catalogVersion)).thenReturn(false);
		when(cmsItemDoesNotExistInCatalogVersionPredicate.test(PAGE_UUID_2, catalogVersion)).thenReturn(false);

		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);

		when(labelExistsInCMSVersionsPredicate.test(eq(PAGE_UUID_1), any())).thenReturn(false);
		when(labelExistsInCMSVersionsPredicate.test(PAGE_UUID_2, VERSION_LABEL)).thenReturn(false);
		when(labelExistsInCMSVersionsPredicate.test(PAGE_UUID_2, DUPLICATE_VERSION_LABEL)).thenReturn(true);

		when(cmsWorkflowTemplateExistsPredicate.negate()).thenReturn(cmsWorkflowTemplateDoesNotExistPredicate);
		when(cmsWorkflowTemplateDoesNotExistPredicate.test(TEMPLATE_CODE)).thenReturn(false);

		when(isAnyItemAlreadyInWorkflowPredicate.test(anyList())).thenReturn(false);
	}

	@Test
	public void givenMissingTemplateCode_WhenValidated_ThenItFails()
	{
		// GIVEN
		cmsWorkflowData.setTemplateCode(null);

		// WHEN
		createWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(FIELD_REQUIRED, FIELD_TEMPLATE_CODE);
	}

	@Test
	public void givenInvalidTemplateCode_WhenValidated_ThenItFails()
	{
		// GIVEN
		when(cmsWorkflowTemplateDoesNotExistPredicate.test(TEMPLATE_CODE)).thenReturn(true);

		// WHEN
		createWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(FIELD_DOES_NOT_EXIST, FIELD_TEMPLATE_CODE);
	}

	@Test
	public void givenMissingAttachments_WhenValidated_ThenItFails()
	{
		// GIVEN
		cmsWorkflowData.setAttachments(null);

		// WHEN
		createWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(FIELD_REQUIRED, FIELD_ATTACHMENTS);

	}

	@Test
	public void givenEmptyAttachmentsList_WhenValidated_ThenItFails()
	{
		// GIVEN
		cmsWorkflowData.setAttachments(Collections.EMPTY_LIST);

		// WHEN
		createWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(WORKFLOW_MISSING_ATTACHMENTS, FIELD_ATTACHMENTS);
	}

	@Test
	public void givenAttachmentOutsideCurrentCatalogVersion_WhenValidated_ThenItFails()
	{
		// GIVEN
		when(cmsItemDoesNotExistInCatalogVersionPredicate.test(PAGE_UUID_2, catalogVersion)).thenReturn(true);

		// WHEN
		createWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(WORKFLOW_INVALID_ATTACHMENTS, FIELD_ATTACHMENTS);
	}

	@Test
	public void givenAttachmentIsAlreadyInAnotherWorkflow_WhenValidated_ThenItFails()
	{
		// GIVEN
		when(isAnyItemAlreadyInWorkflowPredicate.test(cmsWorkflowData.getAttachments())).thenReturn(true);

		// WHEN
		createWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(WORKFLOW_ATTACHMENTS_ALREADY_IN_WORKFLOW, FIELD_ATTACHMENTS);
	}

	@Test
	public void givenCreateVersionIsSelectedAndNoVersionLabelIsProvided_WhenValidated_ThenItFails()
	{
		// GIVEN
		cmsWorkflowData.setCreateVersion(true);

		// WHEN
		createWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(FIELD_REQUIRED, FIELD_VERSION_LABEL);
	}

	@Test
	public void givenCreateVersionIsSelectedAndVersionLabelAlreadyExistsForAnAttachment_WhenValidated_ThenItFails()
	{
		// GIVEN
		setCreateVersion(cmsWorkflowData, DUPLICATE_VERSION_LABEL);

		// WHEN
		createWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasError(FIELD_ALREADY_EXIST, FIELD_VERSION_LABEL);
	}

	@Test
	public void givenValidPayloadWithNoCreateVersionFlag_WhenValidated_ThenItPasses()
	{
		// WHEN
		createWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasNoErrors();
	}

	@Test
	public void givenValidPayloadWithCreateVersionFlag_WhenValidated_ThenItPasses()
	{
		// GIVEN
		setCreateVersion(cmsWorkflowData, VERSION_LABEL);

		// WHEN
		createWorkflowValidator.validate(cmsWorkflowData, errors);

		// THEN
		assertHasNoErrors();
	}

	protected CMSWorkflowData createValidCmsWorkflowData()
	{
		final CMSWorkflowData data = new CMSWorkflowData();
		data.setTemplateCode(TEMPLATE_CODE);
		data.setAttachments(Arrays.asList(PAGE_UUID_1, PAGE_UUID_2));
		data.setDescription(DESCRIPTION);
		data.setCreateVersion(false);

		return data;
	}

	protected void setCreateVersion(final CMSWorkflowData workflowData, final String versionLabel)
	{
		workflowData.setCreateVersion(true);
		workflowData.setVersionLabel(versionLabel);
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
