/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.notificationservices.constants.NotificationservicesConstants;
import de.hybris.platform.notificationservices.jalo.SiteMessage;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.notificationservices.jalo.SiteMessageForCustomer SiteMessageForCustomer}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSiteMessageForCustomer extends GenericItem
{
	/** Qualifier of the <code>SiteMessageForCustomer.message</code> attribute **/
	public static final String MESSAGE = "message";
	/** Qualifier of the <code>SiteMessageForCustomer.customer</code> attribute **/
	public static final String CUSTOMER = "customer";
	/** Qualifier of the <code>SiteMessageForCustomer.sentDate</code> attribute **/
	public static final String SENTDATE = "sentDate";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(MESSAGE, AttributeMode.INITIAL);
		tmp.put(CUSTOMER, AttributeMode.INITIAL);
		tmp.put(SENTDATE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessageForCustomer.customer</code> attribute.
	 * @return the customer
	 */
	public Customer getCustomer(final SessionContext ctx)
	{
		return (Customer)getProperty( ctx, CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessageForCustomer.customer</code> attribute.
	 * @return the customer
	 */
	public Customer getCustomer()
	{
		return getCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessageForCustomer.customer</code> attribute. 
	 * @param value the customer
	 */
	protected void setCustomer(final SessionContext ctx, final Customer value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+CUSTOMER+"' is not changeable", 0 );
		}
		setProperty(ctx, CUSTOMER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessageForCustomer.customer</code> attribute. 
	 * @param value the customer
	 */
	protected void setCustomer(final Customer value)
	{
		setCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessageForCustomer.message</code> attribute.
	 * @return the message
	 */
	public SiteMessage getMessage(final SessionContext ctx)
	{
		return (SiteMessage)getProperty( ctx, MESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessageForCustomer.message</code> attribute.
	 * @return the message
	 */
	public SiteMessage getMessage()
	{
		return getMessage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessageForCustomer.message</code> attribute. 
	 * @param value the message
	 */
	protected void setMessage(final SessionContext ctx, final SiteMessage value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+MESSAGE+"' is not changeable", 0 );
		}
		setProperty(ctx, MESSAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessageForCustomer.message</code> attribute. 
	 * @param value the message
	 */
	protected void setMessage(final SiteMessage value)
	{
		setMessage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessageForCustomer.sentDate</code> attribute.
	 * @return the sentDate
	 */
	public Date getSentDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, SENTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessageForCustomer.sentDate</code> attribute.
	 * @return the sentDate
	 */
	public Date getSentDate()
	{
		return getSentDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessageForCustomer.sentDate</code> attribute. 
	 * @param value the sentDate
	 */
	protected void setSentDate(final SessionContext ctx, final Date value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+SENTDATE+"' is not changeable", 0 );
		}
		setProperty(ctx, SENTDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessageForCustomer.sentDate</code> attribute. 
	 * @param value the sentDate
	 */
	protected void setSentDate(final Date value)
	{
		setSentDate( getSession().getSessionContext(), value );
	}
	
}
