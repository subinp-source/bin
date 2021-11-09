/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.model;

import com.hybris.merchandising.model.AbstractMerchPropertyModel;
import com.hybris.merchandising.model.MerchIndexingConfigModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

/**
 * Generated model class for type MerchImageProperty first defined at extension merchandisingservices.
 */
@SuppressWarnings("all")
public class MerchImagePropertyModel extends AbstractMerchPropertyModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MerchImageProperty";
	
	/**<i>Generated relation code constant for relation <code>MerchIndexingConfig2MerchImageProperty</code> defining source attribute <code>merchIndexingConfig</code> in extension <code>merchandisingservices</code>.</i>*/
	public static final String _MERCHINDEXINGCONFIG2MERCHIMAGEPROPERTY = "MerchIndexingConfig2MerchImageProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchImageProperty.merchIndexingConfigPOS</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String MERCHINDEXINGCONFIGPOS = "merchIndexingConfigPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchImageProperty.merchIndexingConfig</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String MERCHINDEXINGCONFIG = "merchIndexingConfig";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MerchImagePropertyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MerchImagePropertyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexedProperty initial attribute declared by type <code>AbstractMerchProperty</code> at extension <code>merchandisingservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public MerchImagePropertyModel(final SolrIndexedPropertyModel _indexedProperty)
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
	public MerchImagePropertyModel(final SolrIndexedPropertyModel _indexedProperty, final ItemModel _owner)
	{
		super();
		setIndexedProperty(_indexedProperty);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchImageProperty.merchIndexingConfig</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the merchIndexingConfig
	 */
	@Accessor(qualifier = "merchIndexingConfig", type = Accessor.Type.GETTER)
	public MerchIndexingConfigModel getMerchIndexingConfig()
	{
		return getPersistenceContext().getPropertyValue(MERCHINDEXINGCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchImageProperty.merchIndexingConfig</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the merchIndexingConfig
	 */
	@Accessor(qualifier = "merchIndexingConfig", type = Accessor.Type.SETTER)
	public void setMerchIndexingConfig(final MerchIndexingConfigModel value)
	{
		getPersistenceContext().setPropertyValue(MERCHINDEXINGCONFIG, value);
	}
	
}
