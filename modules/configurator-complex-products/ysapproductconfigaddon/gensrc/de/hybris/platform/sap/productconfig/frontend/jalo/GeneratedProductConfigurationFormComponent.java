/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.frontend.jalo;

import de.hybris.platform.cms2.jalo.contents.components.CMSParagraphComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigaddonConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cms2.jalo.contents.components.CMSParagraphComponent ProductConfigurationFormComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedProductConfigurationFormComponent extends CMSParagraphComponent
{
	/** Qualifier of the <code>ProductConfigurationFormComponent.groupStartLevel</code> attribute **/
	public static final String GROUPSTARTLEVEL = "groupStartLevel";
	/** Qualifier of the <code>ProductConfigurationFormComponent.showSummary</code> attribute **/
	public static final String SHOWSUMMARY = "showSummary";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CMSParagraphComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(GROUPSTARTLEVEL, AttributeMode.INITIAL);
		tmp.put(SHOWSUMMARY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationFormComponent.groupStartLevel</code> attribute.
	 * @return the groupStartLevel
	 */
	public Integer getGroupStartLevel(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, GROUPSTARTLEVEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationFormComponent.groupStartLevel</code> attribute.
	 * @return the groupStartLevel
	 */
	public Integer getGroupStartLevel()
	{
		return getGroupStartLevel( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationFormComponent.groupStartLevel</code> attribute. 
	 * @return the groupStartLevel
	 */
	public int getGroupStartLevelAsPrimitive(final SessionContext ctx)
	{
		Integer value = getGroupStartLevel( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationFormComponent.groupStartLevel</code> attribute. 
	 * @return the groupStartLevel
	 */
	public int getGroupStartLevelAsPrimitive()
	{
		return getGroupStartLevelAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationFormComponent.groupStartLevel</code> attribute. 
	 * @param value the groupStartLevel
	 */
	public void setGroupStartLevel(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, GROUPSTARTLEVEL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationFormComponent.groupStartLevel</code> attribute. 
	 * @param value the groupStartLevel
	 */
	public void setGroupStartLevel(final Integer value)
	{
		setGroupStartLevel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationFormComponent.groupStartLevel</code> attribute. 
	 * @param value the groupStartLevel
	 */
	public void setGroupStartLevel(final SessionContext ctx, final int value)
	{
		setGroupStartLevel( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationFormComponent.groupStartLevel</code> attribute. 
	 * @param value the groupStartLevel
	 */
	public void setGroupStartLevel(final int value)
	{
		setGroupStartLevel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationFormComponent.showSummary</code> attribute.
	 * @return the showSummary
	 */
	public Boolean isShowSummary(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SHOWSUMMARY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationFormComponent.showSummary</code> attribute.
	 * @return the showSummary
	 */
	public Boolean isShowSummary()
	{
		return isShowSummary( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationFormComponent.showSummary</code> attribute. 
	 * @return the showSummary
	 */
	public boolean isShowSummaryAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isShowSummary( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationFormComponent.showSummary</code> attribute. 
	 * @return the showSummary
	 */
	public boolean isShowSummaryAsPrimitive()
	{
		return isShowSummaryAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationFormComponent.showSummary</code> attribute. 
	 * @param value the showSummary
	 */
	public void setShowSummary(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SHOWSUMMARY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationFormComponent.showSummary</code> attribute. 
	 * @param value the showSummary
	 */
	public void setShowSummary(final Boolean value)
	{
		setShowSummary( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationFormComponent.showSummary</code> attribute. 
	 * @param value the showSummary
	 */
	public void setShowSummary(final SessionContext ctx, final boolean value)
	{
		setShowSummary( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationFormComponent.showSummary</code> attribute. 
	 * @param value the showSummary
	 */
	public void setShowSummary(final boolean value)
	{
		setShowSummary( getSession().getSessionContext(), value );
	}
	
}
