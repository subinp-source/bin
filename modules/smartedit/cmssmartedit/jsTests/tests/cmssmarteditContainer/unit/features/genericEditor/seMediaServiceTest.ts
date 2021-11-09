/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { IRestOptions, IRestService, RestServiceFactory } from 'smarteditcommons';
import { SeMediaService } from 'cmssmarteditcontainer/components/genericEditor/media/services/SeMediaService';
import { ICMSMedia } from 'cmscommons';

describe('seMediaService', () => {
    let seMediaService: SeMediaService;
    let restServiceFactory: jasmine.SpyObj<RestServiceFactory>;
    let restService: jasmine.SpyObj<IRestService<ICMSMedia>>;

    const MEDIA_RESOURCE_URI = 'MEDIA_RESOURCE_URI';
    const file = jasmine.createSpyObj<File>('file', ['name']);

    const IMAGE_MOCK = {
        file,
        code: 'somecode',
        description: 'somedescription',
        altText: 'somealttext'
    };
    const response = {
        uuid: 'someuuid'
    } as ICMSMedia;

    beforeEach(() => {
        restService = jasmine.createSpyObj('restService', ['save']);
        restService.save.and.callFake((body: any, options: IRestOptions) => {
            if (options.headers.enctype === 'multipart/form-data') {
                return Promise.resolve(response);
            }

            throw new Error(`unexpected parameters passed to IRestService.post`);
        });
        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.and.returnValue(restService);

        seMediaService = new SeMediaService(MEDIA_RESOURCE_URI, restServiceFactory);
    });

    it('should post with proper headers and formData', async () => {
        const result = await seMediaService.uploadMedia(IMAGE_MOCK);
        expect(result).toBe(response);

        const formData: FormData = restService.save.calls.mostRecent().args[0];

        expect(formData.has('file')).toBe(true);
        expect(formData.get('code')).toBe('somecode');
        expect(formData.get('description')).toBe('somedescription');
        expect(formData.get('altText')).toBe('somealttext');
    });
});
