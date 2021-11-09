/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.actions;

import de.hybris.platform.b2b.enums.PermissionStatus;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;
import java.util.Collection;
import java.util.List;



/**
 * 
 */
public interface B2BPermissionResultHelper
{

	/**
	 * Filter result by permission status.
	 * 
	 * @param result
	 *           the result
	 * @param status
	 *           the status
	 * @return the collection
	 */
	public abstract Collection<B2BPermissionResultModel> filterResultByPermissionStatus(
			final Collection<B2BPermissionResultModel> result, final PermissionStatus status);

	/**
	 * Gets the approvers with a specific permission status.
	 * 
	 * @param result
	 *           the result
	 * @param status
	 *           the status
	 * @return the approvers with permission status
	 */
	public abstract List<B2BCustomerModel> getApproversWithPermissionStatus(final Collection<B2BPermissionResultModel> result,
			final PermissionStatus status);

	/**
	 * Checks for open permission result.
	 * 
	 * @param permissionResults
	 *           the approver permissions
	 * @return true, if successful
	 */
	public abstract boolean hasOpenPermissionResult(final Collection<B2BPermissionResultModel> permissionResults);

	/**
	 * Extracts {@link B2BPermissionModel} from {@link B2BPermissionResultModel} collection
	 * 
	 * @param openPermissions
	 *           the open permissions
	 * @return List of Open Permissions extending B2BPermissionModel
	 */
	public abstract List<Class<? extends B2BPermissionModel>> extractPermissionTypes(
			final Collection<B2BPermissionResultModel> openPermissions);

}
