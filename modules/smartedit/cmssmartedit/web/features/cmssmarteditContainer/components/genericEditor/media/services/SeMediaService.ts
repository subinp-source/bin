/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import { IRestService, RestServiceFactory, SeInjectable } from 'smarteditcommons';
import { ICMSMedia } from 'cmscommons';

export interface Media {
    file: File;
    code: string;
    description: string;
    altText: string;
}
/**
 * @ngdoc service
 * @name cmsSmarteditServicesModule.seMediaService
 * @description
 * This service provides functionality to upload images and to fetch images by code for a specific catalog-catalog version combination.
 */
@SeInjectable()
export class SeMediaService {
    private readonly mediaRestService: IRestService<ICMSMedia>;
    constructor(
        private MEDIA_RESOURCE_URI: string,
        private restServiceFactory: RestServiceFactory
    ) {
        this.mediaRestService = this.restServiceFactory.get(this.MEDIA_RESOURCE_URI);
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.seMediaService.uploadMedia
     * @methodOf cmsSmarteditServicesModule.seMediaService
     *
     * @description
     * Uploads the media to the catalog.
     *
     * @param {Object} media The media to be uploaded
     * @param {String} media.code A unique code identifier for the media
     * @param {String} media.description A description of the media
     * @param {String} media.altText An alternate text to be shown for the media
     * @param {File} media.file The {@link https://developer.mozilla.org/en/docs/Web/API/File File} object to be
     * uploaded.
     *
     * @returns {Promise<object>} If request is successful, it returns a promise that resolves with the media object. If the
     * request fails, it resolves with errors from the backend.
     */
    uploadMedia(media: Media): Promise<ICMSMedia> {
        const formData = new FormData();
        lodash.forEach(media, (value, key: string) => {
            formData.append(key, value);
        });

        return this.mediaRestService.save(formData as any, {
            headers: { enctype: 'multipart/form-data' }
        });
    }
}
