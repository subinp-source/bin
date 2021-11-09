/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsoccaddon.validation;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.customerinterestsoccaddon.constants.ErrorMessageConstants;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Arrays;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validate the product's stock level when the notification type is back in stock.
 */
public class BackInStockValidator implements Validator
{
	private ProductFacade productFacade;

	public BackInStockValidator(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return String.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		Assert.notNull(errors, "Errors object must not be null.");
		final String productCode = (String) object;
		try
		{
			final ProductData product = getProductFacade().getProductForCodeAndOptions(productCode,
					Arrays.asList(ProductOption.BASIC, ProductOption.STOCK));
			if (product != null && !StockLevelStatus.OUTOFSTOCK.equals(product.getStock().getStockLevelStatus()))
			{
				errors.reject(ErrorMessageConstants.PRODUCT_STOCK_AVALIABLE, ErrorMessageConstants.NORMAL_PRODUCT_MESSAGE);
			}
		}
		catch (final UnknownIdentifierException e)
		{
			errors.reject(ErrorMessageConstants.PRODUCT_NOT_FOUND, ErrorMessageConstants.NO_PRODUCT_FOUND_MESSAGE);
		}


	}

	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}


}
