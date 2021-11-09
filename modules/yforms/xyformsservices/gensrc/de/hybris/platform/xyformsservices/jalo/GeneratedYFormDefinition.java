/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.xyformsservices.constants.XyformsservicesConstants;
import de.hybris.platform.xyformsservices.jalo.YFormData;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem YFormDefinition}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedYFormDefinition extends GenericItem
{
	/** Qualifier of the <code>YFormDefinition.applicationId</code> attribute **/
	public static final String APPLICATIONID = "applicationId";
	/** Qualifier of the <code>YFormDefinition.formId</code> attribute **/
	public static final String FORMID = "formId";
	/** Qualifier of the <code>YFormDefinition.version</code> attribute **/
	public static final String VERSION = "version";
	/** Qualifier of the <code>YFormDefinition.title</code> attribute **/
	public static final String TITLE = "title";
	/** Qualifier of the <code>YFormDefinition.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>YFormDefinition.documentId</code> attribute **/
	public static final String DOCUMENTID = "documentId";
	/** Qualifier of the <code>YFormDefinition.system</code> attribute **/
	public static final String SYSTEM = "system";
	/** Qualifier of the <code>YFormDefinition.latest</code> attribute **/
	public static final String LATEST = "latest";
	/** Qualifier of the <code>YFormDefinition.status</code> attribute **/
	public static final String STATUS = "status";
	/** Qualifier of the <code>YFormDefinition.content</code> attribute **/
	public static final String CONTENT = "content";
	/** Qualifier of the <code>YFormDefinition.data</code> attribute **/
	public static final String DATA = "data";
	/** Qualifier of the <code>YFormDefinition.categories</code> attribute **/
	public static final String CATEGORIES = "categories";
	/** Relation ordering override parameter constants for Category2YFormDefinitionRelation from ((xyformsservices))*/
	protected static String CATEGORY2YFORMDEFINITIONRELATION_SRC_ORDERED = "relation.Category2YFormDefinitionRelation.source.ordered";
	protected static String CATEGORY2YFORMDEFINITIONRELATION_TGT_ORDERED = "relation.Category2YFormDefinitionRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for Category2YFormDefinitionRelation from ((xyformsservices))*/
	protected static String CATEGORY2YFORMDEFINITIONRELATION_MARKMODIFIED = "relation.Category2YFormDefinitionRelation.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n DATA's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<YFormData> DATAHANDLER = new OneToManyHandler<YFormData>(
	XyformsservicesConstants.TC.YFORMDATA,
	false,
	"formDefinition",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(APPLICATIONID, AttributeMode.INITIAL);
		tmp.put(FORMID, AttributeMode.INITIAL);
		tmp.put(VERSION, AttributeMode.INITIAL);
		tmp.put(TITLE, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(DOCUMENTID, AttributeMode.INITIAL);
		tmp.put(SYSTEM, AttributeMode.INITIAL);
		tmp.put(LATEST, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		tmp.put(CONTENT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.applicationId</code> attribute.
	 * @return the applicationId
	 */
	public String getApplicationId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, APPLICATIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.applicationId</code> attribute.
	 * @return the applicationId
	 */
	public String getApplicationId()
	{
		return getApplicationId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.applicationId</code> attribute. 
	 * @param value the applicationId
	 */
	public void setApplicationId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, APPLICATIONID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.applicationId</code> attribute. 
	 * @param value the applicationId
	 */
	public void setApplicationId(final String value)
	{
		setApplicationId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.categories</code> attribute.
	 * @return the categories - Categories
	 */
	public Collection<Category> getCategories(final SessionContext ctx)
	{
		final List<Category> items = getLinkedItems( 
			ctx,
			false,
			XyformsservicesConstants.Relations.CATEGORY2YFORMDEFINITIONRELATION,
			"Category",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.categories</code> attribute.
	 * @return the categories - Categories
	 */
	public Collection<Category> getCategories()
	{
		return getCategories( getSession().getSessionContext() );
	}
	
	public long getCategoriesCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			XyformsservicesConstants.Relations.CATEGORY2YFORMDEFINITIONRELATION,
			"Category",
			null
		);
	}
	
	public long getCategoriesCount()
	{
		return getCategoriesCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.categories</code> attribute. 
	 * @param value the categories - Categories
	 */
	public void setCategories(final SessionContext ctx, final Collection<Category> value)
	{
		setLinkedItems( 
			ctx,
			false,
			XyformsservicesConstants.Relations.CATEGORY2YFORMDEFINITIONRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(CATEGORY2YFORMDEFINITIONRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.categories</code> attribute. 
	 * @param value the categories - Categories
	 */
	public void setCategories(final Collection<Category> value)
	{
		setCategories( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to categories. 
	 * @param value the item to add to categories - Categories
	 */
	public void addToCategories(final SessionContext ctx, final Category value)
	{
		addLinkedItems( 
			ctx,
			false,
			XyformsservicesConstants.Relations.CATEGORY2YFORMDEFINITIONRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(CATEGORY2YFORMDEFINITIONRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to categories. 
	 * @param value the item to add to categories - Categories
	 */
	public void addToCategories(final Category value)
	{
		addToCategories( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from categories. 
	 * @param value the item to remove from categories - Categories
	 */
	public void removeFromCategories(final SessionContext ctx, final Category value)
	{
		removeLinkedItems( 
			ctx,
			false,
			XyformsservicesConstants.Relations.CATEGORY2YFORMDEFINITIONRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(CATEGORY2YFORMDEFINITIONRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from categories. 
	 * @param value the item to remove from categories - Categories
	 */
	public void removeFromCategories(final Category value)
	{
		removeFromCategories( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.content</code> attribute.
	 * @return the content
	 */
	public String getContent(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.content</code> attribute.
	 * @return the content
	 */
	public String getContent()
	{
		return getContent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.content</code> attribute. 
	 * @param value the content
	 */
	public void setContent(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CONTENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.content</code> attribute. 
	 * @param value the content
	 */
	public void setContent(final String value)
	{
		setContent( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.data</code> attribute.
	 * @return the data
	 */
	public Collection<YFormData> getData(final SessionContext ctx)
	{
		return DATAHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.data</code> attribute.
	 * @return the data
	 */
	public Collection<YFormData> getData()
	{
		return getData( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.data</code> attribute. 
	 * @param value the data
	 */
	public void setData(final SessionContext ctx, final Collection<YFormData> value)
	{
		DATAHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.data</code> attribute. 
	 * @param value the data
	 */
	public void setData(final Collection<YFormData> value)
	{
		setData( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to data. 
	 * @param value the item to add to data
	 */
	public void addToData(final SessionContext ctx, final YFormData value)
	{
		DATAHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to data. 
	 * @param value the item to add to data
	 */
	public void addToData(final YFormData value)
	{
		addToData( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from data. 
	 * @param value the item to remove from data
	 */
	public void removeFromData(final SessionContext ctx, final YFormData value)
	{
		DATAHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from data. 
	 * @param value the item to remove from data
	 */
	public void removeFromData(final YFormData value)
	{
		removeFromData( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.description</code> attribute.
	 * @return the description
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.description</code> attribute.
	 * @return the description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.documentId</code> attribute.
	 * @return the documentId
	 */
	public String getDocumentId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DOCUMENTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.documentId</code> attribute.
	 * @return the documentId
	 */
	public String getDocumentId()
	{
		return getDocumentId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.documentId</code> attribute. 
	 * @param value the documentId
	 */
	public void setDocumentId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DOCUMENTID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.documentId</code> attribute. 
	 * @param value the documentId
	 */
	public void setDocumentId(final String value)
	{
		setDocumentId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.formId</code> attribute.
	 * @return the formId
	 */
	public String getFormId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FORMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.formId</code> attribute.
	 * @return the formId
	 */
	public String getFormId()
	{
		return getFormId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.formId</code> attribute. 
	 * @param value the formId
	 */
	public void setFormId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FORMID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.formId</code> attribute. 
	 * @param value the formId
	 */
	public void setFormId(final String value)
	{
		setFormId( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Category");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(CATEGORY2YFORMDEFINITIONRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.latest</code> attribute.
	 * @return the latest
	 */
	public Boolean isLatest(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, LATEST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.latest</code> attribute.
	 * @return the latest
	 */
	public Boolean isLatest()
	{
		return isLatest( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.latest</code> attribute. 
	 * @return the latest
	 */
	public boolean isLatestAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isLatest( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.latest</code> attribute. 
	 * @return the latest
	 */
	public boolean isLatestAsPrimitive()
	{
		return isLatestAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.latest</code> attribute. 
	 * @param value the latest
	 */
	public void setLatest(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, LATEST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.latest</code> attribute. 
	 * @param value the latest
	 */
	public void setLatest(final Boolean value)
	{
		setLatest( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.latest</code> attribute. 
	 * @param value the latest
	 */
	public void setLatest(final SessionContext ctx, final boolean value)
	{
		setLatest( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.latest</code> attribute. 
	 * @param value the latest
	 */
	public void setLatest(final boolean value)
	{
		setLatest( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final EnumerationValue value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.system</code> attribute.
	 * @return the system
	 */
	public Boolean isSystem(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SYSTEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.system</code> attribute.
	 * @return the system
	 */
	public Boolean isSystem()
	{
		return isSystem( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.system</code> attribute. 
	 * @return the system
	 */
	public boolean isSystemAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isSystem( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.system</code> attribute. 
	 * @return the system
	 */
	public boolean isSystemAsPrimitive()
	{
		return isSystemAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.system</code> attribute. 
	 * @param value the system
	 */
	public void setSystem(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SYSTEM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.system</code> attribute. 
	 * @param value the system
	 */
	public void setSystem(final Boolean value)
	{
		setSystem( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.system</code> attribute. 
	 * @param value the system
	 */
	public void setSystem(final SessionContext ctx, final boolean value)
	{
		setSystem( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.system</code> attribute. 
	 * @param value the system
	 */
	public void setSystem(final boolean value)
	{
		setSystem( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.title</code> attribute.
	 * @return the title
	 */
	public String getTitle(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.title</code> attribute.
	 * @return the title
	 */
	public String getTitle()
	{
		return getTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.title</code> attribute. 
	 * @param value the title
	 */
	public void setTitle(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.title</code> attribute. 
	 * @param value the title
	 */
	public void setTitle(final String value)
	{
		setTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.version</code> attribute.
	 * @return the version
	 */
	public Integer getVersion(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.version</code> attribute.
	 * @return the version
	 */
	public Integer getVersion()
	{
		return getVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.version</code> attribute. 
	 * @return the version
	 */
	public int getVersionAsPrimitive(final SessionContext ctx)
	{
		Integer value = getVersion( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.version</code> attribute. 
	 * @return the version
	 */
	public int getVersionAsPrimitive()
	{
		return getVersionAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.version</code> attribute. 
	 * @param value the version
	 */
	public void setVersion(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, VERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.version</code> attribute. 
	 * @param value the version
	 */
	public void setVersion(final Integer value)
	{
		setVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.version</code> attribute. 
	 * @param value the version
	 */
	public void setVersion(final SessionContext ctx, final int value)
	{
		setVersion( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>YFormDefinition.version</code> attribute. 
	 * @param value the version
	 */
	public void setVersion(final int value)
	{
		setVersion( getSession().getSessionContext(), value );
	}
	
}
