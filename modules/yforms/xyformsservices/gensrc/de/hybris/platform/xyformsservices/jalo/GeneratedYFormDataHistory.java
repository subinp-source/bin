/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.xyformsservices.constants.XyformsservicesConstants;
import de.hybris.platform.xyformsservices.jalo.YFormData;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem YFormDataHistory}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedYFormDataHistory extends GenericItem
{
	/** Qualifier of the <code>YFormDataHistory.formDataId</code> attribute **/
	public static final String FORMDATAID = "formDataId";
	/** Qualifier of the <code>YFormDataHistory.content</code> attribute **/
	public static final String CONTENT = "content";
	/** Qualifier of the <code>YFormDataHistory.formDataPOS</code> attribute **/
	public static final String FORMDATAPOS = "formDataPOS";
	/** Qualifier of the <code>YFormDataHistory.formData</code> attribute **/
	public static final String FORMDATA = "formData";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n FORMDATA's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedYFormDataHistory> FORMDATAHANDLER = new BidirectionalOneToManyHandler<GeneratedYFormDataHistory>(
	XyformsservicesConstants.TC.YFORMDATAHISTORY,
	false,
	"formData",
	"formDataPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(FORMDATAID, AttributeMode.INITIAL);
		tmp.put(CONTENT, AttributeMode.INITIAL);
		tmp.put(FORMDATAPOS, AttributeMode.INITIAL);
		tmp.put(FORMDATA, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.content</code> attribute.
	 * @return the content
	 */
	public String getContent(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.content</code> attribute.
	 * @return the content
	 */
	public String getContent()
	{
		return getContent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDataHistory.content</code> attribute. 
	 * @param value the content
	 */
	public void setContent(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CONTENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDataHistory.content</code> attribute. 
	 * @param value the content
	 */
	public void setContent(final String value)
	{
		setContent( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		FORMDATAHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.formData</code> attribute.
	 * @return the formData
	 */
	public YFormData getFormData(final SessionContext ctx)
	{
		return (YFormData)getProperty( ctx, FORMDATA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.formData</code> attribute.
	 * @return the formData
	 */
	public YFormData getFormData()
	{
		return getFormData( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDataHistory.formData</code> attribute. 
	 * @param value the formData
	 */
	public void setFormData(final SessionContext ctx, final YFormData value)
	{
		FORMDATAHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDataHistory.formData</code> attribute. 
	 * @param value the formData
	 */
	public void setFormData(final YFormData value)
	{
		setFormData( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.formDataId</code> attribute.
	 * @return the formDataId
	 */
	public String getFormDataId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FORMDATAID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.formDataId</code> attribute.
	 * @return the formDataId
	 */
	public String getFormDataId()
	{
		return getFormDataId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDataHistory.formDataId</code> attribute. 
	 * @param value the formDataId
	 */
	public void setFormDataId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FORMDATAID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDataHistory.formDataId</code> attribute. 
	 * @param value the formDataId
	 */
	public void setFormDataId(final String value)
	{
		setFormDataId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.formDataPOS</code> attribute.
	 * @return the formDataPOS
	 */
	 Integer getFormDataPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, FORMDATAPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.formDataPOS</code> attribute.
	 * @return the formDataPOS
	 */
	 Integer getFormDataPOS()
	{
		return getFormDataPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.formDataPOS</code> attribute. 
	 * @return the formDataPOS
	 */
	 int getFormDataPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getFormDataPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.formDataPOS</code> attribute. 
	 * @return the formDataPOS
	 */
	 int getFormDataPOSAsPrimitive()
	{
		return getFormDataPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDataHistory.formDataPOS</code> attribute. 
	 * @param value the formDataPOS
	 */
	 void setFormDataPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, FORMDATAPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDataHistory.formDataPOS</code> attribute. 
	 * @param value the formDataPOS
	 */
	 void setFormDataPOS(final Integer value)
	{
		setFormDataPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDataHistory.formDataPOS</code> attribute. 
	 * @param value the formDataPOS
	 */
	 void setFormDataPOS(final SessionContext ctx, final int value)
	{
		setFormDataPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDataHistory.formDataPOS</code> attribute. 
	 * @param value the formDataPOS
	 */
	 void setFormDataPOS(final int value)
	{
		setFormDataPOS( getSession().getSessionContext(), value );
	}
	
}
