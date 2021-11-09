/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.addon.jalo;

import com.hybris.merchandising.constants.MerchandisingaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.hybris.merchandising.addon.jalo.MerchandisingCarouselComponent MerchandisingCarouselComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedMerchandisingCarouselComponent extends SimpleCMSComponent
{
	/** Qualifier of the <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute **/
	public static final String NUMBERTODISPLAY = "numberToDisplay";
	/** Qualifier of the <code>MerchandisingCarouselComponent.strategy</code> attribute **/
	public static final String STRATEGY = "strategy";
	/** Qualifier of the <code>MerchandisingCarouselComponent.title</code> attribute **/
	public static final String TITLE = "title";
	/** Qualifier of the <code>MerchandisingCarouselComponent.backgroundColour</code> attribute **/
	public static final String BACKGROUNDCOLOUR = "backgroundColour";
	/** Qualifier of the <code>MerchandisingCarouselComponent.textColour</code> attribute **/
	public static final String TEXTCOLOUR = "textColour";
	/** Qualifier of the <code>MerchandisingCarouselComponent.scroll</code> attribute **/
	public static final String SCROLL = "scroll";
	/** Qualifier of the <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute **/
	public static final String VIEWPORTPERCENTAGE = "viewportPercentage";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(NUMBERTODISPLAY, AttributeMode.INITIAL);
		tmp.put(STRATEGY, AttributeMode.INITIAL);
		tmp.put(TITLE, AttributeMode.INITIAL);
		tmp.put(BACKGROUNDCOLOUR, AttributeMode.INITIAL);
		tmp.put(TEXTCOLOUR, AttributeMode.INITIAL);
		tmp.put(SCROLL, AttributeMode.INITIAL);
		tmp.put(VIEWPORTPERCENTAGE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.backgroundColour</code> attribute.
	 * @return the backgroundColour - Carousel background colour.
	 */
	public String getBackgroundColour(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BACKGROUNDCOLOUR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.backgroundColour</code> attribute.
	 * @return the backgroundColour - Carousel background colour.
	 */
	public String getBackgroundColour()
	{
		return getBackgroundColour( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.backgroundColour</code> attribute. 
	 * @param value the backgroundColour - Carousel background colour.
	 */
	public void setBackgroundColour(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BACKGROUNDCOLOUR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.backgroundColour</code> attribute. 
	 * @param value the backgroundColour - Carousel background colour.
	 */
	public void setBackgroundColour(final String value)
	{
		setBackgroundColour( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute.
	 * @return the numberToDisplay - How many items to show in the carousel.
	 */
	public Integer getNumberToDisplay(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, NUMBERTODISPLAY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute.
	 * @return the numberToDisplay - How many items to show in the carousel.
	 */
	public Integer getNumberToDisplay()
	{
		return getNumberToDisplay( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute. 
	 * @return the numberToDisplay - How many items to show in the carousel.
	 */
	public int getNumberToDisplayAsPrimitive(final SessionContext ctx)
	{
		Integer value = getNumberToDisplay( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute. 
	 * @return the numberToDisplay - How many items to show in the carousel.
	 */
	public int getNumberToDisplayAsPrimitive()
	{
		return getNumberToDisplayAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute. 
	 * @param value the numberToDisplay - How many items to show in the carousel.
	 */
	public void setNumberToDisplay(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, NUMBERTODISPLAY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute. 
	 * @param value the numberToDisplay - How many items to show in the carousel.
	 */
	public void setNumberToDisplay(final Integer value)
	{
		setNumberToDisplay( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute. 
	 * @param value the numberToDisplay - How many items to show in the carousel.
	 */
	public void setNumberToDisplay(final SessionContext ctx, final int value)
	{
		setNumberToDisplay( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute. 
	 * @param value the numberToDisplay - How many items to show in the carousel.
	 */
	public void setNumberToDisplay(final int value)
	{
		setNumberToDisplay( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.scroll</code> attribute.
	 * @return the scroll
	 */
	public EnumerationValue getScroll(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, SCROLL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.scroll</code> attribute.
	 * @return the scroll
	 */
	public EnumerationValue getScroll()
	{
		return getScroll( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.scroll</code> attribute. 
	 * @param value the scroll
	 */
	public void setScroll(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, SCROLL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.scroll</code> attribute. 
	 * @param value the scroll
	 */
	public void setScroll(final EnumerationValue value)
	{
		setScroll( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.strategy</code> attribute.
	 * @return the strategy - Strategy to use for carousel.
	 */
	public String getStrategy(final SessionContext ctx)
	{
		return (String)getProperty( ctx, STRATEGY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.strategy</code> attribute.
	 * @return the strategy - Strategy to use for carousel.
	 */
	public String getStrategy()
	{
		return getStrategy( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.strategy</code> attribute. 
	 * @param value the strategy - Strategy to use for carousel.
	 */
	public void setStrategy(final SessionContext ctx, final String value)
	{
		setProperty(ctx, STRATEGY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.strategy</code> attribute. 
	 * @param value the strategy - Strategy to use for carousel.
	 */
	public void setStrategy(final String value)
	{
		setStrategy( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.textColour</code> attribute.
	 * @return the textColour - Carousel text colour.
	 */
	public String getTextColour(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TEXTCOLOUR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.textColour</code> attribute.
	 * @return the textColour - Carousel text colour.
	 */
	public String getTextColour()
	{
		return getTextColour( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.textColour</code> attribute. 
	 * @param value the textColour - Carousel text colour.
	 */
	public void setTextColour(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TEXTCOLOUR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.textColour</code> attribute. 
	 * @param value the textColour - Carousel text colour.
	 */
	public void setTextColour(final String value)
	{
		setTextColour( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.title</code> attribute.
	 * @return the title - Carousel title.
	 */
	public String getTitle(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedMerchandisingCarouselComponent.getTitle requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.title</code> attribute.
	 * @return the title - Carousel title.
	 */
	public String getTitle()
	{
		return getTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.title</code> attribute. 
	 * @return the localized title - Carousel title.
	 */
	public Map<Language,String> getAllTitle(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,TITLE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.title</code> attribute. 
	 * @return the localized title - Carousel title.
	 */
	public Map<Language,String> getAllTitle()
	{
		return getAllTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.title</code> attribute. 
	 * @param value the title - Carousel title.
	 */
	public void setTitle(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedMerchandisingCarouselComponent.setTitle requires a session language", 0 );
		}
		setLocalizedProperty(ctx, TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.title</code> attribute. 
	 * @param value the title - Carousel title.
	 */
	public void setTitle(final String value)
	{
		setTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.title</code> attribute. 
	 * @param value the title - Carousel title.
	 */
	public void setAllTitle(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.title</code> attribute. 
	 * @param value the title - Carousel title.
	 */
	public void setAllTitle(final Map<Language,String> value)
	{
		setAllTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute.
	 * @return the viewportPercentage - Viewport percentage to trigger carousel view event.
	 */
	public Integer getViewportPercentage(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, VIEWPORTPERCENTAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute.
	 * @return the viewportPercentage - Viewport percentage to trigger carousel view event.
	 */
	public Integer getViewportPercentage()
	{
		return getViewportPercentage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute. 
	 * @return the viewportPercentage - Viewport percentage to trigger carousel view event.
	 */
	public int getViewportPercentageAsPrimitive(final SessionContext ctx)
	{
		Integer value = getViewportPercentage( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute. 
	 * @return the viewportPercentage - Viewport percentage to trigger carousel view event.
	 */
	public int getViewportPercentageAsPrimitive()
	{
		return getViewportPercentageAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute. 
	 * @param value the viewportPercentage - Viewport percentage to trigger carousel view event.
	 */
	public void setViewportPercentage(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, VIEWPORTPERCENTAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute. 
	 * @param value the viewportPercentage - Viewport percentage to trigger carousel view event.
	 */
	public void setViewportPercentage(final Integer value)
	{
		setViewportPercentage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute. 
	 * @param value the viewportPercentage - Viewport percentage to trigger carousel view event.
	 */
	public void setViewportPercentage(final SessionContext ctx, final int value)
	{
		setViewportPercentage( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute. 
	 * @param value the viewportPercentage - Viewport percentage to trigger carousel view event.
	 */
	public void setViewportPercentage(final int value)
	{
		setViewportPercentage( getSession().getSessionContext(), value );
	}
	
}
