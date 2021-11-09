/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import de.hybris.platform.subscriptionservices.jalo.BillingEvent;
import de.hybris.platform.subscriptionservices.jalo.BillingFrequency;
import de.hybris.platform.subscriptionservices.jalo.BillingPlan;
import de.hybris.platform.subscriptionservices.jalo.BillingTime;
import de.hybris.platform.subscriptionservices.jalo.OneTimeChargeEntry;
import de.hybris.platform.subscriptionservices.jalo.OverageUsageChargeEntry;
import de.hybris.platform.subscriptionservices.jalo.PerUnitUsageCharge;
import de.hybris.platform.subscriptionservices.jalo.PriceRowsValidConstraint;
import de.hybris.platform.subscriptionservices.jalo.PromotionBillingTimeRestriction;
import de.hybris.platform.subscriptionservices.jalo.RecurringChargeEntry;
import de.hybris.platform.subscriptionservices.jalo.Subscription;
import de.hybris.platform.subscriptionservices.jalo.SubscriptionPricePlan;
import de.hybris.platform.subscriptionservices.jalo.SubscriptionProduct;
import de.hybris.platform.subscriptionservices.jalo.SubscriptionTerm;
import de.hybris.platform.subscriptionservices.jalo.TierUsageChargeEntry;
import de.hybris.platform.subscriptionservices.jalo.UsageUnit;
import de.hybris.platform.subscriptionservices.jalo.VolumeUsageCharge;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>SubscriptionservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSubscriptionservicesManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n CHILDREN's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<AbstractOrder> ABSTRACTMASTERORDER2ABSTRACTCHILDORDERRELATIONCHILDRENHANDLER = new OneToManyHandler<AbstractOrder>(
	CoreConstants.TC.ABSTRACTORDER,
	false,
	"parent",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n CHILDENTRIES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<AbstractOrderEntry> MASTERABSTRACTORDERENTRY2CHILDABSTRACTORDERENTRIESRELATIONCHILDENTRIESHANDLER = new OneToManyHandler<AbstractOrderEntry>(
	CoreConstants.TC.ABSTRACTORDERENTRY,
	false,
	"masterEntry",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("subscriptionTerm", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.product.Product", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("subscriptionServiceId", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.payment.PaymentInfo", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("billingTime", AttributeMode.INITIAL);
		tmp.put("parent", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrder", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("xmlProduct", AttributeMode.INITIAL);
		tmp.put("originalSubscriptionId", AttributeMode.INITIAL);
		tmp.put("originalOrderEntry", AttributeMode.INITIAL);
		tmp.put("masterEntry", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrderEntry", Collections.unmodifiableMap(tmp));
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
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.billingTime</code> attribute.
	 * @return the billingTime - Billing Time
	 */
	public BillingTime getBillingTime(final SessionContext ctx, final AbstractOrder item)
	{
		return (BillingTime)item.getProperty( ctx, SubscriptionservicesConstants.Attributes.AbstractOrder.BILLINGTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.billingTime</code> attribute.
	 * @return the billingTime - Billing Time
	 */
	public BillingTime getBillingTime(final AbstractOrder item)
	{
		return getBillingTime( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.billingTime</code> attribute. 
	 * @param value the billingTime - Billing Time
	 */
	public void setBillingTime(final SessionContext ctx, final AbstractOrder item, final BillingTime value)
	{
		item.setProperty(ctx, SubscriptionservicesConstants.Attributes.AbstractOrder.BILLINGTIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.billingTime</code> attribute. 
	 * @param value the billingTime - Billing Time
	 */
	public void setBillingTime(final AbstractOrder item, final BillingTime value)
	{
		setBillingTime( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.childEntries</code> attribute.
	 * @return the childEntries
	 */
	public Collection<AbstractOrderEntry> getChildEntries(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return MASTERABSTRACTORDERENTRY2CHILDABSTRACTORDERENTRIESRELATIONCHILDENTRIESHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.childEntries</code> attribute.
	 * @return the childEntries
	 */
	public Collection<AbstractOrderEntry> getChildEntries(final AbstractOrderEntry item)
	{
		return getChildEntries( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.childEntries</code> attribute. 
	 * @param value the childEntries
	 */
	public void setChildEntries(final SessionContext ctx, final AbstractOrderEntry item, final Collection<AbstractOrderEntry> value)
	{
		MASTERABSTRACTORDERENTRY2CHILDABSTRACTORDERENTRIESRELATIONCHILDENTRIESHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.childEntries</code> attribute. 
	 * @param value the childEntries
	 */
	public void setChildEntries(final AbstractOrderEntry item, final Collection<AbstractOrderEntry> value)
	{
		setChildEntries( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to childEntries. 
	 * @param value the item to add to childEntries
	 */
	public void addToChildEntries(final SessionContext ctx, final AbstractOrderEntry item, final AbstractOrderEntry value)
	{
		MASTERABSTRACTORDERENTRY2CHILDABSTRACTORDERENTRIESRELATIONCHILDENTRIESHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to childEntries. 
	 * @param value the item to add to childEntries
	 */
	public void addToChildEntries(final AbstractOrderEntry item, final AbstractOrderEntry value)
	{
		addToChildEntries( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from childEntries. 
	 * @param value the item to remove from childEntries
	 */
	public void removeFromChildEntries(final SessionContext ctx, final AbstractOrderEntry item, final AbstractOrderEntry value)
	{
		MASTERABSTRACTORDERENTRY2CHILDABSTRACTORDERENTRIESRELATIONCHILDENTRIESHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from childEntries. 
	 * @param value the item to remove from childEntries
	 */
	public void removeFromChildEntries(final AbstractOrderEntry item, final AbstractOrderEntry value)
	{
		removeFromChildEntries( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.children</code> attribute.
	 * @return the children
	 */
	public Collection<AbstractOrder> getChildren(final SessionContext ctx, final AbstractOrder item)
	{
		return ABSTRACTMASTERORDER2ABSTRACTCHILDORDERRELATIONCHILDRENHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.children</code> attribute.
	 * @return the children
	 */
	public Collection<AbstractOrder> getChildren(final AbstractOrder item)
	{
		return getChildren( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.children</code> attribute. 
	 * @param value the children
	 */
	public void setChildren(final SessionContext ctx, final AbstractOrder item, final Collection<AbstractOrder> value)
	{
		ABSTRACTMASTERORDER2ABSTRACTCHILDORDERRELATIONCHILDRENHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.children</code> attribute. 
	 * @param value the children
	 */
	public void setChildren(final AbstractOrder item, final Collection<AbstractOrder> value)
	{
		setChildren( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to children. 
	 * @param value the item to add to children
	 */
	public void addToChildren(final SessionContext ctx, final AbstractOrder item, final AbstractOrder value)
	{
		ABSTRACTMASTERORDER2ABSTRACTCHILDORDERRELATIONCHILDRENHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to children. 
	 * @param value the item to add to children
	 */
	public void addToChildren(final AbstractOrder item, final AbstractOrder value)
	{
		addToChildren( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from children. 
	 * @param value the item to remove from children
	 */
	public void removeFromChildren(final SessionContext ctx, final AbstractOrder item, final AbstractOrder value)
	{
		ABSTRACTMASTERORDER2ABSTRACTCHILDORDERRELATIONCHILDRENHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from children. 
	 * @param value the item to remove from children
	 */
	public void removeFromChildren(final AbstractOrder item, final AbstractOrder value)
	{
		removeFromChildren( getSession().getSessionContext(), item, value );
	}
	
	public BillingEvent createBillingEvent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.BILLINGEVENT );
			return (BillingEvent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating BillingEvent : "+e.getMessage(), 0 );
		}
	}
	
	public BillingEvent createBillingEvent(final Map attributeValues)
	{
		return createBillingEvent( getSession().getSessionContext(), attributeValues );
	}
	
	public BillingFrequency createBillingFrequency(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.BILLINGFREQUENCY );
			return (BillingFrequency)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating BillingFrequency : "+e.getMessage(), 0 );
		}
	}
	
	public BillingFrequency createBillingFrequency(final Map attributeValues)
	{
		return createBillingFrequency( getSession().getSessionContext(), attributeValues );
	}
	
	public BillingPlan createBillingPlan(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.BILLINGPLAN );
			return (BillingPlan)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating BillingPlan : "+e.getMessage(), 0 );
		}
	}
	
	public BillingPlan createBillingPlan(final Map attributeValues)
	{
		return createBillingPlan( getSession().getSessionContext(), attributeValues );
	}
	
	public OneTimeChargeEntry createOneTimeChargeEntry(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.ONETIMECHARGEENTRY );
			return (OneTimeChargeEntry)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating OneTimeChargeEntry : "+e.getMessage(), 0 );
		}
	}
	
	public OneTimeChargeEntry createOneTimeChargeEntry(final Map attributeValues)
	{
		return createOneTimeChargeEntry( getSession().getSessionContext(), attributeValues );
	}
	
	public OverageUsageChargeEntry createOverageUsageChargeEntry(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.OVERAGEUSAGECHARGEENTRY );
			return (OverageUsageChargeEntry)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating OverageUsageChargeEntry : "+e.getMessage(), 0 );
		}
	}
	
	public OverageUsageChargeEntry createOverageUsageChargeEntry(final Map attributeValues)
	{
		return createOverageUsageChargeEntry( getSession().getSessionContext(), attributeValues );
	}
	
	public PerUnitUsageCharge createPerUnitUsageCharge(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.PERUNITUSAGECHARGE );
			return (PerUnitUsageCharge)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating PerUnitUsageCharge : "+e.getMessage(), 0 );
		}
	}
	
	public PerUnitUsageCharge createPerUnitUsageCharge(final Map attributeValues)
	{
		return createPerUnitUsageCharge( getSession().getSessionContext(), attributeValues );
	}
	
	public PriceRowsValidConstraint createPriceRowsValidConstraint(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.PRICEROWSVALIDCONSTRAINT );
			return (PriceRowsValidConstraint)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating PriceRowsValidConstraint : "+e.getMessage(), 0 );
		}
	}
	
	public PriceRowsValidConstraint createPriceRowsValidConstraint(final Map attributeValues)
	{
		return createPriceRowsValidConstraint( getSession().getSessionContext(), attributeValues );
	}
	
	public PromotionBillingTimeRestriction createPromotionBillingTimeRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.PROMOTIONBILLINGTIMERESTRICTION );
			return (PromotionBillingTimeRestriction)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating PromotionBillingTimeRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public PromotionBillingTimeRestriction createPromotionBillingTimeRestriction(final Map attributeValues)
	{
		return createPromotionBillingTimeRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public RecurringChargeEntry createRecurringChargeEntry(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.RECURRINGCHARGEENTRY );
			return (RecurringChargeEntry)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating RecurringChargeEntry : "+e.getMessage(), 0 );
		}
	}
	
	public RecurringChargeEntry createRecurringChargeEntry(final Map attributeValues)
	{
		return createRecurringChargeEntry( getSession().getSessionContext(), attributeValues );
	}
	
	public Subscription createSubscription(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.SUBSCRIPTION );
			return (Subscription)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating Subscription : "+e.getMessage(), 0 );
		}
	}
	
	public Subscription createSubscription(final Map attributeValues)
	{
		return createSubscription( getSession().getSessionContext(), attributeValues );
	}
	
	public SubscriptionPricePlan createSubscriptionPricePlan(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.SUBSCRIPTIONPRICEPLAN );
			return (SubscriptionPricePlan)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating SubscriptionPricePlan : "+e.getMessage(), 0 );
		}
	}
	
	public SubscriptionPricePlan createSubscriptionPricePlan(final Map attributeValues)
	{
		return createSubscriptionPricePlan( getSession().getSessionContext(), attributeValues );
	}
	
	public SubscriptionProduct createSubscriptionProduct(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.SUBSCRIPTIONPRODUCT );
			return (SubscriptionProduct)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating SubscriptionProduct : "+e.getMessage(), 0 );
		}
	}
	
	public SubscriptionProduct createSubscriptionProduct(final Map attributeValues)
	{
		return createSubscriptionProduct( getSession().getSessionContext(), attributeValues );
	}
	
	public SubscriptionTerm createSubscriptionTerm(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.SUBSCRIPTIONTERM );
			return (SubscriptionTerm)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating SubscriptionTerm : "+e.getMessage(), 0 );
		}
	}
	
	public SubscriptionTerm createSubscriptionTerm(final Map attributeValues)
	{
		return createSubscriptionTerm( getSession().getSessionContext(), attributeValues );
	}
	
	public TierUsageChargeEntry createTierUsageChargeEntry(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.TIERUSAGECHARGEENTRY );
			return (TierUsageChargeEntry)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating TierUsageChargeEntry : "+e.getMessage(), 0 );
		}
	}
	
	public TierUsageChargeEntry createTierUsageChargeEntry(final Map attributeValues)
	{
		return createTierUsageChargeEntry( getSession().getSessionContext(), attributeValues );
	}
	
	public UsageUnit createUsageUnit(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.USAGEUNIT );
			return (UsageUnit)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating UsageUnit : "+e.getMessage(), 0 );
		}
	}
	
	public UsageUnit createUsageUnit(final Map attributeValues)
	{
		return createUsageUnit( getSession().getSessionContext(), attributeValues );
	}
	
	public VolumeUsageCharge createVolumeUsageCharge(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionservicesConstants.TC.VOLUMEUSAGECHARGE );
			return (VolumeUsageCharge)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating VolumeUsageCharge : "+e.getMessage(), 0 );
		}
	}
	
	public VolumeUsageCharge createVolumeUsageCharge(final Map attributeValues)
	{
		return createVolumeUsageCharge( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return SubscriptionservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.masterEntry</code> attribute.
	 * @return the masterEntry
	 */
	public AbstractOrderEntry getMasterEntry(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (AbstractOrderEntry)item.getProperty( ctx, SubscriptionservicesConstants.Attributes.AbstractOrderEntry.MASTERENTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.masterEntry</code> attribute.
	 * @return the masterEntry
	 */
	public AbstractOrderEntry getMasterEntry(final AbstractOrderEntry item)
	{
		return getMasterEntry( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.masterEntry</code> attribute. 
	 * @param value the masterEntry
	 */
	public void setMasterEntry(final SessionContext ctx, final AbstractOrderEntry item, final AbstractOrderEntry value)
	{
		item.setProperty(ctx, SubscriptionservicesConstants.Attributes.AbstractOrderEntry.MASTERENTRY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.masterEntry</code> attribute. 
	 * @param value the masterEntry
	 */
	public void setMasterEntry(final AbstractOrderEntry item, final AbstractOrderEntry value)
	{
		setMasterEntry( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.originalOrderEntry</code> attribute.
	 * @return the originalOrderEntry - YTODO to be added to the new entry configuration: Reference to the original entry of the subscription that is upgraded
	 */
	public OrderEntry getOriginalOrderEntry(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (OrderEntry)item.getProperty( ctx, SubscriptionservicesConstants.Attributes.AbstractOrderEntry.ORIGINALORDERENTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.originalOrderEntry</code> attribute.
	 * @return the originalOrderEntry - YTODO to be added to the new entry configuration: Reference to the original entry of the subscription that is upgraded
	 */
	public OrderEntry getOriginalOrderEntry(final AbstractOrderEntry item)
	{
		return getOriginalOrderEntry( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.originalOrderEntry</code> attribute. 
	 * @param value the originalOrderEntry - YTODO to be added to the new entry configuration: Reference to the original entry of the subscription that is upgraded
	 */
	public void setOriginalOrderEntry(final SessionContext ctx, final AbstractOrderEntry item, final OrderEntry value)
	{
		item.setProperty(ctx, SubscriptionservicesConstants.Attributes.AbstractOrderEntry.ORIGINALORDERENTRY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.originalOrderEntry</code> attribute. 
	 * @param value the originalOrderEntry - YTODO to be added to the new entry configuration: Reference to the original entry of the subscription that is upgraded
	 */
	public void setOriginalOrderEntry(final AbstractOrderEntry item, final OrderEntry value)
	{
		setOriginalOrderEntry( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.originalSubscriptionId</code> attribute.
	 * @return the originalSubscriptionId - YTODO to be added to the new entry configuration: Id of the original subscription that is upgraded
	 */
	public String getOriginalSubscriptionId(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, SubscriptionservicesConstants.Attributes.AbstractOrderEntry.ORIGINALSUBSCRIPTIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.originalSubscriptionId</code> attribute.
	 * @return the originalSubscriptionId - YTODO to be added to the new entry configuration: Id of the original subscription that is upgraded
	 */
	public String getOriginalSubscriptionId(final AbstractOrderEntry item)
	{
		return getOriginalSubscriptionId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.originalSubscriptionId</code> attribute. 
	 * @param value the originalSubscriptionId - YTODO to be added to the new entry configuration: Id of the original subscription that is upgraded
	 */
	public void setOriginalSubscriptionId(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, SubscriptionservicesConstants.Attributes.AbstractOrderEntry.ORIGINALSUBSCRIPTIONID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.originalSubscriptionId</code> attribute. 
	 * @param value the originalSubscriptionId - YTODO to be added to the new entry configuration: Id of the original subscription that is upgraded
	 */
	public void setOriginalSubscriptionId(final AbstractOrderEntry item, final String value)
	{
		setOriginalSubscriptionId( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.parent</code> attribute.
	 * @return the parent
	 */
	public AbstractOrder getParent(final SessionContext ctx, final AbstractOrder item)
	{
		return (AbstractOrder)item.getProperty( ctx, SubscriptionservicesConstants.Attributes.AbstractOrder.PARENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.parent</code> attribute.
	 * @return the parent
	 */
	public AbstractOrder getParent(final AbstractOrder item)
	{
		return getParent( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.parent</code> attribute. 
	 * @param value the parent
	 */
	public void setParent(final SessionContext ctx, final AbstractOrder item, final AbstractOrder value)
	{
		item.setProperty(ctx, SubscriptionservicesConstants.Attributes.AbstractOrder.PARENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.parent</code> attribute. 
	 * @param value the parent
	 */
	public void setParent(final AbstractOrder item, final AbstractOrder value)
	{
		setParent( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.subscriptionServiceId</code> attribute.
	 * @return the subscriptionServiceId - Subscription Service ID
	 */
	public String getSubscriptionServiceId(final SessionContext ctx, final PaymentInfo item)
	{
		return (String)item.getProperty( ctx, SubscriptionservicesConstants.Attributes.CreditCardPaymentInfo.SUBSCRIPTIONSERVICEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.subscriptionServiceId</code> attribute.
	 * @return the subscriptionServiceId - Subscription Service ID
	 */
	public String getSubscriptionServiceId(final PaymentInfo item)
	{
		return getSubscriptionServiceId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CreditCardPaymentInfo.subscriptionServiceId</code> attribute. 
	 * @param value the subscriptionServiceId - Subscription Service ID
	 */
	public void setSubscriptionServiceId(final SessionContext ctx, final PaymentInfo item, final String value)
	{
		item.setProperty(ctx, SubscriptionservicesConstants.Attributes.CreditCardPaymentInfo.SUBSCRIPTIONSERVICEID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CreditCardPaymentInfo.subscriptionServiceId</code> attribute. 
	 * @param value the subscriptionServiceId - Subscription Service ID
	 */
	public void setSubscriptionServiceId(final PaymentInfo item, final String value)
	{
		setSubscriptionServiceId( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.subscriptionTerm</code> attribute.
	 * @return the subscriptionTerm
	 */
	public SubscriptionTerm getSubscriptionTerm(final SessionContext ctx, final Product item)
	{
		return (SubscriptionTerm)item.getProperty( ctx, SubscriptionservicesConstants.Attributes.Product.SUBSCRIPTIONTERM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.subscriptionTerm</code> attribute.
	 * @return the subscriptionTerm
	 */
	public SubscriptionTerm getSubscriptionTerm(final Product item)
	{
		return getSubscriptionTerm( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.subscriptionTerm</code> attribute. 
	 * @param value the subscriptionTerm
	 */
	public void setSubscriptionTerm(final SessionContext ctx, final Product item, final SubscriptionTerm value)
	{
		item.setProperty(ctx, SubscriptionservicesConstants.Attributes.Product.SUBSCRIPTIONTERM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.subscriptionTerm</code> attribute. 
	 * @param value the subscriptionTerm
	 */
	public void setSubscriptionTerm(final Product item, final SubscriptionTerm value)
	{
		setSubscriptionTerm( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.xmlProduct</code> attribute.
	 * @return the xmlProduct - xmlProduct
	 */
	public String getXmlProduct(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, SubscriptionservicesConstants.Attributes.AbstractOrderEntry.XMLPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.xmlProduct</code> attribute.
	 * @return the xmlProduct - xmlProduct
	 */
	public String getXmlProduct(final AbstractOrderEntry item)
	{
		return getXmlProduct( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.xmlProduct</code> attribute. 
	 * @param value the xmlProduct - xmlProduct
	 */
	public void setXmlProduct(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, SubscriptionservicesConstants.Attributes.AbstractOrderEntry.XMLPRODUCT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.xmlProduct</code> attribute. 
	 * @param value the xmlProduct - xmlProduct
	 */
	public void setXmlProduct(final AbstractOrderEntry item, final String value)
	{
		setXmlProduct( getSession().getSessionContext(), item, value );
	}
	
}
