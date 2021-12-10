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
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.product.jalo.AbstractConfiguratorSetting;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type TextFieldConfiguratorSetting.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class TextFieldConfiguratorSetting extends AbstractConfiguratorSetting
{
	/** Qualifier of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute **/
	public static final String TEXTFIELDLABEL = "textFieldLabel";
	/** Qualifier of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute **/
	public static final String TEXTFIELDDEFAULTVALUE = "textFieldDefaultValue";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractConfiguratorSetting.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(TEXTFIELDLABEL, AttributeMode.INITIAL);
		tmp.put(TEXTFIELDDEFAULTVALUE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute.
	 * @return the textFieldDefaultValue - Default value of the text field
	 */
	public String getTextFieldDefaultValue(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("TextFieldConfiguratorSetting.getTextFieldDefaultValue requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "textFieldDefaultValue".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute.
	 * @return the textFieldDefaultValue - Default value of the text field
	 */
	public String getTextFieldDefaultValue()
	{
		return getTextFieldDefaultValue( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute. 
	 * @return the localized textFieldDefaultValue - Default value of the text field
	 */
	public Map<Language,String> getAllTextFieldDefaultValue(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"textFieldDefaultValue".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute. 
	 * @return the localized textFieldDefaultValue - Default value of the text field
	 */
	public Map<Language,String> getAllTextFieldDefaultValue()
	{
		return getAllTextFieldDefaultValue( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute. 
	 * @param value the textFieldDefaultValue - Default value of the text field
	 */
	public void setTextFieldDefaultValue(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("TextFieldConfiguratorSetting.setTextFieldDefaultValue requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "textFieldDefaultValue".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute. 
	 * @param value the textFieldDefaultValue - Default value of the text field
	 */
	public void setTextFieldDefaultValue(final String value)
	{
		setTextFieldDefaultValue( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute. 
	 * @param value the textFieldDefaultValue - Default value of the text field
	 */
	public void setAllTextFieldDefaultValue(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"textFieldDefaultValue".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute. 
	 * @param value the textFieldDefaultValue - Default value of the text field
	 */
	public void setAllTextFieldDefaultValue(final Map<Language,String> value)
	{
		setAllTextFieldDefaultValue( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute.
	 * @return the textFieldLabel - Label of the text field
	 */
	public String getTextFieldLabel(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("TextFieldConfiguratorSetting.getTextFieldLabel requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "textFieldLabel".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute.
	 * @return the textFieldLabel - Label of the text field
	 */
	public String getTextFieldLabel()
	{
		return getTextFieldLabel( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute. 
	 * @return the localized textFieldLabel - Label of the text field
	 */
	public Map<Language,String> getAllTextFieldLabel(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"textFieldLabel".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute. 
	 * @return the localized textFieldLabel - Label of the text field
	 */
	public Map<Language,String> getAllTextFieldLabel()
	{
		return getAllTextFieldLabel( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute. 
	 * @param value the textFieldLabel - Label of the text field
	 */
	public void setTextFieldLabel(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("TextFieldConfiguratorSetting.setTextFieldLabel requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "textFieldLabel".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute. 
	 * @param value the textFieldLabel - Label of the text field
	 */
	public void setTextFieldLabel(final String value)
	{
		setTextFieldLabel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute. 
	 * @param value the textFieldLabel - Label of the text field
	 */
	public void setAllTextFieldLabel(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"textFieldLabel".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute. 
	 * @param value the textFieldLabel - Label of the text field
	 */
	public void setAllTextFieldLabel(final Map<Language,String> value)
	{
		setAllTextFieldLabel( getSession().getSessionContext(), value );
	}
	
}
