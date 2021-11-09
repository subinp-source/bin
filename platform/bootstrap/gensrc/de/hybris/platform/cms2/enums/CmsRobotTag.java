/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CmsRobotTag declared at extension cms2.
 */
public enum CmsRobotTag implements HybrisEnumValue
{
	/**
	 * Generated enum value for CmsRobotTag.INDEX_FOLLOW declared at extension cms2.
	 */
	INDEX_FOLLOW("INDEX_FOLLOW"),
	/**
	 * Generated enum value for CmsRobotTag.INDEX_NOFOLLOW declared at extension cms2.
	 */
	INDEX_NOFOLLOW("INDEX_NOFOLLOW"),
	/**
	 * Generated enum value for CmsRobotTag.NOINDEX_FOLLOW declared at extension cms2.
	 */
	NOINDEX_FOLLOW("NOINDEX_FOLLOW"),
	/**
	 * Generated enum value for CmsRobotTag.NOINDEX_NOFOLLOW declared at extension cms2.
	 */
	NOINDEX_NOFOLLOW("NOINDEX_NOFOLLOW");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CmsRobotTag";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CmsRobotTag";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CmsRobotTag(final String code)
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
