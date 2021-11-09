/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import java.util.List;


import java.util.Objects;
public  class BundleTemplateData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>BundleTemplateData.id</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>BundleTemplateData.name</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>BundleTemplateData.type</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>BundleTemplateData.version</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private String version;

	/** <i>Generated property</i> for <code>BundleTemplateData.bundleTemplates</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private List<BundleTemplateData> bundleTemplates;

	/** <i>Generated property</i> for <code>BundleTemplateData.products</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private List<ProductData> products;

	/** <i>Generated property</i> for <code>BundleTemplateData.maxItemsAllowed</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private int maxItemsAllowed;

	/** <i>Generated property</i> for <code>BundleTemplateData.rootBundleTemplateName</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private String rootBundleTemplateName;
	
	public BundleTemplateData()
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
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setType(final String type)
	{
		this.type = type;
	}

	public String getType() 
	{
		return type;
	}
	
	public void setVersion(final String version)
	{
		this.version = version;
	}

	public String getVersion() 
	{
		return version;
	}
	
	public void setBundleTemplates(final List<BundleTemplateData> bundleTemplates)
	{
		this.bundleTemplates = bundleTemplates;
	}

	public List<BundleTemplateData> getBundleTemplates() 
	{
		return bundleTemplates;
	}
	
	public void setProducts(final List<ProductData> products)
	{
		this.products = products;
	}

	public List<ProductData> getProducts() 
	{
		return products;
	}
	
	public void setMaxItemsAllowed(final int maxItemsAllowed)
	{
		this.maxItemsAllowed = maxItemsAllowed;
	}

	public int getMaxItemsAllowed() 
	{
		return maxItemsAllowed;
	}
	
	public void setRootBundleTemplateName(final String rootBundleTemplateName)
	{
		this.rootBundleTemplateName = rootBundleTemplateName;
	}

	public String getRootBundleTemplateName() 
	{
		return rootBundleTemplateName;
	}
	

}