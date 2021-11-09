/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum DestinationChannel declared at extension apiregistryservices.
 * <p/>
 * Optional enum for DestinationTarget.
 */
public enum DestinationChannel implements HybrisEnumValue
{
	/**
	 * Generated enum value for DestinationChannel.ASM declared at extension yaasyprofileconnect.
	 */
	ASM("ASM"),
	/**
	 * Generated enum value for DestinationChannel.CDS declared at extension profileservices.
	 */
	CDS("CDS"),
	/**
	 * Generated enum value for DestinationChannel.DEFAULT declared at extension apiregistryservices.
	 */
	DEFAULT("DEFAULT"),
	/**
	 * Generated enum value for DestinationChannel.MERCH declared at extension merchandisingservices.
	 */
	MERCH("MERCH");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DestinationChannel";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "DestinationChannel";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private DestinationChannel(final String code)
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
