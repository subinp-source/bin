/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.coupon.backoffice.cockpitng.editor.coupons;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.editor.commonreferenceeditor.AbstractReferenceEditor;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;


/**
 * Default layout for items of coupon code list.
 */
public class DefaultCouponCodeRendererLayout<T, K> extends ReferenceEditorLayout<T>
{
	/**
	 * name of the css class for download coupon codes button
	 */
	protected static final String CSS_DOWNLOAD_COUPON_CODES = "ye-default-download-coupon-codes";
	/**
	 * name of the css class for label of coupon code list item
	 */
	protected static final String CSS_REFERENCE_EDITOR_SELECTED_ITEM_LABEL = "ye-default-reference-editor-selected-item-label";
	/**
	 * name of the css class for list-item content-container of coupon code list item
	 */
	protected static final String CSS_REFERENCE_EDITOR_SELECTED_ITEM_CONTAINER = "ye-default-reference-editor-selected-item-container";

	private final MediaService mediaService;

	public DefaultCouponCodeRendererLayout(final AbstractReferenceEditor<T, K> referenceEditorInterface, final Base configuration,
			final MediaService mediaService)
	{
		super(referenceEditorInterface, configuration);
		this.mediaService = mediaService;
	}

	@Override
	protected ListitemRenderer<T> createSelectedItemsListItemRenderer()
	{
		return new MediaListItemRenderer();
	}

	protected class MediaListItemRenderer implements ListitemRenderer<T>
	{
		@Override
		public void render ( final Listitem item, final T data, final int index)
		{
			final StringBuilder stringRepresentationOfObject = new StringBuilder();
			if (data instanceof MediaModel)
			{
				final MediaModel mediaData = (MediaModel) data;

				stringRepresentationOfObject.append(mediaData.getCode());
				final Label label = new Label(stringRepresentationOfObject.toString());
				label.setSclass(CSS_REFERENCE_EDITOR_SELECTED_ITEM_LABEL);
				label.setMultiline(true);

				final Div imageWrapper = new Div();
				imageWrapper.setSclass(CSS_DOWNLOAD_COUPON_CODES);
				imageWrapper.addEventListener(Events.ON_CLICK, e -> Filedownload.save(mediaService.getDataFromMedia(mediaData),
						mediaData.getMime(), mediaData.getRealFileName()));

				final Div layout = new Div();
				final Listcell cell = new Listcell();
				layout.setSclass(CSS_REFERENCE_EDITOR_SELECTED_ITEM_CONTAINER);
				layout.appendChild(label);
				layout.appendChild(imageWrapper);

				cell.appendChild(layout);
				cell.setParent(item);
			}
		}
	}
}
