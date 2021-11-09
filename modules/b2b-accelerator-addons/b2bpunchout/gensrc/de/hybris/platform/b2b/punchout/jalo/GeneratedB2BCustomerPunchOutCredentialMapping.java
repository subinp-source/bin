/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.jalo;

import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.b2b.punchout.constants.B2bpunchoutConstants;
import de.hybris.platform.b2b.punchout.jalo.PunchOutCredential;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem B2BCustomerPunchOutCredentialMapping}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BCustomerPunchOutCredentialMapping extends GenericItem
{
	/** Qualifier of the <code>B2BCustomerPunchOutCredentialMapping.b2bCustomer</code> attribute **/
	public static final String B2BCUSTOMER = "b2bCustomer";
	/** Qualifier of the <code>B2BCustomerPunchOutCredentialMapping.credentials</code> attribute **/
	public static final String CREDENTIALS = "credentials";
	/**
	* {@link OneToManyHandler} for handling 1:n CREDENTIALS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<PunchOutCredential> CREDENTIALSHANDLER = new OneToManyHandler<PunchOutCredential>(
	B2bpunchoutConstants.TC.PUNCHOUTCREDENTIAL,
	false,
	"B2BCustomerPunchOutCredentialMapping",
	null,
	false,
	true,
	CollectionType.SET
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(B2BCUSTOMER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomerPunchOutCredentialMapping.b2bCustomer</code> attribute.
	 * @return the b2bCustomer
	 */
	public B2BCustomer getB2bCustomer(final SessionContext ctx)
	{
		return (B2BCustomer)getProperty( ctx, B2BCUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomerPunchOutCredentialMapping.b2bCustomer</code> attribute.
	 * @return the b2bCustomer
	 */
	public B2BCustomer getB2bCustomer()
	{
		return getB2bCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomerPunchOutCredentialMapping.b2bCustomer</code> attribute. 
	 * @param value the b2bCustomer
	 */
	public void setB2bCustomer(final SessionContext ctx, final B2BCustomer value)
	{
		setProperty(ctx, B2BCUSTOMER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomerPunchOutCredentialMapping.b2bCustomer</code> attribute. 
	 * @param value the b2bCustomer
	 */
	public void setB2bCustomer(final B2BCustomer value)
	{
		setB2bCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomerPunchOutCredentialMapping.credentials</code> attribute.
	 * @return the credentials
	 */
	public Set<PunchOutCredential> getCredentials(final SessionContext ctx)
	{
		return (Set<PunchOutCredential>)CREDENTIALSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomerPunchOutCredentialMapping.credentials</code> attribute.
	 * @return the credentials
	 */
	public Set<PunchOutCredential> getCredentials()
	{
		return getCredentials( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomerPunchOutCredentialMapping.credentials</code> attribute. 
	 * @param value the credentials
	 */
	public void setCredentials(final SessionContext ctx, final Set<PunchOutCredential> value)
	{
		CREDENTIALSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomerPunchOutCredentialMapping.credentials</code> attribute. 
	 * @param value the credentials
	 */
	public void setCredentials(final Set<PunchOutCredential> value)
	{
		setCredentials( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to credentials. 
	 * @param value the item to add to credentials
	 */
	public void addToCredentials(final SessionContext ctx, final PunchOutCredential value)
	{
		CREDENTIALSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to credentials. 
	 * @param value the item to add to credentials
	 */
	public void addToCredentials(final PunchOutCredential value)
	{
		addToCredentials( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from credentials. 
	 * @param value the item to remove from credentials
	 */
	public void removeFromCredentials(final SessionContext ctx, final PunchOutCredential value)
	{
		CREDENTIALSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from credentials. 
	 * @param value the item to remove from credentials
	 */
	public void removeFromCredentials(final PunchOutCredential value)
	{
		removeFromCredentials( getSession().getSessionContext(), value );
	}
	
}
