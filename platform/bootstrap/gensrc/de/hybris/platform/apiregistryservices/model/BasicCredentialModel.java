/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type BasicCredential first defined at extension apiregistryservices.
 * <p>
 * Basic credentials for webservice.
 */
@SuppressWarnings("all")
public class BasicCredentialModel extends AbstractCredentialModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BasicCredential";
	
	/** <i>Generated constant</i> - Attribute key of <code>BasicCredential.username</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String USERNAME = "username";
	
	/** <i>Generated constant</i> - Attribute key of <code>BasicCredential.password</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String PASSWORD = "password";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BasicCredentialModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BasicCredentialModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractCredential</code> at extension <code>apiregistryservices</code>
	 * @param _password initial attribute declared by type <code>BasicCredential</code> at extension <code>apiregistryservices</code>
	 * @param _username initial attribute declared by type <code>BasicCredential</code> at extension <code>apiregistryservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public BasicCredentialModel(final String _id, final String _password, final String _username)
	{
		super();
		setId(_id);
		setPassword(_password);
		setUsername(_username);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractCredential</code> at extension <code>apiregistryservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _password initial attribute declared by type <code>BasicCredential</code> at extension <code>apiregistryservices</code>
	 * @param _username initial attribute declared by type <code>BasicCredential</code> at extension <code>apiregistryservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public BasicCredentialModel(final String _id, final ItemModel _owner, final String _password, final String _username)
	{
		super();
		setId(_id);
		setOwner(_owner);
		setPassword(_password);
		setUsername(_username);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BasicCredential.password</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the password - Encrypted Password
	 */
	@Accessor(qualifier = "password", type = Accessor.Type.GETTER)
	public String getPassword()
	{
		return getPersistenceContext().getPropertyValue(PASSWORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BasicCredential.username</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the username - Username
	 */
	@Accessor(qualifier = "username", type = Accessor.Type.GETTER)
	public String getUsername()
	{
		return getPersistenceContext().getPropertyValue(USERNAME);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BasicCredential.password</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the password - Encrypted Password
	 */
	@Accessor(qualifier = "password", type = Accessor.Type.SETTER)
	public void setPassword(final String value)
	{
		getPersistenceContext().setPropertyValue(PASSWORD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BasicCredential.username</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the username - Username
	 */
	@Accessor(qualifier = "username", type = Accessor.Type.SETTER)
	public void setUsername(final String value)
	{
		getPersistenceContext().setPropertyValue(USERNAME, value);
	}
	
}
