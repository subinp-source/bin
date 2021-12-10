/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
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
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.xyformsservices.constants.XyformsservicesConstants;
import de.hybris.platform.xyformsservices.jalo.YFormDataHistory;
import de.hybris.platform.xyformsservices.jalo.YFormDefinition;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem YFormData}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedYFormData extends GenericItem
{
	/** Qualifier of the <code>YFormData.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>YFormData.applicationId</code> attribute **/
	public static final String APPLICATIONID = "applicationId";
	/** Qualifier of the <code>YFormData.formId</code> attribute **/
	public static final String FORMID = "formId";
	/** Qualifier of the <code>YFormData.refId</code> attribute **/
	public static final String REFID = "refId";
	/** Qualifier of the <code>YFormData.system</code> attribute **/
	public static final String SYSTEM = "system";
	/** Qualifier of the <code>YFormData.type</code> attribute **/
	public static final String TYPE = "type";
	/** Qualifier of the <code>YFormData.content</code> attribute **/
	public static final String CONTENT = "content";
	/** Qualifier of the <code>YFormData.formDefinition</code> attribute **/
	public static final String FORMDEFINITION = "formDefinition";
	/** Qualifier of the <code>YFormData.history</code> attribute **/
	public static final String HISTORY = "history";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n FORMDEFINITION's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedYFormData> FORMDEFINITIONHANDLER = new BidirectionalOneToManyHandler<GeneratedYFormData>(
	XyformsservicesConstants.TC.YFORMDATA,
	false,
	"formDefinition",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n HISTORY's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<YFormDataHistory> HISTORYHANDLER = new OneToManyHandler<YFormDataHistory>(
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
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(APPLICATIONID, AttributeMode.INITIAL);
		tmp.put(FORMID, AttributeMode.INITIAL);
		tmp.put(REFID, AttributeMode.INITIAL);
		tmp.put(SYSTEM, AttributeMode.INITIAL);
		tmp.put(TYPE, AttributeMode.INITIAL);
		tmp.put(CONTENT, AttributeMode.INITIAL);
		tmp.put(FORMDEFINITION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.applicationId</code> attribute.
	 * @return the applicationId
	 */
	public String getApplicationId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, APPLICATIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.applicationId</code> attribute.
	 * @return the applicationId
	 */
	public String getApplicationId()
	{
		return getApplicationId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.applicationId</code> attribute. 
	 * @param value the applicationId
	 */
	public void setApplicationId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, APPLICATIONID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.applicationId</code> attribute. 
	 * @param value the applicationId
	 */
	public void setApplicationId(final String value)
	{
		setApplicationId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.content</code> attribute.
	 * @return the content
	 */
	public String getContent(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.content</code> attribute.
	 * @return the content
	 */
	public String getContent()
	{
		return getContent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.content</code> attribute. 
	 * @param value the content
	 */
	public void setContent(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CONTENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.content</code> attribute. 
	 * @param value the content
	 */
	public void setContent(final String value)
	{
		setContent( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		FORMDEFINITIONHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.formDefinition</code> attribute.
	 * @return the formDefinition
	 */
	public YFormDefinition getFormDefinition(final SessionContext ctx)
	{
		return (YFormDefinition)getProperty( ctx, FORMDEFINITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.formDefinition</code> attribute.
	 * @return the formDefinition
	 */
	public YFormDefinition getFormDefinition()
	{
		return getFormDefinition( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.formDefinition</code> attribute. 
	 * @param value the formDefinition
	 */
	public void setFormDefinition(final SessionContext ctx, final YFormDefinition value)
	{
		FORMDEFINITIONHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.formDefinition</code> attribute. 
	 * @param value the formDefinition
	 */
	public void setFormDefinition(final YFormDefinition value)
	{
		setFormDefinition( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.formId</code> attribute.
	 * @return the formId
	 */
	public String getFormId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FORMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.formId</code> attribute.
	 * @return the formId
	 */
	public String getFormId()
	{
		return getFormId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.formId</code> attribute. 
	 * @param value the formId
	 */
	public void setFormId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FORMID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.formId</code> attribute. 
	 * @param value the formId
	 */
	public void setFormId(final String value)
	{
		setFormId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.history</code> attribute.
	 * @return the history
	 */
	public List<YFormDataHistory> getHistory(final SessionContext ctx)
	{
		return (List<YFormDataHistory>)HISTORYHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.history</code> attribute.
	 * @return the history
	 */
	public List<YFormDataHistory> getHistory()
	{
		return getHistory( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.history</code> attribute. 
	 * @param value the history
	 */
	public void setHistory(final SessionContext ctx, final List<YFormDataHistory> value)
	{
		HISTORYHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.history</code> attribute. 
	 * @param value the history
	 */
	public void setHistory(final List<YFormDataHistory> value)
	{
		setHistory( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to history. 
	 * @param value the item to add to history
	 */
	public void addToHistory(final SessionContext ctx, final YFormDataHistory value)
	{
		HISTORYHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to history. 
	 * @param value the item to add to history
	 */
	public void addToHistory(final YFormDataHistory value)
	{
		addToHistory( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from history. 
	 * @param value the item to remove from history
	 */
	public void removeFromHistory(final SessionContext ctx, final YFormDataHistory value)
	{
		HISTORYHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from history. 
	 * @param value the item to remove from history
	 */
	public void removeFromHistory(final YFormDataHistory value)
	{
		removeFromHistory( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.id</code> attribute.
	 * @return the id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.id</code> attribute.
	 * @return the id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.refId</code> attribute.
	 * @return the refId
	 */
	public String getRefId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, REFID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.refId</code> attribute.
	 * @return the refId
	 */
	public String getRefId()
	{
		return getRefId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.refId</code> attribute. 
	 * @param value the refId
	 */
	public void setRefId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, REFID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.refId</code> attribute. 
	 * @param value the refId
	 */
	public void setRefId(final String value)
	{
		setRefId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.system</code> attribute.
	 * @return the system
	 */
	public Boolean isSystem(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SYSTEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.system</code> attribute.
	 * @return the system
	 */
	public Boolean isSystem()
	{
		return isSystem( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.system</code> attribute. 
	 * @return the system
	 */
	public boolean isSystemAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isSystem( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.system</code> attribute. 
	 * @return the system
	 */
	public boolean isSystemAsPrimitive()
	{
		return isSystemAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.system</code> attribute. 
	 * @param value the system
	 */
	public void setSystem(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SYSTEM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.system</code> attribute. 
	 * @param value the system
	 */
	public void setSystem(final Boolean value)
	{
		setSystem( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.system</code> attribute. 
	 * @param value the system
	 */
	public void setSystem(final SessionContext ctx, final boolean value)
	{
		setSystem( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.system</code> attribute. 
	 * @param value the system
	 */
	public void setSystem(final boolean value)
	{
		setSystem( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.type</code> attribute.
	 * @return the type
	 */
	public EnumerationValue getType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.type</code> attribute.
	 * @return the type
	 */
	public EnumerationValue getType()
	{
		return getType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.type</code> attribute. 
	 * @param value the type
	 */
	public void setType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, TYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormData.type</code> attribute. 
	 * @param value the type
	 */
	public void setType(final EnumerationValue value)
	{
		setType( getSession().getSessionContext(), value );
	}
	
}
