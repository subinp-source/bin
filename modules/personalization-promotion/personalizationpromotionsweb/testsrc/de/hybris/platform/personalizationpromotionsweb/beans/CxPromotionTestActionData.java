/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationpromotionsweb.beans;

import de.hybris.platform.personalizationfacades.data.ActionData;


public class CxPromotionTestActionData extends ActionData
{
	private static final long serialVersionUID = 1L;
	private String testField;

	public CxPromotionTestActionData()
	{

	}

	public String getTestField()
	{
		return testField;
	}

	public void setTestField(final String testField)
	{
		this.testField = testField;
	}



}
