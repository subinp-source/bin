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
package de.hybris.platform.ruleenginebackoffice.search;

import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService;
import com.hybris.backoffice.widgets.advancedsearch.impl.renderer.AdvancedSearchRenderer;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.CollectionDataType;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Event;

import java.util.function.Consumer;
import java.util.function.Predicate;


/**
 * Customization of {@link AdvancedSearchRenderer} that transforms provided {@link DataAttribute} into a Collection attribute
 * in case attribute is of enumeration type
 */
public class RuleEngineAdvancedSearchRenderer extends AdvancedSearchRenderer
{
	private Predicate<DataAttribute> isApplicableCondition;

	/**
	 * {@inheritDoc}
	 */
	public RuleEngineAdvancedSearchRenderer(final TypeFacade typeFacade, final LabelService labelService,
			final AdvancedSearchOperatorService advancedSearchOperatorService, final PermissionFacade permissionFacade,
			final CockpitLocaleService cockpitLocaleService)
	{
		super(typeFacade, labelService, advancedSearchOperatorService, permissionFacade, cockpitLocaleService);
	}

	/**
	 * {@inheritDoc}
	 */
	public RuleEngineAdvancedSearchRenderer(final TypeFacade typeFacade, final LabelService labelService,
			final WidgetInstanceManager widgetInstanceManager, final AdvancedSearchOperatorService advancedSearchOperatorService,
			final PermissionFacade permissionFacade, final CockpitLocaleService cockpitLocaleService)
	{
		super(typeFacade, labelService, widgetInstanceManager, advancedSearchOperatorService, permissionFacade,
				cockpitLocaleService);
	}

	/**
	 * {@inheritDoc}
	 */
	public RuleEngineAdvancedSearchRenderer(final TypeFacade typeFacade, final LabelService labelService,
			final WidgetInstanceManager widgetInstanceManager, final AdvancedSearchOperatorService advancedSearchOperatorService,
			final PermissionFacade permissionFacade, final CockpitLocaleService cockpitLocaleService,
			final Consumer<Event> editorsEventConsumer)
	{
		super(typeFacade, labelService, widgetInstanceManager, advancedSearchOperatorService, permissionFacade,
				cockpitLocaleService, editorsEventConsumer);
	}

	@Override
	protected String resolveEditorType(final DataAttribute attribute)
	{
		if (attribute.getValueType().isEnum() && getIsApplicableCondition().test(attribute))
		{
			final DataType collectionDataType = new CollectionDataType.CollectionBuilder(attribute.getValueType().getCode(),
					DataType.Type.SET).valueType(attribute.getValueType()).build();

			final DataAttribute collectionTypeAttribute = new DataAttribute.Builder(attribute.getQualifier())
					.valueType(collectionDataType).build();

			return EditorUtils.getEditorType(collectionTypeAttribute, true);
		}

		return super.resolveEditorType(attribute);
	}

	protected Predicate<DataAttribute> getIsApplicableCondition()
	{
		return isApplicableCondition;
	}

	@Required
	public void setIsApplicableCondition(final Predicate<DataAttribute> isApplicableCondition)
	{
		this.isApplicableCondition = isApplicableCondition;
	}
}
