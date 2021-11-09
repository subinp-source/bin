/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;
import java.util.Locale;


public class WorkflowTemplateModelBuilder
{
	private final WorkflowTemplateModel model;

	private WorkflowTemplateModelBuilder()
	{
		model = new WorkflowTemplateModel();
	}

	private WorkflowTemplateModelBuilder(final WorkflowTemplateModel model)
	{
		this.model = model;
	}

	protected WorkflowTemplateModel getModel()
	{
		return this.model;
	}

	public static WorkflowTemplateModelBuilder aModel()
	{
		return new WorkflowTemplateModelBuilder();
	}

	public static WorkflowTemplateModelBuilder fromModel(final WorkflowTemplateModel model)
	{
		return new WorkflowTemplateModelBuilder(model);
	}

	public WorkflowTemplateModel build()
	{
		return this.getModel();
	}

	public WorkflowTemplateModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setName(name, locale);
		return this;
	}

	public WorkflowTemplateModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

	public WorkflowTemplateModelBuilder withCatalogVersions(final List<CatalogVersionModel> catalogVersions)
	{
		getModel().setCatalogVersions(catalogVersions);
		return this;
	}

	public WorkflowTemplateModelBuilder withVisibleForPrincipals(final List<PrincipalModel> principals)
	{
		getModel().setVisibleForPrincipals(principals);
		return this;
	}
}
