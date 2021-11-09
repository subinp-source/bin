/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.model;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.organization.services.OrgUnitService;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Dynamic attribute handler for the Employee.organizationRoles attribute. The Employee.organizationRoles attribute is
 * determined by filtering out non-organization related roles
 */
public class OrganizationRolesAttribute extends AbstractDynamicAttributeHandler<Set<PrincipalGroupModel>, EmployeeModel>
{
	private OrgUnitService orgUnitService;

	@Override
	public Set<PrincipalGroupModel> get(final EmployeeModel employeeModel)
	{
		validateParameterNotNull(employeeModel, "employee must not be null");

		return getOrgUnitService().getRolesForEmployee(employeeModel);
	}

	protected OrgUnitService getOrgUnitService()
	{
		return orgUnitService;
	}

	@Required
	public void setOrgUnitService(final OrgUnitService orgUnitService)
	{
		this.orgUnitService = orgUnitService;
	}

}
