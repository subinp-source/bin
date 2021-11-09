/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum YFormDefinitionStatusEnum declared at extension xyformsservices.
 */
public enum YFormDefinitionStatusEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for YFormDefinitionStatusEnum.ENABLED declared at extension xyformsservices.
	 */
	ENABLED("ENABLED"),
	/**
	 * Generated enum value for YFormDefinitionStatusEnum.DISABLED declared at extension xyformsservices.
	 */
	DISABLED("DISABLED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "YFormDefinitionStatusEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "YFormDefinitionStatusEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private YFormDefinitionStatusEnum(final String code)
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
