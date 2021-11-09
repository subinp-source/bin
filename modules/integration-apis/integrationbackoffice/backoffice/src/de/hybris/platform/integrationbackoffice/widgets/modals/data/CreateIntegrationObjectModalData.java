/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.odata2webservices.enums.IntegrationType;

public class CreateIntegrationObjectModalData
{

	private String name;
	private ComposedTypeModel composedTypeModel;
	private IntegrationType type;

	public CreateIntegrationObjectModalData(final String name, final ComposedTypeModel composedTypeModel,
	                                        final IntegrationType type)
	{
		this.name = name;
		this.composedTypeModel = composedTypeModel;
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public ComposedTypeModel getComposedTypeModel()
	{
		return composedTypeModel;
	}

	public IntegrationType getType()
	{
		return type;
	}

}
