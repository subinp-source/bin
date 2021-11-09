/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.CsticGroup;
import de.hybris.platform.sap.productconfig.runtime.interf.CsticQualifier;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.CsticGroupImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.InstanceModelImpl;

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("javadoc")
@UnitTest
public class UniqueUIKeyGeneratorImplTest
{
	private static final String INST_SEP = "-";
	private static final String SEP = "@";
	private static final String INSTANCE_ID = "1";
	private static final String INSTANCE_NAME = "InstanceName";
	private static final String GROUP_NAME = "groupName";
	private static final String CSTIC_NAME = "CsticName";
	private static final String VALUE_NAME = "ValueName";
	private static final String UIGROUP_ID = "1-THE_PRODUCT@_GEN";
	private static final String UIGROUP_ID_1 = "1-THE_PRODUCT@1";
	private static final String UIGROUP_ID_1_SPECIALIZED = "1-THE_NEW_PRODUCT@1";
	private static final String UNKNOWN_UIGROUP_ID = "XX";
	private static final String UIGROUP_ID_1_NO_INST_NAME = "1-@1";
	private static final String UIGROUP_ID_NO_GROUP = "1-THE_PRODUCT";
	private static final String UIGROUP_ID_MANY_KEY_SEPARATORS = "1-THE_PRODUCT@1@name@suffix";
	private static final String INSTANCE_NAME2 = "THE_PRODUCT";

	private UniqueUIKeyGeneratorImpl classUnderTest;

	private InstanceModel instance;
	private CsticGroup groupModel;
	private CsticModel csticModel;
	private CsticValueModel valueModel;

	@Before
	public void setUp() throws Exception
	{
		classUnderTest = new UniqueUIKeyGeneratorImpl();
		instance = new InstanceModelImpl();
		instance.setId(INSTANCE_ID);
		instance.setName(INSTANCE_NAME);

		groupModel = new CsticGroupImpl();
		groupModel.setName(GROUP_NAME);

		csticModel = new CsticModelImpl();
		csticModel.setName(CSTIC_NAME);

		valueModel = new CsticValueModelImpl();
		valueModel.setName(VALUE_NAME);
	}

	@Test
	public void testGenerateGroupIdForInstance()
	{
		final String groupId = classUnderTest.generateGroupIdForInstance(instance);
		assertEquals("Wrong ui group id", INSTANCE_ID + INST_SEP + INSTANCE_NAME, groupId);
	}

	@Test
	public void testGenerateGroupId()
	{
		final String groupId = classUnderTest.generateGroupIdForGroup(instance, groupModel);
		assertEquals("Wrong ui group id", INSTANCE_ID + INST_SEP + INSTANCE_NAME + SEP + GROUP_NAME, groupId);
	}

	@Test
	public void testGenerateCticId()
	{
		final String csticId = classUnderTest.generateCsticId(csticModel, null, "prefix");
		assertEquals("Wrong ui cstic id", "prefix" + SEP + CSTIC_NAME, csticId);
	}

	@Test
	public void testGenerateCticIdWithValue()
	{
		final String csticId = classUnderTest.generateCsticId(csticModel, valueModel, "prefix");
		assertEquals("Wrong ui cstic id", "prefix" + SEP + CSTIC_NAME + SEP + VALUE_NAME, csticId);
	}

	@Test
	public void testretrieveInstanceId()
	{
		final String groupId = classUnderTest.generateGroupIdForInstance(instance);
		final String instanceId = classUnderTest.retrieveInstanceId(groupId);
		assertEquals("Wrong instance id", INSTANCE_ID, instanceId);
	}

	@Test
	public void testSplitId()
	{
		final CsticQualifier csticQualifier = classUnderTest.splitId("instanceId-instanceName@groupName@csticName");
		assertEquals("instanceId", csticQualifier.getInstanceId());
		assertEquals("instanceName", csticQualifier.getInstanceName());
		assertEquals("groupName", csticQualifier.getGroupName());
		assertEquals("csticName", csticQualifier.getCsticName());
	}

	@Test
	public void testSplitMultipleMinusId()
	{
		final CsticQualifier csticQualifier = classUnderTest.splitId("instanceId-1-instanceName@groupName@csticName");
		assertEquals("instanceId", csticQualifier.getInstanceId());
		assertEquals("1-instanceName", csticQualifier.getInstanceName());
		assertEquals("groupName", csticQualifier.getGroupName());
		assertEquals("csticName", csticQualifier.getCsticName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSplitId_Invalid_noGroup()
	{
		final CsticQualifier csticQualifier = classUnderTest.splitId("instanceId-instanceName@csticName");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSplitId_Invalid_noInstName()
	{
		final CsticQualifier csticQualifier = classUnderTest.splitId("instanceId.groupName@csticName");
	}

	@Test
	public void testGetBuilderChecksCapacity()
	{
		StringBuilder strBuilder = classUnderTest.getStrBuilder();
		strBuilder.ensureCapacity(1025);
		strBuilder = classUnderTest.getStrBuilder();
		assertEquals(128, strBuilder.capacity());
	}


	@Test
	public void testGenerateId()
	{
		final CsticQualifier csticQualifier = new CsticQualifier();
		csticQualifier.setInstanceId("instanceId");
		csticQualifier.setInstanceName("instanceName");
		csticQualifier.setCsticName("csticName");
		csticQualifier.setGroupName("groupName");
		final String csticUiKey = classUnderTest.generateId(csticQualifier);
		assertEquals("instanceId-instanceName@groupName@csticName", csticUiKey);
	}

	@Test
	public void testExtractInstanceNameFromGroupId()
	{
		String instanceName = classUnderTest.extractInstanceNameFromGroupId(UIGROUP_ID);
		assertEquals(INSTANCE_NAME2, instanceName);

		instanceName = classUnderTest.extractInstanceNameFromGroupId(UIGROUP_ID_1);
		assertEquals(INSTANCE_NAME2, instanceName);

		instanceName = classUnderTest.extractInstanceNameFromGroupId(UIGROUP_ID_NO_GROUP);
		assertEquals(INSTANCE_NAME2, instanceName);

		instanceName = classUnderTest.extractInstanceNameFromGroupId(UIGROUP_ID_MANY_KEY_SEPARATORS);
		assertEquals(INSTANCE_NAME2, instanceName);

		instanceName = classUnderTest.extractInstanceNameFromGroupId(UNKNOWN_UIGROUP_ID);
		assertNull(instanceName);

		instanceName = classUnderTest.extractInstanceNameFromGroupId(UIGROUP_ID_1_NO_INST_NAME);
		assertEquals("", instanceName);
	}

}
