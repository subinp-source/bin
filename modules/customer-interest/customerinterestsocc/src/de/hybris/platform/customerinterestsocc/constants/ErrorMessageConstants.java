/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsocc.constants;

/**
 * Error message constants
 */
public class ErrorMessageConstants
{
	public static final String NO_PRODUCT_FOUND_MESSAGE = "The product does not exist.";
	public static final String NO_PRODUCT_INTERESTS_FOUND_MESSAGE = "There is no product interest subscribed for this product.";
	public static final String NO_PRODUCT_FOUND = "productNotFound";
	public static final String NO_PRODUCT_INTERESTS = "noProductInterest";
	public static final String EXIST_PRODUCT_INTERESTS = "existProductInterest";
	public static final String PRODUCT_STOCK_AVALIABLE = "productStockAvaliable";
	public static final String NOTIFICATION_TYPE_INVALID = "notificationTypeInvalid";

	public static final String PAGESIZE_INVALID_MESSAGE = "The pagesize can not be less than or equal to 0.";
	public static final String PAGESIZE_INVALID = "invalidPageSize";

	public static final String NORMAL_PRODUCT_MESSAGE = "Stock available for this product, therefore this operation is not applicable.";
	public static final String NO_PRODUCT_INTEREST_MESSAGE = "There is no product interest subscribed for this product.";

	public static final String PARAMETER_PRODUCTCODE_REQUIRED_MESSAGE = "Product code is required.";

	public static final String PRODUCT_INTERESTS_ALREADY_SUBSCRIBED_MESSAGE = "This product has been subscribed for the specified notification type.";
	public static final String INVALID_NOTIFICATION_TYPE_MESSAGE = "The value for notification type is invalid.";

	public static final String PRODUCT_NOT_FOUND = "productNotFound";
	public static final String PARAMETER_PRODUCTCODE_REQUIRED = "noProductCode";

	public static final String PARAMETER_PRODUCTCODE_TOOLONG_REQUIRED = "tooLongProductCode";
	public static final String PARAMETER_PRODUCTCODE_TOOLONG_MESSAGE = "Product code must be between 1 and {0} characters long.";

	public static final String FIELD_REQUIRED_TOOLONG = "requiredAndNotTooLong";
	public static final String FIELD_REQUIRED_TOOLONG_MESSAGE = "{0} is required and must be between 1 and {1} characters long.";
	public static final String FIELD_TOOLONG = "notTooLong";
	public static final String FIELD_TOOLONG_MESSAGE = "{0} must be between 1 and {1} characters long.";

	private ErrorMessageConstants()
	{
		//empty to avoid instantiating this constant class
	}
}