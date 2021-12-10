/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.timedaccesspromotionengineservices.constants.TimedaccesspromotionengineservicesConstants;
import de.hybris.platform.timedaccesspromotionengineservices.jalo.FlashBuyCoupon;
import de.hybris.platform.timedaccesspromotionengineservices.jalo.FlashBuyCronJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>TimedaccesspromotionengineservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedTimedaccesspromotionengineservicesManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("processingFlashBuyOrder", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.Cart", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public FlashBuyCoupon createFlashBuyCoupon(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( TimedaccesspromotionengineservicesConstants.TC.FLASHBUYCOUPON );
			return (FlashBuyCoupon)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating FlashBuyCoupon : "+e.getMessage(), 0 );
		}
	}
	
	public FlashBuyCoupon createFlashBuyCoupon(final Map attributeValues)
	{
		return createFlashBuyCoupon( getSession().getSessionContext(), attributeValues );
	}
	
	public FlashBuyCronJob createFlashBuyCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( TimedaccesspromotionengineservicesConstants.TC.FLASHBUYCRONJOB );
			return (FlashBuyCronJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating FlashBuyCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public FlashBuyCronJob createFlashBuyCronJob(final Map attributeValues)
	{
		return createFlashBuyCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return TimedaccesspromotionengineservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.processingFlashBuyOrder</code> attribute.
	 * @return the processingFlashBuyOrder - set the status for the flashbuy
	 */
	public Boolean isProcessingFlashBuyOrder(final SessionContext ctx, final Cart item)
	{
		return (Boolean)item.getProperty( ctx, TimedaccesspromotionengineservicesConstants.Attributes.Cart.PROCESSINGFLASHBUYORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.processingFlashBuyOrder</code> attribute.
	 * @return the processingFlashBuyOrder - set the status for the flashbuy
	 */
	public Boolean isProcessingFlashBuyOrder(final Cart item)
	{
		return isProcessingFlashBuyOrder( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.processingFlashBuyOrder</code> attribute. 
	 * @return the processingFlashBuyOrder - set the status for the flashbuy
	 */
	public boolean isProcessingFlashBuyOrderAsPrimitive(final SessionContext ctx, final Cart item)
	{
		Boolean value = isProcessingFlashBuyOrder( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.processingFlashBuyOrder</code> attribute. 
	 * @return the processingFlashBuyOrder - set the status for the flashbuy
	 */
	public boolean isProcessingFlashBuyOrderAsPrimitive(final Cart item)
	{
		return isProcessingFlashBuyOrderAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.processingFlashBuyOrder</code> attribute. 
	 * @param value the processingFlashBuyOrder - set the status for the flashbuy
	 */
	public void setProcessingFlashBuyOrder(final SessionContext ctx, final Cart item, final Boolean value)
	{
		item.setProperty(ctx, TimedaccesspromotionengineservicesConstants.Attributes.Cart.PROCESSINGFLASHBUYORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.processingFlashBuyOrder</code> attribute. 
	 * @param value the processingFlashBuyOrder - set the status for the flashbuy
	 */
	public void setProcessingFlashBuyOrder(final Cart item, final Boolean value)
	{
		setProcessingFlashBuyOrder( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.processingFlashBuyOrder</code> attribute. 
	 * @param value the processingFlashBuyOrder - set the status for the flashbuy
	 */
	public void setProcessingFlashBuyOrder(final SessionContext ctx, final Cart item, final boolean value)
	{
		setProcessingFlashBuyOrder( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.processingFlashBuyOrder</code> attribute. 
	 * @param value the processingFlashBuyOrder - set the status for the flashbuy
	 */
	public void setProcessingFlashBuyOrder(final Cart item, final boolean value)
	{
		setProcessingFlashBuyOrder( getSession().getSessionContext(), item, value );
	}
	
}
