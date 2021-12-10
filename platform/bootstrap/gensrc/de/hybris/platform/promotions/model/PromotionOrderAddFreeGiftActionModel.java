/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PromotionOrderAddFreeGiftAction first defined at extension promotions.
 */
@SuppressWarnings("all")
public class PromotionOrderAddFreeGiftActionModel extends AbstractPromotionActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PromotionOrderAddFreeGiftAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderAddFreeGiftAction.freeProduct</code> attribute defined at extension <code>promotions</code>. */
	public static final String FREEPRODUCT = "freeProduct";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PromotionOrderAddFreeGiftActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PromotionOrderAddFreeGiftActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _freeProduct initial attribute declared by type <code>PromotionOrderAddFreeGiftAction</code> at extension <code>promotions</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public PromotionOrderAddFreeGiftActionModel(final ProductModel _freeProduct)
	{
		super();
		setFreeProduct(_freeProduct);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _freeProduct initial attribute declared by type <code>PromotionOrderAddFreeGiftAction</code> at extension <code>promotions</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public PromotionOrderAddFreeGiftActionModel(final ProductModel _freeProduct, final ItemModel _owner)
	{
		super();
		setFreeProduct(_freeProduct);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderAddFreeGiftAction.freeProduct</code> attribute defined at extension <code>promotions</code>. 
	 * @return the freeProduct - The product given away as a gift
	 */
	@Accessor(qualifier = "freeProduct", type = Accessor.Type.GETTER)
	public ProductModel getFreeProduct()
	{
		return getPersistenceContext().getPropertyValue(FREEPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderAddFreeGiftAction.freeProduct</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the freeProduct - The product given away as a gift
	 */
	@Accessor(qualifier = "freeProduct", type = Accessor.Type.SETTER)
	public void setFreeProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(FREEPRODUCT, value);
	}
	
}
