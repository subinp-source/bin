/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies;

import de.hybris.platform.b2b.model.B2BPermissionModel;


/**
 * @param <R>
 *           the generic type B2BPermissionResultModel
 * @param <P>
 *           the generic type AbstractOrderModel
 * @param <S>
 *           the generic type B2BCustomerModel
 */
public interface PermissionEvaluateStrategy<R, P, S> extends EvaluateStrategy<R, P, S>
{
	/**
	 * Gets the permission type.
	 * 
	 * @return the permission type
	 */

	public abstract Class<? extends B2BPermissionModel> getPermissionType();
}
