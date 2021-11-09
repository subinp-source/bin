/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceorgaddon.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Pojo for 'B2b user group' form.
 */
public class B2BUserGroupForm
{
	private String uid;
	private String name;
	private String parentUnit;
	private boolean active;
	private String originalUid;

	public String getOriginalUid()
	{
		return originalUid;
	}

	public void setOriginalUid(final String originalUid)
	{
		this.originalUid = originalUid;
	}

	@NotNull(message = "{usergroup.uid.invalid}")
	@Size(min = 1, max = 255, message = "{usergroup.uid.invalid}")
	public String getUid()
	{
		return uid;
	}

	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	@NotNull(message = "{usergroup.name.invalid}")
	@Size(min = 1, max = 255, message = "{usergroup.name.invalid}")
	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	@NotNull(message = "{usergroup.unit.invalid}")
	public String getParentUnit()
	{
		return parentUnit;
	}

	public void setParentUnit(final String parentUnit)
	{
		this.parentUnit = parentUnit;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(final boolean active)
	{
		this.active = active;
	}
}
