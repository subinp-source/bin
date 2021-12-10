/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.jalo;

import com.hybris.merchandising.constants.MerchandisingservicesConstants;
import com.hybris.merchandising.jalo.AbstractMerchProperty;
import com.hybris.merchandising.jalo.MerchIndexingConfig;
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
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem MerchImageProperty}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedMerchImageProperty extends AbstractMerchProperty
{
	/** Qualifier of the <code>MerchImageProperty.merchIndexingConfigPOS</code> attribute **/
	public static final String MERCHINDEXINGCONFIGPOS = "merchIndexingConfigPOS";
	/** Qualifier of the <code>MerchImageProperty.merchIndexingConfig</code> attribute **/
	public static final String MERCHINDEXINGCONFIG = "merchIndexingConfig";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n MERCHINDEXINGCONFIG's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedMerchImageProperty> MERCHINDEXINGCONFIGHANDLER = new BidirectionalOneToManyHandler<GeneratedMerchImageProperty>(
	MerchandisingservicesConstants.TC.MERCHIMAGEPROPERTY,
	false,
	"merchIndexingConfig",
	"merchIndexingConfigPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractMerchProperty.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(MERCHINDEXINGCONFIGPOS, AttributeMode.INITIAL);
		tmp.put(MERCHINDEXINGCONFIG, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		MERCHINDEXINGCONFIGHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchImageProperty.merchIndexingConfig</code> attribute.
	 * @return the merchIndexingConfig
	 */
	public MerchIndexingConfig getMerchIndexingConfig(final SessionContext ctx)
	{
		return (MerchIndexingConfig)getProperty( ctx, MERCHINDEXINGCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchImageProperty.merchIndexingConfig</code> attribute.
	 * @return the merchIndexingConfig
	 */
	public MerchIndexingConfig getMerchIndexingConfig()
	{
		return getMerchIndexingConfig( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchImageProperty.merchIndexingConfig</code> attribute. 
	 * @param value the merchIndexingConfig
	 */
	public void setMerchIndexingConfig(final SessionContext ctx, final MerchIndexingConfig value)
	{
		MERCHINDEXINGCONFIGHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchImageProperty.merchIndexingConfig</code> attribute. 
	 * @param value the merchIndexingConfig
	 */
	public void setMerchIndexingConfig(final MerchIndexingConfig value)
	{
		setMerchIndexingConfig( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchImageProperty.merchIndexingConfigPOS</code> attribute.
	 * @return the merchIndexingConfigPOS
	 */
	 Integer getMerchIndexingConfigPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, MERCHINDEXINGCONFIGPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchImageProperty.merchIndexingConfigPOS</code> attribute.
	 * @return the merchIndexingConfigPOS
	 */
	 Integer getMerchIndexingConfigPOS()
	{
		return getMerchIndexingConfigPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchImageProperty.merchIndexingConfigPOS</code> attribute. 
	 * @return the merchIndexingConfigPOS
	 */
	 int getMerchIndexingConfigPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getMerchIndexingConfigPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchImageProperty.merchIndexingConfigPOS</code> attribute. 
	 * @return the merchIndexingConfigPOS
	 */
	 int getMerchIndexingConfigPOSAsPrimitive()
	{
		return getMerchIndexingConfigPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchImageProperty.merchIndexingConfigPOS</code> attribute. 
	 * @param value the merchIndexingConfigPOS
	 */
	 void setMerchIndexingConfigPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, MERCHINDEXINGCONFIGPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchImageProperty.merchIndexingConfigPOS</code> attribute. 
	 * @param value the merchIndexingConfigPOS
	 */
	 void setMerchIndexingConfigPOS(final Integer value)
	{
		setMerchIndexingConfigPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchImageProperty.merchIndexingConfigPOS</code> attribute. 
	 * @param value the merchIndexingConfigPOS
	 */
	 void setMerchIndexingConfigPOS(final SessionContext ctx, final int value)
	{
		setMerchIndexingConfigPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchImageProperty.merchIndexingConfigPOS</code> attribute. 
	 * @param value the merchIndexingConfigPOS
	 */
	 void setMerchIndexingConfigPOS(final int value)
	{
		setMerchIndexingConfigPOS( getSession().getSessionContext(), value );
	}
	
}
