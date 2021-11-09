/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum RegistrationStatus declared at extension apiregistryservices.
 * <p/>
 * Status of registration to the target system.
 */
public enum RegistrationStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for RegistrationStatus.STARTED declared at extension apiregistryservices.
	 */
	STARTED("STARTED"),
	/**
	 * Generated enum value for RegistrationStatus.IN_PROGRESS declared at extension apiregistryservices.
	 */
	IN_PROGRESS("IN_PROGRESS"),
	/**
	 * Generated enum value for RegistrationStatus.REGISTERED declared at extension apiregistryservices.
	 */
	REGISTERED("REGISTERED"),
	/**
	 * Generated enum value for RegistrationStatus.ERROR declared at extension apiregistryservices.
	 */
	ERROR("ERROR");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "RegistrationStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "RegistrationStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private RegistrationStatus(final String code)
	{
		this.code = code.intern();
	}
	
	
	/**
	 * Gets the code of this enum value.
	 *  
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Gets the type this enum value belongs to.
	 *  
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return SIMPLE_CLASSNAME;
	}
	
}
