/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import java.util.Locale;


public class DeliveryModeModelBuilder
{
	private final DeliveryModeModel model;

	private DeliveryModeModelBuilder()
	{
		model = new DeliveryModeModel();
	}

	protected DeliveryModeModel getModel()
	{
		return this.model;
	}

	public static DeliveryModeModelBuilder aModel()
	{
		return new DeliveryModeModelBuilder();
	}

	public DeliveryModeModel build()
	{
		return getModel();
	}

	public DeliveryModeModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

	public DeliveryModeModelBuilder withActive(final Boolean active)
	{
		getModel().setActive(active);
		return this;
	}

	public DeliveryModeModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setName(name, locale);
		return this;
	}

}