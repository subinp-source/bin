/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsocc.validation;

import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsfacades.productinterest.ProductInterestFacade;
import de.hybris.platform.customerinterestsocc.constants.ErrorMessageConstants;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.validators.NotificationTypeValidator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 *
 * Validates the request fields for product interests
 * 
 */
public class ProductInterestsValidator
{
	private final ProductInterestFacade productInterestFacade;

	private final ProductService productService;

	private final NotificationTypeValidator notificationTypeValidator;

	private final Map<String, Validator> notificationTypeValidatorMap;

	private final ParamNotEmptyOrTooLongValidator productCodeNotEmptyOrTooLongValidator;

	public ProductInterestsValidator(final ProductInterestFacade productInterestFacade, final ProductService productService,
			final NotificationTypeValidator notificationTypeValidator, final Map<String, Validator> notificationTypeValidatorMap,
			final ParamNotEmptyOrTooLongValidator productCodeNotEmptyOrTooLongValidator)
	{
		this.productInterestFacade = productInterestFacade;
		this.productService = productService;
		this.notificationTypeValidator = notificationTypeValidator;
		this.notificationTypeValidatorMap = notificationTypeValidatorMap;
		this.productCodeNotEmptyOrTooLongValidator = productCodeNotEmptyOrTooLongValidator;
	}

	public void validateProductInterestCreation(final String productCode, final String notificationType, final Errors errors)
	{
		getProductCodeNotEmptyOrTooLongValidator().validate(productCode, errors);
		if(!errors.hasErrors()){
			validateProduct(productCode, errors);
		}
		validateNotificationType(notificationType, errors);

		if (!errors.hasErrors() && isProductInStock(productCode, notificationType, errors))
		{
			validateProductInterest(RequestMethod.POST, productCode, notificationType, errors);
		}

		handleErrors(errors);
	}

	public void validateProductInterestRemoval(final String productCode, final String notificationType, final Errors errors)
	{
		getProductCodeNotEmptyOrTooLongValidator().validate(productCode, errors);
		validateNotificationType(notificationType, errors);
		if (!errors.hasErrors())
		{
			validateProductInterest(RequestMethod.DELETE, productCode, notificationType, errors);
		}
		handleErrors(errors);
	}

	public void validateProductInterestRetrieval(final String productCode, final String notificationType, final Errors errors)
	{

		getProductCodeNotEmptyOrTooLongValidator().validate(productCode, errors, false);

		if (StringUtils.isNoneBlank(notificationType))
		{
			validateNotificationType(notificationType, errors);
		}
		handleErrors(errors);
	}

	protected boolean isProductInStock(final String productCode, final String notificationType, final Errors errors)
	{
		final Validator productStockValidator = getNotificationTypeValidatorMap().get(notificationType);
		productStockValidator.validate(productCode, errors);

		return !errors.hasErrors();
	}

	protected void validateProductInterest(final RequestMethod requestMethod, final String productCode,
			final String notificationType, final Errors errors)
	{
		List<ProductInterestData> productInterests = getProductInterestFacade().getProductInterestsForNotificationType(productCode,
				NotificationType.valueOf(notificationType));
		if (requestMethod.equals(RequestMethod.POST))
		{
			productInterests = productInterests.stream().filter(x -> new DateTime(x.getExpiryDate()).isAfterNow())
					.collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(productInterests))
			{
				errors.reject(ErrorMessageConstants.EXIST_PRODUCT_INTERESTS,
						ErrorMessageConstants.PRODUCT_INTERESTS_ALREADY_SUBSCRIBED_MESSAGE);
			}
		}
		else
		{
			if (CollectionUtils.isEmpty(productInterests))
			{
				errors.reject(ErrorMessageConstants.NO_PRODUCT_INTERESTS, ErrorMessageConstants.NO_PRODUCT_INTERESTS_FOUND_MESSAGE);
			}
		}
	}

	protected void validateNotificationType(final String notificationType, final Errors errors)
	{
		getNotificationTypeValidator().validate(notificationType, errors);
	}


	protected void validateProduct(final String productCode, final Errors errors)
	{
		try
		{
			getProductService().getProductForCode(productCode);
		}
		catch (final UnknownIdentifierException e)
		{
			errors.reject(ErrorMessageConstants.PRODUCT_NOT_FOUND, ErrorMessageConstants.NO_PRODUCT_FOUND_MESSAGE);
		}
	}

	public void validatePageSize(final int pageSize, final Errors errors)
	{
		if (pageSize <= 0)
		{
			errors.reject(ErrorMessageConstants.PAGESIZE_INVALID, ErrorMessageConstants.PAGESIZE_INVALID_MESSAGE);
		}

		handleErrors(errors);
	}

	protected void handleErrors(final Errors errors)
	{
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

	protected ProductInterestFacade getProductInterestFacade()
	{
		return productInterestFacade;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	protected NotificationTypeValidator getNotificationTypeValidator()
	{
		return notificationTypeValidator;
	}

	protected Map<String, Validator> getNotificationTypeValidatorMap()
	{
		return notificationTypeValidatorMap;
	}

	protected ParamNotEmptyOrTooLongValidator getProductCodeNotEmptyOrTooLongValidator()
	{
		return productCodeNotEmptyOrTooLongValidator;
	}
}