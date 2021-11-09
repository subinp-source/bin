/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { LanguageService } from 'smarteditcommons';
import { RichTextFieldLocalizationService } from './RichTextFieldLocalizationService';

describe('richTextFieldLocalizationService', () => {
    let richTextFieldLocalizationService: RichTextFieldLocalizationService;
    let languageService: jasmine.SpyObj<LanguageService>;

    let resolvedLocaleToCKEDITORLocaleMap: any;

    let originalCKEDITOR: any;

    beforeAll(() => {
        originalCKEDITOR = window.CKEDITOR;
    });

    afterAll(() => {
        window.CKEDITOR = originalCKEDITOR;
    });

    beforeEach(() => {
        window.CKEDITOR = {
            config: {}
        };

        languageService = jasmine.createSpyObj<LanguageService>('languageService', [
            'getResolveLocale'
        ]);
        resolvedLocaleToCKEDITORLocaleMap = {
            en: 'xx'
        };

        richTextFieldLocalizationService = new RichTextFieldLocalizationService(
            languageService,
            resolvedLocaleToCKEDITORLocaleMap
        );
    });

    describe('localizeCKEditor', () => {
        it("should set global variable CKEDITOR's language to the current locale's equivalent in CKEDITOR when the conversion exists", async (done) => {
            const existingLocale = 'en';
            expect(resolvedLocaleToCKEDITORLocaleMap[existingLocale]).not.toBeUndefined();

            languageService.getResolveLocale.and.returnValue(Promise.resolve(existingLocale));

            await richTextFieldLocalizationService.localizeCKEditor();

            expect(languageService.getResolveLocale).toHaveBeenCalled();
            expect(CKEDITOR.config.language).toEqual('xx');

            done();
        });

        it("should set global variable CKEDITOR's language to the current locale when the conversion does not exist", async (done) => {
            const nonexistingLocale = 'zz';
            expect(resolvedLocaleToCKEDITORLocaleMap[nonexistingLocale]).toBeUndefined();

            languageService.getResolveLocale.and.returnValue(Promise.resolve(nonexistingLocale));

            await richTextFieldLocalizationService.localizeCKEditor();

            expect(languageService.getResolveLocale).toHaveBeenCalled();
            expect(CKEDITOR.config.language).toEqual(nonexistingLocale);

            done();
        });
    });
});
