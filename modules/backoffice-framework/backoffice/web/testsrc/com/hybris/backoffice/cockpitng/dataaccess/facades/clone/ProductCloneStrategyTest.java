/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.clone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;


@RunWith(MockitoJUnitRunner.class)
public class ProductCloneStrategyTest
{
	@InjectMocks
	private ProductCloneStrategy productCloneStrategy;
	@Mock
	private ModelService modelService;
	@Mock
	private ProductModel mockProduct;
	@Mock
	private DataType dataType;
	@Mock
	private TypeService typeService;
	@Mock
	private ComposedTypeModel composedTypeMock;

	@Before
	public void setUp()
	{
		when(dataType.isSingleton()).thenReturn(false);
		when(modelService.clone(any())).thenReturn(mockProduct);

		when(typeService.getComposedTypeForClass(any())).thenReturn(composedTypeMock);
		when(composedTypeMock.getSingleton()).thenReturn(false);

		final AttributeDescriptorModel attributeDescriptorMock = mock(AttributeDescriptorModel.class);
		when(typeService.getAttributeDescriptor(any(ComposedTypeModel.class), any())).thenReturn(attributeDescriptorMock);
		when(attributeDescriptorMock.getPartOf()).thenReturn(true);
	}

	@Test
	public void shouldNotHandleNotProductModel()
	{
		//given - setUp
		//when
		final boolean canHandle = productCloneStrategy.canHandle(new Object());
		//then
		assertThat(canHandle).isFalse();
	}

	@Test
	public void shouldHandleProductModel()
	{
		//given - setUp
		//when
		final boolean canHandle = productCloneStrategy.canHandle(new ProductModel());
		//then
		assertThat(canHandle).isTrue();
	}

	@Test
	public void shouldCloneProduct()
	{
		//given - setUp
		//when
		final ProductModel clonedProduct = productCloneStrategy.clone(new ProductModel());
		//then
		verify(modelService).clone(any(), any(ModelCloningContext.class));
		verify(modelService).setAttributeValue(clonedProduct, ProductModel.CODE, null);
	}

	@Test(expected = IllegalStateException.class)
	public void shouldNotCloneNewProduct()
	{
		//given
		when(modelService.isNew(any())).thenReturn(true);
		//when
		productCloneStrategy.clone(new ProductModel());
		//then
		//exception
	}

	@Test
	public void shouldNotHandleSingleton()
	{
		//given - setUp
		when(composedTypeMock.getSingleton()).thenReturn(true);
		//when
		final boolean canHandle = productCloneStrategy.canHandle(new ProductModel());
		//then
		assertThat(canHandle).isFalse();
	}
}
