/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.y2ysync;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;


public class ColumnDefinitionAssert extends AbstractAssert<ColumnDefinitionAssert, Y2YColumnDefinitionModel>
{

	protected ColumnDefinitionAssert(final Y2YColumnDefinitionModel actual)
	{
		super(actual, ColumnDefinitionAssert.class);
	}

	public static ColumnDefinitionAssert assertThat(final Y2YColumnDefinitionModel actual)
	{
		return new ColumnDefinitionAssert(actual);
	}

	public ColumnDefinitionAssert withImpexHeader(final String impexHeader)
	{
		Assertions.assertThat(actual.getImpexHeader()).isEqualTo(impexHeader);
		return this;
	}

	public ColumnDefinitionAssert withColumnName(final String columnName)
	{
		Assertions.assertThat(actual.getColumnName()).isEqualTo(columnName);
		return this;
	}


	public ColumnDefinitionAssert withLanguage(final LanguageModel languageModel)
	{
		Assertions.assertThat(actual.getLanguage()).isNotNull();
		Assertions.assertThat(actual.getLanguage().getIsocode()).isEqualTo(languageModel.getIsocode());
		return this;
	}

	public ColumnDefinitionAssert withoutLanguage()
	{
		Assertions.assertThat(actual.getLanguage()).isNull();
		return this;
	}

}
