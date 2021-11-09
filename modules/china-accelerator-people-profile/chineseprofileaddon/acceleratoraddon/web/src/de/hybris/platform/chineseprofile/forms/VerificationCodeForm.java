/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofile.forms;

/**
 * Form object for verify binding mobile number.
 */
public class VerificationCodeForm
{
	private String mobileNumber;

	private String verificationCode;

	private String codeType;

	public VerificationCodeForm()
	{
		super();
	}

	public VerificationCodeForm(final String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

	public String getMobileNumber()
	{
		return mobileNumber;
	}

	public void setMobileNumber(final String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

	public String getVerificationCode()
	{
		return verificationCode;
	}

	public void setVerificationCode(final String verificationCode)
	{
		this.verificationCode = verificationCode;
	}

	public String getCodeType()
	{
		return codeType;
	}

	public void setCodeType(final String codeType)
	{
		this.codeType = codeType;
	}
}
