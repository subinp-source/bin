/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;



/**
 * ChangeUID event, implementation of {@link AbstractCommerceUserEvent}
 */
public class ChangeUIDEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{
	private String oldUid;
	private String newUid;

	/**
	 * Default constructor
	 */
	public ChangeUIDEvent()
	{
		super();
	}

	/**
	 * Parameterized Constructor
	 * 
	 * @param oldUid
	 * @param newUid
	 */
	public ChangeUIDEvent(final String oldUid, final String newUid)
	{
		super();
		this.oldUid = oldUid;
		this.newUid = newUid;
	}

	public String getOldUid()
	{
		return oldUid;
	}

	public void setOldUid(final String oldUid)
	{
		this.oldUid = oldUid;
	}

	public String getNewUid()
	{
		return newUid;
	}

	public void setNewUid(final String newUid)
	{
		this.newUid = newUid;
	}

}
