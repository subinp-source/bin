/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.tree.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.tree.node.DynamicNode;
import com.hybris.cockpitng.tree.util.TreeUtils;


@RunWith(MockitoJUnitRunner.class)
public class CatalogTreeModelPopulatorFilteringTest
{

	@Spy
	@InjectMocks
	private CatalogTreeModelPopulator populator;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private CatalogModel catalog;

	@Mock
	private DynamicNode root;

	@Mock
	private LabelService labelService;

	@Mock
	private CockpitContext cockpitContext;

	@Mock
	private TypeFacade typeFacade;

	@Mock
	private DataType emptyType;

	@Before
	public void setUp() throws TypeNotFoundException
	{
		doReturn(cockpitContext).when(root).getContext();
		doReturn(false).when(populator).simpleLabelsEnabled(any());
		doReturn(catalog).when(catalogVersion).getCatalog();
		doReturn("Hardware").when(catalog).getId();
		doReturn("Fixed").when(catalogVersion).getVersion();
		doReturn(emptyType).when(typeFacade).load(any());
		doReturn(Collections.emptyList()).when(emptyType).getAttributes();
	}

	@Test
	public void buildSpanningTreeShouldReturnEmptyCollectionOnEmptyInput()
	{
		//when
		final List<NavigationNode> actual = populator.buildSpanningTree(root, Collections.emptyList(), catalogVersion);

		//then
		assertThat(actual).isEmpty();
	}

	@Test
	public void buildSpanningTreeShouldReturnEmptyCollectionIfTheMatchIsNotFromTheSameCV()
	{
		//given
		final CategoryModel category = mockCategory("from different catalog version");
		doReturn(mock(CatalogVersionModel.class)).when(category).getCatalogVersion();

		//when
		final List<NavigationNode> result = populator.buildSpanningTree(root, Collections.singletonList(category), catalogVersion);

		//then
		assertThat(result).isEmpty();
	}

	@Test
	public void buildSpanningTreeShouldReturnTheOnlyNodeIfFromTheSameCV()
	{
		//given
		final CategoryModel category = mockCategory("from same catalog version");
		doReturn(catalogVersion).when(category).getCatalogVersion();

		//when
		final List<NavigationNode> result = populator.buildSpanningTree(root, Collections.singletonList(category), catalogVersion);

		//then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getData()).isEqualTo(category);
	}

	/**
	 * CV - A - (x1)
	 */
	@Test
	public void buildSpanningTreeShouldReturnBranchContainingGivenNodeAsLeaf()
	{
		//given
		final CategoryModel a = mockCategory("A");
		doReturn(Collections.emptyList()).when(a).getSupercategories();
		doReturn(catalogVersion).when(a).getCatalogVersion();
		final CategoryModel x1 = mockCategory("x1");
		doReturn(Collections.singletonList(a)).when(x1).getSupercategories();

		//when
		final List<NavigationNode> result = populator.buildSpanningTree(root, Collections.singletonList(x1), catalogVersion);

		//then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getData()).isEqualTo(a);
		assertThat(result.get(0).getChildren()).hasSize(1);
		assertThat(result.get(0).getChildren().get(0).getData()).isEqualTo(x1);
	}

	/**
	 * CV - A - AA - (x1)
	 */
	@Test
	public void buildSpanningTreeShouldReturnBranchContainingGivenNodeAsNestedLeaf()
	{
		//given
		final CategoryModel a = mockCategory("A");
		doReturn(Collections.emptyList()).when(a).getSupercategories();

		final CategoryModel aa = mockCategory("AA");
		doReturn(Collections.singletonList(a)).when(aa).getSupercategories();

		final CategoryModel x1 = mockCategory("x1");
		doReturn(Collections.singletonList(aa)).when(x1).getSupercategories();

		//when
		final List<NavigationNode> result = populator.buildSpanningTree(root, Collections.singletonList(x1), catalogVersion);

		//then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getData()).isEqualTo(a);
		assertThat(result.get(0).getChildren()).hasSize(1);
		assertThat(result.get(0).getChildren().get(0).getData()).isEqualTo(aa);
		assertThat(result.get(0).getChildren().get(0).getChildren()).hasSize(1);
		assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getData()).isEqualTo(x1);
	}

	/**
	 * {@code
	 * CV - B - BA - (x10)
	 *             - (x11)
	 *             - (x101) - Z - (x1010)
	 * }
	 */
	@Test
	public void buildSpanningTreeShouldReturnBranchContainingGivenNodesAsNestedLeafs()
	{
		//given
		final CategoryModel b = mockCategory("B");
		doReturn(Collections.emptyList()).when(b).getSupercategories();

		final CategoryModel ba = mockCategory("BA");
		doReturn(Collections.singletonList(b)).when(ba).getSupercategories();

		final CategoryModel x10 = mockCategory("x10");
		doReturn(Collections.singletonList(ba)).when(x10).getSupercategories();

		final CategoryModel x11 = mockCategory("x11");
		doReturn(Collections.singletonList(ba)).when(x11).getSupercategories();

		final CategoryModel x101 = mockCategory("x101");
		doReturn(Collections.singletonList(ba)).when(x101).getSupercategories();

		final CategoryModel z = mockCategory("Z");
		doReturn(Collections.singletonList(x101)).when(z).getSupercategories();

		final CategoryModel x1010 = mockCategory("x1010");
		doReturn(Collections.singletonList(z)).when(x1010).getSupercategories();

		doReturn(Arrays.asList(navNode(z))).when(populator).getChildren(argThat(new ArgumentMatcher<>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (argument instanceof NavigationNode)
				{
					return ((NavigationNode) argument).getData() == x101;
				}
				return false;
			}
		}));

		//when
		final List<NavigationNode> result = populator.buildSpanningTree(root, Arrays.asList(x10, x101, x11, x1010), catalogVersion);

		//then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getData()).isEqualTo(b);
		assertThat(result.get(0).getChildren()).hasSize(1);
		assertThat(result.get(0).getChildren().get(0).getData()).isEqualTo(ba);
		assertThat(result.get(0).getChildren().get(0).getChildren()).hasSize(3);
		assertThat(result.get(0).getChildren().get(0).getChildren().stream().map(node -> node.getData()))
				.containsExactlyInAnyOrder(x10, x11, x101);

		final Optional<NavigationNode> navNodeX101 = result.get(0).getChildren().get(0).getChildren().stream()
				.filter(node -> node.getData() == x101).findFirst();
		assertThat(navNodeX101).isNotEmpty();
		assertThat(navNodeX101.orElseThrow().getChildren()).hasSize(1);
		final NavigationNode navNodeZ = navNodeX101.orElseThrow().getChildren().get(0);
		assertThat(navNodeZ.getData()).isEqualTo(z);
		assertThat(navNodeZ.getChildren()).hasSize(1);
		assertThat(navNodeZ.getChildren().get(0).getData()).isEqualTo(x1010);
	}

	/**
	 * {@code
	 * CV - C - (x12) - P
	 *                - Q
	 * }
	 */
	@Test
	public void buildSpanningTreeShouldReturnTheWholeBranchWithChildren()
	{
		//given
		final CategoryModel c = mockCategory("C");
		doReturn(Collections.emptyList()).when(c).getSupercategories();

		final CategoryModel x12 = mockCategory("x12");
		doReturn(Collections.singletonList(c)).when(x12).getSupercategories();

		final CategoryModel p = mockCategory("P");
		doReturn(Collections.singletonList(x12)).when(p).getSupercategories();

		final CategoryModel q = mockCategory("Q");
		doReturn(Collections.singletonList(x12)).when(q).getSupercategories();

		doReturn(Arrays.asList(navNode(p), navNode(q))).when(populator).getChildren(argThat(new ArgumentMatcher<>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (argument instanceof NavigationNode)
				{
					return ((NavigationNode) argument).getData() == x12;
				}
				return false;
			}
		}));

		//when
		final List<NavigationNode> result = populator.buildSpanningTree(root, Arrays.asList(x12), catalogVersion);

		//then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getData()).isEqualTo(c);
		assertThat(result.get(0).getChildren()).hasSize(1);
		assertThat(result.get(0).getChildren().get(0).getData()).isEqualTo(x12);
		assertThat(result.get(0).getChildren().get(0).getChildren()).hasSize(2);
		assertThat(result.get(0).getChildren().get(0).getChildren().stream().map(node -> node.getData()))
				.containsExactlyInAnyOrder(p, q);
	}

	/**
	 * CV - C - (x13) - (x14) - Y
	 */
	@Test
	public void buildSpanningTreeShouldReturnNestedCategories()
	{
		//given
		final CategoryModel c = mockCategory("C");
		doReturn(Collections.emptyList()).when(c).getSupercategories();

		final CategoryModel x13 = mockCategory("x13");
		doReturn(Collections.singletonList(c)).when(x13).getSupercategories();

		final CategoryModel x14 = mockCategory("x14");
		doReturn(Collections.singletonList(x13)).when(x14).getSupercategories();

		final CategoryModel y = mockCategory("Y");
		doReturn(Collections.singletonList(x13)).when(y).getSupercategories();

		doReturn(Arrays.asList(navNode(x14), navNode(y))).when(populator).getChildren(argThat(new ArgumentMatcher<>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (argument instanceof NavigationNode)
				{
					return ((NavigationNode) argument).getData() == x13;
				}
				return false;
			}
		}));

		//when
		final List<NavigationNode> result = populator.buildSpanningTree(root, Arrays.asList(x13, x14), catalogVersion);

		//then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getData()).isEqualTo(c);
		assertThat(result.get(0).getChildren()).hasSize(1);
		assertThat(result.get(0).getChildren().get(0).getData()).isEqualTo(x13);
		assertThat(result.get(0).getChildren().get(0).getChildren()).hasSize(2);
	}

	@Test
	public void preparePopulatingQueryShouldIncludeSubtypes()
	{
		//when
		final AdvancedSearchQueryData data = populator.preparePopulatingQuery(root, catalogVersion,"", false, TreeUtils.MatchMode.CONTAINS);

		//then
		assertThat(data.isIncludeSubtypes()).isTrue();
	}
	
	private NavigationNode navNode(final CategoryModel cat)
	{
		return new SimpleNode(cat.getCode(), cat);
	}

	private CategoryModel mockCategory(final String name)
	{
		final CategoryModel category = mock(CategoryModel.class);
		doReturn(name).when(category).toString();
		doReturn(catalogVersion).when(category).getCatalogVersion();
		doReturn(name).when(category).getCode();
		return category;
	}

}
