/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.admin.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.admin.dao.SnIndexTypeDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;

import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@IntegrationTest
public class SnIndexTypeModelTest extends ServicelayerTransactionalTest
{
	private static final String INVALID_ID = "__id1__";

	private static final String ID1 = "id1";
	private static final String ID2 = "id2";

	private static final String NAME1 = "name1";
	private static final String NAME2 = "name2";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Resource
	private TypeService typeService;

	@Resource
	private ModelService modelService;

	@Resource
	private SnIndexTypeDao snIndexTypeDao;

	private ComposedTypeModel itemComposedType;

	@Before
	public void setUp()
	{
		itemComposedType = typeService.getComposedTypeForClass(ProductModel.class);
	}

	@Test
	public void getNonExistingIndexType()
	{
		// when
		final Optional<SnIndexTypeModel> indexTypeOptional = snIndexTypeDao.findIndexTypeById(ID1);

		// then
		assertFalse(indexTypeOptional.isPresent());
	}

	@Test
	public void createIndexType()
	{
		// given
		final SnIndexTypeModel indexType = modelService.create(SnIndexTypeModel.class);
		indexType.setId(ID1);
		indexType.setName(NAME1);
		indexType.setItemComposedType(itemComposedType);

		// when
		modelService.save(indexType);

		final Optional<SnIndexTypeModel> createdIndexTypeOptional = snIndexTypeDao.findIndexTypeById(ID1);

		// then
		assertTrue(createdIndexTypeOptional.isPresent());
		final SnIndexTypeModel createdType = createdIndexTypeOptional.get();
		assertEquals(ID1, createdType.getId());
		assertEquals(NAME1, createdType.getName());
	}

	@Test
	public void failToCreateIndexTypeWithInvalidId()
	{
		// given
		final SnIndexTypeModel indexType = modelService.create(SnIndexTypeModel.class);
		indexType.setId(INVALID_ID);
		indexType.setName(NAME1);
		indexType.setItemComposedType(itemComposedType);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(indexType);
	}

	@Test
	public void failToCreateIndexTypeWithoutId()
	{
		// given
		final SnIndexTypeModel indexType = modelService.create(SnIndexTypeModel.class);
		indexType.setName(NAME1);
		indexType.setItemComposedType(itemComposedType);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(indexType);
	}

	@Test
	public void failToCreateIndexTypeWithoutItemComposedType()
	{
		// given
		final SnIndexTypeModel indexType = modelService.create(SnIndexTypeModel.class);
		indexType.setId(ID1);
		indexType.setName(NAME1);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(indexType);
	}

	@Test
	public void createMultipleIndexTypes()
	{
		// given
		final SnIndexTypeModel indexType1 = modelService.create(SnIndexTypeModel.class);
		indexType1.setId(ID1);
		indexType1.setName(NAME1);
		indexType1.setItemComposedType(itemComposedType);

		final SnIndexTypeModel IndexType2 = modelService.create(SnIndexTypeModel.class);
		IndexType2.setId(ID2);
		IndexType2.setName(NAME2);
		IndexType2.setItemComposedType(itemComposedType);

		// when
		modelService.save(indexType1);
		modelService.save(IndexType2);

		final Optional<SnIndexTypeModel> createdIndexType1Optional = snIndexTypeDao.findIndexTypeById(ID1);
		final Optional<SnIndexTypeModel> createdIndexType2Optional = snIndexTypeDao.findIndexTypeById(ID2);

		// then
		assertTrue(createdIndexType1Optional.isPresent());
		final SnIndexTypeModel createdIndexType1 = createdIndexType1Optional.get();
		assertEquals(ID1, createdIndexType1.getId());
		assertEquals(NAME1, createdIndexType1.getName());

		assertTrue(createdIndexType2Optional.isPresent());
		final SnIndexTypeModel createdIndexType2 = createdIndexType2Optional.get();
		assertEquals(ID2, createdIndexType2.getId());
		assertEquals(NAME2, createdIndexType2.getName());
	}

	@Test
	public void failToCreateMultipleindexTypesWithSameId()
	{
		// given
		final SnIndexTypeModel indexType1 = modelService.create(SnIndexTypeModel.class);
		indexType1.setId(ID1);
		indexType1.setName(NAME1);
		indexType1.setItemComposedType(itemComposedType);

		final SnIndexTypeModel indexType2 = modelService.create(SnIndexTypeModel.class);
		indexType2.setId(ID1);
		indexType2.setName(NAME2);
		indexType2.setItemComposedType(itemComposedType);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(indexType1);
		modelService.save(indexType2);
	}
}
