/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TabSchemaDataMapper } from './TabSchemaDataMapper';

describe('GenericEditor - TabSchemaDataMapper', () => {
    it('should map each tab with their fields and call the nested field mappers', () => {
        const field1Mapper = jasmine.createSpyObj('field1Mapper', ['toValue', 'toSchema']);
        const field2Mapper = jasmine.createSpyObj('field2Mapper', ['toValue', 'toSchema']);

        field1Mapper.id = 'field1';
        field2Mapper.id = 'field2';

        field1Mapper.toValue.and.returnValue('field_1_value');
        field2Mapper.toValue.and.returnValue('field_2_value');

        field1Mapper.toSchema.and.returnValue({ schema: 'field_1_schema' });
        field2Mapper.toSchema.and.returnValue({ schema: 'field_2_schema' });

        const mapper = new TabSchemaDataMapper('111', [field1Mapper, field2Mapper]);

        expect(mapper.toValue()).toEqual({
            field1: 'field_1_value',
            field2: 'field_2_value'
        });

        expect(mapper.toSchema()).toEqual({
            type: 'group',
            persist: false,
            schemas: {
                field1: { schema: 'field_1_schema' } as any,
                field2: { schema: 'field_2_schema' } as any
            }
        });
    });
});
