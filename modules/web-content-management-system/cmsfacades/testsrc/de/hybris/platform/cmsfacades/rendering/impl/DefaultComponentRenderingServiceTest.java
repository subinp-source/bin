/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cmsfacades.common.service.RestrictionAwareService;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.rendering.cache.RenderingCacheService;
import de.hybris.platform.cmsfacades.rendering.visibility.RenderingVisibilityService;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;



@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultComponentRenderingServiceTest
{
	public static final String COMPONENT_UID = "test-component-uid";
	public static final String CATALOG_CODE = "catalog-electronics";
	public static final String CATEGORY_CODE = "category-camera";
	public static final String PRODUCT_CODE = "product-camera-battery";

	@InjectMocks
	private DefaultComponentRenderingService componentRenderingService;

	@Mock
	private FacadeValidationService facadeValidationService;
	@Mock
	private CMSDataFactory cmsDataFactory;
	@Mock
	private CMSComponentService cmsComponentService;
	@Mock
	private Converter<AbstractCMSComponentModel, AbstractCMSComponentData> componentRenderingConverter;
	@Mock
	private RestrictionAwareService restrictionAwareService;
	@Mock
	private RenderingVisibilityService renderingVisibilityService;

	@Mock
	private Errors errors;
	@Mock
	private AbstractCMSComponentModel componentModel;
	@Mock
	private AbstractCMSComponentData componentData;
	@Mock
	private RestrictionData restrictionData;
	@Mock
	private SearchPageData searchPageData;
	@Mock
	private SearchPageData searchPageDataResult;
	@Mock
	private RenderingCacheService<AbstractCMSComponentData> renderingCacheService;

	@Before
	public void setUp()
	{
		doAnswer((invocationOnMock) -> {
			final Object[] args = invocationOnMock.getArguments();
			return ((Supplier<?>) args[1]).get();
		}).when(restrictionAwareService).execute(any(), any());
	}

	@Test(expected = ValidationException.class)
	public void shouldFailGetComponentByIdWithValidationError() throws CMSItemNotFoundException
	{
		doThrow(new ValidationException(errors)).when(facadeValidationService).validate(any(), any());

		componentRenderingService.getComponentById(COMPONENT_UID, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE);

		verifyZeroInteractions(cmsDataFactory);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailGetComponentByIdWithNotFoundError() throws CMSItemNotFoundException
	{
		when(cmsComponentService.getAbstractCMSComponent(COMPONENT_UID)).thenThrow(new CMSItemNotFoundException("invalid id"));

		componentRenderingService.getComponentById(COMPONENT_UID, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE);

		verifyZeroInteractions(cmsDataFactory);
	}

	@Test
	public void shouldGetComponentById() throws CMSItemNotFoundException
	{
		when(cmsComponentService.getAbstractCMSComponent(COMPONENT_UID)).thenReturn(componentModel);
		doReturn(Optional.of(componentData)).when(restrictionAwareService).execute(any(), any());

		componentRenderingService.getComponentById(COMPONENT_UID, CATEGORY_CODE, null, null);

		verify(cmsDataFactory).createRestrictionData(CATEGORY_CODE, null, null);
		verify(restrictionAwareService).execute(any(), any());
	}

	@Test
	public void shouldGetComponentData()
	{
		when(renderingVisibilityService.isVisible(componentModel)).thenReturn(Boolean.TRUE);
		when(componentRenderingConverter.convert(componentModel)).thenReturn(componentData);
		when(renderingCacheService.cacheOrElse(any(), any())).thenReturn(componentData);

		final Optional<AbstractCMSComponentData> optionalData = componentRenderingService.getComponentData(componentModel,
				restrictionData);

		assertThat(optionalData.get(), not(nullValue()));
	}

	@Test
	public void shouldGetEmptyComponentDataWhenComponentIsNotVisible()
	{
		when(renderingVisibilityService.isVisible(componentModel)).thenReturn(Boolean.FALSE);

		final Optional<AbstractCMSComponentData> optionalData = componentRenderingService.getComponentData(componentModel,
				restrictionData);

		assertThat(optionalData, is(Optional.empty()));
		verifyZeroInteractions(componentRenderingConverter);
	}

	@Test(expected = ValidationException.class)
	public void shouldFailGetComponentsByIdsWithValidationError() throws CMSItemNotFoundException
	{
		doThrow(new ValidationException(errors)).when(facadeValidationService).validate(any(), any());

		componentRenderingService.getComponentsByIds(Arrays.asList(COMPONENT_UID), CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE,
				searchPageData);

		verifyZeroInteractions(cmsDataFactory);
	}

	@Test
	public void shouldGetZeroComponentsByIdsWhenItemsNotFound() throws CMSItemNotFoundException
	{
		when(searchPageDataResult.getResults()).thenReturn(Collections.emptyList());
		when(cmsComponentService.getAbstractCMSComponents(Arrays.asList(COMPONENT_UID), searchPageData))
				.thenReturn(searchPageDataResult);

		final SearchPageData<AbstractCMSComponentData> results = componentRenderingService
				.getComponentsByIds(Arrays.asList(COMPONENT_UID), CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE, searchPageData);

		assertThat(results.getResults(), empty());
		verify(searchPageDataResult).getPagination();
		verify(searchPageDataResult).getSorts();
	}

	@Test
	public void shouldGetComponentsByIds() throws CMSItemNotFoundException
	{
		when(searchPageDataResult.getResults()).thenReturn(Arrays.asList(componentModel));
		when(cmsComponentService.getAbstractCMSComponents(Arrays.asList(COMPONENT_UID), searchPageData))
				.thenReturn(searchPageDataResult);
		when(renderingVisibilityService.isVisible(componentModel)).thenReturn(Boolean.TRUE);
		when(componentRenderingConverter.convert(componentModel)).thenReturn(componentData);
		when(renderingCacheService.cacheOrElse(any(), any())).thenReturn(componentData);

		final SearchPageData<AbstractCMSComponentData> results = componentRenderingService
				.getComponentsByIds(Arrays.asList(COMPONENT_UID), CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE, searchPageData);

		assertThat(results.getResults(), hasSize(1));
		verify(searchPageDataResult).getPagination();
		verify(searchPageDataResult).getSorts();
	}

	@Test
	public void shouldGetAllComponents() throws CMSItemNotFoundException
	{
		when(searchPageDataResult.getResults()).thenReturn(Arrays.asList(componentModel));
		when(cmsComponentService.getAllAbstractCMSComponents(searchPageData)).thenReturn(searchPageDataResult);
		when(renderingVisibilityService.isVisible(componentModel)).thenReturn(Boolean.TRUE);
		when(componentRenderingConverter.convert(componentModel)).thenReturn(componentData);
		when(renderingCacheService.cacheOrElse(any(), any())).thenReturn(componentData);

		final SearchPageData<AbstractCMSComponentData> results = componentRenderingService.getAllComponents(CATEGORY_CODE,
				PRODUCT_CODE, CATALOG_CODE, searchPageData);

		assertThat(results.getResults(), hasSize(1));
		verify(searchPageDataResult).getPagination();
		verify(searchPageDataResult).getSorts();
	}

}
