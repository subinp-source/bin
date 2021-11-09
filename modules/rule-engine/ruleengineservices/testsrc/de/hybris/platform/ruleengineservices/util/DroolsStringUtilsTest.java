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
package de.hybris.platform.ruleengineservices.util;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.core.io.ClassPathResource;
import java.io.*;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;


@UnitTest
public class DroolsStringUtilsTest
{

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();
	private DroolsStringUtils droolsStringUtils;
	private Map<String, List<String>> testDataMap;


	@Before
	public void setUp()
	{

		droolsStringUtils = new DroolsStringUtils();
		testDataMap = new HashMap<>();
        try
		{
			final ClassPathResource classPathResource = new ClassPathResource("ruleengineservices/test/util/DrlStringUtilTestSet.txt");
			final InputStream inputStream = classPathResource.getInputStream();
			final Reader reader = new InputStreamReader(inputStream,"UTF-8");
			final BufferedReader br = new BufferedReader(reader);
			String s1 = null;
			while ((s1 = br.readLine()) != null) {
				final String strArr[] = s1.split("<->");
				final String typeKey = strArr[0];
				if (!testDataMap.containsKey(typeKey)) {
					testDataMap.put(typeKey, new ArrayList<>());
				}
				for (int i = 1; i < strArr.length; i++) {
					testDataMap.get(typeKey).add(strArr[i]);
				}

			}
			br.close();
			reader.close();
		}catch (Exception e){
         e.printStackTrace();
		}finally {

		}

	}

	@Test
	public void testValidateVariableNameFailed()
	{
		for(String s: testDataMap.get("VariableNameFail")) {
			expectedException.expect(RuntimeException.class);
			expectedException.expectMessage("Not a valid variable name for Java: " + s);
			droolsStringUtils.validateVariableName(s);
		}
	}

	@Test
	public void testValidateVariableNameOk()
	{

		for(int i=0;i<testDataMap.get("VariableNameSuccess").size();i++) {
			assertThat(testDataMap.get("VariableNameSuccessExpected").get(i)).isEqualTo(droolsStringUtils.validateVariableName(testDataMap.get("VariableNameSuccess").get(i)));
		}
	}

	@Test
	public void testValidateFQCNOk()
	{
		for(int i=0;i<testDataMap.get("FQCNSuccess").size();i++) {
			assertThat(testDataMap.get("FQCNSuccessExpected").get(i)).isEqualTo(droolsStringUtils.validateFQCN(testDataMap.get("FQCNSuccess").get(i)));
		}
	}

	@Test
	public void testValidateFQCNFailed()
	{
		for(String s: testDataMap.get("FQCNFail")) {
			expectedException.expect(RuntimeException.class);
			expectedException.expectMessage("Not a valid Java FQCN: " + s);
			droolsStringUtils.validateFQCN(s);
		}
	}

	@Test
	public void testValidateSpringBeanNameOk()
	{
		for(int i=0; i<testDataMap.get("SpringBeanNameSuccess").size();i++) {
			assertThat( testDataMap.get("SpringBeanNameSuccessExpected").get(i)).isEqualTo(droolsStringUtils.validateSpringBeanName(testDataMap.get("SpringBeanNameSuccess").get(i)));
		}
	}

	@Test
	public void testValidateSpringBeanNameFailed()
	{
		for(String s: testDataMap.get("SpringBeanNameFail")) {
			expectedException.expect(RuntimeException.class);
			expectedException.expectMessage("Not a valid spring bean name: " + s);
			droolsStringUtils.validateSpringBeanName(s);
		}
	}

	@Test
	public void testValidateIndentationOk()
	{
		for(int i=0; i<testDataMap.get("IndentationSuccess").size();i++) {
			assertThat(testDataMap.get("IndentationSuccessExpected").get(i)).isEqualTo(droolsStringUtils.validateIndentation(testDataMap.get("IndentationSuccess").get(i)));
		}
	}


	@Test
	public void testValidateIndentationFailed()
	{
		//final String indentation = "d";
		for(String s: testDataMap.get("IndentationFail")) {
			expectedException.expect(RuntimeException.class);
			expectedException.expectMessage("Indentation must not contain non-white space characters: '" + s + "'");
			droolsStringUtils.validateIndentation(s);
		}

		final String indentation1 = "";
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Indentation must not contain non-white space characters: '" + indentation1 + "'");
		droolsStringUtils.validateIndentation(indentation1);
	}

	@Test
	public void testencodeObjectToMvelStringLiteralOk()
	{
		for(int i=0;i< testDataMap.get("encodeMvelStringLiteral").size();i++) {

			assertThat(testDataMap.get("encodeMvelStringLiteralExpected").get(i)).isEqualTo(droolsStringUtils.encodeMvelStringLiteral(testDataMap.get("encodeMvelStringLiteral").get(i)));
		}
	}

}
