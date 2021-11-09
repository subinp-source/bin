/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2webservices.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.junit.Test
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

@UnitTest
class HttpServletRequestToODataContextConverterUnitTest extends Specification
{

	def requestConverter = Stub(Converter)
	def oDataContextGenerator = Stub(ODataContextGenerator)

	def converter = new HttpServletRequestToODataContextConverter()

	def setup()
	{
		converter.setODataContextGenerator(oDataContextGenerator)
		converter.setRequestConverter(requestConverter)
	}

	@Test
	def "convert successful"()
	{
		given:
		def context = Mock(ODataContext)
		requestConverter.convert(_) >> Mock(ODataRequest)
		oDataContextGenerator.generate(_) >> context

		expect:
		converter.convert(Mock(HttpServletRequest)) == context
	}
}
