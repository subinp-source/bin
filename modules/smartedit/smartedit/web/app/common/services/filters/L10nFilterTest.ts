/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { LanguageService, TypedMap } from 'smarteditcommons';
import { L10nFilter } from './L10nFilter';
import { CrossFrameEventService } from '../crossFrame/CrossFrameEventService';

describe('L10nFilter', () => {
    let transform: ReturnType<typeof L10nFilter.transform>;
    let languageService: jasmine.SpyObj<LanguageService>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;

    const mockLocalizedMap: TypedMap<string> = {
        en: 'the content to edit',
        fr: 'le contenu a editer'
    };

    beforeEach(() => {
        languageService = jasmine.createSpyObj<LanguageService>('languageService', [
            'getResolveLocaleIsoCode'
        ]);
        languageService.getResolveLocaleIsoCode.and.returnValue(Promise.resolve('en'));

        crossFrameEventService = jasmine.createSpyObj<CrossFrameEventService>(
            'crossFrameEventService',
            ['subscribe']
        );

        transform = L10nFilter.transform(languageService, crossFrameEventService);
    });

    it('initializes', () => {
        expect(crossFrameEventService.subscribe).toHaveBeenCalled();
    });

    it('GIVEN the langugage has not been resolved WHEN I call transform with Localized Map THEN inititalFilter is called and it returns the same value', () => {
        const message = transform(mockLocalizedMap);
        expect(message).toEqual(mockLocalizedMap);
    });

    it('GIVEN the language has been resolved WHEN I call transform with Localized Map THEN it returns translation for current language', (done) => {
        setTimeout(() => {
            const message = transform(mockLocalizedMap);
            expect(message).toBe('the content to edit');
            done();
        });
    });

    it('GIVEN the language has been resolved WHEN I call transform with a string THEN it returns the same string', (done) => {
        setTimeout(() => {
            const message = transform('the content to edit');
            expect(message).toBe('the content to edit');
            done();
        });
    });

    it('GIVEN the language has been resolved WHEN I call transform with an empty object THEN it returns undefined', (done) => {
        setTimeout(() => {
            const message = transform({});
            expect(message).toBe(undefined);
            done();
        });
    });

    it('GIVEN the language has been resolved WHEN I call transform with Localized Map which doesnt contain text for resolved language THEN it defaults to the first entry', (done) => {
        setTimeout(() => {
            const message = transform({ fr: 'le contenu a editer', pl: 'tresc edytowac' });
            expect(message).toBe('le contenu a editer');
            done();
        });
    });
});
