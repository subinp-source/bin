/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.frontend.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ProductAddConfigToCartComponent first defined at extension ysapproductconfigaddon.
 * <p>
 * Add configuration to cart button for product configuration addon.
 */
@SuppressWarnings("all")
public class ProductAddConfigToCartComponentModel extends CMSParagraphComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductAddConfigToCartComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductAddConfigToCartComponent.showOnlyForLongConfigurations</code> attribute defined at extension <code>ysapproductconfigaddon</code>. */
	public static final String SHOWONLYFORLONGCONFIGURATIONS = "showOnlyForLongConfigurations";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductAddConfigToCartComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductAddConfigToCartComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ProductAddConfigToCartComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ProductAddConfigToCartComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductAddConfigToCartComponent.showOnlyForLongConfigurations</code> attribute defined at extension <code>ysapproductconfigaddon</code>. 
	 * @return the showOnlyForLongConfigurations
	 */
	@Accessor(qualifier = "showOnlyForLongConfigurations", type = Accessor.Type.GETTER)
	public Boolean getShowOnlyForLongConfigurations()
	{
		return getPersistenceContext().getPropertyValue(SHOWONLYFORLONGCONFIGURATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductAddConfigToCartComponent.showOnlyForLongConfigurations</code> attribute defined at extension <code>ysapproductconfigaddon</code>. 
	 *  
	 * @param value the showOnlyForLongConfigurations
	 */
	@Accessor(qualifier = "showOnlyForLongConfigurations", type = Accessor.Type.SETTER)
	public void setShowOnlyForLongConfigurations(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SHOWONLYFORLONGCONFIGURATIONS, value);
	}
	
}
