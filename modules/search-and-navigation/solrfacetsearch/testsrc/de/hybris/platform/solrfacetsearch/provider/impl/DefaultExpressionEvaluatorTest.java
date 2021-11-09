/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;


@UnitTest
public class DefaultExpressionEvaluatorTest
{
	@Mock
	private ItemModel model;

	@Mock
	private ApplicationContext applicationContext;

	public DefaultExpressionEvaluatorTest()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testEvaluatingExpression()
	{
		//given
		final DefaultExpressionEvaluator defaultExpressionEvaluator = new DefaultExpressionEvaluator();
		defaultExpressionEvaluator.setApplicationContext(applicationContext);
		defaultExpressionEvaluator.setParser(new SpelExpressionParser());

		final String exprValue = "comments[0].code";
		final String expressionValue = "testCode";

		final CommentModel commentModel = new CommentModel();
		commentModel.setCode(expressionValue);

		final List<CommentModel> commentModels = new ArrayList<>();
		commentModels.add(commentModel);

		when(model.getComments()).thenReturn(commentModels);

		//when
		final Object result = defaultExpressionEvaluator.evaluate(exprValue, model);

		//then
		Assert.assertTrue(result instanceof String);
		Assert.assertEquals(result, expressionValue);
	}
}
