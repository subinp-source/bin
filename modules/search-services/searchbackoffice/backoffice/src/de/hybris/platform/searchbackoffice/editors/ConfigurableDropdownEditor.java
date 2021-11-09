/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchbackoffice.editors;

import de.hybris.platform.searchbackoffice.common.SnDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.util.BackofficeSpringUtil;


public class ConfigurableDropdownEditor implements CockpitEditorRenderer<Object>
{
	protected static final String DATA_PROVIDER_KEY = "dataProvider";
	protected static final String DATA_PROVIDER_PARAMETER_PREFIX = "dataProviderParameter_";

	protected static final String PLACEHOLDER_KEY = "placeholder";

	protected static final Pattern EXPRESSION_PATTERN = Pattern.compile("^\\{(.*)\\}$");

	@Resource
	private PropertyValueService propertyValueService;

	@Override
	public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
	{
		final SnDataProvider<Object, Object> dataProvider = createDataProvider(context);
		final Map<String, Object> dataProviderParameters = createDataProviderParameters(context);
		final ListModel<Object> model = createModel(context, dataProvider, dataProviderParameters);

		final Combobox combobox = new Combobox();
		combobox.setReadonly(true);
		combobox.setDisabled(!context.isEditable());
		combobox.setModel(model);

		combobox.setItemRenderer((item, data, index) -> {
			item.setValue(dataProvider.getValue(data));
			item.setLabel(dataProvider.getLabel(data));
		});

		combobox.addEventListener(Events.ON_CHANGE, event -> {
			final Comboitem selectedItem = combobox.getSelectedItem();
			if (selectedItem == null && CollectionUtils.isNotEmpty(combobox.getItems()))
			{
				combobox.setSelectedIndex(0);
			}
			else if (selectedItem != null)
			{
				listener.onValueChanged(selectedItem.getValue());
			}
		});

		final String placeholder = Objects.toString(context.getParameter(PLACEHOLDER_KEY));
		if (StringUtils.isNotBlank(placeholder))
		{
			combobox.setPlaceholder(Labels.getLabel(placeholder));
		}

		parent.appendChild(combobox);
	}

	protected SnDataProvider<Object, Object> createDataProvider(final EditorContext<Object> context)
	{
		final String dataProvider = Objects.toString(context.getParameter(DATA_PROVIDER_KEY));
		return BackofficeSpringUtil.getBean(dataProvider);
	}

	protected Map<String, Object> createDataProviderParameters(final EditorContext<Object> context)
	{
		final Map<String, Object> dataProviderParameters = context.getParameters().entrySet().stream()
				.filter(this::isDataProviderParameter)
				.collect(Collectors.toMap(this::extractKey, entry -> evaluate(context, entry.getValue())));

		return dataProviderParameters;
	}

	protected boolean isDataProviderParameter(final Entry<String, Object> entry)
	{
		return StringUtils.startsWith(entry.getKey(), DATA_PROVIDER_PARAMETER_PREFIX);
	}

	protected String extractKey(final Entry<String, Object> entry)
	{
		return StringUtils.removeStart(entry.getKey(), DATA_PROVIDER_PARAMETER_PREFIX);
	}

	protected Object evaluate(final EditorContext<Object> context, final Object value)
	{
		if (value instanceof String)
		{
			final Matcher matcher = EXPRESSION_PATTERN.matcher((String) value);
			if (matcher.find())
			{
				final String expression = matcher.group(1);
				return propertyValueService.readValue(context.getParameters(), expression);
			}
		}

		return value;
	}

	protected ListModel<Object> createModel(final EditorContext<Object> context,
			final SnDataProvider<Object, Object> dataProvider, final Map<String, Object> dataProviderParameters)
	{
		final List<Object> data = new ArrayList<>();

		if (context.isOptional())
		{
			data.add(null);
		}

		data.addAll(dataProvider.getData(dataProviderParameters));

		final ListModelList<Object> model = new ListModelList<>(data);

		final Object initialValue = context.getInitialValue();
		if (initialValue != null)
		{
			final Optional<Object> selectedItem = data.stream()
					.filter(item -> Objects.equals(initialValue, dataProvider.getValue(item))).findFirst();

			if (selectedItem.isPresent())
			{
				model.setSelection(Collections.singletonList(selectedItem.get()));
			}
		}

		return model;
	}
}
