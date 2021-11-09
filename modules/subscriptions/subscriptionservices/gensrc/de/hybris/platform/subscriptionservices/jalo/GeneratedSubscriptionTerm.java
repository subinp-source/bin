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
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import de.hybris.platform.subscriptionservices.jalo.BillingPlan;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.SubscriptionTerm SubscriptionTerm}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSubscriptionTerm extends GenericItem
{
	/** Qualifier of the <code>SubscriptionTerm.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>SubscriptionTerm.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>SubscriptionTerm.termOfServiceNumber</code> attribute **/
	public static final String TERMOFSERVICENUMBER = "termOfServiceNumber";
	/** Qualifier of the <code>SubscriptionTerm.cancellable</code> attribute **/
	public static final String CANCELLABLE = "cancellable";
	/** Qualifier of the <code>SubscriptionTerm.termOfServiceRenewal</code> attribute **/
	public static final String TERMOFSERVICERENEWAL = "termOfServiceRenewal";
	/** Qualifier of the <code>SubscriptionTerm.termOfServiceFrequency</code> attribute **/
	public static final String TERMOFSERVICEFREQUENCY = "termOfServiceFrequency";
	/** Qualifier of the <code>SubscriptionTerm.billingPlan</code> attribute **/
	public static final String BILLINGPLAN = "billingPlan";
	/** Qualifier of the <code>SubscriptionTerm.subscriptionProducts</code> attribute **/
	public static final String SUBSCRIPTIONPRODUCTS = "subscriptionProducts";
	/**
	* {@link OneToManyHandler} for handling 1:n SUBSCRIPTIONPRODUCTS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Product> SUBSCRIPTIONPRODUCTSHANDLER = new OneToManyHandler<Product>(
	CoreConstants.TC.PRODUCT,
	false,
	"subscriptionTerm",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(TERMOFSERVICENUMBER, AttributeMode.INITIAL);
		tmp.put(CANCELLABLE, AttributeMode.INITIAL);
		tmp.put(TERMOFSERVICERENEWAL, AttributeMode.INITIAL);
		tmp.put(TERMOFSERVICEFREQUENCY, AttributeMode.INITIAL);
		tmp.put(BILLINGPLAN, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.billingPlan</code> attribute.
	 * @return the billingPlan - Billing Plan
	 */
	public BillingPlan getBillingPlan(final SessionContext ctx)
	{
		return (BillingPlan)getProperty( ctx, BILLINGPLAN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.billingPlan</code> attribute.
	 * @return the billingPlan - Billing Plan
	 */
	public BillingPlan getBillingPlan()
	{
		return getBillingPlan( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.billingPlan</code> attribute. 
	 * @param value the billingPlan - Billing Plan
	 */
	public void setBillingPlan(final SessionContext ctx, final BillingPlan value)
	{
		setProperty(ctx, BILLINGPLAN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.billingPlan</code> attribute. 
	 * @param value the billingPlan - Billing Plan
	 */
	public void setBillingPlan(final BillingPlan value)
	{
		setBillingPlan( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.cancellable</code> attribute.
	 * @return the cancellable - Cancellable
	 */
	public Boolean isCancellable(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, CANCELLABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.cancellable</code> attribute.
	 * @return the cancellable - Cancellable
	 */
	public Boolean isCancellable()
	{
		return isCancellable( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.cancellable</code> attribute. 
	 * @return the cancellable - Cancellable
	 */
	public boolean isCancellableAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isCancellable( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.cancellable</code> attribute. 
	 * @return the cancellable - Cancellable
	 */
	public boolean isCancellableAsPrimitive()
	{
		return isCancellableAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.cancellable</code> attribute. 
	 * @param value the cancellable - Cancellable
	 */
	public void setCancellable(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, CANCELLABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.cancellable</code> attribute. 
	 * @param value the cancellable - Cancellable
	 */
	public void setCancellable(final Boolean value)
	{
		setCancellable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.cancellable</code> attribute. 
	 * @param value the cancellable - Cancellable
	 */
	public void setCancellable(final SessionContext ctx, final boolean value)
	{
		setCancellable( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.cancellable</code> attribute. 
	 * @param value the cancellable - Cancellable
	 */
	public void setCancellable(final boolean value)
	{
		setCancellable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+ID+"' is not changeable", 0 );
		}
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSubscriptionTerm.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSubscriptionTerm.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.subscriptionProducts</code> attribute.
	 * @return the subscriptionProducts
	 */
	public Collection<Product> getSubscriptionProducts(final SessionContext ctx)
	{
		return SUBSCRIPTIONPRODUCTSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.subscriptionProducts</code> attribute.
	 * @return the subscriptionProducts
	 */
	public Collection<Product> getSubscriptionProducts()
	{
		return getSubscriptionProducts( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.subscriptionProducts</code> attribute. 
	 * @param value the subscriptionProducts
	 */
	public void setSubscriptionProducts(final SessionContext ctx, final Collection<Product> value)
	{
		SUBSCRIPTIONPRODUCTSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.subscriptionProducts</code> attribute. 
	 * @param value the subscriptionProducts
	 */
	public void setSubscriptionProducts(final Collection<Product> value)
	{
		setSubscriptionProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to subscriptionProducts. 
	 * @param value the item to add to subscriptionProducts
	 */
	public void addToSubscriptionProducts(final SessionContext ctx, final Product value)
	{
		SUBSCRIPTIONPRODUCTSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to subscriptionProducts. 
	 * @param value the item to add to subscriptionProducts
	 */
	public void addToSubscriptionProducts(final Product value)
	{
		addToSubscriptionProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from subscriptionProducts. 
	 * @param value the item to remove from subscriptionProducts
	 */
	public void removeFromSubscriptionProducts(final SessionContext ctx, final Product value)
	{
		SUBSCRIPTIONPRODUCTSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from subscriptionProducts. 
	 * @param value the item to remove from subscriptionProducts
	 */
	public void removeFromSubscriptionProducts(final Product value)
	{
		removeFromSubscriptionProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.termOfServiceFrequency</code> attribute.
	 * @return the termOfServiceFrequency - Term of Service Frequency
	 */
	public EnumerationValue getTermOfServiceFrequency(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, TERMOFSERVICEFREQUENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.termOfServiceFrequency</code> attribute.
	 * @return the termOfServiceFrequency - Term of Service Frequency
	 */
	public EnumerationValue getTermOfServiceFrequency()
	{
		return getTermOfServiceFrequency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.termOfServiceFrequency</code> attribute. 
	 * @param value the termOfServiceFrequency - Term of Service Frequency
	 */
	public void setTermOfServiceFrequency(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, TERMOFSERVICEFREQUENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.termOfServiceFrequency</code> attribute. 
	 * @param value the termOfServiceFrequency - Term of Service Frequency
	 */
	public void setTermOfServiceFrequency(final EnumerationValue value)
	{
		setTermOfServiceFrequency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.termOfServiceNumber</code> attribute.
	 * @return the termOfServiceNumber - Term of Service Number
	 */
	public Integer getTermOfServiceNumber(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, TERMOFSERVICENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.termOfServiceNumber</code> attribute.
	 * @return the termOfServiceNumber - Term of Service Number
	 */
	public Integer getTermOfServiceNumber()
	{
		return getTermOfServiceNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.termOfServiceNumber</code> attribute. 
	 * @return the termOfServiceNumber - Term of Service Number
	 */
	public int getTermOfServiceNumberAsPrimitive(final SessionContext ctx)
	{
		Integer value = getTermOfServiceNumber( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.termOfServiceNumber</code> attribute. 
	 * @return the termOfServiceNumber - Term of Service Number
	 */
	public int getTermOfServiceNumberAsPrimitive()
	{
		return getTermOfServiceNumberAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.termOfServiceNumber</code> attribute. 
	 * @param value the termOfServiceNumber - Term of Service Number
	 */
	public void setTermOfServiceNumber(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, TERMOFSERVICENUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.termOfServiceNumber</code> attribute. 
	 * @param value the termOfServiceNumber - Term of Service Number
	 */
	public void setTermOfServiceNumber(final Integer value)
	{
		setTermOfServiceNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.termOfServiceNumber</code> attribute. 
	 * @param value the termOfServiceNumber - Term of Service Number
	 */
	public void setTermOfServiceNumber(final SessionContext ctx, final int value)
	{
		setTermOfServiceNumber( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.termOfServiceNumber</code> attribute. 
	 * @param value the termOfServiceNumber - Term of Service Number
	 */
	public void setTermOfServiceNumber(final int value)
	{
		setTermOfServiceNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.termOfServiceRenewal</code> attribute.
	 * @return the termOfServiceRenewal - Term of Service Renewal
	 */
	public EnumerationValue getTermOfServiceRenewal(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, TERMOFSERVICERENEWAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionTerm.termOfServiceRenewal</code> attribute.
	 * @return the termOfServiceRenewal - Term of Service Renewal
	 */
	public EnumerationValue getTermOfServiceRenewal()
	{
		return getTermOfServiceRenewal( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.termOfServiceRenewal</code> attribute. 
	 * @param value the termOfServiceRenewal - Term of Service Renewal
	 */
	public void setTermOfServiceRenewal(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, TERMOFSERVICERENEWAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionTerm.termOfServiceRenewal</code> attribute. 
	 * @param value the termOfServiceRenewal - Term of Service Renewal
	 */
	public void setTermOfServiceRenewal(final EnumerationValue value)
	{
		setTermOfServiceRenewal( getSession().getSessionContext(), value );
	}
	
}
