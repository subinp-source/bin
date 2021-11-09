/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices;

import java.io.Serializable;
import java.util.Set;


import java.util.Objects;
public  class CxCalculationContext  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CxCalculationContext.segmentUpdateProviders</code> property defined at extension <code>personalizationservices</code>. */
		
	private Set<String> segmentUpdateProviders;
	
	public CxCalculationContext()
	{
		// default constructor
	}
	
	public void setSegmentUpdateProviders(final Set<String> segmentUpdateProviders)
	{
		this.segmentUpdateProviders = segmentUpdateProviders;
	}

	public Set<String> getSegmentUpdateProviders() 
	{
		return segmentUpdateProviders;
	}
	

}