/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.rules.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.ruleengineservices.jalo.SourceRule;
import de.hybris.platform.sap.productconfig.rules.constants.SapproductconfigrulesConstants;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.sap.productconfig.rules.jalo.ProductConfigSourceRule ProductConfigSourceRule}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedProductConfigSourceRule extends SourceRule
{
	/** Qualifier of the <code>ProductConfigSourceRule.messageSeverity</code> attribute **/
	public static final String MESSAGESEVERITY = "messageSeverity";
	/** Qualifier of the <code>ProductConfigSourceRule.messageForCstic</code> attribute **/
	public static final String MESSAGEFORCSTIC = "messageForCstic";
	/** Qualifier of the <code>ProductConfigSourceRule.baseStores</code> attribute **/
	public static final String BASESTORES = "baseStores";
	/** Relation ordering override parameter constants for BaseStoreForCPQRule from ((sapproductconfigrules))*/
	protected static String BASESTOREFORCPQRULE_SRC_ORDERED = "relation.BaseStoreForCPQRule.source.ordered";
	protected static String BASESTOREFORCPQRULE_TGT_ORDERED = "relation.BaseStoreForCPQRule.target.ordered";
	/** Relation disable markmodifed parameter constants for BaseStoreForCPQRule from ((sapproductconfigrules))*/
	protected static String BASESTOREFORCPQRULE_MARKMODIFIED = "relation.BaseStoreForCPQRule.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SourceRule.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(MESSAGESEVERITY, AttributeMode.INITIAL);
		tmp.put(MESSAGEFORCSTIC, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigSourceRule.baseStores</code> attribute.
	 * @return the baseStores
	 */
	public Set<BaseStore> getBaseStores(final SessionContext ctx)
	{
		final List<BaseStore> items = getLinkedItems( 
			ctx,
			false,
			SapproductconfigrulesConstants.Relations.BASESTOREFORCPQRULE,
			"BaseStore",
			null,
			false,
			false
		);
		return new LinkedHashSet<BaseStore>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigSourceRule.baseStores</code> attribute.
	 * @return the baseStores
	 */
	public Set<BaseStore> getBaseStores()
	{
		return getBaseStores( getSession().getSessionContext() );
	}
	
	public long getBaseStoresCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			SapproductconfigrulesConstants.Relations.BASESTOREFORCPQRULE,
			"BaseStore",
			null
		);
	}
	
	public long getBaseStoresCount()
	{
		return getBaseStoresCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigSourceRule.baseStores</code> attribute. 
	 * @param value the baseStores
	 */
	public void setBaseStores(final SessionContext ctx, final Set<BaseStore> value)
	{
		setLinkedItems( 
			ctx,
			false,
			SapproductconfigrulesConstants.Relations.BASESTOREFORCPQRULE,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTOREFORCPQRULE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigSourceRule.baseStores</code> attribute. 
	 * @param value the baseStores
	 */
	public void setBaseStores(final Set<BaseStore> value)
	{
		setBaseStores( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseStores. 
	 * @param value the item to add to baseStores
	 */
	public void addToBaseStores(final SessionContext ctx, final BaseStore value)
	{
		addLinkedItems( 
			ctx,
			false,
			SapproductconfigrulesConstants.Relations.BASESTOREFORCPQRULE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTOREFORCPQRULE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseStores. 
	 * @param value the item to add to baseStores
	 */
	public void addToBaseStores(final BaseStore value)
	{
		addToBaseStores( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseStores. 
	 * @param value the item to remove from baseStores
	 */
	public void removeFromBaseStores(final SessionContext ctx, final BaseStore value)
	{
		removeLinkedItems( 
			ctx,
			false,
			SapproductconfigrulesConstants.Relations.BASESTOREFORCPQRULE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTOREFORCPQRULE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseStores. 
	 * @param value the item to remove from baseStores
	 */
	public void removeFromBaseStores(final BaseStore value)
	{
		removeFromBaseStores( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("BaseStore");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(BASESTOREFORCPQRULE_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigSourceRule.messageForCstic</code> attribute.
	 * @return the messageForCstic - Message for characteristic
	 */
	public String getMessageForCstic(final SessionContext ctx)
	{
		return (String)getProperty( ctx, MESSAGEFORCSTIC);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigSourceRule.messageForCstic</code> attribute.
	 * @return the messageForCstic - Message for characteristic
	 */
	public String getMessageForCstic()
	{
		return getMessageForCstic( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigSourceRule.messageForCstic</code> attribute. 
	 * @param value the messageForCstic - Message for characteristic
	 */
	public void setMessageForCstic(final SessionContext ctx, final String value)
	{
		setProperty(ctx, MESSAGEFORCSTIC,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigSourceRule.messageForCstic</code> attribute. 
	 * @param value the messageForCstic - Message for characteristic
	 */
	public void setMessageForCstic(final String value)
	{
		setMessageForCstic( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigSourceRule.messageSeverity</code> attribute.
	 * @return the messageSeverity - Severity of the rule related message.
	 */
	public EnumerationValue getMessageSeverity(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, MESSAGESEVERITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigSourceRule.messageSeverity</code> attribute.
	 * @return the messageSeverity - Severity of the rule related message.
	 */
	public EnumerationValue getMessageSeverity()
	{
		return getMessageSeverity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigSourceRule.messageSeverity</code> attribute. 
	 * @param value the messageSeverity - Severity of the rule related message.
	 */
	public void setMessageSeverity(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, MESSAGESEVERITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigSourceRule.messageSeverity</code> attribute. 
	 * @param value the messageSeverity - Severity of the rule related message.
	 */
	public void setMessageSeverity(final EnumerationValue value)
	{
		setMessageSeverity( getSession().getSessionContext(), value );
	}
	
}
