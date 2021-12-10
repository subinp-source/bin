/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.basesite.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.basestore.data.BaseStoreData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import java.util.List;


import java.util.Objects;
public  class BaseSiteData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>BaseSiteData.uid</code> property defined at extension <code>commercefacades</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>BaseSiteData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>BaseSiteData.stores</code> property defined at extension <code>commercefacades</code>. */
		
	private List<BaseStoreData> stores;

	/** <i>Generated property</i> for <code>BaseSiteData.theme</code> property defined at extension <code>commercefacades</code>. */
		
	private String theme;

	/** <i>Generated property</i> for <code>BaseSiteData.defaultLanguage</code> property defined at extension <code>commercefacades</code>. */
		
	private LanguageData defaultLanguage;

	/** <i>Generated property</i> for <code>BaseSiteData.locale</code> property defined at extension <code>commercefacades</code>. */
		
	private String locale;

	/** <i>Generated property</i> for <code>BaseSiteData.channel</code> property defined at extension <code>commercefacades</code>. */
		
	private String channel;

	/** <i>Generated property</i> for <code>BaseSiteData.urlPatterns</code> property defined at extension <code>cmsfacades</code>. */
		
	private List<String> urlPatterns;

	/** <i>Generated property</i> for <code>BaseSiteData.defaultPreviewCatalogId</code> property defined at extension <code>cmsfacades</code>. */
		
	private String defaultPreviewCatalogId;

	/** <i>Generated property</i> for <code>BaseSiteData.defaultPreviewCategoryCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String defaultPreviewCategoryCode;

	/** <i>Generated property</i> for <code>BaseSiteData.defaultPreviewProductCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String defaultPreviewProductCode;

	/** <i>Generated property</i> for <code>BaseSiteData.urlEncodingAttributes</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private List<String> urlEncodingAttributes;
	
	public BaseSiteData()
	{
		// default constructor
	}
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	public String getUid() 
	{
		return uid;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setStores(final List<BaseStoreData> stores)
	{
		this.stores = stores;
	}

	public List<BaseStoreData> getStores() 
	{
		return stores;
	}
	
	public void setTheme(final String theme)
	{
		this.theme = theme;
	}

	public String getTheme() 
	{
		return theme;
	}
	
	public void setDefaultLanguage(final LanguageData defaultLanguage)
	{
		this.defaultLanguage = defaultLanguage;
	}

	public LanguageData getDefaultLanguage() 
	{
		return defaultLanguage;
	}
	
	public void setLocale(final String locale)
	{
		this.locale = locale;
	}

	public String getLocale() 
	{
		return locale;
	}
	
	public void setChannel(final String channel)
	{
		this.channel = channel;
	}

	public String getChannel() 
	{
		return channel;
	}
	
	public void setUrlPatterns(final List<String> urlPatterns)
	{
		this.urlPatterns = urlPatterns;
	}

	public List<String> getUrlPatterns() 
	{
		return urlPatterns;
	}
	
	public void setDefaultPreviewCatalogId(final String defaultPreviewCatalogId)
	{
		this.defaultPreviewCatalogId = defaultPreviewCatalogId;
	}

	public String getDefaultPreviewCatalogId() 
	{
		return defaultPreviewCatalogId;
	}
	
	public void setDefaultPreviewCategoryCode(final String defaultPreviewCategoryCode)
	{
		this.defaultPreviewCategoryCode = defaultPreviewCategoryCode;
	}

	public String getDefaultPreviewCategoryCode() 
	{
		return defaultPreviewCategoryCode;
	}
	
	public void setDefaultPreviewProductCode(final String defaultPreviewProductCode)
	{
		this.defaultPreviewProductCode = defaultPreviewProductCode;
	}

	public String getDefaultPreviewProductCode() 
	{
		return defaultPreviewProductCode;
	}
	
	public void setUrlEncodingAttributes(final List<String> urlEncodingAttributes)
	{
		this.urlEncodingAttributes = urlEncodingAttributes;
	}

	public List<String> getUrlEncodingAttributes() 
	{
		return urlEncodingAttributes;
	}
	

}