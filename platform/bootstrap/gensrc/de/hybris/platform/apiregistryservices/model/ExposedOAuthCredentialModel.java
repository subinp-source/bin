/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

/**
 * Generated model class for type ExposedOAuthCredential first defined at extension apiregistryservices.
 * <p>
 * OAuth credentials for CS webservice.
 */
@SuppressWarnings("all")
public class ExposedOAuthCredentialModel extends AbstractCredentialModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ExposedOAuthCredential";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExposedOAuthCredential.oAuthClientDetails</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String OAUTHCLIENTDETAILS = "oAuthClientDetails";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExposedOAuthCredential.password</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String PASSWORD = "password";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ExposedOAuthCredentialModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ExposedOAuthCredentialModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractCredential</code> at extension <code>apiregistryservices</code>
	 * @param _oAuthClientDetails initial attribute declared by type <code>ExposedOAuthCredential</code> at extension <code>apiregistryservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ExposedOAuthCredentialModel(final String _id, final OAuthClientDetailsModel _oAuthClientDetails)
	{
		super();
		setId(_id);
		setOAuthClientDetails(_oAuthClientDetails);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractCredential</code> at extension <code>apiregistryservices</code>
	 * @param _oAuthClientDetails initial attribute declared by type <code>ExposedOAuthCredential</code> at extension <code>apiregistryservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ExposedOAuthCredentialModel(final String _id, final OAuthClientDetailsModel _oAuthClientDetails, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOAuthClientDetails(_oAuthClientDetails);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExposedOAuthCredential.oAuthClientDetails</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the oAuthClientDetails - OAuth Details
	 */
	@Accessor(qualifier = "oAuthClientDetails", type = Accessor.Type.GETTER)
	public OAuthClientDetailsModel getOAuthClientDetails()
	{
		return getPersistenceContext().getPropertyValue(OAUTHCLIENTDETAILS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExposedOAuthCredential.password</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the password - OAuth Password
	 */
	@Accessor(qualifier = "password", type = Accessor.Type.GETTER)
	public String getPassword()
	{
		return getPersistenceContext().getPropertyValue(PASSWORD);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExposedOAuthCredential.oAuthClientDetails</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the oAuthClientDetails - OAuth Details
	 */
	@Accessor(qualifier = "oAuthClientDetails", type = Accessor.Type.SETTER)
	public void setOAuthClientDetails(final OAuthClientDetailsModel value)
	{
		getPersistenceContext().setPropertyValue(OAUTHCLIENTDETAILS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExposedOAuthCredential.password</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the password - OAuth Password
	 */
	@Accessor(qualifier = "password", type = Accessor.Type.SETTER)
	public void setPassword(final String value)
	{
		getPersistenceContext().setPropertyValue(PASSWORD, value);
	}
	
}
