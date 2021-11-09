/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.validators.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.validation.daos.ConstraintDao;
import de.hybris.platform.validation.localized.LocalizedAttributeConstraint;
import de.hybris.platform.validation.localized.LocalizedConstraintsRegistry;
import de.hybris.platform.validation.localized.TypeLocalizedConstraints;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.validation.groups.Default;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.hybris.backoffice.daos.BackofficeValidationDao;


@RunWith(MockitoJUnitRunner.class)
public class ExcelLocalizedConstraintsProviderTest
{

	@Mock
	private LocalizedConstraintsRegistry localizedConstraintsRegistry;
	@Mock
	private ConstraintDao constraintDao;
	@Mock
	private BackofficeValidationDao validationDao;

	@InjectMocks
	private ExcelLocalizedConstraintsProvider facade;

	private static final Class PRODUCT_MODEL_CLASS = ProductModel.class;
	private static final String DEFAULT_CONSTRAINT_GROUP = "default";
	private ConstraintGroupModel defaultConstraintGroupModel;
	private static final Class DEFAULT_CONSTRAINT_GROUP_CLASS = Default.class;
	private static final List<String> CONSTRAINT_GROUPS = Lists.newArrayList(DEFAULT_CONSTRAINT_GROUP);


	@Before
	public void setUp() throws Exception
	{
		defaultConstraintGroupModel = mock(ConstraintGroupModel.class);
		initializeLocalizedConstraintsRegistry();
		given(validationDao.getConstraintGroups(CONSTRAINT_GROUPS)).willReturn(Lists.newArrayList(defaultConstraintGroupModel));
		given(constraintDao.getTargetClass(defaultConstraintGroupModel)).willReturn(DEFAULT_CONSTRAINT_GROUP_CLASS);
	}

	@Test
	public void shouldReturnEmptyListWhenNoConstraintsAreDefined()
	{
		// given
		setProductConstraints(Collections.emptyList());

		// when
		final Collection<LocalizedAttributeConstraint> constraints = facade.getLocalizedAttributeConstraints(PRODUCT_MODEL_CLASS,
				CONSTRAINT_GROUPS);

		// then
		assertThat(constraints).isEmpty();
	}

	@Test
	public void shouldReturnConstraintForIncludedGroups()
	{
		// given
		final LocalizedAttributeConstraint constraintInDefaultGroup = mock(LocalizedAttributeConstraint.class);
		given(constraintInDefaultGroup.definedForGroups(DEFAULT_CONSTRAINT_GROUP_CLASS)).willReturn(true);

		setProductConstraints(Lists.newArrayList(constraintInDefaultGroup));

		// when
		final Collection<LocalizedAttributeConstraint> constraints = facade.getLocalizedAttributeConstraints(PRODUCT_MODEL_CLASS,
				CONSTRAINT_GROUPS);

		// then
		assertThat(constraints).hasSize(1);
		assertThat(constraints.iterator().next()).isEqualTo(constraintInDefaultGroup);
	}

	@Test
	public void shouldNotReturnConstraintFromNotIncludedGroup()
	{
		// given
		final LocalizedAttributeConstraint constraintInDifferentGroup = mock(LocalizedAttributeConstraint.class);
		given(constraintInDifferentGroup.definedForGroups(DEFAULT_CONSTRAINT_GROUP_CLASS)).willReturn(false);

		setProductConstraints(Lists.newArrayList(constraintInDifferentGroup));

		// when
		final Collection<LocalizedAttributeConstraint> constraints = facade.getLocalizedAttributeConstraints(PRODUCT_MODEL_CLASS,
				CONSTRAINT_GROUPS);

		// then
		assertThat(constraints).isEmpty();
	}

	@Test
	public void shouldReturnConstraintFromIncludedGroupAndFilterOutOthers()
	{
		// given
		final LocalizedAttributeConstraint constraintInDefaultGroup = mock(LocalizedAttributeConstraint.class);
		final LocalizedAttributeConstraint constraintInOtherGroup = mock(LocalizedAttributeConstraint.class);
		given(constraintInDefaultGroup.definedForGroups(DEFAULT_CONSTRAINT_GROUP_CLASS)).willReturn(true);
		given(constraintInOtherGroup.definedForGroups(DEFAULT_CONSTRAINT_GROUP_CLASS)).willReturn(false);

		setProductConstraints(Lists.newArrayList(constraintInDefaultGroup, constraintInOtherGroup));

		// when

		final Collection<LocalizedAttributeConstraint> constraints = facade.getLocalizedAttributeConstraints(PRODUCT_MODEL_CLASS,
				CONSTRAINT_GROUPS);

		// then
		assertThat(constraints).hasSize(1);
		assertThat(constraints.iterator().next()).isEqualTo(constraintInDefaultGroup);
	}

	private void initializeLocalizedConstraintsRegistry()
	{
		// return no constraints for supertypes of Product
		final TypeLocalizedConstraints constraintsForSupertypes = mock(TypeLocalizedConstraints.class);
		given(constraintsForSupertypes.getConstraints()).willReturn(Lists.newArrayList());
		given(localizedConstraintsRegistry.get(any())).willReturn(constraintsForSupertypes);
	}

	private void setProductConstraints(final List<LocalizedAttributeConstraint> productConstraints)
	{
		final TypeLocalizedConstraints constraintsForProduct = mock(TypeLocalizedConstraints.class);
		given(constraintsForProduct.getConstraints()).willReturn(productConstraints);
		given(localizedConstraintsRegistry.get(PRODUCT_MODEL_CLASS)).willReturn(constraintsForProduct);
	}


}
