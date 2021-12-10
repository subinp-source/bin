/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.notificationservices.constants.NotificationservicesConstants;
import de.hybris.platform.notificationservices.jalo.SiteMessage;
import de.hybris.platform.notificationservices.jalo.SiteMessageForCustomer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type <code>NotificationservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedNotificationservicesManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("emailPreference", AttributeMode.INITIAL);
		tmp.put("smsPreference", AttributeMode.INITIAL);
		tmp.put("notificationChannels", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.user.Customer", Collections.unmodifiableMap(tmp));
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
	
	public SiteMessage createSiteMessage(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( NotificationservicesConstants.TC.SITEMESSAGE );
			return (SiteMessage)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating SiteMessage : "+e.getMessage(), 0 );
		}
	}
	
	public SiteMessage createSiteMessage(final Map attributeValues)
	{
		return createSiteMessage( getSession().getSessionContext(), attributeValues );
	}
	
	public SiteMessageForCustomer createSiteMessageForCustomer(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( NotificationservicesConstants.TC.SITEMESSAGEFORCUSTOMER );
			return (SiteMessageForCustomer)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating SiteMessageForCustomer : "+e.getMessage(), 0 );
		}
	}
	
	public SiteMessageForCustomer createSiteMessageForCustomer(final Map attributeValues)
	{
		return createSiteMessageForCustomer( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.emailPreference</code> attribute.
	 * @return the emailPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public Boolean isEmailPreference(final SessionContext ctx, final Customer item)
	{
		return (Boolean)item.getProperty( ctx, NotificationservicesConstants.Attributes.Customer.EMAILPREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.emailPreference</code> attribute.
	 * @return the emailPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public Boolean isEmailPreference(final Customer item)
	{
		return isEmailPreference( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.emailPreference</code> attribute. 
	 * @return the emailPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public boolean isEmailPreferenceAsPrimitive(final SessionContext ctx, final Customer item)
	{
		Boolean value = isEmailPreference( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.emailPreference</code> attribute. 
	 * @return the emailPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public boolean isEmailPreferenceAsPrimitive(final Customer item)
	{
		return isEmailPreferenceAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.emailPreference</code> attribute. 
	 * @param value the emailPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public void setEmailPreference(final SessionContext ctx, final Customer item, final Boolean value)
	{
		item.setProperty(ctx, NotificationservicesConstants.Attributes.Customer.EMAILPREFERENCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.emailPreference</code> attribute. 
	 * @param value the emailPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public void setEmailPreference(final Customer item, final Boolean value)
	{
		setEmailPreference( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.emailPreference</code> attribute. 
	 * @param value the emailPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public void setEmailPreference(final SessionContext ctx, final Customer item, final boolean value)
	{
		setEmailPreference( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.emailPreference</code> attribute. 
	 * @param value the emailPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public void setEmailPreference(final Customer item, final boolean value)
	{
		setEmailPreference( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return NotificationservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.notificationChannels</code> attribute.
	 * @return the notificationChannels
	 */
	public Set<EnumerationValue> getNotificationChannels(final SessionContext ctx, final Customer item)
	{
		Set<EnumerationValue> coll = (Set<EnumerationValue>)item.getProperty( ctx, NotificationservicesConstants.Attributes.Customer.NOTIFICATIONCHANNELS);
		return coll != null ? coll : Collections.EMPTY_SET;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.notificationChannels</code> attribute.
	 * @return the notificationChannels
	 */
	public Set<EnumerationValue> getNotificationChannels(final Customer item)
	{
		return getNotificationChannels( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.notificationChannels</code> attribute. 
	 * @param value the notificationChannels
	 */
	public void setNotificationChannels(final SessionContext ctx, final Customer item, final Set<EnumerationValue> value)
	{
		item.setProperty(ctx, NotificationservicesConstants.Attributes.Customer.NOTIFICATIONCHANNELS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.notificationChannels</code> attribute. 
	 * @param value the notificationChannels
	 */
	public void setNotificationChannels(final Customer item, final Set<EnumerationValue> value)
	{
		setNotificationChannels( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.smsPreference</code> attribute.
	 * @return the smsPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public Boolean isSmsPreference(final SessionContext ctx, final Customer item)
	{
		return (Boolean)item.getProperty( ctx, NotificationservicesConstants.Attributes.Customer.SMSPREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.smsPreference</code> attribute.
	 * @return the smsPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public Boolean isSmsPreference(final Customer item)
	{
		return isSmsPreference( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.smsPreference</code> attribute. 
	 * @return the smsPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public boolean isSmsPreferenceAsPrimitive(final SessionContext ctx, final Customer item)
	{
		Boolean value = isSmsPreference( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.smsPreference</code> attribute. 
	 * @return the smsPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public boolean isSmsPreferenceAsPrimitive(final Customer item)
	{
		return isSmsPreferenceAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.smsPreference</code> attribute. 
	 * @param value the smsPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public void setSmsPreference(final SessionContext ctx, final Customer item, final Boolean value)
	{
		item.setProperty(ctx, NotificationservicesConstants.Attributes.Customer.SMSPREFERENCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.smsPreference</code> attribute. 
	 * @param value the smsPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public void setSmsPreference(final Customer item, final Boolean value)
	{
		setSmsPreference( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.smsPreference</code> attribute. 
	 * @param value the smsPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public void setSmsPreference(final SessionContext ctx, final Customer item, final boolean value)
	{
		setSmsPreference( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.smsPreference</code> attribute. 
	 * @param value the smsPreference - Deprecated since 6.7, will be removed in the future.
	 */
	public void setSmsPreference(final Customer item, final boolean value)
	{
		setSmsPreference( getSession().getSessionContext(), item, value );
	}
	
}
