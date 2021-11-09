/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.admin.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.admin.data.SnFieldTypeInfo
import de.hybris.platform.searchservices.admin.service.SnFieldTypeRegistry
import de.hybris.platform.searchservices.enums.SnFieldType
import de.hybris.platform.searchservices.integration.admin.AbstractSnAdminSpec

import java.util.stream.Collectors

import javax.annotation.Resource

import org.junit.Test


@IntegrationTest
public class SnFieldTypeRegistrySpec extends AbstractSnAdminSpec {

	@Resource
	SnFieldTypeRegistry snFieldTypeRegistry

	@Test
	def "Field type info configured for all field types"() {
		when:
		List<SnFieldTypeInfo> fieldtypeInfos = SnFieldType.values().stream().map(snFieldTypeRegistry.&getFieldTypeInfo)
			.collect(Collectors.toList())

		then:
		fieldtypeInfos.every {
			it != null
			it.fieldType != null
			it.valueType != null
			it.supportedQueryTypes != null
		}
	}
}
