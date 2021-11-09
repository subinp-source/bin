/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.builders;

import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import java.util.List;
import java.util.Map;

public class IntegrationObjectAuditReportBuilder extends AbstractAuditReportBuilder
{

	@Override
	public void traversePayload(Map map)
	{
		final String RETURN_INTEGRATION_OBJECT_ITEM = "returnIntegrationObjectItem";
		final String COMPOSED_TYPE = "ComposedType_";
		final String COMPOSED_TYPE_OF_ITEM = "ComposedTypeOfItem";
		if (map.containsKey(RETURN_INTEGRATION_OBJECT_ITEM))
		{
			Object itemObject = map.get(RETURN_INTEGRATION_OBJECT_ITEM);
			String type = "";
			if (itemObject instanceof Map)
			{
				if (((Map) itemObject).containsKey(COMPOSED_TYPE_OF_ITEM)
						&& ((Map) itemObject).get(COMPOSED_TYPE_OF_ITEM) instanceof Map
						&& ((Map) ((Map) itemObject).get(COMPOSED_TYPE_OF_ITEM)).containsKey(COMPOSED_TYPE)
						&& ((Map) ((Map) itemObject).get(COMPOSED_TYPE_OF_ITEM)).get(COMPOSED_TYPE) instanceof String)
				{
					type = (String) ((Map) ((Map) itemObject).get(COMPOSED_TYPE_OF_ITEM)).get(COMPOSED_TYPE);
					map.put(COMPOSED_TYPE, type);
				}
				try
				{
					map.remove("returnIntegrationObjectItem");
				}
				catch (ClassCastException classCaseException)
				{
					LOG.warn("The IO audit config file or payload generation implementation has been changed.");
				}
				catch (Exception exception)
				{
					LOG.warn("The returnIntegrationObjectItem composedType extraction failed.");
				}
			}
			return;
		}

		map.entrySet().forEach(object -> {
			Map.Entry entry = (Map.Entry) object;
			Object value = entry.getValue();
			if (value instanceof Map)
			{
				traversePayload((Map) value);
			}
			else if (value instanceof List)
			{
				for (Object listElement : (List) value)
				{
					if (listElement instanceof Map)
					{
						traversePayload((Map) listElement);
					}
				}
			}
		});
	}

	@Override
	public String getDownloadFileName()
	{
		return ((IntegrationObjectModel) this.getSelectedModel()).getCode();
	}
}
