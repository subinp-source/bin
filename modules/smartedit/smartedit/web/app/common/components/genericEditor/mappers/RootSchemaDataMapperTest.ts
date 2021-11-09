/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { RootSchemaDataMapper } from './RootSchemaDataMapper';

describe('GenericEditor - RootSchemaDataMapper', () => {
    it('should map root data, schema and call the nested tab mappers', () => {
        const tab1Mapper = jasmine.createSpyObj('tab1Mapper', ['toValue', 'toSchema']);
        const tab2Mapper = jasmine.createSpyObj('tab2Mapper', ['toValue', 'toSchema']);

        tab1Mapper.id = 'tab1';
        tab2Mapper.id = 'tab2';

        tab1Mapper.toValue.and.returnValue('tab_1_value');
        tab2Mapper.toValue.and.returnValue('tab_2_value');

        tab1Mapper.toSchema.and.returnValue({ schema: 'tab_1_schema' });
        tab2Mapper.toSchema.and.returnValue({ schema: 'tab_2_schema' });

        const mapper = new RootSchemaDataMapper(
            [tab1Mapper, tab2Mapper],
            {
                data: 'tab_data'
            } as any,
            { fields: 'field_map_data' } as any
        );

        expect(mapper.toValue()).toEqual({
            tab1: 'tab_1_value',
            tab2: 'tab_2_value'
        });
        expect(mapper.toSchema()).toEqual({
            type: 'group',
            component: 'tabs',
            schemas: {
                tab1: { schema: 'tab_1_schema' } as any,
                tab2: { schema: 'tab_2_schema' } as any
            },
            inputs: {
                tabs: { data: 'tab_data' },
                fieldsMap: { fields: 'field_map_data' }
            }
        });
    });
});
