/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.model.SiteMessageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type SiteMessageForCustomer first defined at extension notificationservices.
 */
@SuppressWarnings("all")
public class SiteMessageForCustomerModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SiteMessageForCustomer";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessageForCustomer.message</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String MESSAGE = "message";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessageForCustomer.customer</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String CUSTOMER = "customer";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessageForCustomer.sentDate</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String SENTDATE = "sentDate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SiteMessageForCustomerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SiteMessageForCustomerModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _customer initial attribute declared by type <code>SiteMessageForCustomer</code> at extension <code>notificationservices</code>
	 * @param _message initial attribute declared by type <code>SiteMessageForCustomer</code> at extension <code>notificationservices</code>
	 * @param _sentDate initial attribute declared by type <code>SiteMessageForCustomer</code> at extension <code>notificationservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SiteMessageForCustomerModel(final CustomerModel _customer, final SiteMessageModel _message, final Date _sentDate)
	{
		super();
		setCustomer(_customer);
		setMessage(_message);
		setSentDate(_sentDate);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _customer initial attribute declared by type <code>SiteMessageForCustomer</code> at extension <code>notificationservices</code>
	 * @param _message initial attribute declared by type <code>SiteMessageForCustomer</code> at extension <code>notificationservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sentDate initial attribute declared by type <code>SiteMessageForCustomer</code> at extension <code>notificationservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SiteMessageForCustomerModel(final CustomerModel _customer, final SiteMessageModel _message, final ItemModel _owner, final Date _sentDate)
	{
		super();
		setCustomer(_customer);
		setMessage(_message);
		setOwner(_owner);
		setSentDate(_sentDate);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessageForCustomer.customer</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the customer
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
	public CustomerModel getCustomer()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessageForCustomer.message</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the message
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.GETTER)
	public SiteMessageModel getMessage()
	{
		return getPersistenceContext().getPropertyValue(MESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessageForCustomer.sentDate</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the sentDate
	 */
	@Accessor(qualifier = "sentDate", type = Accessor.Type.GETTER)
	public Date getSentDate()
	{
		return getPersistenceContext().getPropertyValue(SENTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SiteMessageForCustomer.customer</code> attribute defined at extension <code>notificationservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the customer
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.SETTER)
	public void setCustomer(final CustomerModel value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMER, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SiteMessageForCustomer.message</code> attribute defined at extension <code>notificationservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the message
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.SETTER)
	public void setMessage(final SiteMessageModel value)
	{
		getPersistenceContext().setPropertyValue(MESSAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SiteMessageForCustomer.sentDate</code> attribute defined at extension <code>notificationservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sentDate
	 */
	@Accessor(qualifier = "sentDate", type = Accessor.Type.SETTER)
	public void setSentDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(SENTDATE, value);
	}
	
}
