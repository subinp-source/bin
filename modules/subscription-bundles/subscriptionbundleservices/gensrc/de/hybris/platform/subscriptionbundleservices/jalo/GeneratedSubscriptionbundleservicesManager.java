/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionbundleservices.jalo;

import de.hybris.platform.configurablebundleservices.constants.ConfigurableBundleServicesConstants;
import de.hybris.platform.configurablebundleservices.jalo.AbstractBundleRule;
import de.hybris.platform.configurablebundleservices.jalo.AutoPickBundleSelectionCriteria;
import de.hybris.platform.configurablebundleservices.jalo.ChangeProductPriceBundleRule;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.subscriptionbundleservices.constants.SubscriptionbundleservicesConstants;
import de.hybris.platform.subscriptionservices.jalo.BillingEvent;
import de.hybris.platform.subscriptionservices.jalo.BillingTime;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>SubscriptionbundleservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSubscriptionbundleservicesManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n CHANGEPRODUCTPRICEBUNDLERULES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ChangeProductPriceBundleRule> BILLINGEVENT2CHANGEPRODUCTPRICEBUNDLERULECHANGEPRODUCTPRICEBUNDLERULESHANDLER = new OneToManyHandler<ChangeProductPriceBundleRule>(
	ConfigurableBundleServicesConstants.TC.CHANGEPRODUCTPRICEBUNDLERULE,
	false,
	"billingEvent",
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
		tmp.put("billingEvent", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.configurablebundleservices.jalo.ChangeProductPriceBundleRule", Collections.unmodifiableMap(tmp));
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
	 * <i>Generated method</i> - Getter of the <code>ChangeProductPriceBundleRule.billingEvent</code> attribute.
	 * @return the billingEvent
	 */
	public BillingEvent getBillingEvent(final SessionContext ctx, final ChangeProductPriceBundleRule item)
	{
		return (BillingEvent)item.getProperty( ctx, SubscriptionbundleservicesConstants.Attributes.ChangeProductPriceBundleRule.BILLINGEVENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeProductPriceBundleRule.billingEvent</code> attribute.
	 * @return the billingEvent
	 */
	public BillingEvent getBillingEvent(final ChangeProductPriceBundleRule item)
	{
		return getBillingEvent( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ChangeProductPriceBundleRule.billingEvent</code> attribute. 
	 * @param value the billingEvent
	 */
	public void setBillingEvent(final SessionContext ctx, final ChangeProductPriceBundleRule item, final BillingEvent value)
	{
		item.setProperty(ctx, SubscriptionbundleservicesConstants.Attributes.ChangeProductPriceBundleRule.BILLINGEVENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ChangeProductPriceBundleRule.billingEvent</code> attribute. 
	 * @param value the billingEvent
	 */
	public void setBillingEvent(final ChangeProductPriceBundleRule item, final BillingEvent value)
	{
		setBillingEvent( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingEvent.changeProductPriceBundleRules</code> attribute.
	 * @return the changeProductPriceBundleRules
	 */
	public Collection<ChangeProductPriceBundleRule> getChangeProductPriceBundleRules(final SessionContext ctx, final BillingEvent item)
	{
		return BILLINGEVENT2CHANGEPRODUCTPRICEBUNDLERULECHANGEPRODUCTPRICEBUNDLERULESHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingEvent.changeProductPriceBundleRules</code> attribute.
	 * @return the changeProductPriceBundleRules
	 */
	public Collection<ChangeProductPriceBundleRule> getChangeProductPriceBundleRules(final BillingEvent item)
	{
		return getChangeProductPriceBundleRules( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingEvent.changeProductPriceBundleRules</code> attribute. 
	 * @param value the changeProductPriceBundleRules
	 */
	public void setChangeProductPriceBundleRules(final SessionContext ctx, final BillingEvent item, final Collection<ChangeProductPriceBundleRule> value)
	{
		BILLINGEVENT2CHANGEPRODUCTPRICEBUNDLERULECHANGEPRODUCTPRICEBUNDLERULESHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingEvent.changeProductPriceBundleRules</code> attribute. 
	 * @param value the changeProductPriceBundleRules
	 */
	public void setChangeProductPriceBundleRules(final BillingEvent item, final Collection<ChangeProductPriceBundleRule> value)
	{
		setChangeProductPriceBundleRules( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to changeProductPriceBundleRules. 
	 * @param value the item to add to changeProductPriceBundleRules
	 */
	public void addToChangeProductPriceBundleRules(final SessionContext ctx, final BillingEvent item, final ChangeProductPriceBundleRule value)
	{
		BILLINGEVENT2CHANGEPRODUCTPRICEBUNDLERULECHANGEPRODUCTPRICEBUNDLERULESHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to changeProductPriceBundleRules. 
	 * @param value the item to add to changeProductPriceBundleRules
	 */
	public void addToChangeProductPriceBundleRules(final BillingEvent item, final ChangeProductPriceBundleRule value)
	{
		addToChangeProductPriceBundleRules( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from changeProductPriceBundleRules. 
	 * @param value the item to remove from changeProductPriceBundleRules
	 */
	public void removeFromChangeProductPriceBundleRules(final SessionContext ctx, final BillingEvent item, final ChangeProductPriceBundleRule value)
	{
		BILLINGEVENT2CHANGEPRODUCTPRICEBUNDLERULECHANGEPRODUCTPRICEBUNDLERULESHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from changeProductPriceBundleRules. 
	 * @param value the item to remove from changeProductPriceBundleRules
	 */
	public void removeFromChangeProductPriceBundleRules(final BillingEvent item, final ChangeProductPriceBundleRule value)
	{
		removeFromChangeProductPriceBundleRules( getSession().getSessionContext(), item, value );
	}
	
	public AutoPickBundleSelectionCriteria createAutoPickBundleSelectionCriteria(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionbundleservicesConstants.TC.AUTOPICKBUNDLESELECTIONCRITERIA );
			return (AutoPickBundleSelectionCriteria)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating AutoPickBundleSelectionCriteria : "+e.getMessage(), 0 );
		}
	}
	
	public AutoPickBundleSelectionCriteria createAutoPickBundleSelectionCriteria(final Map attributeValues)
	{
		return createAutoPickBundleSelectionCriteria( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return SubscriptionbundleservicesConstants.EXTENSIONNAME;
	}
	
}
