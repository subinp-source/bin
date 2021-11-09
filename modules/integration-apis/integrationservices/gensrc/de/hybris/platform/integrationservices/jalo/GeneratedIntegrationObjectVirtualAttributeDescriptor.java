/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.jalo;

import de.hybris.platform.integrationservices.constants.IntegrationservicesConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem IntegrationObjectVirtualAttributeDescriptor}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedIntegrationObjectVirtualAttributeDescriptor extends GenericItem
{
	/** Qualifier of the <code>IntegrationObjectVirtualAttributeDescriptor.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>IntegrationObjectVirtualAttributeDescriptor.logicLocation</code> attribute **/
	public static final String LOGICLOCATION = "logicLocation";
	/** Qualifier of the <code>IntegrationObjectVirtualAttributeDescriptor.type</code> attribute **/
	public static final String TYPE = "type";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(LOGICLOCATION, AttributeMode.INITIAL);
		tmp.put(TYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObjectVirtualAttributeDescriptor.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObjectVirtualAttributeDescriptor.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObjectVirtualAttributeDescriptor.code</code> attribute. 
	 * @param value the code
	 */
	protected void setCode(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+CODE+"' is not changeable", 0 );
		}
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObjectVirtualAttributeDescriptor.code</code> attribute. 
	 * @param value the code
	 */
	protected void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObjectVirtualAttributeDescriptor.logicLocation</code> attribute.
	 * @return the logicLocation - Specifies where the logic is located. Acceptable values are of this format:
	 *                         model://scriptModelCode
	 */
	public String getLogicLocation(final SessionContext ctx)
	{
		return (String)getProperty( ctx, LOGICLOCATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObjectVirtualAttributeDescriptor.logicLocation</code> attribute.
	 * @return the logicLocation - Specifies where the logic is located. Acceptable values are of this format:
	 *                         model://scriptModelCode
	 */
	public String getLogicLocation()
	{
		return getLogicLocation( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObjectVirtualAttributeDescriptor.logicLocation</code> attribute. 
	 * @param value the logicLocation - Specifies where the logic is located. Acceptable values are of this format:
	 *                         model://scriptModelCode
	 */
	public void setLogicLocation(final SessionContext ctx, final String value)
	{
		setProperty(ctx, LOGICLOCATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObjectVirtualAttributeDescriptor.logicLocation</code> attribute. 
	 * @param value the logicLocation - Specifies where the logic is located. Acceptable values are of this format:
	 *                         model://scriptModelCode
	 */
	public void setLogicLocation(final String value)
	{
		setLogicLocation( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObjectVirtualAttributeDescriptor.type</code> attribute.
	 * @return the type - The logic's return or input type. The return type will be used as the type in the schema for the attribute.
	 */
	public Type getType(final SessionContext ctx)
	{
		return (Type)getProperty( ctx, TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObjectVirtualAttributeDescriptor.type</code> attribute.
	 * @return the type - The logic's return or input type. The return type will be used as the type in the schema for the attribute.
	 */
	public Type getType()
	{
		return getType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObjectVirtualAttributeDescriptor.type</code> attribute. 
	 * @param value the type - The logic's return or input type. The return type will be used as the type in the schema for the attribute.
	 */
	public void setType(final SessionContext ctx, final Type value)
	{
		setProperty(ctx, TYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObjectVirtualAttributeDescriptor.type</code> attribute. 
	 * @param value the type - The logic's return or input type. The return type will be used as the type in the schema for the attribute.
	 */
	public void setType(final Type value)
	{
		setType( getSession().getSessionContext(), value );
	}
	
}
