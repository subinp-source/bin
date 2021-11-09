/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { L10nPipe } from './L10nPipe';
import { Subscription } from 'rxjs';
import { CrossFrameEventService, L10nService, LanguageService } from '../../services';

describe('L10n Pipe', () => {
    const mockLocalizedMap = {
        en: 'dummyText in english',
        fr: 'dummyText in french'
    };

    let languageService: jasmine.SpyObj<LanguageService>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;
    let l10nService: L10nService;

    let l10nPipe: L10nPipe;
    beforeEach(() => {
        languageService = jasmine.createSpyObj('languageService', ['getResolveLocaleIsoCode']);
        crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['subscribe']);
        l10nService = new L10nService(languageService, crossFrameEventService);

        l10nPipe = new L10nPipe(l10nService);
    });

    let transformSub: Subscription;
    afterEach(() => {
        if (transformSub) {
            transformSub.unsubscribe();
        }
    });

    it('GIVEN text THEN it should return the same text', async () => {
        await resolveLanguage('en');

        transformSub = l10nPipe.transform('dummyText in english').subscribe((text) => {
            expect(text).toBe('dummyText in english');
        });
    });

    it('GIVEN "localized map" THEN it should return translated text for the current language', async () => {
        await resolveLanguage('fr');

        transformSub = l10nPipe.transform(mockLocalizedMap).subscribe((text) => {
            expect(text).toBe('dummyText in french');
        });
    });

    it('GIVEN "localized map" without key for current language THEN it should return translation of the first key', async () => {
        await resolveLanguage('pl');

        transformSub = l10nPipe.transform(mockLocalizedMap).subscribe((text) => {
            expect(text).toBe('dummyText in english');
        });
    });

    it('GIVEN no string and no "localized map" THEN it should return undefined', async () => {
        await resolveLanguage('fr');

        transformSub = l10nPipe.transform(123 as any).subscribe((text) => {
            expect(text).toBe(undefined);
        });
    });

    function resolveLanguage(lang: string): Promise<void> {
        languageService.getResolveLocaleIsoCode.and.returnValue(Promise.resolve(lang));
        return l10nService.resolveLanguage();
    }
});
