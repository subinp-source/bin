/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 08-Nov-2021, 4:51:28 PM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.util.Objects;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.CategoryRAO;
import java.util.Set;

/**
* @deprecated not used anymore, instead use OrderEntryRAO.productCode
*/
@Deprecated(since = "2005", forRemoval = true)
public  class ProductRAO extends AbstractActionedRAO 
{

	/** <i>Generated property</i> for <code>ProductRAO.code</code> property defined at extension <code>ruleengineservices</code>. */
	private String code;
	/** <i>Generated property</i> for <code>ProductRAO.categories</code> property defined at extension <code>ruleengineservices</code>. */
	private Set<CategoryRAO> categories;
	/** <i>Generated property</i> for <code>ProductRAO.baseProductCodes</code> property defined at extension <code>ruleengineservices</code>. */
	private Set<String> baseProductCodes;
		
	public ProductRAO()
	{
		// default constructor
	}
	
		public void setCode(final String code)
	{
		this.code = code;
	}
		public String getCode() 
	{
		return code;
	}
		
		/**
	* @deprecated not used anymore, instead category relationship is handled via 'categoryCodes' property
	*/
	@Deprecated(since = "2005", forRemoval = true)
	public void setCategories(final Set<CategoryRAO> categories)
	{
		this.categories = categories;
	}
	/**
	* @deprecated not used anymore, instead category relationship is handled via 'categoryCodes' property
	*/
	@Deprecated(since = "2005", forRemoval = true)
		public Set<CategoryRAO> getCategories() 
	{
		return categories;
	}
		
		public void setBaseProductCodes(final Set<String> baseProductCodes)
	{
		this.baseProductCodes = baseProductCodes;
	}
		public Set<String> getBaseProductCodes() 
	{
		return baseProductCodes;
	}
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final ProductRAO other = (ProductRAO) o;
		return				Objects.equals(getCode(), other.getCode())
  ;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

				attribute = code;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		
		return result;
	}
	}
