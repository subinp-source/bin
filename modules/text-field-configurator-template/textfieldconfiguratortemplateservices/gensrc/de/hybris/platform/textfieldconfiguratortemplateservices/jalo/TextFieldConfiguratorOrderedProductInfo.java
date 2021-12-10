/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.textfieldconfiguratortemplateservices.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.order.jalo.AbstractOrderEntryProductInfo;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type TextFieldConfiguredProductInfo.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class TextFieldConfiguratorOrderedProductInfo extends AbstractOrderEntryProductInfo
{
	/** Qualifier of the <code>TextFieldConfiguredProductInfo.configurationLabel</code> attribute **/
	public static final String CONFIGURATIONLABEL = "configurationLabel";
	/** Qualifier of the <code>TextFieldConfiguredProductInfo.configurationValue</code> attribute **/
	public static final String CONFIGURATIONVALUE = "configurationValue";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractOrderEntryProductInfo.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CONFIGURATIONLABEL, AttributeMode.INITIAL);
		tmp.put(CONFIGURATIONVALUE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguredProductInfo.configurationLabel</code> attribute.
	 * @return the configurationLabel - Text fiel label
	 */
	public String getConfigurationLabel(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "configurationLabel".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguredProductInfo.configurationLabel</code> attribute.
	 * @return the configurationLabel - Text fiel label
	 */
	public String getConfigurationLabel()
	{
		return getConfigurationLabel( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguredProductInfo.configurationLabel</code> attribute. 
	 * @param value the configurationLabel - Text fiel label
	 */
	public void setConfigurationLabel(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "configurationLabel".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguredProductInfo.configurationLabel</code> attribute. 
	 * @param value the configurationLabel - Text fiel label
	 */
	public void setConfigurationLabel(final String value)
	{
		setConfigurationLabel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguredProductInfo.configurationValue</code> attribute.
	 * @return the configurationValue - Text field value
	 */
	public String getConfigurationValue(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "configurationValue".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguredProductInfo.configurationValue</code> attribute.
	 * @return the configurationValue - Text field value
	 */
	public String getConfigurationValue()
	{
		return getConfigurationValue( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguredProductInfo.configurationValue</code> attribute. 
	 * @param value the configurationValue - Text field value
	 */
	public void setConfigurationValue(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "configurationValue".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguredProductInfo.configurationValue</code> attribute. 
	 * @param value the configurationValue - Text field value
	 */
	public void setConfigurationValue(final String value)
	{
		setConfigurationValue( getSession().getSessionContext(), value );
	}
	
}
