/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cmsfacades.common.service.RestrictionAwareService;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.rendering.suppliers.page.RenderingPageModelSupplier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;



@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPageRenderingServiceTest
{
	private final String INVALID_RENDERING_ATTRIBUTE = "invalidRenderingAttribute";
	private final String VALID_PAGE_TYPE_CODE = "validPageTypeCode";
	private final String VALID_LABEL_OR_ID = "validLabelOrId";
	private final String VALID_CODE = "validCode";

	@InjectMocks
	private DefaultPageRenderingService pageRenderingService;

	@Mock
	private FacadeValidationService facadeValidationService;

	@Mock
	private RenderingPageModelSupplier renderingPageModelSupplier;
	@Mock
	private RestrictionData restrictionData;
	@Mock
	private AbstractPageModel abstractPageModel;
	@Mock
	private AbstractPageData abstractPageData;
	@Mock
	private RestrictionAwareService restrictionAwareService;
	@Mock
	private CMSDataFactory cmsDataFactory;
	@Mock
	private Errors errors;
	@Mock
	private FieldError fieldError;
	@Mock
	private List<RenderingPageModelSupplier> renderingPageModelSuppliers;
	@Mock
	private Predicate<String> predicate;

	@Before
	public void setup()
	{
		when(renderingPageModelSupplier.getConstrainedBy()).thenReturn(predicate);
		when(predicate.test(any())).thenReturn(true);
		renderingPageModelSuppliers = Arrays.asList(renderingPageModelSupplier);
		pageRenderingService.setRenderingPageModelSuppliers(renderingPageModelSuppliers);
	}

	@Test(expected = ValidationException.class)
	public void shouldThrowExceptionIfAnyAttributeIsWrong() throws CMSItemNotFoundException
	{
		// GIVEN
		doThrow(new ValidationException(errors)).when(facadeValidationService).validate(any(), any());

		// WHEN
		pageRenderingService.getPageRenderingData(INVALID_RENDERING_ATTRIBUTE, INVALID_RENDERING_ATTRIBUTE,
				INVALID_RENDERING_ATTRIBUTE);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldThrowExceptionIfInvalidPageLabelOrId() throws CMSItemNotFoundException
	{
		// GIVEN
		doThrow(new ValidationException(errors)).when(facadeValidationService).validate(any(), any());
		when(errors.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
		when(fieldError.getField()).thenReturn("pageLabelOrId");
		when(fieldError.getCode()).thenReturn(CmsfacadesConstants.INVALID_PAGE_LABEL_OR_ID);

		// WHEN
		pageRenderingService.getPageRenderingData(VALID_PAGE_TYPE_CODE, INVALID_RENDERING_ATTRIBUTE, VALID_CODE);
	}

	@Test
	public void shouldReturnAbstractPageData() throws CMSItemNotFoundException
	{
		// GIVEN
		doNothing().when(facadeValidationService).validate(any(), any());
		when(cmsDataFactory.createRestrictionData()).thenReturn(restrictionData);
		when(renderingPageModelSupplier.getRestrictionData(any())).thenReturn(Optional.of(restrictionData));
		when(renderingPageModelSupplier.getPageModel(any())).thenReturn(Optional.of(abstractPageModel));
		when(restrictionAwareService.execute(any(), any())).thenReturn(abstractPageData);

		// WHEN
		final AbstractPageData pageData = pageRenderingService.getPageRenderingData(VALID_PAGE_TYPE_CODE, VALID_LABEL_OR_ID,
				VALID_CODE);

		// THEN
		Assert.assertThat(pageData, equalTo(abstractPageData));

	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldThrowExceptionIfPageNotFound() throws CMSItemNotFoundException
	{
		// GIVEN
		doNothing().when(facadeValidationService).validate(any(), any());
		when(cmsDataFactory.createRestrictionData()).thenReturn(restrictionData);
		when(renderingPageModelSupplier.getRestrictionData(any())).thenReturn(Optional.of(restrictionData));
		when(renderingPageModelSupplier.getPageModel(any())).thenReturn(Optional.empty());
		when(restrictionAwareService.execute(any(), any())).thenReturn(abstractPageData);

		// WHEN
		pageRenderingService.getPageRenderingData(VALID_PAGE_TYPE_CODE, VALID_LABEL_OR_ID, VALID_CODE);
	}
}
