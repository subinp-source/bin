/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofile.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateProfileForm;


/**
 * Form object for updating profile, add a attribute mobileNumber.
 */
public class ChineseUpdateProfileForm extends UpdateProfileForm
{

	private String mobileNumber;

	public String getMobileNumber()
	{
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

}
