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
import de.hybris.platform.searchservices.admin.dao.SnFieldDao;
import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.validation.services.ValidationService;

import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@IntegrationTest
public class SnFieldModelTest extends ServicelayerTransactionalTest
{
	private static final String TYPE_CODE = "typeCode";

	private static final String INVALID_ID = "__id1__";

	private static final String ID1 = "id1";
	private static final String ID2 = "id2";

	private static final String NAME1 = "name1";
	private static final String NAME2 = "name2";

	private static final Float WEIGHT1 = Float.valueOf(2.5f);
	private static final Float WEIGHT2 = Float.valueOf(5f);

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Resource
	private TypeService typeService;

	@Resource
	private ModelService modelService;

	@Resource
	private ValidationService validationService;

	@Resource
	private SnFieldDao snFieldDao;

	private SnIndexTypeModel indexType;

	@Before
	public void setUp() throws Exception
	{
		final ComposedTypeModel itemComposedType = typeService.getComposedTypeForClass(ProductModel.class);

		indexType = modelService.create(SnIndexTypeModel.class);
		indexType.setId(TYPE_CODE);
		indexType.setItemComposedType(itemComposedType);

		modelService.save(indexType);

		importCsv("/impex/essentialdata-searchservices-validation.impex", "UTF-8");
		validationService.reloadValidationEngine();
	}

	@Test
	public void getNonExistingField()
	{
		// when
		final Optional<SnFieldModel> fieldOptional = snFieldDao.findFieldByIndexTypeAndId(indexType, ID1);

		// then
		assertFalse(fieldOptional.isPresent());
	}

	@Test
	public void createField()
	{
		// given
		final SnFieldModel field = modelService.create(SnFieldModel.class);
		field.setId(ID1);
		field.setName(NAME1);
		field.setIndexType(indexType);
		field.setFieldType(SnFieldType.STRING);
		field.setWeight(WEIGHT1);

		// when
		modelService.save(field);

		final Optional<SnFieldModel> createdFieldOptional = snFieldDao.findFieldByIndexTypeAndId(indexType, ID1);

		// then
		assertTrue(createdFieldOptional.isPresent());
		final SnFieldModel createdField = createdFieldOptional.get();
		assertEquals(ID1, createdField.getId());
		assertEquals(NAME1, createdField.getName());
		assertEquals(WEIGHT1, createdField.getWeight());
	}

	@Test
	public void failToCreateFieldWithInvalidId()
	{
		// given
		final SnFieldModel field = modelService.create(SnFieldModel.class);
		field.setId(INVALID_ID);
		field.setName(NAME1);
		field.setIndexType(indexType);
		field.setFieldType(SnFieldType.STRING);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(field);
	}

	@Test
	public void failToCreateFieldWithoutId()
	{
		// given
		final SnFieldModel field = modelService.create(SnFieldModel.class);
		field.setName(NAME1);
		field.setIndexType(indexType);
		field.setFieldType(SnFieldType.STRING);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(field);
	}

	@Test
	public void failToCreateFieldWithoutType()
	{
		// given
		final SnFieldModel field = modelService.create(SnFieldModel.class);
		field.setId(ID1);
		field.setName(NAME1);
		field.setFieldType(SnFieldType.STRING);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(field);
	}

	@Test
	public void failToCreatefieldWithoutFieldType()
	{
		// given
		final SnFieldModel field = modelService.create(SnFieldModel.class);
		field.setId(ID1);
		field.setName(NAME1);
		field.setIndexType(indexType);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(field);
	}

	@Test
	public void failToCreatefieldWithNegativeWeight()
	{
		// given
		final SnFieldModel field = modelService.create(SnFieldModel.class);
		field.setId(ID1);
		field.setName(NAME1);
		field.setIndexType(indexType);
		field.setFieldType(SnFieldType.STRING);
		field.setWeight(-1.0f);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(field);
	}

	@Test
	public void failToCreatefieldWithZeroWeight()
	{
		// given
		final SnFieldModel field = modelService.create(SnFieldModel.class);
		field.setId(ID1);
		field.setName(NAME1);
		field.setIndexType(indexType);
		field.setFieldType(SnFieldType.STRING);
		field.setWeight(0.0f);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(field);
	}

	@Test
	public void createMultipleFields()
	{
		// given
		final SnFieldModel field1 = modelService.create(SnFieldModel.class);
		field1.setId(ID1);
		field1.setName(NAME1);
		field1.setIndexType(indexType);
		field1.setFieldType(SnFieldType.STRING);
		field1.setWeight(WEIGHT1);

		final SnFieldModel field2 = modelService.create(SnFieldModel.class);
		field2.setId(ID2);
		field2.setName(NAME2);
		field2.setIndexType(indexType);
		field2.setFieldType(SnFieldType.STRING);
		field2.setWeight(WEIGHT2);

		// when
		modelService.save(field1);
		modelService.save(field2);

		final Optional<SnFieldModel> createdField1Optional = snFieldDao.findFieldByIndexTypeAndId(indexType, ID1);
		final Optional<SnFieldModel> createdField2Optional = snFieldDao.findFieldByIndexTypeAndId(indexType, ID2);

		// then
		assertTrue(createdField1Optional.isPresent());
		final SnFieldModel createdField1 = createdField1Optional.get();
		assertEquals(ID1, createdField1.getId());
		assertEquals(NAME1, createdField1.getName());
		assertEquals(WEIGHT1, createdField1.getWeight());

		assertTrue(createdField2Optional.isPresent());
		final SnFieldModel createdField2 = createdField2Optional.get();
		assertEquals(ID2, createdField2.getId());
		assertEquals(NAME2, createdField2.getName());
		assertEquals(WEIGHT2, createdField2.getWeight());
	}

	@Test
	public void failToCreateMultipleFieldsWithSameId()
	{
		// given
		final SnFieldModel field1 = modelService.create(SnFieldModel.class);
		field1.setId(ID1);
		field1.setName(NAME1);
		field1.setIndexType(indexType);
		field1.setFieldType(SnFieldType.STRING);

		final SnFieldModel field2 = modelService.create(SnFieldModel.class);
		field2.setId(ID1);
		field2.setName(NAME2);
		field2.setIndexType(indexType);
		field2.setFieldType(SnFieldType.STRING);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(field1);
		modelService.save(field2);
	}
}
