/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.enums.QuoteAction;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Unit test for DefaultQuoteMetadataValidationStrategy
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultQuoteMetadataValidationStrategyTest
{
	private final DefaultQuoteMetadataValidationStrategy defaultQuoteMetadataValidationStrategy = new DefaultQuoteMetadataValidationStrategy();

	@Mock
	private QuoteModel quoteModel;
	@Mock
	private UserModel userModel;

	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateNullQuoteName()
	{
		given(quoteModel.getName()).willReturn(null);

		defaultQuoteMetadataValidationStrategy.validate(QuoteAction.SUBMIT, quoteModel, userModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateEmptyQuoteName()
	{
		given(quoteModel.getName()).willReturn("");

		defaultQuoteMetadataValidationStrategy.validate(QuoteAction.SUBMIT, quoteModel, userModel);
	}

	@Test
	public void shouldValidate()
	{
		given(quoteModel.getName()).willReturn("myQuoteName");

		defaultQuoteMetadataValidationStrategy.validate(QuoteAction.SUBMIT, quoteModel, userModel);
	}
}
