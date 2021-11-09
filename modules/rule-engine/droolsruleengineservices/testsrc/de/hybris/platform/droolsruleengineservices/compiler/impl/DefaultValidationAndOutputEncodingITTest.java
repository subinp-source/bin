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
package de.hybris.platform.droolsruleengineservices.compiler.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleActionsGenerator;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleConditionsGenerator;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleGeneratorContext;

import de.hybris.platform.droolsruleengineservices.impl.AbstractRuleEngineServicesTest;
import de.hybris.platform.impex.jalo.ImpExException;

import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;

import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.compiler.*;
import de.hybris.platform.ruleengineservices.compiler.impl.*;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContextProvider;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.RuleGroupModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;

import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.ruleengineservices.rule.services.RuleParametersService;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;


import de.hybris.platform.ruleengineservices.rule.strategies.RuleConverterException;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import javax.annotation.Resource;

import java.io.IOException;
import java.io.InputStream;

import java.util.*;


import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.junit.Assert.assertEquals;


/**
 * Tests the Validation and output encoding.
 */
@IntegrationTest
public class DefaultValidationAndOutputEncodingITTest extends AbstractRuleEngineServicesTest
{


	private static final String USD = "USD";

	@Resource
	private DefaultDroolsRuleTargetCodeGenerator droolsRuleTargetCodeGenerator;
	private SourceRuleModel sourceRule;

	private DroolsRuleModel droolsRule;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Resource
	private DroolsRuleConditionsGenerator droolsRuleConditionsGenerator;
	@Resource
	private DroolsRuleActionsGenerator droolsRuleActionsGenerator;
	@Resource
	private RuleService ruleService;


	@Resource
	private RuleCompilationContextProvider ruleCompilationContextProvider;

	@Resource
	private RuleIrVariablesGeneratorFactory ruleIrVariablesGeneratorFactory;

	@Resource
	private DefaultRuleCompilerService ruleCompilerService;

	@Resource
	private DefaultRuleSourceCodeTranslatorFactory defaultRuleSourceCodeTranslatorFactory;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private RuleParametersService ruleParametersService;
	@Resource
	private DefaultRuleIrProcessorFactory defaultRuleIrProcessorFactory;



	@Resource
	private DefaultRuleCompilerContextFactory ruleCompilerContextFactory;

	@Resource
	private RuleDao ruleDao;

	@Resource
	private ModelService modelService;

	private String droolRuleUuid = "a33dc303-11ef-4719-82f5-ea6d8ae6dec9";

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/droolsruleengineservices/test/outputEncodeing_source_rule.impex", "utf-8");
	}

	@Test
	public void testOutputEncoding() throws IOException
	{
		final DroolsRuleGeneratorContext generatorContext = prepareContext();

		final String generatedDrl = droolsRuleTargetCodeGenerator.generateRuleContent(generatorContext);

		final String expectedDroolsContentCode = getResourceAsString(
				"/droolsruleengineservices/test/compiler/sony_generateWholedroolContents.bin");
		assertEquals(expectedDroolsContentCode, generatedDrl);

	}


	@Test
	public void testValidationOfActions() throws IOException
	{
		final DroolsRuleGeneratorContext generatorContext = prepareContext();

		List<RuleIrAction> actions = generatorContext.getRuleIr().getActions();
		if (CollectionUtils.isEmpty(actions))
		{
			actions = emptyList();
		}

		for (final RuleIrAction action : actions)
		{
			if (action instanceof RuleIrExecutableAction)
			{
				((RuleIrExecutableAction) action).setActionId("Map");
				return;
			}
		}

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Not a valid spring bean name: " + "Map");
		droolsRuleTargetCodeGenerator.generateRuleContent(generatorContext);
	}



	protected DroolsRuleGeneratorContext prepareContext() throws IOException
	{

		final String moduleName1 = "promotions-module-junit";
		final AbstractRuleModel rule1 = ruleDao.findRuleByCode("sony_category_test");
		final RuleIrVariablesGenerator variablesGenerator = ruleIrVariablesGeneratorFactory.createVariablesGenerator();

		RuleCompilationContext ruleCompilationContext = ruleCompilationContextProvider.getRuleCompilationContext();
		final DefaultRuleCompilerContext context = ruleCompilerContextFactory
				.createContext(ruleCompilationContext, rule1, moduleName1, variablesGenerator);

		final RuleSourceCodeTranslator sourceCodeTranslator = defaultRuleSourceCodeTranslatorFactory
				.getSourceCodeTranslator(context);
		final RuleIr ruleIr = sourceCodeTranslator.translate(context);

		// process the intermediate representation
		final List<RuleIrProcessor> irProcessors = defaultRuleIrProcessorFactory.getRuleIrProcessors();
		for (final RuleIrProcessor irProcessor : irProcessors)
		{
			irProcessor.process(context, ruleIr);
		}

		// convert the intermediate representation to the rule engine format
		final String moduleName = context.getModuleName();

		final AbstractRuleModel rule = context.getRule();
		final String ruleCode = rule.getCode();
		DroolsRuleModel droolsRule;
		final AbstractRuleEngineRuleModel engineRule = getPlatformRuleEngineService().getRuleForCodeAndModule(ruleCode, moduleName);

		if (isNull(engineRule))
		{
			droolsRule = getModelService().create(DroolsRuleModel.class);
			final RuleType ruleType = ruleService.getEngineRuleTypeForRuleType(rule.getClass());
			droolsRule.setRuleType(ruleType);
			droolsRule.setCode(ruleCode);

		}
		else
		{
			if (engineRule instanceof DroolsRuleModel)
			{
				droolsRule = (DroolsRuleModel) engineRule;

			}
			else
			{
				throw new RuleCompilerException(
						String.format("Given rule with the code: %s is not of the type DroolsRuleModel.", ruleCode));
			}
		}
		droolsRule.setUuid(droolRuleUuid);
		droolsRule.setSourceRule(rule);
		droolsRule.setActive(Boolean.TRUE);

		final String ruleGroupCode = getRuleGroupCode(rule);
		droolsRule.setRuleGroupCode(ruleGroupCode);

		for (final LanguageModel language : commonI18NService.getAllLanguages())
		{
			final Locale locale = commonI18NService.getLocaleForLanguage(language);
			droolsRule.setMessageFired(rule.getMessageFired(locale), locale);
		}

		try
		{
			final String ruleParameters = ruleParametersService.convertParametersToString(context.getRuleParameters());
			droolsRule.setRuleParameters(ruleParameters);
		}
		catch (final RuleConverterException e)
		{
			throw new RuleCompilerException("RuleConverterException caught: ", e);
		}

		final DroolsRuleGeneratorContext generatorContext = createGeneratorContext(context, ruleIr, droolsRule);

		return generatorContext;
	}

	protected String getRuleGroupCode(final AbstractRuleModel rule)
	{
		final RuleGroupModel ruleGroup = rule.getRuleGroup();
		if (nonNull(ruleGroup) && isNotEmpty(ruleGroup.getCode()))
		{
			return ruleGroup.getCode();
		}
		return null;
	}

	protected DroolsRuleGeneratorContext createGeneratorContext(final RuleCompilerContext context, final RuleIr ruleIr,
			final DroolsRuleModel droolsRule)
	{
		return new DefaultDroolsGeneratorContext(context, ruleIr, droolsRule);
	}

	protected String getResourceAsString(final String name) throws IOException
	{
		try (InputStream inputStream = getClass().getResourceAsStream(name))
		{
			return IOUtils.toString(this.getClass().getResourceAsStream(name), "UTF-8");
		}
	}


}
