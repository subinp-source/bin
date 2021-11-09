/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.dao;

import de.hybris.platform.core.model.user.EmployeeModel;

import java.util.List;


/**
 * DAO with B2B registration specific methods
 */
public interface B2BRegistrationDao
{

	/**
	 * @param userGroup
	 *           The user group to look for
	 * @return All employees that are part of the specified user group
	 */
	public List<EmployeeModel> getEmployeesInUserGroup(String userGroup);

}
