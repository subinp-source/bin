/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { ExperienceService } from 'smartedit/services';
import { annotationService, GatewayProxied, IExperience, IPreviewData } from 'smarteditcommons';

describe('experienceService', () => {
    const MOCK_STOREFRONT_PREVIEW_URL = 'someMockPreviewStorefrontUrl';
    const MOCK_RESOURCE_PATH = 'https://somedomain/storefronturl';

    const $location = jasmine.createSpyObj('$location', ['absUrl']);
    const logService = jasmine.createSpyObj('logService', ['error']);
    const previewService = jasmine.createSpyObj('previewService', [
        'getResourcePathFromPreviewUrl',
        'updateUrlWithNewPreviewTicketId'
    ]);

    let experienceService: ExperienceService;

    beforeEach(() => {
        experienceService = new ExperienceService($location, logService, previewService);
    });

    it('checks GatewayProxied', () => {
        const decoratorObj = annotationService.getClassAnnotation(
            ExperienceService,
            GatewayProxied
        );
        expect(decoratorObj).toEqual([
            'loadExperience',
            'updateExperiencePageContext',
            'getCurrentExperience',
            'setCurrentExperience',
            'hasCatalogVersionChanged',
            'buildRefreshedPreviewUrl',
            'compareWithCurrentExperience'
        ]);
    });

    it('WHEN buildRefreshedPreviewUrl is called THEN it retrieves the current experience and returns the updated url with new preview ticket id', async (done) => {
        const experience: any = {
            siteDescriptor: {
                previewUrl: '/someURI/?someSite=site',
                uid: 'someSiteId'
            },
            catalogDescriptor: {
                catalogId: 'someCatalog',
                catalogVersion: 'someVersion'
            },
            languageDescriptor: {
                isocode: 'someLanguage'
            }
        } as IExperience;
        const url = 'http://server/';

        spyOn(experienceService, 'getCurrentExperience').and.returnValue(
            Promise.resolve(experience)
        );
        $location.absUrl.and.returnValue(url);
        previewService.getResourcePathFromPreviewUrl.and.returnValue(
            Promise.resolve(MOCK_RESOURCE_PATH)
        );
        previewService.updateUrlWithNewPreviewTicketId.and.returnValue(
            Promise.resolve(MOCK_STOREFRONT_PREVIEW_URL)
        );

        experience.catalogVersions = [
            {
                catalog: experience.catalogDescriptor.catalogId,
                catalogVersion: experience.catalogDescriptor.catalogVersion
            }
        ];
        experience.language = experience.languageDescriptor.isocode;
        experience.resourcePath = MOCK_RESOURCE_PATH;
        // Act
        await experienceService.buildRefreshedPreviewUrl();

        // Assert
        expect(previewService.getResourcePathFromPreviewUrl).toHaveBeenCalledWith(
            experience.siteDescriptor.previewUrl
        );
        expect(previewService.updateUrlWithNewPreviewTicketId).toHaveBeenCalledWith(url, {
            catalogVersions: [{ catalog: 'someCatalog', catalogVersion: 'someVersion' }],
            language: 'someLanguage',
            resourcePath: 'https://somedomain/storefronturl',
            siteId: 'someSiteId'
        } as IPreviewData);
        done();
    });
});
