/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.jalo.component;

import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.xyformsservices.constants.XyformsservicesConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.xyformsservices.jalo.component.YFormCMSComponent YFormCMSComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedYFormCMSComponent extends SimpleCMSComponent
{
	/** Qualifier of the <code>YFormCMSComponent.applicationId</code> attribute **/
	public static final String APPLICATIONID = "applicationId";
	/** Qualifier of the <code>YFormCMSComponent.formId</code> attribute **/
	public static final String FORMID = "formId";
	/** Qualifier of the <code>YFormCMSComponent.formDataId</code> attribute **/
	public static final String FORMDATAID = "formDataId";
	/** Qualifier of the <code>YFormCMSComponent.action</code> attribute **/
	public static final String ACTION = "action";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(APPLICATIONID, AttributeMode.INITIAL);
		tmp.put(FORMID, AttributeMode.INITIAL);
		tmp.put(FORMDATAID, AttributeMode.INITIAL);
		tmp.put(ACTION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.action</code> attribute.
	 * @return the action
	 */
	public EnumerationValue getAction(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, ACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.action</code> attribute.
	 * @return the action
	 */
	public EnumerationValue getAction()
	{
		return getAction( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormCMSComponent.action</code> attribute. 
	 * @param value the action
	 */
	public void setAction(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, ACTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormCMSComponent.action</code> attribute. 
	 * @param value the action
	 */
	public void setAction(final EnumerationValue value)
	{
		setAction( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.applicationId</code> attribute.
	 * @return the applicationId
	 */
	public String getApplicationId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, APPLICATIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.applicationId</code> attribute.
	 * @return the applicationId
	 */
	public String getApplicationId()
	{
		return getApplicationId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormCMSComponent.applicationId</code> attribute. 
	 * @param value the applicationId
	 */
	public void setApplicationId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, APPLICATIONID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormCMSComponent.applicationId</code> attribute. 
	 * @param value the applicationId
	 */
	public void setApplicationId(final String value)
	{
		setApplicationId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.formDataId</code> attribute.
	 * @return the formDataId
	 */
	public String getFormDataId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FORMDATAID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.formDataId</code> attribute.
	 * @return the formDataId
	 */
	public String getFormDataId()
	{
		return getFormDataId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormCMSComponent.formDataId</code> attribute. 
	 * @param value the formDataId
	 */
	public void setFormDataId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FORMDATAID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormCMSComponent.formDataId</code> attribute. 
	 * @param value the formDataId
	 */
	public void setFormDataId(final String value)
	{
		setFormDataId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.formId</code> attribute.
	 * @return the formId
	 */
	public String getFormId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FORMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.formId</code> attribute.
	 * @return the formId
	 */
	public String getFormId()
	{
		return getFormId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormCMSComponent.formId</code> attribute. 
	 * @param value the formId
	 */
	public void setFormId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FORMID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormCMSComponent.formId</code> attribute. 
	 * @param value the formId
	 */
	public void setFormId(final String value)
	{
		setFormId( getSession().getSessionContext(), value );
	}
	
}
