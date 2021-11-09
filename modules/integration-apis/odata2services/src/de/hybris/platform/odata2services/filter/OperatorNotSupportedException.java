/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.filter;

import de.hybris.platform.odata2services.odata.persistence.lookup.InvalidLookupDataException;

import org.apache.olingo.odata2.api.uri.expression.BinaryOperator;

/**
 * Throws this exception when the {@link BinaryOperator} is not supported
 */
public class OperatorNotSupportedException extends InvalidLookupDataException
{
	private static final String MESSAGE = "Operator [%s] is not supported";

	/**
	 * Constructor to create OperatorNotSupportedException Exception.
	 *
	 * @param operator {@link BinaryOperator} that is not supported by this extension
	 */
	public OperatorNotSupportedException(final BinaryOperator operator)
	{
		super(String.format(MESSAGE, operator.toUriLiteral()), "operator_not_supported");
	}
}
