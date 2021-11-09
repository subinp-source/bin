/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PromotionOrderChangeDeliveryModeAction first defined at extension promotions.
 */
@SuppressWarnings("all")
public class PromotionOrderChangeDeliveryModeActionModel extends AbstractPromotionActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PromotionOrderChangeDeliveryModeAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderChangeDeliveryModeAction.deliveryMode</code> attribute defined at extension <code>promotions</code>. */
	public static final String DELIVERYMODE = "deliveryMode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PromotionOrderChangeDeliveryModeActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PromotionOrderChangeDeliveryModeActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _deliveryMode initial attribute declared by type <code>PromotionOrderChangeDeliveryModeAction</code> at extension <code>promotions</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public PromotionOrderChangeDeliveryModeActionModel(final DeliveryModeModel _deliveryMode)
	{
		super();
		setDeliveryMode(_deliveryMode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _deliveryMode initial attribute declared by type <code>PromotionOrderChangeDeliveryModeAction</code> at extension <code>promotions</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public PromotionOrderChangeDeliveryModeActionModel(final DeliveryModeModel _deliveryMode, final ItemModel _owner)
	{
		super();
		setDeliveryMode(_deliveryMode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderChangeDeliveryModeAction.deliveryMode</code> attribute defined at extension <code>promotions</code>. 
	 * @return the deliveryMode - The delivery mode to apply to the order
	 */
	@Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
	public DeliveryModeModel getDeliveryMode()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYMODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderChangeDeliveryModeAction.deliveryMode</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the deliveryMode - The delivery mode to apply to the order
	 */
	@Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
	public void setDeliveryMode(final DeliveryModeModel value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYMODE, value);
	}
	
}
