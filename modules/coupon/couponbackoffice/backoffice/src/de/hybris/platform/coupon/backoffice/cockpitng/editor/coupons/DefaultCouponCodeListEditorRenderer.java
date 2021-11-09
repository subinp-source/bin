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

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.trim;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Filedownload;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout;
import com.hybris.cockpitng.editor.defaultmultireferenceeditor.DefaultMultiReferenceEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.pageable.PageableList;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.servicelayer.media.MediaService;


/**
 * Default coupon code list editor.
 */
public class DefaultCouponCodeListEditorRenderer<T> extends DefaultMultiReferenceEditor<T>
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCouponCodeListEditorRenderer.class);

	@Resource
	private MediaService mediaService;
	private EditorContext editorContext;

	@Override
	public ReferenceEditorLayout<T> createReferenceLayout(final EditorContext context)
	{
		this.editorContext = context;
		final DefaultCouponCodeRendererLayout ret = new DefaultCouponCodeRendererLayout(this,
				loadBaseConfiguration(getTypeCode(), (WidgetInstanceManager) context.getParameter(Editor.WIDGET_INSTANCE_MANAGER)),
				mediaService);
		ret.setPlaceholderKey(getPlaceholderKey());
		return ret;
	}

	@Override
	public void updateReferencesListBoxModel(final String searchText)
	{
		final Object parentObject = editorContext.getParameter(PARENT_OBJECT);
		if (hasListEntries(parentObject))
		{
			final MultiCodeCouponModel couponModel = (MultiCodeCouponModel) parentObject;
			List<MediaModel> generatedCodes = new ArrayList(couponModel.getGeneratedCodes());
			if (isNotBlank(searchText))
			{
				generatedCodes = generatedCodes.stream()
						.filter(m -> trim(m.getCode()).toLowerCase().contains(searchText.toLowerCase())).collect(toList());
			}
			pageable = new PageableList(generatedCodes, pageSize, getTypeCode());
		}
	}

	/**
	 * The addSelectedObject method is overridden in order to prevent addition of generated coupon code media object from
	 * the multi reference editor drop down to the list of already generated coupon media. Instead download action has
	 * been added to the item in the editor drop down.
	 */
	@Override
	public void addSelectedObject(final T obj)
	{
		if (obj instanceof MediaModel)
		{
			final MediaModel mediaData = (MediaModel) obj;
			Filedownload.save(mediaService.getDataFromMedia(mediaData), mediaData.getMime(), mediaData.getRealFileName());
		}
	}

	protected boolean hasListEntries(final Object coupon)
	{
		return nonNull(coupon) && coupon instanceof MultiCodeCouponModel
				&& isNotEmpty(((MultiCodeCouponModel) coupon).getGeneratedCodes());
	}

	@Override
	protected Base loadBaseConfiguration(final String typeCode, final WidgetInstanceManager wim)
	{
		Base config = null;

		final DefaultConfigContext configContext = new DefaultConfigContext("base");
		configContext.setType(typeCode);

		try
		{
			config = wim.loadConfiguration(configContext, Base.class);
			if (config == null)
			{
				LOG.warn("Loaded UI configuration is null. Ignoring.");
			}
		}
		catch (final CockpitConfigurationNotFoundException ccnfe)
		{
			LOG.debug("Could not find UI configuration for given context (" + configContext + ").", ccnfe);
		}
		catch (final CockpitConfigurationException cce)
		{
			LOG.error("Could not load cockpit config for the given context '" + configContext + "'.", cce);
		}

		return config;
	}
}
