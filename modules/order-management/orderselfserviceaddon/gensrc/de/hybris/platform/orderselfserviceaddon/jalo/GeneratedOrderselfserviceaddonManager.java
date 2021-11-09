/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderselfserviceaddon.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.orderselfserviceaddon.constants.OrderselfserviceaddonConstants;
import de.hybris.platform.orderselfserviceaddon.jalo.actions.CancelOrderAction;
import de.hybris.platform.orderselfserviceaddon.jalo.actions.CancelReturnAction;
import de.hybris.platform.orderselfserviceaddon.jalo.actions.ReturnOrderAction;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>OrderselfserviceaddonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedOrderselfserviceaddonManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
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
	
	public CancelOrderAction createCancelOrderAction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( OrderselfserviceaddonConstants.TC.CANCELORDERACTION );
			return (CancelOrderAction)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating CancelOrderAction : "+e.getMessage(), 0 );
		}
	}
	
	public CancelOrderAction createCancelOrderAction(final Map attributeValues)
	{
		return createCancelOrderAction( getSession().getSessionContext(), attributeValues );
	}
	
	public CancelReturnAction createCancelReturnAction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( OrderselfserviceaddonConstants.TC.CANCELRETURNACTION );
			return (CancelReturnAction)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating CancelReturnAction : "+e.getMessage(), 0 );
		}
	}
	
	public CancelReturnAction createCancelReturnAction(final Map attributeValues)
	{
		return createCancelReturnAction( getSession().getSessionContext(), attributeValues );
	}
	
	public ReturnOrderAction createReturnOrderAction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( OrderselfserviceaddonConstants.TC.RETURNORDERACTION );
			return (ReturnOrderAction)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ReturnOrderAction : "+e.getMessage(), 0 );
		}
	}
	
	public ReturnOrderAction createReturnOrderAction(final Map attributeValues)
	{
		return createReturnOrderAction( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return OrderselfserviceaddonConstants.EXTENSIONNAME;
	}
	
}
