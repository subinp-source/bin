/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { LocalizedSchemaDataMapper } from './LocalizedSchemaDataMapper';

describe('GenericEditor - LocalizedSchemaDataMapper', () => {
    it('should map a localized field and call the nested field mappers', () => {
        const fieldEnglishMapper = jasmine.createSpyObj('fieldEnglishMapper', [
            'toValue',
            'toSchema'
        ]);
        const fieldFrenchMapper = jasmine.createSpyObj('fieldFrenchMapper', [
            'toValue',
            'toSchema'
        ]);

        fieldEnglishMapper.toValue.and.returnValue('field_english');
        fieldFrenchMapper.toValue.and.returnValue('field_french');

        fieldEnglishMapper.toSchema.and.returnValue({ schema: 'english' });
        fieldFrenchMapper.toSchema.and.returnValue({ schema: 'french' });

        fieldEnglishMapper.id = 'en';
        fieldFrenchMapper.id = 'fr';

        const mapper = new LocalizedSchemaDataMapper(
            'localized',
            [fieldEnglishMapper, fieldFrenchMapper],
            {
                qualifier: 'localized'
            } as any,
            ['en', 'fr'] as any,
            {
                payload: '123'
            }
        );

        expect(mapper.toValue()).toEqual({
            en: 'field_english',
            fr: 'field_french'
        });
        expect(mapper.toSchema()).toEqual({
            type: 'group',
            component: 'localized',
            schemas: {
                en: { schema: 'english' } as any,
                fr: { schema: 'french' } as any
            },
            inputs: {
                field: {
                    qualifier: 'localized'
                },
                languages: ['en', 'fr'],
                component: { payload: '123' }
            }
        });
    });
});
