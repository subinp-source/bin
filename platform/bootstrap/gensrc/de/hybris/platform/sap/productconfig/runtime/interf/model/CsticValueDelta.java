/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:32:59 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.runtime.interf.model;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ValueChangeType;
import java.util.List;


import java.util.Objects;
public  class CsticValueDelta  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Name of the instance the characteristic belongs to<br/><br/><i>Generated property</i> for <code>CsticValueDelta.instanceName</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String instanceName;

	/** Id of the instance the characteristic belongs to<br/><br/><i>Generated property</i> for <code>CsticValueDelta.instanceId</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String instanceId;

	/** Name of characteristic that has been changed<br/><br/><i>Generated property</i> for <code>CsticValueDelta.csticName</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String csticName;

	/** Name of values that has been changed<br/><br/><i>Generated property</i> for <code>CsticValueDelta.valueNames</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private List<String> valueNames;

	/** Type of change<br/><br/><i>Generated property</i> for <code>CsticValueDelta.changeType</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private ValueChangeType changeType;
	
	public CsticValueDelta()
	{
		// default constructor
	}
	
	public void setInstanceName(final String instanceName)
	{
		this.instanceName = instanceName;
	}

	public String getInstanceName() 
	{
		return instanceName;
	}
	
	public void setInstanceId(final String instanceId)
	{
		this.instanceId = instanceId;
	}

	public String getInstanceId() 
	{
		return instanceId;
	}
	
	public void setCsticName(final String csticName)
	{
		this.csticName = csticName;
	}

	public String getCsticName() 
	{
		return csticName;
	}
	
	public void setValueNames(final List<String> valueNames)
	{
		this.valueNames = valueNames;
	}

	public List<String> getValueNames() 
	{
		return valueNames;
	}
	
	public void setChangeType(final ValueChangeType changeType)
	{
		this.changeType = changeType;
	}

	public ValueChangeType getChangeType() 
	{
		return changeType;
	}
	

}