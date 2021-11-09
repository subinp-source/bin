/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GenericEditorField, GenericEditorWidgetData, SettingsService } from 'smarteditcommons';
import { RichTextFieldComponent } from './RichTextFieldComponent';
import {
    GenericEditorSanitizationService,
    RichTextFieldLocalizationService,
    RichTextLoaderService
} from './services';

describe('RichTextField', () => {
    let seRichTextLoaderService: jasmine.SpyObj<RichTextLoaderService>;

    let genericEditorSanitizationService: jasmine.SpyObj<GenericEditorSanitizationService>;
    let seRichTextFieldLocalizationService: jasmine.SpyObj<RichTextFieldLocalizationService>;
    let richTextFieldComponent: RichTextFieldComponent;
    let settingsService: jasmine.SpyObj<SettingsService>;

    let editable: any;
    let editorInstance: any;
    let seRichTextConfiguration: any;

    let originalCKEDITOR: any;

    const widget: GenericEditorWidgetData<any> = {
        field: {
            qualifier: 'someQualifier'
        } as GenericEditorField,
        qualifier: 'en',
        isFieldDisabled: () => false,
        model: {},
        // id: null
    };
    beforeAll(() => {
        originalCKEDITOR = window.CKEDITOR;
    });

    afterAll(() => {
        window.CKEDITOR = originalCKEDITOR;
    });

    beforeEach(async (done) => {
        settingsService = jasmine.createSpyObj<SettingsService>('settingsService', ['get']);
        seRichTextLoaderService = jasmine.createSpyObj<RichTextLoaderService>(
            'seRichTextLoaderService',
            ['load']
        );
        seRichTextConfiguration = {};
        seRichTextFieldLocalizationService = jasmine.createSpyObj<RichTextFieldLocalizationService>(
            'seRichTextFieldLocalizationService',
            ['localizeCKEditor']
        );

        window.CKEDITOR = jasmine.createSpyObj('CKEDITOR', ['replace', 'on']);
        window.CKEDITOR.dtd = {
            $nonBodyContent: {},
            $block: {
                div: 1
            },
            $listItem: {},
            $tableContent: {}
        };

        editorInstance = jasmine.createSpyObj('editorInstance', [
            'destroy',
            'on',
            'getData',
            'fire',
            'editable'
        ]);

        editable = jasmine.createSpyObj('editable', ['attachListener']);
        editorInstance.editable.and.returnValue(editable);
        window.CKEDITOR.replace.and.returnValue(editorInstance);
        editorInstance.getData.and.returnValue('changed value');

        genericEditorSanitizationService = jasmine.createSpyObj<GenericEditorSanitizationService>(
            'genericEditorSanitizationService',
            ['isSanitized']
        );

        seRichTextLoaderService.load.and.returnValue(Promise.resolve());

        settingsService.get.and.returnValue(Promise.resolve('false'));

        richTextFieldComponent = new RichTextFieldComponent(
            widget,
            seRichTextLoaderService,
            seRichTextConfiguration,
            genericEditorSanitizationService,
            seRichTextFieldLocalizationService,
            settingsService
        );

        (richTextFieldComponent as any).textarea = document.createElement('textarea');

        await richTextFieldComponent.ngAfterViewInit();
        done();
    });

    describe('component init ', () => {
        it('should set allowed content to false if property "cms.components.allowUnsafeJavaScript" is false', async () => {
            settingsService.get.and.returnValue(Promise.resolve('false'));
            await richTextFieldComponent.ngAfterViewInit();

            expect(seRichTextConfiguration).toEqual({
                allowedContent: false
            });
        });

        it('should set allowed content to true if property "cms.components.allowUnsafeJavaScript" is true', async () => {
            settingsService.get.and.returnValue(Promise.resolve('true'));
            await richTextFieldComponent.ngAfterViewInit();

            expect(seRichTextConfiguration).toEqual({
                allowedContent: true
            });
        });
    });

    describe('controller', () => {
        describe('onMode', () => {
            it('should be attach editable listener if the mode is source', () => {
                editorInstance.mode = 'source';
                richTextFieldComponent.onMode();
                expect(editable.attachListener).toHaveBeenCalled();
            });

            it('should not be attach editable listener if the mode is not source', () => {
                editorInstance.mode = 'wysiwyg';
                richTextFieldComponent.onMode();
                expect(editable.attachListener).not.toHaveBeenCalled();
            });
        });

        describe('onInstanceReady', () => {
            it('should be called set rules method with attributes', () => {
                const setRules = jasmine.createSpy('setRules');

                const MOCK_EV = {
                    editor: {
                        dataProcessor: {
                            writer: {
                                setRules
                            }
                        }
                    }
                };

                richTextFieldComponent.onInstanceReady(MOCK_EV);
                expect(setRules).toHaveBeenCalledWith('br', {
                    indent: false,
                    breakBeforeOpen: false,
                    breakAfterOpen: false,
                    breakBeforeClose: false,
                    breakAfterClose: false
                });
                expect(setRules).toHaveBeenCalledWith('div', {
                    indent: false,
                    breakBeforeOpen: false,
                    breakAfterOpen: false,
                    breakBeforeClose: false,
                    breakAfterClose: false
                });
            });
        });
    });

    describe('on data change', () => {
        it('should call genericEditorSanitizationService', () => {
            richTextFieldComponent.widget.qualifier = 'en';
            richTextFieldComponent.widget.model = {
                en: '<div><script>alert(/"I am a snippet/");</script></div>'
            };
            richTextFieldComponent.widget.field = {} as any;
            richTextFieldComponent.reassignUserCheck();
            expect(genericEditorSanitizationService.isSanitized).toHaveBeenCalled();
        });

        it('reassignUserCheck WILL set requiresUserCheck as true on field with javascript snippet WHEN sanitized content does not match unsanitized content', () => {
            richTextFieldComponent.widget.qualifier = 'en';
            richTextFieldComponent.widget.model = {
                en: '<div><script>alert(/"I am a snippet/");</script></div>'
            };
            richTextFieldComponent.widget.field = {} as any;
            richTextFieldComponent.reassignUserCheck();
            expect(
                richTextFieldComponent.widget.field.requiresUserCheck[
                    richTextFieldComponent.widget.qualifier
                ]
            ).toBe(true);
        });

        it('reassignUserCheck WILL set requiresUserCheck as true on field WHEN sanitized content does not match unsanitized content', () => {
            richTextFieldComponent.widget.qualifier = 'en';

            richTextFieldComponent.widget.model = {
                en: '"http://"'
            };
            richTextFieldComponent.widget.field = {} as any;
            richTextFieldComponent.reassignUserCheck();
            expect(
                richTextFieldComponent.widget.field.requiresUserCheck[
                    richTextFieldComponent.widget.qualifier
                ]
            ).toBe(true);
        });

        it('reassignUserCheck WILL not set requiresUserCheck on field WHEN sanitized content matches unsanitized content.', () => {
            genericEditorSanitizationService.isSanitized.and.returnValue(true);

            richTextFieldComponent.widget.qualifier = 'en';
            richTextFieldComponent.widget.model = {
                en: '<p>Valid Html</p>'
            };
            richTextFieldComponent.widget.field = {} as any;
            richTextFieldComponent.reassignUserCheck();

            expect(
                richTextFieldComponent.widget.field.requiresUserCheck[
                    richTextFieldComponent.widget.qualifier
                ]
            ).toBe(false);
        });

        it('reassignUserCheck WILL not set requiresUserCheck on field WHEN there is no content', () => {
            richTextFieldComponent.widget.model = {};
            richTextFieldComponent.widget.field = {} as any;

            richTextFieldComponent.reassignUserCheck();
            expect(
                richTextFieldComponent.widget.field.requiresUserCheck[
                    richTextFieldComponent.widget.qualifier
                ]
            ).toBe(false);
        });

        it('reassignUserCheck WILL not set requiresUserCheck on field WHEN the model is not defined', () => {
            richTextFieldComponent.widget.field = {} as any;
            richTextFieldComponent.widget.model = undefined;

            richTextFieldComponent.reassignUserCheck();
            expect(
                richTextFieldComponent.widget.field.requiresUserCheck[
                    richTextFieldComponent.widget.qualifier
                ]
            ).toBe(false);
        });
    });
});
