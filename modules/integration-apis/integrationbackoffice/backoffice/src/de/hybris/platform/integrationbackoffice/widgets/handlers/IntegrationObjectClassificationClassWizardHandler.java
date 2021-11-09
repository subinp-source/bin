/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.handlers;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.SelectedClassificationAttributesData;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.backoffice.attributechooser.AttributeChooserForm;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;

/**
 * Classification chooser handler. The handler gets selected attributes, finds leaves and sends the leaves to selectedClassificationAttributes output.
 */
public class IntegrationObjectClassificationClassWizardHandler implements FlowActionHandler
{

	private static final Logger LOG = Log.getLogger(IntegrationObjectClassificationClassWizardHandler.class);

	private static final String SOCKET_OUT_SELECTED_CLASSIFICATION_ATTRIBUTES = "selectedClassificationAttributes";
	private static final String PARAM_KEY_SELECTED_ATTRIBUTES = "classificationAttributesFormModelKey";
	public static final String USE_FULL_QUALIFIER = "useFullClassificationQualifier";
	private ModelService modelService;

	@Override
	public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> map)
	{
		Set<Attribute> selectedAttribute = adapter.getWidgetInstanceManager()
		                                          .getModel()
		                                          .getValue(map.get(PARAM_KEY_SELECTED_ATTRIBUTES), AttributeChooserForm.class)
		                                          .getChosenAttributes();
		List<ClassAttributeAssignmentModel> classificationAttributes = findAttributes(selectedAttribute);
		final boolean useFullClassificationQualifier = useFullClassificationQualifier(adapter.getWidgetInstanceManager());
		adapter.getWidgetInstanceManager().sendOutput(SOCKET_OUT_SELECTED_CLASSIFICATION_ATTRIBUTES,
				new SelectedClassificationAttributesData(classificationAttributes, useFullClassificationQualifier));
		adapter.done();
	}

	private boolean useFullClassificationQualifier(final WidgetInstanceManager wim)
	{
		final Boolean useFullQualifier = wim.getModel().getValue(USE_FULL_QUALIFIER, Boolean.class);
		return BooleanUtils.isTrue(useFullQualifier);
	}

	private List<ClassAttributeAssignmentModel> findAttributes(Set<Attribute> attributes)
	{
		return findLeaves(attributes).stream().map(leaf -> loadAssignment(leaf.getQualifier())).filter(Optional::isPresent)
		                             .map(Optional::get).collect(Collectors.toList());
	}

	private Optional<ClassAttributeAssignmentModel> loadAssignment(String pk)
	{
		try
		{
			return Optional.ofNullable(getModelService().get(PK.parse(pk))).map(ClassAttributeAssignmentModel.class::cast);
		}
		catch (RuntimeException ex)
		{
			LOG.debug("Cannot load classification assignment model", ex);
			return Optional.empty();
		}
	}

	private Collection<Attribute> findLeaves(Collection<Attribute> children)
	{
		List<Attribute> leaves = new ArrayList<>();
		for (Attribute child : children)
		{
			if (!child.hasSubAttributes())
			{
				leaves.add(child);
			}
			else
			{
				leaves.addAll(findLeaves(child.getSubAttributes()));
			}
		}
		return leaves;
	}

	private ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}
}