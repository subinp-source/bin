/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { EditorFieldMappingService } from './EditorFieldMappingService';
import { GenericEditorField, GenericEditorMapping, GenericEditorStructure } from '../types';
import { LogService } from '@smart/utils';
import { BooleanComponent, ShortStringComponent } from '../widgets';
import { DateTimePickerComponent } from '../widgets/DateTimePickerComponent';
import { LongStringComponent } from '../widgets/LongStringComponent/LongStringComponent';
import { RichTextFieldComponent } from '../widgets/RichTextField';
import { EnumComponent } from '../widgets/EnumComponent/EnumComponent';
import { DropdownComponent } from '../widgets/DropdownComponent/DropdownComponent';
import { NumberComponent } from '../widgets/NumberComponent';
import { FloatComponent } from '../widgets/FloatComponent';
import { EmailComponent } from '../widgets/EmailComponent';

describe('genericEditorMappingService -', () => {
    // --------------------------------------------------------------------------------------
    // Constants
    // --------------------------------------------------------------------------------------
    const WILDCARD = '*';
    const STRUCTURE_TYPE_NAME_1 = 'SOME STRUCTURE TYPE1';
    const COMPONENT_TYPE_NAME = 'SOME COMPONENT TYPE';
    const DISCRIMINATOR = 'SOME DISCRIMINATOR';
    const componentTypeStructure: GenericEditorStructure = {} as GenericEditorStructure;
    // --------------------------------------------------------------------------------------
    // Variables
    // --------------------------------------------------------------------------------------
    let editorFieldMappingService: EditorFieldMappingService;
    let field: GenericEditorField;

    let log: jasmine.SpyObj<LogService>;

    // --------------------------------------------------------------------------------------
    // Before Each
    // --------------------------------------------------------------------------------------

    beforeEach(() => {
        log = jasmine.createSpyObj('log', ['log']);

        editorFieldMappingService = new EditorFieldMappingService(log);

        spyOn(editorFieldMappingService, '_cleanTemplate').and.callThrough();
        spyOn(editorFieldMappingService, '_exactValueMatchPredicate').and.callThrough();

        // Ensure editor is clean before each test.
        editorFieldMappingService._editorsFieldMapping = [];
        editorFieldMappingService._fieldsTabsMapping = [];

        field = {
            cmsStructureType: STRUCTURE_TYPE_NAME_1,
            smarteditComponentType: COMPONENT_TYPE_NAME,
            qualifier: DISCRIMINATOR
        };
    });

    // --------------------------------------------------------------------------------------
    // Tests
    // --------------------------------------------------------------------------------------
    describe('Common Mapping -', () => {
        const PAYLOAD = 'some payload';
        let EXACT_MATCH_PREDICATE: (e: string, a: string) => boolean;
        let collection: GenericEditorMapping[];

        beforeEach(() => {
            collection = [];
            EXACT_MATCH_PREDICATE = editorFieldMappingService._exactValueMatchPredicate;
        });

        it('WHEN addMapping is called THEN it stores the right mapping', () => {
            // GIVEN

            // WHEN
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                DISCRIMINATOR,
                PAYLOAD,
                collection
            );

            // THEN
            expect(collection.length).toBe(1);
            expect(collection[0].structureTypeMatcher).toBe(STRUCTURE_TYPE_NAME_1);
            expect(collection[0].componentTypeMatcher).toBe(COMPONENT_TYPE_NAME);
            expect(collection[0].discriminatorMatcher).toBe(DISCRIMINATOR);
            expect(collection[0].value).toBe(PAYLOAD);
        });

        it('GIVEN a mapping is defined WHEN addMapping is called with the same keys THEN it replaces the original mapping', () => {
            // GIVEN
            const obj = { someFunc: () => true };
            const spy = spyOn(obj, 'someFunc');
            const somePayload = 'some other payload';

            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                obj.someFunc,
                DISCRIMINATOR,
                PAYLOAD,
                collection
            );

            // WHEN
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                obj.someFunc,
                DISCRIMINATOR,
                somePayload,
                collection
            );

            // THEN
            expect(collection.length).toBe(1);
            expect(collection[0].structureTypeMatcher).toBe(STRUCTURE_TYPE_NAME_1);
            expect(collection[0].componentTypeMatcher).toBe(spy);
            expect(collection[0].discriminatorMatcher).toBe(DISCRIMINATOR);
            expect(collection[0].value).toBe(somePayload);
        });

        it('GIVEN a mapping is defined WHEN getMapping is called THEN the mapping will be returned if there is a perfect match', () => {
            // GIVEN
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                DISCRIMINATOR,
                PAYLOAD,
                collection
            );

            // WHEN
            const mapping = editorFieldMappingService._getMapping(
                field,
                componentTypeStructure,
                collection
            );

            // THEN
            expect(mapping).toBe(PAYLOAD);
            expect(EXACT_MATCH_PREDICATE).toHaveBeenCalledWith(
                STRUCTURE_TYPE_NAME_1,
                field.cmsStructureType
            );
            expect(EXACT_MATCH_PREDICATE).toHaveBeenCalledWith(
                COMPONENT_TYPE_NAME,
                field.smarteditComponentType
            );
            expect(EXACT_MATCH_PREDICATE).toHaveBeenCalledWith(DISCRIMINATOR, field.qualifier);
        });

        it('GIVEN a mapping is defined WHEN getMapping is called THEN the mapping will be returned if there is a partial match', () => {
            // GIVEN
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                WILDCARD,
                DISCRIMINATOR,
                PAYLOAD,
                collection
            );

            // WHEN
            const mapping = editorFieldMappingService._getMapping(
                field,
                componentTypeStructure,
                collection
            );

            // THEN
            expect(mapping).toBe(PAYLOAD);
            expect(EXACT_MATCH_PREDICATE).toHaveBeenCalledWith(
                STRUCTURE_TYPE_NAME_1,
                field.cmsStructureType
            );
            expect(EXACT_MATCH_PREDICATE).not.toHaveBeenCalledWith(field.smarteditComponentType);
            expect(EXACT_MATCH_PREDICATE).toHaveBeenCalledWith(DISCRIMINATOR, field.qualifier);
        });

        it('GIVEN a mapping is defined WHEN getMapping is called THEN the no mapping will be returned if there is no match', () => {
            // GIVEN
            const otherDiscriminator = 'some other discriminator';
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                otherDiscriminator,
                PAYLOAD,
                collection
            );

            // WHEN
            const mapping = editorFieldMappingService._getMapping(
                field,
                componentTypeStructure,
                collection
            );

            // THEN
            expect(mapping).toBe(null);
            expect(EXACT_MATCH_PREDICATE).toHaveBeenCalledWith(
                STRUCTURE_TYPE_NAME_1,
                field.cmsStructureType
            );
            expect(EXACT_MATCH_PREDICATE).toHaveBeenCalledWith(
                COMPONENT_TYPE_NAME,
                field.smarteditComponentType
            );
            expect(EXACT_MATCH_PREDICATE).toHaveBeenCalledWith(otherDiscriminator, field.qualifier);
        });

        it('GIVEN a mapping is defined with custom predicates WHEN getMapping is called THEN the predicates will be executed to determine if there is a match', () => {
            // GIVEN
            const structureTypePredicate = jasmine.createSpy('structureTypePredicate');
            const componentTypePredicate = jasmine.createSpy('componentTypePredicate');
            const discriminatorPredicate = jasmine.createSpy('discriminatorPredicate');

            structureTypePredicate.and.returnValue(true);
            componentTypePredicate.and.returnValue(true);
            discriminatorPredicate.and.returnValue(true);

            editorFieldMappingService._addMapping(
                structureTypePredicate,
                componentTypePredicate,
                discriminatorPredicate,
                PAYLOAD,
                collection
            );

            // WHEN
            const mapping = editorFieldMappingService._getMapping(
                field,
                componentTypeStructure,
                collection
            );

            // THEN
            expect(mapping).toBe(PAYLOAD);
            expect(structureTypePredicate).toHaveBeenCalledWith(
                field.cmsStructureType,
                field,
                componentTypeStructure
            );
            expect(componentTypePredicate).toHaveBeenCalledWith(
                field.smarteditComponentType,
                field,
                componentTypeStructure
            );
            expect(discriminatorPredicate).toHaveBeenCalledWith(
                field.qualifier,
                field,
                componentTypeStructure
            );
        });

        it('GIVEN a mapping is defined with custom predicates WHEN getMapping is called and there is no match THEN then no mapping will be returned', () => {
            // GIVEN
            const structureTypePredicate = jasmine.createSpy('structureTypePredicate');
            const componentTypePredicate = jasmine.createSpy('componentTypePredicate');
            const discriminatorPredicate = jasmine.createSpy('discriminatorPredicate');

            structureTypePredicate.and.returnValue(true);
            componentTypePredicate.and.returnValue(false);
            discriminatorPredicate.and.returnValue(true);

            editorFieldMappingService._addMapping(
                structureTypePredicate,
                componentTypePredicate,
                discriminatorPredicate,
                PAYLOAD,
                collection
            );

            // WHEN
            const mapping = editorFieldMappingService._getMapping(
                field,
                componentTypeStructure,
                collection
            );

            // THEN
            expect(mapping).toBe(null);
            expect(structureTypePredicate).toHaveBeenCalledWith(
                field.cmsStructureType,
                field,
                componentTypeStructure
            );
            expect(componentTypePredicate).toHaveBeenCalledWith(
                field.smarteditComponentType,
                field,
                componentTypeStructure
            );
            expect(discriminatorPredicate).not.toHaveBeenCalled();
        });

        it('GIVEN several mappings are defined WHEN getMapping is called THEN the mapping with the exact match will be returned', () => {
            // GIVEN
            const value1 = 'some value1';
            const value2 = 'some value2';
            const value3 = 'some value3';
            const value4 = 'some value4';

            editorFieldMappingService._addMapping(
                WILDCARD,
                COMPONENT_TYPE_NAME,
                DISCRIMINATOR,
                value1,
                collection
            );
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                DISCRIMINATOR,
                value2,
                collection
            );
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                WILDCARD,
                DISCRIMINATOR,
                value3,
                collection
            );
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                WILDCARD,
                value4,
                collection
            );

            // WHEN
            const mapping = editorFieldMappingService._getMapping(
                field,
                componentTypeStructure,
                collection
            );

            // THEN
            expect(mapping).toBe(value2);
        });

        it('GIVEN several mappings are defined WHEN getMapping is called THEN the mapping with the exact match will be returned', () => {
            // GIVEN
            const value1 = 'some value1';
            const value2 = 'some value2';
            const value3 = 'some value3';
            const value4 = 'some value4';

            const structureTypePredicate = jasmine.createSpy('structureTypePredicate');
            const componentTypePredicate = jasmine.createSpy('componentTypePredicate');
            const discriminatorPredicate = jasmine.createSpy('discriminatorPredicate');

            structureTypePredicate.and.returnValue(true);
            componentTypePredicate.and.returnValue(true);
            discriminatorPredicate.and.returnValue(true);

            editorFieldMappingService._addMapping(
                WILDCARD,
                COMPONENT_TYPE_NAME,
                DISCRIMINATOR,
                value1,
                collection
            );
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                DISCRIMINATOR,
                value2,
                collection
            );
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                WILDCARD,
                DISCRIMINATOR,
                value3,
                collection
            );
            editorFieldMappingService._addMapping(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                WILDCARD,
                value4,
                collection
            );

            editorFieldMappingService._addMapping(
                WILDCARD,
                componentTypePredicate,
                discriminatorPredicate,
                value1,
                collection
            );
            editorFieldMappingService._addMapping(
                structureTypePredicate,
                componentTypePredicate,
                discriminatorPredicate,
                value2,
                collection
            );
            editorFieldMappingService._addMapping(
                structureTypePredicate,
                WILDCARD,
                discriminatorPredicate,
                value3,
                collection
            );
            editorFieldMappingService._addMapping(
                structureTypePredicate,
                componentTypePredicate,
                WILDCARD,
                value4,
                collection
            );

            // WHEN
            const mapping = editorFieldMappingService._getMapping(
                field,
                componentTypeStructure,
                collection
            );

            // THEN
            expect(mapping).toBe(value2);
        });
    });

    describe('Mapping Predicates -', () => {
        it('GIVEN an exact matching predicate WHEN evaluated with an exact match THEN it returns true', () => {
            // GIVEN
            const expectedValue = 'some expected value';
            const actualValue = 'some expected value';
            const predicate = editorFieldMappingService._exactValueMatchPredicate;

            // WHEN
            const result = predicate(expectedValue, actualValue);

            // THEN
            expect(result).toBe(true);
        });

        it('GIVEN an exact matching predicate WHEN evaluated with a partial match THEN it returns false', () => {
            // GIVEN
            const expectedValue = 'some expected value';
            const actualValue = 'other value';
            const predicate = editorFieldMappingService._exactValueMatchPredicate;

            // WHEN
            const result = predicate(expectedValue, actualValue);

            // THEN
            expect(result).toBe(false);
        });
    });

    describe('Field Mapping -', () => {
        it('WHEN addFieldMapping is called THEN it is delegated to _addMapping', () => {
            // GIVEN
            const value = { template: 'some value' };
            const fieldMappingCollection = editorFieldMappingService._editorsFieldMapping;

            spyOn(editorFieldMappingService, '_addMapping');

            // WHEN
            editorFieldMappingService.addFieldMapping(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                DISCRIMINATOR,
                value
            );

            // THEN
            expect(editorFieldMappingService._addMapping).toHaveBeenCalledWith(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                DISCRIMINATOR,
                value,
                fieldMappingCollection
            );
        });

        it('WHEN getEditorFieldMapping is called THEN it is delegated to _getMapping', () => {
            // GIVEN
            const expectedMapping = {
                template: 'some template'
            };
            const fieldMappingCollection = editorFieldMappingService._editorsFieldMapping;
            spyOn(editorFieldMappingService, '_getMapping').and.returnValue(expectedMapping);

            // WHEN
            const result = editorFieldMappingService.getEditorFieldMapping(
                field,
                componentTypeStructure
            );

            // THEN
            expect(result).toBe(expectedMapping);
            expect(editorFieldMappingService._getMapping).toHaveBeenCalledWith(
                field,
                componentTypeStructure,
                fieldMappingCollection
            );
            expect(editorFieldMappingService._cleanTemplate).toHaveBeenCalledWith(
                expectedMapping.template
            );
        });

        it('WHEN _registerDefaultFieldMappings is called THEN all default mappings are added', () => {
            // GIVEN
            expect(editorFieldMappingService._editorsFieldMapping.length).toBe(0);

            // WHEN
            editorFieldMappingService._registerDefaultFieldMappings();

            // THEN
            field.cmsStructureType = 'Boolean';
            expect(
                editorFieldMappingService.getEditorFieldMapping(field, componentTypeStructure)
            ).toEqual({
                component: BooleanComponent
            });

            field.cmsStructureType = 'ShortString';
            expect(
                editorFieldMappingService.getEditorFieldMapping(field, componentTypeStructure)
            ).toEqual({
                component: ShortStringComponent
            });

            field.cmsStructureType = 'LongString';
            expect(
                editorFieldMappingService.getEditorFieldMapping(field, componentTypeStructure)
            ).toEqual({
                component: LongStringComponent
            });

            field.cmsStructureType = 'RichText';
            expect(
                editorFieldMappingService.getEditorFieldMapping(field, componentTypeStructure)
            ).toEqual({
                component: RichTextFieldComponent
            });

            field.cmsStructureType = 'Number';
            expect(
                editorFieldMappingService.getEditorFieldMapping(field, componentTypeStructure)
            ).toEqual({
                component: NumberComponent
            });

            field.cmsStructureType = 'Float';
            expect(
                editorFieldMappingService.getEditorFieldMapping(field, componentTypeStructure)
            ).toEqual({
                component: FloatComponent
            });

            field.cmsStructureType = 'Dropdown';
            expect(
                editorFieldMappingService.getEditorFieldMapping(field, componentTypeStructure)
            ).toEqual({
                component: DropdownComponent
            });

            field.cmsStructureType = 'DateTime';
            expect(
                editorFieldMappingService.getEditorFieldMapping(field, componentTypeStructure)
            ).toEqual({
                component: DateTimePickerComponent
            });

            field.cmsStructureType = 'Enum';
            expect(
                editorFieldMappingService.getEditorFieldMapping(field, componentTypeStructure)
            ).toEqual({
                component: EnumComponent
            });

            field.cmsStructureType = 'Email';
            expect(
                editorFieldMappingService.getEditorFieldMapping(field, componentTypeStructure)
            ).toEqual({
                component: EmailComponent
            });
        });
    });

    describe('Tab Mapping -', () => {
        it('WHEN addFieldTabMapping is called THEN it is delegated to _addMapping', () => {
            // GIVEN
            const value = 'some value';
            const fieldTabMappingCollection = editorFieldMappingService._fieldsTabsMapping;
            spyOn(editorFieldMappingService, '_addMapping');

            // WHEN
            editorFieldMappingService.addFieldTabMapping(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                DISCRIMINATOR,
                value
            );

            // THEN
            expect(editorFieldMappingService._addMapping).toHaveBeenCalledWith(
                STRUCTURE_TYPE_NAME_1,
                COMPONENT_TYPE_NAME,
                DISCRIMINATOR,
                value,
                fieldTabMappingCollection
            );
        });

        it('WHEN getFieldTabMapping is called THEN it is delegated to _getMapping', () => {
            // GIVEN
            const expectedMapping = 'some mapping';
            const fieldTabMappingCollection = editorFieldMappingService._fieldsTabsMapping;
            spyOn(editorFieldMappingService, '_getMapping').and.returnValue(expectedMapping);

            // WHEN
            const result = editorFieldMappingService.getFieldTabMapping(
                field,
                componentTypeStructure
            );

            // THEN
            expect(result).toBe(expectedMapping);
            expect(editorFieldMappingService._getMapping).toHaveBeenCalledWith(
                field,
                componentTypeStructure,
                fieldTabMappingCollection
            );
        });
    });
});
