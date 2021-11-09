/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Payload } from '../../../dtos';
import { GenericEditorField, GenericEditorFieldMessage, GenericEditorTab } from '../types';
import { GenericEditorState } from './GenericEditorState';

import { FormGrouping } from '@smart/utils';
import { proxifyDataObject } from './InternalProperty';

describe('GenericEditor - GenericEditorState', () => {
    const tabs: GenericEditorTab[] = [];

    let form: jasmine.SpyObj<FormGrouping>;
    let seValidationMessageParser: jasmine.Spy;

    beforeEach(() => {
        seValidationMessageParser = jasmine
            .createSpy('seValidationMessageParser')
            .and.callFake(function(message: any) {
                return {
                    message
                };
            });

        form = jasmine.createSpyObj('form', ['control']);
    });
    it('method transform will call customSanitize on a field when defined', () => {
        const fields: GenericEditorField[] = [
            {
                qualifier: 'specialField',
                cmsStructureType: 'ShortString',
                localized: true,
                customSanitize(value: string, sanitize: (str: string) => string): string {
                    return `${value}_sanitized`;
                }
            }
        ] as any;

        const payload: any = {
            specialField: 'some_value'
        };

        const state = new GenericEditorState(
            'id',
            form,
            payload,
            {},
            {},
            tabs,
            fields,
            [] as any,
            {} as any
        );

        const sanitizedPayload = state.sanitizedPayload();
        expect(sanitizedPayload).toEqual({
            specialField: 'some_value_sanitized'
        });
    });
    // sanitize is deprecated since 1808, ECP-4765. The method will be removed in 2105.
    xit('method sanitizedPayload will remove dangerous characters from a localized ShortString component', () => {
        const fields: GenericEditorField[] = [
            {
                qualifier: 'urlLink',
                cmsStructureType: 'ShortString',
                localized: true
            }
        ] as any;

        const payload: Payload = {
            urlLink: {
                en: "/pathwithxss/onclick='alert(1)'"
            }
        };

        const state = new GenericEditorState(
            'id',
            form,
            payload,
            {},
            {},
            tabs,
            fields,
            [] as any,
            {} as any
        );

        const sanitizedPayload = state.sanitizedPayload();

        expect(sanitizedPayload).toEqual({
            urlLink: {
                en: "/pathwithxss/onclick='alert\\(1\\)'"
            }
        });
    });
    // sanitize is deprecated since 1808, ECP-4765. The method will be removed in 2105.
    xit('method sanitizedPayload will remove dangerous characters from a LongString component', () => {
        const fields: GenericEditorField[] = [
            {
                qualifier: 'urlLink',
                cmsStructureType: 'ShortString'
            }
        ] as any;

        const payload: any = {
            urlLink: "/pathwithxss/onclick='alert(1)'"
        };

        const state = new GenericEditorState(
            'id',
            form,
            payload,
            {},
            {},
            tabs,
            fields,
            [] as any,
            {} as any
        );

        const sanitizedPayload = state.sanitizedPayload();

        expect(sanitizedPayload).toEqual({
            urlLink: "/pathwithxss/onclick='alert\\(1\\)'"
        });
    });
    // sanitize is deprecated since 1808, ECP-4765. The method will be removed in 2105.
    xit('method isDirty will sanitize before checking if pristine and component HTML are equal', () => {
        let pristine: Payload = {
            a: {
                en:
                    '<h2>search</h2><p>Suggestions</p><ul>	<li>The</li>	<li>The</li>	<li>Test</li></ul>'
            },
            b: '1',
            c:
                '<h2>search</h2> \n<p>Suggestions</p><ul>\n<li>The</li><li>The</li><li>Test</li></ul>'
        };

        let component = {
            a: {
                en:
                    '<h2>search</h2> \n<p>Suggestions</p><ul>\n<li>The</li><li>The</li><li>Test</li></ul>'
            },
            b: '1',
            c: '<h2>search</h2><p>Suggestions</p><ul>	<li>The</li>	<li>The</li>	<li>Test</li></ul>'
        };

        let fields: GenericEditorField[] = [
            {
                cmsStructureType: 'RichText',
                qualifier: 'a',
                localized: true
            },
            {
                qualifier: 'b',
                cmsStructureType: 'someType'
            },
            {
                qualifier: 'c',
                cmsStructureType: 'RichText',
                localized: false
            }
        ] as any;

        let state = new GenericEditorState(
            'id',
            form,
            component,
            {},
            pristine,
            tabs,
            fields,
            [] as any,
            {} as any
        );

        expect(state.isDirty()).toEqual(false);

        pristine = {
            a: {
                en: '<h2>test1</h2> <p>test2</p>'
            }
        };

        component = {
            a: {
                en: '<h2>TEST2</h2> \n<p>test1</p>'
            }
        } as any;

        fields = [
            {
                cmsStructureType: 'RichText',
                qualifier: 'a',
                localized: true
            }
        ] as any;

        state = new GenericEditorState(
            'id',
            form,
            component,
            {},
            pristine,
            tabs,
            fields,
            [] as any,
            {} as any
        );

        expect(state.isDirty()).toEqual(true);
    });

    it('isDirty will return true even for properties that are not fields', () => {
        const pristine = {
            a: '123 ',
            b: '0'
        };

        const component = {
            a: '123',
            b: ''
        };

        const state = new GenericEditorState(
            'id',
            form,
            component,
            {},
            pristine,
            tabs,
            [],
            [] as any,
            {} as any
        );

        expect(state.isDirty()).toEqual(true);
    });

    it('displayValidationErrors will add errors messages and localization languages to the field', () => {
        const validationErrors = [
            {
                message: 'This field cannot contain special characters',
                reason: 'missing',
                subject: 'field1',
                subjectType: 'parameter',
                type: 'ValidationError'
            },
            {
                message: 'This field is required and must to be between 1 and 255 characters long.',
                reason: 'missing',
                subject: 'field2',
                subjectType: 'parameter',
                type: 'ValidationError'
            }
        ];

        const fields: GenericEditorField[] = [
            {
                qualifier: 'field1',
                cmsStructureType: 'something'
            },
            {
                qualifier: 'field2',
                cmsStructureType: 'something'
            }
        ];

        const state = new GenericEditorState(
            'id',
            form,
            {},
            {},
            {},
            tabs,
            fields,
            [] as any,
            {} as any
        );

        spyOn(state as any, '_getParseValidationMessage').and.returnValue(
            seValidationMessageParser
        );

        state.displayValidationMessages(validationErrors, true);

        expect(fields[0].messages.length).toEqual(1);
        expect(fields[0].messages.length).toEqual(1);

        expect(fields[0].messages[0].message).toEqual(
            'This field cannot contain special characters'
        );
        expect(fields[0].messages[0].marker).toEqual('field1');

        expect(fields[1].messages[0].message).toEqual(
            'This field is required and must to be between 1 and 255 characters long.'
        );
        expect(fields[1].messages[0].marker).toEqual('field2');
    });

    it('displayValidationErrors will add errors messages and localization languages to the field', () => {
        const validationErrors = [
            {
                message: 'This field cannot contain special characters',
                reason: 'missing',
                subject: 'field1',
                subjectType: 'parameter',
                type: 'ValidationError'
            },
            {
                message: 'This field is required and must to be between 1 and 255 characters long.',
                reason: 'missing',
                subject: 'field2',
                subjectType: 'parameter',
                type: 'ValidationError'
            }
        ];

        const fields: GenericEditorField[] = [
            {
                qualifier: 'field1',
                cmsStructureType: 'something'
            },
            {
                qualifier: 'field2',
                cmsStructureType: 'something'
            }
        ];

        const state = new GenericEditorState(
            'id',
            form,
            {},
            {},
            {},
            tabs,
            fields,
            [] as any,
            {} as any
        );

        spyOn(state as any, '_getParseValidationMessage').and.callFake(
            (m: GenericEditorFieldMessage) => seValidationMessageParser(m)
        );

        state.displayValidationMessages(validationErrors, true);

        expect(fields[0].messages.length).toEqual(1);
        expect(fields[0].messages.length).toEqual(1);

        expect(fields[0].messages[0].message).toEqual(
            'This field cannot contain special characters'
        );
        expect(fields[0].messages[0].marker).toEqual('field1');

        expect(fields[1].messages[0].message).toEqual(
            'This field is required and must to be between 1 and 255 characters long.'
        );
        expect(fields[1].messages[0].marker).toEqual('field2');
    });

    it('displayValidationMessages will add language from validation errors for the language property if the field is localized else will add the qualifier to the language property ', () => {
        seValidationMessageParser.and.callFake((message: any) => {
            const error: any = {};
            if (message === 'This field cannot contain special characters. Language: [en]') {
                error.message = 'This field cannot contain special characters.';
                error.language = 'en';
            } else {
                error.message = message;
            }
            return error;
        });

        const validationErrors = [
            {
                message: 'This field cannot contain special characters. Language: [en]',
                reason: 'missing',
                subject: 'field1',
                subjectType: 'parameter',
                type: 'ValidationError'
            },
            {
                message: 'This field is required and must to be between 1 and 255 characters long.',
                reason: 'missing',
                subject: 'field2',
                subjectType: 'parameter',
                type: 'ValidationError'
            }
        ];

        const fields: GenericEditorField[] = [
            {
                qualifier: 'field1',
                cmsStructureType: 'someType',
                localized: true
            },
            {
                qualifier: 'field2',
                cmsStructureType: 'someType'
            }
        ];

        const state = new GenericEditorState(
            'id',
            form,
            {},
            {},
            {},
            tabs,
            fields,
            [] as any,
            {} as any
        );

        spyOn(state as any, '_getParseValidationMessage').and.callFake(
            (m: GenericEditorFieldMessage) => seValidationMessageParser(m)
        );

        state.displayValidationMessages(validationErrors, true);

        expect(fields[0].messages.length).toEqual(1);
        expect(fields[0].messages.length).toEqual(1);

        expect(fields[0].messages[0].message).toEqual(
            'This field cannot contain special characters.'
        );
        expect(fields[0].messages[0].marker).toEqual('en');

        expect(fields[1].messages[0].message).toEqual(
            'This field is required and must to be between 1 and 255 characters long.'
        );
        expect(fields[1].messages[0].marker).toEqual('field2');
    });

    it('displayValidationMessages will not show the message if it has already been added to the list of messages', () => {
        const validationErrors = [
            {
                message: 'This field cannot contain special characters. Language: [en]',
                reason: 'missing',
                subject: 'field1',
                subjectType: 'parameter',
                type: 'ValidationError'
            },
            {
                message: 'This field is required and must to be between 1 and 255 characters long.',
                reason: 'missing',
                subject: 'field2',
                subjectType: 'parameter',
                type: 'ValidationError'
            }
        ];

        const fields = [
            {
                qualifier: 'field1',
                localized: true,
                messages: [
                    {
                        message: 'This field cannot contain special characters. Language: [en]',
                        reason: 'missing',
                        subject: 'field1',
                        subjectType: 'parameter',
                        type: 'ValidationError',
                        uniqId:
                            'eyJtZXNzYWdlIjoiVGhpcyBmaWVsZCBjYW5ub3QgY29udGFpbiBzcGVjaWFsIGNoYXJhY3RlcnMuIiwicmVhc29uIjoibWlzc2luZyIsInN1YmplY3QiOiJmaWVsZDEiLCJzdWJqZWN0VHlwZSI6InBhcmFtZXRlciIsInR5cGUiOiJWYWxpZGF0aW9uRXJyb3IiLCJsYW5ndWFnZSI6ImVuIiwibWFya2VyIjoiZW4ifQ=='
                    }
                ]
            },
            {
                qualifier: 'field2'
            }
        ] as GenericEditorField[];

        const state = new GenericEditorState(
            'id',
            form,
            {},
            {},
            {},
            tabs,
            fields,
            [] as any,
            {} as any
        );

        spyOn(state as any, '_getParseValidationMessage').and.callFake(function(message: any) {
            return {
                message: 'This field cannot contain special characters.',
                language: 'en',
                marker: 'en'
            };
        });
        state.displayValidationMessages(validationErrors, true);

        expect(fields[0].messages.length).toEqual(1);
        expect(fields[0].messages[0]).toEqual({
            message: 'This field cannot contain special characters.',
            reason: 'missing',
            subject: 'field1',
            subjectType: 'parameter',
            language: 'en',
            marker: 'en',
            type: 'ValidationError',
            uniqId:
                'eyJtZXNzYWdlIjoiVGhpcyBmaWVsZCBjYW5ub3QgY29udGFpbiBzcGVjaWFsIGNoYXJhY3RlcnMuIiwicmVhc29uIjoibWlzc2luZyIsInN1YmplY3QiOiJmaWVsZDEiLCJzdWJqZWN0VHlwZSI6InBhcmFtZXRlciIsInR5cGUiOiJWYWxpZGF0aW9uRXJyb3IiLCJsYW5ndWFnZSI6ImVuIiwibWFya2VyIjoiZW4ifQ=='
        });
    });

    it('GIVEN a list of validationMessages WHEN displayValidationErrors is called with keepAllErrors as false THEN it will filter out all fields that are pristine', () => {
        const validationErrors = [
            {
                message: 'This field cannot contain special characters',
                reason: 'missing',
                subject: 'field1',
                subjectType: 'parameter',
                type: 'ValidationError',
                fromSubmit: false,
                isNonPristine: true
            },
            {
                message: 'This field is required and must to be between 1 and 255 characters long.',
                reason: 'missing',
                subject: 'field2',
                subjectType: 'parameter',
                type: 'ValidationError',
                fromSubmit: false,
                isNonPristine: false
            }
        ];

        const fields: GenericEditorField[] = [
            {
                qualifier: 'field1',
                cmsStructureType: 'someType'
            },
            {
                qualifier: 'field2',
                cmsStructureType: 'someType'
            }
        ];

        const state = new GenericEditorState(
            'id',
            form,
            {},
            {},
            {},
            tabs,
            fields,
            [] as any,
            {} as any
        );

        spyOn(state as any, '_getParseValidationMessage').and.callFake(
            (m: GenericEditorFieldMessage) => seValidationMessageParser(m)
        );

        state.displayValidationMessages(validationErrors, false);

        expect(fields[0].messages.length).toEqual(1);
        expect(fields[0].messages[0].subject).toEqual('field1');
    });

    it('fieldsAreUserChecked WILL fail validation WHEN a required checkbox field is not checked', () => {
        const fields: GenericEditorField[] = [
            {
                qualifier: 'content',
                cmsStructureType: 'Paragraph',
                requiresUserCheck: {
                    content: true
                },
                isUserChecked: false
            }
        ];

        const state = new GenericEditorState(
            'id',
            form,
            {},
            {},
            {},
            tabs,
            fields,
            [] as any,
            {} as any
        );

        const valid = state.fieldsAreUserChecked();
        expect(valid).toBeFalsy();
    });

    it('should patch the component', () => {
        const fields: GenericEditorField[] = [
            {
                qualifier: 'property'
            },
            {
                qualifier: 'content',
                localized: true
            }
        ] as any;

        const component = {
            content: {
                en: 'content_en'
            },
            property: 'property_data'
        };

        const state = new GenericEditorState(
            'id',
            form,
            component,
            proxifyDataObject(component),
            {},
            tabs,
            fields,
            [] as any,
            {} as any
        );

        state.patchComponent({
            content: {
                en: 'content_new_value'
            },
            property: 'property_new_value'
        });

        expect(state.component).toEqual({
            content: {
                en: 'content_new_value'
            },
            property: 'property_new_value'
        });
    });
});
