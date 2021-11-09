/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.bulkedit.renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.hybris.backoffice.bulkedit.ValidationResult;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.validation.impl.DefaultValidationInfo;

import de.hybris.platform.core.model.product.ProductModel;


@RunWith(MockitoJUnitRunner.class)
public class BulkEditValidationRendererTest
{

	@Mock
	private TypeFacade typeFacade;

	@InjectMocks
	private BulkEditValidationRenderer renderer;

	@Test
	public void shouldAddTypePrefixToToInvalidPropertyPath()
	{
		// given
		final ProductModel product = mock(ProductModel.class);
		final DefaultValidationInfo info = new DefaultValidationInfo();
		info.setInvalidPropertyPath("name[en]");
		final List<ValidationResult> validations = Lists.newArrayList(new ValidationResult(product, Lists.newArrayList(info)));

		doReturn("Product").when(typeFacade).getType(product);

		// when
		final List<ValidationResult> results = renderer.addTypePrefixToToInvalidPropertyPath(validations);

		// then
		assertThat(results).hasSize(1);
		assertThat(results.get(0).getItem()).isEqualTo(product);
		assertThat(results.get(0).getValidationInfos()).hasSize(1);
		assertThat(results.get(0).getValidationInfos().get(0).getInvalidPropertyPath()).isEqualTo("Product.name[en]");
	}
}
