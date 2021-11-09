/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies;


/**
 * @param <R>
 *           the generic type B2B*ResultModel
 * @param <P>
 *           the generic type AbstractOrderModel
 * @param <S>
 *           the generic type B2BCustomerModel
 */
public interface EvaluateStrategy<R, P, S>
{

	/**
	 * Evaluate.
	 * 
	 * @param order
	 *           the AbstractOrderModel
	 * @param customer
	 *           the B2BCustomerModel
	 * @return the B2BPermissionResultModel
	 */
	public abstract R evaluate(P order, S customer);

}
