/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.utility;

import com.hybris.backoffice.renderer.attributeschooser.ClassificationAttributesExportRenderer;

import de.hybris.platform.core.model.ItemModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hybris.platform.integrationbackoffice.widgets.handlers.IntegrationObjectClassificationClassWizardHandler;

import org.apache.commons.lang3.BooleanUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;

import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.backoffice.attributechooser.AttributeChooserForm;
import com.hybris.backoffice.attributechooser.AttributeChooserRenderer;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;

/**
 * This renderer generally works in the same way as {@link ClassificationAttributesExportRenderer} but it removes
 * localized leaves if {@link #PARAM_EXCLUDE_LOCALIZED_NODES} is set to true.
 *
 * @deprecated will be replaced in 20.05
 */
@Deprecated(forRemoval = true)
public class ClassificationAttributesWithoutLocalizationRenderer extends ClassificationAttributesExportRenderer
{
	public static final String PARAM_EXCLUDE_LOCALIZED_NODES = "excludeLocalizedNodes";
	private static final String LABEL_USE_FULL_QUALIFIER = "integrationbackoffice.wizard.classification.usefullqualifier.label";
	public static final String SCSS_YW_USE_FULL_QUALIFIER_CONTAINER = "yw-use-full-qualifier-container";
	public static final String SCSS_YW_USE_FULL_QUALIFIER_CHECKBOX = "ye-switch-checkbox";

	@Override
	public void render(final Component parent, final ViewType customView, final Map<String, String> params,
	                   final DataType dataType, final WidgetInstanceManager wim)
	{
		renderUseFullClassificationQualifierCheckbox(parent, wim);
		parent.addForward(AttributeChooserRenderer.EVENT_ATTRIBUTES_SELECTED, parent, Editor.ON_VALUE_CHANGED);
		final AttributeChooserForm attributesForm = getAttributesForm(wim, params);
		if (!attributesForm.hasPopulatedAttributes())
		{
			final List<ItemModel> items = getItems(wim, params);
			populateAttributesChooserForm(attributesForm, items, getRetrieveMode(params));
			if (BooleanUtils.toBoolean(params.get(PARAM_EXCLUDE_LOCALIZED_NODES)))
			{
				removeLocalizedLeaves(getAttributesForm(wim, params));
			}
		}
		getAttributesChooserRenderer().render(parent, createAttributesChooserConfig(wim, params), attributesForm, dataType, wim);
	}

	protected void removeLocalizedLeaves(final AttributeChooserForm attributesForm)
	{
		Set<Attribute> catalogVersionsNodes = attributesForm.getAvailableAttributes();
		for (Attribute catalogVersionNode : catalogVersionsNodes)
		{
			for (Attribute category : catalogVersionNode.getSubAttributes())
			{
				for (Attribute classificationAttribute : category.getSubAttributes())
				{
					classificationAttribute.setSubAttributes(null);
				}
			}
		}
	}

	protected void renderUseFullClassificationQualifierCheckbox(final Component parent, final WidgetInstanceManager wim)
	{
		final Div useFullQualifierContainer = new Div();
		useFullQualifierContainer.setSclass(SCSS_YW_USE_FULL_QUALIFIER_CONTAINER);
		parent.appendChild(useFullQualifierContainer);
		final Checkbox fullQualifier = new Checkbox();
		fullQualifier.setSclass(SCSS_YW_USE_FULL_QUALIFIER_CHECKBOX);
		useFullQualifierContainer.appendChild(fullQualifier);
		fullQualifier.setLabel(Labels.getLabel(LABEL_USE_FULL_QUALIFIER));
		fullQualifier.addEventListener(Events.ON_CHECK, (CheckEvent event) -> wim.getModel()
		                                                                         .setValue(
				                                                                         IntegrationObjectClassificationClassWizardHandler.USE_FULL_QUALIFIER,
				                                                                         event.isChecked()));
	}
}