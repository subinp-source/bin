/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.index.data;

import java.io.Serializable;


import java.util.Objects;
public  class SnIndex  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SnIndex.id</code> property defined at extension <code>searchservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>SnIndex.indexTypeId</code> property defined at extension <code>searchservices</code>. */
		
	private String indexTypeId;

	/** <i>Generated property</i> for <code>SnIndex.active</code> property defined at extension <code>searchservices</code>. */
		
	private Boolean active;
	
	public SnIndex()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setIndexTypeId(final String indexTypeId)
	{
		this.indexTypeId = indexTypeId;
	}

	public String getIndexTypeId() 
	{
		return indexTypeId;
	}
	
	public void setActive(final Boolean active)
	{
		this.active = active;
	}

	public Boolean getActive() 
	{
		return active;
	}
	

	@Override
	public boolean equals(final Object o)
	{
		if (o == null) return false;
		if (o == this) return true;

        if (getClass() != o.getClass()) return false;

		final SnIndex other = (SnIndex) o;
		return Objects.equals(getId(), other.getId())
			&& Objects.equals(getIndexTypeId(), other.getIndexTypeId())
			&& Objects.equals(getActive(), other.getActive());
    }

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

		attribute = id;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = indexTypeId;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		attribute = active;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());

		return result;
	}
}