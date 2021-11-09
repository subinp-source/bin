/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
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
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.configurablebundleservices.jalo.DisableProductBundleRule DisableProductBundleRule}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedDisableProductBundleRule extends AbstractBundleRule
{
	/** Qualifier of the <code>DisableProductBundleRule.bundleTemplate</code> attribute **/
	public static final String BUNDLETEMPLATE = "bundleTemplate";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n BUNDLETEMPLATE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedDisableProductBundleRule> BUNDLETEMPLATEHANDLER = new BidirectionalOneToManyHandler<GeneratedDisableProductBundleRule>(
	ConfigurableBundleServicesConstants.TC.DISABLEPRODUCTBUNDLERULE,
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
		tmp.put(BUNDLETEMPLATE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DisableProductBundleRule.bundleTemplate</code> attribute.
	 * @return the bundleTemplate
	 */
	public BundleTemplate getBundleTemplate(final SessionContext ctx)
	{
		return (BundleTemplate)getProperty( ctx, BUNDLETEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DisableProductBundleRule.bundleTemplate</code> attribute.
	 * @return the bundleTemplate
	 */
	public BundleTemplate getBundleTemplate()
	{
		return getBundleTemplate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DisableProductBundleRule.bundleTemplate</code> attribute. 
	 * @param value the bundleTemplate
	 */
	public void setBundleTemplate(final SessionContext ctx, final BundleTemplate value)
	{
		BUNDLETEMPLATEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DisableProductBundleRule.bundleTemplate</code> attribute. 
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
	
}
