/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.jalo;

import de.hybris.platform.b2b.constants.B2BCommerceConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BReportingSet B2BReportingSet}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BReportingSet extends GenericItem
{
	/** Qualifier of the <code>B2BReportingSet.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>B2BReportingSet.ReportingEntries</code> attribute **/
	public static final String REPORTINGENTRIES = "ReportingEntries";
	/** Relation ordering override parameter constants for B2BReportingEntry from ((b2bcommerce))*/
	protected static String B2BREPORTINGENTRY_SRC_ORDERED = "relation.B2BReportingEntry.source.ordered";
	protected static String B2BREPORTINGENTRY_TGT_ORDERED = "relation.B2BReportingEntry.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BReportingEntry from ((b2bcommerce))*/
	protected static String B2BREPORTINGENTRY_MARKMODIFIED = "relation.B2BReportingEntry.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BReportingSet.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BReportingSet.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BReportingSet.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BReportingSet.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Item");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(B2BREPORTINGENTRY_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BReportingSet.ReportingEntries</code> attribute.
	 * @return the ReportingEntries
	 */
	public Set<Item> getReportingEntries(final SessionContext ctx)
	{
		final List<Item> items = getLinkedItems( 
			ctx,
			false,
			B2BCommerceConstants.Relations.B2BREPORTINGENTRY,
			"Item",
			null,
			Utilities.getRelationOrderingOverride(B2BREPORTINGENTRY_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<Item>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BReportingSet.ReportingEntries</code> attribute.
	 * @return the ReportingEntries
	 */
	public Set<Item> getReportingEntries()
	{
		return getReportingEntries( getSession().getSessionContext() );
	}
	
	public long getReportingEntriesCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			B2BCommerceConstants.Relations.B2BREPORTINGENTRY,
			"Item",
			null
		);
	}
	
	public long getReportingEntriesCount()
	{
		return getReportingEntriesCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BReportingSet.ReportingEntries</code> attribute. 
	 * @param value the ReportingEntries
	 */
	public void setReportingEntries(final SessionContext ctx, final Set<Item> value)
	{
		setLinkedItems( 
			ctx,
			false,
			B2BCommerceConstants.Relations.B2BREPORTINGENTRY,
			null,
			value,
			Utilities.getRelationOrderingOverride(B2BREPORTINGENTRY_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BREPORTINGENTRY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BReportingSet.ReportingEntries</code> attribute. 
	 * @param value the ReportingEntries
	 */
	public void setReportingEntries(final Set<Item> value)
	{
		setReportingEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to ReportingEntries. 
	 * @param value the item to add to ReportingEntries
	 */
	public void addToReportingEntries(final SessionContext ctx, final Item value)
	{
		addLinkedItems( 
			ctx,
			false,
			B2BCommerceConstants.Relations.B2BREPORTINGENTRY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BREPORTINGENTRY_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BREPORTINGENTRY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to ReportingEntries. 
	 * @param value the item to add to ReportingEntries
	 */
	public void addToReportingEntries(final Item value)
	{
		addToReportingEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from ReportingEntries. 
	 * @param value the item to remove from ReportingEntries
	 */
	public void removeFromReportingEntries(final SessionContext ctx, final Item value)
	{
		removeLinkedItems( 
			ctx,
			false,
			B2BCommerceConstants.Relations.B2BREPORTINGENTRY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BREPORTINGENTRY_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BREPORTINGENTRY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from ReportingEntries. 
	 * @param value the item to remove from ReportingEntries
	 */
	public void removeFromReportingEntries(final Item value)
	{
		removeFromReportingEntries( getSession().getSessionContext(), value );
	}
	
}
