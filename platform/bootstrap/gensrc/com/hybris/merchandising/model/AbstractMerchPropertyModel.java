/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

/**
 * Generated model class for type AbstractMerchProperty first defined at extension merchandisingservices.
 */
@SuppressWarnings("all")
public class AbstractMerchPropertyModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractMerchProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMerchProperty.indexedProperty</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String INDEXEDPROPERTY = "indexedProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMerchProperty.merchMappedName</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String MERCHMAPPEDNAME = "merchMappedName";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractMerchPropertyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractMerchPropertyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexedProperty initial attribute declared by type <code>AbstractMerchProperty</code> at extension <code>merchandisingservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public AbstractMerchPropertyModel(final SolrIndexedPropertyModel _indexedProperty)
	{
		super();
		setIndexedProperty(_indexedProperty);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexedProperty initial attribute declared by type <code>AbstractMerchProperty</code> at extension <code>merchandisingservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public AbstractMerchPropertyModel(final SolrIndexedPropertyModel _indexedProperty, final ItemModel _owner)
	{
		super();
		setIndexedProperty(_indexedProperty);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMerchProperty.indexedProperty</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the indexedProperty - Unique identifier
	 */
	@Accessor(qualifier = "indexedProperty", type = Accessor.Type.GETTER)
	public SolrIndexedPropertyModel getIndexedProperty()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDPROPERTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMerchProperty.merchMappedName</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the merchMappedName - Indexed type
	 */
	@Accessor(qualifier = "merchMappedName", type = Accessor.Type.GETTER)
	public String getMerchMappedName()
	{
		return getPersistenceContext().getPropertyValue(MERCHMAPPEDNAME);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMerchProperty.indexedProperty</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the indexedProperty - Unique identifier
	 */
	@Accessor(qualifier = "indexedProperty", type = Accessor.Type.SETTER)
	public void setIndexedProperty(final SolrIndexedPropertyModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDPROPERTY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMerchProperty.merchMappedName</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the merchMappedName - Indexed type
	 */
	@Accessor(qualifier = "merchMappedName", type = Accessor.Type.SETTER)
	public void setMerchMappedName(final String value)
	{
		getPersistenceContext().setPropertyValue(MERCHMAPPEDNAME, value);
	}
	
}
