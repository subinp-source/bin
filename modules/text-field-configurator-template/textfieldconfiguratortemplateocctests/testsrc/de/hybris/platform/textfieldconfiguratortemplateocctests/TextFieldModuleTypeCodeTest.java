/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.textfieldconfiguratortemplateocctests;


import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.property.DBPersistenceManager;
import de.hybris.platform.test.ReservedTypecodeTest;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * CPQ version of the {@link ReservedTypecodeTest}. Limits the scope to CPQ extensions. Additionally and exclsuion list
 * can be mainatined until a type code has been maintained in the reservedTypeCode list by platform. <br>
 * <br>
 * Unfortunately we had to copy {@link ReservedTypecodeTest} to be able to add the cpqExclusion list. In case the
 * private static members and methods are evetually chnaged to be at least protected, we can re-place most content of
 * this class by inheriting the ReservedTypecodeTest.
 */
@IntegrationTest
public class TextFieldModuleTypeCodeTest extends HybrisJUnit4Test
{

	private Properties reservedCodes;
	private final DBPersistenceManager manager = (DBPersistenceManager) Registry.getPersistenceManager();
	private List<String> includedExtensions;
	private List<Integer> cpqExclusion;
	private static final Logger LOG = Logger.getLogger(TextFieldModuleTypeCodeTest.class);


	protected TextFieldModuleHelper getModuleHelper()
	{
		return new TextFieldModuleHelper();
	}

	@Before
	public void setUp() throws IOException
	{
		includedExtensions = getIncludedExtensions();
		cpqExclusion = getCPQExclusions();
		prepareTest();
	}

	protected List<Integer> getCPQExclusions()
	{
		// remove exclusions, after they have been added to the "/core/unittest/reservedTypecodes.txt" list!
		final ArrayList<Integer> cpqExclusionList = new ArrayList<>();
		return cpqExclusionList;
	}

	protected ArrayList<String> getIncludedExtensions()
	{
		return new ArrayList<>(Arrays.asList(getModuleHelper().getModuleExtensions().split(",")));
	}

	protected void prepareTest() throws IOException
	{
		final InputStream inputStream = ReservedTypecodeTest.class.getResourceAsStream("/core/unittest/reservedTypecodes.txt");
		reservedCodes = new Properties();
		try
		{
			reservedCodes.load(inputStream);
		}
		finally
		{
			inputStream.close();
		}

		for (int i = 0; i < includedExtensions.size(); i++)
		{
			final String ext = includedExtensions.get(i);
			includedExtensions.set(i, ext.trim());
		}
	}

	@Test
	public void testReservedTypecodes()
	{
		final List<String> errors = new ArrayList<String>();
		for (final ComposedType cType : getTypes())
		{
			final ItemDeployment depl = manager.getItemDeployment(manager.getJNDIName(cType.getCode()));
			final int code = cType.getItemTypeCode();
			if (depl == null || !TypeManager.getInstance().getRootComposedType(code).equals(cType))
			{
				continue;
			}

			final String reservedType = reservedCodes.getProperty(Integer.toString(code));
			if (reservedType == null)
			{

				if (cpqExclusion.contains(Integer.valueOf(code)))
				{
					LOG.warn("Type Code '" + code
							+ "' has been added to exclusion list, please make sure it is added soon to file platform/ext/core/resources/core/unittest/reservedTypecodes.txt, otherwise pipeline build will fail!");
				}
				else
				{
					errors.add("Typecode " + code + " of type " + cType.getCode()
							+ " is not listed as reserved typecode. If you have added the type new please add it to file platform/ext/core/resources/core/unittest/reservedTypecodes.txt. This will ensure that this typecode will not be used in future for other types even if your extension is not installed. This will avoid compatibility problems.");
				}
			}
			else
			{

				if (cpqExclusion.contains(Integer.valueOf(code)))
				{
					LOG.info("Type Code '" + code
							+ "' is now also added to file platform/ext/core/resources/core/unittest/reservedTypecodes.txt, so we can remove it from CPQ exclsuion list!");
				}

				if (!reservedType.equalsIgnoreCase(cType.getCode()))
				{
					if (cpqExclusion.contains(Integer.valueOf(code)))
					{
						LOG.warn("Type Code '" + code
								+ "' has been added to exclusion list, please make sure it is added soon to file platform/ext/core/resources/core/unittest/reservedTypecodes.txt, otherwise pipeline build will fail!");
					}
					else
					{
						errors.add("Reserved typecode " + code + " does not match expected type " + reservedType + " instead type "
								+ cType.getCode()
								+ " was found. This will happen if you have added a new typecode which was used in former days. Please change your typecode to a one not listed at platform/ext/core/resources/core/unittest/reservedTypecodes.txt. Otherwise you get in danger to be not compatible to former releases!!!");
					}
				}
			}
		}

		final StringBuilder errorBuilder = new StringBuilder();
		errorBuilder.append("The following type code conflicts have been found:\n");
		for (final String error : errors)
		{
			errorBuilder.append(error).append("\n");
		}
		assertTrue(errorBuilder.toString(), errors.isEmpty());
	}

	private List<ComposedType> getTypes()
	{
		final List<ComposedType> returnList = new ArrayList<ComposedType>();

		for (final ComposedType cType : TypeManager.getInstance().getAllComposedTypes())
		{
			for (final String extension : includedExtensions)
			{
				if (cType.getExtensionName().toLowerCase(Locale.getDefault()).equals(extension.toLowerCase(Locale.getDefault())))
				{
					returnList.add(cType);
					break;
				}
			}
		}

		return returnList;
	}
}
