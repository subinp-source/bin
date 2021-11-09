/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceorgaddon.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Pojo for 'B2b unit' form.
 */
public class B2BUnitForm
{
	private String uid;
	private String name;
	private String parentUnit;
	private String originalUid;
	private String approvalProcessCode;

	@NotNull(message = "{unit.uid.invalid}")
	@Size(min = 1, max = 255, message = "{unit.uid.invalid}")
	public String getUid()
	{
		return uid;
	}

	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	@NotNull(message = "{unit.name.invalid}")
	@Size(min = 1, max = 255, message = "{unit.name.invalid}")
	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getParentUnit()
	{
		return parentUnit;
	}

	public void setParentUnit(final String parentUnit)
	{
		this.parentUnit = parentUnit;
	}

	public String getApprovalProcessCode()
	{
		return approvalProcessCode;
	}

	public void setApprovalProcessCode(final String approvalProcessCode)
	{
		this.approvalProcessCode = approvalProcessCode;
	}

	public String getOriginalUid()
	{
		return originalUid;
	}

	public void setOriginalUid(final String originalUid)
	{
		this.originalUid = originalUid;
	}
}
