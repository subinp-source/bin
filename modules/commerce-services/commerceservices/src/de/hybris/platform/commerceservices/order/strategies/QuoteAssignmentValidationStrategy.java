/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies;


import de.hybris.platform.commerceservices.order.CommerceQuoteAssignmentException;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;


/**
 * Strategy to help validate if a quote can be assigned to the given assignee
 */
public interface QuoteAssignmentValidationStrategy
{
	/**
	 * Validates if quote can be assigned to an assignee by an assigner, else throws IllegalArgumentException
	 *
	 * @param quote
	 *           the quote to be assigned
	 * @param assignee
	 *           the user to which the quote should be assigned
	 * @param assigner
	 *           the user assigning the quote to the assignee
	 * @throws CommerceQuoteAssignmentException
	 *            thrown if the quote assignment fails
	 */
	void validateQuoteAssignment(QuoteModel quote, UserModel assignee, UserModel assigner);

	/**
	 * Validates if quote can be unassigned by an assigner, else throws IllegalArgumentException
	 *
	 * @param quote
	 *           the quote to be assigned
	 * @param assigner
	 *           the user assigning the quote to the assignee
	 * @throws CommerceQuoteAssignmentException
	 *            thrown if the quote unassignment fails
	 */
	void validateQuoteUnassignment(QuoteModel quote, UserModel assigner);
}
