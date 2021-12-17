/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.runtime.interf.impl;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.CsticParameter;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.ValueParameter;
import java.util.List;


import java.util.Objects;
public  class CsticParameterWithValues  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Characteristic parameter<br/><br/><i>Generated property</i> for <code>CsticParameterWithValues.cstic</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private CsticParameter cstic;

	/** Possible values for this characteristic<br/><br/><i>Generated property</i> for <code>CsticParameterWithValues.values</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private List<ValueParameter> values;
	
	public CsticParameterWithValues()
	{
		// default constructor
	}
	
	public void setCstic(final CsticParameter cstic)
	{
		this.cstic = cstic;
	}

	public CsticParameter getCstic() 
	{
		return cstic;
	}
	
	public void setValues(final List<ValueParameter> values)
	{
		this.values = values;
	}

	public List<ValueParameter> getValues() 
	{
		return values;
	}
	

}