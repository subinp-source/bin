package com.hybris.merchandising.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum ScrollType declared at extension merchandisingaddon.
 */
public enum ScrollType implements HybrisEnumValue
{
	/**
	 * Generated enum value for ScrollType.one declared at extension merchandisingaddon.
	 */
	ONE("one"),
	/**
	 * Generated enum value for ScrollType.allVisible declared at extension merchandisingaddon.
	 */
	ALLVISIBLE("allVisible");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ScrollType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ScrollType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ScrollType(final String code)
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
