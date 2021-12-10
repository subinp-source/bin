/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.model.redirect;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SolrAbstractKeywordRedirect first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrAbstractKeywordRedirectModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrAbstractKeywordRedirect";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrAbstractKeywordRedirect.hmcLabel</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String HMCLABEL = "hmcLabel";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrAbstractKeywordRedirectModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrAbstractKeywordRedirectModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SolrAbstractKeywordRedirectModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrAbstractKeywordRedirect.hmcLabel</code> dynamic attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the hmcLabel
	 */
	@Accessor(qualifier = "hmcLabel", type = Accessor.Type.GETTER)
	public String getHmcLabel()
	{
		return getPersistenceContext().getDynamicValue(this,HMCLABEL);
	}
	
}
