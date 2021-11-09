/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.document.data

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.searchservices.admin.data.SnField
import de.hybris.platform.searchservices.document.data.SnDocument

import org.junit.Test

import spock.lang.Specification


@UnitTest
public class SnDocumentSpec extends Specification {

	static final DOCUMENT_ID = "doc1"

	static final FIELD_ID1 = "field1"
	static final FIELD_ID2 = "field2"

	static final VALUE1 = "value1"
	static final VALUE2 = "value2"

	@Test
	def "Create empty document"() {
		when:
		SnDocument document = new SnDocument()

		then:
		document.getId() == null
		document.getFields() != null
		document.getFields().isEmpty()
	}

	@Test
	def "Create document"() {
		when:
		SnDocument document = new SnDocument()
		document.setId(DOCUMENT_ID)

		then:
		document.getId() == DOCUMENT_ID
		document.getFields() != null
		document.getFields().isEmpty()
	}

	@Test
	def "Add field value"() {
		given:
		SnDocument document = new SnDocument()
		document.setId(DOCUMENT_ID)

		SnField field = new SnField(id: FIELD_ID1)

		when:
		document.setFieldValue(field, VALUE1)

		then:
		document.getId() == DOCUMENT_ID
		document.getFields() != null
		document.getFields().size() == 1
		document.getFields().get(FIELD_ID1) == VALUE1
	}

	@Test
	def "Add multiple field values"() {
		given:
		SnDocument document = new SnDocument()
		document.setId(DOCUMENT_ID)

		SnField field1 = new SnField(id: FIELD_ID1)
		SnField field2 = new SnField(id: FIELD_ID2)

		when:
		document.setFieldValue(field1, VALUE1)
		document.setFieldValue(field2, VALUE2)

		then:
		document.getId() == DOCUMENT_ID
		document.getFields() != null
		document.getFields().size() == 2
		document.getFields().get(FIELD_ID1) == VALUE1
		document.getFields().get(FIELD_ID2) == VALUE2
	}

	@Test
	def "Replace field value"() {
		given:
		SnDocument document = new SnDocument()
		document.setId(DOCUMENT_ID)

		SnField field = new SnField(id: FIELD_ID1)

		document.setFieldValue(field, VALUE1)

		when:
		document.setFieldValue(field, VALUE2)

		then:
		document.getId() == DOCUMENT_ID
		document.getFields() != null
		document.getFields().size() == 1
		document.getFields().get(FIELD_ID1) == VALUE2
	}
}
