/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { FieldSchemaDataMapper } from './FieldSchemaDataMapper';

describe('GenericEditor - FieldSchemaDataMapper', () => {
    it('should map data and schema for a field mapper', () => {
        const mapper = new FieldSchemaDataMapper(
            'headline',
            {
                qualifier: 'headline',
                required: true,
                editable: true
            } as any,
            true,
            {
                headline: 'headline_data'
            }
        );

        expect(mapper.toValue()).toBe('headline_data');
        expect(mapper.toSchema()).toEqual({
            type: 'field',
            component: 'field',
            validators: {
                required: true
            },
            inputs: {
                id: 'headline',
                field: {
                    qualifier: 'headline',
                    required: true,
                    editable: true
                },
                qualifier: 'headline',
                component: {
                    headline: 'headline_data'
                }
            }
        });
    });

    it('should not set validation required for fields that are not editable', () => {
        const mapper = new FieldSchemaDataMapper(
            'headline',
            {
                qualifier: 'headline',
                required: true,
                editable: false
            } as any,
            true,
            {
                headline: 'headline_data'
            }
        );

        expect(mapper.toSchema()).toEqual({
            type: 'field',
            component: 'field',
            validators: {
                required: undefined
            },
            inputs: {
                id: 'headline',
                field: {
                    qualifier: 'headline',
                    required: true,
                    editable: false
                },
                qualifier: 'headline',
                component: {
                    headline: 'headline_data'
                }
            }
        });
    });
});
