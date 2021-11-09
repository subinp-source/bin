/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.company;


import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;


public interface B2BGroupCycleValidator
{
	/**
	 * Validate the groups given a group and group member
	 * 
	 * @param principalGroupModel
	 * @param groupMember
	 * @return Boolean result based on whether cyclic groups were detected or not
	 */
	boolean validateGroups(PrincipalGroupModel principalGroupModel, PrincipalGroupModel groupMember);

	/**
	 * Validate the members given a group and group member
	 * 
	 * @param principalGroupModel
	 * @param member
	 * @return Boolean result based on whether cyclic groups were detected or not
	 */
	boolean validateMembers(PrincipalGroupModel principalGroupModel, PrincipalModel member);
}
