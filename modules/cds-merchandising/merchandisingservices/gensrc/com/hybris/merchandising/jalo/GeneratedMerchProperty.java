/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.jalo;

import com.hybris.merchandising.constants.MerchandisingservicesConstants;
import com.hybris.merchandising.jalo.AbstractMerchProperty;
import com.hybris.merchandising.jalo.MerchIndexingConfig;
import com.hybris.merchandising.jalo.MerchProductDirectoryConfig;
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
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem MerchProperty}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedMerchProperty extends AbstractMerchProperty
{
	/** Qualifier of the <code>MerchProperty.merchProductDirectoryConfigPOS</code> attribute **/
	public static final String MERCHPRODUCTDIRECTORYCONFIGPOS = "merchProductDirectoryConfigPOS";
	/** Qualifier of the <code>MerchProperty.merchProductDirectoryConfig</code> attribute **/
	public static final String MERCHPRODUCTDIRECTORYCONFIG = "merchProductDirectoryConfig";
	/** Qualifier of the <code>MerchProperty.merchIndexingConfigPOS</code> attribute **/
	public static final String MERCHINDEXINGCONFIGPOS = "merchIndexingConfigPOS";
	/** Qualifier of the <code>MerchProperty.merchIndexingConfig</code> attribute **/
	public static final String MERCHINDEXINGCONFIG = "merchIndexingConfig";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n MERCHPRODUCTDIRECTORYCONFIG's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedMerchProperty> MERCHPRODUCTDIRECTORYCONFIGHANDLER = new BidirectionalOneToManyHandler<GeneratedMerchProperty>(
	MerchandisingservicesConstants.TC.MERCHPROPERTY,
	false,
	"merchProductDirectoryConfig",
	"merchProductDirectoryConfigPOS",
	true,
	true,
	CollectionType.LIST
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n MERCHINDEXINGCONFIG's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedMerchProperty> MERCHINDEXINGCONFIGHANDLER = new BidirectionalOneToManyHandler<GeneratedMerchProperty>(
	MerchandisingservicesConstants.TC.MERCHPROPERTY,
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
		tmp.put(MERCHPRODUCTDIRECTORYCONFIGPOS, AttributeMode.INITIAL);
		tmp.put(MERCHPRODUCTDIRECTORYCONFIG, AttributeMode.INITIAL);
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
		MERCHPRODUCTDIRECTORYCONFIGHANDLER.newInstance(ctx, allAttributes);
		MERCHINDEXINGCONFIGHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchIndexingConfig</code> attribute.
	 * @return the merchIndexingConfig
	 */
	public MerchIndexingConfig getMerchIndexingConfig(final SessionContext ctx)
	{
		return (MerchIndexingConfig)getProperty( ctx, MERCHINDEXINGCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchIndexingConfig</code> attribute.
	 * @return the merchIndexingConfig
	 */
	public MerchIndexingConfig getMerchIndexingConfig()
	{
		return getMerchIndexingConfig( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchIndexingConfig</code> attribute. 
	 * @param value the merchIndexingConfig
	 */
	public void setMerchIndexingConfig(final SessionContext ctx, final MerchIndexingConfig value)
	{
		MERCHINDEXINGCONFIGHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchIndexingConfig</code> attribute. 
	 * @param value the merchIndexingConfig
	 */
	public void setMerchIndexingConfig(final MerchIndexingConfig value)
	{
		setMerchIndexingConfig( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchIndexingConfigPOS</code> attribute.
	 * @return the merchIndexingConfigPOS
	 */
	 Integer getMerchIndexingConfigPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, MERCHINDEXINGCONFIGPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchIndexingConfigPOS</code> attribute.
	 * @return the merchIndexingConfigPOS
	 */
	 Integer getMerchIndexingConfigPOS()
	{
		return getMerchIndexingConfigPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchIndexingConfigPOS</code> attribute. 
	 * @return the merchIndexingConfigPOS
	 */
	 int getMerchIndexingConfigPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getMerchIndexingConfigPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchIndexingConfigPOS</code> attribute. 
	 * @return the merchIndexingConfigPOS
	 */
	 int getMerchIndexingConfigPOSAsPrimitive()
	{
		return getMerchIndexingConfigPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchIndexingConfigPOS</code> attribute. 
	 * @param value the merchIndexingConfigPOS
	 */
	 void setMerchIndexingConfigPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, MERCHINDEXINGCONFIGPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchIndexingConfigPOS</code> attribute. 
	 * @param value the merchIndexingConfigPOS
	 */
	 void setMerchIndexingConfigPOS(final Integer value)
	{
		setMerchIndexingConfigPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchIndexingConfigPOS</code> attribute. 
	 * @param value the merchIndexingConfigPOS
	 */
	 void setMerchIndexingConfigPOS(final SessionContext ctx, final int value)
	{
		setMerchIndexingConfigPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchIndexingConfigPOS</code> attribute. 
	 * @param value the merchIndexingConfigPOS
	 */
	 void setMerchIndexingConfigPOS(final int value)
	{
		setMerchIndexingConfigPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchProductDirectoryConfig</code> attribute.
	 * @return the merchProductDirectoryConfig
	 */
	public MerchProductDirectoryConfig getMerchProductDirectoryConfig(final SessionContext ctx)
	{
		return (MerchProductDirectoryConfig)getProperty( ctx, MERCHPRODUCTDIRECTORYCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchProductDirectoryConfig</code> attribute.
	 * @return the merchProductDirectoryConfig
	 */
	public MerchProductDirectoryConfig getMerchProductDirectoryConfig()
	{
		return getMerchProductDirectoryConfig( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchProductDirectoryConfig</code> attribute. 
	 * @param value the merchProductDirectoryConfig
	 */
	public void setMerchProductDirectoryConfig(final SessionContext ctx, final MerchProductDirectoryConfig value)
	{
		MERCHPRODUCTDIRECTORYCONFIGHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchProductDirectoryConfig</code> attribute. 
	 * @param value the merchProductDirectoryConfig
	 */
	public void setMerchProductDirectoryConfig(final MerchProductDirectoryConfig value)
	{
		setMerchProductDirectoryConfig( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchProductDirectoryConfigPOS</code> attribute.
	 * @return the merchProductDirectoryConfigPOS
	 */
	 Integer getMerchProductDirectoryConfigPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, MERCHPRODUCTDIRECTORYCONFIGPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchProductDirectoryConfigPOS</code> attribute.
	 * @return the merchProductDirectoryConfigPOS
	 */
	 Integer getMerchProductDirectoryConfigPOS()
	{
		return getMerchProductDirectoryConfigPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchProductDirectoryConfigPOS</code> attribute. 
	 * @return the merchProductDirectoryConfigPOS
	 */
	 int getMerchProductDirectoryConfigPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getMerchProductDirectoryConfigPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProperty.merchProductDirectoryConfigPOS</code> attribute. 
	 * @return the merchProductDirectoryConfigPOS
	 */
	 int getMerchProductDirectoryConfigPOSAsPrimitive()
	{
		return getMerchProductDirectoryConfigPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchProductDirectoryConfigPOS</code> attribute. 
	 * @param value the merchProductDirectoryConfigPOS
	 */
	 void setMerchProductDirectoryConfigPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, MERCHPRODUCTDIRECTORYCONFIGPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchProductDirectoryConfigPOS</code> attribute. 
	 * @param value the merchProductDirectoryConfigPOS
	 */
	 void setMerchProductDirectoryConfigPOS(final Integer value)
	{
		setMerchProductDirectoryConfigPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchProductDirectoryConfigPOS</code> attribute. 
	 * @param value the merchProductDirectoryConfigPOS
	 */
	 void setMerchProductDirectoryConfigPOS(final SessionContext ctx, final int value)
	{
		setMerchProductDirectoryConfigPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProperty.merchProductDirectoryConfigPOS</code> attribute. 
	 * @param value the merchProductDirectoryConfigPOS
	 */
	 void setMerchProductDirectoryConfigPOS(final int value)
	{
		setMerchProductDirectoryConfigPOS( getSession().getSessionContext(), value );
	}
	
}
