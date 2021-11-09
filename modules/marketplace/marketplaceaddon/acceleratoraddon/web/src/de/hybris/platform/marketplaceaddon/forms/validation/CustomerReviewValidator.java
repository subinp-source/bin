/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceaddon.forms.validation;

import de.hybris.platform.marketplaceaddon.forms.OrderReviewForm;
import de.hybris.platform.marketplaceaddon.forms.ProductReviewForm;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component("customerReviewValidator")
public class CustomerReviewValidator implements Validator
{
	private static final int COMMENT_LENGTH = 4000;
	private static final double RATING_MIN = 0.5;

	private static final double RATING_MAX = 5;

	@Override
	public boolean supports(final Class<?> paramClass)
	{
		return OrderReviewForm.class == paramClass;
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final OrderReviewForm form = (OrderReviewForm) object;
		final Double satisfaction = form.getSatisfaction();
		final Double delivery = form.getDelivery();
		final Double communication = form.getCommunication();
		final String comment = form.getComment();


		validateRating(satisfaction, "satisfaction", "order.review.satisfaction.invalid", errors);
		validateRating(delivery, "delivery", "order.review.delivery.invalid", errors);
		validateRating(communication, "communication", "order.review.communication.invalid", errors);

		if (StringUtils.isNotEmpty(comment) && comment.length() > COMMENT_LENGTH)
		{
			errors.rejectValue("orderReviewForm.comment", "order.review.comment.invalid");
		}

		final List<ProductReviewForm> productReviewForms = form.getProductReviewForms();
		for (int i = 0; i < productReviewForms.size(); i++)
		{
			final ProductReviewForm productReviewForm = productReviewForms.get(i);
			validateProductReview(productReviewForm, i, errors);
		}

	}

	protected void validateProductReview(final ProductReviewForm productReviewForm, final int index, final Errors errors)
	{
		final Double rating = productReviewForm.getRating();
		validateRating(rating, "productReviewForms[" + index + "].rating", "review.rating.invalid", errors);

		final String comment = productReviewForm.getComment();
		if (StringUtils.isNotEmpty(comment) && comment.length() > COMMENT_LENGTH)
		{
			errors.rejectValue("productReviewForms[" + index + "].comment", "order.review.comment.invalid");
		}
	}

	protected void validateRating(final Double rating, final String fieldName, final String msgKey, final Errors errors)
	{
		if (rating == null || rating.doubleValue() < RATING_MIN || rating.doubleValue() > RATING_MAX)
		{
			errors.rejectValue(fieldName, msgKey);
		}
	}

}
