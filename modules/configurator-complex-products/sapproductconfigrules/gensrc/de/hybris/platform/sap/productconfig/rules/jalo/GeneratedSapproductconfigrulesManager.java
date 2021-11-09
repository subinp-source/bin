/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.rules.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.ruleengine.jalo.AbstractRuleEngineRule;
import de.hybris.platform.sap.productconfig.rules.constants.SapproductconfigrulesConstants;
import de.hybris.platform.sap.productconfig.rules.jalo.ProductConfigSourceRule;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type <code>SapproductconfigrulesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSapproductconfigrulesManager extends Extension
{
	/** Relation ordering override parameter constants for BaseStoreForCPQRule from ((sapproductconfigrules))*/
	protected static String BASESTOREFORCPQRULE_SRC_ORDERED = "relation.BaseStoreForCPQRule.source.ordered";
	protected static String BASESTOREFORCPQRULE_TGT_ORDERED = "relation.BaseStoreForCPQRule.target.ordered";
	/** Relation disable markmodifed parameter constants for BaseStoreForCPQRule from ((sapproductconfigrules))*/
	protected static String BASESTOREFORCPQRULE_MARKMODIFIED = "relation.BaseStoreForCPQRule.markmodified";
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("messageSeverity", AttributeMode.INITIAL);
		tmp.put("messageForCstic", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.ruleengine.jalo.AbstractRuleEngineRule", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public ProductConfigSourceRule createProductConfigSourceRule(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigrulesConstants.TC.PRODUCTCONFIGSOURCERULE );
			return (ProductConfigSourceRule)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ProductConfigSourceRule : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigSourceRule createProductConfigSourceRule(final Map attributeValues)
	{
		return createProductConfigSourceRule( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return SapproductconfigrulesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.messageForCstic</code> attribute.
	 * @return the messageForCstic - Message for characteristic
	 */
	public String getMessageForCstic(final SessionContext ctx, final AbstractRuleEngineRule item)
	{
		return (String)item.getProperty( ctx, SapproductconfigrulesConstants.Attributes.AbstractRuleEngineRule.MESSAGEFORCSTIC);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.messageForCstic</code> attribute.
	 * @return the messageForCstic - Message for characteristic
	 */
	public String getMessageForCstic(final AbstractRuleEngineRule item)
	{
		return getMessageForCstic( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractRuleEngineRule.messageForCstic</code> attribute. 
	 * @param value the messageForCstic - Message for characteristic
	 */
	public void setMessageForCstic(final SessionContext ctx, final AbstractRuleEngineRule item, final String value)
	{
		item.setProperty(ctx, SapproductconfigrulesConstants.Attributes.AbstractRuleEngineRule.MESSAGEFORCSTIC,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractRuleEngineRule.messageForCstic</code> attribute. 
	 * @param value the messageForCstic - Message for characteristic
	 */
	public void setMessageForCstic(final AbstractRuleEngineRule item, final String value)
	{
		setMessageForCstic( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.messageSeverity</code> attribute.
	 * @return the messageSeverity - Severity of the rule related message.
	 */
	public EnumerationValue getMessageSeverity(final SessionContext ctx, final AbstractRuleEngineRule item)
	{
		return (EnumerationValue)item.getProperty( ctx, SapproductconfigrulesConstants.Attributes.AbstractRuleEngineRule.MESSAGESEVERITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.messageSeverity</code> attribute.
	 * @return the messageSeverity - Severity of the rule related message.
	 */
	public EnumerationValue getMessageSeverity(final AbstractRuleEngineRule item)
	{
		return getMessageSeverity( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractRuleEngineRule.messageSeverity</code> attribute. 
	 * @param value the messageSeverity - Severity of the rule related message.
	 */
	public void setMessageSeverity(final SessionContext ctx, final AbstractRuleEngineRule item, final EnumerationValue value)
	{
		item.setProperty(ctx, SapproductconfigrulesConstants.Attributes.AbstractRuleEngineRule.MESSAGESEVERITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractRuleEngineRule.messageSeverity</code> attribute. 
	 * @param value the messageSeverity - Severity of the rule related message.
	 */
	public void setMessageSeverity(final AbstractRuleEngineRule item, final EnumerationValue value)
	{
		setMessageSeverity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.productConfigSourceRules</code> attribute.
	 * @return the productConfigSourceRules
	 */
	public Set<ProductConfigSourceRule> getProductConfigSourceRules(final SessionContext ctx, final BaseStore item)
	{
		final List<ProductConfigSourceRule> items = item.getLinkedItems( 
			ctx,
			true,
			SapproductconfigrulesConstants.Relations.BASESTOREFORCPQRULE,
			"ProductConfigSourceRule",
			null,
			false,
			false
		);
		return new LinkedHashSet<ProductConfigSourceRule>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.productConfigSourceRules</code> attribute.
	 * @return the productConfigSourceRules
	 */
	public Set<ProductConfigSourceRule> getProductConfigSourceRules(final BaseStore item)
	{
		return getProductConfigSourceRules( getSession().getSessionContext(), item );
	}
	
	public long getProductConfigSourceRulesCount(final SessionContext ctx, final BaseStore item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			SapproductconfigrulesConstants.Relations.BASESTOREFORCPQRULE,
			"ProductConfigSourceRule",
			null
		);
	}
	
	public long getProductConfigSourceRulesCount(final BaseStore item)
	{
		return getProductConfigSourceRulesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.productConfigSourceRules</code> attribute. 
	 * @param value the productConfigSourceRules
	 */
	public void setProductConfigSourceRules(final SessionContext ctx, final BaseStore item, final Set<ProductConfigSourceRule> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			SapproductconfigrulesConstants.Relations.BASESTOREFORCPQRULE,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTOREFORCPQRULE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.productConfigSourceRules</code> attribute. 
	 * @param value the productConfigSourceRules
	 */
	public void setProductConfigSourceRules(final BaseStore item, final Set<ProductConfigSourceRule> value)
	{
		setProductConfigSourceRules( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productConfigSourceRules. 
	 * @param value the item to add to productConfigSourceRules
	 */
	public void addToProductConfigSourceRules(final SessionContext ctx, final BaseStore item, final ProductConfigSourceRule value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			SapproductconfigrulesConstants.Relations.BASESTOREFORCPQRULE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTOREFORCPQRULE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productConfigSourceRules. 
	 * @param value the item to add to productConfigSourceRules
	 */
	public void addToProductConfigSourceRules(final BaseStore item, final ProductConfigSourceRule value)
	{
		addToProductConfigSourceRules( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productConfigSourceRules. 
	 * @param value the item to remove from productConfigSourceRules
	 */
	public void removeFromProductConfigSourceRules(final SessionContext ctx, final BaseStore item, final ProductConfigSourceRule value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			SapproductconfigrulesConstants.Relations.BASESTOREFORCPQRULE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BASESTOREFORCPQRULE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productConfigSourceRules. 
	 * @param value the item to remove from productConfigSourceRules
	 */
	public void removeFromProductConfigSourceRules(final BaseStore item, final ProductConfigSourceRule value)
	{
		removeFromProductConfigSourceRules( getSession().getSessionContext(), item, value );
	}
	
}
