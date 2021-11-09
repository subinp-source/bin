/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditaddon.service;

import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_PARTIAL_CLASS;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_PAGE_UID_PARTIAL_CLASS;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_PAGE_UUID_PARTIAL_CLASS;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailTemplateService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.smarteditaddon.cms.services.impl.CMSSmartEditDynamicAttributeService;
import de.hybris.platform.acceleratorservices.email.data.EmailPageData;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Required;


/**
 * The DefaultSmartEditEmailTemplateService is used to render email pages based on their velocity templates for
 * pages and components.
 */
public class SmartEditEmailTemplateService extends DefaultEmailTemplateService
{

	private final static Logger LOGGER = Logger.getLogger(SmartEditEmailTemplateService.class.getName());

	private static final String JS_TEMPLATE = "<script type=\"text/javascript\" src=\"%s\"></script>";
	private static final String CSS_TEMPLATE = "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"%s\"/>";
	private static final String PRE_CONTENT_SLOT_MARK = "${ctx.cmsSlotContents.";
	private static final String POST_CONTENT_SLOT_MARK = "}";

	private CMSPageService cmsPageService;
	private CMSSmartEditDynamicAttributeService cmsDynamicAttributeService;
	private SmartEditEmailComponentRenderingService smartEditEmailComponentRenderingService;
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Override
	public String getPageTemplate(final EmailPageData emailPageData) throws CMSItemNotFoundException
	{
		final EmailPageModel pageForRequest = (EmailPageModel) getCmsPageService().getPageForId(emailPageData.getPageUid());
		final String baseTemplateRendering = super.getPageTemplate(emailPageData);
		return addSmartEditTags(baseTemplateRendering, pageForRequest, emailPageData);
	}

	protected String addSmartEditTags(final String emailHtml, final EmailPageModel cmsPage, final EmailPageData emailPageData)
	{
		final Document doc = Jsoup.parse(emailHtml);
		final Collection<ContentSlotData> slots = getCmsPageService().getContentSlotsForPage(cmsPage);

		addSmartEditPageBodyAttributes(doc, cmsPage);
		wrapContentSlots(doc, slots);
		addJavascripts(doc, emailPageData);
		addCssLinks(doc, emailPageData);

		return doc.toString();
	}

	/**
	 * Add page UID, page UUID, and catalog version css classes to the <body> element of the page
	 * @param doc The jsoup document
	 * @param page The email page to be rendered
	 */
	protected void addSmartEditPageBodyAttributes(final Document doc, final EmailPageModel page)
	{
		final ItemData pageData = getUniqueItemIdentifierService().getItemData(page).get();
		final ItemData catalogVersionData = getUniqueItemIdentifierService().getItemData(page.getCatalogVersion()).get();
		final Element body = doc.body();

		body.addClass(SMARTEDIT_CONTRACT_PAGE_UID_PARTIAL_CLASS + page.getUid());
		body.addClass(SMARTEDIT_CONTRACT_PAGE_UUID_PARTIAL_CLASS + pageData.getItemId());
		body.addClass(SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_PARTIAL_CLASS + catalogVersionData.getItemId());
	}

	protected void wrapContentSlots(final Document doc, final Collection<ContentSlotData> slots)
	{
		// find all elements with cms content slot marks
		final Elements elements = doc.select(":containsOwn(" + PRE_CONTENT_SLOT_MARK + ")");

		elements.forEach((element) ->
		{
			// get element value (body)
			final String content = element.html();
			// extract the placeholder because body may have more elements
			final int beginIndex = content.indexOf(PRE_CONTENT_SLOT_MARK);
			final int endIndex = content.indexOf(POST_CONTENT_SLOT_MARK, beginIndex);
			final String placeholder = content.substring(beginIndex, endIndex + 1);
			// get slot name from placeholder
			final String slotName = placeholder.substring(PRE_CONTENT_SLOT_MARK.length(), placeholder.length() - 1);

			final ContentSlotData contentSlotData = findContentSlotByPosition(slotName, slots);
			if (contentSlotData == null)
			{
				throw new IllegalStateException("Unable to find slots defined in email page template");
			}

			final ContentSlotModel contentSlot = contentSlotData.getContentSlot();

			final StringBuilder components = new StringBuilder();
			// add divs to all components in this tag

			contentSlotData.getCMSComponents()
					.forEach((cmsComponentModel) ->
					{
						final String renderedComponent = getSmartEditEmailComponentRenderingService().renderComponent(cmsComponentModel);
						components.append(createSmartEditDivForComponent(cmsComponentModel, contentSlot, renderedComponent));
					});
			// wrap components with content slot tag
			final String smartEditDiv = createSmartEditDivForContentSlot(contentSlot, components.toString());

			// replace content with wrapped placeholder and put them back to html
			element.html(content.replace(placeholder, smartEditDiv));
		});
	}

	protected void addJavascripts(final Document doc, final EmailPageData emailPageData)
	{
		emailPageData.getJsPaths().forEach((jsPath) -> {
			doc.body().append(createJsScript(jsPath));
		});
	}

	protected void addCssLinks(final Document doc, final EmailPageData emailPageData)
	{
		emailPageData.getCssPaths().forEach((cssPath) -> {
			doc.head().append(createCssLink(cssPath));
		});
	}

	protected String createSmartEditDivForContentSlot(final ContentSlotModel contentSlot, final String content)
	{
		final Map<String, String> attributes = getCmsDynamicAttributeService().getDynamicContentSlotAttributes(contentSlot,
				null, null);
		return createSmartEditDiv(content, attributes);
	}

	protected String createSmartEditDiv(final String content, final Map<String, String> attributes)
	{
		final Element el = new Element(Tag.valueOf("div"), "");
		for (Map.Entry<String, String> attribute : attributes.entrySet())
		{
			el.attr(attribute.getKey(), attribute.getValue());
		}
		el.html(content);
		return el.toString();
	}

	protected String createSmartEditDivForComponent(final AbstractCMSComponentModel model,
			final ContentSlotModel contentSlot, final String divContent)
	{
		final Map<String, String> attributes = getCmsDynamicAttributeService().getDynamicComponentAttributes(model,
				contentSlot);
		return createSmartEditDiv(divContent, attributes);
	}

	protected ContentSlotData findContentSlotByPosition(final String slotName, final Collection<ContentSlotData> slots)
	{
		return slots
				.stream()
				.filter(e -> e.getPosition().equals(slotName))
				.findAny()
				.orElse(null);
	}

	protected String createJsScript(final String jsPath)
	{
		return String.format(JS_TEMPLATE, jsPath);
	}

	protected String createCssLink(final String cssPath)
	{
		return String.format(CSS_TEMPLATE, cssPath);
	}

	protected CMSPageService getCmsPageService()
	{
		return cmsPageService;
	}

	@Required
	public void setCmsPageService(final CMSPageService cmsPageService)
	{
		this.cmsPageService = cmsPageService;
	}

	protected CMSSmartEditDynamicAttributeService getCmsDynamicAttributeService()
	{
		return cmsDynamicAttributeService;
	}

	@Required
	public void setCmsDynamicAttributeService(final CMSSmartEditDynamicAttributeService cmsDynamicAttributeService)
	{
		this.cmsDynamicAttributeService = cmsDynamicAttributeService;
	}

	protected SmartEditEmailComponentRenderingService getSmartEditEmailComponentRenderingService()
	{
		return smartEditEmailComponentRenderingService;
	}

	@Required
	public void setSmartEditEmailComponentRenderingService(
			final SmartEditEmailComponentRenderingService smartEditEmailComponentRenderingService)
	{
		this.smartEditEmailComponentRenderingService = smartEditEmailComponentRenderingService;
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}
}
