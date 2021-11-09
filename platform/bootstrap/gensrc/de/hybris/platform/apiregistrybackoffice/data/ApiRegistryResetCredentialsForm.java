/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistrybackoffice.data;

import java.io.Serializable;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import java.util.List;


import java.util.Objects;
public  class ApiRegistryResetCredentialsForm  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ApiRegistryResetCredentialsForm.credential</code> property defined at extension <code>apiregistrybackoffice</code>. */
		
	private ExposedOAuthCredentialModel credential;

	/** <i>Generated property</i> for <code>ApiRegistryResetCredentialsForm.impactedDestinations</code> property defined at extension <code>apiregistrybackoffice</code>. */
		
	private List<ExposedDestinationModel> impactedDestinations;

	/** <i>Generated property</i> for <code>ApiRegistryResetCredentialsForm.impactedCredentials</code> property defined at extension <code>apiregistrybackoffice</code>. */
		
	private List<ExposedOAuthCredentialModel> impactedCredentials;

	/** <i>Generated property</i> for <code>ApiRegistryResetCredentialsForm.clientId</code> property defined at extension <code>apiregistrybackoffice</code>. */
		
	private String clientId;

	/** <i>Generated property</i> for <code>ApiRegistryResetCredentialsForm.clientSecret</code> property defined at extension <code>apiregistrybackoffice</code>. */
		
	private String clientSecret;

	/** <i>Generated property</i> for <code>ApiRegistryResetCredentialsForm.gracePeriod</code> property defined at extension <code>apiregistrybackoffice</code>. */
		
	private Integer gracePeriod;
	
	public ApiRegistryResetCredentialsForm()
	{
		// default constructor
	}
	
	public void setCredential(final ExposedOAuthCredentialModel credential)
	{
		this.credential = credential;
	}

	public ExposedOAuthCredentialModel getCredential() 
	{
		return credential;
	}
	
	public void setImpactedDestinations(final List<ExposedDestinationModel> impactedDestinations)
	{
		this.impactedDestinations = impactedDestinations;
	}

	public List<ExposedDestinationModel> getImpactedDestinations() 
	{
		return impactedDestinations;
	}
	
	public void setImpactedCredentials(final List<ExposedOAuthCredentialModel> impactedCredentials)
	{
		this.impactedCredentials = impactedCredentials;
	}

	public List<ExposedOAuthCredentialModel> getImpactedCredentials() 
	{
		return impactedCredentials;
	}
	
	public void setClientId(final String clientId)
	{
		this.clientId = clientId;
	}

	public String getClientId() 
	{
		return clientId;
	}
	
	public void setClientSecret(final String clientSecret)
	{
		this.clientSecret = clientSecret;
	}

	public String getClientSecret() 
	{
		return clientSecret;
	}
	
	public void setGracePeriod(final Integer gracePeriod)
	{
		this.gracePeriod = gracePeriod;
	}

	public Integer getGracePeriod() 
	{
		return gracePeriod;
	}
	

}