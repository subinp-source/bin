/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_ATTACHMENTS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_STATUSES;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_INVALID_ATTACHMENT;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_INVALID_STATUS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cronjob.enums.CronJobStatus;

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
public class FindWorkflowValidatorTest
{
	private static final String VALID_UUID = "valid-cmsitem-uuid";
	private static final String INVALID = "INVALID";

	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private BiPredicate<String, CatalogVersionModel> cmsItemExistsInCatalogVersionPredicate;
	@Mock
	private BiPredicate<String, CatalogVersionModel> cmsItemExistsInCatalogVersionPredicateNegate;

	@InjectMocks
	private FindWorkflowValidator validator;

	private CMSWorkflowData workflowData;
	private Errors errors;

	@Before
	public void setUp()
	{
		workflowData = new CMSWorkflowData();
		errors = new BeanPropertyBindingResult(workflowData, CMSWorkflowData.class.getSimpleName());

		when(cmsItemExistsInCatalogVersionPredicate.negate()).thenReturn(cmsItemExistsInCatalogVersionPredicateNegate);
		when(cmsItemExistsInCatalogVersionPredicateNegate.test(any(), any())).thenReturn(false);
	}

	@Test
	public void shouldPassValidation()
	{
		workflowData.setAttachments(Arrays.asList(VALID_UUID));
		workflowData.setStatuses(Arrays.asList(CronJobStatus.ABORTED.getCode()));

		validator.validate(workflowData, errors);

		assertFalse(errors.hasErrors());
		verify(cmsAdminSiteService).getActiveCatalogVersion();
		verify(cmsItemExistsInCatalogVersionPredicate).negate();
	}

	@Test
	public void shouldFailInvalidStatus()
	{
		workflowData.setStatuses(Arrays.asList(INVALID));

		validator.validate(workflowData, errors);

		assertHasError(WORKFLOW_INVALID_STATUS, FIELD_STATUSES);
	}

	@Test
	public void shouldFailInvalidUuid()
	{
		when(cmsItemExistsInCatalogVersionPredicateNegate.test(any(), any())).thenReturn(true);
		workflowData.setAttachments(Arrays.asList(INVALID));

		validator.validate(workflowData, errors);

		assertHasError(WORKFLOW_INVALID_ATTACHMENT, FIELD_ATTACHMENTS);
	}

	protected void assertHasError(final String expectedErrorCode, final String fieldName)
	{
		assertThat(errors.getFieldErrorCount(), equalTo(1));
		assertThat(errors.getFieldErrors().get(0).getCode(), equalTo(expectedErrorCode));
		assertThat(errors.getFieldErrors().get(0).getField(), equalTo(fieldName));
	}
}
