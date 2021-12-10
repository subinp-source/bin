/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.addon.model;

import com.hybris.merchandising.enums.ScrollType;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type MerchandisingCarouselComponent first defined at extension merchandisingaddon.
 */
@SuppressWarnings("all")
public class MerchandisingCarouselComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MerchandisingCarouselComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute defined at extension <code>merchandisingaddon</code>. */
	public static final String NUMBERTODISPLAY = "numberToDisplay";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchandisingCarouselComponent.strategy</code> attribute defined at extension <code>merchandisingaddon</code>. */
	public static final String STRATEGY = "strategy";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchandisingCarouselComponent.title</code> attribute defined at extension <code>merchandisingaddon</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchandisingCarouselComponent.backgroundColour</code> attribute defined at extension <code>merchandisingaddon</code>. */
	public static final String BACKGROUNDCOLOUR = "backgroundColour";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchandisingCarouselComponent.textColour</code> attribute defined at extension <code>merchandisingaddon</code>. */
	public static final String TEXTCOLOUR = "textColour";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchandisingCarouselComponent.scroll</code> attribute defined at extension <code>merchandisingaddon</code>. */
	public static final String SCROLL = "scroll";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute defined at extension <code>merchandisingaddon</code>. */
	public static final String VIEWPORTPERCENTAGE = "viewportPercentage";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MerchandisingCarouselComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MerchandisingCarouselComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _numberToDisplay initial attribute declared by type <code>MerchandisingCarouselComponent</code> at extension <code>merchandisingaddon</code>
	 * @param _strategy initial attribute declared by type <code>MerchandisingCarouselComponent</code> at extension <code>merchandisingaddon</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public MerchandisingCarouselComponentModel(final CatalogVersionModel _catalogVersion, final int _numberToDisplay, final String _strategy, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setNumberToDisplay(_numberToDisplay);
		setStrategy(_strategy);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _numberToDisplay initial attribute declared by type <code>MerchandisingCarouselComponent</code> at extension <code>merchandisingaddon</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _strategy initial attribute declared by type <code>MerchandisingCarouselComponent</code> at extension <code>merchandisingaddon</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public MerchandisingCarouselComponentModel(final CatalogVersionModel _catalogVersion, final int _numberToDisplay, final ItemModel _owner, final String _strategy, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setNumberToDisplay(_numberToDisplay);
		setOwner(_owner);
		setStrategy(_strategy);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.backgroundColour</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 * @return the backgroundColour - Carousel background colour.
	 */
	@Accessor(qualifier = "backgroundColour", type = Accessor.Type.GETTER)
	public String getBackgroundColour()
	{
		return getPersistenceContext().getPropertyValue(BACKGROUNDCOLOUR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 * @return the numberToDisplay - How many items to show in the carousel.
	 */
	@Accessor(qualifier = "numberToDisplay", type = Accessor.Type.GETTER)
	public int getNumberToDisplay()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(NUMBERTODISPLAY));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.scroll</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 * @return the scroll
	 */
	@Accessor(qualifier = "scroll", type = Accessor.Type.GETTER)
	public ScrollType getScroll()
	{
		return getPersistenceContext().getPropertyValue(SCROLL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.strategy</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 * @return the strategy - Strategy to use for carousel.
	 */
	@Accessor(qualifier = "strategy", type = Accessor.Type.GETTER)
	public String getStrategy()
	{
		return getPersistenceContext().getPropertyValue(STRATEGY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.textColour</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 * @return the textColour - Carousel text colour.
	 */
	@Accessor(qualifier = "textColour", type = Accessor.Type.GETTER)
	public String getTextColour()
	{
		return getPersistenceContext().getPropertyValue(TEXTCOLOUR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.title</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 * @return the title - Carousel title.
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getTitle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.title</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 * @param loc the value localization key 
	 * @return the title - Carousel title.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TITLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 * @return the viewportPercentage - Viewport percentage to trigger carousel view event.
	 */
	@Accessor(qualifier = "viewportPercentage", type = Accessor.Type.GETTER)
	public int getViewportPercentage()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(VIEWPORTPERCENTAGE));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchandisingCarouselComponent.backgroundColour</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 *  
	 * @param value the backgroundColour - Carousel background colour.
	 */
	@Accessor(qualifier = "backgroundColour", type = Accessor.Type.SETTER)
	public void setBackgroundColour(final String value)
	{
		getPersistenceContext().setPropertyValue(BACKGROUNDCOLOUR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchandisingCarouselComponent.numberToDisplay</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 *  
	 * @param value the numberToDisplay - How many items to show in the carousel.
	 */
	@Accessor(qualifier = "numberToDisplay", type = Accessor.Type.SETTER)
	public void setNumberToDisplay(final int value)
	{
		getPersistenceContext().setPropertyValue(NUMBERTODISPLAY, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchandisingCarouselComponent.scroll</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 *  
	 * @param value the scroll
	 */
	@Accessor(qualifier = "scroll", type = Accessor.Type.SETTER)
	public void setScroll(final ScrollType value)
	{
		getPersistenceContext().setPropertyValue(SCROLL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchandisingCarouselComponent.strategy</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 *  
	 * @param value the strategy - Strategy to use for carousel.
	 */
	@Accessor(qualifier = "strategy", type = Accessor.Type.SETTER)
	public void setStrategy(final String value)
	{
		getPersistenceContext().setPropertyValue(STRATEGY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchandisingCarouselComponent.textColour</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 *  
	 * @param value the textColour - Carousel text colour.
	 */
	@Accessor(qualifier = "textColour", type = Accessor.Type.SETTER)
	public void setTextColour(final String value)
	{
		getPersistenceContext().setPropertyValue(TEXTCOLOUR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchandisingCarouselComponent.title</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 *  
	 * @param value the title - Carousel title.
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		setTitle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>MerchandisingCarouselComponent.title</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 *  
	 * @param value the title - Carousel title.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TITLE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchandisingCarouselComponent.viewportPercentage</code> attribute defined at extension <code>merchandisingaddon</code>. 
	 *  
	 * @param value the viewportPercentage - Viewport percentage to trigger carousel view event.
	 */
	@Accessor(qualifier = "viewportPercentage", type = Accessor.Type.SETTER)
	public void setViewportPercentage(final int value)
	{
		getPersistenceContext().setPropertyValue(VIEWPORTPERCENTAGE, toObject(value));
	}
	
}
