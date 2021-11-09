/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsservices.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.customerinterestsservices.constants.CustomerinterestsservicesConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem ProductInterest}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedProductInterest extends GenericItem
{
	/** Qualifier of the <code>ProductInterest.expiryDate</code> attribute **/
	public static final String EXPIRYDATE = "expiryDate";
	/** Qualifier of the <code>ProductInterest.baseStore</code> attribute **/
	public static final String BASESTORE = "baseStore";
	/** Qualifier of the <code>ProductInterest.notificationType</code> attribute **/
	public static final String NOTIFICATIONTYPE = "notificationType";
	/** Qualifier of the <code>ProductInterest.emailEnabled</code> attribute **/
	public static final String EMAILENABLED = "emailEnabled";
	/** Qualifier of the <code>ProductInterest.smsEnabled</code> attribute **/
	public static final String SMSENABLED = "smsEnabled";
	/** Qualifier of the <code>ProductInterest.notificationChannels</code> attribute **/
	public static final String NOTIFICATIONCHANNELS = "notificationChannels";
	/** Qualifier of the <code>ProductInterest.baseSite</code> attribute **/
	public static final String BASESITE = "baseSite";
	/** Qualifier of the <code>ProductInterest.language</code> attribute **/
	public static final String LANGUAGE = "language";
	/** Qualifier of the <code>ProductInterest.customer</code> attribute **/
	public static final String CUSTOMER = "customer";
	/** Qualifier of the <code>ProductInterest.product</code> attribute **/
	public static final String PRODUCT = "product";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n CUSTOMER's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedProductInterest> CUSTOMERHANDLER = new BidirectionalOneToManyHandler<GeneratedProductInterest>(
	CustomerinterestsservicesConstants.TC.PRODUCTINTEREST,
	false,
	"customer",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n PRODUCT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedProductInterest> PRODUCTHANDLER = new BidirectionalOneToManyHandler<GeneratedProductInterest>(
	CustomerinterestsservicesConstants.TC.PRODUCTINTEREST,
	false,
	"product",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(EXPIRYDATE, AttributeMode.INITIAL);
		tmp.put(BASESTORE, AttributeMode.INITIAL);
		tmp.put(NOTIFICATIONTYPE, AttributeMode.INITIAL);
		tmp.put(EMAILENABLED, AttributeMode.INITIAL);
		tmp.put(SMSENABLED, AttributeMode.INITIAL);
		tmp.put(NOTIFICATIONCHANNELS, AttributeMode.INITIAL);
		tmp.put(BASESITE, AttributeMode.INITIAL);
		tmp.put(LANGUAGE, AttributeMode.INITIAL);
		tmp.put(CUSTOMER, AttributeMode.INITIAL);
		tmp.put(PRODUCT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.baseSite</code> attribute.
	 * @return the baseSite - Attribute contains base site object that will be used in the process.
	 */
	public BaseSite getBaseSite(final SessionContext ctx)
	{
		return (BaseSite)getProperty( ctx, BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.baseSite</code> attribute.
	 * @return the baseSite - Attribute contains base site object that will be used in the process.
	 */
	public BaseSite getBaseSite()
	{
		return getBaseSite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.baseSite</code> attribute. 
	 * @param value the baseSite - Attribute contains base site object that will be used in the process.
	 */
	public void setBaseSite(final SessionContext ctx, final BaseSite value)
	{
		setProperty(ctx, BASESITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.baseSite</code> attribute. 
	 * @param value the baseSite - Attribute contains base site object that will be used in the process.
	 */
	public void setBaseSite(final BaseSite value)
	{
		setBaseSite( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.baseStore</code> attribute.
	 * @return the baseStore
	 */
	public BaseStore getBaseStore(final SessionContext ctx)
	{
		return (BaseStore)getProperty( ctx, BASESTORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.baseStore</code> attribute.
	 * @return the baseStore
	 */
	public BaseStore getBaseStore()
	{
		return getBaseStore( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.baseStore</code> attribute. 
	 * @param value the baseStore
	 */
	public void setBaseStore(final SessionContext ctx, final BaseStore value)
	{
		setProperty(ctx, BASESTORE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.baseStore</code> attribute. 
	 * @param value the baseStore
	 */
	public void setBaseStore(final BaseStore value)
	{
		setBaseStore( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		CUSTOMERHANDLER.newInstance(ctx, allAttributes);
		PRODUCTHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.customer</code> attribute.
	 * @return the customer
	 */
	public Customer getCustomer(final SessionContext ctx)
	{
		return (Customer)getProperty( ctx, CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.customer</code> attribute.
	 * @return the customer
	 */
	public Customer getCustomer()
	{
		return getCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final SessionContext ctx, final Customer value)
	{
		CUSTOMERHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final Customer value)
	{
		setCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.emailEnabled</code> attribute.
	 * @return the emailEnabled
	 */
	public Boolean isEmailEnabled(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, EMAILENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.emailEnabled</code> attribute.
	 * @return the emailEnabled
	 */
	public Boolean isEmailEnabled()
	{
		return isEmailEnabled( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.emailEnabled</code> attribute. 
	 * @return the emailEnabled
	 */
	public boolean isEmailEnabledAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isEmailEnabled( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.emailEnabled</code> attribute. 
	 * @return the emailEnabled
	 */
	public boolean isEmailEnabledAsPrimitive()
	{
		return isEmailEnabledAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.emailEnabled</code> attribute. 
	 * @param value the emailEnabled
	 */
	public void setEmailEnabled(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, EMAILENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.emailEnabled</code> attribute. 
	 * @param value the emailEnabled
	 */
	public void setEmailEnabled(final Boolean value)
	{
		setEmailEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.emailEnabled</code> attribute. 
	 * @param value the emailEnabled
	 */
	public void setEmailEnabled(final SessionContext ctx, final boolean value)
	{
		setEmailEnabled( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.emailEnabled</code> attribute. 
	 * @param value the emailEnabled
	 */
	public void setEmailEnabled(final boolean value)
	{
		setEmailEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.expiryDate</code> attribute.
	 * @return the expiryDate - Deprecated since 1905, will be replaced by creation time plus expiryDay.
	 */
	public Date getExpiryDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, EXPIRYDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.expiryDate</code> attribute.
	 * @return the expiryDate - Deprecated since 1905, will be replaced by creation time plus expiryDay.
	 */
	public Date getExpiryDate()
	{
		return getExpiryDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.expiryDate</code> attribute. 
	 * @param value the expiryDate - Deprecated since 1905, will be replaced by creation time plus expiryDay.
	 */
	public void setExpiryDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, EXPIRYDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.expiryDate</code> attribute. 
	 * @param value the expiryDate - Deprecated since 1905, will be replaced by creation time plus expiryDay.
	 */
	public void setExpiryDate(final Date value)
	{
		setExpiryDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.language</code> attribute.
	 * @return the language
	 */
	public Language getLanguage(final SessionContext ctx)
	{
		return (Language)getProperty( ctx, LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.language</code> attribute.
	 * @return the language
	 */
	public Language getLanguage()
	{
		return getLanguage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.language</code> attribute. 
	 * @param value the language
	 */
	public void setLanguage(final SessionContext ctx, final Language value)
	{
		setProperty(ctx, LANGUAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.language</code> attribute. 
	 * @param value the language
	 */
	public void setLanguage(final Language value)
	{
		setLanguage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.notificationChannels</code> attribute.
	 * @return the notificationChannels
	 */
	public Set<EnumerationValue> getNotificationChannels(final SessionContext ctx)
	{
		Set<EnumerationValue> coll = (Set<EnumerationValue>)getProperty( ctx, NOTIFICATIONCHANNELS);
		return coll != null ? coll : Collections.EMPTY_SET;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.notificationChannels</code> attribute.
	 * @return the notificationChannels
	 */
	public Set<EnumerationValue> getNotificationChannels()
	{
		return getNotificationChannels( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.notificationChannels</code> attribute. 
	 * @param value the notificationChannels
	 */
	public void setNotificationChannels(final SessionContext ctx, final Set<EnumerationValue> value)
	{
		setProperty(ctx, NOTIFICATIONCHANNELS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.notificationChannels</code> attribute. 
	 * @param value the notificationChannels
	 */
	public void setNotificationChannels(final Set<EnumerationValue> value)
	{
		setNotificationChannels( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.notificationType</code> attribute.
	 * @return the notificationType
	 */
	public EnumerationValue getNotificationType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, NOTIFICATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.notificationType</code> attribute.
	 * @return the notificationType
	 */
	public EnumerationValue getNotificationType()
	{
		return getNotificationType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.notificationType</code> attribute. 
	 * @param value the notificationType
	 */
	public void setNotificationType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, NOTIFICATIONTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.notificationType</code> attribute. 
	 * @param value the notificationType
	 */
	public void setNotificationType(final EnumerationValue value)
	{
		setNotificationType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.product</code> attribute.
	 * @return the product
	 */
	public Product getProduct(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.product</code> attribute.
	 * @return the product
	 */
	public Product getProduct()
	{
		return getProduct( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final SessionContext ctx, final Product value)
	{
		PRODUCTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final Product value)
	{
		setProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.smsEnabled</code> attribute.
	 * @return the smsEnabled
	 */
	public Boolean isSmsEnabled(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SMSENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.smsEnabled</code> attribute.
	 * @return the smsEnabled
	 */
	public Boolean isSmsEnabled()
	{
		return isSmsEnabled( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.smsEnabled</code> attribute. 
	 * @return the smsEnabled
	 */
	public boolean isSmsEnabledAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isSmsEnabled( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.smsEnabled</code> attribute. 
	 * @return the smsEnabled
	 */
	public boolean isSmsEnabledAsPrimitive()
	{
		return isSmsEnabledAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.smsEnabled</code> attribute. 
	 * @param value the smsEnabled
	 */
	public void setSmsEnabled(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SMSENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.smsEnabled</code> attribute. 
	 * @param value the smsEnabled
	 */
	public void setSmsEnabled(final Boolean value)
	{
		setSmsEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.smsEnabled</code> attribute. 
	 * @param value the smsEnabled
	 */
	public void setSmsEnabled(final SessionContext ctx, final boolean value)
	{
		setSmsEnabled( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterest.smsEnabled</code> attribute. 
	 * @param value the smsEnabled
	 */
	public void setSmsEnabled(final boolean value)
	{
		setSmsEnabled( getSession().getSessionContext(), value );
	}
	
}
