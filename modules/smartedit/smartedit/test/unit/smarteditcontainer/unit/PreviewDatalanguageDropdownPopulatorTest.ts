/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { PreviewDatalanguageDropdownPopulator } from 'smarteditcontainer/services';
import { LanguageService } from 'smarteditcommons';

describe('PreviewDatalanguageDropdownPopulator', () => {
    let previewDatalanguageDropdownPopulator: PreviewDatalanguageDropdownPopulator;
    let languageService: jasmine.SpyObj<LanguageService>;

    const languageDescriptors = [
        {
            isocode: 'en',
            nativeName: 'English'
        },
        {
            isocode: 'hi',
            nativeName: 'Hindi'
        },
        {
            isocode: 'te',
            nativeName: 'Telugu'
        }
    ];

    beforeEach(() => {
        languageService = jasmine.createSpyObj('languageService', ['getLanguagesForSite']);

        previewDatalanguageDropdownPopulator = new PreviewDatalanguageDropdownPopulator(
            languageService
        );
    });

    it('GIVEN a correct siteId WHEN PreviewDatalanguageDropdownPopulator.populate is called THEN populate will return a list of associated languages', async () => {
        // GIVEN
        languageService.getLanguagesForSite.and.returnValue(Promise.resolve(languageDescriptors));

        // WHEN
        const languages = await previewDatalanguageDropdownPopulator.populate({
            field: {
                qualifier: 'somequalifier',
                dependsOn: 'catalog',
                cmsStructureType: undefined
            },
            model: {
                catalog: 'siteId1|myCatalogId1|myCatalogVersion1'
            },
            selection: undefined,
            search: undefined
        });

        // THEN
        expect(languageService.getLanguagesForSite).toHaveBeenCalledWith('siteId1');
        expect(languages).toEqual([
            {
                id: 'en',
                label: 'English'
            },
            {
                id: 'hi',
                label: 'Hindi'
            },
            {
                id: 'te',
                label: 'Telugu'
            }
        ]);
    });

    it('GIVEN a correct siteId WHEN PreviewDatalanguageDropdownPopulator.populate is called with a search string THEN populate will return a list of associated languages filtered based on the search string', async () => {
        // GIVEN
        languageService.getLanguagesForSite.and.returnValue(Promise.resolve(languageDescriptors));

        // WHEN
        const languages = await previewDatalanguageDropdownPopulator.populate({
            field: {
                qualifier: 'somequalifier',
                dependsOn: 'catalog',
                cmsStructureType: null
            },
            model: {
                catalog: 'siteId1|myCatalogId1|myCatalogVersion1'
            },
            selection: null,
            search: 'te'
        });

        // THEN
        expect(languageService.getLanguagesForSite).toHaveBeenCalledWith('siteId1');
        expect(languages).toEqual([
            {
                id: 'te',
                label: 'Telugu'
            }
        ]);
    });

    it('GIVEN a wrong siteId WHEN PreviewDatalanguageDropdownPopulator.populate is called THEN populate will return a rejected promise', async (done) => {
        // GIVEN
        languageService.getLanguagesForSite.and.returnValue(Promise.reject());

        // WHEN
        await previewDatalanguageDropdownPopulator
            .populate({
                field: {
                    qualifier: 'somequalifier',
                    dependsOn: 'catalog',
                    cmsStructureType: null
                },
                model: {
                    catalog: 'siteIdX|myCatalogId1|myCatalogVersion1'
                },
                selection: null,
                search: undefined
            })
            .catch((e) => {
                // THEN
                expect(languageService.getLanguagesForSite).toHaveBeenCalledWith('siteIdX');
                done();
            });
    });
});
