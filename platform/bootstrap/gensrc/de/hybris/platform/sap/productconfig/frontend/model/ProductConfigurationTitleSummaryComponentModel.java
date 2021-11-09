/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
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
 * Generated model class for type ProductConfigurationTitleSummaryComponent first defined at extension ysapproductconfigaddon.
 * <p>
 * Add configuration to cart button for product configuration addon.
 */
@SuppressWarnings("all")
public class ProductConfigurationTitleSummaryComponentModel extends CMSParagraphComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductConfigurationTitleSummaryComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfigurationTitleSummaryComponent.showSummary</code> attribute defined at extension <code>ysapproductconfigaddon</code>. */
	public static final String SHOWSUMMARY = "showSummary";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductConfigurationTitleSummaryComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductConfigurationTitleSummaryComponentModel(final ItemModelContext ctx)
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
	public ProductConfigurationTitleSummaryComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public ProductConfigurationTitleSummaryComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationTitleSummaryComponent.showSummary</code> attribute defined at extension <code>ysapproductconfigaddon</code>. 
	 * @return the showSummary
	 */
	@Accessor(qualifier = "showSummary", type = Accessor.Type.GETTER)
	public Boolean getShowSummary()
	{
		return getPersistenceContext().getPropertyValue(SHOWSUMMARY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfigurationTitleSummaryComponent.showSummary</code> attribute defined at extension <code>ysapproductconfigaddon</code>. 
	 *  
	 * @param value the showSummary
	 */
	@Accessor(qualifier = "showSummary", type = Accessor.Type.SETTER)
	public void setShowSummary(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SHOWSUMMARY, value);
	}
	
}
