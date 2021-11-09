/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.user;

import de.hybris.platform.commercewebservicescommons.dto.user.PrincipalWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of an User Group
 */
@ApiModel(value="UserGroup", description="Representation of an User Group")
public  class UserGroupWsDTO extends PrincipalWsDTO 
{

 

	/** List of members<br/><br/><i>Generated property</i> for <code>UserGroupWsDTO.members</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="members", value="List of members") 	
	private List<PrincipalWsDTO> members;

	/** List of subgroups<br/><br/><i>Generated property</i> for <code>UserGroupWsDTO.subGroups</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="subGroups", value="List of subgroups") 	
	private List<UserGroupWsDTO> subGroups;

	/** Number of members<br/><br/><i>Generated property</i> for <code>UserGroupWsDTO.membersCount</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="membersCount", value="Number of members") 	
	private Integer membersCount;
	
	public UserGroupWsDTO()
	{
		// default constructor
	}
	
	public void setMembers(final List<PrincipalWsDTO> members)
	{
		this.members = members;
	}

	public List<PrincipalWsDTO> getMembers() 
	{
		return members;
	}
	
	public void setSubGroups(final List<UserGroupWsDTO> subGroups)
	{
		this.subGroups = subGroups;
	}

	public List<UserGroupWsDTO> getSubGroups() 
	{
		return subGroups;
	}
	
	public void setMembersCount(final Integer membersCount)
	{
		this.membersCount = membersCount;
	}

	public Integer getMembersCount() 
	{
		return membersCount;
	}
	

}