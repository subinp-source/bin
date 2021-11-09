/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.messagecentercsservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.messagecentercsservices.constants.MessagecentercsservicesConstants;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem Conversation}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedConversation extends GenericItem
{
	/** Qualifier of the <code>Conversation.uid</code> attribute **/
	public static final String UID = "uid";
	/** Qualifier of the <code>Conversation.status</code> attribute **/
	public static final String STATUS = "status";
	/** Qualifier of the <code>Conversation.agent</code> attribute **/
	public static final String AGENT = "agent";
	/** Qualifier of the <code>Conversation.customer</code> attribute **/
	public static final String CUSTOMER = "customer";
	/** Qualifier of the <code>Conversation.closeTime</code> attribute **/
	public static final String CLOSETIME = "closeTime";
	/** Qualifier of the <code>Conversation.messages</code> attribute **/
	public static final String MESSAGES = "messages";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(UID, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		tmp.put(AGENT, AttributeMode.INITIAL);
		tmp.put(CUSTOMER, AttributeMode.INITIAL);
		tmp.put(CLOSETIME, AttributeMode.INITIAL);
		tmp.put(MESSAGES, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.agent</code> attribute.
	 * @return the agent
	 */
	public Employee getAgent(final SessionContext ctx)
	{
		return (Employee)getProperty( ctx, AGENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.agent</code> attribute.
	 * @return the agent
	 */
	public Employee getAgent()
	{
		return getAgent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.agent</code> attribute. 
	 * @param value the agent
	 */
	public void setAgent(final SessionContext ctx, final Employee value)
	{
		setProperty(ctx, AGENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.agent</code> attribute. 
	 * @param value the agent
	 */
	public void setAgent(final Employee value)
	{
		setAgent( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.closeTime</code> attribute.
	 * @return the closeTime
	 */
	public Date getCloseTime(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, CLOSETIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.closeTime</code> attribute.
	 * @return the closeTime
	 */
	public Date getCloseTime()
	{
		return getCloseTime( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.closeTime</code> attribute. 
	 * @param value the closeTime
	 */
	public void setCloseTime(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, CLOSETIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.closeTime</code> attribute. 
	 * @param value the closeTime
	 */
	public void setCloseTime(final Date value)
	{
		setCloseTime( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.customer</code> attribute.
	 * @return the customer
	 */
	public Customer getCustomer(final SessionContext ctx)
	{
		return (Customer)getProperty( ctx, CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.customer</code> attribute.
	 * @return the customer
	 */
	public Customer getCustomer()
	{
		return getCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final SessionContext ctx, final Customer value)
	{
		setProperty(ctx, CUSTOMER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final Customer value)
	{
		setCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.messages</code> attribute.
	 * @return the messages
	 */
	public String getMessages(final SessionContext ctx)
	{
		return (String)getProperty( ctx, MESSAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.messages</code> attribute.
	 * @return the messages
	 */
	public String getMessages()
	{
		return getMessages( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.messages</code> attribute. 
	 * @param value the messages
	 */
	public void setMessages(final SessionContext ctx, final String value)
	{
		setProperty(ctx, MESSAGES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.messages</code> attribute. 
	 * @param value the messages
	 */
	public void setMessages(final String value)
	{
		setMessages( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final EnumerationValue value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.uid</code> attribute.
	 * @return the uid
	 */
	public String getUid(final SessionContext ctx)
	{
		return (String)getProperty( ctx, UID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Conversation.uid</code> attribute.
	 * @return the uid
	 */
	public String getUid()
	{
		return getUid( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.uid</code> attribute. 
	 * @param value the uid
	 */
	public void setUid(final SessionContext ctx, final String value)
	{
		setProperty(ctx, UID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Conversation.uid</code> attribute. 
	 * @param value the uid
	 */
	public void setUid(final String value)
	{
		setUid( getSession().getSessionContext(), value );
	}
	
}
