/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.jalo;

import de.hybris.platform.configurablebundleservices.constants.ConfigurableBundleServicesConstants;
import de.hybris.platform.configurablebundleservices.jalo.AbstractBundleRule;
import de.hybris.platform.configurablebundleservices.jalo.BundleTemplate;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.configurablebundleservices.jalo.ChangeProductPriceBundleRule ChangeProductPriceBundleRule}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedChangeProductPriceBundleRule extends AbstractBundleRule
{
	/** Qualifier of the <code>ChangeProductPriceBundleRule.price</code> attribute **/
	public static final String PRICE = "price";
	/** Qualifier of the <code>ChangeProductPriceBundleRule.currency</code> attribute **/
	public static final String CURRENCY = "currency";
	/** Qualifier of the <code>ChangeProductPriceBundleRule.bundleTemplate</code> attribute **/
	public static final String BUNDLETEMPLATE = "bundleTemplate";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n BUNDLETEMPLATE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedChangeProductPriceBundleRule> BUNDLETEMPLATEHANDLER = new BidirectionalOneToManyHandler<GeneratedChangeProductPriceBundleRule>(
	ConfigurableBundleServicesConstants.TC.CHANGEPRODUCTPRICEBUNDLERULE,
	false,
	"bundleTemplate",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractBundleRule.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(PRICE, AttributeMode.INITIAL);
		tmp.put(CURRENCY, AttributeMode.INITIAL);
		tmp.put(BUNDLETEMPLATE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeProductPriceBundleRule.bundleTemplate</code> attribute.
	 * @return the bundleTemplate
	 */
	public BundleTemplate getBundleTemplate(final SessionContext ctx)
	{
		return (BundleTemplate)getProperty( ctx, BUNDLETEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeProductPriceBundleRule.bundleTemplate</code> attribute.
	 * @return the bundleTemplate
	 */
	public BundleTemplate getBundleTemplate()
	{
		return getBundleTemplate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ChangeProductPriceBundleRule.bundleTemplate</code> attribute. 
	 * @param value the bundleTemplate
	 */
	public void setBundleTemplate(final SessionContext ctx, final BundleTemplate value)
	{
		BUNDLETEMPLATEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ChangeProductPriceBundleRule.bundleTemplate</code> attribute. 
	 * @param value the bundleTemplate
	 */
	public void setBundleTemplate(final BundleTemplate value)
	{
		setBundleTemplate( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		BUNDLETEMPLATEHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeProductPriceBundleRule.currency</code> attribute.
	 * @return the currency - Currency
	 */
	public Currency getCurrency(final SessionContext ctx)
	{
		return (Currency)getProperty( ctx, CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeProductPriceBundleRule.currency</code> attribute.
	 * @return the currency - Currency
	 */
	public Currency getCurrency()
	{
		return getCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ChangeProductPriceBundleRule.currency</code> attribute. 
	 * @param value the currency - Currency
	 */
	public void setCurrency(final SessionContext ctx, final Currency value)
	{
		setProperty(ctx, CURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ChangeProductPriceBundleRule.currency</code> attribute. 
	 * @param value the currency - Currency
	 */
	public void setCurrency(final Currency value)
	{
		setCurrency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeProductPriceBundleRule.price</code> attribute.
	 * @return the price - price set by bundle rule
	 */
	public BigDecimal getPrice(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, PRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeProductPriceBundleRule.price</code> attribute.
	 * @return the price - price set by bundle rule
	 */
	public BigDecimal getPrice()
	{
		return getPrice( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ChangeProductPriceBundleRule.price</code> attribute. 
	 * @param value the price - price set by bundle rule
	 */
	public void setPrice(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, PRICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ChangeProductPriceBundleRule.price</code> attribute. 
	 * @param value the price - price set by bundle rule
	 */
	public void setPrice(final BigDecimal value)
	{
		setPrice( getSession().getSessionContext(), value );
	}
	
}
