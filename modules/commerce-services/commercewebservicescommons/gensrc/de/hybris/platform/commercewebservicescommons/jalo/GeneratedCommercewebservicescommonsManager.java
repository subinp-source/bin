/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.jalo;

import de.hybris.platform.commercewebservicescommons.constants.CommercewebservicescommonsConstants;
import de.hybris.platform.commercewebservicescommons.jalo.OldCartRemovalCronJob;
import de.hybris.platform.commercewebservicescommons.jalo.expressupdate.cron.OrderStatusUpdateCleanerCronJob;
import de.hybris.platform.commercewebservicescommons.jalo.expressupdate.cron.ProductExpressUpdateCleanerCronJob;
import de.hybris.platform.commercewebservicescommons.jalo.payment.OldPaymentSubscriptionResultRemovalCronJob;
import de.hybris.platform.commercewebservicescommons.jalo.payment.PaymentSubscriptionResult;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>CommercewebservicescommonsManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedCommercewebservicescommonsManager extends Extension
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
	
	public OldCartRemovalCronJob createOldCartRemovalCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommercewebservicescommonsConstants.TC.OLDCARTREMOVALCRONJOB );
			return (OldCartRemovalCronJob)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating OldCartRemovalCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public OldCartRemovalCronJob createOldCartRemovalCronJob(final Map attributeValues)
	{
		return createOldCartRemovalCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public OldPaymentSubscriptionResultRemovalCronJob createOldPaymentSubscriptionResultRemovalCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommercewebservicescommonsConstants.TC.OLDPAYMENTSUBSCRIPTIONRESULTREMOVALCRONJOB );
			return (OldPaymentSubscriptionResultRemovalCronJob)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating OldPaymentSubscriptionResultRemovalCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public OldPaymentSubscriptionResultRemovalCronJob createOldPaymentSubscriptionResultRemovalCronJob(final Map attributeValues)
	{
		return createOldPaymentSubscriptionResultRemovalCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public OrderStatusUpdateCleanerCronJob createOrderStatusUpdateCleanerCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommercewebservicescommonsConstants.TC.ORDERSTATUSUPDATECLEANERCRONJOB );
			return (OrderStatusUpdateCleanerCronJob)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating OrderStatusUpdateCleanerCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public OrderStatusUpdateCleanerCronJob createOrderStatusUpdateCleanerCronJob(final Map attributeValues)
	{
		return createOrderStatusUpdateCleanerCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public PaymentSubscriptionResult createPaymentSubscriptionResult(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommercewebservicescommonsConstants.TC.PAYMENTSUBSCRIPTIONRESULT );
			return (PaymentSubscriptionResult)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating PaymentSubscriptionResult : "+e.getMessage(), 0 );
		}
	}
	
	public PaymentSubscriptionResult createPaymentSubscriptionResult(final Map attributeValues)
	{
		return createPaymentSubscriptionResult( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductExpressUpdateCleanerCronJob createProductExpressUpdateCleanerCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CommercewebservicescommonsConstants.TC.PRODUCTEXPRESSUPDATECLEANERCRONJOB );
			return (ProductExpressUpdateCleanerCronJob)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductExpressUpdateCleanerCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public ProductExpressUpdateCleanerCronJob createProductExpressUpdateCleanerCronJob(final Map attributeValues)
	{
		return createProductExpressUpdateCleanerCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return CommercewebservicescommonsConstants.EXTENSIONNAME;
	}
	
}
