/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.model.AbstractSnIndexerItemSourceModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type FlexibleSearchSnIndexerItemSource first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class FlexibleSearchSnIndexerItemSourceModel extends AbstractSnIndexerItemSourceModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "FlexibleSearchSnIndexerItemSource";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlexibleSearchSnIndexerItemSource.query</code> attribute defined at extension <code>searchservices</code>. */
	public static final String QUERY = "query";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FlexibleSearchSnIndexerItemSourceModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FlexibleSearchSnIndexerItemSourceModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _query initial attribute declared by type <code>FlexibleSearchSnIndexerItemSource</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public FlexibleSearchSnIndexerItemSourceModel(final String _query)
	{
		super();
		setQuery(_query);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _query initial attribute declared by type <code>FlexibleSearchSnIndexerItemSource</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public FlexibleSearchSnIndexerItemSourceModel(final ItemModel _owner, final String _query)
	{
		super();
		setOwner(_owner);
		setQuery(_query);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchSnIndexerItemSource.query</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.GETTER)
	public String getQuery()
	{
		return getPersistenceContext().getPropertyValue(QUERY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlexibleSearchSnIndexerItemSource.query</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.SETTER)
	public void setQuery(final String value)
	{
		getPersistenceContext().setPropertyValue(QUERY, value);
	}
	
}
