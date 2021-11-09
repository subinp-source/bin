package de.hybris.platform.integrationbackofficetest.widgets.modals.generator;

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationbackoffice.widgets.modals.generator.DefaultIntegrationObjectImpexGenerator;
import de.hybris.platform.integrationbackoffice.widgets.modals.generator.IntegrationObjectImpexGenerator;
import de.hybris.platform.integrationbackofficetest.widgets.modals.controllers.MetadataViewerControllerIntegrationTest;
import de.hybris.platform.integrationservices.model.AbstractIntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel;
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.servicelayer.ServicelayerTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class DefaultIntegrationObjectImpexGenerationIntegrationTest extends ServicelayerTest
{
	private IntegrationObjectImpexGenerator integrationObjectImpexGenerator;

	@Before
	public void setUp()
	{
		integrationObjectImpexGenerator = new DefaultIntegrationObjectImpexGenerator();
	}

	private void setupItemTypeMatch() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"; ItemTypeMatchImpexTest; INBOUND",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]; itemTypeMatch(code)",
				"; ItemTypeMatchImpexTest	; Product       	; Product       	; 	    ; ALL_SUB_AND_SUPER_TYPES	;",
				"; ItemTypeMatchImpexTest	; CatalogVersion	; CatalogVersion	; 	    ; RESTRICT_TO_ITEM_TYPE  	;",
				"; ItemTypeMatchImpexTest	; StockLevel    	; StockLevel    	; true	; ALL_SUB_AND_SUPER_TYPES	;",
				"; ItemTypeMatchImpexTest	; Catalog       	; Catalog       	; 	    ; ALL_SUBTYPES           	;",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; ItemTypeMatchImpexTest:Product       	; catalogVersion	; Product:catalogVersion	; ItemTypeMatchImpexTest:CatalogVersion	; true	;",
				"; ItemTypeMatchImpexTest:Product       	; code          	; Product:code          	;                         	            ; true	;",
				"; ItemTypeMatchImpexTest:CatalogVersion	; catalog       	; CatalogVersion:catalog	; ItemTypeMatchImpexTest:Catalog       	; true	;",
				"; ItemTypeMatchImpexTest:CatalogVersion	; version       	; CatalogVersion:version	;                         	            ; true	;",
				"; ItemTypeMatchImpexTest:StockLevel    	; product       	; StockLevel:product    	; ItemTypeMatchImpexTest:Product       	; 	;",
				"; ItemTypeMatchImpexTest:StockLevel    	; productCode       ; StockLevel:productCode    ;                         	            ; true	;",
				"; ItemTypeMatchImpexTest:Catalog       	; id            	; Catalog:id            	;                         	            ; true	;"
		);
	}

	private void setupClassification() throws ImpExException
	{
		importImpEx(
				"$SYSTEM=Electronics",
				"$VERSION=Staged",
				"$SYSTEM_VERSION=$SYSTEM:$VERSION",
				"$catalogVersionHeader=catalogVersion(catalog(id), version)",
				"$systemVersionHeader=systemVersion(catalog(id), version)",
				"INSERT_UPDATE ClassificationSystem; id[unique = true]",
				"                                  ; $SYSTEM",
				"INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]",
				"                                         ; $SYSTEM                   ; $VERSION",
				"INSERT_UPDATE ClassificationClass; code[unique = true]; $catalogVersionHeader[unique = true]",
				"                                 ; dimensions         ; $SYSTEM_VERSION",
				"INSERT_UPDATE ClassificationAttributeUnit; $systemVersionHeader[unique = true]; code[unique = true]; symbol; unitType",
				"                                         ; $SYSTEM_VERSION                    ; centimeters        ; cm    ; measurement",
				"INSERT_UPDATE ClassificationAttribute; code[unique = true]; $systemVersionHeader[unique = true]",
				"                                     ; height             ; $SYSTEM_VERSION",
				"                                     ; width              ; $SYSTEM_VERSION",
				"                                     ; depth              ; $SYSTEM_VERSION",
				"$class=classificationClass($catalogVersionHeader, code)",
				"$attribute=classificationAttribute($systemVersionHeader, code)",
				"INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]     ; $attribute[unique = true]; unit($systemVersionHeader, code); attributeType(code)",
				"                                      ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:height   ; $SYSTEM_VERSION:centimeters     ; number",
				"                                      ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:width    ; $SYSTEM_VERSION:centimeters     ; number",
				"                                      ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:depth    ; $SYSTEM_VERSION:centimeters     ; number");

		importImpEx(
				"$IO=ProductIOClassificationTest",
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"                               ; $IO                ; INBOUND",
				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]",
				"                                   ; $IO                                   ; Product            ; Product   ; true",
				"                                   ; $IO                                   ; Catalog            ; Catalog",
				"                                   ; $IO                                   ; CatalogVersion     ; CatalogVersion",
				"$item=integrationObjectItem(integrationObject(code), code)",
				"$descriptor=attributeDescriptor(enclosingType(code), qualifier)",
				"$attributeType=returnIntegrationObjectItem(integrationObject(code), code)",
				"INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor           ; $attributeType;",
				"                                            ; $IO:Product         ; code                        ; Product:code",
				"                                            ; $IO:Product         ; catalogVersion              ; Product:catalogVersion; $IO:CatalogVersion",
				"                                            ; $IO:CatalogVersion  ; version                     ; CatalogVersion:version",
				"                                            ; $IO:CatalogVersion  ; catalog                     ; CatalogVersion:catalog; $IO:Catalog",
				"                                            ; $IO:Catalog         ; id                          ; Catalog:id",
				"$IO=ProductIOClassificationTest",
				"$SYSTEM_VERSION=Electronics:Staged",
				"$item=integrationObjectItem(integrationObject(code), code)",
				"$systemVersionHeader=systemVersion(catalog(id), version)",
				"$classificationClassHeader=classificationClass(catalogVersion(catalog(id), version), code)",
				"$classificationAttributeHeader=classificationAttribute($systemVersionHeader, code)",
				"$classificationAssignment=classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)",
				"INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment",
				"                                                          ; $IO:Product         ; height                      ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:height",
				"                                                          ; $IO:Product         ; depth                       ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:depth",
				"                                                          ; $IO:Product         ; width                       ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:width");
	}

	private void setupVirtualAttributesTest() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"; VirtualAttrImpexTest; INBOUND",
				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]; itemTypeMatch(code)",
				"; VirtualAttrImpexTest; Catalog        ; Catalog        ;      ; ;",
				"; VirtualAttrImpexTest; CatalogVersion ; CatalogVersion ;      ; ;",
				"; VirtualAttrImpexTest; Product        ; Product        ; true ; ;",
				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; VirtualAttrImpexTest:Catalog        ; id             ; Catalog:id             ;                                     ; ;",
				"; VirtualAttrImpexTest:CatalogVersion ; version        ; CatalogVersion:version ;                                     ; ;",
				"; VirtualAttrImpexTest:CatalogVersion ; catalog        ; CatalogVersion:catalog ; VirtualAttrImpexTest:Catalog        ; ;",
				"; VirtualAttrImpexTest:Product        ; code           ; Product:code           ;                                     ; ;",
				"; VirtualAttrImpexTest:Product        ; catalogVersion ; Product:catalogVersion ; VirtualAttrImpexTest:CatalogVersion ; ;",
				"; VirtualAttrImpexTest:Product        ; onlineDate     ; Product:onlineDate",
				"INSERT_UPDATE IntegrationObjectItemVirtualAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; retrievalDescriptor(code)",
				"; VirtualAttrImpexTest:Product ; virtintValueDescriptor        ; intValueDescriptor",
				"; VirtualAttrImpexTest:Product ; virtfloatValueDescriptor      ; floatValueDescriptor",
				"; VirtualAttrImpexTest:Product ; virtdoubleValueDescriptor     ; doubleValueDescriptor",
				"; VirtualAttrImpexTest:Product ; virtbooleanValueDescriptor    ; booleanValueDescriptor",
				"; VirtualAttrImpexTest:Product ; virtbyteValueDescriptor       ; byteValueDescriptor",
				"; VirtualAttrImpexTest:Product ; virtlongValueDescriptor       ; longValueDescriptor",
				"; VirtualAttrImpexTest:Product ; virtshortValueDescriptor      ; shortValueDescriptor",
				"; VirtualAttrImpexTest:Product ; virtcharValueDescriptor       ; charValueDescriptor",
				"; VirtualAttrImpexTest:Product ; virtbigDecimalValueDescriptor ; bigDecimalValueDescriptor",
				"; VirtualAttrImpexTest:Product ; formattedOnlineDate           ; formattedOnlineDate",
				"INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]; logicLocation; type(code)",
				"; booleanValueDescriptor    ; model://booleanValue    ; java.lang.Boolean",
				"; charValueDescriptor       ; model://charValue       ; java.lang.Character",
				"; byteValueDescriptor       ; model://byteValue       ; java.lang.Byte",
				"; floatValueDescriptor      ; model://floatValue      ; java.lang.Float",
				"; doubleValueDescriptor     ; model://doubleValue     ; java.lang.Double",
				"; longValueDescriptor       ; model://longValue       ; java.lang.Long",
				"; bigDecimalValueDescriptor ; model://bigDecimalValue ; java.math.BigDecimal",
				"; intValueDescriptor        ; model://intValue        ; java.lang.Integer",
				"; shortValueDescriptor      ; model://shortValue      ; java.lang.Short",
				"; formattedOnlineDate       ; model://formattedOnlineDateScript"
		);
	}

	@Test
	public void testImpexStringWithClassificationAttributes() throws ImpExException
	{
		setupClassification();
		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"ProductIOClassificationTest");
		assertEquals("ProductIOClassificationTest", integrationObjectModel.getCode());

		final String generatedImpex = integrationObjectImpexGenerator.generateImpex(integrationObjectModel);
		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("ProductIOClassificationTest"));
		integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode("ProductIOClassificationTest");
		assertNull(integrationObjectModel);

		importImpEx(generatedImpex);
		integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode("ProductIOClassificationTest");
		assertEquals("ProductIOClassificationTest", integrationObjectModel.getCode());
		assertEquals(integrationObjectModel.getItems().size(), 3);
		assertTrue(integrationObjectModel.getItems().stream().map(IntegrationObjectItemModel::getCode).collect(Collectors.toSet())
		                                 .containsAll(Arrays.asList("Product", "Catalog", "CatalogVersion")));
		Set<String> attributeSet = integrationObjectModel.getItems().stream()
		                                                 .flatMap(item -> item.getAttributes().stream())
		                                                 .map(IntegrationObjectItemAttributeModel::getAttributeName)
		                                                 .collect(Collectors.toSet());
		assertEquals(attributeSet.size(), 5);
		assertTrue(attributeSet.containsAll(Arrays.asList("code", "catalogVersion", "id", "version", "catalog")));
		assertTrue(integrationObjectModel.getItems().stream()
		                                 .filter(item -> item.getCode().equals("Product"))
		                                 .flatMap(item -> item.getClassificationAttributes().stream())
		                                 .map(AbstractIntegrationObjectItemAttributeModel::getAttributeName)
		                                 .collect(Collectors.toSet())
		                                 .containsAll(Arrays.asList("height", "depth", "width")));

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("ProductIOClassificationTest"));
	}

	@Test
	public void testImpexItemTypeMatch() throws ImpExException
	{
		setupItemTypeMatch();
		IntegrationObjectModel objectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode("ItemTypeMatchImpexTest");
		assertNotNull(objectModel);
		assertEquals(objectModel.getItems().size(), 4);
		assertEquals(IntegrationObjectTestUtil.findIntegrationObjectItemByCodeAndIntegrationObject("Product", objectModel)
		                                      .getItemTypeMatch()
		                                      .getCode(), "ALL_SUB_AND_SUPER_TYPES");
		assertEquals(IntegrationObjectTestUtil.findIntegrationObjectItemByCodeAndIntegrationObject("CatalogVersion", objectModel)
		                                      .getItemTypeMatch()
		                                      .getCode(), "RESTRICT_TO_ITEM_TYPE");
		assertEquals(IntegrationObjectTestUtil.findIntegrationObjectItemByCodeAndIntegrationObject("StockLevel", objectModel)
		                                      .getItemTypeMatch()
		                                      .getCode(), "ALL_SUB_AND_SUPER_TYPES");
		assertEquals(IntegrationObjectTestUtil.findIntegrationObjectItemByCodeAndIntegrationObject("Catalog", objectModel)
		                                      .getItemTypeMatch()
		                                      .getCode(), "ALL_SUBTYPES");

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("ItemTypeMatchImpexTest"));
	}

	@Test
	public void testImpexVirtualAttributes() throws ImpExException
	{
		setupVirtualAttributesTest();
		IntegrationObjectModel objectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode("VirtualAttrImpexTest");
		assertNotNull(objectModel);

		final String generatedImpex = integrationObjectImpexGenerator.generateImpex(objectModel);
		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class, object -> object.getCode().equals("VirtualAttrImpexTest"));
		importImpEx(generatedImpex);
		objectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode("VirtualAttrImpexTest");

		assertNotNull(objectModel);
		final Set<IntegrationObjectItemModel> items = objectModel.getItems();
		assertTrue(items.stream()
		                .map(IntegrationObjectItemModel::getCode)
		                .collect(Collectors.toSet())
		                .containsAll(Arrays.asList("Product", "Catalog", "CatalogVersion")));
		assertTrue(items.stream()
		                .flatMap(item -> item.getAttributes().stream())
		                .map(IntegrationObjectItemAttributeModel::getAttributeName)
		                .collect(Collectors.toSet())
		                .containsAll(Arrays.asList("code", "catalogVersion", "id", "version", "catalog")));

		final Map<String, String> expectedVirtualAttributes = Map.of(
				"virtintValueDescriptor", "intValueDescriptor",
				"virtfloatValueDescriptor", "floatValueDescriptor",
				"virtdoubleValueDescriptor", "doubleValueDescriptor",
				"virtbooleanValueDescriptor", "booleanValueDescriptor",
				"virtbyteValueDescriptor", "byteValueDescriptor",
				"virtlongValueDescriptor", "longValueDescriptor",
				"virtshortValueDescriptor", "shortValueDescriptor",
				"virtcharValueDescriptor", "charValueDescriptor",
				"virtbigDecimalValueDescriptor", "bigDecimalValueDescriptor",
				"formattedOnlineDate", "formattedOnlineDate"
		);
		final Map<String, String> actualVirtualAttributes =
				items.stream()
				     .flatMap(item -> item.getVirtualAttributes().stream())
				     .collect(Collectors.toSet())
				     .stream()
				     .collect(Collectors.toMap(IntegrationObjectItemVirtualAttributeModel::getAttributeName,
						     va -> va.getRetrievalDescriptor().getCode()));
		assertEquals(expectedVirtualAttributes, actualVirtualAttributes);

		final Map<String, IntegrationObjectVirtualAttributeDescriptorModel> actualDescriptors =
				items.stream()
				     .flatMap(item -> item.getVirtualAttributes().stream())
				     .collect(Collectors.toSet())
				     .stream()
				     .map(IntegrationObjectItemVirtualAttributeModel::getRetrievalDescriptor)
				     .collect(Collectors.toSet())
				     .stream()
				     .collect(Collectors.toMap(IntegrationObjectVirtualAttributeDescriptorModel::getCode,
						     descriptor -> descriptor));
		final IntegrationObjectVirtualAttributeDescriptorModel actualDesc1 = actualDescriptors.get("intValueDescriptor");
		final IntegrationObjectVirtualAttributeDescriptorModel actualDesc2 = actualDescriptors.get("floatValueDescriptor");
		final IntegrationObjectVirtualAttributeDescriptorModel actualDesc3 = actualDescriptors.get("doubleValueDescriptor");
		final IntegrationObjectVirtualAttributeDescriptorModel actualDesc4 = actualDescriptors.get("booleanValueDescriptor");
		final IntegrationObjectVirtualAttributeDescriptorModel actualDesc5 = actualDescriptors.get("byteValueDescriptor");
		final IntegrationObjectVirtualAttributeDescriptorModel actualDesc6 = actualDescriptors.get("longValueDescriptor");
		final IntegrationObjectVirtualAttributeDescriptorModel actualDesc7 = actualDescriptors.get("shortValueDescriptor");
		final IntegrationObjectVirtualAttributeDescriptorModel actualDesc8 = actualDescriptors.get("charValueDescriptor");
		final IntegrationObjectVirtualAttributeDescriptorModel actualDesc9 = actualDescriptors.get("bigDecimalValueDescriptor");
		final IntegrationObjectVirtualAttributeDescriptorModel actualDesc10 = actualDescriptors.get("formattedOnlineDate");
		assertEquals("model://intValue", actualDesc1.getLogicLocation());
		assertEquals("model://floatValue", actualDesc2.getLogicLocation());
		assertEquals("model://doubleValue", actualDesc3.getLogicLocation());
		assertEquals("model://booleanValue", actualDesc4.getLogicLocation());
		assertEquals("model://byteValue", actualDesc5.getLogicLocation());
		assertEquals("model://longValue", actualDesc6.getLogicLocation());
		assertEquals("model://shortValue", actualDesc7.getLogicLocation());
		assertEquals("model://charValue", actualDesc8.getLogicLocation());
		assertEquals("model://bigDecimalValue", actualDesc9.getLogicLocation());
		assertEquals("model://formattedOnlineDateScript", actualDesc10.getLogicLocation());
		assertEquals("java.lang.Integer", actualDesc1.getType().getCode());
		assertEquals("java.lang.Float", actualDesc2.getType().getCode());
		assertEquals("java.lang.Double", actualDesc3.getType().getCode());
		assertEquals("java.lang.Boolean", actualDesc4.getType().getCode());
		assertEquals("java.lang.Byte", actualDesc5.getType().getCode());
		assertEquals("java.lang.Long", actualDesc6.getType().getCode());
		assertEquals("java.lang.Short", actualDesc7.getType().getCode());
		assertEquals("java.lang.Character", actualDesc8.getType().getCode());
		assertEquals("java.math.BigDecimal", actualDesc9.getType().getCode());
		assertEquals("java.lang.String", actualDesc10.getType().getCode());

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class, object -> object.getCode().equals("VirtualAttrImpexTest"));
	}

	public static String loadFileAsString(final String fileLocation) throws IOException
	{
		final ClassLoader classLoader = MetadataViewerControllerIntegrationTest.class.getClassLoader();
		final URL url = classLoader.getResource(fileLocation);
		File file = null;
		if (url != null)
		{
			file = new File(url.getFile());
		}

		return Files.readString(Paths.get(file.getPath()));
	}
}
