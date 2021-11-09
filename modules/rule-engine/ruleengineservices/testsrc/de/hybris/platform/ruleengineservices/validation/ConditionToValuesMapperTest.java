/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengineservices.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ConditionToValuesMapperTest<T>
{
	@Mock
	private RuleParameterValueMapper<T> parameterValueMapper;

	@InjectMocks
	private ConditionToValuesMapper<T> mapper;

	@Before
	public void setUp()
	{
		mapper.setParameterNames(Set.of("parameterName1", "parameterName2"));
		mapper.setDefinitionId("definitionId");
		mapper.setType("Type(SomeType)");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIAEOnNullParameter()
	{
		mapper.apply(null);
	}

	@Test
	public void shouldReturnListOfMappedValues()
	{
		final RuleConditionData childCondition1 = new RuleConditionData();
		childCondition1.setDefinitionId("definitionId");

		final RuleParameterData parameter1 = new RuleParameterData();
		parameter1.setType("Type(SomeType)");
		parameter1.setValue("value1");
		final RuleParameterData parameter2 = new RuleParameterData();
		parameter2.setType("OtherType2");
		parameter2.setValue("value2");

		childCondition1.setParameters(Map.of("parameterName1", parameter1, "parameter2", parameter2));

		final RuleConditionData childCondition2 = new RuleConditionData();
		childCondition2.setDefinitionId("definitionId");
		final RuleParameterData parameter3 = new RuleParameterData();
		parameter3.setType("OtherType3");
		parameter3.setValue("value3");
		final RuleParameterData parameter4 = new RuleParameterData();
		parameter4.setType("Type(SomeType)");
		parameter4.setValue("value4");

		childCondition2.setParameters(Map.of("parameter3", parameter3, "parameterName2", parameter4));

		final RuleConditionData mainCondition = new RuleConditionData();
		mainCondition.setChildren(List.of(childCondition1, childCondition2));

		final T object1 = (T) mock(Object.class);
		final T object2 = (T) mock(Object.class);

		when(parameterValueMapper.fromString("value1")).thenReturn(object1);
		when(parameterValueMapper.fromString("value4")).thenReturn(object2);

		final List<T> result = mapper.apply(mainCondition);

		assertThat(result).isNotNull().isNotEmpty().containsOnly(object1, object2);
	}

	@Test
	public void shouldReturnEmptyListOnNonMatchingType()
	{
		final RuleConditionData childCondition1 = new RuleConditionData();
		childCondition1.setDefinitionId("otherDefinitionId");

		final RuleParameterData parameter1 = new RuleParameterData();
		parameter1.setType("OtherType");
		parameter1.setValue("value1");
		final RuleParameterData parameter2 = new RuleParameterData();
		parameter2.setType("OtherType2");
		parameter2.setValue("value2");

		childCondition1.setParameters(Map.of("parameter1", parameter1, "parameter2", parameter2));

		final RuleConditionData childCondition2 = new RuleConditionData();
		final RuleParameterData parameter3 = new RuleParameterData();
		parameter3.setType("OtherType3");
		parameter3.setValue("value3");
		final RuleParameterData parameter4 = new RuleParameterData();
		parameter4.setType("OtherType2");
		parameter4.setValue("value4");

		childCondition2.setParameters(Map.of("parameter3", parameter3, "parameter4", parameter4));

		final RuleConditionData mainCondition = new RuleConditionData();
		mainCondition.setChildren(List.of(childCondition1, childCondition2));

		final List<T> result = mapper.apply(mainCondition);

		assertThat(result).isNotNull().isEmpty();
	}
}
