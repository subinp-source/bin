/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.services;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Map;


/**
 * The Interface B2BApprovalProcessService.
 */
public interface B2BApprovalProcessService
{
	/**
	 * Gets the processes.
	 *
	 * @param store
	 *           the store
	 * @return the processes
	 */
	Map<String, String> getProcesses(BaseStoreModel store);

	/**
	 * Get approval process code from unit, checking parent units up the organization tree up to the root unit if process
	 * is not set in the <code>unit</code>
	 *
	 * @param unit
	 * @return approval process code
	 */
	String getApprovalProcessCodeForUnit(B2BUnitModel unit);
}
