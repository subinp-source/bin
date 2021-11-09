/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.facetdata;

import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.SpellingSuggestionData;


import java.util.Objects;
/**
 * POJO containing the result page for product search.
 */
public  class ProductSearchPageData<STATE, RESULT> extends FacetSearchPageData<STATE, RESULT> 
{

 

	/** <i>Generated property</i> for <code>ProductSearchPageData<STATE, RESULT>.freeTextSearch</code> property defined at extension <code>commerceservices</code>. */
		
	private String freeTextSearch;

	/** <i>Generated property</i> for <code>ProductSearchPageData<STATE, RESULT>.categoryCode</code> property defined at extension <code>commerceservices</code>. */
		
	private String categoryCode;

	/** <i>Generated property</i> for <code>ProductSearchPageData<STATE, RESULT>.keywordRedirectUrl</code> property defined at extension <code>commerceservices</code>. */
		
	private String keywordRedirectUrl;

	/** <i>Generated property</i> for <code>ProductSearchPageData<STATE, RESULT>.spellingSuggestion</code> property defined at extension <code>commerceservices</code>. */
		
	private SpellingSuggestionData<STATE> spellingSuggestion;
	
	public ProductSearchPageData()
	{
		// default constructor
	}
	
	public void setFreeTextSearch(final String freeTextSearch)
	{
		this.freeTextSearch = freeTextSearch;
	}

	public String getFreeTextSearch() 
	{
		return freeTextSearch;
	}
	
	public void setCategoryCode(final String categoryCode)
	{
		this.categoryCode = categoryCode;
	}

	public String getCategoryCode() 
	{
		return categoryCode;
	}
	
	public void setKeywordRedirectUrl(final String keywordRedirectUrl)
	{
		this.keywordRedirectUrl = keywordRedirectUrl;
	}

	public String getKeywordRedirectUrl() 
	{
		return keywordRedirectUrl;
	}
	
	public void setSpellingSuggestion(final SpellingSuggestionData<STATE> spellingSuggestion)
	{
		this.spellingSuggestion = spellingSuggestion;
	}

	public SpellingSuggestionData<STATE> getSpellingSuggestion() 
	{
		return spellingSuggestion;
	}
	

}