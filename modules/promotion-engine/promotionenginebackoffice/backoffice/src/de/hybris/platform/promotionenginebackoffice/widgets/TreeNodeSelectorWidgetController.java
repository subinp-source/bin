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
package de.hybris.platform.promotionenginebackoffice.widgets;

import static java.util.Objects.nonNull;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.hybris.backoffice.navigation.TreeNodeSelector;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.util.DefaultWidgetController;


/**
 * The class is responsible for selecting specified Tree Node at Explorer Tree according to the role->node mappings.
 *
 */
public class TreeNodeSelectorWidgetController extends DefaultWidgetController
{
	private static final String SOCKET_IN_GENERIC_INPUT = "genericInput";
	private static final String TREE_NODE_SELECTOR_OUTPUT = "treeNodeSelector";
	private static final String ROLE_TO_NODE_MAPPING = "roleToNodeMapping";
	private static final String KEY_VALUE_SEPARATOR = "keyValueSeparator";
	private static final String VALUES_SEPARATOR = "valuesSeparator";
	private static final String DEFAULT_MAPPING_KEY = "defaultMappingKey";

	@WireVariable
	private transient CockpitUserService cockpitUserService;
	@WireVariable
	private transient AuthorityGroupService authorityGroupService;

	@SocketEvent(socketId = SOCKET_IN_GENERIC_INPUT)
	public void genericInput(@SuppressWarnings("unused") final Object data)
	{
		final String nodeToSelect = nodeToSelect(getWidgetParameter(ROLE_TO_NODE_MAPPING));
		if (nonNull(nodeToSelect))
		{
			sendOutput(TREE_NODE_SELECTOR_OUTPUT, new TreeNodeSelector(nodeToSelect, true));
		}
	}

	protected String nodeToSelect(final String mappingAsString)
	{
		final String keyValueSeparator = getObligatoryWidgetParameter(KEY_VALUE_SEPARATOR);
		final String valuesSeparator = getObligatoryWidgetParameter(VALUES_SEPARATOR);

		String nodeToSelect = null;
		if (StringUtils.isNotEmpty(mappingAsString))
		{
			if (!mappingAsString.contains(keyValueSeparator) && !mappingAsString.contains(valuesSeparator))
			{
				nodeToSelect = mappingAsString;
			}
			else if (mappingAsString.contains(keyValueSeparator))
			{
				final Map<String, String> mapping = Splitter.on(valuesSeparator).withKeyValueSeparator(keyValueSeparator)
						.split(mappingAsString);

				final String activeUserRoleCode = getActiveUserRoleCode();
				if (mapping.containsKey(activeUserRoleCode))
				{
					nodeToSelect = mapping.get(activeUserRoleCode);
				}
				else
				{
					nodeToSelect = mapping.get(getWidgetParameter(DEFAULT_MAPPING_KEY));
				}
			}
		}
		return nodeToSelect;
	}

	protected String getActiveUserRoleCode()
	{
		final String userId = cockpitUserService.getCurrentUser();
		final AuthorityGroup activeUserRole = authorityGroupService.getActiveAuthorityGroupForUser(userId);
		return nonNull(activeUserRole) ? activeUserRole.getCode() : null;
	}

	protected String getWidgetParameter(final String paramName)
	{
		return getWidgetSettings().getString(paramName);
	}

	protected String getObligatoryWidgetParameter(final String paramName)
	{
		final String parameter = getWidgetParameter(paramName);
		Preconditions.checkArgument(StringUtils.isNotEmpty(parameter),
				String.format("'%s' widget setting is obligatory", paramName));
		return parameter;
	}
}
