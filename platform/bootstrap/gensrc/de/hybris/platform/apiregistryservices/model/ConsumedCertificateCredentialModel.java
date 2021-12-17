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
 * Generated model class for type ConsumedCertificateCredential first defined at extension apiregistryservices.
 * <p>
 * Certificate-based credentials for external webservice.
 */
@SuppressWarnings("all")
public class ConsumedCertificateCredentialModel extends AbstractCredentialModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ConsumedCertificateCredential";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsumedCertificateCredential.certificateData</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String CERTIFICATEDATA = "certificateData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsumedCertificateCredential.privateKey</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String PRIVATEKEY = "privateKey";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConsumedCertificateCredentialModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConsumedCertificateCredentialModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractCredential</code> at extension <code>apiregistryservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ConsumedCertificateCredentialModel(final String _id)
	{
		super();
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractCredential</code> at extension <code>apiregistryservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ConsumedCertificateCredentialModel(final String _id, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsumedCertificateCredential.certificateData</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the certificateData - Serialized Authentication Object
	 */
	@Accessor(qualifier = "certificateData", type = Accessor.Type.GETTER)
	public String getCertificateData()
	{
		return getPersistenceContext().getPropertyValue(CERTIFICATEDATA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsumedCertificateCredential.privateKey</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the privateKey - Serialized Authentication Object
	 */
	@Accessor(qualifier = "privateKey", type = Accessor.Type.GETTER)
	public String getPrivateKey()
	{
		return getPersistenceContext().getPropertyValue(PRIVATEKEY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsumedCertificateCredential.certificateData</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the certificateData - Serialized Authentication Object
	 */
	@Accessor(qualifier = "certificateData", type = Accessor.Type.SETTER)
	public void setCertificateData(final String value)
	{
		getPersistenceContext().setPropertyValue(CERTIFICATEDATA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsumedCertificateCredential.privateKey</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the privateKey - Serialized Authentication Object
	 */
	@Accessor(qualifier = "privateKey", type = Accessor.Type.SETTER)
	public void setPrivateKey(final String value)
	{
		getPersistenceContext().setPropertyValue(PRIVATEKEY, value);
	}
	
}
