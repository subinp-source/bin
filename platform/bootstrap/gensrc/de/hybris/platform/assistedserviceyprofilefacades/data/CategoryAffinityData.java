/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedserviceyprofilefacades.data;

import de.hybris.platform.assistedserviceyprofilefacades.data.AffinityData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;


import java.util.Objects;
public  class CategoryAffinityData extends AffinityData 
{

 

	/** <i>Generated property</i> for <code>CategoryAffinityData.categoryData</code> property defined at extension <code>assistedserviceyprofilefacades</code>. */
		
	private CategoryData categoryData;

	/** <i>Generated property</i> for <code>CategoryAffinityData.image</code> property defined at extension <code>assistedserviceyprofilefacades</code>. */
		
	private ImageData image;
	
	public CategoryAffinityData()
	{
		// default constructor
	}
	
	public void setCategoryData(final CategoryData categoryData)
	{
		this.categoryData = categoryData;
	}

	public CategoryData getCategoryData() 
	{
		return categoryData;
	}
	
	public void setImage(final ImageData image)
	{
		this.image = image;
	}

	public ImageData getImage() 
	{
		return image;
	}
	

}