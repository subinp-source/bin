package de.hybris.platform.integrationbackofficetest.widgets.modals.generator;

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.modals.generator.DefaultIntegrationObjectJsonGenerator;
import de.hybris.platform.integrationservices.config.ReadOnlyAttributesConfiguration;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class DefaultIntegrationObjectJsonGeneratorIntegrationTest extends ServicelayerTest
{
	@Resource
	private ReadOnlyAttributesConfiguration defaultIntegrationServicesConfiguration;
	@Resource
	private TypeService typeService;
	private DefaultIntegrationObjectJsonGenerator jsonGenerator;
	private Gson gson;

	@Before
	public void setUp()
	{
		final GenericApplicationContext applicationContext = (GenericApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

		final AbstractBeanDefinition validationDefinition = BeanDefinitionBuilder.rootBeanDefinition(ReadService.class)
		                                                                         .getBeanDefinition();
		beanFactory.registerBeanDefinition("readService", validationDefinition);

		final ReadService readService = (ReadService) Registry.getApplicationContext().getBean("readService");
		readService.setTypeService(typeService);
		jsonGenerator = new DefaultIntegrationObjectJsonGenerator(readService, defaultIntegrationServicesConfiguration);
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	private void setBasicJSONTest() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; BasicJSONTest; INBOUND ",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false] ",
				"; BasicJSONTest; CatalogVersion; CatalogVersion; ;",
				"; BasicJSONTest; Product       ; Product       ; true;",
				"; BasicJSONTest; Catalog       ; Catalog       ; ;",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; BasicJSONTest:CatalogVersion; active            ; CatalogVersion:active     ;                             ; ;",
				"; BasicJSONTest:CatalogVersion; catalog           ; CatalogVersion:catalog    ; BasicJSONTest:Catalog       ; true;",
				"; BasicJSONTest:CatalogVersion; version           ; CatalogVersion:version    ;                             ; true;",
				"; BasicJSONTest:Product       ; manufacturerAID   ; Product:manufacturerAID   ;                             ; ;",
				"; BasicJSONTest:Product       ; numberContentUnits; Product:numberContentUnits;                             ; ;",
				"; BasicJSONTest:Product       ; catalogVersion    ; Product:catalogVersion    ; BasicJSONTest:CatalogVersion; true;",
				"; BasicJSONTest:Product       ; offlineDate       ; Product:offlineDate       ;                             ; ;",
				"; BasicJSONTest:Product       ; code              ; Product:code              ;                             ; true;",
				"; BasicJSONTest:Product       ; numberOfReviews   ; Product:numberOfReviews   ;                             ; ;",
				"; BasicJSONTest:Catalog       ; id                ; Catalog:id                ;                             ; true;"
		);
	}

	private void setMapJSONTest() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; MapJSONTest; INBOUND ",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false] ",
				"; MapJSONTest; Catalog       ; Catalog       ; ;  ",
				"; MapJSONTest; CatalogVersion; CatalogVersion; ;  ",
				"; MapJSONTest; Product       ; Product       ; true;  ",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false] ",
				"; MapJSONTest:Catalog       ; id            ; Catalog:id            ;                        ; true;  ",
				"; MapJSONTest:CatalogVersion; catalog       ; CatalogVersion:catalog; MapJSONTest:Catalog       ; true;  ",
				"; MapJSONTest:CatalogVersion; version       ; CatalogVersion:version;                        ; true;  ",
				"; MapJSONTest:Product       ; catalogVersion; Product:catalogVersion; MapJSONTest:CatalogVersion; true;  ",
				"; MapJSONTest:Product       ; code          ; Product:code          ;                        ; true;  ",
				"; MapJSONTest:Product       ; description   ; Product:description   ;                        ; ;  ",
				"; MapJSONTest:Product       ; deliveryTime  ; Product:deliveryTime  ;                        ; ;  "
		);
	}

	private void setCollectionJSONTest() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; CollectionJSONTest; INBOUND ",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false] ",
				"; CollectionJSONTest; DeliveryMode  ; DeliveryMode  ; ;  ",
				"; CollectionJSONTest; CatalogVersion; CatalogVersion; ;  ",
				"; CollectionJSONTest; Media         ; Media         ; ;  ",
				"; CollectionJSONTest; Catalog       ; Catalog       ; ;  ",
				"; CollectionJSONTest; Product       ; Product       ; true;  ",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false] ",
				"; CollectionJSONTest:DeliveryMode  ; code          ; DeliveryMode:code     ;                                  ; true;  ",
				"; CollectionJSONTest:CatalogVersion; version       ; CatalogVersion:version;                                  ; true;  ",
				"; CollectionJSONTest:CatalogVersion; catalog       ; CatalogVersion:catalog; CollectionJSONTest:Catalog       ; true;  ",
				"; CollectionJSONTest:Media         ; catalogVersion; Media:catalogVersion  ; CollectionJSONTest:CatalogVersion; true;  ",
				"; CollectionJSONTest:Media         ; code          ; Media:code            ;                                  ; true;  ",
				"; CollectionJSONTest:Catalog       ; id            ; Catalog:id            ;                                  ; true;  ",
				"; CollectionJSONTest:Product       ; code          ; Product:code          ;                                  ; true;  ",
				"; CollectionJSONTest:Product       ; catalogVersion; Product:catalogVersion; CollectionJSONTest:CatalogVersion; true;  ",
				"; CollectionJSONTest:Product       ; data_sheet    ; Product:data_sheet    ; CollectionJSONTest:Media         ; ;  ",
				"; CollectionJSONTest:Product       ; deliveryTime  ; Product:deliveryTime  ;                                  ; ;  ",
				"; CollectionJSONTest:Product       ; deliveryModes ; Product:deliveryModes ; CollectionJSONTest:DeliveryMode  ; ;  "
		);
	}

	private void setMapNotSupportedJSONTest() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; MapNotSupportedJSONTest; INBOUND",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false] ",
				"; MapNotSupportedJSONTest; Product       ; Product       ; true;  ",
				"; MapNotSupportedJSONTest; Catalog       ; Catalog       ; ;  ",
				"; MapNotSupportedJSONTest; CatalogVersion; CatalogVersion; ;",
				"; MapNotSupportedJSONTest; ArticleStatus ; ArticleStatus ; ;",

				"INSERT_UPDATE IntegrationObjectItemAttribute[disable.interceptor.types=validate]; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; MapNotSupportedJSONTest:Product       ; articleStatus ; Product:articleStatus        ; MapNotSupportedJSONTest:ArticleStatus; ;",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; MapNotSupportedJSONTest:Product       ; code          ; Product:code                 ;                        ; true;  ",
				"; MapNotSupportedJSONTest:Product       ; catalogVersion; Product:catalogVersion       ; MapNotSupportedJSONTest:CatalogVersion; true;  ",
				"; MapNotSupportedJSONTest:Product       ; averageRating ; Product:averageRating        ;                        ; ;  ",
				"; MapNotSupportedJSONTest:Catalog       ; id            ; Catalog:id                   ;                        ; true;  ",
				"; MapNotSupportedJSONTest:CatalogVersion; generationDate; CatalogVersion:generationDate;                        ; ;  ",
				"; MapNotSupportedJSONTest:CatalogVersion; version       ; CatalogVersion:version       ;                        ; true;  ",
				"; MapNotSupportedJSONTest:CatalogVersion; generatorInfo ; CatalogVersion:generatorInfo ;                        ; ;  ",
				"; MapNotSupportedJSONTest:CatalogVersion; inclAssurance ; CatalogVersion:inclAssurance ;                        ; ;  ",
				"; MapNotSupportedJSONTest:CatalogVersion; catalog       ; CatalogVersion:catalog       ; MapNotSupportedJSONTest:Catalog       ; true;",
				"; MapNotSupportedJSONTest:ArticleStatus ; code          ; ArticleStatus:code           ;                        ; true;"
		);
	}

	private void setCircularDependencyJSONTest() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; CircularDependencyJSONTest; INBOUND ",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false] ",
				"; CircularDependencyJSONTest; OrderEntry; OrderEntry; ;  ",
				"; CircularDependencyJSONTest; Order     ; Order     ; true;  ",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false] ",
				"; CircularDependencyJSONTest:OrderEntry; order  ; OrderEntry:order; CircularDependencyJSONTest:Order     ; true;  ",
				"; CircularDependencyJSONTest:Order     ; entries; Order:entries   ; CircularDependencyJSONTest:OrderEntry; ;  ",
				"; CircularDependencyJSONTest:Order     ; code   ; Order:code      ;                  ; true;  "
		);
	}

	private void setCircularDependencyComplexJSONTest() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; CircularDependencyComplexJSONTest; INBOUND ",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false] ",
				"; CircularDependencyComplexJSONTest; Order     ; Order     ; true;  ",
				"; CircularDependencyComplexJSONTest; User      ; User      ; ;  ",
				"; CircularDependencyComplexJSONTest; OrderEntry; OrderEntry; ;  ",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false] ",
				"; CircularDependencyComplexJSONTest:Order     ; entries        ; Order:entries        ; CircularDependencyComplexJSONTest:OrderEntry; ;  ",
				"; CircularDependencyComplexJSONTest:Order     ; user           ; Order:user           ; CircularDependencyComplexJSONTest:User      ; true;  ",
				"; CircularDependencyComplexJSONTest:Order     ; originalVersion; Order:originalVersion; CircularDependencyComplexJSONTest:Order     ; ;  ",
				"; CircularDependencyComplexJSONTest:Order     ; placedBy       ; Order:placedBy       ; CircularDependencyComplexJSONTest:User      ; ;  ",
				"; CircularDependencyComplexJSONTest:User      ; uid            ; User:uid             ;                  ; true;  ",
				"; CircularDependencyComplexJSONTest:OrderEntry; order          ; OrderEntry:order     ; CircularDependencyComplexJSONTest:Order     ; true;  "
		);
	}

	private void setProductIOClassificationTest() throws ImpExException
	{
		importImpEx(
				"# The following ImpEx provides a full example of",
				"# 1) Creating classifications",
				"# 2) Creating Integration Object with classification attributes",
				"# ***************************************************************************************",
				"#     Set up the classifications",
				"# ***************************************************************************************",
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
				"                                      ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:depth    ; $SYSTEM_VERSION:centimeters     ; number",
				"# ***************************************************************************************",
				"#     Set up integration objects with regular attributes and classification attributes",
				"# ***************************************************************************************",
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
				"                                                          ; $IO:Product         ; width                       ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:width"
		);
	}

	private void setProductIOClassificationEnumReferenceAndMultivalued() throws ImpExException
	{
		importImpEx(
				"# The following ImpEx provides a full example of",
				"# 1) Creating classifications",
				"# 2) Creating Integration Object with classification attributes",
				"# ***************************************************************************************",
				"#     Set up the classifications",
				"# ***************************************************************************************",
				"$SYSTEM=Electronics",
				"$VERSION=Staged",
				"$SYSTEM_VERSION=$SYSTEM:$VERSION",
				"$catalogVersionHeader=catalogVersion(catalog(id), version)",
				"$systemVersionHeader=systemVersion(catalog(id), version)",
				"INSERT_UPDATE ClassificationSystem; id[unique = true]",
				"                                  ; $SYSTEM",
				"                                  ; Alternative",
				"INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]",
				"                                         ; $SYSTEM                   ; $VERSION",
				"                                         ; Alternative               ; Products",
				"INSERT_UPDATE ClassificationClass; code[unique = true]; $catalogVersionHeader[unique = true]",
				"                                 ; dimensions         ; $SYSTEM_VERSION",
				"                                 ; QA                 ; $SYSTEM_VERSION",
				"                                 ; alternativeProduct ; Alternative:Products",


				"INSERT_UPDATE ClassificationAttributeUnit; $systemVersionHeader[unique = true]; code[unique = true]; symbol; unitType",
				"                                         ; $SYSTEM_VERSION                    ; centimeters        ; cm    ; measurement",
				"INSERT_UPDATE ClassificationAttribute; code[unique = true]; $systemVersionHeader[unique = true]",
				"                                     ;depth               ; $SYSTEM_VERSION",
				"                                     ;bool               ; $SYSTEM_VERSION",
				"                                     ;valueList           ; $SYSTEM_VERSION",
				"                                     ;ReferenceTypeM      ; $SYSTEM_VERSION",
				"                                     ;tester              ; $SYSTEM_VERSION",
				"                                     ;dateM              ; $SYSTEM_VERSION",
				"                                     ;valueListM         ; $SYSTEM_VERSION",
				"                                     ;stringType          ; $SYSTEM_VERSION",
				"                                     ;numberM             ; $SYSTEM_VERSION",
				"                                     ;stringTypeM         ; $SYSTEM_VERSION",
				"                                     ;date                ; $SYSTEM_VERSION",
				"                                     ;classificationName  ; Alternative:Products",
				"                                     ;boolM              ; $SYSTEM_VERSION",

				"$class=classificationClass($catalogVersionHeader, code)",
				"$attribute=classificationAttribute($systemVersionHeader, code)",
				"INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]     ; $attribute[unique = true]; unit($systemVersionHeader, code); attributeType(code);multiValued ; referenceType(code)",
				"                                         ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:dateM    ;      ; date;true",
				"                                         ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:stringTypeM   ;      ; string;true",
				"                                         ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:numberM    ;      ; number;true",
				"                                         ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:boolM    ;      ; boolean;true",
				"                                         ; $SYSTEM_VERSION:QA; $SYSTEM_VERSION:tester    ;      ; reference ; false ; Employee",
				"                                         ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:valueListM    ;      ; enum;true",
				"                                         ; $SYSTEM_VERSION:QA; $SYSTEM_VERSION:ReferenceTypeM    ;      ; reference;true;Employee",
				"                                         ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:depth    ; $SYSTEM_VERSION:centimeters     ; number;false ;",
				"                                         ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:bool    ;      ; boolean; false ;",
				"                                         ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:valueList    ;      ; enum; false ;",
				"                                         ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:stringType    ;      ; string; false ;",
				"                                         ; $SYSTEM_VERSION:dimensions; $SYSTEM_VERSION:date    ;      ; date; false;",
				"                                         ; Alternative:Products:alternativeProduct;Alternative:Products:classificationName; ;string; false ;",

				"$IO=ProductIOClassificationEnumReferenceAndMultivalued",
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"; $IO; INBOUND",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]",
				"; $IO ; Catalog         ; Catalog         ;   ;",
				"; $IO ; Product         ; Product         ; true  ;",
				"; $IO ; Employee        ; Employee        ;   ;",
				"; $IO ; CatalogVersion  ; CatalogVersion  ;   ;",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; $IO:Catalog         ; id              ; Catalog:id              ;                                   ; true  ;",
				"; $IO:Product         ; code            ; Product:code            ;                                   ; true  ;",
				"; $IO:Product         ; catalogVersion  ; Product:catalogVersion  ; $IO:CatalogVersion  ; true  ;",
				"; $IO:Employee        ; uid             ; Employee:uid            ;                                   ; true  ;",
				"; $IO:CatalogVersion  ; catalog         ; CatalogVersion:catalog  ; $IO:Catalog         ; true  ;",
				"; $IO:CatalogVersion  ; version         ; CatalogVersion:version  ;                                   ; true  ;",

				"$SYSTEM_VERSION=Electronics:Staged",
				"$item=integrationObjectItem(integrationObject(code), code)",
				"$systemVersionHeader=systemVersion(catalog(id), version)",
				"$classificationClassHeader=classificationClass(catalogVersion(catalog(id), version), code)",
				"$classificationAttributeHeader=classificationAttribute($systemVersionHeader, code)",
				"$classificationAssignment=classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)",
				"INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment; returnIntegrationObjectItem(integrationObject(code), code)",
				"; $IO:Product         ; bool                ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:bool",
				"; $IO:Product         ; valueList           ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:valueList",
				"; $IO:Product         ; ReferenceTypeM      ; $SYSTEM_VERSION:QA:$SYSTEM_VERSION:ReferenceTypeM;$IO:Employee",
				"; $IO:Product         ; depth               ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:depth",
				"; $IO:Product         ; tester             ; $SYSTEM_VERSION:QA:$SYSTEM_VERSION:tester; $IO:Employee",
				"; $IO:Product         ; dateM               ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:dateM",
				"; $IO:Product         ; valueListM          ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:valueListM",
				"; $IO:Product         ; stringType          ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:stringType",
				"; $IO:Product         ; numberM             ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:numberM",
				"; $IO:Product         ; stringTypeM         ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:stringTypeM",
				"; $IO:Product         ; date                ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:date",
				"; $IO:Product         ; boolM               ; $SYSTEM_VERSION:dimensions:$SYSTEM_VERSION:boolM",
				"; $IO:Product         ; classificationName  ; Alternative:Products:alternativeProduct:Alternative:Products:classificationName"
		);
	}

	private void setReadOnlyJSONTest() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"; ReadOnlyJSONTest; INBOUND",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]",
				"; ReadOnlyJSONTest; Catalog       ; Catalog       ; ;",
				"; ReadOnlyJSONTest; Product       ; Product       ; true;",
				"; ReadOnlyJSONTest; CatalogVersion; CatalogVersion; ;",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; ReadOnlyJSONTest:Catalog       ; id            ; Catalog:id            ;                     ; true;",
				"; ReadOnlyJSONTest:Product       ; creationtime  ; Product:creationtime  ;                     ; ;",
				"; ReadOnlyJSONTest:Product       ; code          ; Product:code          ;                     ; true;",
				"; ReadOnlyJSONTest:Product       ; catalogVersion; Product:catalogVersion; ReadOnlyJSONTest:CatalogVersion; true;",
				"; ReadOnlyJSONTest:Product       ; modifiedtime  ; Product:modifiedtime  ;                     ; ;",
				"; ReadOnlyJSONTest:CatalogVersion; version       ; CatalogVersion:version;                     ; true;",
				"; ReadOnlyJSONTest:CatalogVersion; catalog       ; CatalogVersion:catalog; ReadOnlyJSONTest:Catalog       ; true;"
		);
	}

	@Test
	public void basicJsonTest() throws FileNotFoundException, ImpExException
	{
		setBasicJSONTest();
		final Map<String, Object> jsonObject = loadPayload("test/json/BasicJSONTestExpected.json");
		jsonObject.put("numberOfReviews", 123); //to fix Integer formatting
		final String expectedJsonString = gson.toJson(jsonObject);

		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode("BasicJSONTest");
		assertNotNull(integrationObjectModel);
		assertEquals("BasicJSONTest", integrationObjectModel.getCode());

		final String actualJsonString = jsonGenerator.generateJson(integrationObjectModel);
		assertEquals(expectedJsonString, actualJsonString);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class, object -> object.getCode().equals("BasicJSONTest"));
	}

	@Test
	public void mapJsonTest() throws FileNotFoundException, ImpExException
	{
		setMapJSONTest();
		final Map<String, Object> jsonObject = loadPayload("test/json/MapJSONTestExpected.json");
		final String expectedJsonString = gson.toJson(jsonObject);

		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode("MapJSONTest");
		assertNotNull(integrationObjectModel);
		assertEquals("MapJSONTest", integrationObjectModel.getCode());

		final String actualJsonString = jsonGenerator.generateJson(integrationObjectModel);
		assertEquals(expectedJsonString, actualJsonString);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class, object -> object.getCode().equals("MapJSONTest"));
	}

	@Test
	public void collectionJsonTest() throws FileNotFoundException, ImpExException
	{
		setCollectionJSONTest();
		final Map<String, Object> jsonObject = loadPayload("test/json/CollectionJSONTestExpected.json");
		final String expectedJsonString = gson.toJson(jsonObject);

		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"CollectionJSONTest");
		assertNotNull(integrationObjectModel);
		assertEquals("CollectionJSONTest", integrationObjectModel.getCode());

		final String actualJsonString = jsonGenerator.generateJson(integrationObjectModel);

		assertEquals(expectedJsonString, actualJsonString);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class, object -> object.getCode().equals("CollectionJSONTest"));
	}

	@Test
	public void mapOfMapsNotSupportedJsonTest() throws FileNotFoundException, ImpExException
	{
		setMapNotSupportedJSONTest();
		final Map<String, Object> jsonObject = loadPayload("test/json/MapNotSupportedJSONTestExpected.json");
		final String expectedJsonString = gson.toJson(jsonObject);

		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"MapNotSupportedJSONTest");
		assertNotNull(integrationObjectModel);

		assertEquals("MapNotSupportedJSONTest", integrationObjectModel.getCode());

		final String actualJsonString = jsonGenerator.generateJson(integrationObjectModel);

		assertEquals(expectedJsonString, actualJsonString);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("MapNotSupportedJSONTest"));
	}

	@Test
	public void circularDependencyJsonTest() throws FileNotFoundException, ImpExException
	{
		setCircularDependencyJSONTest();
		final Map<String, Object> jsonObject = loadPayload("test/json/CircularDependencyJSONTestExpected.json");
		final String expectedJsonString = gson.toJson(jsonObject);

		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"CircularDependencyJSONTest");
		assertNotNull(integrationObjectModel);

		assertEquals("CircularDependencyJSONTest", integrationObjectModel.getCode());

		final String actualJsonString = jsonGenerator.generateJson(integrationObjectModel);

		assertEquals(expectedJsonString, actualJsonString);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("CircularDependencyJSONTest"));
	}

	@Test
	public void circularDependencyComplexJsonTest() throws FileNotFoundException, ImpExException
	{
		setCircularDependencyComplexJSONTest();
		final Map<String, Object> jsonObject = loadPayload("test/json/CircularDependencyComplexJSONTestExpected.json");
		final String expectedJsonString = gson.toJson(jsonObject);

		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"CircularDependencyComplexJSONTest");
		assertNotNull(integrationObjectModel);

		assertEquals("CircularDependencyComplexJSONTest", integrationObjectModel.getCode());

		final String actualJsonString = jsonGenerator.generateJson(integrationObjectModel);

		assertEquals(expectedJsonString, actualJsonString);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("CircularDependencyComplexJSONTest"));
	}

	@Test
	public void classificationJsonTest() throws FileNotFoundException, ImpExException
	{
		setProductIOClassificationTest();
		final Map<String, Object> jsonObject = loadPayload("test/json/ProductIOClassificationJSONTestExpected.json");
		final String expectedJsonString = gson.toJson(jsonObject);

		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"ProductIOClassificationTest");
		assertNotNull(integrationObjectModel);

		assertEquals("ProductIOClassificationTest", integrationObjectModel.getCode());

		final String actualJsonString = jsonGenerator.generateJson(integrationObjectModel);

		assertEquals(expectedJsonString, actualJsonString);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("ProductIOClassificationTest"));
	}

	@Test
	public void classificationCollectionEnumReferenceJsonTest() throws FileNotFoundException, ImpExException
	{
		setProductIOClassificationEnumReferenceAndMultivalued();
		final Map<String, Object> jsonObject = loadPayload("test/json/ProductIOClassificationEnumReferenceAndMultivalued.json");
		final String expectedJsonString = gson.toJson(jsonObject);

		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"ProductIOClassificationEnumReferenceAndMultivalued");
		assertNotNull(integrationObjectModel);

		assertEquals("ProductIOClassificationEnumReferenceAndMultivalued", integrationObjectModel.getCode());

		final String actualJsonString = jsonGenerator.generateJson(integrationObjectModel);

		assertEquals(expectedJsonString, actualJsonString);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("ProductIOClassificationEnumReferenceAndMultivalued"));
	}

	@Test
	public void readOnlyJsonTest() throws FileNotFoundException, ImpExException
	{
		setReadOnlyJSONTest();
		final Map<String, Object> jsonObject = loadPayload("test/json/ReadOnlyJSONTestExpected.json");
		final String expectedJsonString = gson.toJson(jsonObject);

		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode("ReadOnlyJSONTest");
		assertNotNull(integrationObjectModel);

		assertEquals("ReadOnlyJSONTest", integrationObjectModel.getCode());

		final String actualJsonString = jsonGenerator.generateJson(integrationObjectModel);

		assertEquals(expectedJsonString, actualJsonString);

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class, object -> object.getCode().equals("ReadOnlyJSONTest"));
	}

	public static Map<String, Object> loadPayload(final String payloadLocation) throws FileNotFoundException
	{
		final ClassLoader classLoader = DefaultIntegrationObjectJsonGeneratorIntegrationTest.class.getClassLoader();
		final URL url = classLoader.getResource(payloadLocation);
		if (url != null)
		{
			final File file = new File(url.getFile());
			final Gson gson = new Gson();
			final JsonReader reader = new JsonReader(new FileReader(file));
			return gson.fromJson(reader, Map.class);
		}
		return null;
	}
}
