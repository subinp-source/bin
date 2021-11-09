/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.spring.config;

import java.util.ArrayList;
import java.util.List;


public class MultipleListMergeBean
{
	private List<MergeTestBean> propertyList;
	private List<MergeTestBean> fieldList;

	public MultipleListMergeBean()
	{
		this.propertyList = new ArrayList<MergeTestBean>();
		this.fieldList = new ArrayList<MergeTestBean>();
	}

	public List<MergeTestBean> getPropertyList()
	{
		return propertyList;
	}

	public void setPropertyList(final List<MergeTestBean> propertyList)
	{
		this.propertyList = propertyList;
	}

	public void setFieldList(final List<MergeTestBean> fieldList)
	{
		this.fieldList = fieldList;
	}
}