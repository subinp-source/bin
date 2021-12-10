/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices;

import java.io.Serializable;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import java.util.Set;


import java.util.Objects;
public  class CxUpdateSegmentContext  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CxUpdateSegmentContext.segmentProviders</code> property defined at extension <code>personalizationservices</code>. */
		
	private Set<String> segmentProviders;

	/** Full update will add and also remove not used segments<br/><br/><i>Generated property</i> for <code>CxUpdateSegmentContext.fullUpdate</code> property defined at extension <code>personalizationservices</code>. */
		
	private boolean fullUpdate;

	/** <i>Generated property</i> for <code>CxUpdateSegmentContext.baseSites</code> property defined at extension <code>personalizationservices</code>. */
		
	private Set<BaseSiteModel> baseSites;
	
	public CxUpdateSegmentContext()
	{
		// default constructor
	}
	
	public void setSegmentProviders(final Set<String> segmentProviders)
	{
		this.segmentProviders = segmentProviders;
	}

	public Set<String> getSegmentProviders() 
	{
		return segmentProviders;
	}
	
	public void setFullUpdate(final boolean fullUpdate)
	{
		this.fullUpdate = fullUpdate;
	}

	public boolean isFullUpdate() 
	{
		return fullUpdate;
	}
	
	public void setBaseSites(final Set<BaseSiteModel> baseSites)
	{
		this.baseSites = baseSites;
	}

	public Set<BaseSiteModel> getBaseSites() 
	{
		return baseSites;
	}
	

}