/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.editor.utility;

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.IntegrationObjectRootUtils;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.servicelayer.ServicelayerTest;

import org.junit.Test;

public class IntegrationObjectRootUtilsIntegrationTest extends ServicelayerTest
{
	private void setSingleRootTestImpex() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; SingleRootTestImpex; INBOUND ",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false] ",
				"; SingleRootTestImpex; OrgUnit; OrgUnit; true;  ",
				"; SingleRootTestImpex; Address; Address; ;  ",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false] ",
				"; SingleRootTestImpex:OrgUnit; uid           ; OrgUnit:uid           ;                  ; true;  ",
				"; SingleRootTestImpex:OrgUnit; contactAddress; OrgUnit:contactAddress; SingleRootTestImpex:Address; ;  ",
				"; SingleRootTestImpex:Address; fax           ; Address:fax           ;                  ; ;  ",
				"; SingleRootTestImpex:Address; company       ; Address:company       ;                  ; ;  ",
				"; SingleRootTestImpex:Address; cellphone     ; Address:cellphone     ;                  ; ;  ",
				"; SingleRootTestImpex:Address; email         ; Address:email         ;                  ; true;  "
		);
	}

	private void setNoRootTestImpex() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"; NoRootTestImpex; INBOUND",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]",
				"; NoRootTestImpex; Product       ; Product       ;   ;",
				"; NoRootTestImpex; Category      ; Category      ; ;",
				"; NoRootTestImpex; Catalog       ; Catalog       ; ;",
				"; NoRootTestImpex; CatalogVersion; CatalogVersion; ;",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; NoRootTestImpex:Product       ; code          ; Product:code          ;                                        ; ;",
				"; NoRootTestImpex:Product       ; catalogVersion; Product:catalogVersion; NoRootTestImpex:CatalogVersion; ;",
				"; NoRootTestImpex:Category      ; code          ; Category:code         ;                                        ; true;",
				"; NoRootTestImpex:Category      ; name          ; Category:name         ;                                        ; ;",
				"; NoRootTestImpex:Category      ; products      ; Category:products     ; NoRootTestImpex:Product       ; ;",
				"; NoRootTestImpex:Catalog       ; id            ; Catalog:id            ;                                        ; ;",
				"; NoRootTestImpex:CatalogVersion; catalog       ; CatalogVersion:catalog; NoRootTestImpex:Catalog       ; ;",
				"; NoRootTestImpex:CatalogVersion; version       ; CatalogVersion:version;                                        ; ;",
				"; NoRootTestImpex:CatalogVersion; active        ; CatalogVersion:active ;                                        ; ;"
		);
	}

	private void setMultiRootTestImpex() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"; MultiRootTestImpex; INBOUND",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]",
				"; MultiRootTestImpex; Product       ; Product       ;   ;",
				"; MultiRootTestImpex; Category      ; Category      ; true;",
				"; MultiRootTestImpex; Catalog       ; Catalog       ; ;",
				"; MultiRootTestImpex; CatalogVersion; CatalogVersion; ;",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; MultiRootTestImpex:Product       ; code          ; Product:code          ;                                        ; ;",
				"; MultiRootTestImpex:Product       ; catalogVersion; Product:catalogVersion; MultiRootTestImpex:CatalogVersion; ;",
				"; MultiRootTestImpex:Category      ; code          ; Category:code         ;                                        ; true;",
				"; MultiRootTestImpex:Category      ; name          ; Category:name         ;                                        ; ;",
				"; MultiRootTestImpex:Category      ; products      ; Category:products     ; MultiRootTestImpex:Product       ; ;",
				"; MultiRootTestImpex:Catalog       ; id            ; Catalog:id            ;                                        ; ;",
				"; MultiRootTestImpex:CatalogVersion; catalog       ; CatalogVersion:catalog; MultiRootTestImpex:Catalog       ; ;",
				"; MultiRootTestImpex:CatalogVersion; version       ; CatalogVersion:version;                                        ; ;",
				"; MultiRootTestImpex:CatalogVersion; active        ; CatalogVersion:active ;                                        ; ;"
		);
	}

	private void setSingleRootCircularDepTestImpex() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"; SingleRootCircularDepTestImpex; INBOUND",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]",
				"; SingleRootCircularDepTestImpex; OrderEntry; OrderEntry; ;",
				"; SingleRootCircularDepTestImpex; Order     ; Order     ; true;",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; SingleRootCircularDepTestImpex:OrderEntry; order  ; OrderEntry:order; SingleRootCircularDepTestImpex:Order     ; true;",
				"; SingleRootCircularDepTestImpex:Order     ; code   ; Order:code      ;                 ; true;",
				"; SingleRootCircularDepTestImpex:Order     ; entries; Order:entries   ; SingleRootCircularDepTestImpex:OrderEntry; ;"
		);
	}

	@Test
	public void getIntegrationObjectSingleBooleanRoot() throws ImpExException
	{
		setSingleRootTestImpex();
		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"SingleRootTestImpex");
		assertNotNull(integrationObjectModel);

		final String expectedRoot = "OrgUnit";
		final String actualRoot = IntegrationObjectRootUtils.resolveIntegrationObjectRoot(integrationObjectModel)
		                                                    .getRootItem()
		                                                    .getCode();

		assertEquals("SingleRootTestImpex", integrationObjectModel.getCode());
		assertEquals(expectedRoot, actualRoot);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("SingleRootTestImpex"));

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class, object -> object.getCode().equals("SingleRootTestImpex"));
	}

	@Test
	public void getIntegrationObjectNoBooleanRoot() throws ImpExException
	{
		setNoRootTestImpex();
		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"NoRootTestImpex");
		assertNotNull(integrationObjectModel);

		final String expectedRoot = "Category";
		final String actualRoot = IntegrationObjectRootUtils.resolveIntegrationObjectRoot(integrationObjectModel)
		                                                    .getRootItem()
		                                                    .getCode();

		assertEquals("NoRootTestImpex", integrationObjectModel.getCode());
		assertEquals(expectedRoot, actualRoot);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class, object -> object.getCode().equals("NoRootTestImpex"));
	}

	@Test
	public void getIntegrationObjectMultiBooleanRoot() throws ImpExException
	{
		setMultiRootTestImpex();
		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"MultiRootTestImpex");
		assertNotNull(integrationObjectModel);

		integrationObjectModel.getItems().forEach(item -> {
			if (item.getCode().equals("Product"))
			{
				item.setRoot(true);
			}
		});

		final String expectedRoot = "Category";
		final String actualRoot = IntegrationObjectRootUtils.resolveIntegrationObjectRoot(integrationObjectModel)
		                                                    .getRootItem()
		                                                    .getCode();

		assertEquals("MultiRootTestImpex", integrationObjectModel.getCode());
		assertEquals(expectedRoot, actualRoot);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("MultiRootTestImpex"));
	}

	@Test
	public void getIntegrationObjectSingleBooleanRootCircularDep() throws ImpExException
	{
		setSingleRootCircularDepTestImpex();
		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"SingleRootCircularDepTestImpex");
		assertNotNull(integrationObjectModel);

		final String expectedRoot = "Order";
		final String actualRoot = IntegrationObjectRootUtils.resolveIntegrationObjectRoot(integrationObjectModel)
		                                                    .getRootItem()
		                                                    .getCode();

		assertEquals("SingleRootCircularDepTestImpex", integrationObjectModel.getCode());
		assertEquals(expectedRoot, actualRoot);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("SingleRootCircularDepTestImpex"));
	}
}
