/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.basesite;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.basestore.BaseStoreWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.LanguageWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Base Site
 */
@ApiModel(value="BaseSite", description="Representation of a Base Site")
public  class BaseSiteWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Unique identifier of Basesite<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.uid</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="uid", value="Unique identifier of Basesite") 	
	private String uid;

	/** Name of Basesite<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of Basesite") 	
	private String name;

	/** List of Basestores<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.stores</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="stores", value="List of Basestores") 	
	private List<BaseStoreWsDTO> stores;

	/** Theme of Basesite<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.theme</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="theme", value="Theme of Basesite") 	
	private String theme;

	/** Default language for Basesite<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.defaultLanguage</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="defaultLanguage", value="Default language for Basesite") 	
	private LanguageWsDTO defaultLanguage;

	/** Locale data for Basesite<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.locale</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="locale", value="Locale data for Basesite") 	
	private String locale;

	/** Channel<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.channel</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="channel", value="Channel") 	
	private String channel;

	/** List of url encoding attributes<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.urlEncodingAttributes</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="urlEncodingAttributes", value="List of url encoding attributes") 	
	private Collection<String> urlEncodingAttributes;

	/** List of url patterns<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.urlPatterns</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="urlPatterns", value="List of url patterns") 	
	private Collection<String> urlPatterns;

	/** Default preview catalog id<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.defaultPreviewCatalogId</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="defaultPreviewCatalogId", value="Default preview catalog id") 	
	private String defaultPreviewCatalogId;

	/** Default preview category code<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.defaultPreviewCategoryCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="defaultPreviewCategoryCode", value="Default preview category code") 	
	private String defaultPreviewCategoryCode;

	/** Default preview product code<br/><br/><i>Generated property</i> for <code>BaseSiteWsDTO.defaultPreviewProductCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="defaultPreviewProductCode", value="Default preview product code") 	
	private String defaultPreviewProductCode;
	
	public BaseSiteWsDTO()
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
	
	public void setStores(final List<BaseStoreWsDTO> stores)
	{
		this.stores = stores;
	}

	public List<BaseStoreWsDTO> getStores() 
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
	
	public void setDefaultLanguage(final LanguageWsDTO defaultLanguage)
	{
		this.defaultLanguage = defaultLanguage;
	}

	public LanguageWsDTO getDefaultLanguage() 
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
	
	public void setUrlEncodingAttributes(final Collection<String> urlEncodingAttributes)
	{
		this.urlEncodingAttributes = urlEncodingAttributes;
	}

	public Collection<String> getUrlEncodingAttributes() 
	{
		return urlEncodingAttributes;
	}
	
	public void setUrlPatterns(final Collection<String> urlPatterns)
	{
		this.urlPatterns = urlPatterns;
	}

	public Collection<String> getUrlPatterns() 
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
	

}