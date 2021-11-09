/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.editor.utility;

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.core.model.type.TypeModel
import de.hybris.platform.integrationbackoffice.services.ReadService
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.DefaultEditorAttributesFilteringService
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorBlacklists
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAttributesFilteringService
import de.hybris.platform.integrationservices.config.ReadOnlyAttributesConfiguration
import de.hybris.platform.servicelayer.type.TypeService
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.reflect.Field
import java.lang.reflect.Modifier

@UnitTest
class EditorAttributesFilteringServiceSpockUnitTest extends Specification {
    //Set up services
    def typeServiceStub = Stub(TypeService)
    def readOnlyAttributes = Stub(ReadOnlyAttributesConfiguration)
    def readService = Spy(ReadService)
    def editorAttributesFilteringService = new DefaultEditorAttributesFilteringService(readService);

    //Setting up valid attribute types
    @Shared
    def collectionType = Stub(TypeModel) {
        getItemtype() >> "CollectionType"
    }

    @Shared
    def composedType = Stub(TypeModel) {
        getItemtype() >> "ComposedType"
    }

    @Shared
    def enumeratedType = Stub(TypeModel) {
        getItemtype() >> "EnumerationMetaType"
    }

    @Shared
    def atomicType = Stub(TypeModel) {
        getItemtype() >> "AtomicType"
    }

    @Shared
    def mapType = Stub(TypeModel) {
        getItemtype() >> "MapType"
    }

    @Shared
    def invalidType = Stub(TypeModel) {
        getItemtype() >> "InvalidType"
    }

    //Setting up attributes
    @Shared
    def collectionAttr = Stub(AttributeDescriptorModel) {
        getAttributeType() >> collectionType
        getItemtype() >> "ParentCollectionType"
        getQualifier() >> "collectionQualifier"
    }

    @Shared
    def composedAttr = Stub(AttributeDescriptorModel) {
        getAttributeType() >> composedType
        getItemtype() >> "ParentComposedType"
        getQualifier() >> "composedQualifier"
    }

    @Shared
    def enumeratedAttr = Stub(AttributeDescriptorModel) {
        getAttributeType() >> enumeratedType
        getItemtype() >> "ParentEnumerationMetaType"
        getQualifier() >> "enumerationMetaTypeQualifier"
    }

    @Shared
    def atomicAttr = Stub(AttributeDescriptorModel) {
        getAttributeType() >> atomicType
        getItemtype() >> "ParentAtomicType"
        getQualifier() >> "atomicQualifier"
    }

    @Shared
    def mapAttr = Stub(AttributeDescriptorModel) {
        getAttributeType() >> mapType
        getItemtype() >> "ParentMapType"
        getQualifier() >> "mapQualifier"
    }

    @Shared
    def invalidAttr = Stub(AttributeDescriptorModel) {
        getAttributeType() >> invalidType
        getItemtype() >> "ParentInvalidType"
        getQualifier() >> "invalidQualifier"
    }


    //Creating list of valid attributes
    @Shared
    Set<AttributeDescriptorModel> validAttributesList = new HashSet<>(Arrays.asList(collectionAttr, composedAttr, enumeratedAttr, atomicAttr, mapAttr))
    @Shared
    //Creating list of valid attributes with one invalid attribute for some test cases
    Set<AttributeDescriptorModel> invalidAttributesList = new HashSet<>(Arrays.asList(collectionAttr, invalidAttr, atomicAttr))

    @Shared
    //Empty collection for tests
    Set<AttributeDescriptorModel> emptyAttrList = new HashSet<>();

    def setup() {
        given:
        //Setting the private variables we need, using the variables we defined above
        readService.setTypeService(typeServiceStub)
        readService.setReadOnlyAttributesConfiguration(readOnlyAttributes)
        //The only valid attribute types are the ones that we have defined so lets Mock that
        when:
        typeServiceStub.isAssignableFrom("CollectionType", "CollectionType")
        typeServiceStub.isAssignableFrom("ComposedType", "ComposedType")
        typeServiceStub.isAssignableFrom("EnumerationMetaType", "EnumerationMetaType")
        typeServiceStub.isAssignableFrom("AtomicType", "AtomicType")
        typeServiceStub.isAssignableFrom("MapType", "MapType")
        then:
        typeServiceStub.isAssignableFrom("CollectionType", "CollectionType") >> true
        typeServiceStub.isAssignableFrom("ComposedType", "ComposedType") >> true
        typeServiceStub.isAssignableFrom("EnumerationMetaType", "EnumerationMetaType") >> true
        typeServiceStub.isAssignableFrom("AtomicType", "AtomicType") >> true
        typeServiceStub.isAssignableFrom("MapType", "MapType") >> true
    }

    @Test
    @Unroll
    def "Attributes List=#attributesList, Contains All=#containsAll, Attribute Removed=#attrRemoved, Type BlackList=#typesBlackList, AttributesBlackList=#attributesBlackList, WhiteList=#whiteListAttr, listSize=#listSize"() {
        given:
        def composedTypeModel = Mock(ComposedTypeModel)
        //Manually stubbing/overriding our types blacklist
        Field typesBlackListField = EditorBlacklists.getDeclaredField("typesBlackList")
        typesBlackListField.setAccessible(true)
        Field attributesBlackListField = EditorBlacklists.getDeclaredField("attributeBlackList")
        attributesBlackListField.setAccessible(true)

        Field modifiersField = Field.class.getDeclaredField("modifiers")
        modifiersField.setAccessible(true)
        modifiersField.set(typesBlackListField, typesBlackListField.getModifiers() & ~Modifier.FINAL)
        modifiersField.set(attributesBlackListField, attributesBlackListField.getModifiers() & ~Modifier.FINAL)

        //Setting the blacklists
        typesBlackListField.set(null, new HashSet(Arrays.asList(typesBlackList)))
        attributesBlackListField.set(null, new HashSet(Arrays.asList(attributesBlackList)))

        //Setting the attributes that are to be returned
        readService.getAttributesForType(_) >> attributesList
        //Setting the readonly attributes list
        readService.getReadOnlyAttributesAsAttributeDescriptorModels(_) >> whiteListAttr
        expect:
        editorAttributesFilteringService.filterAttributesForAttributesMap(composedTypeModel).size() == listSize
        editorAttributesFilteringService.filterAttributesForAttributesMap(composedTypeModel)
                .containsAll(attributesList) == containsAll
        //Checking to make sure that attr got removed
        !editorAttributesFilteringService.filterAttributesForAttributesMap(composedTypeModel)
                .contains(attrRemoved)

        where:
        attributesList        | containsAll | attrRemoved    | typesBlackList         | attributesBlackList | whiteListAttr                                          | listSize
        validAttributesList   | true        | true           | "NoEffect"             | "NoEffect"          | emptyAttrList                                          | 5
        invalidAttributesList | false       | invalidAttr    | "NoEffect"             | "NoEffect"          | emptyAttrList                                          | 2
        validAttributesList   | false       | collectionAttr | "ParentCollectionType" | "NoEffect"          | emptyAttrList                                          | 4
        validAttributesList   | false       | atomicAttr     | "NoEffect"             | "atomicQualifier"   | emptyAttrList                                          | 4
        validAttributesList   | false       | composedAttr   | "ParentComposedType"   | "ComposedType"      | emptyAttrList                                          | 4
        validAttributesList   | false       | atomicAttr     | "ParentCollectionType" | "atomicQualifier"   | emptyAttrList                                          | 3
        validAttributesList   | false       | collectionAttr | "ParentCollectionType" | "atomicQualifier"   | emptyAttrList                                          | 3
        validAttributesList   | true        | true           | "NoEffect"             | "mapQualifier"      | new HashSet(Arrays.asList(mapAttr))                    | 5
        validAttributesList   | true        | true           | "CollectionType"       | "NoEffect"          | new HashSet(Arrays.asList(collectionAttr))             | 5
        validAttributesList   | false       | atomicAttr     | "CollectionType"       | "atomicQualifier"   | new HashSet(Arrays.asList(collectionAttr))             | 4
        validAttributesList   | true        | true           | "CollectionType"       | "atomicQualifier"   | new HashSet(Arrays.asList(collectionAttr, atomicAttr)) | 5

    }

    @Unroll
    def "Attributes List=#attributesList, Contains All=#containsAll, Attribute Removed=#attrRemoved, AttributesBlackList=#attributesBlackList, WhiteList=#whiteListAttr, listSize=#listSize"() {
        given:
        def composedTypeModel = Mock(ComposedTypeModel)
        //Manually stubbing/overriding our types blacklist
        Field attributesBlackListField = EditorBlacklists.getDeclaredField("attributeBlackList")
        attributesBlackListField.setAccessible(true)

        Field modifiersField = Field.class.getDeclaredField("modifiers")
        modifiersField.setAccessible(true)
        modifiersField.set(attributesBlackListField, attributesBlackListField.getModifiers() & ~Modifier.FINAL)

        //Setting the blacklists
        attributesBlackListField.set(null, new HashSet(Arrays.asList(attributesBlackList)))

        //Setting the attributes that are to be returned
        readService.getAttributesForType(_) >> attributesList
        expect:
        editorAttributesFilteringService.filterAttributesForTree(composedTypeModel).size() == listSize
        editorAttributesFilteringService.filterAttributesForTree(composedTypeModel)
                .containsAll(attributesList) == containsAll
        //Checking to make sure that attr got removed
        !editorAttributesFilteringService.filterAttributesForTree(composedTypeModel)
                .contains(attrRemoved)

        where:
        attributesList      | containsAll | attrRemoved    | attributesBlackList            | listSize
        validAttributesList | false       | false          | "NoEffect"                     | 2
        validAttributesList | false       | composedAttr   | "composedQualifier"            | 1
        validAttributesList | false       | enumeratedAttr | "enumerationMetaTypeQualifier" | 1
    }

    @Test
    def "check the content of attribute blackList."() {
        given:
        def blacklist = EditorBlacklists.getAttributeBlackList()

        expect:
        blacklist.containsAll([
                "allDocuments",
                "assignedCockpitItemTemplates",
                "comments",
                "creationtime",
                "itemtype",
                "modifiedtime",
                "owner",
                "pk",
                "savedValues",
                "sealed",
                "synchronizationSources",
                "synchronizedCopies",
                "classificationIndexString"])
    }

    @Test
    def "check the content of type blackList."() {
        given:
        def blacklist = EditorBlacklists.getTypesBlackList()

        expect:
        blacklist.containsAll([
                "Item",
                "LogFile",
                "Trigger",
                "ItemSyncTimestamp",
                "ProcessTaskLog",
                "CronJob",
                "JobSearchRestriction",
                "Step"])
    }

    @Test
    @Unroll
    def "#blacklistName cannot be modified."() {
        when:
        blacklist.add("cannot change an unmodifiable collection.")

        then:
        thrown(exception)

        where:
        blacklist                                | exception                     | blacklistName
        EditorBlacklists.getAttributeBlackList() | UnsupportedOperationException | "attribute blacklist"
        EditorBlacklists.getTypesBlackList()     | UnsupportedOperationException | "type blacklist"
    }
}
